package com.sap.sgs.phosphor.fosstars.model.score.oss;

import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.FUZZED_IN_OSS_FUZZ;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.LANGUAGES;
import static com.sap.sgs.phosphor.fosstars.model.value.Language.C;
import static com.sap.sgs.phosphor.fosstars.model.value.Language.CPP;
import static java.lang.Boolean.FALSE;

import com.sap.sgs.phosphor.fosstars.model.Value;
import com.sap.sgs.phosphor.fosstars.model.qa.ScoreVerification;
import com.sap.sgs.phosphor.fosstars.model.qa.TestVectors;
import com.sap.sgs.phosphor.fosstars.model.score.FeatureBasedScore;
import com.sap.sgs.phosphor.fosstars.model.value.Languages;
import com.sap.sgs.phosphor.fosstars.model.value.ScoreValue;
import java.io.IOException;
import java.io.InputStream;

/**
 * <p>The score shows how an open-source project uses fuzzing to find security issues.</p>
 *
 * <p>The score is based on the following features.</p>
 * <ul>
 *   <li>{@link com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures#LANGUAGES}</li>
 *   <li>{@link com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures#FUZZED_IN_OSS_FUZZ}</li>
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
  public ScoreValue calculate(Value... values) {
    Value<Languages> languages = find(LANGUAGES, values);
    Value<Boolean> fuzzedInOssFuzz = find(FUZZED_IN_OSS_FUZZ, values);

    ScoreValue scoreValue = scoreValue(MIN, languages, fuzzedInOssFuzz);

    boolean applicable = languages.isUnknown() || languages.get().containsAnyOf(UNSAFE_LANGUAGES);
    if (!applicable) {
      return scoreValue.makeNotApplicable();
    }

    return fuzzedInOssFuzz.orElse(FALSE) ? scoreValue.set(MAX) : scoreValue.set(MIN);
  }

  /**
   * This class implements a verification procedure for {@link FuzzingScore}.
   * The class loads test vectors,
   * and provides methods to verify a {@link FuzzingScore}
   * against those test vectors.
   */
  public static class Verification extends ScoreVerification {

    /**
     * A name of a resource which contains the test vectors.
     */
    private static final String TEST_VECTORS_YAML = "FuzzingScoreTestVectors.yml";

    /**
     * Initializes a {@link Verification}
     * for a {@link FuzzingScore}.
     *
     * @param score A score to be verified.
     * @param vectors A list of test vectors.
     */
    public Verification(FuzzingScore score, TestVectors vectors) {
      super(score, vectors);
    }

    /**
     * Creates an instance of {@link Verification} for a specified score. The method loads test
     * vectors from a default resource.
     *
     * @param score The score to be verified.
     * @return An instance of {@link FuzzingScore}.
     */
    static Verification createFor(FuzzingScore score) throws IOException {
      try (InputStream is = Verification.class.getResourceAsStream(TEST_VECTORS_YAML)) {
        return new Verification(score, TestVectors.loadFromYaml(is));
      }
    }
  }
}
