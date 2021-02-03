package com.sap.oss.phosphor.fosstars.data.github.experimental.graphql;

import com.sap.oss.phosphor.fosstars.data.github.experimental.graphql.data.Advisory;
import com.sap.oss.phosphor.fosstars.data.github.experimental.graphql.data.GitHubAdvisoryEntry;
import com.sap.oss.phosphor.fosstars.data.github.experimental.graphql.data.Identifier;
import com.sap.oss.phosphor.fosstars.data.github.experimental.graphql.data.Node;
import com.sap.oss.phosphor.fosstars.model.value.PackageManager;
import com.sap.oss.phosphor.fosstars.util.Json;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

/**
 * This class offers an interface to GitHub security advisories using GrahhQL APIs exposed by
 * GitHub.
 * 
 * @see <a href=
 *      "https://help.github.com/en/github/managing-security-vulnerabilities/about-github-security-advisories">GitHub
 *      Security Advisory</a>
 */
public class GitHubAdvisories {

  /**
   * An template used to query for Security Advisories. This is the template to use for the first
   * run.
   */
  private static final String GRAPHQL_FIRST_RUN_TEMPLATE = "first_run_template";

  /**
   * An template used to query for Security Advisories. This is the template to be used for page
   * traversing.
   */
  private static final String GRAPHQL_NEXT_PAGE_RUN_TEMPLATE = "next_page_run_template";

  /**
   * First n advisories per page.
   */
  private static final int FIRST_N_ADVISORIES = 100;

  /**
   * The token to access GitHub API.
   */
  private final String gitHubToken;

  /**
   * Initializes a data provider.
   *
   * @param gitHubToken GitHub Token.
   */
  public GitHubAdvisories(String gitHubToken) {
    this.gitHubToken = Objects.requireNonNull(gitHubToken, "The GitHub token cannot be null!");
  }
  
  /**
   * Get the all the advisories for the given artifact and the package manager which are not present
   * in NVD database. This is done by checking if the advisory has a CVE associated to it.
   * 
   * @param ecosystem The GitHub project package management used.
   * @param artifact The artifact identifier for the GitHub project (E.g MAVEN for Java, NPM for
   *        JavaScript packages).
   * @return List of type {@link Node}. Each {@link Node} is an {@link Advisory}.
   * @throws IOException if something goes wrong.
   */
  public List<Node> advisoriesFor(PackageManager ecosystem, String artifact) throws IOException {
    List<Node> advisories = new ArrayList<>();
    for (Node node : download(ecosystem.name(), artifact)) {
      // TODO: This check needs to be removed to allow it to gather all possible advisories from the
      // Security Advisory Database https://github.com/SAP/fosstars-rating-core/issues/143
      if (!hasCve(node.getAdvisory())) {
        advisories.add(node);
      }
    }

    return Collections.unmodifiableList(advisories);
  }

  /**
   * Downloads the advisories related to the input parameters from GitHub Security Advisory
   * Database.
   * 
   * @param ecosystem The GitHub project package management used.
   * @param artifact The artifact identifier for the GitHub project (E.g MAVEN for Java, NPM for
   *        JavaScript packages).
   * @return List of type {@link Node}. Each {@link Node} is an {@link Advisory}.
   * @throws IOException if something goes wrong.
   */
  private List<Node> download(String ecosystem, String artifact) throws IOException {
    List<Node> advisories = new ArrayList<>();

    String jsonEntity = String.format("{\"query\" : \"%s\"}",
        String.format(load(GRAPHQL_FIRST_RUN_TEMPLATE), FIRST_N_ADVISORIES, ecosystem, artifact));

    boolean nextPage = true;

    do {
      GitHubAdvisoryEntry entry = httpRequest(gitHubToken, jsonEntity);

      advisories.addAll(nodes(entry));

      nextPage = hasNextPage(entry);
      jsonEntity = String.format("{\"query\" : \"%s\"}", String
          .format(load(GRAPHQL_NEXT_PAGE_RUN_TEMPLATE), FIRST_N_ADVISORIES, endCursor(entry),
              ecosystem, artifact));
    } while (nextPage);
    
    return advisories;
  }

