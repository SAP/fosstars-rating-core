package com.sap.oss.phosphor.fosstars.data.github;

import static com.sap.oss.phosphor.fosstars.maven.MavenUtils.browse;
import static com.sap.oss.phosphor.fosstars.maven.MavenUtils.readModel;
import static com.sap.oss.phosphor.fosstars.maven.ModelVisitor.Location.BUILD;
import static com.sap.oss.phosphor.fosstars.maven.ModelVisitor.Location.REPORTING;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.OWASP_DEPENDENCY_CHECK_FAIL_CVSS_THRESHOLD;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.OWASP_DEPENDENCY_CHECK_USAGE;
import static com.sap.oss.phosphor.fosstars.model.other.Utils.setOf;
import static com.sap.oss.phosphor.fosstars.model.value.OwaspDependencyCheckUsage.MANDATORY;
import static com.sap.oss.phosphor.fosstars.model.value.OwaspDependencyCheckUsage.NOT_USED;
import static com.sap.oss.phosphor.fosstars.model.value.OwaspDependencyCheckUsage.OPTIONAL;

import com.sap.oss.phosphor.fosstars.maven.AbstractModelVisitor;
import com.sap.oss.phosphor.fosstars.maven.ModelVisitor.Location;
import com.sap.oss.phosphor.fosstars.model.Feature;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.ValueSet;
import com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures;
import com.sap.oss.phosphor.fosstars.model.subject.oss.GitHubProject;
import com.sap.oss.phosphor.fosstars.model.value.CVSS;
import com.sap.oss.phosphor.fosstars.model.value.OwaspDependencyCheckCvssThresholdValue;
import com.sap.oss.phosphor.fosstars.model.value.OwaspDependencyCheckUsage;
import com.sap.oss.phosphor.fosstars.model.value.OwaspDependencyCheckUsageValue;
import com.sap.oss.phosphor.fosstars.model.value.ValueHashSet;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
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
public class UsesOwaspDependencyCheck extends GitHubCachingDataProvider {

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
  public UsesOwaspDependencyCheck(GitHubDataFetcher fetcher) {
    super(fetcher);
  }

  @Override
  public Set<Feature<?>> supportedFeatures() {
    return setOf(
        OWASP_DEPENDENCY_CHECK_USAGE, 
        OWASP_DEPENDENCY_CHECK_FAIL_CVSS_THRESHOLD);
  }

  @Override
  protected ValueSet fetchValuesFor(GitHubProject project) throws IOException {
    logger.info("Figuring out if the project uses OWASP Dependency Check ...");
    LocalRepository repository = GitHubDataFetcher.localRepositoryFor(project);
    return selectBetter(checkMaven(repository), checkGradle(repository));
  }

  private static ValueSet selectBetter(ValueSet firstSet, ValueSet secondSet) {
    OwaspDependencyCheckUsage firstUsage = firstSet.of(OWASP_DEPENDENCY_CHECK_USAGE)
        .map(Value::get).orElse(NOT_USED);
    OwaspDependencyCheckUsage secondUsage = secondSet.of(OWASP_DEPENDENCY_CHECK_USAGE)
        .map(Value::get).orElse(NOT_USED);

    if (firstUsage != secondUsage) {
      return firstUsage.compareTo(secondUsage) < 0 ? firstSet : secondSet;
    }

    OwaspDependencyCheckCvssThresholdValue firstThreshold
        = firstSet.of(OWASP_DEPENDENCY_CHECK_FAIL_CVSS_THRESHOLD)
            .filter(OwaspDependencyCheckCvssThresholdValue.class::isInstance)
            .map(OwaspDependencyCheckCvssThresholdValue.class::cast)
            .orElse(OWASP_DEPENDENCY_CHECK_FAIL_CVSS_THRESHOLD.notSpecifiedValue());
    OwaspDependencyCheckCvssThresholdValue secondThreshold
        = secondSet.of(OWASP_DEPENDENCY_CHECK_FAIL_CVSS_THRESHOLD)
            .filter(OwaspDependencyCheckCvssThresholdValue.class::isInstance)
            .map(OwaspDependencyCheckCvssThresholdValue.class::cast)
            .orElse(OWASP_DEPENDENCY_CHECK_FAIL_CVSS_THRESHOLD.notSpecifiedValue());

    if (!firstThreshold.specified() && !secondThreshold.specified()) {
      return firstSet;
    }

    if (firstThreshold.specified() && !secondThreshold.specified()) {
      return firstSet;
    }

    if (!firstThreshold.specified() && secondThreshold.specified()) {
      return secondSet;
    }

    return firstThreshold.get() > secondThreshold.get() ? firstSet : secondSet;
  }

