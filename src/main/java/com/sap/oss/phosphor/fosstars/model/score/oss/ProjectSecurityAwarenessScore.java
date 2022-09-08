package com.sap.oss.phosphor.fosstars.model.score.oss;

import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.FUZZED_IN_OSS_FUZZ;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.HAS_BUG_BOUNTY_PROGRAM;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.HAS_EXECUTABLE_BINARIES;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.HAS_SECURITY_POLICY;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.HAS_SECURITY_TEAM;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.OWASP_DEPENDENCY_CHECK_USAGE;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.SIGNS_ARTIFACTS;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_ADDRESS_SANITIZER;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_DEPENDABOT;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_FIND_SEC_BUGS;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_LGTM_CHECKS;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_MEMORY_SANITIZER;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_NOHTTP;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_OWASP_ESAPI;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_OWASP_JAVA_ENCODER;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_OWASP_JAVA_HTML_SANITIZER;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_SIGNED_COMMITS;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_SNYK;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_UNDEFINED_BEHAVIOR_SANITIZER;
import static com.sap.oss.phosphor.fosstars.model.value.OwaspDependencyCheckUsage.NOT_USED;

import com.sap.oss.phosphor.fosstars.model.Feature;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.score.FeatureBasedScore;
import com.sap.oss.phosphor.fosstars.model.value.OwaspDependencyCheckUsage;
import com.sap.oss.phosphor.fosstars.model.value.ScoreValue;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang3.ArrayUtils;

/**
 * <p>The security awareness score is currently based on the following features.</p>
 * <ul>
 *   <li>
 *     {@link com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures#HAS_EXECUTABLE_BINARIES}
 *   </li>
 *   <li>
 *     {@link com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures#HAS_SECURITY_POLICY}
 *   </li>
 *   <li>
 *     {@link com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures#HAS_SECURITY_TEAM}
 *   </li>
 *   <li>
 *     {@link com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures#HAS_BUG_BOUNTY_PROGRAM}
 *   </li>
 *   <li>
 *     {@link com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures#USES_SIGNED_COMMITS}
 *   </li>
 *   <li>
 *     {@link com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures#SIGNS_ARTIFACTS}
 *   </li>
 *   <li>
 *     {@link com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures#FUZZED_IN_OSS_FUZZ}
 *   </li>
 *   <li>
 *     {@link
 *     com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures#OWASP_DEPENDENCY_CHECK_USAGE}
 *   </li>
 *   <li>
 *     {@link com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures#USES_DEPENDABOT}
 *   </li>
 *   <li>
 *     {@link com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures#USES_SNYK}
 *   </li>
 *   <li>
 *     {@link com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures#USES_NOHTTP}
 *   </li>
 *   <li>
 *     {@link com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures#USES_LGTM_CHECKS}
 *   </li>
 *   <li>
 *     {@link com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures#USES_FIND_SEC_BUGS}
 *   </li>
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
 *   <li>
 *     {@link com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures#USES_ADDRESS_SANITIZER}
 *   </li>
 *   <li>
 *     {@link com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures#USES_MEMORY_SANITIZER}
 *   </li>
 *   <li>
 *     {@link com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures
 *     #USES_UNDEFINED_BEHAVIOR_SANITIZER}
 *   </li>
 *   <li>
 *      {@link com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures
 *      #OWASP_DEPENDENCY_CHECK_USAGE}
 *   </li>
 * </ul>
 * <p>A project gets the maximum score if it has both a security policy and a security team.</p>
 */
public class ProjectSecurityAwarenessScore extends FeatureBasedScore {

  /**
   * A number of points which are added to a score value if a project has a security policy.
   */
  private static final double SECURITY_POLICY_POINTS = 2.0;

  /**
   * A number of points which are added to a score value if a project has a security team.
   */
  private static final double SECURITY_TEAM_POINTS = 3.0;

  /**
   * A number of points which are added to a score value if a project uses verified signed commits.
   */
  private static final double SIGNED_COMMITS_POINTS = 0.5;

  /**
   * A number of points which are added to a score value if a project signs its artifacts.
   */
  private static final double SIGNED_ARTIFACTS_POINTS = 0.5;

  /**
   * A number of points which are added to a score value if a project has a bug bounty program.
   */
  private static final double BUG_BOUNTY_PROGRAM_POINTS = 4.0;

  /**
   * A number of points which are added to a score value
   * if a project uses a security tool or library.
   */
  private static final double SECURITY_TOOL_POINTS = 1.0;

  /**
   * A number of points which are subtracted from a score value if a project has executable
   * binaries.
   */
  private static final double EXECUTABLE_BINARIES_POINTS = 2.0;

