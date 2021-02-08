package com.sap.oss.phosphor.fosstars.model.score.oss;

import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.LANGUAGES;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.WORST_LGTM_GRADE;
import static com.sap.oss.phosphor.fosstars.model.other.Utils.findValue;
import static com.sap.oss.phosphor.fosstars.model.value.Language.C;
import static com.sap.oss.phosphor.fosstars.model.value.Language.CPP;
import static com.sap.oss.phosphor.fosstars.model.value.Language.C_SHARP;
import static com.sap.oss.phosphor.fosstars.model.value.Language.GO;
import static com.sap.oss.phosphor.fosstars.model.value.Language.JAVA;
import static com.sap.oss.phosphor.fosstars.model.value.Language.JAVASCRIPT;
import static com.sap.oss.phosphor.fosstars.model.value.Language.PYTHON;
import static com.sap.oss.phosphor.fosstars.model.value.Language.TYPESCRIPT;

import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.score.FeatureBasedScore;
import com.sap.oss.phosphor.fosstars.model.value.Languages;
import com.sap.oss.phosphor.fosstars.model.value.LgtmGrade;
import com.sap.oss.phosphor.fosstars.model.value.ScoreValue;
import java.util.EnumMap;

/**
 * <p>The score shows if and how a project addresses issues reported by LGTM. The score is based
 * on the for following features.</p>
 * <ul>
 *   <li>{@link com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures#WORST_LGTM_GRADE}</li>
 *   <li>{@link com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures#LANGUAGES}</li>
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
  public ScoreValue calculate(Value<?>... values) {
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
}
