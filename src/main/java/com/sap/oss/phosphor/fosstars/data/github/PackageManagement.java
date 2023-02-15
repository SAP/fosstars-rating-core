package com.sap.oss.phosphor.fosstars.data.github;

import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.LANGUAGES;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.PACKAGE_MANAGERS;
import static com.sap.oss.phosphor.fosstars.model.value.Language.C_SHARP;
import static com.sap.oss.phosphor.fosstars.model.value.Language.F_SHARP;
import static com.sap.oss.phosphor.fosstars.model.value.Language.GO;
import static com.sap.oss.phosphor.fosstars.model.value.Language.JAVA;
import static com.sap.oss.phosphor.fosstars.model.value.Language.JAVASCRIPT;
import static com.sap.oss.phosphor.fosstars.model.value.Language.PHP;
import static com.sap.oss.phosphor.fosstars.model.value.Language.PYTHON;
import static com.sap.oss.phosphor.fosstars.model.value.Language.RUBY;
import static com.sap.oss.phosphor.fosstars.model.value.Language.SCALA;
import static com.sap.oss.phosphor.fosstars.model.value.Language.VISUALBASIC;
import static com.sap.oss.phosphor.fosstars.model.value.PackageManager.COMPOSER;
import static com.sap.oss.phosphor.fosstars.model.value.PackageManager.DOTNET;
import static com.sap.oss.phosphor.fosstars.model.value.PackageManager.GOMODULES;
import static com.sap.oss.phosphor.fosstars.model.value.PackageManager.GRADLE;
import static com.sap.oss.phosphor.fosstars.model.value.PackageManager.MAVEN;
import static com.sap.oss.phosphor.fosstars.model.value.PackageManager.NPM;
import static com.sap.oss.phosphor.fosstars.model.value.PackageManager.PIP;
import static com.sap.oss.phosphor.fosstars.model.value.PackageManager.RUBYGEMS;
import static com.sap.oss.phosphor.fosstars.model.value.PackageManager.YARN;

import com.sap.oss.phosphor.fosstars.model.Feature;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.ValueSet;
import com.sap.oss.phosphor.fosstars.model.subject.oss.GitHubProject;
import com.sap.oss.phosphor.fosstars.model.value.Language;
import com.sap.oss.phosphor.fosstars.model.value.Languages;
import com.sap.oss.phosphor.fosstars.model.value.PackageManager;
import com.sap.oss.phosphor.fosstars.model.value.PackageManagers;
import com.sap.oss.phosphor.fosstars.model.value.ValueHashSet;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * This data provider returns package managers which are used in a project.
 */
public class PackageManagement extends CachedSingleFeatureGitHubDataProvider<PackageManagers> {

  /**
   * A minimal number of characters in a config to consider it valid.
   */
  private static final int ACCEPTABLE_CONFIG_SIZE = 100;

  /**
   * Maps a programming language to its package manager.
   */
  private static final Map<Language, PackageManagers> KNOWN_PACKAGE_MANAGERS
      = new EnumMap<>(Language.class);

  static {
    register(JAVA, MAVEN, GRADLE);
    register(SCALA, MAVEN);
    register(JAVASCRIPT, NPM, YARN);
    register(C_SHARP, DOTNET);
    register(F_SHARP, DOTNET);
    register(VISUALBASIC, DOTNET);
    register(PYTHON, PIP);
    register(RUBY, RUBYGEMS);
    register(PHP, COMPOSER);
    register(GO, GOMODULES);
  }

  /**
   * Maps a package manager to a list of its possible config files.
   *
   * @see <a href="https://help.github.com/en/github/visualizing-repository-data-with-graphs/listing-the-packages-that-a-repository-depends-on">
   *      Listing the packages that a repository depends on</a>
   */
  private static final Map<PackageManager, Predicate<String>[]> CONFIG_FILES_PATTERNS
      = new EnumMap<>(PackageManager.class);

  static {
    register(MAVEN, "pom.xml"::equals);
    register(GRADLE, "build.gradle"::equals);
    register(NPM, "package-lock.json"::equals, "package.json"::equals);
    register(YARN, "yarn.lock"::equals, "package.json"::equals);
    register(DOTNET, ".csproj"::equals, ".vbproj"::equals, ".nuspec"::equals,
        ".vcxproj"::equals, ".fsproj"::equals, "packages.config"::equals);
    register(RUBYGEMS, "Gemfile.lock"::equals, "Gemfile"::equals, ".gemspec"::endsWith);
    register(COMPOSER, "composer.json"::equals, "composer.lock"::equals);
    register(GOMODULES, "go.mod"::equals, "go.sum"::equals);
  }

