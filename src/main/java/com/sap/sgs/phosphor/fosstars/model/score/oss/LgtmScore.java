package com.sap.sgs.phosphor.fosstars.model.score.oss;

import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.WORST_LGTM_GRADE;
import static com.sap.sgs.phosphor.fosstars.model.other.Utils.findValue;

import com.sap.sgs.phosphor.fosstars.model.Value;
import com.sap.sgs.phosphor.fosstars.model.qa.ScoreVerification;
import com.sap.sgs.phosphor.fosstars.model.qa.TestVectors;
import com.sap.sgs.phosphor.fosstars.model.score.FeatureBasedScore;
import com.sap.sgs.phosphor.fosstars.model.value.LgtmGrade;
import com.sap.sgs.phosphor.fosstars.model.value.ScoreValue;
import java.io.IOException;
import java.io.InputStream;
import java.util.EnumMap;

/**
 * <p>The score shows if and how a project addresses issues reported by LGTM. The score is based
 * on the {@link com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures#WORST_LGTM_GRADE}
 * feature.</p>
 */
public class LgtmScore extends FeatureBasedScore {

  /**
   * Defines how LGTM grades contribute to the score value.
   */
  private static final EnumMap<LgtmGrade, Double> GRADE_TO_POINTS = new EnumMap<>(LgtmGrade.class);

  static {
    GRADE_TO_POINTS.put(LgtmGrade.A_PLUS, 10.0);
    GRADE_TO_POINTS.put(LgtmGrade.A, 9.0);
    GRADE_TO_POINTS.put(LgtmGrade.B, 7.0);
    GRADE_TO_POINTS.put(LgtmGrade.C, 5.0);
    GRADE_TO_POINTS.put(LgtmGrade.D, 2.0);
    GRADE_TO_POINTS.put(LgtmGrade.E, 0.0);
  }

  /**
   * Initializes a new {@link LgtmScore}.
   */
  LgtmScore() {
    super("How a project addresses issues reported by LGTM", WORST_LGTM_GRADE);
  }

  @Override
  public ScoreValue calculate(Value... values) {
    Value<LgtmGrade> worstLgtmGrade = findValue(values, WORST_LGTM_GRADE,
        "Hey! You have to tell me the worst LGTM grade for the project!");

    // TODO: The score should check languages that CodeQL supports

    ScoreValue scoreValue = scoreValue(MIN, worstLgtmGrade);

    if (worstLgtmGrade.isUnknown()) {
      return scoreValue.makeUnknown();
    }

    worstLgtmGrade.processIfKnown(grade -> scoreValue.increase(GRADE_TO_POINTS.get(grade)));

    return scoreValue;
  }

  /**
   * This class implements a verification procedure for {@link LgtmScore}. The class loads test
   * vectors, and provides methods to verify a {@link LgtmScore} against those test vectors.
   */
  public static class Verification extends ScoreVerification {

    /**
     * A name of a resource which contains the test vectors.
     */
    private static final String TEST_VECTORS_YAML = "LgtmScoreTestVectors.yml";

    /**
     * Initializes a {@link Verification} for a {@link LgtmScore}.
     *
     * @param score A score to be verified.
     * @param vectors A list of test vectors.
     */
    public Verification(LgtmScore score, TestVectors vectors) {
      super(score, vectors);
    }

    /**
     * Creates an instance of {@link Verification} for a specified score. The method loads test
     * vectors from a default resource.
     *
     * @param score The score to be verified.
     * @return An instance of {@link Verification}.
     */
    static Verification createFor(LgtmScore score) throws IOException {
      try (InputStream is = Verification.class.getResourceAsStream(TEST_VECTORS_YAML)) {
        return new Verification(score, TestVectors.loadFromYaml(is));
      }
    }
  }
}
