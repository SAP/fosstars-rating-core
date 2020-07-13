package com.sap.sgs.phosphor.fosstars.data.github;

import static com.sap.sgs.phosphor.fosstars.maven.MavenUtils.browse;
import static com.sap.sgs.phosphor.fosstars.maven.MavenUtils.readModel;
import static com.sap.sgs.phosphor.fosstars.maven.ModelVisitor.Location.BUILD;
import static com.sap.sgs.phosphor.fosstars.maven.ModelVisitor.Location.MANAGEMENT;
import static com.sap.sgs.phosphor.fosstars.maven.ModelVisitor.Location.PROFILE;
import static com.sap.sgs.phosphor.fosstars.maven.ModelVisitor.Location.REPORTING;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.OWASP_DEPENDENCY_CHECK_FAIL_CVSS_THRESHOLD;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.OWASP_DEPENDENCY_CHECK_SCAN_AVAILABILITY;
import static com.sap.sgs.phosphor.fosstars.model.value.Status.MANDATORY;
import static com.sap.sgs.phosphor.fosstars.model.value.Status.NOTFOUND;
import static com.sap.sgs.phosphor.fosstars.model.value.Status.OPTIONAL;

import com.sap.sgs.phosphor.fosstars.maven.ModelVisitor;
import com.sap.sgs.phosphor.fosstars.maven.ModelVisitor.Location;
import com.sap.sgs.phosphor.fosstars.model.Feature;
import com.sap.sgs.phosphor.fosstars.model.ValueSet;
import com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures;
import com.sap.sgs.phosphor.fosstars.model.value.Status;
import com.sap.sgs.phosphor.fosstars.model.value.ValueHashSet;
import com.sap.sgs.phosphor.fosstars.tool.github.GitHubProject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.apache.maven.model.Model;
import org.apache.maven.model.Plugin;
import org.apache.maven.model.ReportPlugin;
import org.codehaus.plexus.util.xml.Xpp3Dom;

/**
 * This data provider checks if an open-source project uses OWASP Dependency Check Maven plugin to
 * scan dependencies for known vulnerabilities.
 * In particular, it tires to fill out the following features:
 * <ul>
 *   <li>{@link OssFeatures#OWASP_DEPENDENCY_CHECK_SCAN_AVAILABILITY}</li>
 *   <li>{@link OssFeatures#OWASP_DEPENDENCY_CHECK_FAIL_CVSS_THRESHOLD}</li>
 * </ul>
 */
public class UsesOwaspDependencyScan extends GitHubCachingDataProvider {

  /**
   * The CVSS score which is not within the considered range.
   * 
   * @see <a href="https://nvd.nist.gov/vuln-metrics/cvss">CVSS</a>
   */
  private static final double OUT_OF_BOUND_CVSS_SCORE = 11.0;

  /**
   * A list of configurations associated to OWASP Dependency check plugin which can fail the build.
   */
  private static final Map<String, String> FAIL_BUILD_CONFIGURATIONS = new HashMap<>();

  static {
    FAIL_BUILD_CONFIGURATIONS.put("junitFailOnCVSS", "number"); 
    FAIL_BUILD_CONFIGURATIONS.put("failBuildOnCVSS", "number");
    FAIL_BUILD_CONFIGURATIONS.put("failBuildOnAnyVulnerability", "boolean");
  }

  /**
   * The set of OWASP Dependency features.
   */
  static final Set<Feature> OWASP_FEATURES = new HashSet<>(
      Arrays.asList(
          OWASP_DEPENDENCY_CHECK_SCAN_AVAILABILITY,
          OWASP_DEPENDENCY_CHECK_FAIL_CVSS_THRESHOLD));

  /**
   * Initializes a data provider.
   *
   * @param fetcher An interface to GitHub.
   */
  public UsesOwaspDependencyScan(GitHubDataFetcher fetcher) {
    super(fetcher);
  }

