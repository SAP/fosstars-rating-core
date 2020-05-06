package com.sap.sgs.phosphor.fosstars.model.score.oss;

import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.LANGUAGES;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.USES_ADDRESS_SANITIZER;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.USES_MEMORY_SANITIZER;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.USES_UNDEFINED_BEHAVIOR_SANITIZER;
import static com.sap.sgs.phosphor.fosstars.model.value.Language.C;
import static com.sap.sgs.phosphor.fosstars.model.value.Language.CPP;

import com.sap.sgs.phosphor.fosstars.model.Feature;
import com.sap.sgs.phosphor.fosstars.model.Value;
import com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures;
import com.sap.sgs.phosphor.fosstars.model.qa.ScoreVerification;
import com.sap.sgs.phosphor.fosstars.model.qa.TestVectors;
import com.sap.sgs.phosphor.fosstars.model.score.FeatureBasedScore;
import com.sap.sgs.phosphor.fosstars.model.value.Languages;
import com.sap.sgs.phosphor.fosstars.model.value.ScoreValue;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>The score shows how an open-source project tests for memory-corruption issues such as
 * buffer overflows, use-after-free and so on.</p>
 *
 * <p>The score is based on the following features:
 * <ul>
 *   <li>{@link OssFeatures#LANGUAGES}</li>
 *   <li>{@link OssFeatures#USES_ADDRESS_SANITIZER}</li>
 *   <li>{@link OssFeatures#USES_MEMORY_SANITIZER}</li>
 *   <li>{@link OssFeatures#USES_UNDEFINED_BEHAVIOR_SANITIZER}</li>
 * </ul>
 * </p>
 */
public class MemorySafetyTestingScore extends FeatureBasedScore {

  /**
   * Programming languages that allow direct access to memory.
   */
  private static final Languages UNSAFE_LANGUAGES = Languages.of(C, CPP);

  /**
   * Defines points for using a sanitizer.
   */
  private static final Map<Feature, Double> POINTS = new HashMap<>();

  static {
    POINTS.put(USES_ADDRESS_SANITIZER, 7.0);
    POINTS.put(USES_MEMORY_SANITIZER, 1.5);
    POINTS.put(USES_UNDEFINED_BEHAVIOR_SANITIZER, 1.5);
  }

  /**
   * Initializes a new score.
   */
  MemorySafetyTestingScore() {
    super("How a project tests for memory-corruption issues",
        LANGUAGES,
        USES_ADDRESS_SANITIZER,
        USES_MEMORY_SANITIZER,
        USES_UNDEFINED_BEHAVIOR_SANITIZER);
  }

  @Override
  public ScoreValue calculate(Value... values) {
    Value<Languages> languages = find(LANGUAGES, values);

    ScoreValue scoreValue = scoreValue(MIN, languages);

    boolean applicable = languages.isUnknown() || languages.get().containsAnyOf(UNSAFE_LANGUAGES);
    if (!applicable) {
      return scoreValue.makeNotApplicable();
    }

    List<Value<Boolean>> interestingValues = Arrays.asList(
        find(USES_ADDRESS_SANITIZER, values),
        find(USES_MEMORY_SANITIZER, values),
        find(USES_UNDEFINED_BEHAVIOR_SANITIZER, values)
    );

    for (Value<Boolean> value : interestingValues) {
      if (value.orElse(Boolean.FALSE)) {
        scoreValue.increase(POINTS.get(value.feature()));
      }
      scoreValue.usedValues(value);
    }

    return scoreValue;
  }

  /**
   * This class implements a verification procedure for {@link MemorySafetyTestingScore}.
   * The class loads test vectors,
   * and provides methods to verify a {@link MemorySafetyTestingScore}
   * against those test vectors.
   */
  public static class Verification extends ScoreVerification {

    /**
     * A name of a resource which contains the test vectors.
     */
    private static final String TEST_VECTORS_YAML = "MemorySafetyScoreTestVectors.yml";

    /**
     * Initializes a {@link Verification}
     * for a {@link MemorySafetyTestingScore}.
     *
     * @param score A score to be verified.
     * @param vectors A list of test vectors.
     */
    public Verification(MemorySafetyTestingScore score, TestVectors vectors) {
      super(score, vectors);
    }

    /**
     * Creates an instance of {@link Verification} for a specified score. The method loads test
     * vectors from a default resource.
     *
     * @param score The score to be verified.
     * @return An instance of {@link MemorySafetyTestingScore}.
     */
    static Verification createFor(MemorySafetyTestingScore score) throws IOException {
      try (InputStream is = Verification.class.getResourceAsStream(TEST_VECTORS_YAML)) {
        return new Verification(score, TestVectors.loadFromYaml(is));
      }
    }
  }
}
