package com.sap.oss.phosphor.fosstars.data.artifact;

import static com.sap.oss.phosphor.fosstars.model.Subject.cast;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.ARTIFACT_VERSION;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.RELEASED_ARTIFACT_VERSIONS;

import com.sap.oss.phosphor.fosstars.data.AbstractReleaseInfoLoader;
import com.sap.oss.phosphor.fosstars.model.Subject;
import com.sap.oss.phosphor.fosstars.model.ValueSet;
import com.sap.oss.phosphor.fosstars.model.subject.oss.MavenArtifact;
import com.sap.oss.phosphor.fosstars.model.value.ArtifactVersion;
import com.sap.oss.phosphor.fosstars.model.value.ArtifactVersions;
import com.sap.oss.phosphor.fosstars.model.value.ValueHashSet;
import java.io.IOException;
import java.util.Objects;
import java.util.Set;

/**
 * This data provider tries to fill out the
 * {@link com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures#RELEASED_ARTIFACT_VERSIONS}
 * and {@link com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures#ARTIFACT_VERSION}
 * features. This data provider gathers release info about {@link MavenArtifact}.
 */
public class ReleaseInfoFromMaven extends AbstractReleaseInfoLoader {

  @Override
  public ReleaseInfoFromMaven update(Subject subject, ValueSet values) throws IOException {
    Objects.requireNonNull(values, "Oh no! Values is null!");
    MavenArtifact mavenArtifact = cast(subject, MavenArtifact.class);

    values.update(RELEASED_ARTIFACT_VERSIONS.unknown());
    values.update(ARTIFACT_VERSION.unknown());

    Set<ArtifactVersion> artifactVersions = versionsOf(mavenArtifact);
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
    releaseInfoFromMaven.update(mavenArtifact, values);
    values.forEach(System.out::println);
  }
}