package com.sap.sgs.phosphor.fosstars.data.github;

import static com.sap.sgs.phosphor.fosstars.maven.MavenUtils.browse;
import static com.sap.sgs.phosphor.fosstars.maven.MavenUtils.readModel;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.USES_OWASP_ESAPI;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.USES_OWASP_JAVA_ENCODER;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.USES_OWASP_JAVA_HTML_SANITIZER;
import static com.sap.sgs.phosphor.fosstars.model.other.Utils.setOf;

import com.sap.sgs.phosphor.fosstars.maven.AbstractModelVisitor;
import com.sap.sgs.phosphor.fosstars.model.Feature;
import com.sap.sgs.phosphor.fosstars.model.ValueSet;
import com.sap.sgs.phosphor.fosstars.model.value.ValueHashSet;
import com.sap.sgs.phosphor.fosstars.tool.github.GitHubProject;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.Set;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;

/**
 * The data provider checks if a project uses security libraries offered by OWASP.
 * It fills out the following features:
 * <ul>
 *   <li>
 *     {@link com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures#USES_OWASP_ESAPI}
 *   </li>
 *   <li>
 *     {@link com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures#USES_OWASP_JAVA_ENCODER}
 *   </li>
 *   <li>
 *     {@link
 *     com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures#USES_OWASP_JAVA_HTML_SANITIZER}
 *   </li>
 * </ul>
 */
public class OwaspSecurityLibraries extends GitHubCachingDataProvider {

  /**
   * Initializes a data provider.
   *
   * @param fetcher An interface to GitHub.
   */
  public OwaspSecurityLibraries(GitHubDataFetcher fetcher) {
    super(fetcher);
  }

  @Override
  protected Set<Feature> supportedFeatures() {
    return setOf(USES_OWASP_ESAPI, USES_OWASP_JAVA_ENCODER, USES_OWASP_JAVA_HTML_SANITIZER);
  }

  @Override
  protected ValueSet fetchValuesFor(GitHubProject project) throws IOException {
    logger.info("Figuring out if the project uses OWASP security libraries ...");

    LocalRepository repository = GitHubDataFetcher.localRepositoryFor(project);
    ValueSet values = new ValueHashSet();
    checkMaven(repository, values);
    checkGradle(repository, values);

    return values;
  }

  private static void checkMaven(LocalRepository repository, ValueSet values) throws IOException {
    Optional<InputStream> content = repository.read("pom.xml");

    if (content.isPresent()) {
      try (InputStream is = content.get()) {
        Model model = readModel(is);

        Visitor visitor = browse(model, withVisitor());
        values.update(USES_OWASP_ESAPI.value(visitor.foundOwaspEsapi));
        values.update(USES_OWASP_JAVA_ENCODER.value(visitor.foundOwaspJavaEncoder));
        values.update(USES_OWASP_JAVA_HTML_SANITIZER.value(visitor.foundOwaspJavaHtmlSanitizer));
      }
    }
  }

  private static void checkGradle(LocalRepository repository, ValueSet values) {
    // TODO: implement
  }

  /**
   * Creates a new visitor for searching OWASP ESAPI in a POM file.
   */
  private static Visitor withVisitor() {
    return new Visitor();
  }

  /**
   * A visitor for searching security libraries in a POM file.
   */
  private static class Visitor extends AbstractModelVisitor {

    /**
     * This flag shows whether OWASP ESAPI was found in a POM file or not.
     */
    private boolean foundOwaspEsapi = false;

    /**
     * This flag shows whether OWASP Java Encoder was found in a POM file or not.
     */
    private boolean foundOwaspJavaEncoder = false;

    /**
     * This flag shows whether OWASP Java HTML Sanitizer was found in a POM file or not.
     */
    private boolean foundOwaspJavaHtmlSanitizer = false;

    @Override
    public void accept(Dependency dependency, Set<Location> locations) {
      if (isOwaspEsapi(dependency)) {
        foundOwaspEsapi = true;
      }

      if (isOwaspJavaEncoder(dependency)) {
        foundOwaspJavaEncoder = true;
      }

      if (isOwaspJavaHtmlSanitizer(dependency)) {
        foundOwaspJavaHtmlSanitizer = true;
      }
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

    /**
     * Checks if a dependency is OWASP Java Encoder.
     *
     * @param dependency The dependency to be checked.
     * @return True if the dependency is OWASP Java Encoder, false otherwise.
     */
    private static boolean isOwaspJavaEncoder(Dependency dependency) {
      String artifactId = dependency.getArtifactId();
      return "org.owasp.encoder".equals(dependency.getGroupId())
          && ("encoder".equals(artifactId) || "encoder-jsp".equals(artifactId));
    }

    /**
     * Checks if a dependency is OWASP Java HTML Sanitizer.
     *
     * @param dependency The dependency to be checked.
     * @return True if the dependency is OWASP Java HTML Sanitizer, false otherwise.
     */
    private static boolean isOwaspJavaHtmlSanitizer(Dependency dependency) {
      return "com.googlecode.owasp-java-html-sanitizer".equals(dependency.getGroupId())
          && "owasp-java-html-sanitizer".equals(dependency.getArtifactId());
    }
  }
}
