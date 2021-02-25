package com.sap.oss.phosphor.fosstars.data.github;

import static com.sap.oss.phosphor.fosstars.maven.MavenUtils.browse;
import static com.sap.oss.phosphor.fosstars.maven.MavenUtils.readModel;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_OWASP_ESAPI;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_OWASP_JAVA_ENCODER;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_OWASP_JAVA_HTML_SANITIZER;
import static com.sap.oss.phosphor.fosstars.model.other.Utils.setOf;

import com.sap.oss.phosphor.fosstars.maven.AbstractModelVisitor;
import com.sap.oss.phosphor.fosstars.model.Feature;
import com.sap.oss.phosphor.fosstars.model.ValueSet;
import com.sap.oss.phosphor.fosstars.model.subject.oss.GitHubProject;
import com.sap.oss.phosphor.fosstars.model.value.ValueHashSet;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;

/**
 * The data provider checks if a project uses security libraries offered by OWASP.
 * It fills out the following features:
 * <ul>
 *   <li>
 *     {@link com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures#USES_OWASP_ESAPI}
 *   </li>
 *   <li>
 *     {@link com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures#USES_OWASP_JAVA_ENCODER}
 *   </li>
 *   <li>
 *     {@link
 *     com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures#USES_OWASP_JAVA_HTML_SANITIZER}
 *   </li>
 * </ul>
 */
public class OwaspSecurityLibraries extends GitHubCachingDataProvider {

  /**
   * A set of supported scopes for dependencies.
   */
  private static final Set<String> SUPPORTED_GRADLE_SCOPES
      = setOf("implementation", "compile", "runtime");

  /**
   * Initializes a data provider.
   *
   * @param fetcher An interface to GitHub.
   */
  public OwaspSecurityLibraries(GitHubDataFetcher fetcher) {
    super(fetcher);
  }

  @Override
  public Set<Feature<?>> supportedFeatures() {
    return setOf(USES_OWASP_ESAPI, USES_OWASP_JAVA_ENCODER, USES_OWASP_JAVA_HTML_SANITIZER);
  }

  @Override
  protected ValueSet fetchValuesFor(GitHubProject project) throws IOException {
    logger.info("Figuring out if the project uses OWASP security libraries ...");

    ValueSet values = new ValueHashSet();

    // set default values
    values.update(USES_OWASP_ESAPI.value(false));
    values.update(USES_OWASP_JAVA_ENCODER.value(false));
    values.update(USES_OWASP_JAVA_HTML_SANITIZER.value(false));

    LocalRepository repository = GitHubDataFetcher.localRepositoryFor(project);

    checkMaven(repository, values);
    checkGradle(repository, values);

    return values;
  }

  /**
   * Looks for the feature in a Maven project.
   *
   * @param repository Project's repository.
   * @param values A value set to be updated with the features.
   * @throws IOException If something went wrong.
   */
  private static void checkMaven(LocalRepository repository, ValueSet values) throws IOException {
    Optional<InputStream> content = repository.read("pom.xml");

    if (content.isPresent()) {
      try (InputStream is = content.get()) {
        Model model = readModel(is);

        Visitor visitor = browse(model, withVisitor());

        if (visitor.foundOwaspEsapi) {
          values.update(USES_OWASP_ESAPI.value(true));
        }

        if (visitor.foundOwaspJavaEncoder) {
          values.update(USES_OWASP_JAVA_ENCODER.value(true));
        }

        if (visitor.foundOwaspJavaHtmlSanitizer) {
          values.update(USES_OWASP_JAVA_HTML_SANITIZER.value(true));
        }
      }
    }
  }

  /**
   * Looks for the feature in a Gradle project.
   *
   * @param repository Project's repository.
   * @param values A value set to be updated with the features.
   * @throws IOException If something went wrong.
   */
  private static void checkGradle(LocalRepository repository, ValueSet values) throws IOException {
    for (Path gradleFile : repository.files(path -> path.getFileName().endsWith(".gradle"))) {
      Optional<List<String>> something = repository.readLinesOf(gradleFile);
      if (!something.isPresent()) {
        return;
      }
      List<String> file = something.get();

      if (foundOwaspEsapiInGradle(file)) {
        values.update(USES_OWASP_ESAPI.value(true));
      }

      if (foundOwaspJavaEncoderInGradle(file)) {
        values.update(USES_OWASP_JAVA_ENCODER.value(true));
      }

      if (foundOwaspJavaHtmlSanitizerInGradle(file)) {
        values.update(USES_OWASP_JAVA_HTML_SANITIZER.value(true));
      }
    }
  }

  /**
   * Check if OWASP ESAPI is used in a Gradle file.
   *
   * @param file The content of the file.
   * @return True if the library is found, false otherwise.
   */
  private static boolean foundOwaspEsapiInGradle(List<String> file) {
    return hasDependencyInGradle(file, "org.owasp.esapi:esapi");
  }

  /**
   * Check if OWASP Java Encoder is used in a Gradle file.
   *
   * @param file The content of the file.
   * @return True if the library is found, false otherwise.
   */
  private static boolean foundOwaspJavaEncoderInGradle(List<String> file) {
    return hasDependencyInGradle(file,
        "org.owasp.encoder:encoder", "org.owasp.encoder:encoder-jsp");
  }

  /**
   * Check if OWASP Java HTML Sanitizer is used in a Gradle file.
   *
   * @param file The content of the file.
   * @return True if the library is found, false otherwise.
   */
  private static boolean foundOwaspJavaHtmlSanitizerInGradle(List<String> file) {
    return hasDependencyInGradle(file,
        "com.googlecode.owasp-java-html-sanitizer:owasp-java-html-sanitizer");
  }

  /**
   * Check if a gradle file contains one of the specified dependencies.
   *
   * @param content The content of the file.
   * @param groupAndArtifactIds The dependencies to be checked.
   * @return True if one of the dependencies is found, false otherwise.
   */
  private static boolean hasDependencyInGradle(
      List<String> content, String... groupAndArtifactIds) {

    boolean foundDependenciesSection = false;
    for (String line : content) {
      line = line.trim();

      if ("dependencies {".equals(line)) {
        foundDependenciesSection = true;
        continue;
      }

      if (!foundDependenciesSection) {
        continue;
      }

      if ("}".equals(line)) {
        break;
      }

      String[] parts = line.split("\\s+");
      if (parts.length < 2) {
        continue;
      }

      String scope = parts[0];
      if (!SUPPORTED_GRADLE_SCOPES.contains(scope)) {
        continue;
      }

      String gav = parts[1];
      if (Arrays.stream(groupAndArtifactIds).anyMatch(gav::contains)) {
        return true;
      }
    }

    return false;
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
