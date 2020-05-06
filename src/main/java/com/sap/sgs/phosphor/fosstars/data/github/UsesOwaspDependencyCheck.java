package com.sap.sgs.phosphor.fosstars.data.github;

import com.sap.sgs.phosphor.fosstars.model.Feature;
import com.sap.sgs.phosphor.fosstars.model.Value;
import com.sap.sgs.phosphor.fosstars.model.feature.BooleanFeature;
import com.sap.sgs.phosphor.fosstars.tool.github.GitHubProject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Optional;
import org.apache.maven.model.Model;
import org.apache.maven.model.Plugin;
import org.apache.maven.model.ReportPlugin;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

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

    Model model = readModel(content.get());

    if (model.getBuild() != null) {
      for (Plugin plugin : model.getBuild().getPlugins()) {
        if (isDependencyCheck(plugin)) {
          return true;
        }
      }
    }

    if (model.getReporting() != null) {
      for (ReportPlugin plugin : model.getReporting().getPlugins()) {
        if (isDependencyCheck(plugin)) {
          return true;
        }
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
   * Parses a pom.xml file.
   *
   * @param content The content of the pom.xml file.
   * @return A {@link Model} which represents the pom.xml file.
   * @throws IOException If something went wrong.
   */
  private static Model readModel(InputStream content) throws IOException {
    try {
      return new MavenXpp3Reader().read(content);
    } catch (XmlPullParserException e) {
      throw new IOException(e);
    }
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
