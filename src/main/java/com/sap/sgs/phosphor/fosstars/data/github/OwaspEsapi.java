package com.sap.sgs.phosphor.fosstars.data.github;

import static com.sap.sgs.phosphor.fosstars.maven.MavenUtils.browse;
import static com.sap.sgs.phosphor.fosstars.maven.MavenUtils.readModel;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.USES_OWASP_ESAPI;

import com.sap.sgs.phosphor.fosstars.maven.AbstractModelVisitor;
import com.sap.sgs.phosphor.fosstars.model.Feature;
import com.sap.sgs.phosphor.fosstars.model.Value;
import com.sap.sgs.phosphor.fosstars.tool.github.GitHubProject;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.Set;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;

/**
 * The data provider checks if a project uses OWASP Enterprise Security API (ESAPI) and fills out
 * the {@link com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures#USES_OWASP_ESAPI} feature.
 */
public class OwaspEsapi extends CachedSingleFeatureGitHubDataProvider {

  /**
   * Initializes a data provider.
   *
   * @param fetcher An interface to GitHub.
   */
  public OwaspEsapi(GitHubDataFetcher fetcher) {
    super(fetcher);
  }

  @Override
  protected Feature supportedFeature() {
    return USES_OWASP_ESAPI;
  }

  @Override
  protected Value fetchValueFor(GitHubProject project) throws IOException {
    logger.info("Figuring out if the project uses OWASP Enterprise Security API (ESAPI) ...");
    LocalRepository repository = GitHubDataFetcher.localRepositoryFor(project);
    return USES_OWASP_ESAPI.value(usesOwaspEsapiIn(repository));
  }

  /**
   * Checks if a project uses OWASP ESAPI.
   *
   * @param repository Project's repository.
   * @return True if the project uses OWASP ESAPI, false otherwise.
   */
  private boolean usesOwaspEsapiIn(LocalRepository repository) throws IOException {
    return usesOwaspEsapiInMaven(repository) || usesOwaspEsapiInGradle(repository);
  }

  /**
   * Checks if a Maven project uses OWASP ESAPI.
   *
   * @param repository Project's repository.
   * @return True if the project uses OWASP ESAPI, false otherwise.
   */
  private boolean usesOwaspEsapiInMaven(LocalRepository repository) throws IOException {
    Optional<InputStream> content = repository.read("pom.xml");

    if (!content.isPresent()) {
      return false;
    }

    try (InputStream is = content.get()) {
      Model model = readModel(is);

      return browse(model, withVisitor()).result();
    }
  }

  /**
   * Checks if a Gradle project uses OWASP ESAPI.
   *
   * @param repository Project's repository.
   * @return True if the project uses OWASP ESAPI, false otherwise.
   */
  private boolean usesOwaspEsapiInGradle(LocalRepository repository) {
    // TODO: implement
    return false;
  }

  /**
   * Creates a new visitor for searching OWASP ESAPI in a POM file.
   */
  private static Visitor withVisitor() {
    return new Visitor();
  }

  /**
   * A visitor for searching OWASP ESAPI in a POM file.
   */
  private static class Visitor extends AbstractModelVisitor {

    /**
     * This flag shows whether OWASP ESAPI was found in a POM file or not.
     */
    private boolean result = false;

    @Override
    public void accept(Dependency dependency, Set<Location> locations) {
      if (isOwaspEsapi(dependency)) {
        result = true;
      }
    }

    /**
     * Tells whether OWASP ESAPI was found or not.
     *
     * @return True if OWASP ESAPI was found, false otherwise.
     */
    boolean result() {
      return result;
    }

    /**
     * Checks if a dependency is OWASP ESAPI.
     *
     * @param dependency The dependency to be checked.
     * @return True if the dependency is OWASP ESAPI, false otherwise.
     */
    private static boolean isOwaspEsapi(Dependency dependency) {
      return "org.owasp.esapi".equals(dependency.getGroupId())
          && "esapi".equals(dependency.getArtifactId());
    }
  }
}
