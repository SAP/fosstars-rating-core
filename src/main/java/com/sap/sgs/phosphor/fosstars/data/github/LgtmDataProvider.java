package com.sap.sgs.phosphor.fosstars.data.github;

import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.USES_LGTM_CHECKS;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.WORST_LGTM_GRADE;
import static com.sap.sgs.phosphor.fosstars.model.other.Utils.setOf;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sap.sgs.phosphor.fosstars.model.Feature;
import com.sap.sgs.phosphor.fosstars.model.Value;
import com.sap.sgs.phosphor.fosstars.model.ValueSet;
import com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures;
import com.sap.sgs.phosphor.fosstars.model.value.LgtmGrade;
import com.sap.sgs.phosphor.fosstars.model.value.UnknownValue;
import com.sap.sgs.phosphor.fosstars.model.value.ValueHashSet;
import com.sap.sgs.phosphor.fosstars.tool.github.GitHubProject;
import java.io.IOException;
import java.time.Duration;
import java.util.Set;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.kohsuke.github.GHCheckRun;
import org.kohsuke.github.GHCommit;

/**
 * The data provider gathers info about how a project uses static analysis with LGTM.
 * In particular, it tires to fill out the following features:
 * <ul>
 *   <li>{@link OssFeatures#USES_LGTM_CHECKS}</li>
 *   <li>{@link OssFeatures#WORST_LGTM_GRADE}</li>
 * </ul>
 */
public class LgtmDataProvider extends GitHubCachingDataProvider {

  /**
   * Three months.
   */
  private static final Duration THREE_MONTHS = Duration.ofDays(90);

  /**
   * For parsing JSON.
   */
  private static final ObjectMapper MAPPER = new ObjectMapper();

  /**
   * Initializes a data provider.
   *
   * @param fetcher An interface to GitHub.
   */
  public LgtmDataProvider(GitHubDataFetcher fetcher) {
    super(fetcher);
  }

  @Override
  protected Set<Feature> supportedFeatures() {
    return setOf(USES_LGTM_CHECKS, WORST_LGTM_GRADE);
  }

  @Override
  protected ValueSet fetchValuesFor(GitHubProject project) throws IOException {
    return ValueHashSet.from(usesLgtmChecks(project), worstLgtmGrade(project));
  }

  /**
   * Loads info about the project from LGTM.
   *
   * @return The info about the project.
   */
  private JsonNode lgtmProjectInfo(GitHubProject project) throws IOException {
    try (CloseableHttpClient client = httpClient()) {
      String url = String.format("https://lgtm.com/api/v1.0/projects/g/%s", project.path());
      HttpGet httpGetRequest = new HttpGet(url);
      httpGetRequest.addHeader(HttpHeaders.ACCEPT, ContentType.APPLICATION_JSON.getMimeType());
      try (CloseableHttpResponse httpResponse = client.execute(httpGetRequest)) {
        return MAPPER.readTree(httpResponse.getEntity().getContent());
      }
    }
  }

  /**
   * Returns an HTTP client.
   */
  CloseableHttpClient httpClient() {
    return HttpClients.createDefault();
  }

  /**
   * Parses a JSON response from the LGTM API
   * and tries to figure out if the project has been analysed.
   *
   * @return A value of the {@link OssFeatures#USES_LGTM_CHECKS} feature.
   */
  private Value<Boolean> usesLgtmChecks(GitHubProject project) throws IOException {
    for (GHCommit commit : fetcher.githubCommitsFor(project, THREE_MONTHS)) {
      if (hasLgtmChecks(commit)) {
        return USES_LGTM_CHECKS.value(true);
      }
    }

    return USES_LGTM_CHECKS.value(false);
  }

  /**
   * Parses a JSON response from the LGTM API
   * and tries to figure out the worst grade for a project.
   *
   * @param project The project.
   * @return A value of the {@link OssFeatures#WORST_LGTM_GRADE} feature.
   */
  private Value<LgtmGrade> worstLgtmGrade(GitHubProject project) throws IOException {
    JsonNode json = lgtmProjectInfo(project);
    LgtmGrade worstGrade = null;
    if (json.has("languages") && json.get("languages").isArray()) {
      for (JsonNode item : json.get("languages")) {
        if (!item.has("grade")) {
          continue;
        }

        LgtmGrade grade = LgtmGrade.parse(item.get("grade").asText());
        if (worstGrade == null || grade.compareTo(worstGrade) > 0) {
          worstGrade = grade;
        }
      }
    }

    if (worstGrade != null) {
      return WORST_LGTM_GRADE.value(worstGrade);
    }

    return UnknownValue.of(WORST_LGTM_GRADE);
  }

  /**
   * Checks if a commit has LGTM checks.
   *
   * @param commit The commit to be checked.
   * @return True if the commit has LGTM checks, false otherwise.
   * @throws IOException If something went wrong.
   */
  private static boolean hasLgtmChecks(GHCommit commit) throws IOException {
    for (GHCheckRun checkRun : commit.getCheckRuns()) {
      if (checkRun.getName().startsWith("LGTM analysis")) {
        return true;
      }
    }

    return false;
  }
}
