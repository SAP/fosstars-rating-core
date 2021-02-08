package com.sap.oss.phosphor.fosstars.data.github;

import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.FUZZED_IN_OSS_FUZZ;

import com.sap.oss.phosphor.fosstars.model.Feature;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures;
import com.sap.oss.phosphor.fosstars.model.subject.oss.GitHubProject;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.io.IOUtils;

/**
 * This data provider check if an open-source project is included to the OSS-Fuzz project.
 * It fills out the {@link OssFeatures#FUZZED_IN_OSS_FUZZ} feature.
 */
public class FuzzedInOssFuzz extends CachedSingleFeatureGitHubDataProvider<Boolean> {

  /**
   * OSS-Fuzz project on GitHub.
   */
  static final GitHubProject OSS_FUZZ_PROJECT = new GitHubProject("google", "oss-fuzz");

  /**
   * Initializes a data provider.
   *
   * @param fetcher An interface to GitHub.
   */
  public FuzzedInOssFuzz(GitHubDataFetcher fetcher) {
    super(fetcher);
  }

  @Override
  protected Feature<Boolean> supportedFeature() {
    return FUZZED_IN_OSS_FUZZ;
  }

  @Override
  protected Value<Boolean> fetchValueFor(GitHubProject project) throws IOException {
    logger.info("Figuring out if the project is fuzzed in OSS-Fuzz ...");

    LocalRepository ossFuzzRepository = GitHubDataFetcher.localRepositoryFor(OSS_FUZZ_PROJECT);

    try (Stream<Path> paths = Files.walk(ossFuzzRepository.info().path())) {
      List<Path> dockerFiles = paths
          .filter(Files::isRegularFile)
          .filter(path -> "Dockerfile".equals(path.getFileName().toString()))
          .collect(Collectors.toList());

      String url = project.scm().toString();
      for (Path dockerFile : dockerFiles) {
        try (InputStream is = Files.newInputStream(dockerFile)) {
          String content = IOUtils.toString(is);
          if (content.contains(url)) {
            return FUZZED_IN_OSS_FUZZ.value(true);
          }
        }
      }
    }

    return FUZZED_IN_OSS_FUZZ.value(false);
  }
}
