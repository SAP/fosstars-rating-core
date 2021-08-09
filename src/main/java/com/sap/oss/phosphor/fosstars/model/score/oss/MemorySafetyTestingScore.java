package com.sap.oss.phosphor.fosstars.model.score.oss;

import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.LANGUAGES;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_ADDRESS_SANITIZER;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_MEMORY_SANITIZER;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_UNDEFINED_BEHAVIOR_SANITIZER;
import static com.sap.oss.phosphor.fosstars.model.value.Language.C;
import static com.sap.oss.phosphor.fosstars.model.value.Language.CPP;

import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.score.FeatureBasedScore;
import com.sap.oss.phosphor.fosstars.model.value.Languages;
import com.sap.oss.phosphor.fosstars.model.value.ScoreValue;

/**
 * <p>The score shows how an open-source project tests for memory-corruption issues such as
 * buffer overflows, use-after-free and so on.</p>
 *
 * <p>The score is based on the following features.</p>
 * <ul>
 *   <li>{@link com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures#LANGUAGES}
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
 * </ul>
 */
public class MemorySafetyTestingScore extends FeatureBasedScore {

  /**
   * Programming languages that allow direct access to memory.
   */
  private static final Languages UNSAFE_LANGUAGES = Languages.of(C, CPP);

  /**
   * How the score increases for a project that uses AddressSanitizer.
   */
  private static final double POINTS_FOR_ADDRESS_SANITIZER = 7.0;

  /**
   * How the score increases for a project that uses MemorySanitizer.
   */
  private static final double POINTS_FOR_MEMORY_SANITIZER = 1.5;

  /**
   * How the score increases for a project that uses UndefinedBehaviorSanitizer.
   */
  private static final double POINTS_FOR_UNDEFINED_BEHAVIOR_SANITIZER = 1.5;

  /**
   * Initializes a new score.
   */
  MemorySafetyTestingScore() {
    super("How a project tests for memory-safety issues",
        LANGUAGES,
        USES_ADDRESS_SANITIZER,
        USES_MEMORY_SANITIZER,
        USES_UNDEFINED_BEHAVIOR_SANITIZER);
  }

  @Override
  public ScoreValue calculate(Value<?>... values) {
    Value<Languages> languages = find(LANGUAGES, values);
    Value<Boolean> usesAddressSanitizer = find(USES_ADDRESS_SANITIZER, values);
    Value<Boolean> usesMemorySanitizer = find(USES_MEMORY_SANITIZER, values);
    Value<Boolean> usesUndefinedBehaviorSanitizer = find(USES_UNDEFINED_BEHAVIOR_SANITIZER, values);

    ScoreValue scoreValue = scoreValue(MIN,
        languages, usesAddressSanitizer, usesMemorySanitizer, usesUndefinedBehaviorSanitizer);

    if (allUnknown(scoreValue.usedValues())) {
      return scoreValue.makeUnknown();
    }

    boolean applicable = languages.isUnknown() || languages.get().containsAnyOf(UNSAFE_LANGUAGES);
    if (!applicable) {
      return scoreValue.makeNotApplicable();
    }

    if (usesAddressSanitizer.orElse(Boolean.FALSE)) {
      scoreValue.increase(POINTS_FOR_ADDRESS_SANITIZER);
    }

    if (usesMemorySanitizer.orElse(Boolean.FALSE)) {
      scoreValue.increase(POINTS_FOR_MEMORY_SANITIZER);
    }

    if (usesUndefinedBehaviorSanitizer.orElse(Boolean.FALSE)) {
      scoreValue.increase(POINTS_FOR_UNDEFINED_BEHAVIOR_SANITIZER);
    }

    return scoreValue;
  }
}