  /**
   * Checks if a project uses OWASP Dependency Check Maven plugin.
   *
   * @param repository The project's repository.
   * @return A set of corresponding values.
   * @throws IOException If something goes wrong.
   */
  private static ValueSet checkMaven(LocalRepository repository) throws IOException {
    ValueSet values = new ValueHashSet();
    values.update(OWASP_DEPENDENCY_CHECK_USAGE.value(NOT_USED));
    values.update(OWASP_DEPENDENCY_CHECK_FAIL_CVSS_THRESHOLD.notSpecifiedValue());

    Optional<InputStream> content = repository.read("pom.xml");

    if (content.isPresent()) {
      try (InputStream is = content.get()) {
        Model model = readModel(is);

        Visitor visitor = browse(model, withVisitor());

        OwaspDependencyCheckUsageValue usage = OWASP_DEPENDENCY_CHECK_USAGE.value(visitor.usage());

        OwaspDependencyCheckCvssThresholdValue threshold = visitor.threshold()
            .filter(value -> CVSS.MIN <= value && value <= CVSS.MAX)
            .map(OWASP_DEPENDENCY_CHECK_FAIL_CVSS_THRESHOLD::value)
            .orElse(OWASP_DEPENDENCY_CHECK_FAIL_CVSS_THRESHOLD.notSpecifiedValue());

        values.update(usage, threshold);
      }
    }

    return values;
  }

  /**
   * Checks if a project uses OWASP Dependency Check Gradle plugin.
   *
   * @param repository The project's repository.
   * @return A set of corresponding values.
   * @throws IOException If something went wrong.
   */
  private static ValueSet checkGradle(LocalRepository repository) throws IOException {
    ValueSet values = new ValueHashSet();
    values.update(OWASP_DEPENDENCY_CHECK_USAGE.value(NOT_USED));
    values.update(OWASP_DEPENDENCY_CHECK_FAIL_CVSS_THRESHOLD.notSpecifiedValue());

    for (Path gradleFile : repository.files(path -> path.getFileName().endsWith(".gradle"))) {
      Optional<List<String>> something = repository.readLinesOf(gradleFile);
      if (!something.isPresent()) {
        continue;
      }
      List<String> file = something.get();

      if (foundOwaspDependencyCheckInGradle(file)) {
        boolean isMainFile = "build.gradle".equals(gradleFile.toString());

        OwaspDependencyCheckUsageValue usage
            = OWASP_DEPENDENCY_CHECK_USAGE.value(isMainFile ? MANDATORY : OPTIONAL);

        OwaspDependencyCheckCvssThresholdValue threshold = lookForThresholdInGradle(file)
            .filter(value -> CVSS.MIN <= value && value <= CVSS.MAX)
            .map(OWASP_DEPENDENCY_CHECK_FAIL_CVSS_THRESHOLD::value)
            .orElse(OWASP_DEPENDENCY_CHECK_FAIL_CVSS_THRESHOLD.notSpecifiedValue());

        values.update(usage, threshold);

        if (usage.get() == MANDATORY) {
          break;
        }
      }
    }

    return values;
  }

  /**
   * Searches for a threshold for OWASP Dependency Check in a Gradle file.
   *
   * @param content The content of the file.
   * @return The threshold if found.
   */
  private static Optional<Double> lookForThresholdInGradle(List<String> content) {
    boolean foundDependencyCheckConfig = false;
    for (String line : content) {
      line = line.trim();

      if (line.contains("dependencyCheck")) {
        foundDependencyCheckConfig = true;
        continue;
      }

      if (!foundDependencyCheckConfig) {
        continue;
      }

      if ("}".equals(line)) {
        break;
      }

      // check for Dependency Check parameters
      for (Map.Entry<String, String> entry : FAIL_BUILD_CONFIGURATIONS.entrySet()) {
        String[] parts = line.split("=");
        if (parts.length != 2) {
          continue;
        }

        String name = entry.getKey();
        if (!parts[0].trim().equals(name)) {
          continue;
        }

        String value = parts[1].trim();
        String type = entry.getValue();
        Double score = parseScore(value, type);

        // stop once one of the parameters is found
        return Optional.ofNullable(score);
      }
    }

    return Optional.empty();
  }

  /**
   * Checks if a Gradle file calls OWASP Dependency Check.
   *
   * @param content The content of the file.
   * @return True if the file calls the tool, false otherwise.
   */
  private static boolean foundOwaspDependencyCheckInGradle(List<String> content) {
    return content.stream()
        .filter(line -> line.contains("apply plugin:"))
        .anyMatch(line -> line.contains("org.owasp.dependencycheck"));
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
  private static class Visitor extends AbstractModelVisitor {

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