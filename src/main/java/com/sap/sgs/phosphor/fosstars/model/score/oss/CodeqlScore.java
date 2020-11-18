package com.sap.sgs.phosphor.fosstars.model.score.oss;

import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.RUNS_CODEQL_SCANS;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.USES_CODEQL_CHECKS;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.USES_LGTM_CHECKS;
import static com.sap.sgs.phosphor.fosstars.model.other.Utils.findValue;

import com.sap.sgs.phosphor.fosstars.model.Value;
import com.sap.sgs.phosphor.fosstars.model.qa.ScoreVerification;
import com.sap.sgs.phosphor.fosstars.model.qa.TestVectors;
import com.sap.sgs.phosphor.fosstars.model.score.FeatureBasedScore;
import com.sap.sgs.phosphor.fosstars.model.value.ScoreValue;
import java.io.IOException;
import java.io.InputStream;

/**
 * <p>The score shows if and how a project uses static analysis with CodeQL.
 * The score is based on the following features.</p>
 * <ul>
 *  <li>{@link com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures#USES_LGTM_CHECKS}</li>
 *  <li>{@link com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures#USES_CODEQL_CHECKS}</li>
 *  <li>{@link com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures#RUNS_CODEQL_SCANS}</li>
 * </ul>
 */
public class CodeqlScore extends FeatureBasedScore {

  /**
   * Defines how the score value is increased if a project runs CodeQL scans.
   */
  private static final double CODEQL_SCANS_POINTS = 5.0;

  /**
   * Defines how the score value is increased if a project runs CodeQL checks for commits.
   */
  private static final double CODEQL_CHECKS_POINTS = 7.0;

  /**
   * Initializes a new {@link CodeqlScore}.
   */
  CodeqlScore() {
    super("How a project uses CodeQL", USES_LGTM_CHECKS, USES_CODEQL_CHECKS, RUNS_CODEQL_SCANS);
  }

  @Override
  public ScoreValue calculate(Value... values) {
    Value<Boolean> usesLgtmChecks = findValue(values, USES_LGTM_CHECKS,
        "Hey! You have to tell me if the project uses LGTM checks!");
    Value<Boolean> usesCodeqlChecks = findValue(values, USES_CODEQL_CHECKS,
        "Hey! You have to tell me if the project uses CodeQL checks!");
    Value<Boolean> runsCodeqlScans = findValue(values, RUNS_CODEQL_SCANS,
        "Hey! You have to tell me if the project runs CodeQL scans!");

    // TODO: The score should check languages that CodeQL supports

    ScoreValue scoreValue = scoreValue(MIN, usesLgtmChecks, usesCodeqlChecks, runsCodeqlScans);

    if (allUnknown(usesLgtmChecks, usesCodeqlChecks, runsCodeqlScans)) {
      return scoreValue.makeUnknown();
    }

    if (usesLgtmChecks.orElse(false) || usesCodeqlChecks.orElse(false)) {
      scoreValue.increase(CODEQL_CHECKS_POINTS);
    }

    if (runsCodeqlScans.orElse(false)) {
      scoreValue.increase(CODEQL_SCANS_POINTS);
    }

    return scoreValue;
  }

  /**
   * This class implements a verification procedure for {@link CodeqlScore}. The class loads test
   * vectors, and provides methods to verify a {@link CodeqlScore} against those test vectors.
   */
  public static class Verification extends ScoreVerification {

    /**
     * A name of a resource which contains the test vectors.
     */
    private static final String TEST_VECTORS_YAML = "CodeqlScoreTestVectors.yml";

    /**
     * Initializes a {@link Verification} for a {@link CodeqlScore}.
     *
     * @param score A score to be verified.
     * @param vectors A list of test vectors.
     */
    public Verification(CodeqlScore score, TestVectors vectors) {
      super(score, vectors);
    }

    /**
     * Creates an instance of {@link Verification} for a specified score. The method loads test
     * vectors from a default resource.
     *
     * @param score The score to be verified.
     * @return An instance of {@link Verification}.
     */
    static Verification createFor(CodeqlScore score) throws IOException {
      try (InputStream is = Verification.class.getResourceAsStream(TEST_VECTORS_YAML)) {
        return new Verification(score, TestVectors.loadFromYaml(is));
      }
    }
  }
}
