package com.sap.sgs.phosphor.fosstars.model.score.oss;

import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.VULNERABILITIES;
import static com.sap.sgs.phosphor.fosstars.model.other.Utils.findValue;
import static com.sap.sgs.phosphor.fosstars.model.value.Vulnerability.Resolution.UNPATCHED;

import com.sap.sgs.phosphor.fosstars.model.Score;
import com.sap.sgs.phosphor.fosstars.model.Value;
import com.sap.sgs.phosphor.fosstars.model.qa.ScoreVerification;
import com.sap.sgs.phosphor.fosstars.model.qa.TestVectors;
import com.sap.sgs.phosphor.fosstars.model.score.FeatureBasedScore;
import com.sap.sgs.phosphor.fosstars.model.value.CVSS;
import com.sap.sgs.phosphor.fosstars.model.value.ScoreValue;
import com.sap.sgs.phosphor.fosstars.model.value.Vulnerabilities;
import com.sap.sgs.phosphor.fosstars.model.value.Vulnerability;
import java.io.IOException;
import java.io.InputStream;

/**
 * <p>The score analyses unpatched vulnerabilities in an open-source project.
 * For each unpatched vulnerability, the score functions applies a penalty to the overall score.
 * The penalty depends on severity of a particular vulnerability.</p>
 */
public class UnpatchedVulnerabilitiesScore extends FeatureBasedScore {

  /**
   * The default CVSS score for a vulnerability if no score specified.
   */
  private static final double DEFAULT_CVSS = 10.0;

  /**
   * The penalty for vulnerabilities with high severity.
   */
  private static final double HIGH_SEVERITY_PENALTY = 8.0;

  /**
   * The penalty for vulnerabilities with medium severity.
   */
  private static final double MEDIUM_SEVERITY_PENALTY = 4.0;

  /**
   * The penalty for vulnerabilities with low severity.
   */
  private static final double LOW_SEVERITY_PENALTY = 1.0;

  /**
   * This is a template for explanation messages.
   */
  private static final String EXPLANATION_TEMPLATE =
      "Found %d unpatched vulnerabilities with %s severity "
          + "which decreased the score on %2.2f (%d * %2.2f)";

  /**
   * Initializes a new score.
   */
  UnpatchedVulnerabilitiesScore() {
    super("How well vulnerabilities are patched", VULNERABILITIES);
  }

  @Override
  public ScoreValue calculate(Value... values) {
    Value<Vulnerabilities> vulnerabilities = findValue(values, VULNERABILITIES,
        "Hey! Give me info about vulnerabilities!");

    if (vulnerabilities.isUnknown()) {
      return scoreValue(Score.MIN, vulnerabilities);
    }

    int highSeverityIssues = 0;
    int mediumSeverityIssues = 0;
    int lowSeverityIssues = 0;

    // TODO: should confidence depend on a number of vulnerabilities with unknown resolution?
    for (Vulnerability entry : vulnerabilities.get().entries()) {
      if (entry.resolution() != UNPATCHED) {
        continue;
      }

      CVSS cvss = entry.cvss();
      double value = cvss.isUnknown() ? DEFAULT_CVSS : cvss.value();

      if (value >= 7.0) {
        highSeverityIssues++;
      } else if (value >= 4) {
        mediumSeverityIssues++;
      } else {
        lowSeverityIssues++;
      }
    }

    ScoreValue scoreValue = scoreValue(Score.MAX, vulnerabilities);

    applyPenaltyIfNecessary(scoreValue, highSeverityIssues, "high", HIGH_SEVERITY_PENALTY);
    applyPenaltyIfNecessary(scoreValue, mediumSeverityIssues, "medium", MEDIUM_SEVERITY_PENALTY);
    applyPenaltyIfNecessary(scoreValue, lowSeverityIssues, "low", LOW_SEVERITY_PENALTY);

    if (highSeverityIssues == 0 && mediumSeverityIssues == 0 && lowSeverityIssues == 0) {
      scoreValue.explain("No unpatched vulnerabilities found which is good");
    }

    return scoreValue;
  }

  /**
   * Apply a penalty to a score value if issues found.
   *
   * @param scoreValue The score value to be updated.
   * @param issues The number of issues.
   * @param severity The severity of issues in human-readable format.
   * @param penalty The penalty for one issue.
   */
  private static void applyPenaltyIfNecessary(
      ScoreValue scoreValue, int issues, String severity, double penalty) {

    if (issues > 0) {
      double overallPenalty = penalty * issues;
      scoreValue.decrease(overallPenalty);
      scoreValue.explain(String.format(EXPLANATION_TEMPLATE,
          issues, severity, overallPenalty, issues, penalty));
    }
  }

  /**
   * This class implements a verification procedure for {@link UnpatchedVulnerabilitiesScore}.
   * The class loads test vectors,
   * and provides methods to verify a {@link UnpatchedVulnerabilitiesScore}
   * against those test vectors.
   */
  public static class Verification extends ScoreVerification {

    /**
     * A name of a resource which contains the test vectors.
     */
    private static final String TEST_VECTORS_YAML = "UnpatchedVulnerabilitiesScoreTestVectors.yml";

    /**
     * Initializes a {@link Verification} for a {@link UnpatchedVulnerabilitiesScore}.
     *
     * @param score A score to be verified.
     * @param vectors A list of test vectors.
     */
    public Verification(UnpatchedVulnerabilitiesScore score, TestVectors vectors) {
      super(score, vectors);
    }

    /**
     * Creates an instance of {@link Verification} for a specified score. The method loads test
     * vectors from a default resource.
     *
     * @param score The score to be verified.
     * @return An instance of {@link UnpatchedVulnerabilitiesScore}.
     */
    static Verification createFor(UnpatchedVulnerabilitiesScore score) throws IOException {
      try (InputStream is = Verification.class.getResourceAsStream(TEST_VECTORS_YAML)) {
        return new Verification(score, TestVectors.loadFromYaml(is));
      }
    }
  }
}
