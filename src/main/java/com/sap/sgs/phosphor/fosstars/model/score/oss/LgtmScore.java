package com.sap.sgs.phosphor.fosstars.model.score.oss;

import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.USES_LGTM;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.WORST_LGTM_GRADE;
import static com.sap.sgs.phosphor.fosstars.model.other.Utils.findValue;

import com.sap.sgs.phosphor.fosstars.model.Confidence;
import com.sap.sgs.phosphor.fosstars.model.Value;
import com.sap.sgs.phosphor.fosstars.model.qa.ScoreVerification;
import com.sap.sgs.phosphor.fosstars.model.qa.TestVector;
import com.sap.sgs.phosphor.fosstars.model.score.FeatureBasedScore;
import com.sap.sgs.phosphor.fosstars.model.value.LgtmGrade;
import com.sap.sgs.phosphor.fosstars.model.value.ScoreValue;
import java.io.IOException;
import java.io.InputStream;
import java.util.EnumMap;
import java.util.List;

/**
 * The score shows if and how a project addresses issues reported by LGTM.
 * The score is based on the following features
 * <ul>
 *   <li>{@link com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures#USES_LGTM}</li>
 *   <li>{@link com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures#WORST_LGTM_GRADE}</li>
 * </ul>
 */
public class LgtmScore extends FeatureBasedScore {

  private static final double LGTM_USAGE_POINTS = 2.0;

  private static final EnumMap<LgtmGrade, Double> GRADE_TO_POINTS = new EnumMap<>(LgtmGrade.class);

  static {
    GRADE_TO_POINTS.put(LgtmGrade.A_PLUS, 8.0);
    GRADE_TO_POINTS.put(LgtmGrade.A, 8.0);
    GRADE_TO_POINTS.put(LgtmGrade.B, 6.0);
    GRADE_TO_POINTS.put(LgtmGrade.C, 4.0);
    GRADE_TO_POINTS.put(LgtmGrade.D, 2.0);
    GRADE_TO_POINTS.put(LgtmGrade.E, 0.0);
  }

  /**
   * Initializes a new {@link LgtmScore}.
   */
  LgtmScore() {
    super("How a project addresses issues reported by LGTM",
        USES_LGTM, WORST_LGTM_GRADE);
  }

  @Override
  public ScoreValue calculate(Value... values) {
    Value<Boolean> usesLgtm = findValue(values, USES_LGTM,
        "Hey! You have to tell me if the project uses LGTM!");
    Value<LgtmGrade> worstLgtmGrade = findValue(values, WORST_LGTM_GRADE,
        "Hey! You have to tell me the worst LGTM grade for the project!");

    if (usesLgtm.isUnknown() && !worstLgtmGrade.isUnknown()) {
      throw new IllegalArgumentException(
          "Hey! It's unknown if the project uses LGTM but then you gave me the worst grade!");
    }

    ScoreValue scoreValue = new ScoreValue(this);

    usesLgtm.processIfKnown(uses -> {
      if (uses) {
        scoreValue.increase(LGTM_USAGE_POINTS);
      } else if (!worstLgtmGrade.isUnknown()) {
        throw new IllegalArgumentException(
            "Hey! You told me that LGTM is not used but then provided the worst grade!");
      }
    });

    worstLgtmGrade.processIfKnown(grade -> scoreValue.increase(GRADE_TO_POINTS.get(grade)));

    scoreValue.confidence(Confidence.make(usesLgtm, worstLgtmGrade));
    scoreValue.usedValues(usesLgtm, worstLgtmGrade);

    return scoreValue;
  }

  /**
   * This class implements a verification procedure for {@link LgtmScore}.
   * The class loads test vectors, and provides methods to verify a {@link LgtmScore}
   * against those test vectors.
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
    public Verification(LgtmScore score, List<TestVector> vectors) {
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
        return new Verification(score, loadTestVectorsFromYaml(is));
      }
    }
  }
}
