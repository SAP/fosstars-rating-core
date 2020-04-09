package com.sap.sgs.phosphor.fosstars.data.github;

import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.LANGUAGES;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.PACKAGE_MANAGERS;
import static com.sap.sgs.phosphor.fosstars.model.value.Language.C_SHARP;
import static com.sap.sgs.phosphor.fosstars.model.value.Language.F_SHARP;
import static com.sap.sgs.phosphor.fosstars.model.value.Language.JAVA;
import static com.sap.sgs.phosphor.fosstars.model.value.Language.JAVASCRIPT;
import static com.sap.sgs.phosphor.fosstars.model.value.Language.PHP;
import static com.sap.sgs.phosphor.fosstars.model.value.Language.PYTHON;
import static com.sap.sgs.phosphor.fosstars.model.value.Language.RUBY;
import static com.sap.sgs.phosphor.fosstars.model.value.Language.SCALA;
import static com.sap.sgs.phosphor.fosstars.model.value.Language.VISUALBASIC;
import static com.sap.sgs.phosphor.fosstars.model.value.PackageManager.COMPOSER;
import static com.sap.sgs.phosphor.fosstars.model.value.PackageManager.DOTNET;
import static com.sap.sgs.phosphor.fosstars.model.value.PackageManager.GRADLE;
import static com.sap.sgs.phosphor.fosstars.model.value.PackageManager.MAVEN;
import static com.sap.sgs.phosphor.fosstars.model.value.PackageManager.NPM;
import static com.sap.sgs.phosphor.fosstars.model.value.PackageManager.PIP;
import static com.sap.sgs.phosphor.fosstars.model.value.PackageManager.RUBYGEMS;
import static com.sap.sgs.phosphor.fosstars.model.value.PackageManager.YARN;

import com.sap.sgs.phosphor.fosstars.model.Value;
import com.sap.sgs.phosphor.fosstars.model.ValueSet;
import com.sap.sgs.phosphor.fosstars.model.value.Language;
import com.sap.sgs.phosphor.fosstars.model.value.Languages;
import com.sap.sgs.phosphor.fosstars.model.value.PackageManager;
import com.sap.sgs.phosphor.fosstars.model.value.PackageManagers;
import com.sap.sgs.phosphor.fosstars.model.value.ValueHashSet;
import java.io.IOException;
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import org.kohsuke.github.GHContent;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;

/**
 * This data provider returns package managers which are used in a project.
 */
public class PackageManagement extends AbstractGitHubDataProvider {

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
   * @param where A GitHub organization of user name.
   * @param name A name of a repository.
   * @param github An interface to the GitHub API.
   */
  public PackageManagement(String where, String name, GitHub github) {
    super(where, name, github);
  }

  @Override
  public PackageManagement update(ValueSet values) throws IOException {
    Objects.requireNonNull(values, "Hey! Values can't be null!");
    logger.info("Looking for package managers ...");
    values.update(packageManagers());
    return this;
  }

  /**
   * Gets a list of package managers that are used in a project.
   *
   * @return A value of the feature
   *         {@link com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures#PACKAGE_MANAGERS}.
   * @throws IOException If something went wrong.
   */
  private Value<PackageManagers> packageManagers() throws IOException {
    Optional<Value> something = cache().get(url, PACKAGE_MANAGERS);
    if (something.isPresent()) {
      return something.get();
    }

    PackageManagers possiblePackageManagers = new PackageManagers();
    for (Language language : languages().get()) {
      if (KNOWN_PACKAGE_MANAGERS.containsKey(language)) {
        possiblePackageManagers.add(KNOWN_PACKAGE_MANAGERS.get(language));
      }
    }

    GHRepository repository = github.getRepository(path);
    PackageManagers packageManagers = new PackageManagers();
    for (GHContent content : repository.getDirectoryContent("/")) {
      if (!content.isFile()) {
        continue;
      }

      possiblePackageManagers.list().forEach(packageManager -> {
        if (isKnownConfigFile(content, packageManager)) {
          packageManagers.add(packageManager);
        }
      });
    }

    Value<PackageManagers> value = PACKAGE_MANAGERS.value(packageManagers);
    cache().put(url, value);

    return value;
  }

  /**
   * Checks if a file from a repository looks like a config for a package manager.
   *
   * @param content The file.
   * @param packageManager The package manager.
   * @return True if the file is a config for the package manager, false otherwise.
   */
  static boolean isKnownConfigFile(GHContent content, PackageManager packageManager) {
    if (!CONFIG_FILES_PATTERNS.containsKey(packageManager)) {
      return false;
    }

    for (Predicate<String> matcher : CONFIG_FILES_PATTERNS.get(packageManager)) {
      if (matcher.test(content.getName()) && content.getSize() >= ACCEPTABLE_CONFIG_SIZE) {
        return true;
      }
    }

    return false;
  }

  /**
   * Returns a programming languages that are used in a project.
   *
   * @return The languages.
   * @throws IOException If something went wrong.
   */
  Languages languages() throws IOException {
    ValueSet values = new ValueHashSet();
    languagesProvider().update(values);

    Optional<Value> something = values.of(LANGUAGES);
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
    return new ProgrammingLanguages(where, name, github);
  }

  /**
   * This entry point is for demo and testing purposes.
   */
  public static void main(String... args) throws IOException {
    GitHub github = GitHub.connectUsingOAuth(System.getenv("TOKEN"));
    PackageManagement provider = new PackageManagement(
        "spring-projects", "spring-security", github);
    ValueSet values = new ValueHashSet();
    provider.update(values);
    System.out.println("package managers: " + values.of(PACKAGE_MANAGERS).get());
  }

}
