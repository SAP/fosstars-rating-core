package com.sap.oss.phosphor.fosstars.data.artifact;

import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.ARTIFACT_VERSION;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.RELEASED_ARTIFACT_VERSIONS;

import com.fasterxml.jackson.databind.JsonNode;
import com.sap.oss.phosphor.fosstars.data.AbstractReleaseInfoLoader;
import com.sap.oss.phosphor.fosstars.data.DataProvider;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.ValueSet;
import com.sap.oss.phosphor.fosstars.model.subject.oss.NpmArtifact;
import com.sap.oss.phosphor.fosstars.model.value.ArtifactVersion;
import com.sap.oss.phosphor.fosstars.model.value.ArtifactVersions;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

/**
 * This data provider tries to fill out the
 * {@link com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures#RELEASED_ARTIFACT_VERSIONS}
 * and {@link com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures#ARTIFACT_VERSION}
 * feature. This data provider gathers release info about {@link NpmArtifact}.
 */
public class ReleaseInfoFromNpm extends AbstractReleaseInfoLoader<NpmArtifact> {

  /**
   * Gathers release information about NPM artifacts.
   * 
   * @param npmArtifact A {@link NpmArtifact}.
   * @return A {@link JsonNode} containing the NPM artifact release information.
   * @throws IOException If something goes wrong.
   */
  private JsonNode npmArtifactReleaseInfo(NpmArtifact npmArtifact) throws IOException {
    String url = String.format("https://registry.npmjs.org/%s", npmArtifact.identifier());
    return fetch(url);
  }

  @Override
  public DataProvider<NpmArtifact> update(NpmArtifact npmArtifact, ValueSet values)
      throws IOException {
    JsonNode json = npmArtifactReleaseInfo(npmArtifact);

    if (json.has("time")) {
      Set<ArtifactVersion> artifactVersions = new HashSet<>();
      Iterator<Entry<String, JsonNode>> fields = json.get("time").fields();

      while (fields.hasNext()) {
        Entry<String, JsonNode> field = fields.next();
        if ("modified".equals(field.getKey()) || "created".equals(field.getKey())) {
          continue;
        }
        artifactVersions.add(
            new ArtifactVersion(field.getKey(), convertToLocalDate(field.getValue().asText())));
      }
      values.update(RELEASED_ARTIFACT_VERSIONS.value(new ArtifactVersions(artifactVersions)));
      updateArtifactVersion(npmArtifact, artifactVersions, values);
      return this;
    }
    values.update(RELEASED_ARTIFACT_VERSIONS.unknown());
    values.update(ARTIFACT_VERSION.unknown());
    return this;
  }

  /**
   * Update the {@link com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures#ARTIFACT_VERSION}
   * value based on given parameters.
   * If version of NpmArtifact is not in the set of artifact versions or the NpmArtifact
   * does not have a version set the ARTIFACT_VERSION is set to unknown.
   *
   * @param npmArtifact The npm artifact.
   * @param artifactVersions All found artifact versions.
   * @param values The set of values to be updated.
   */
  private static void updateArtifactVersion(NpmArtifact npmArtifact,
      Set<ArtifactVersion> artifactVersions, ValueSet values) {

    Value<ArtifactVersion> match = npmArtifact.version()
        .flatMap(version -> artifactVersions.stream()
            .filter(v -> v.version().equals(version))
            .findFirst())
        .map(ARTIFACT_VERSION::value)
        .orElseGet(ARTIFACT_VERSION::unknown);
    values.update(match);
  }
}
