package com.sap.sgs.phosphor.fosstars.model.score.oss;

import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.LANGUAGES;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.WORST_LGTM_GRADE;
import static com.sap.sgs.phosphor.fosstars.model.other.Utils.findValue;
import static com.sap.sgs.phosphor.fosstars.model.value.Language.C;
import static com.sap.sgs.phosphor.fosstars.model.value.Language.CPP;
import static com.sap.sgs.phosphor.fosstars.model.value.Language.C_SHARP;
import static com.sap.sgs.phosphor.fosstars.model.value.Language.GO;
import static com.sap.sgs.phosphor.fosstars.model.value.Language.JAVA;
import static com.sap.sgs.phosphor.fosstars.model.value.Language.JAVASCRIPT;
import static com.sap.sgs.phosphor.fosstars.model.value.Language.PYTHON;
import static com.sap.sgs.phosphor.fosstars.model.value.Language.TYPESCRIPT;

import com.sap.sgs.phosphor.fosstars.model.Value;
import com.sap.sgs.phosphor.fosstars.model.qa.ScoreVerification;
import com.sap.sgs.phosphor.fosstars.model.qa.TestVectors;
import com.sap.sgs.phosphor.fosstars.model.score.FeatureBasedScore;
import com.sap.sgs.phosphor.fosstars.model.value.Languages;
import com.sap.sgs.phosphor.fosstars.model.value.LgtmGrade;
import com.sap.sgs.phosphor.fosstars.model.value.ScoreValue;
import java.io.IOException;
import java.io.InputStream;
import java.util.EnumMap;

/**
 * <p>The score shows if and how a project addresses issues reported by LGTM. The score is based
 * on the for following features.</p>
 * <ul>
 *   <li>{@link com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures#WORST_LGTM_GRADE}</li>
 *   <li>{@link com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures#LANGUAGES}</li>
 * </ul>
 */
public class LgtmScore extends FeatureBasedScore {

  /**
   * Programming languages supported by CodeQL.
   *
   * @see <a href="https://lgtm.com/help/lgtm/about-lgtm">About LGTM</a>
   */
  private static final Languages SUPPORTED_LANGUAGES = Languages.of(
      C, CPP, JAVA, C_SHARP, PYTHON, GO, JAVASCRIPT, TYPESCRIPT);

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
    super("How a project addresses issues reported by LGTM", WORST_LGTM_GRADE, LANGUAGES);
  }

  @Override
  public ScoreValue calculate(Value... values) {
    Value<LgtmGrade> worstLgtmGrade = findValue(values, WORST_LGTM_GRADE,
        "Hey! You have to tell me the worst LGTM grade for the project!");
    Value<Languages> languages = findValue(values, LANGUAGES,
        "Hey! You have to tell me which languages the project uses!");

    ScoreValue scoreValue = scoreValue(MIN, worstLgtmGrade, languages);

    if (allUnknown(worstLgtmGrade, languages)) {
      return scoreValue.makeUnknown();
    }

    if (!languages.isUnknown() && !SUPPORTED_LANGUAGES.containsAnyOf(languages.get())) {
      return scoreValue.makeNotApplicable().explain(
          "The score is N/A because the project uses languages that are not supported by LGTM.");
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
