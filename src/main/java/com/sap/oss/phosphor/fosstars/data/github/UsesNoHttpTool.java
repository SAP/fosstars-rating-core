package com.sap.oss.phosphor.fosstars.data.github;

import static com.sap.oss.phosphor.fosstars.maven.MavenUtils.browse;
import static com.sap.oss.phosphor.fosstars.maven.MavenUtils.readModel;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_NOHTTP;

import com.sap.oss.phosphor.fosstars.maven.AbstractModelVisitor;
import com.sap.oss.phosphor.fosstars.model.Feature;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.subject.oss.GitHubProject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Optional;
import java.util.Set;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;
import org.apache.maven.model.Plugin;

/**
 * This data provider checks if an open-source project uses
 * <a href="https://github.com/spring-io/nohttp">nohttp</a> tool to make sure that
 * plain HTTP is not used.
 * The provider fills out the
 * {@link com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures#USES_NOHTTP} feature.
 */
public class UsesNoHttpTool extends CachedSingleFeatureGitHubDataProvider<Boolean> {

  /**
   * Initializes a data provider.
   *
   * @param fetcher An interface to GitHub.
   */
  public UsesNoHttpTool(GitHubDataFetcher fetcher) {
    super(fetcher);
  }

  @Override
  protected Feature<Boolean> supportedFeature() {
    return USES_NOHTTP;
  }

  @Override
  protected Value<Boolean> fetchValueFor(GitHubProject project) throws IOException {
    logger.info("Figuring out if the project uses nohttp ...");

    LocalRepository repository = GitHubDataFetcher.localRepositoryFor(project);
    return USES_NOHTTP.value(checkMaven(repository) || checkGradle(repository));
  }

  /**
   * Checks if a project uses NoHTTP with Maven.
   *
   * @param repository The project's repository.
   * @return True if the project uses the plugin, false otherwise.
   */
  private boolean checkMaven(LocalRepository repository) throws IOException {
    Optional<InputStream> content = repository.read("pom.xml");

    if (!content.isPresent()) {
      return false;
    }

    try (InputStream is = content.get()) {
      Model model = readModel(is);
      return browse(model, withVisitor()).result;

    }
  }

  /**
   * Checks if a project uses nohttp with Gradle.
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

  /**
   * Creates a new visitor for searching the nohttp tool.
   */
  private static Visitor withVisitor() {
    return new Visitor();
  }

  /**
   * A visitor for searching the nohttp tool.
   */
  private static class Visitor extends AbstractModelVisitor {

    /**
     * This flag shows whether the nohttp tool was found in a POM file or not.
     */
    private boolean result;

    @Override
    public void accept(Plugin plugin, Set<Location> locations) {
      if (isNoHttp(plugin)) {
        result = true;
      }
    }
  }
}