  /**
   * A description of the score.
   */
  private static final String DESCRIPTION = String.format(
      "The score shows how a project is aware of security. "
          + "If the project has a security policy, then the score adds %2.2f. "
          + "If the project has a security team, then the score adds %2.2f. "
          + "If the project uses verified signed commits, then the score adds %2.2f. "
          + "If the project has a bug bounty program, then the score adds %2.2f. "
          + "If the project signs its artifacts, then the score adds %2.2f. "
          + "If the project uses a security tool or library, then the score adds %2.2f. "
          + "If the project has executable binaries, then the score subtracts %2.2f.",
      SECURITY_POLICY_POINTS, SECURITY_TEAM_POINTS, SIGNED_COMMITS_POINTS,
      BUG_BOUNTY_PROGRAM_POINTS, SIGNED_ARTIFACTS_POINTS, SECURITY_TOOL_POINTS,
      EXECUTABLE_BINARIES_POINTS);

  /**
   * Features that tell if a project uses specific security tools.
   */
  private static final Feature<?>[] SECURITY_TOOLS_FEATURES = new Feature[] {
      FUZZED_IN_OSS_FUZZ,
      USES_DEPENDABOT, USES_SNYK,
      USES_NOHTTP, USES_LGTM_CHECKS, USES_FIND_SEC_BUGS,
      USES_OWASP_ESAPI, USES_OWASP_JAVA_ENCODER, USES_OWASP_JAVA_HTML_SANITIZER,
      USES_ADDRESS_SANITIZER, USES_MEMORY_SANITIZER, USES_UNDEFINED_BEHAVIOR_SANITIZER,
      OWASP_DEPENDENCY_CHECK_USAGE
  };

  /**
   * Initializes a new {@link ProjectSecurityAwarenessScore}.
   */
  ProjectSecurityAwarenessScore() {
    super("How well open-source community is aware about security", DESCRIPTION,
        ArrayUtils.addAll(SECURITY_TOOLS_FEATURES,
            HAS_SECURITY_POLICY, HAS_SECURITY_TEAM, HAS_BUG_BOUNTY_PROGRAM,
            USES_SIGNED_COMMITS, SIGNS_ARTIFACTS, HAS_EXECUTABLE_BINARIES));
  }

  @Override
  public ScoreValue calculate(Value<?>... values) {
    Value<Boolean> securityPolicy = find(HAS_SECURITY_POLICY, values);
    Value<Boolean> securityTeam = find(HAS_SECURITY_TEAM, values);
    Value<Boolean> signedCommits = find(USES_SIGNED_COMMITS, values);
    Value<Boolean> hasBugBountyProgram = find(HAS_BUG_BOUNTY_PROGRAM, values);
    Value<Boolean> signsArtifacts = find(SIGNS_ARTIFACTS, values);
    Value<Boolean> hasExecutableBinaries = find(HAS_EXECUTABLE_BINARIES, values);

    List<Value<?>> securityToolsValues = new ArrayList<>();
    Arrays.stream(SECURITY_TOOLS_FEATURES)
        .forEach(feature -> securityToolsValues.add(find(feature, values)));

    List<Value<?>> usedValues = new ArrayList<>();
    usedValues.addAll(Arrays.asList(securityPolicy, securityTeam, signedCommits,
        hasBugBountyProgram, signsArtifacts, hasExecutableBinaries));
    usedValues.addAll(securityToolsValues);

    ScoreValue scoreValue = scoreValue(MIN, usedValues);

    if (allUnknown(scoreValue.usedValues())) {
      return scoreValue.makeUnknown();
    }

    securityPolicy.processIfKnown(exists -> {
      if (exists) {
        scoreValue.increase(SECURITY_POLICY_POINTS);
      }
    });

    securityTeam.processIfKnown(exists -> {
      if (exists) {
        scoreValue.increase(SECURITY_TEAM_POINTS);
      }
    });

    signedCommits.processIfKnown(yes -> {
      if (yes) {
        scoreValue.increase(SIGNED_COMMITS_POINTS);
      }
    });

    hasBugBountyProgram.processIfKnown(exists -> {
      if (exists) {
        scoreValue.increase(BUG_BOUNTY_PROGRAM_POINTS);
      }
    });

    signsArtifacts.processIfKnown(yes -> {
      if (yes) {
        scoreValue.increase(SIGNED_ARTIFACTS_POINTS);
      }
    });

    hasExecutableBinaries.processIfKnown(yes -> {
      if (yes) {
        scoreValue.decrease(EXECUTABLE_BINARIES_POINTS);
      }
    });

    long n = securityToolsValues.stream().filter(ProjectSecurityAwarenessScore::usedSecurityTools)
        .count();
    scoreValue.increase(n * SECURITY_TOOL_POINTS);

    return scoreValue;
  }

  /**
   * Checks if a value means that one of the security tools is used in a project.
   *
   * @param value The value to be examined.
   * @return True if a value means that one of the security tools is used, false otherwise.
   * @throws IllegalArgumentException In case of an unexpected type of the value.
   */
  private static boolean usedSecurityTools(Value<?> value) {
    if (value.isUnknown()) {
      return false;
    }

    Object object = value.get();
    if (object instanceof Boolean) {
      return (Boolean) object;
    }

    if (object instanceof OwaspDependencyCheckUsage) {
      OwaspDependencyCheckUsage usage = (OwaspDependencyCheckUsage) object;
      return usage != NOT_USED;
    }

    throw new IllegalArgumentException(
        String.format("Hey! This is an unexpected value: %s", value));
  }
}
