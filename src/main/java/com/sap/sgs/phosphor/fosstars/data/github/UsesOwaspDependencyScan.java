package com.sap.sgs.phosphor.fosstars.data.github;

import static com.sap.sgs.phosphor.fosstars.maven.MavenUtils.browse;
import static com.sap.sgs.phosphor.fosstars.maven.MavenUtils.readModel;
import static com.sap.sgs.phosphor.fosstars.maven.ModelVisitor.Location.BUILD;
import static com.sap.sgs.phosphor.fosstars.maven.ModelVisitor.Location.PROFILE;
import static com.sap.sgs.phosphor.fosstars.maven.ModelVisitor.Location.REPORTING;
import static com.sap.sgs.phosphor.fosstars.model.feature.OwaspDependencyCheckCvssThreshold.OUT_OF_BOUND;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.OWASP_DEPENDENCY_CHECK_FAIL_CVSS_THRESHOLD;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.OWASP_DEPENDENCY_CHECK_USAGE;
import static com.sap.sgs.phosphor.fosstars.model.value.OwaspDependencyCheckUsage.MANDATORY;
import static com.sap.sgs.phosphor.fosstars.model.value.OwaspDependencyCheckUsage.NOT_USED;
import static com.sap.sgs.phosphor.fosstars.model.value.OwaspDependencyCheckUsage.OPTIONAL;

import com.sap.sgs.phosphor.fosstars.maven.ModelVisitor;
import com.sap.sgs.phosphor.fosstars.maven.ModelVisitor.Location;
import com.sap.sgs.phosphor.fosstars.model.Feature;
import com.sap.sgs.phosphor.fosstars.model.ValueSet;
import com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures;
import com.sap.sgs.phosphor.fosstars.model.value.OwaspDependencyCheckUsage;
import com.sap.sgs.phosphor.fosstars.model.value.ValueHashSet;
import com.sap.sgs.phosphor.fosstars.tool.github.GitHubProject;
import java.io.IOException;
import java.io.InputStream;
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
 * This data provider checks if an open-source project uses OWASP Dependency Check to scan
 * dependencies for known vulnerabilities. In particular, it tires to fill out the following
 * features:
 * <ul>
 *   <li>{@link OssFeatures#OWASP_DEPENDENCY_CHECK_USAGE}</li>
 *   <li>{@link OssFeatures#OWASP_DEPENDENCY_CHECK_FAIL_CVSS_THRESHOLD}</li>
 * </ul>
 */
public class UsesOwaspDependencyScan extends GitHubCachingDataProvider {

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
          OWASP_DEPENDENCY_CHECK_USAGE,
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

    ValueSet values = defaultValues();
    checkMaven(repository, values);
    checkGradle(repository, values);

    return values;
  }

  /**
   * Generates a {@link ValueSet} of features with default values.
   * 
   * @return {@link ValueSet} with the default values.
   */
  private static ValueSet defaultValues() {
    return new ValueHashSet(
        OWASP_DEPENDENCY_CHECK_USAGE.value(NOT_USED),
        OWASP_DEPENDENCY_CHECK_FAIL_CVSS_THRESHOLD.value(OUT_OF_BOUND));
  }

  /**
   * Checks if a project uses OWASP Dependency Check Maven plugin.
   *
   * @param repository The project's repository.
   * @param values set of type {@link ValueSet}.
   * @return True if the project uses the plugin, false otherwise.
   */
  private static void checkMaven(LocalRepository repository, ValueSet values) throws IOException {
    Optional<InputStream> content = repository.read("pom.xml");

    if (content.isPresent()) {
      try (InputStream is = content.get()) {
        Model model = readModel(is);

        Visitor visitor = browse(model, withVisitor());

        values.update(OWASP_DEPENDENCY_CHECK_USAGE.value(visitor.status));
        values.update(OWASP_DEPENDENCY_CHECK_FAIL_CVSS_THRESHOLD.value(visitor.score));
      }
    }
  }

  /**
   * Checks if a project uses OWASP Dependency Check Gradle plugin.
   *
   * @param repository The project's repository.
   * @param values set of type {@link ValueSet}.
   * @return True if the project uses the plugin, false otherwise.
   */
  private static void checkGradle(LocalRepository repository, ValueSet values) throws IOException {
    // Nothing to be done.
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
   * Check the usage of OWASP Dependency Check plugin in the project.
   * 
   * @param locations Set of location hits which indicates where exactly the plugin was found.
   * @return The usage status of type {@link OwaspDependencyCheckUsage}.
   */
  private static OwaspDependencyCheckUsage usageFrom(Set<Location> locations) {
    if (locations == null || locations.isEmpty()) {
      return NOT_USED;
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
    for (String name : FAIL_BUILD_CONFIGURATIONS.keySet()) {
      Optional<String> content = parameter(name, configuration);

      if (content.isPresent()) {
        String type = FAIL_BUILD_CONFIGURATIONS.get(name);
        return parseScore(content.get(), type);
      }
    }

    return OUT_OF_BOUND;
  }

  /**
   * Parse the value depending on the given type. The type can be 'boolean' or 'number'. If
   * 'boolean', then return 0.0, otherwise parse the given string value to 'number'. If the input is
   * null or not parseable, then an exception is thrown.
   * 
   * @param value the value to be parsed.
   * @param type the type of the string value.
   * @return the parsed value.
   */
  private static double parseScore(String value, String type) {
    if ("boolean".equals(type)) {
      return 0.0;
    }
    return Double.parseDouble(value);
  }

  /**
   * Get the value if a configuration exists.
   * 
   * @param name The name of the configuration to find.
   * @param configuration The list of configurations associated to OWASP Dependency check plugin.
   * @return The value of the configuration if found.
   */
  private static Optional<String> parameter(String name, Object configuration) {
    if (configuration == null || !(configuration instanceof Xpp3Dom)) {
      return Optional.empty();
    }

    Xpp3Dom dom = ((Xpp3Dom) configuration).getChild(name);
    if (dom == null) {
      return Optional.empty();
    }

    return Optional.ofNullable(dom.getValue());
  }

  /**
   * Checks whether the plugin will be executed as a mandatory step during the build.
   * 
   * @param locations The set of {@link Location}.
   * @return True if the plugin will be executed as a mandatory step, false otherwise.
   */
  private static boolean mandatoryCheckFor(Set<Location> locations) {
    return (locations.contains(BUILD) || locations.contains(REPORTING))  
        && !locations.contains(PROFILE);
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
    private double score = OUT_OF_BOUND;

    /**
     * The default usage status of plugin in the project.
     */
    private OwaspDependencyCheckUsage status = NOT_USED;

    /**
     * The flag which closes down when a plugin location is found.
     */
    private boolean flag = true;

    @Override
    public void accept(Plugin plugin, Set<Location> locations) {
      if (isDependencyCheck(plugin) && flag) {
        status = usageFrom(locations);
        score = cvssScoreFrom(plugin.getConfiguration());
        flag = false;
      }
    }

    @Override
    public void accept(ReportPlugin plugin, Set<Location> locations) {
      if (isDependencyCheck(plugin) && flag) {
        status = usageFrom(locations);
        score = cvssScoreFrom(plugin.getConfiguration());
        flag = false;
      }
    }
  }
}