package com.sap.sgs.phosphor.fosstars.data.github;

import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.SCANS_FOR_VULNERABLE_DEPENDENCIES;

import com.sap.sgs.phosphor.fosstars.model.Value;
import com.sap.sgs.phosphor.fosstars.model.ValueSet;
import com.sap.sgs.phosphor.fosstars.model.value.BooleanValue;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import org.apache.maven.model.Model;
import org.apache.maven.model.Plugin;
import org.apache.maven.model.ReportPlugin;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.kohsuke.github.GHContent;
import org.kohsuke.github.GHFileNotFoundException;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;

/**
 * This data provider checks if an open-source project uses OWASP Dependency Check Maven plugin to
 * scan dependencies for known vulnerabilities. If it does, the provider sets {@link
 * com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures#SCANS_FOR_VULNERABLE_DEPENDENCIES}
 * to true.
 */
public class UsesOwaspDependencyCheck extends AbstractGitHubDataProvider {

  /**
   * Initializes a data provider.
   *
   * @param where A GitHub organization of user name.
   * @param name A name of a repository.
   * @param github An interface to the GitHub API.
   */
  public UsesOwaspDependencyCheck(String where, String name, GitHub github) {
    super(where, name, github);
  }

  @Override
  public UsesOwaspDependencyCheck update(ValueSet values) throws IOException {
    Objects.requireNonNull(values, "Hey! Values can't be null!");
    System.out.println("[+] Figuring out if the project uses OWASP Dependency Check ...");

    GHRepository repository = github.getRepository(path);
    boolean answer = checkMaven(repository) || checkGradle(repository);
    Value<Boolean> value = new BooleanValue(SCANS_FOR_VULNERABLE_DEPENDENCIES, answer);
    values.update(value);

    return this;
  }

  /**
   * Checks if a project uses OWASP Dependency Check Maven plugin.
   *
   * @param repository The project's repository.
   * @return True if the project uses the plugin, false otherwise.
   */
  private boolean checkMaven(GHRepository repository) throws IOException {
    GHContent content;
    try {
      content = repository.getFileContent("pom.xml");
    } catch (GHFileNotFoundException e) {
      return false;
    }

    if (content == null || !content.isFile()) {
      return false;
    }

    Model model = readModel(content);

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
  private boolean checkGradle(GHRepository repository) {
    // TODO: implement
    return false;
  }

  /**
   * Parses a pom.xml file.
   *
   * @param content The content of the pom.xml file.
   * @return A {@link Model} which represents the pom.xml file.
   * @throws IOException If something went wrong.
   */
  private static Model readModel(GHContent content) throws IOException {
    try (InputStream is = content.read()) {
      try {
        return new MavenXpp3Reader().read(is);
      } catch (XmlPullParserException e) {
        throw new IOException(e);
      }
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