  /**
   * Adds a number of known package managers for a language.
   *
   * @param language The language.
   * @param list The package managers.
   */
  private static void register(Language language, PackageManager... list) {
    if (KNOWN_PACKAGE_MANAGERS.containsKey(language)) {
      throw new IllegalArgumentException(String.format("%s is already there!", language));
    }
    KNOWN_PACKAGE_MANAGERS.put(language, new PackageManagers(list));
  }

  /**
   * Adds a list of possible config files for a package manager.
   *
   * @param packageManager The package manager.
   * @param matchers A list of matchers for the possible config files.
   */
  private static void register(PackageManager packageManager, Predicate<String>... matchers) {
    if (CONFIG_FILES_PATTERNS.containsKey(packageManager)) {
      throw new IllegalArgumentException(String.format("%s is already there!", packageManager));
    }
    CONFIG_FILES_PATTERNS.put(packageManager, matchers);
  }

  /**
   * Initializes a data provider.
   *
   * @param fetcher An interface to GitHub.
   */
  public PackageManagement(GitHubDataFetcher fetcher) {
    super(fetcher);
  }

  @Override
  protected Feature<PackageManagers> supportedFeature() {
    return PACKAGE_MANAGERS;
  }

  @Override
  protected Value<PackageManagers> fetchValueFor(GitHubProject project) throws IOException {
    logger.info("Looking for package managers ...");
    return packageManagers(project);
  }

  /**
   * Gets a list of package managers that are used in a project.
   *
   * @param project The project.
   * @return A value of the feature
   *         {@link com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures#PACKAGE_MANAGERS}.
   * @throws IOException If something went wrong.
   */
  private Value<PackageManagers> packageManagers(GitHubProject project) throws IOException {
    PackageManagers possiblePackageManagers = new PackageManagers();
    for (Language language : languages(project)) {
      if (KNOWN_PACKAGE_MANAGERS.containsKey(language)) {
        possiblePackageManagers.add(KNOWN_PACKAGE_MANAGERS.get(language));
      }
    }

    PackageManagers packageManagers = new PackageManagers();
    GitHubDataFetcher.localRepositoryFor(project).files(Files::isRegularFile)
        .forEach(path -> {
          for (PackageManager packageManager : possiblePackageManagers) {
            if (isKnownConfigFile(path, packageManager)) {
              packageManagers.add(packageManager);
            }
          }
        });

    return PACKAGE_MANAGERS.value(packageManagers);
  }

  /**
   * Checks if a file looks like a config of a specified package manager.
   *
   * @param path A path to the file.
   * @param packageManager The package manager.
   * @return True if a file looks like a config of the package manager, false otherwise.
   */
  static boolean isKnownConfigFile(Path path, PackageManager packageManager) {
    if (!CONFIG_FILES_PATTERNS.containsKey(packageManager)) {
      return false;
    }

    try {
      if (!Files.isRegularFile(path)) {
        return false;
      }

      if (Files.size(path) < ACCEPTABLE_CONFIG_SIZE) {
        return false;
      }

      for (Predicate<String> matcher : CONFIG_FILES_PATTERNS.get(packageManager)) {
        if (matcher.test(path.getFileName().toString())) {
          return true;
        }
      }
    } catch (IOException e) {
      return false;
    }

    return false;
  }

  /**
   * Returns a programming languages that are used in a project.
   *
   * @param project  The project.
   * @return The languages.
   * @throws IOException If something went wrong.
   */
  Languages languages(GitHubProject project) throws IOException {
    ValueSet values = new ValueHashSet();
    languagesProvider().update(project, values);

    Optional<Value<Languages>> something = values.of(LANGUAGES);
    if (!something.isPresent()) {
      return Languages.empty();
    }

    Value<Languages> value = something.get();
    return value.get();
  }

  /**
   * Creates an instance of {@link ProgrammingLanguages}.
   */
  ProgrammingLanguages languagesProvider() {
    return new ProgrammingLanguages(fetcher);
  }
}