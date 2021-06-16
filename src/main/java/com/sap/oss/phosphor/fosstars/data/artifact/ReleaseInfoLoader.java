package com.sap.oss.phosphor.fosstars.data.artifact;

import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.RELEASED_ARTIFACT_VERSIONS;

import com.sap.oss.phosphor.fosstars.data.AbstractReleaseInfoLoader;
import com.sap.oss.phosphor.fosstars.data.github.ReleasesFromGitHub;
import com.sap.oss.phosphor.fosstars.model.Subject;
import com.sap.oss.phosphor.fosstars.model.ValueSet;
import com.sap.oss.phosphor.fosstars.model.subject.oss.Artifact;
import com.sap.oss.phosphor.fosstars.model.subject.oss.GitHubProject;
import com.sap.oss.phosphor.fosstars.model.subject.oss.MavenArtifact;
import com.sap.oss.phosphor.fosstars.model.subject.oss.NpmArtifact;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

/**
 * This data provider tries to fill out the
 * {@link com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures#RELEASED_ARTIFACT_VERSIONS}
 * feature. It is based on one of the following data providers:
 * <ul>
 * <li>{@link ReleaseInfoFromMaven}</li>
 * <li>{@link ReleaseInfoFromNpm}</li>
 * <li>{@link ReleasesFromGitHub} if
 * {@link com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures#RELEASED_ARTIFACT_VERSIONS}
 * feature could not be filled up from one of the above-mentioned data providers.</li>
 * </ul>
 */
public class ReleaseInfoLoader extends AbstractReleaseInfoLoader {

  /**
   * A {@link ReleasesFromGitHub} data provider.
   */
  private final ReleasesFromGitHub releasesFromGitHub;

  /**
   * A {@link ReleaseInfoFromMaven} data provider.
   */
  private final ReleaseInfoFromMaven releaseInfoFromMaven;

  /**
   * A {@link ReleaseInfoFromNpm} data provider.
   */
  private final ReleaseInfoFromNpm releaseInfoFromNpm;

  /**
   * Initializes a data provider.
   *
   * @param releasesFromGitHub A {@link ReleasesFromGitHub} data provider.
   * @param releaseInfoFromMaven A {@link ReleaseInfoFromMaven} data provider.
   * @param releaseInfoFromNpm A {@link ReleaseInfoFromNpm} data provider.
   */
  public ReleaseInfoLoader(ReleasesFromGitHub releasesFromGitHub,
      ReleaseInfoFromMaven releaseInfoFromMaven, ReleaseInfoFromNpm releaseInfoFromNpm) {
    this.releasesFromGitHub = Objects.requireNonNull(releasesFromGitHub,
        "Oh no! You gave me a null instead of a ReleasesFromGitHub!");
    this.releaseInfoFromMaven = Objects.requireNonNull(releaseInfoFromMaven,
        "Oh no! You gave me a null instead of a ReleaseInfoFromMaven!");
    this.releaseInfoFromNpm = Objects.requireNonNull(releaseInfoFromNpm,
        "Oh no! You gave me a null instead of a ReleaseInfoFromNpm!");
  }

  @Override
  public ReleaseInfoLoader update(Subject subject, ValueSet values) throws IOException {
    Objects.requireNonNull(subject, "Oh no! Subject cannot be null");
    Objects.requireNonNull(values, "Oh no! Values cannot be null");

    if (hasFeatureIn(values)) {
      throw new IOException("Oh no! RELEASED_ARTIFACT_VERSIONS feature is already known.");
    }

    if (subject instanceof MavenArtifact) {
      releaseInfoFromMaven.update((MavenArtifact) subject, values);
    } else if (subject instanceof NpmArtifact) {
      releaseInfoFromNpm.update((NpmArtifact) subject, values);
    }

    Optional<GitHubProject> project = projectOf(subject);
    if (!hasFeatureIn(values) && project.isPresent()) {
      releasesFromGitHub.update(project.get(), values);
    }

    if (!hasFeatureIn(values)) {
      values.update(RELEASED_ARTIFACT_VERSIONS.unknown());
    }

    return this;
  }

  /**
   * Looks for a project on GitHub that is associated with a subject.
   *
   * @param subject The subject.
   * @return A project on GitHub if found.
   */
  private Optional<GitHubProject> projectOf(Subject subject) {
    if (subject instanceof GitHubProject) {
      return Optional.of((GitHubProject) subject);
    }

    if (subject instanceof Artifact) {
      return ((Artifact) subject).project();
    }

    return Optional.empty();
  }

  /**
   * Checks if the values contain the known feature
   * {@link com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures#RELEASED_ARTIFACT_VERSIONS}.
   *
   * @param values The {@link ValueSet}.
   * @return true if the values has the feature
   *          {@link com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures
   *          #RELEASED_ARTIFACT_VERSIONS}. Otherwise false.
   */
  private static boolean hasFeatureIn(ValueSet values) {
    return values.of(RELEASED_ARTIFACT_VERSIONS).isPresent()
        && !values.of(RELEASED_ARTIFACT_VERSIONS).get().isUnknown();
  }

  @Override
  public boolean supports(Subject subject) {
    return subject instanceof Artifact;
  }
}