package com.sap.sgs.phosphor.fosstars.data.github;

import static com.sap.sgs.phosphor.fosstars.maven.MavenUtils.readModel;

import com.sap.sgs.phosphor.fosstars.model.Feature;
import com.sap.sgs.phosphor.fosstars.model.Value;
import com.sap.sgs.phosphor.fosstars.model.feature.BooleanFeature;
import com.sap.sgs.phosphor.fosstars.tool.github.GitHubProject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Optional;
import org.apache.maven.model.BuildBase;
import org.apache.maven.model.Model;
import org.apache.maven.model.Plugin;
import org.apache.maven.model.PluginManagement;
import org.apache.maven.model.Profile;
import org.apache.maven.model.ReportPlugin;
import org.apache.maven.model.Reporting;

/**
 * This data provider checks if an open-source project uses OWASP Dependency Check Maven plugin to
 * scan dependencies for known vulnerabilities.
 */
public class UsesOwaspDependencyCheck extends CachedSingleFeatureGitHubDataProvider {

  /**
   * A feature which is filled out by the provider.
   */
  static final Feature<Boolean> USES_OWASP_DEPENDENCY_CHECK
      = new BooleanFeature("If a project uses OWASP Dependency Check");

  /**
   * Initializes a data provider.
   *
   * @param fetcher An interface to GitHub.
   */
  public UsesOwaspDependencyCheck(GitHubDataFetcher fetcher) {
    super(fetcher);
  }

  @Override
  protected Feature supportedFeature() {
    return USES_OWASP_DEPENDENCY_CHECK;
  }

  @Override
  protected Value<Boolean> fetchValueFor(GitHubProject project) throws IOException {
    logger.info("Figuring out if the project uses OWASP Dependency Check ...");
    LocalRepository repository = fetcher.localRepositoryFor(project);
    boolean answer = checkMaven(repository) || checkGradle(repository);
    return USES_OWASP_DEPENDENCY_CHECK.value(answer);
  }

  /**
   * Checks if a project uses OWASP Dependency Check Maven plugin.
   *
   * @param repository The project's repository.
   * @return True if the project uses the plugin, false otherwise.
   */
  private boolean checkMaven(LocalRepository repository) throws IOException {
    Optional<InputStream> content = repository.read("pom.xml");

    if (!content.isPresent()) {
      return false;
    }

    Model model;
    try (InputStream is = content.get()) {
      model = readModel(is);
    }

    // first, check if the plugin is used in the build section
    BuildBase build = model.getBuild();
    if (hasDependencyCheckIn(build)) {
      return true;
    }

    // next, check if the plugin is used in the reporting section
    Reporting reporting = model.getReporting();
    if (hasDependencyCheckIn(reporting)) {
      return true;
    }

    // finally, check if the plugin is used in one of the profiles
    if (model.getProfiles() != null) {
      for (Profile profile : model.getProfiles()) {
        build = profile.getBuild();
        if (hasDependencyCheckIn(build)) {
          return true;
        }

        reporting = profile.getReporting();
        if (hasDependencyCheckIn(reporting)) {
          return true;
        }
      }
    }

    return false;
  }

  /**
   * Checks if OWASP Dependency Check plugin is used in the build section of POM file.
   *
   * @param build The build section of POM file.
   * @return True if the plugin was found, false otherwise.
   */
  private static boolean hasDependencyCheckIn(BuildBase build) {
    if (build == null) {
      return false;
    }

    List<Plugin> plugins = build.getPlugins();
    if (hasDependencyCheckIn(plugins)) {
      return true;
    }

    PluginManagement pluginManagement = build.getPluginManagement();
    if (pluginManagement == null) {
      return false;
    }

    plugins = pluginManagement.getPlugins();
    return hasDependencyCheckIn(plugins);
  }

  /**
   * Check if one of the provider plugins is OWASP Dependency Check plugin.
   *
   * @param plugins The plugins to be checked.
   * @return True is OWASP Dependency Check plugin was found, false otherwise.
   */
  private static boolean hasDependencyCheckIn(List<Plugin> plugins) {
    if (plugins == null) {
      return false;
    }

    for (Plugin plugin : plugins) {
      if (isDependencyCheck(plugin)) {
        return true;
      }
    }

    return false;
  }

  /**
   * Checks if the reporting section of POM file includes OWASP Dependency Check plugin.
   *
   * @param reporting The reporting section to be checked.
   * @return True if the plugin was found, false otherwise.
   */
  private static boolean hasDependencyCheckIn(Reporting reporting) {
    if (reporting == null) {
      return false;
    }

    for (ReportPlugin plugin : reporting.getPlugins()) {
      if (isDependencyCheck(plugin)) {
        return true;
      }
    }

    return false;
  }

  /**
   * Checks if a project uses OWASP Dependency Check Gradle plugin.
   *
   * @param repository The project's repository.
   * @return True if the project uses the plugin, false otherwise.
   */
  private boolean checkGradle(LocalRepository repository) throws IOException {
    Optional<InputStream> content = repository.read("build.gradle");

    if (!content.isPresent()) {
      return false;
    }

    try (BufferedReader reader = new BufferedReader(new InputStreamReader(content.get()))) {
      String line;
      while ((line = reader.readLine()) != null) {
        if (line.trim().contains("org.owasp:dependency-check-gradle")) {
          return true;
        }
      }
    }

    return false;
  }

  /**
   * Check if a plugin is OWASP Dependency Check plugin.
   *
   * @param plugin The plugin to be checked.
   * @return True if the plugin is OWASP Dependency Check plugin, false otherwise.
   */
  private static boolean isDependencyCheck(Plugin plugin) {
    return "org.owasp".equals(plugin.getGroupId())
        && "dependency-check-maven".equals(plugin.getArtifactId());
  }

  /**
   * Check if a report plugin is OWASP Dependency Check plugin.
   *
   * @param plugin The report plugin to be checked.
   * @return True if the report plugin is OWASP Dependency Check plugin, false otherwise.
   */
  private static boolean isDependencyCheck(ReportPlugin plugin) {
    return "org.owasp".equals(plugin.getGroupId())
        && "dependency-check-maven".equals(plugin.getArtifactId());
  }
}
