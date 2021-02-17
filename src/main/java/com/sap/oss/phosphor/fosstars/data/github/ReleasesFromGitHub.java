package com.sap.oss.phosphor.fosstars.data.github;

import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.RELEASED_ARTIFACT_VERSIONS;

import com.sap.oss.phosphor.fosstars.model.Feature;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.subject.oss.GitHubProject;
import com.sap.oss.phosphor.fosstars.model.value.ArtifactVersion;
import com.sap.oss.phosphor.fosstars.model.value.ArtifactVersions;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.kohsuke.github.GHOrganization;
import org.kohsuke.github.GHRelease;
import org.kohsuke.github.GHRepository;

/**
 * This data provider collects version information from GitHub releases.
 */
public class ReleasesFromGitHub extends CachedSingleFeatureGitHubDataProvider {

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

    String orgName = project.organization().name();
    logger.info("Get info from '{}' org and project '{}'...", orgName, project.name());
    GHOrganization org = fetcher.github().getOrganization(orgName);
    GHRepository repo = org.getRepository(project.name());

    List<GHRelease> releases = repo.listReleases().toList();

    Set<ArtifactVersion> artifactVersions = releases.stream()
        .map(r -> new ArtifactVersion(r.getName(), convertToLocalDate(r.getPublished_at())))
        .collect(Collectors.toSet());

    return RELEASED_ARTIFACT_VERSIONS.value(new ArtifactVersions(artifactVersions));
  }

  private LocalDate convertToLocalDate(Date date) {
    // TODO (mibo): check if this is correct with time zones
    return date.toInstant()
        .atZone(ZoneId.systemDefault())
        .toLocalDate();
  }
}