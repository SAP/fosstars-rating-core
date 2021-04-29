package com.sap.oss.phosphor.fosstars.data.artifact;

import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.ARTIFACT_VERSION;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.RELEASED_ARTIFACT_VERSIONS;

import com.fasterxml.jackson.databind.JsonNode;
import com.sap.oss.phosphor.fosstars.data.AbstractReleaseInfoLoader;
import com.sap.oss.phosphor.fosstars.data.DataProvider;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.ValueSet;
import com.sap.oss.phosphor.fosstars.model.subject.oss.MavenArtifact;
import com.sap.oss.phosphor.fosstars.model.value.ArtifactVersion;
import com.sap.oss.phosphor.fosstars.model.value.ArtifactVersions;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

/**
 * This data provider tries to fill out the
 * {@link com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures#RELEASED_ARTIFACT_VERSIONS}
 * and {@link com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures#ARTIFACT_VERSION}
 * features. This data provider gathers release info about {@link MavenArtifact}.
 */
public class ReleaseInfoFromMaven extends AbstractReleaseInfoLoader<MavenArtifact> {

  /**
   * Standard character type encoding.
   */
  private static final String UTF_8 = StandardCharsets.UTF_8.name();

  /**
   * Gathers release information about Maven artifacts.
   * 
   * @param mavenArtifact A {@link MavenArtifact}.
   * @return A {@link JsonNode} containing the Maven artifact release information.
   * @throws IOException If something goes wrong.
   */
  private JsonNode mavenArtifactReleaseInfo(MavenArtifact mavenArtifact) throws IOException {
    String groupQuery = URLEncoder.encode(String.format("g:\"%s\"", mavenArtifact.group()), UTF_8);
    String artifactQuery =
        URLEncoder.encode(String.format("a:\"%s\"", mavenArtifact.artifact()), UTF_8);
    String preparedQuery = String.format("%s+AND+%s", groupQuery, artifactQuery);

    String url =
        String.format("https://search.maven.org/solrsearch/select?q=%s&core=gav&wt=json&rows=10000",
            preparedQuery);
    return fetch(url);
  }

  @Override
  public DataProvider<MavenArtifact> update(MavenArtifact mavenArtifact, ValueSet values)
      throws IOException {
    JsonNode json = mavenArtifactReleaseInfo(mavenArtifact);
    JsonNode docs = json.at("/response/docs");

    if (docs.isArray()) {
      Set<ArtifactVersion> artifactVersions = new HashSet<>();
      for (JsonNode item : docs) {
        if (item.has("v") && item.has("timestamp")) {
          artifactVersions.add(new ArtifactVersion(item.get("v").asText(),
              convertEpochToLocalDate(item.get("timestamp").asLong())));
        }
      }
      values.update(RELEASED_ARTIFACT_VERSIONS.value(new ArtifactVersions(artifactVersions)));
      updateArtifactVersion(mavenArtifact, artifactVersions, values);
      return this;
    }

    values.update(RELEASED_ARTIFACT_VERSIONS.unknown());
    values.update(ARTIFACT_VERSION.unknown());
    return this;
  }

  /**
   * Update the {@link com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures#ARTIFACT_VERSION}
   * value based on given parameters.
   * If version of MavenArtifact is not in the set of artifact versions or the MavenArtifact
   * does not have a version set the ARTIFACT_VERSION is set to unknown.
   *
   * @param mavenArtifact The maven artifact.
   * @param artifactVersions All found artifact versions.
   * @param values The set of values to be updated.
   */
  private static void updateArtifactVersion(MavenArtifact mavenArtifact,
      Set<ArtifactVersion> artifactVersions, ValueSet values) {

    Value<ArtifactVersion> match = mavenArtifact.version()
        .flatMap(version -> artifactVersions.stream()
          .filter(v -> v.getVersion().equals(version))
          .findFirst())
        .map(ARTIFACT_VERSION::value)
        .orElseGet(ARTIFACT_VERSION::unknown);
    values.update(match);
  }
}
