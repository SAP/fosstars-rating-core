package com.sap.oss.phosphor.fosstars.data.artifact;

import static com.sap.oss.phosphor.fosstars.model.Subject.cast;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.ARTIFACT_VERSION;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.RELEASED_ARTIFACT_VERSIONS;

import com.fasterxml.jackson.databind.JsonNode;
import com.sap.oss.phosphor.fosstars.data.AbstractReleaseInfoLoader;
import com.sap.oss.phosphor.fosstars.model.Subject;
import com.sap.oss.phosphor.fosstars.model.ValueSet;
import com.sap.oss.phosphor.fosstars.model.subject.oss.MavenArtifact;
import com.sap.oss.phosphor.fosstars.model.value.ArtifactVersion;
import com.sap.oss.phosphor.fosstars.model.value.ArtifactVersions;
import com.sap.oss.phosphor.fosstars.model.value.ValueHashSet;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * This data provider tries to fill out the
 * {@link com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures#RELEASED_ARTIFACT_VERSIONS}
 * and {@link com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures#ARTIFACT_VERSION}
 * features. This data provider gathers release info about {@link MavenArtifact}.
 */
public class ReleaseInfoFromMaven extends AbstractReleaseInfoLoader {

  /**
   * Standard character type encoding.
   */
  private static final String UTF_8 = StandardCharsets.UTF_8.name();

  /**
   * Gathers release information about Maven artifacts.
   * 
   * @param mavenArtifact A {@link MavenArtifact}.
   * @param start The start index to get the list of versions.
   * @return A {@link JsonNode} containing the Maven artifact release information.
   * @throws IOException If something goes wrong.
   */
  private JsonNode mavenArtifactReleaseInfo(MavenArtifact mavenArtifact, int start)
      throws IOException {
    String groupQuery = URLEncoder.encode(String.format("g:\"%s\"", mavenArtifact.group()), UTF_8);
    String artifactQuery =
        URLEncoder.encode(String.format("a:\"%s\"", mavenArtifact.artifact()), UTF_8);
    String preparedQuery = String.format("%s+AND+%s", groupQuery, artifactQuery);

    String url = String.format(
        "https://search.maven.org/solrsearch/select?q=%s&core=gav&wt=json&rows=100&start=%s",
        preparedQuery, start);
    return fetch(url);
  }

  /**
   * Gathers versions from Maven artifacts.
   * 
   * @param mavenArtifact A {@link MavenArtifact}.
   * @param artifactVersions The set of {@link ArtifactVersion}s.
   * @param start The start index to get the list of versions.
   * @return A filled set {@link ArtifactVersion}s.
   * @throws IOException If something goes wrong.
   */
  private Set<ArtifactVersion> fetchVersions(MavenArtifact mavenArtifact,
      Set<ArtifactVersion> artifactVersions, int start) throws IOException {
    JsonNode json = mavenArtifactReleaseInfo(mavenArtifact, start);
    JsonNode docs = json.at("/response/docs");

    if (docs.isArray()) {
      for (JsonNode item : docs) {
        if (item.has("v") && item.has("timestamp")) {
          artifactVersions.add(new ArtifactVersion(item.get("v").asText(),
              convertEpochToLocalDate(item.get("timestamp").asLong())));
        }
      }
    }

    int numFound = json.at("/response/numFound").asInt();
    if (numFound > artifactVersions.size()) {
      return fetchVersions(mavenArtifact, artifactVersions, start + 100);
    }
    return artifactVersions;
  }

  @Override
  public ReleaseInfoFromMaven update(Subject subject, ValueSet values) throws IOException {
    Objects.requireNonNull(values, "Oh no! Values is null!");
    MavenArtifact mavenArtifact = cast(subject, MavenArtifact.class);

    values.update(RELEASED_ARTIFACT_VERSIONS.unknown());
    values.update(ARTIFACT_VERSION.unknown());

    Set<ArtifactVersion> artifactVersions = fetchVersions(mavenArtifact, new HashSet<>(), 0);
    if (!artifactVersions.isEmpty()) {
      values.update(RELEASED_ARTIFACT_VERSIONS.value(new ArtifactVersions(artifactVersions)));
      updateArtifactVersion(mavenArtifact.version(), artifactVersions, values);
    }
    return this;
  }

  @Override
  public boolean supports(Subject subject) {
    return subject instanceof MavenArtifact;
  }

  /**
   * Main function used for testing purpose only.
   * 
   * @param args String array
   * @throws IOException If something goes wrong.
   */
  public static void main(String[] args) throws IOException {
    MavenArtifact mavenArtifact =
        new MavenArtifact("com.fasterxml.jackson.core", "jackson-databind", "2.9.8", null);
    ReleaseInfoFromMaven releaseInfoFromMaven = new ReleaseInfoFromMaven();
    ValueSet values = new ValueHashSet();
    releaseInfoFromMaven = releaseInfoFromMaven.update(mavenArtifact, values);
    values.forEach(System.out::println);
  }
}