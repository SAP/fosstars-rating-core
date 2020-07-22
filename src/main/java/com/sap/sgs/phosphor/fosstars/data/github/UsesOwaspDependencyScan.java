package com.sap.sgs.phosphor.fosstars.data.github;

import static com.sap.sgs.phosphor.fosstars.maven.MavenUtils.browse;
import static com.sap.sgs.phosphor.fosstars.maven.MavenUtils.readModel;
import static com.sap.sgs.phosphor.fosstars.maven.ModelVisitor.Location.BUILD;
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
import com.sap.sgs.phosphor.fosstars.model.value.OwaspDependencyCheckUsage;
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
   * Checks if a project uses OWASP Dependency Check Maven plugin.
   *
   * @param repository The project's repository.
   * @param values set of type {@link ValueSet}.
   * @throws IOException if something goes wrong.
   */
  private static void checkMaven(LocalRepository repository, ValueSet values) throws IOException {
    Optional<InputStream> content = repository.read("pom.xml");

    if (content.isPresent()) {
      try (InputStream is = content.get()) {
        Model model = readModel(is);

        Visitor visitor = browse(model, withVisitor());

        values.update(OWASP_DEPENDENCY_CHECK_USAGE
            .value(usage(visitor.foundMandatory, visitor.foundOptional)));

        if (visitor.score == null) {
          values.update(OWASP_DEPENDENCY_CHECK_FAIL_CVSS_THRESHOLD.notSpecifiedValue());
        } else {
          values.update(OWASP_DEPENDENCY_CHECK_FAIL_CVSS_THRESHOLD.value(visitor.score));
        }
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
   * Get {@link OwaspDependencyCheckUsage} based on where the plugin is found. 
   *
   * @param foundMandatory if the plugin is run as a mandatory check.
   * @param foundOptional if the plugin is run as an optional check.
   * @return {@link OwaspDependencyCheckUsage} based on where the plugin is found.
   */
  private static OwaspDependencyCheckUsage usage(boolean foundMandatory, boolean foundOptional) {
    // TODO: move the method to the Visitor
    if (foundMandatory) {
      return MANDATORY;
    } else if (foundOptional) {
      return OPTIONAL;
    }
    return NOT_USED;
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
   * Check if plugin configurations have an attribute which can fail the build on finding
   * vulnerabilities above a given CVSS score. Hence, look for possible CVSS score value which
   * indicates when to fail the build.
   *
   * @param configuration The plugin configuration to be checked.
   * @return The CVSS score which indicates how the build should fail. Otherwise null.
   */
  private static Double cvssScoreFrom(Object configuration) {
    // The OWASP Dependency Check plugin is found and checking configurations.
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
   *    <li>if 'number', then parse the value.</li>
   *    <li>if 'boolean' is true, then return 0.0, otherwise null.</li>
   *    <li>if the type can be parsed, an exception is thrown.</li>
   * </ul>
   * 
   * @param value the value to be parsed.
   * @param type the type of the string value.
   * @return the parsed value.
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
      // TODO: foundOptional should not be considered
      if (foundMandatory || foundOptional) {
        return;
      }
      
      if (isDependencyCheck(plugin)) {
        if (mandatoryCheckFor(locations)) {
          foundMandatory = true;
        } else {
          foundOptional = true;
        }
        score = cvssScoreFrom(plugin.getConfiguration());
      }
    }

    @Override
    public void accept(ReportPlugin plugin, Set<Location> locations) {
      // TODO: the method looks similar to the one above
      //       can we do anything?
      if (foundMandatory || foundOptional) {
        return;
      }
      
      if (isDependencyCheck(plugin)) {
        if (mandatoryCheckFor(locations)) {
          foundMandatory = true;
        } else {
          foundOptional = true;
        }
        score = cvssScoreFrom(plugin.getConfiguration());
      }
    }
  }
}