  @Override
  protected Set<Feature> supportedFeatures() {
    return OWASP_FEATURES;
  }

  @Override
  protected ValueSet fetchValuesFor(GitHubProject project) throws IOException {
    logger.info("Figuring out if the project uses OWASP Dependency Check ...");
    LocalRepository repository = GitHubDataFetcher.localRepositoryFor(project);
    return new ValueHashSet(
        defaultValues(),
        checkMaven(repository),
        checkGradle(repository));
  }

  /**
   * Generates a {@link ValueSet} of features with default values.
   * 
   * @return {@link ValueSet} of features.
   */
  private static ValueSet defaultValues() {
    return new ValueHashSet(
        OWASP_DEPENDENCY_CHECK_SCAN_AVAILABILITY.value(NOTFOUND),
        OWASP_DEPENDENCY_CHECK_FAIL_CVSS_THRESHOLD.value(OUT_OF_BOUND_CVSS_SCORE));
  }

  /**
   * Checks if a project uses OWASP Dependency Check Maven plugin.
   *
   * @param repository The project's repository.
   * @return True if the project uses the plugin, false otherwise.
   */
  private static ValueSet checkMaven(LocalRepository repository) throws IOException {
    Optional<InputStream> content = repository.read("pom.xml");

    if (!content.isPresent()) {
      return ValueHashSet.empty();
    }

    try (InputStream is = content.get()) {
      Model model = readModel(is);

      Visitor visitor = browse(model, withVisitor());
      
      return new ValueHashSet(
          OWASP_DEPENDENCY_CHECK_SCAN_AVAILABILITY.value(visitor.status),
          OWASP_DEPENDENCY_CHECK_FAIL_CVSS_THRESHOLD.value(visitor.score));
    }
  }

  /**
   * Checks if a project uses OWASP Dependency Check Gradle plugin.
   *
   * @param repository The project's repository.
   * @return True if the project uses the plugin, false otherwise.
   */
  private static ValueSet checkGradle(LocalRepository repository) throws IOException {
    Optional<InputStream> content = repository.read("build.gradle");

    if (content.isPresent()) {
      try (BufferedReader reader = new BufferedReader(new InputStreamReader(content.get()))) {
        String line;
        while ((line = reader.readLine()) != null) {
          if (line.trim().contains("org.owasp:dependency-check-gradle")) {
            return new ValueHashSet(
                OWASP_DEPENDENCY_CHECK_SCAN_AVAILABILITY.value(MANDATORY),
                OWASP_DEPENDENCY_CHECK_FAIL_CVSS_THRESHOLD.value(11.0));
          }
        }
      }
    }

    return ValueHashSet.empty();
  }

  /**
   * Check if a plugin is OWASP Dependency Check plugin.
   *
   * @param plugin The plugin to be checked.
   * @return True if the plugin is OWASP Dependency Check plugin, false otherwise.
   */
  private static boolean isDependencyCheck(Plugin plugin) {
    return isDependencyCheck(plugin.getGroupId(), plugin.getArtifactId());
  }

  /**
   * Check if a report plugin is OWASP Dependency Check plugin.
   *
   * @param plugin The report plugin to be checked.
   * @return True if the report plugin is OWASP Dependency Check plugin, false otherwise.
   */
  private static boolean isDependencyCheck(ReportPlugin plugin) {
    return isDependencyCheck(plugin.getGroupId(), plugin.getArtifactId());
  }

  /**
   * Check if groupId and artifactId matches with OWASP Dependency GA.
   *
   * @param groupId to be checked.
   * @param artifactId to be checked.
   * @return True if the OWASP Dependency GA matches, false otherwise.
   */
  private static boolean isDependencyCheck(String groupId, String artifactId) {
    return "org.owasp".equals(groupId) && "dependency-check-maven".equals(artifactId);
  }