  /**
   * Does a REST API call to public URL <a link="https://api.github.com/graphql">GitHub GraphQL
   * API</a> to list all the security advisories from GitHub Security Advisory.
   * 
   * @param gitHubToken The GitHub Token.
   * @param jsonEntity is the query which needs to used to call GraphQL API.
   * @return The {@link GitHubAdvisoryEntry} object.
   * @throws IOException #{@link HttpClient} may throw an exception during GraphQL call.
   */
  private GitHubAdvisoryEntry httpRequest(String gitHubToken, String jsonEntity)
      throws IOException {
    try (CloseableHttpClient client = httpClient()) {
      HttpPost httpPostRequest = buildRequest(gitHubToken, jsonEntity);
      try (CloseableHttpResponse response = client.execute(httpPostRequest)) {
        return Json.mapper().readValue(
            response.getEntity().getContent(), GitHubAdvisoryEntry.class);
      }
    }
  }

  /**
   * Builds a {@link HttpPost} request.
   * 
   * @param gitHubToken is the GitHub Token.
   * @param jsonEntity is the query which needs to used to call GraphQL API.
   * @return The {@link HttpPost} object.
   * @throws UnsupportedEncodingException if something is wrong.
   */
  private HttpPost buildRequest(String gitHubToken, String jsonEntity)
      throws UnsupportedEncodingException {
    String url = "https://api.github.com/graphql";

    HttpPost httpPostRequest = new HttpPost(url);
    if (StringUtils.isNotEmpty(gitHubToken)) {
      httpPostRequest.addHeader(HttpHeaders.AUTHORIZATION, String.format("bearer %s", gitHubToken));
    }
    httpPostRequest.addHeader(HttpHeaders.ACCEPT, ContentType.APPLICATION_JSON.getMimeType());
    httpPostRequest.setEntity(new StringEntity(jsonEntity));
    return httpPostRequest;
  }

  /**
   * Creates an HTTP client.
   *
   * @return An HTTP client.
   */
  CloseableHttpClient httpClient() {
    return HttpClients.createDefault();
  }

  /**
   * Loads the GraphQL template, used as query to call GraphQL API.
   * 
   * @param file name of the template.
   * @return String content of the template.
   * @throws IOException if something goes wrong.
   */
  private String load(String file) throws IOException {
    try (final InputStream is = getClass().getResourceAsStream(file)) {
      return IOUtils.toString(is, "UTF-8").replaceAll("(\\r|\\n)", StringUtils.EMPTY);
    }
  }

  /**
   * Get the last index of the first n elements.
   * 
   * @param entry object of the type of {@link GitHubAdvisoryEntry}.
   * @return The last index.
   */
  private String endCursor(GitHubAdvisoryEntry entry) {
    return entry.getData().getSecurityVulnerabilities().getPageInfo().getEndCursor();
  }

  /**
   * Get the next page flag.
   * 
   * @param entry object of the type of {@link GitHubAdvisoryEntry}.
   * @return true if there is a next page. Otherwise false.
   */
  private boolean hasNextPage(GitHubAdvisoryEntry entry) {
    return entry.getData().getSecurityVulnerabilities().getPageInfo().getHasNextPage();
  }

  /**
   * Extract all the advisories from the data {@link GitHubAdvisoryEntry}.
   * 
   * @param entry object of the type of {@link GitHubAdvisoryEntry}.
   * @return List o type {@link Node}.
   */
  private List<Node> nodes(GitHubAdvisoryEntry entry) {
    return entry.getData().getSecurityVulnerabilities().getNodes();
  }

  /**
   * Check if the advisory has a CVE associated to it.
   * 
   * @param advisory of object type {@link Advisory}.
   * @return true if the {@link Advisory} has a CVE. Otherwise false.
   */
  private boolean hasCve(Advisory advisory) {
    for (Identifier identifier : advisory.getIdentifiers()) {
      if (identifier.getType().equals("CVE")) {
        return true;
      }
    }
    return false;
  }

  /**
   * This is for testing purpose only.
   *
   * @param args Command line arguments.
   * @throws IOException if something goes wrong.
   */
  public static void main(String... args) throws IOException {
    String token = System.getenv("TOKEN");
    GitHubAdvisories gitHubAdvisories = new GitHubAdvisories(token);
    List<Node> advisories =
        gitHubAdvisories.advisoriesFor(PackageManager.MAVEN,
            "com.fasterxml.jackson.core:jackson-databind");
    System.out.println("Total count :" + advisories.size());
  }
}
