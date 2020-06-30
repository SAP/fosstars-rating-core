package com.sap.sgs.phosphor.fosstars.data.github;

import static com.sap.sgs.phosphor.fosstars.maven.MavenUtils.readModel;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.USES_NOHTTP;

import com.sap.sgs.phosphor.fosstars.model.Feature;
import com.sap.sgs.phosphor.fosstars.model.Value;
import com.sap.sgs.phosphor.fosstars.tool.github.GitHubProject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import org.apache.maven.model.BuildBase;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;
import org.apache.maven.model.Plugin;
import org.apache.maven.model.Profile;
import org.kohsuke.github.GHContent;
import org.kohsuke.github.GHFileNotFoundException;
import org.kohsuke.github.GHRepository;

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
   * @param fetcher An interface to GitHub.
   */
  public UsesNoHttpTool(GitHubDataFetcher fetcher) {
    super(fetcher);
  }

  @Override
  protected Feature supportedFeature() {
    return USES_NOHTTP;
  }

  @Override
  protected Value fetchValueFor(GitHubProject project) throws IOException {
    logger.info("Figuring out if the project uses nohttp ...");

    GHRepository repository = fetcher.repositoryFor(project);
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

    Model model = readModel(content.read());

    BuildBase build = model.getBuild();
    List<Profile> profiles = model.getProfiles();

    return hasNoHttpIn(build) || hasNoHttpIn(profiles);
  }

  /**
   * Checks if a build section in POM file runs the nohttp tool.
   *
   * @param build The build section to be checked.
   * @return True if the build section runs the tool, false otherwise.
   */
  private static boolean hasNoHttpIn(BuildBase build) {
    return build != null && build.getPlugins().stream().anyMatch(UsesNoHttpTool::isNoHttp);
  }

  /**
   * Checks if one of the profiles in POM file runs the nohttp tool.
   *
   * @param profiles The profiles to be checked.
   * @return True if at least one of the profiles runs the tool, false otherwise.
   */
  private static boolean hasNoHttpIn(List<Profile> profiles) {
    return profiles != null && profiles.stream().anyMatch(UsesNoHttpTool::hasNoHttpIn);
  }

  /**
   * Checks if a profile in POM file runs the nohttp tool.
   *
   * @param profile The profile to be checked.
   * @return True if the profile runs the tool, false otherwise.
   */
  private static boolean hasNoHttpIn(Profile profile) {
    if (profile == null) {
      return false;
    }

    BuildBase build = profile.getBuild();
    return hasNoHttpIn(build);
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
