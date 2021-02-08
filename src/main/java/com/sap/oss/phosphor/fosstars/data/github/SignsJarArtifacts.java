package com.sap.oss.phosphor.fosstars.data.github;

import static com.sap.oss.phosphor.fosstars.maven.MavenUtils.browse;
import static com.sap.oss.phosphor.fosstars.maven.MavenUtils.readModel;
import static com.sap.oss.phosphor.fosstars.maven.ModelVisitor.Location.BUILD;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.SIGNS_ARTIFACTS;

import com.sap.oss.phosphor.fosstars.maven.AbstractModelVisitor;
import com.sap.oss.phosphor.fosstars.model.Feature;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures;
import com.sap.oss.phosphor.fosstars.model.subject.oss.GitHubProject;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.Set;
import org.apache.maven.model.Model;
import org.apache.maven.model.Plugin;

/**
 * <p>This data provider checks if an open-source project signs its JAR artifacts. In particular,
 * the data provider checks if a project uses
 * <a href="http://maven.apache.org/plugins/maven-gpg-plugin/">Maven GPG plugin</a>.</p>
 * <p>The provider fills out the {@link OssFeatures#SIGNS_ARTIFACTS} feature.</p>
 */
public class SignsJarArtifacts extends CachedSingleFeatureGitHubDataProvider<Boolean> {

  /**
   * Initializes a data provider.
   *
   * @param fetcher An interface to GitHub.
   */
  public SignsJarArtifacts(GitHubDataFetcher fetcher) {
    super(fetcher);
  }

  @Override
  protected Feature<Boolean> supportedFeature() {
    return SIGNS_ARTIFACTS;
  }

  @Override
  protected Value<Boolean> fetchValueFor(GitHubProject project) throws IOException {
    logger.info("Figuring out if the project signs jar files ...");
    LocalRepository repository = GitHubDataFetcher.localRepositoryFor(project);
    boolean answer = checkMaven(repository);
    return SIGNS_ARTIFACTS.value(answer);
  }

  /**
   * Checks if a project uses Maven GPG plugin.
   *
   * @param repository The project's repository.
   * @return True if the project uses Maven GPG plugin, false otherwise.
   * @throws IOException If something went wrong.
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
   * Checks a plugin is the Maven GPG plugin.
   *
   * @param plugin The plugin to be checked.
   * @return True if the plugin is the Maven GPG plugin, false otherwise.
   */
  private static boolean isMavenGpg(Plugin plugin) {
    return plugin != null
        && "org.apache.maven.plugins".equals(plugin.getGroupId())
        && "maven-gpg-plugin".equals(plugin.getArtifactId());
  }

  /**
   * Creates a new visitor for searching for Maven GPG plugin in a build section of a POM file.
   */
  private static Visitor withVisitor() {
    return new Visitor();
  }

  /**
   * A visitor for searching for Maven GPG plugin in a build section of a POM file.
   */
  private static class Visitor extends AbstractModelVisitor {

    /**
     * This flag shows whether Maven GPG plugin was found in a build section of a POM file or not.
     */
    private boolean result = false;

    @Override
    public void accept(Plugin plugin, Set<Location> locations) {
      if (isMavenGpg(plugin) && locations.contains(BUILD)) {
        result = true;
      }
    }

  }

}
