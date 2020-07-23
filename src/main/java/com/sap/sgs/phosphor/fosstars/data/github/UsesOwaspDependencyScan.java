package com.sap.sgs.phosphor.fosstars.data.github;

import static com.sap.sgs.phosphor.fosstars.maven.MavenUtils.browse;
import static com.sap.sgs.phosphor.fosstars.maven.MavenUtils.readModel;
import static com.sap.sgs.phosphor.fosstars.maven.ModelVisitor.Location.BUILD;
import static com.sap.sgs.phosphor.fosstars.maven.ModelVisitor.Location.MANAGEMENT;
import static com.sap.sgs.phosphor.fosstars.maven.ModelVisitor.Location.PROFILE;
import static com.sap.sgs.phosphor.fosstars.maven.ModelVisitor.Location.REPORTING;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.OWASP_DEPENDENCY_CHECK_FAIL_CVSS_THRESHOLD;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.OWASP_DEPENDENCY_CHECK_USAGE;
import static com.sap.sgs.phosphor.fosstars.model.other.Utils.setOf;
import static com.sap.sgs.phosphor.fosstars.model.value.OwaspDependencyCheckUsage.MANDATORY;
import static com.sap.sgs.phosphor.fosstars.model.value.OwaspDependencyCheckUsage.NOT_USED;
import static com.sap.sgs.phosphor.fosstars.model.value.OwaspDependencyCheckUsage.OPTIONAL;

import com.sap.sgs.phosphor.fosstars.maven.ModelVisitor;
import com.sap.sgs.phosphor.fosstars.maven.ModelVisitor.Location;
import com.sap.sgs.phosphor.fosstars.model.Feature;
import com.sap.sgs.phosphor.fosstars.model.ValueSet;
import com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures;
import com.sap.sgs.phosphor.fosstars.model.value.OwaspDependencyCheckCvssThresholdValue;
import com.sap.sgs.phosphor.fosstars.model.value.OwaspDependencyCheckUsage;
import com.sap.sgs.phosphor.fosstars.model.value.OwaspDependencyCheckUsageValue;
import com.sap.sgs.phosphor.fosstars.model.value.ValueHashSet;
import com.sap.sgs.phosphor.fosstars.tool.github.GitHubProject;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.apache.maven.model.Model;
import org.apache.maven.model.Plugin;
import org.apache.maven.model.ReportPlugin;
import org.codehaus.plexus.util.xml.Xpp3Dom;
import sun.plugin.PluginURLJarFileCallBack;

/**
 * This data provider checks if an open-source project uses OWASP Dependency Check to scan
 * dependencies for known vulnerabilities. In particular, it tries to fill out the following
 * features:
 * <ul>
 *   <li>{@link OssFeatures#OWASP_DEPENDENCY_CHECK_USAGE}</li>
 *   <li>{@link OssFeatures#OWASP_DEPENDENCY_CHECK_FAIL_CVSS_THRESHOLD}</li>
 * </ul>
 */
public class UsesOwaspDependencyScan extends GitHubCachingDataProvider {

  /**
   * A list of configurations associated to OWASP Dependency Check plugin which can fail the build.
   */
  private static final Map<String, String> FAIL_BUILD_CONFIGURATIONS = new HashMap<>();

