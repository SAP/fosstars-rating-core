package com.sap.sgs.phosphor.fosstars.data.github;

import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.USES_ADDRESS_SANITIZER;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.USES_MEMORY_SANITIZER;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.USES_UNDEFINED_BEHAVIOR_SANITIZER;
import static com.sap.sgs.phosphor.fosstars.model.other.Utils.setOf;

import com.sap.sgs.phosphor.fosstars.model.Feature;
import com.sap.sgs.phosphor.fosstars.model.ValueSet;
import com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures;
import com.sap.sgs.phosphor.fosstars.model.value.ValueHashSet;
import com.sap.sgs.phosphor.fosstars.tool.github.GitHubProject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * The data providers checks if a project uses sanitizers. It gathers the following features:
 * <ul>
 *   <li>{@link OssFeatures#USES_ADDRESS_SANITIZER}</li>
 *   <li>{@link OssFeatures#USES_MEMORY_SANITIZER}</li>
 *   <li>{@link OssFeatures#USES_UNDEFINED_BEHAVIOR_SANITIZER}</li>
 * </ul>
 */
public class UsesSanitizers extends GitHubCachingDataProvider {

  /**
   * A compiler option that defines sanitizers.
   */
  private static final String SANITIZER_OPTION = "-fsanitize=";

  /**
   * A list of well-known file names of build configs.
   */
  private static final String[] BUILD_CONFIGS = {
      ".travis.yml", "Configure", "CMakeLists.txt"
  };

  /**
   * A list of well-known files extensions of build configs.
   */
  private static final String[] BUILD_CONFIG_SUFFIXES = {
      ".ac", ".cmake"
  };

  /**
   * Initializes a data provider.
   *
   * @param fetcher An interface to GitHub.
   */
  public UsesSanitizers(GitHubDataFetcher fetcher) {
    super(fetcher);
  }

  @Override
  protected Set<Feature> supportedFeatures() {
    return setOf(USES_ADDRESS_SANITIZER, USES_MEMORY_SANITIZER, USES_UNDEFINED_BEHAVIOR_SANITIZER);
  }

  @Override
  protected ValueSet fetchValuesFor(GitHubProject project) throws IOException {
    Objects.requireNonNull(project, "Oh no! Project is null!");

    logger.info("Figuring out if the project uses sanitizers ...");

    ValueSet values = new ValueHashSet();
    values.update(USES_ADDRESS_SANITIZER.value(false));
    values.update(USES_MEMORY_SANITIZER.value(false));
    values.update(USES_UNDEFINED_BEHAVIOR_SANITIZER.value(false));

    LocalRepository repository = fetcher.localRepositoryFor(project);

    List<Path> files = repository.files(
        path -> Files.isRegularFile(path) && maybeBuildConfig(path));

    for (Path path : files) {
      Optional<String> content = repository.file(path);
      if (!content.isPresent()) {
        continue;
      }

      List<String> sanitizers = lookForSanitizers(content.get());
      for (String sanitizer : sanitizers) {
        if (sanitizer.contains("address")) {
          values.update(USES_ADDRESS_SANITIZER.value(true));
        }

        if (sanitizer.contains("memory")) {
          values.update(USES_MEMORY_SANITIZER.value(true));
        }

        if (sanitizer.contains("undefined")) {
          values.update(USES_UNDEFINED_BEHAVIOR_SANITIZER.value(true));
        }
      }
    }

    return values;
  }

  /**
   * Returns true if a file looks like a build config, false otherwise.
   */
  static boolean maybeBuildConfig(Path path) {
    String filename = path.getFileName().toString();

    for (String config : BUILD_CONFIGS) {
      if (config.equals(filename)) {
        return true;
      }
    }

    for (String suffix : BUILD_CONFIG_SUFFIXES) {
      if (filename.endsWith(suffix)) {
        return true;
      }
    }

    return false;
  }

  /**
   * Looks for options that enable one of the sanitizers.
   *
   * @param content The content to be checked.
   * @return A list of lines that contain options that enable the sanitizers.
   * @throws IOException If something went wrong.
   */
  static List<String> lookForSanitizers(String content) throws IOException {
    List<String> lines = new ArrayList<>();

    try (BufferedReader reader = new BufferedReader(new StringReader(content))) {
      String line;
      while ((line = reader.readLine()) != null) {
        int start = line.indexOf(SANITIZER_OPTION);
        if (start >= 0) {
          int end = line.indexOf(" ", start);
          if (end < 0) {
            end = line.length();
          }
          lines.add(line.substring(start, end));
        }
      }
    }

    return lines;
  }
}
