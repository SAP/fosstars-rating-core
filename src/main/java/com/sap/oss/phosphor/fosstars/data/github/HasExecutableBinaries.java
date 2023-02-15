package com.sap.oss.phosphor.fosstars.data.github;

import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.HAS_EXECUTABLE_BINARIES;

import com.google.common.base.Predicate;
import com.sap.oss.phosphor.fosstars.model.Feature;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.subject.oss.GitHubProject;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

/**
 * The data provider tries to figure out if an open-source project has executable binaries (for
 * example, .class, .pyc, .exe)..
 */
public class HasExecutableBinaries extends CachedSingleFeatureGitHubDataProvider<Boolean> {

  /**
   * List of file extensions deemed as executable binaries.
   */
  static final List<String> FILE_EXTENSIONS = Arrays.asList(".crx", ".deb", ".dex", ".dey", ".elf",
      ".o", ".so", ".iso", ".class", ".jar", ".bundle", ".dylib", ".lib", ".msi", ".acm", ".ax",
      ".cpl", ".dll", ".drv", ".efi", ".exe", ".mui", ".ocx", ".scr", ".sys", ".tsp", ".pyc",
      ".pyo", ".par", ".rpm", ".swf", ".torrent", ".cab", ".whl");

  /**
   * Predicate to confirm if there is a file in open-source project with the executable binary
   * extension.
   */
  private static final Predicate<Path> FILE_EXTENSIONS_PREDICATE = path -> isExecutableBinary(path);

  /**
   * Initializes a data provider.
   *
   * @param fetcher An interface to GitHub.
   */
  public HasExecutableBinaries(GitHubDataFetcher fetcher) {
    super(fetcher);
  }

  @Override
  protected Feature<Boolean> supportedFeature() {
    return HAS_EXECUTABLE_BINARIES;
  }

  @Override
  protected Value<Boolean> fetchValueFor(GitHubProject project) throws IOException {
    logger.info("Figuring out if the project has executable binaries...");

    LocalRepository localRepository = loadLocalRepository(project);
    List<Path> paths = localRepository.files(FILE_EXTENSIONS_PREDICATE);
    return HAS_EXECUTABLE_BINARIES.value(!paths.isEmpty());
  }

  /**
   * Fetch the locally cloned repository.
   * 
   * @param project The GitHub project.
   * @return {@link LocalRepository} clone repository.
   * @throws IOException If something went wrong.
   */
  LocalRepository loadLocalRepository(GitHubProject project) throws IOException {
    return GitHubDataFetcher.localRepositoryFor(project);
  }

  /**
   * Check if the file represented by the path is a executable binary file.
   * 
   * @param path The file path.
   * @return true if the executable binary file type is found, otherwise false.
   */
  private static boolean isExecutableBinary(Path path) {
    return FILE_EXTENSIONS.stream().anyMatch(ext -> path.getFileName().toString().endsWith(ext));
  }
}