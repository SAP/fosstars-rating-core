package com.sap.oss.phosphor.fosstars.data.github;

import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.RELEASED_ARTIFACT_VERSIONS;

import com.sap.oss.phosphor.fosstars.model.Feature;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.subject.oss.GitHubProject;
import com.sap.oss.phosphor.fosstars.model.value.ArtifactVersion;
import com.sap.oss.phosphor.fosstars.model.value.ArtifactVersions;
import com.sap.oss.phosphor.fosstars.model.value.SemanticVersion;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.kohsuke.github.GHRelease;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GHTag;

/**
 * This data provider collects version information from GitHub releases.
 */
public class ReleasesFromGitHub extends CachedSingleFeatureGitHubDataProvider<ArtifactVersions> {

  /**
   * Initializes a data provider.
   *
   * @param fetcher An interface to GitHub.
   */
  public ReleasesFromGitHub(GitHubDataFetcher fetcher) {
    super(fetcher);
  }

  @Override
  protected Feature<ArtifactVersions> supportedFeature() {
    return RELEASED_ARTIFACT_VERSIONS;
  }

  @Override
  protected Value<ArtifactVersions> fetchValueFor(GitHubProject project) throws IOException {
    logger.info("Fetching GitHub release information ...");

    GHRepository repo = fetcher.repositoryFor(project);

    List<GHRelease> releases = repo.listReleases().toList();
    Set<ArtifactVersion> artifactVersions;
    if (releases.isEmpty()) {
      logger.info("No release information found. Let's try to extract version info from tags ...");
      artifactVersions = repo.listTags().toList().stream()
          .filter(tag -> SemanticVersion.isSemanticVersion(tag.getName()))
          .map(this::createArtifactVersion)
          .filter(Objects::nonNull)
          .collect(Collectors.toSet());
    } else {
      artifactVersions = releases.stream()
        .filter(Objects::nonNull)
        .map(r -> new ArtifactVersion(r.getName(), convertToLocalDate(r.getPublished_at())))
        .collect(Collectors.toSet());
    }

    return RELEASED_ARTIFACT_VERSIONS.value(new ArtifactVersions(artifactVersions));
  }

  private ArtifactVersion createArtifactVersion(GHTag tag) {
    try {
      return new ArtifactVersion(tag.getName(),
          convertToLocalDate(tag.getCommit().getCommitDate()));
    } catch (IOException e) {
      logger.warn("Could not create artifact version!", e);
      return null;
    }
  }

  /**
   * Convert a Date to a LocalDateTime instance using the system default ZoneId.
   *
   * @param date The date to be converted.
   * @return The time as LocalDateTime.
   */
  private static LocalDateTime convertToLocalDate(Date date) {
    return date.toInstant()
        .atZone(ZoneId.systemDefault())
        .toLocalDateTime();
  }
}
