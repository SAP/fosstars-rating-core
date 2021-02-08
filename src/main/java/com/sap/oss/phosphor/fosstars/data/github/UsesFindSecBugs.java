package com.sap.oss.phosphor.fosstars.data.github;

import static com.sap.oss.phosphor.fosstars.maven.MavenUtils.browse;
import static com.sap.oss.phosphor.fosstars.maven.MavenUtils.readModel;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_FIND_SEC_BUGS;

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
import org.apache.maven.model.ReportPlugin;
import org.codehaus.plexus.util.xml.Xpp3Dom;

/**
 * <p>This data provider checks if an open-source project uses
 * <a href="https://find-sec-bugs.github.io/">FindSecBugs</a>.</p>
 *
 * <p>The provider fills out the {@link OssFeatures#USES_FIND_SEC_BUGS} feature.</p>
 */
public class UsesFindSecBugs extends CachedSingleFeatureGitHubDataProvider<Boolean> {

  /**
   * Initializes a data provider.
   *
   * @param fetcher An interface to GitHub.
   */
  public UsesFindSecBugs(GitHubDataFetcher fetcher) {
    super(fetcher);
  }

  @Override
  protected Feature<Boolean> supportedFeature() {
    return USES_FIND_SEC_BUGS;
  }

  @Override
  protected Value<Boolean> fetchValueFor(GitHubProject project) throws IOException {
    logger.info("Figuring out if the project uses FindSecBugs ...");
    LocalRepository repository = GitHubDataFetcher.localRepositoryFor(project);
    boolean answer = checkMaven(repository);
    return USES_FIND_SEC_BUGS.value(answer);
  }

  /**
   * Checks if a repository uses FindSecBugs with Maven.
   *
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

    try (InputStream is = content.get()) {
      Model model = readModel(is);
      return browse(model, withVisitor()).result;
    }
  }

  /**
   * Check if a plugin runs FindSecBugs.
   *
   * @param plugin The plugin to be checked.
   * @return True if the plugin runs FindSecBugs, false otherwise.
   */
  private static boolean isFindSecBugs(Plugin plugin) {

    // first, check if the plugin is com.github.spotbugs:spotbugs-maven-plugin
    if (!"com.github.spotbugs".equals(plugin.getGroupId())
        || !"spotbugs-maven-plugin".equals(plugin.getArtifactId())) {

      return false;
    }

    // then, check if the plugin contains a configuration that uses FindSedBugs
    Object configuration = plugin.getConfiguration();
    return isFindSecBugs(configuration);
  }

  /**
   * Check if a report plugin runs FindSecBugs.
   *
   * @param plugin The plugin to be checked.
   * @return True if the plugin runs FindSecBugs, false otherwise.
   */
  private static boolean isFindSecBugs(ReportPlugin plugin) {

    // first, check if the plugin is com.github.spotbugs:spotbugs-maven-plugin
    if (!"com.github.spotbugs".equals(plugin.getGroupId())
        || !"spotbugs-maven-plugin".equals(plugin.getArtifactId())) {

      return false;
    }

    // then, check if the plugin contains a configuration that uses FindSedBugs
    Object configuration = plugin.getConfiguration();
    return isFindSecBugs(configuration);
  }

  /**
   * Checks if an object is a configuration of FindSecBugs plugin.
   *
   * @param object The object to be checked.
   * @return True if the object is a configuration of FindSecBugs plugin, false otherwise.
   */
  private static boolean isFindSecBugs(Object object) {
    if (object instanceof Xpp3Dom == false) {
      return false;
    }
    Xpp3Dom configuration = (Xpp3Dom) object;

    return hasFindSecBugs(configuration);
  }

  /**
   * Checks if an element is FindSecBugs.
   *
   * @param element The element to be checked.
   * @return True if an element is
   *         the "com.h3xstream.findsecbugs:findsecbugs-plugin" plugin, false otherwise.
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
   * Checks if a configuration has FindSecBugs.
   *
   * @param configuration The configuration to be checked.
   * @return True if a configuration contains FindSecBugs plugin, false otherwise.
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

  /**
   * Creates a visitor for searching FindSecBugs in a POM file.
   */
  private static Visitor withVisitor() {
    return new Visitor();
  }

  /**
   * A visitor for searching FindSecBugs in a POM file.
   */
  private static class Visitor extends AbstractModelVisitor {

    /**
     * A visitor for searching FindSecBugs in a POM file.
     */
    private boolean result = false;

    @Override
    public void accept(Plugin plugin, Set<Location> locations) {
      if (isFindSecBugs(plugin)) {
        result = true;
      }
    }

    @Override
    public void accept(ReportPlugin plugin, Set<Location> locations) {
      if (isFindSecBugs(plugin)) {
        result = true;
      }
    }
  }

}
