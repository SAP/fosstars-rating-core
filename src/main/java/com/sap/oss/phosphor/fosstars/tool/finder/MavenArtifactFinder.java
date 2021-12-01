package com.sap.oss.phosphor.fosstars.tool.finder;

import com.fasterxml.jackson.databind.JsonNode;
import com.sap.oss.phosphor.fosstars.maven.GAV;
import com.sap.oss.phosphor.fosstars.model.subject.oss.GitHubProject;
import com.sap.oss.phosphor.fosstars.model.subject.oss.MavenArtifact;
import com.sap.oss.phosphor.fosstars.tool.GitHubProjectFinder;
import com.sap.oss.phosphor.fosstars.tool.MavenScmFinder;
import com.sap.oss.phosphor.fosstars.util.Json;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

/**
 * The class also checks specific artifacts and add them to results of the scan if they exist.
 */
public class MavenArtifactFinder extends AbstractEntityFinder<MavenArtifact> {

  /**
   * Standard character type encoding.
   */
  private static final String UTF_8 = StandardCharsets.UTF_8.name();

  /**
   * Gather maven artifacts with the possible released versions.
   *
   * @return A list of artifacts.
   * @throws IOException If something went wrong.
   */
  @Override
  public List<MavenArtifact> run() throws IOException {
    List<MavenArtifact> mavenArtifacts = new ArrayList<>();
    for (MavenArtifactConfig mavenArtifactConfig : config.mavenArtifactConfigs()) {
      mavenArtifacts
          .addAll(artifactsFrom(mavenArtifactConfig.group(), mavenArtifactConfig.artifact()));
    }
    return mavenArtifacts;
  }

  /**
   * Sets a config.
   *
   * @param config The config to be used.
   * @return The same {@link MavenArtifactFinder}.
   */
  @Override
  public Finder set(FinderConfig config) {
    this.config = Objects.requireNonNull(config, "Oh no! Config can't be null!");
    return this;
  }

  /**
   * Gathers release information about Maven artifacts.
   *
   * @param group    A group of the {@link MavenArtifact}.
   * @param artifact name of the {@link MavenArtifact}.
   * @return A {@link JsonNode} containing the Maven artifact release information.
   * @throws IOException If something goes wrong.
   */
  private JsonNode versionsIn(String group, String artifact) throws IOException {
    String groupQuery = URLEncoder.encode(String.format("g:\"%s\"", group), UTF_8);
    String artifactQuery =
        URLEncoder.encode(String.format("a:\"%s\"", artifact), UTF_8);
    String preparedQuery = String.format("%s+AND+%s", groupQuery, artifactQuery);

    String url =
        String.format("https://search.maven.org/solrsearch/select?q=%s&core=gav&wt=json&rows=10000",
            preparedQuery);
    return fetch(url);
  }

  /**
   * Gets the list of Maven artifacts.
   *
   * @param group    A group of the {@link MavenArtifact}.
   * @param artifact name of the {@link MavenArtifact}.
   * @return A list of {@link MavenArtifact}s.
   * @throws IOException If something goes wrong.
   */
  public List<MavenArtifact> artifactsFrom(String group, String artifact) throws IOException {
    JsonNode json = versionsIn(group, artifact);
    JsonNode docs = json.at("/response/docs");

    List<MavenArtifact> artifacts = new ArrayList<>();
    if (docs.isArray()) {
      for (JsonNode item : docs) {
        if (item.has("v")) {
          String version = item.get("v").asText();
          Optional<GitHubProject> project = new MavenScmFinder()
              .findGithubProjectFor(new GAV(group, artifact, version));
          if (project.isPresent()) {
            artifacts.add(new MavenArtifact(group, artifact, version, project.get()));
          }
        }
      }
    }
    return artifacts;
  }

  /**
   * Fetches info from the URL.
   *
   * @param url from which the information needs to be collected.
   * @return The info from the URL.
   * @throws IOException If something went wrong.
   */
  protected JsonNode fetch(String url) throws IOException {
    try (CloseableHttpClient client = httpClient()) {
      HttpGet httpGetRequest = new HttpGet(url);
      httpGetRequest.addHeader(HttpHeaders.ACCEPT, ContentType.APPLICATION_JSON.getMimeType());
      try (CloseableHttpResponse httpResponse = client.execute(httpGetRequest)) {
        return Json.mapper().readTree(httpResponse.getEntity().getContent());
      }
    }
  }

  /**
   * Creates an HTTP client.
   *
   * @return An HTTP client.
   */
  public CloseableHttpClient httpClient() {
    return HttpClients.createDefault();
  }
}
