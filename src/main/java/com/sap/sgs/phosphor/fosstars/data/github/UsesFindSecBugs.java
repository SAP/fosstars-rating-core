package com.sap.sgs.phosphor.fosstars.data.github;

import static com.sap.sgs.phosphor.fosstars.maven.MavenUtils.readModel;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.USES_FIND_SEC_BUGS;

import com.sap.sgs.phosphor.fosstars.model.Feature;
import com.sap.sgs.phosphor.fosstars.model.Value;
import com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures;
import com.sap.sgs.phosphor.fosstars.tool.github.GitHubProject;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import org.apache.maven.model.Model;
import org.apache.maven.model.Plugin;
import org.codehaus.plexus.util.xml.Xpp3Dom;

/**
 * <p>This data provider checks if an open-source project uses
 * <a href="https://find-sec-bugs.github.io/">FindSecBugs</a>.</p>
 *
 * <p>The provider fills out the {@link OssFeatures#USES_FIND_SEC_BUGS} feature.</p>
 */
public class UsesFindSecBugs extends CachedSingleFeatureGitHubDataProvider {

  /**
   * Initializes a data provider.
   *
   * @param fetcher An interface to GitHub.
   */
  public UsesFindSecBugs(GitHubDataFetcher fetcher) {
    super(fetcher);
  }

  @Override
  protected Feature supportedFeature() {
    return USES_FIND_SEC_BUGS;
  }

  @Override
  protected Value fetchValueFor(GitHubProject project) throws IOException {
    logger.info("Figuring out if the project uses FindSecBugs ...");
    LocalRepository repository = fetcher.localRepositoryFor(project);
    boolean answer = checkMaven(repository);
    return USES_FIND_SEC_BUGS.value(answer);
  }

  /**
   * Checks if a repository uses FindSecBugs with Maven.
   * @param repository The repository.
   * @return True if the project uses FindSecBugs, false otherwise.
   * @throws IOException If something went wrong
   * @see <a href="https://github.com/find-sec-bugs/find-sec-bugs/wiki/Maven-configuration">Maven configuration</a>
   */
  private boolean checkMaven(LocalRepository repository) throws IOException {
    Optional<InputStream> content = repository.read("pom.xml");

    if (!content.isPresent()) {
      return false;
    }

    Model model = readModel(content.get());

    if (model.getBuild() != null) {
      for (Plugin plugin : model.getBuild().getPlugins()) {
        if (isFindSecBugs(plugin)) {
          return true;
        }
      }
    }

    return false;
  }

  private static boolean isFindSecBugs(Plugin plugin) {

    // first, check if the plugin is com.github.spotbugs:spotbugs-maven-plugin
    if (!"com.github.spotbugs".equals(plugin.getGroupId())
        || !"spotbugs-maven-plugin".equals(plugin.getArtifactId())) {

      return false;
    }

    if (plugin.getConfiguration() == null) {
      return false;
    }

    if (plugin.getConfiguration() instanceof Xpp3Dom == false) {
      return false;
    }
    Xpp3Dom configuration = (Xpp3Dom) plugin.getConfiguration();

    return hasFindSecBugs(configuration);
  }

  /**
   * Returns true if an element is
   * the "com.h3xstream.findsecbugs:findsecbugs-plugin" plugin, false otherwise.
   */
  private static boolean isFindSecBugs(Xpp3Dom element) {
    if (!"plugin".equals(element.getName())) {
      return false;
    }

    Xpp3Dom groupId = element.getChild("groupId");
    if (groupId == null || !"com.h3xstream.findsecbugs".equals(groupId.getValue())) {
      return false;
    }

    Xpp3Dom artifactId = element.getChild("artifactId");
    return artifactId != null && "findsecbugs-plugin".equals(artifactId.getValue());
  }

  /**
   * Returns true if a configuration contains FindSecBugs plugin, false otherwise.
   */
  private static boolean hasFindSecBugs(Xpp3Dom configuration) {
    Xpp3Dom plugins = configuration.getChild("plugins");
    if (plugins == null) {
      return false;
    }

    for (Xpp3Dom child : plugins.getChildren()) {
      if (isFindSecBugs(child)) {
        return true;
      }
    }

    return false;
  }

}
