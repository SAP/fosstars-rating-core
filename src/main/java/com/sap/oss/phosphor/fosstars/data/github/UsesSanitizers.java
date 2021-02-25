package com.sap.oss.phosphor.fosstars.data.github;

import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_ADDRESS_SANITIZER;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_MEMORY_SANITIZER;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_UNDEFINED_BEHAVIOR_SANITIZER;
import static com.sap.oss.phosphor.fosstars.model.other.Utils.setOf;

import com.sap.oss.phosphor.fosstars.model.Feature;
import com.sap.oss.phosphor.fosstars.model.ValueSet;
import com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures;
import com.sap.oss.phosphor.fosstars.model.subject.oss.GitHubProject;
import com.sap.oss.phosphor.fosstars.model.value.ValueHashSet;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
   * A regex for searching sanitizers.
   */
  private static final Pattern PATTERN
      = Pattern.compile("-fsanitize=\\s*((address|memory|undefined)[\\s,]*)*");

  /**
   * A compiler option that defines sanitizers.
   */
  private static final String SANITIZER_OPTION = "-fsanitize=";

  /**
   * A list of well-known file names of build configs.
   */
  private static final String[] BUILD_CONFIGS = {
      ".travis.yml", "Configure", "CMakeLists.txt", "Makefile"
  };

  /**
   * A list of well-known files extensions of build configs.
   */
  private static final String[] BUILD_CONFIG_SUFFIXES = {
      ".ac", ".cmake", ".bazel"
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
  public Set<Feature<?>> supportedFeatures() {
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

    LocalRepository repository = GitHubDataFetcher.localRepositoryFor(project);

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
   * Checks if a file looks like a build config.
   *
   * @return True if a file looks like a build config, false otherwise.
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
   * Looks for options that enable sanitizers.
   *
   * @param content The content to be checked.
   * @return A list of options that enable sanitizers.
   * @throws IOException If something went wrong.
   */
  static List<String> lookForSanitizers(String content) throws IOException {
    List<String> options = new ArrayList<>();

    try (BufferedReader reader = new BufferedReader(new StringReader(content))) {
      String line;
      while ((line = reader.readLine()) != null) {
        Matcher matcher = PATTERN.matcher(line);
        if (matcher.find()) {
          options.addAll(parseOptions(matcher.group(0).trim()));
        }
      }
    }

    return options;
  }

  /**
   * Looks for options that enable sanitizers.
   *
   * @param line The string to be parsed.
   * @return A list of options that enable sanitizers.
   */
  static List<String> parseOptions(String line) {
    List<String> options = new ArrayList<>();

    line = line.trim();
    if (!line.startsWith(SANITIZER_OPTION)) {
      return options;
    }

    line = line.substring(SANITIZER_OPTION.length());

    // tokenize by a comma that may be surrounded by whitespaces
    String[] values = line.split("\\s*,\\s*");
    for (String value : values) {
      value = value.trim();
      if (value.isEmpty()) {
        continue;
      }

      options.add(value);
    }

    return options;
  }
}
