package com.sap.sgs.phosphor.fosstars.data.github;

import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.USES_VERIFIED_SIGNED_COMMITS;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sap.sgs.phosphor.fosstars.model.Value;
import com.sap.sgs.phosphor.fosstars.model.ValueSet;
import com.sap.sgs.phosphor.fosstars.model.value.BooleanValue;
import com.sap.sgs.phosphor.fosstars.model.value.UnknownValue;
import com.sap.sgs.phosphor.fosstars.tool.github.GitHubProject;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Optional;
import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.kohsuke.github.GitHub;

/**
 * This data provider checks if a project uses verified signed commits. The check would be based on
 * a density ratio compared against a percentage threshold
 * ({@link #SIGNED_COMMIT_PERCENTAGE_THRESHOLD}).
 */
public class UsesSignedCommits extends AbstractGitHubDataProvider {

  /**
   * For parsing JSON.
   */
  private static final ObjectMapper MAPPER = new ObjectMapper();

  /**
   * 1 year in millis.
   */
  private static final long DELTA = 365 * 24 * 60 * 60 * 1000L;

  /**
   * The threshold in % of number of verified signed commits against total number of commits in a
   * year.
   */
  private static final double SIGNED_COMMIT_PERCENTAGE_THRESHOLD = 90;

  /**
   * Maximum number of items to be populated per page during the call to GitHub API.
   */
  private static final int MAX_ITEMS_PER_PAGE = 100;

  /**
   * First page number during the call to GitHub API.
   */
  private static final int FIRST_PAGE = 1;

  /**
   * A GitHub token provided by user to access the GitHub API.
   */
  private final String githubToken;