  static {
    FAIL_BUILD_CONFIGURATIONS.put("failBuildOnAnyVulnerability", "boolean");
    FAIL_BUILD_CONFIGURATIONS.put("failBuildOnCVSS", "number");
    FAIL_BUILD_CONFIGURATIONS.put("junitFailOnCVSS", "number"); 
  }

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
    return setOf(
        OWASP_DEPENDENCY_CHECK_USAGE, 
        OWASP_DEPENDENCY_CHECK_FAIL_CVSS_THRESHOLD);
  }

  @Override
  protected ValueSet fetchValuesFor(GitHubProject project) throws IOException {
    logger.info("Figuring out if the project uses OWASP Dependency Check ...");
    LocalRepository repository = GitHubDataFetcher.localRepositoryFor(project);

    ValueSet values = new ValueHashSet();
    checkMaven(repository, values);
    checkGradle(repository, values);

    return values;
  }

  /**
   * Checks if a project uses OWASP Dependency Check Maven plugin,
   * and updates a value set if the plugin is found.
   *
   * @param repository The project's repository.
   * @param values A {@link ValueSet} to be updated.
   * @throws IOException If something goes wrong.
   */
  private static void checkMaven(LocalRepository repository, ValueSet values) throws IOException {
    Optional<InputStream> content = repository.read("pom.xml");

    if (content.isPresent()) {
      try (InputStream is = content.get()) {
        Model model = readModel(is);

        Visitor visitor = browse(model, withVisitor());

        OwaspDependencyCheckUsageValue usage = OWASP_DEPENDENCY_CHECK_USAGE.value(visitor.usage());

        OwaspDependencyCheckCvssThresholdValue threshold = visitor.threshold()
            .map(OWASP_DEPENDENCY_CHECK_FAIL_CVSS_THRESHOLD::value)
            .orElse(OWASP_DEPENDENCY_CHECK_FAIL_CVSS_THRESHOLD.notSpecifiedValue());

        values.update(usage, threshold);
      }
    }
  }

  /**
   * Checks if a project uses OWASP Dependency Check Gradle plugin.
   *
   * @param repository The project's repository.
   * @param values set of type {@link ValueSet}.
   */
  private static void checkGradle(LocalRepository repository, ValueSet values) {
    // Nothing to be done.
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
   * Check if a plugin configuration has an attribute that specifies a CVSS threshold,
   * so that the plugin fails the build if vulnerabilities with higher CVSS score are found.
   *
   * @param configuration The plugin configuration to be checked.
   * @return The CVSS threshold if found, otherwise null.
   */
  private static Double cvssScoreFrom(Object configuration) {
    for (String name : FAIL_BUILD_CONFIGURATIONS.keySet()) {
      Optional<String> content = parameter(name, configuration);

      if (content.isPresent()) {
        String type = FAIL_BUILD_CONFIGURATIONS.get(name);
        return parseScore(content.get(), type);
      }
    }

    return null;
  }

  /**
   * Parse the value depending on the given type. The type can be 'boolean' or 'number'. 
   * <ul>
   *    <li>If the type is 'number', then parse the value.</li>
   *    <li>If the type is 'boolean' and the value is true, then return 0.0, otherwise null.</li>
   *    <li>If the type is unknown, an exception is thrown.</li>
   * </ul>
   * 
   * @param value The value to be parsed.
   * @param type The type of the string value.
   * @return The parsed value.
   */
  private static Double parseScore(String value, String type) {
    switch (type) {
      case "number":
        return Double.parseDouble(value);
      case "boolean":
        return Boolean.parseBoolean(value) ? 0.0 : null;
      default:
        throw new IllegalArgumentException(String.format("Unknown type: %s", type));
    }
  }

  /**
   * Get the value if a configuration exists.
   * 
   * @param name The name of the configuration to find.
   * @param configuration The list of configurations associated to OWASP Dependency Check plugin.
   * @return The value of the configuration if found.
   */
  private static Optional<String> parameter(String name, Object configuration) {
    if (configuration instanceof Xpp3Dom == false) {
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
    return locations.size() == 1 && (locations.contains(BUILD) || locations.contains(REPORTING));
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
     * If the plugin is run as a mandatory check.
     */
    private boolean foundMandatory = false;
    
    /**
     * If the plugin is run as an optional check.
     */
    private boolean foundOptional = false;
    
    /**
     * The CVSS threshold score to fail the build. 
     */
    private Double score; 

    @Override
    public void accept(Plugin plugin, Set<Location> locations) {
      accept(plugin.getGroupId(), plugin.getArtifactId(), plugin.getConfiguration(), locations);
    }

    @Override
    public void accept(ReportPlugin plugin, Set<Location> locations) {
      accept(plugin.getGroupId(), plugin.getArtifactId(), plugin.getConfiguration(), locations);
    }

    /**
     * Check if a plugin is OWASP Dependency Check plugin.
     *
     * @param groupId A group id of the plugins.
     * @param artifactId An artifact id of the plugin.
     * @param configuration A configuration of the plugin.
     * @param locations Locations of the plugin.
     */
    private void accept(
        String groupId, String artifactId, Object configuration, Set<Location> locations) {

      if (foundMandatory) {
        return;
      }

      if (isDependencyCheck(groupId, artifactId)) {
        if (mandatoryCheckFor(locations)) {
          foundMandatory = true;
        } else {
          foundOptional = true;
        }

        score = cvssScoreFrom(configuration);
      }
    }

    /**
     * Get {@link OwaspDependencyCheckUsage} based on where the plugin is found.
     *
     * @return {@link OwaspDependencyCheckUsage} based on where the plugin is found.
     */
    OwaspDependencyCheckUsage usage() {
      if (foundMandatory) {
        return MANDATORY;
      }

      if (foundOptional) {
        return OPTIONAL;
      }

      return NOT_USED;
    }

    /**
     * Get a CVSS threshold if found.
     *
     * @return The threshold.
     */
    Optional<Double> threshold() {
      return Optional.ofNullable(score);
    }
  }
}