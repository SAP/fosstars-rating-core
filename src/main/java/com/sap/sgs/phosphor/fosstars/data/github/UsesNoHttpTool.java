package com.sap.sgs.phosphor.fosstars.data.github;

import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.USES_NOHTTP;

import com.sap.sgs.phosphor.fosstars.model.Feature;
import com.sap.sgs.phosphor.fosstars.model.Value;
import com.sap.sgs.phosphor.fosstars.tool.github.GitHubProject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;
import org.apache.maven.model.Plugin;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.kohsuke.github.GHContent;
import org.kohsuke.github.GHFileNotFoundException;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;

/**
 * This data provider checks if an open-source project uses
 * <a href="https://github.com/spring-io/nohttp">nohttp</a> tool to make sure that
 * plain HTTP is not used.
 * The provider fills out the
 * {@link com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures#USES_NOHTTP} feature.
 */
public class UsesNoHttpTool extends CachedSingleFeatureGitHubDataProvider {

  /**
   * Initializes a data provider.
   *
   * @param github An interface to the GitHub API.
   */
  public UsesNoHttpTool(GitHub github) {
    super(github);
  }

  @Override
  protected Feature supportedFeature() {
    return USES_NOHTTP;
  }

  @Override
  protected Value fetchValueFor(GitHubProject project) throws IOException {
    logger.info("Figuring out if the project uses NoHTTP ...");

    GHRepository repository = gitHubDataFetcher().repositoryFor(project, github);
    return USES_NOHTTP.value(checkMaven(repository) || checkGradle(repository));
  }

  /**
   * Checks if a project uses NoHTTP with Maven.
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
        if (isNoHttp(plugin)) {
          return true;
        }
      }
    }

    return false;
  }

  /**
   * Checks if a project uses nohttp with Gradle.
   *
   * @param repository The project's repository.
   * @return True if the project uses the plugin, false otherwise.
   */
  private boolean checkGradle(GHRepository repository) throws IOException {
    GHContent content;
    try {
      content = repository.getFileContent("build.gradle");
    } catch (GHFileNotFoundException e) {
      return false;
    }

    if (content == null || !content.isFile()) {
      return false;
    }

    try (BufferedReader reader = new BufferedReader(new InputStreamReader(content.read()))) {
      String line;
      while ((line = reader.readLine()) != null) {
        if (line.trim().startsWith("id \"io.spring.nohttp\"")) {
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
   * Check if a plugin is maven-checkstyle-plugin with nohttp.
   *
   * @param plugin The plugin to be checked.
   * @return True if the plugin runs nohttp, false otherwise.
   * @see <a href="https://github.com/spring-io/nohttp/tree/master/samples/nohttp-maven-sample">
   *   Demo of using nohttp checkstyle with Maven</a>
   */
  private static boolean isNoHttp(Plugin plugin) {

    // first, check if the plugin is maven-checkstyle-plugin
    if (!"org.apache.maven.plugins".equals(plugin.getGroupId())
        || !"maven-checkstyle-plugin".equals(plugin.getArtifactId())) {

      return false;
    }

    // next, check if one of the dependencies is nohttp
    for (Dependency dependency : plugin.getDependencies()) {
      if ("io.spring.nohttp".equals(dependency.getGroupId())
          && "nohttp-checkstyle".equals(dependency.getArtifactId())) {

        return true;
      }
    }

    return false;
  }
}
