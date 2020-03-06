package com.sap.sgs.phosphor.fosstars.model.score.oss;

import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.VULNERABILITIES;
import static com.sap.sgs.phosphor.fosstars.model.other.Utils.findValue;
import static com.sap.sgs.phosphor.fosstars.model.value.Vulnerability.Resolution.UNPATCHED;

import com.sap.sgs.phosphor.fosstars.model.Score;
import com.sap.sgs.phosphor.fosstars.model.Value;
import com.sap.sgs.phosphor.fosstars.model.qa.ScoreVerification;
import com.sap.sgs.phosphor.fosstars.model.qa.TestVector;
import com.sap.sgs.phosphor.fosstars.model.score.FeatureBasedScore;
import com.sap.sgs.phosphor.fosstars.model.value.CVSS;
import com.sap.sgs.phosphor.fosstars.model.value.ScoreValue;
import com.sap.sgs.phosphor.fosstars.model.value.Vulnerabilities;
import com.sap.sgs.phosphor.fosstars.model.value.Vulnerability;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * The score analyses unpatched vulnerabilities in an open-source project.
 * For each unpatched vulnerability, the score functions applies a penalty to the overall score.
 * The penalty depends on severity of a particular vulnerability.
 */
public class UnpatchedVulnerabilitiesScore extends FeatureBasedScore {

  /**
   * The default CVSS score for a vulnerability if no score specified.
   */
  private static final double DEFAULT_CVSS = 10.0;

  /**
   * The penalty for vulnerabilities with high severity.
   */
  private static final double HIGH_SEVERITY_PENALTY = 10.0;

  /**
   * The penalty for vulnerabilities with medium severity.
   */
  private static final double MEDIUM_SEVERITY_PENALTY = 2.0;

  /**
   * The penalty for vulnerabilities with low severity.
   */
  private static final double LOW_SEVERITY_PENALTY = 1.0;

  /**
   * Default constructor.
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

    int highSeverity = 0;
    int mediumSeverity = 0;
    int lowSeverity = 0;

    // TODO: should confidence depend on a number of vulnerabilities with unknown resolution?
    for (Vulnerability entry : vulnerabilities.get().entries()) {
      if (entry.resolution() != UNPATCHED) {
        continue;
      }

      CVSS cvss = entry.cvss();
      double value = cvss.isUnknown() ? DEFAULT_CVSS : cvss.value();

      if (value >= 7.0) {
        highSeverity++;
      } else if (value >= 4) {
        mediumSeverity++;
      } else {
        lowSeverity++;
      }
    }

    double scorePoints = Score.MAX;

    scorePoints -= HIGH_SEVERITY_PENALTY * highSeverity;
    scorePoints -= MEDIUM_SEVERITY_PENALTY * mediumSeverity;
    scorePoints -= LOW_SEVERITY_PENALTY * lowSeverity;

    return scoreValue(scorePoints, vulnerabilities);
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
    private static final String TEST_VECTORS_CSV = "UnpatchedVulnerabilitiesScoreTestVectors.csv";

    /**
     * Initializes a {@link Verification} for a {@link UnpatchedVulnerabilitiesScore}.
     *
     * @param score A score to be verified.
     * @param vectors A list of test vectors.
     */
    public Verification(UnpatchedVulnerabilitiesScore score, List<TestVector> vectors) {
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
      try (InputStream is = UnpatchedVulnerabilitiesScore.Verification.class
          .getResourceAsStream(TEST_VECTORS_CSV)) {

        return new Verification(score, loadTestVectorsFromCsvResource(score.features(), is));
      }
    }
  }
}
