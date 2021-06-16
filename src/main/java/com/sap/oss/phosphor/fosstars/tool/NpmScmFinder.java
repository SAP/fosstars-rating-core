package com.sap.oss.phosphor.fosstars.tool;

import static com.sap.oss.phosphor.fosstars.model.subject.oss.GitHubProject.isOnGitHub;
import static java.lang.String.format;

import com.fasterxml.jackson.databind.JsonNode;
import com.sap.oss.phosphor.fosstars.model.subject.oss.GitHubProject;
import com.sap.oss.phosphor.fosstars.util.Json;
import java.io.IOException;
import java.util.Locale;
import java.util.Optional;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

/**
 * The class takes a name of an NPM package and looks for a URL to its SCM.
 */
public class NpmScmFinder {

  /**
   * Takes a name of an NPM package and looks for its SCM on GitHub.
   *
   * @param identifier The name of the package.
   * @return A project on GitHub if found.
   * @throws IOException If something went wrong.
   */
  public Optional<GitHubProject> findGithubProjectFor(String identifier) throws IOException {
    Optional<String> scm = scmForNpm(identifier);
    if (!scm.isPresent()) {
      return Optional.empty();
    }

    String url = scm.get();

    if (isOnGitHub(url)) {
      return Optional.of(GitHubProject.parse(url));
    }

    return Optional.empty();
  }

  /**
   * Looks for an SCM for an NPM artifact.
   *
   * @param identifier An identifier of the NPM artifact.
   * @return An SCM for the artifact if found.
   * @throws IOException If something went wrong.
   */
  public Optional<String> scmForNpm(String identifier) throws IOException {
    String registryUrl = format("https://registry.npmjs.org/%s", identifier);
    JsonNode json = fetchJsonFrom(registryUrl);
    JsonNode repo = json.get("repository");
    if (repo == null) {
      return Optional.empty();
    }

    if (!repo.has("type") || !"git".equalsIgnoreCase(repo.get("type").asText())) {
      return Optional.empty();
    }

    String url = repo.get("url").asText().toLowerCase(Locale.US);
    int index = url.indexOf("github.com/");
    if (index > 0) {
      return Optional.of("https://" + url.substring(index));
    }

    return Optional.empty();
  }

  /**
   * Fetch JSON from a specified URL.
   *
   * @param url The URL.
   * @return A {@link JsonNode}.
   * @throws IOException If something went wrong.
   */
  static JsonNode fetchJsonFrom(String url) throws IOException {
    try (CloseableHttpClient client = HttpClients.createDefault()) {
      HttpGet httpGetRequest = new HttpGet(url);
      httpGetRequest.addHeader(HttpHeaders.ACCEPT, ContentType.APPLICATION_JSON.getMimeType());
      try (CloseableHttpResponse httpResponse = client.execute(httpGetRequest)) {
        return Json.mapper().readTree(httpResponse.getEntity().getContent());
      }
    }
  }
}
