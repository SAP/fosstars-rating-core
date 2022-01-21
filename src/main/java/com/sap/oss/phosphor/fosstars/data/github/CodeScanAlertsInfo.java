package com.sap.oss.phosphor.fosstars.data.github;

import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.RUNS_CODEQL_SCANS;
import static com.sap.oss.phosphor.fosstars.model.other.Utils.setOf;

import com.sap.oss.phosphor.fosstars.model.Feature;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.ValueSet;
import com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures;
import com.sap.oss.phosphor.fosstars.model.subject.oss.GitHubProject;
import com.sap.oss.phosphor.fosstars.model.value.ValueHashSet;
import java.io.IOException;
import java.util.Set;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

/**
 * This data provider gather info about vulnerability alerts in a project. The
 * provider fills out the following features:
 * <ul>
 * <li>{@link OssFeatures#RUNS_CODEQL_SCANS}</li>
 * </ul>
 */

public class CodeScanAlertsInfo extends GitHubCachingDataProvider {

  /**
  * Initializes a data provider.
  *
  * @param fetcher An interface to GitHub.
  */
  public CodeScanAlertsInfo(GitHubDataFetcher fetcher) {
    super(fetcher);
  }

  @Override
  public Set<Feature<?>> supportedFeatures() {
    return setOf(RUNS_CODEQL_SCANS);
  }

  @Override
  protected ValueSet fetchValuesFor(GitHubProject project) {
    logger.info("Gathering info about Code-Scan alerts ...");
    return ValueHashSet.from(enabledCodeScanAlerts(project));
  }

  /**
  * Check if Code-Scan are enabled for a project.
  *
  * @param project The project.
  * @return A value of {@link OssFeatures#RUNS_CODEQL_SCANS}.
  * @see <a href=
  *      "https://docs.github.com/en/code-security/code-scanning/automatically-scanning-your-code-for-vulnerabilities-and-errors/configuring-code-scanning">Check
  *      if Code-Scan are enabled for a repository</a>
  */
  private Value<Boolean> enabledCodeScanAlerts(GitHubProject project) {
    try (CloseableHttpClient client = httpClient()) {
      String url = String.format("https://api.github.com/repos/%s/%s/code-scanning/alerts",project.organization().name(), project.name());
      HttpGet request = new HttpGet(url);
      request.addHeader(HttpHeaders.ACCEPT, "application/vnd.github.v3+json");
      request.addHeader(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", fetcher.token()));
      try (CloseableHttpResponse response = client.execute(request)) {
        return RUNS_CODEQL_SCANS.value(response.getStatusLine().getStatusCode() == 200)
        .explainIf(false,"Code-Scan are not enabled in the project");
      }
    } catch (IOException e) {
      logger.warn("Oops! Could not check whether Code-Scan are enabled or not", e);
    }
    return RUNS_CODEQL_SCANS.unknown()
    .explain("Could not check whether or not Code-Scan are enabled in the project");
  }

  /**
  * Creates an HTTP client.
  *
  * @return A new HTTP client.
  */
  CloseableHttpClient httpClient() {
    return HttpClients.createDefault();
  }

}