  /**
   * Check the availability of OWASP Dependency plugin in the project.
   * 
   * @param locations Set of location hits which indicates where exactly the plugin was found.
   * @return The availability status of type {@link Status}.
   */
  private static Status availablityFrom(Set<Location> locations) {
    if (locations == null || locations.isEmpty()) {
      return NOTFOUND;
    }

    if (mandatoryCheckFor(locations)) {
      return MANDATORY;
    }

    return OPTIONAL;
  }

  /**
   * Check if plugin configurations have an attribute which can fail the build on finding
   * vulnerabilities above a given cvss score. Hence, look for possible cvss score value which
   * indicates, when to fail the build. The cvss score is in between 0.0 <= x <= 10.0. If the 
   * score is > 11.0, then no vulnerabilities are considered to fail the build as this value 
   * is out of bound.
   *
   * @param configuration The plugin configuration to be checked.
   * @return The cvss score which indicates how the build should fail.
   */
  private static double cvssScoreFrom(Object configuration) {
    // The OWASP dependency plugin is found and checking configurations.
    for (String config : FAIL_BUILD_CONFIGURATIONS.keySet()) {
      Optional<String> content = domParser(config, configuration);
      String type = FAIL_BUILD_CONFIGURATIONS.get(config);

      if (content.isPresent()) {
        return parseScore(content.get(), type);
      }
    }

    return OUT_OF_BOUND_CVSS_SCORE;
  }

  /**
   * Parse the value depending on the given type. The type can be 'boolean' or 'number'. If
   * 'boolean', then return 0.0, otherwise parse the given string value to 'number'.
   * 
   * @param value the value to be parsed.
   * @param type the type of the string value.
   * @return the parsed value.
   */
  private static double parseScore(String value, String type) {
    if ("number".equals(type)) {
      return Double.parseDouble(value);
    }
    return 0.0;
  }

  /**
   * Get the value if a configuration exists.
   * 
   * @param configName The name of the configuration to find.
   * @param configuration The list of configurations associated to OWASP Dependency check plugin.
   * @return The value of the configuration if found.
   */
  private static Optional<String> domParser(String configName, Object configuration) {
    if (configuration == null) {
      return Optional.empty();
    }

    Xpp3Dom dom = ((Xpp3Dom) configuration).getChild(configName);
    if (dom == null) {
      return Optional.empty();
    }

    return Optional.ofNullable(dom.getValue());
  }

  /**
   * Returns whether the plugin will be executed as a mandatory step during the build.
   * 
   * @param defaultParam The location of the plugin.
   * @param locations The set of {@link Location}.
   * @return True if the plugin will be executed as a mandatory step, false otherwise.
   */
  private static boolean mandatoryCheckFor(Set<Location> locations) {
    return (locations.contains(BUILD) || locations.contains(REPORTING))  
        && !locations.contains(PROFILE)
        && !locations.contains(MANAGEMENT);
  }

  /**
   * Creates a new visitor for searching OWASP Dependency Check in a POM file.
   */
  private static Visitor withVisitor() {
    return new Visitor();
  }

  /**
   * A visitor for searching OWASP Dependency Check in a POM file.
   */
  private static class Visitor implements ModelVisitor {

    /**
     * The cvss threshold configuration's cvss score.
     */
    private double score = OUT_OF_BOUND_CVSS_SCORE;

    /**
     * The availability status of plugin in the project.
     */
    private Status status = NOTFOUND;

    @Override
    public void accept(Plugin plugin, Set<Location> locations) {
      if (isDependencyCheck(plugin)) {
        status = availablityFrom(locations);
        score = cvssScoreFrom(plugin.getConfiguration());
      }
    }

    @Override
    public void accept(ReportPlugin plugin, Set<Location> locations) {
      if (isDependencyCheck(plugin)) {
        status = availablityFrom(locations);
        score = cvssScoreFrom(plugin.getConfiguration());
      }
    }
  }
}