  /**
   * The date format to be used during GitHub API call.
   */
  private static final SimpleDateFormat DATE_FORMAT =
      new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.US);

  /**
   * Initializes a data provider.
   *
   * @param github An interface to the GitHub API.
   * @param githubToken provided by user to access GitHub API.
   */
  public UsesSignedCommits(GitHub github, String githubToken) {
    super(github);
    this.githubToken = githubToken;
  }

  @Override
  protected UsesSignedCommits doUpdate(GitHubProject project, ValueSet values) {
    logger.info("Figuring out if the project uses verified signed commits ...");

    Optional<Value> something = cache.get(project, USES_VERIFIED_SIGNED_COMMITS);
    if (something.isPresent()) {
      values.update(something.get());
      return this;
    }

    Value<Boolean> signedCommits = usesSignedCommits(project);
    values.update(signedCommits);
    cache.put(project, signedCommits, tomorrow());

    return this;
  }

  /**
   * It gathers the list of commits within one year from the current date. Then it calculates
   * density ratio. The density ratio is the percentage calculation of number of verified signed
   * commits against the total number of commits in the year. If the density ratio in % crosses a
   * certain percentage threshold ({@link #SIGNED_COMMIT_PERCENTAGE_THRESHOLD}), then it says that
   * the project uses verified signed commits. Otherwise, it says that the project does not use
   * verified signed commits.
   *
   * @param project The project.
   * @return {@link BooleanValue} value of the feature.
   */
  private Value<Boolean> usesSignedCommits(GitHubProject project) {
    try {
      return new BooleanValue(USES_VERIFIED_SIGNED_COMMITS, askGithub(project));
    } catch (IOException e) {
      logger.warn("Couldn't fetch data from api.github.com!", e);
      return UnknownValue.of(USES_VERIFIED_SIGNED_COMMITS);
    }
  }

  /**
   * Check if a project uses signed verified commits.
   *
   * @param project The project.
   * @return boolean True if the project uses verified signed commits. Otherwise, False
   * @throws IOException #{@link HttpClient} may throw an exception during REST call.
   * @see: GitHub Issue: <a link="https://github.com/github-api/github-api/issues/737">Get commit or
   *       tag signature verified flag</a> and the associated
   *       <a link= "https://github.com/github-api/github-api/pull/738">Pull Request</a>.
   */  
  private boolean askGithub(GitHubProject project) throws IOException {
    // TODO: The data gathering process here from GitHub API will have to be merged with other
    // related data providers. More information can be found in
    // https://github.com/SAP/fosstars-rating-core/issues/69
    
    int signedCounter = 0;
    int counter = 0;
    String commitDate = DATE_FORMAT.format(new Date(System.currentTimeMillis() - DELTA));
    int pageNumber = FIRST_PAGE;
    Page page;
    
    do {
      page = nextPage(project.path(), commitDate, pageNumber++);
      
      for (JsonNode commitNode : page.commitNodes) {
        counter++;
        JsonNode verifiedNode = commitNode.findPath("verified");
        if (verifiedNode != null && verifiedNode.booleanValue()) {
          signedCounter++;
        }
      }
    } while (page.hasNextPage);
    
    return counter > 0 && signedCounter > 0
        && (signedCounter * 100 / counter) >= SIGNED_COMMIT_PERCENTAGE_THRESHOLD;
  }

  /**
   * Does a REST API call to public URL <a link="https://api.github.com">GitHub API</a> to list all
   * the commits after a specific date.
   *
   * @param path The path to the project.
   * @param commitDate Only commits after this date will be returned. 
   *                   This is a timestamp in ISO 8601, format: YYYY-MM-DDTHH:MM:SSZ
   * @param pageNumber Index of the page.
   * @return The {@link Page} object.
   * @throws IOException #{@link HttpClient} may throw an exception during REST call.
   */
  private Page nextPage(String path, String commitDate, int pageNumber) throws IOException {
    try (CloseableHttpClient client = httpClient()) {
      HttpGet httpGetRequest = buildRequest(path, commitDate, pageNumber);
      try (CloseableHttpResponse response = client.execute(httpGetRequest)) {
        return new Page(hasNextPage(response), MAPPER.readTree(response.getEntity().getContent()));
      }
    }
  }

  /**
   * Builds a {@link HttpGet} request.
   *
   * @param path The path to the project.
   * @param commitDate This is a timestamp in ISO 8601, format: YYYY-MM-DDTHH:MM:SSZ
   * @param pageNumber Index of the page.
   * @return The {@link HttpGet} object.
   */
  private HttpGet buildRequest(String path, String commitDate, int pageNumber) {
    String url =
        String.format("https://api.github.com/repos/%s/commits?since=%s&page=%s&per_page=%s",
            path, commitDate, pageNumber, MAX_ITEMS_PER_PAGE);

    HttpGet httpGetRequest = new HttpGet(url);
    httpGetRequest.addHeader(HttpHeaders.ACCEPT, ContentType.APPLICATION_JSON.getMimeType());

    if (githubToken != null && !githubToken.isEmpty()) {
      httpGetRequest.addHeader(HttpHeaders.AUTHORIZATION, String.format("token %s", githubToken));
    }

    return httpGetRequest;
  }

  /**
   * Returns an HTTP client.
   */
  CloseableHttpClient httpClient() {
    return HttpClients.createDefault();
  }

  /**
   * Check if the next page is available.
   * 
   * @param response {@link CloseableHttpResponse} from the GET Request.
   * @return True if the next page is available, false otherwise.
   */
  private boolean hasNextPage(CloseableHttpResponse response) {
    Header header = response.getFirstHeader("link");
    return header != null && header.toString().contains("rel=\"next\"");
  }

  /**
   * This class is used to store the commit nodes and hasNextPage indication after each call to
   * GitHub API. After the call the variables hasNextPage and commitNodes gets populated.
   */
  private static class Page {

    private final boolean hasNextPage;
    private final JsonNode commitNodes;

    private Page(boolean hasNextPage, JsonNode commitNodes) {
      this.hasNextPage = hasNextPage;
      this.commitNodes = commitNodes;
    }
  }
}