package com.sap.oss.phosphor.fosstars.model.score.oss;

import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.FUZZED_IN_OSS_FUZZ;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.LANGUAGES;
import static com.sap.oss.phosphor.fosstars.model.value.Language.C;
import static com.sap.oss.phosphor.fosstars.model.value.Language.CPP;
import static java.lang.Boolean.FALSE;

import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.score.FeatureBasedScore;
import com.sap.oss.phosphor.fosstars.model.value.Languages;
import com.sap.oss.phosphor.fosstars.model.value.ScoreValue;

/**
 * <p>The score shows how an open-source project uses fuzzing to find security issues.</p>
 *
 * <p>The score is based on the following features.</p>
 * <ul>
 *   <li>{@link com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures#LANGUAGES}</li>
 *   <li>{@link com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures#FUZZED_IN_OSS_FUZZ}</li>
 * </ul>
 */
public class FuzzingScore extends FeatureBasedScore {

  /**
   * Programming languages that allow direct access to memory.
   */
  private static final Languages UNSAFE_LANGUAGES = Languages.of(C, CPP);

  /**
   * Initializes a new score.
   */
  FuzzingScore() {
    super("How a project uses fuzzing",
        LANGUAGES, FUZZED_IN_OSS_FUZZ);
  }

  @Override
  public ScoreValue calculate(Value<?>... values) {
    Value<Languages> languages = find(LANGUAGES, values);
    Value<Boolean> fuzzedInOssFuzz = find(FUZZED_IN_OSS_FUZZ, values);

    ScoreValue scoreValue = scoreValue(MIN, languages, fuzzedInOssFuzz);

    if (allUnknown(scoreValue.usedValues())) {
      return scoreValue.makeUnknown();
    }

    boolean applicable = languages.isUnknown() || languages.get().containsAnyOf(UNSAFE_LANGUAGES);
    if (!applicable) {
      return scoreValue.makeNotApplicable();
    }

    return fuzzedInOssFuzz.orElse(FALSE) ? scoreValue.set(MAX) : scoreValue.set(MIN);
  }
}
