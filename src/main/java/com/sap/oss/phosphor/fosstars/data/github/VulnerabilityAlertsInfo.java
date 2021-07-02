package com.sap.oss.phosphor.fosstars.data.github;

import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.ENABLED_VULNERABILITY_ALERTS_ON_GITHUB;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.HAS_UNRESOLVED_VULNERABILITY_ALERTS;
import static com.sap.oss.phosphor.fosstars.model.other.Utils.setOf;

import com.fasterxml.jackson.databind.JsonNode;
import com.sap.oss.phosphor.fosstars.model.Feature;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.ValueSet;
import com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures;
import com.sap.oss.phosphor.fosstars.model.subject.oss.GitHubProject;
import com.sap.oss.phosphor.fosstars.model.value.ValueHashSet;
import com.sap.oss.phosphor.fosstars.util.Json;
import java.io.IOException;
import java.util.Set;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;

/**
 * This data provider gather info about vulnerability alerts in a project.
 * The provider fills out the following features:
 * <ul>
 *   <li>{@link OssFeatures#ENABLED_VULNERABILITY_ALERTS_ON_GITHUB}</li>
 *   <li>{@link OssFeatures#HAS_UNRESOLVED_VULNERABILITY_ALERTS}</li>
 * </ul>
 */
public class VulnerabilityAlertsInfo extends GitHubCachingDataProvider {

  /**
   * A template for GraphQL query for getting a number of unresolved vulnerability alerts
   * in a project.
   */
  private static final String VULNERABILITY_ALERT_TOTAL_COUNT_QUERY_TEMPLATE
      = "query { "
      + "repository (owner: \\\"%s\\\", name: \\\"%s\\\") { vulnerabilityAlerts { totalCount } } }";

  /**
   * Initializes a data provider.
   *
   * @param fetcher An interface to GitHub.
   */
  public VulnerabilityAlertsInfo(GitHubDataFetcher fetcher) {
    super(fetcher);
  }

  @Override
  public Set<Feature<?>> supportedFeatures() {
    return setOf(ENABLED_VULNERABILITY_ALERTS_ON_GITHUB, HAS_UNRESOLVED_VULNERABILITY_ALERTS);
  }

  @Override
  protected ValueSet fetchValuesFor(GitHubProject project) {
    logger.info("Gathering info about vulnerability alerts ...");
    return ValueHashSet.from(
        enabledVulnerabilityAlerts(project),
        hasUnresolvedVulnerabilityAlerts(project));
  }

  /**
   * Check if vulnerability alerts are enabled for a project.
   *
   * @param project The project.
   * @return A value of {@link OssFeatures#ENABLED_VULNERABILITY_ALERTS_ON_GITHUB}.
   * @see <a href="https://docs.github.com/en/rest/reference/repos#check-if-vulnerability-alerts-are-enabled-for-a-repository">Check if vulnerability alerts are enabled for a repository</a>
   */
  private Value<Boolean> enabledVulnerabilityAlerts(GitHubProject project) {
    try (CloseableHttpClient client = httpClient()) {
      String url = String.format("https://api.github.com/repos/%s/%s/vulnerability-alerts",
          project.organization().name(), project.name());
      HttpGet request = new HttpGet(url);
      request.addHeader(HttpHeaders.ACCEPT, "application/vnd.github.dorian-preview+json");
      request.addHeader(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", fetcher.token()));
      try (CloseableHttpResponse response = client.execute(request)) {
        return ENABLED_VULNERABILITY_ALERTS_ON_GITHUB
            .value(response.getStatusLine().getStatusCode() == 204)
            .explainIf(false, "Vulnerability alerts are not enabled in the project");
      }
    } catch (IOException e) {
      logger.warn("Oops! Could not check whether vulnerability alerts are enabled or not", e);
    }

    return ENABLED_VULNERABILITY_ALERTS_ON_GITHUB.unknown()
        .explain("Could not check whether or not vulnerability alerts are enabled in the project");
  }

  /**
   * Check whether a project has unresolved vulnerability alerts or not.
   *
   * @param project The project.
   * @return A value of {@link OssFeatures#HAS_UNRESOLVED_VULNERABILITY_ALERTS}.
   */
  private Value<Boolean> hasUnresolvedVulnerabilityAlerts(GitHubProject project) {
    try (CloseableHttpClient client = httpClient()) {
      HttpPost request = new HttpPost("https://api.github.com/graphql");
      request.addHeader(HttpHeaders.ACCEPT, ContentType.APPLICATION_JSON.getMimeType());
      request.addHeader(HttpHeaders.AUTHORIZATION, String.format("bearer %s", fetcher.token()));
      String query = String.format(VULNERABILITY_ALERT_TOTAL_COUNT_QUERY_TEMPLATE,
          project.organization().name(), project.name());
      String body = String.format("{ \"query\": \"%s\" }", query);
      request.setEntity(new StringEntity(body));
      try (CloseableHttpResponse response = client.execute(request)) {
        if (response.getStatusLine().getStatusCode() != 200) {
          return HAS_UNRESOLVED_VULNERABILITY_ALERTS.unknown()
              .explain("Could not retrieve vulnerability alerts");
        }

        JsonNode root = Json.mapper().readTree(response.getEntity().getContent());
        JsonNode node = root.at("/data/repository/vulnerabilityAlerts/totalCount");
        if (!node.canConvertToInt()) {
          return HAS_UNRESOLVED_VULNERABILITY_ALERTS.unknown()
              .explain("Could not retrieve vulnerability alerts");
        }

        return HAS_UNRESOLVED_VULNERABILITY_ALERTS.value(node.asInt() > 0)
            .explainIf(true, "The project has unresolved vulnerability alerts");
      }
    } catch (IOException e) {
      logger.warn("Oops! Could not check whether the project has vulnerability alerts or not", e);
    }

    return HAS_UNRESOLVED_VULNERABILITY_ALERTS.unknown()
        .explain("Could not retrieve vulnerability alerts");
  }

  /**
   * Creates an HTTP client.
   *
   * @return A new HTTP client.
   */
  CloseableHttpClient httpClient() {
    return HttpClients.createDefault();
  }

  /**
   * Command-line interface for testing.
   *
   * @param args Command-line options.
   * @throws Exception If something went wrong.
   */
  public static void main(String... args) throws Exception {
    String token = args[0];
    String url = args[1];
    GitHub github = new GitHubBuilder().withOAuthToken(token).build();
    GitHubDataFetcher fetcher = new GitHubDataFetcher(github, token);
    VulnerabilityAlertsInfo provider = new VulnerabilityAlertsInfo(fetcher);
    GitHubProject project = GitHubProject.parse(url);
    ValueSet values = provider.fetchValuesFor(project);
    System.out.println(values.of(ENABLED_VULNERABILITY_ALERTS_ON_GITHUB));
    System.out.println(values.of(HAS_UNRESOLVED_VULNERABILITY_ALERTS));
  }

}
