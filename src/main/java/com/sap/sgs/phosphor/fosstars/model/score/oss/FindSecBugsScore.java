package com.sap.sgs.phosphor.fosstars.model.score.oss;

import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.LANGUAGES;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.USES_FIND_SEC_BUGS;
import static com.sap.sgs.phosphor.fosstars.model.value.Language.JAVA;
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
 * <p>The score shows if an open-source project uses FindSecBugs to catch security issues.</p>
 *
 * <p>The score is based on the following features.</p>
 * <ul>
 *   <li>{@link com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures#LANGUAGES}</li>
 *   <li>{@link com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures#USES_FIND_SEC_BUGS}</li>
 * </ul>
 */
public class FindSecBugsScore extends FeatureBasedScore {

  /**
   * Initializes a new score.
   */
  FindSecBugsScore() {
    super("How a project uses FindSecBugs", LANGUAGES, USES_FIND_SEC_BUGS);
  }

  @Override
  public ScoreValue calculate(Value... values) {
    Value<Languages> languages = find(LANGUAGES, values);
    Value<Boolean> usesFindSecBugs = find(USES_FIND_SEC_BUGS, values);

    ScoreValue scoreValue = scoreValue(MIN, languages, usesFindSecBugs);

    if (allUnknown(languages, usesFindSecBugs)) {
      return scoreValue.makeUnknown();
    }

    boolean applicable = languages.isUnknown() || languages.get().containsAnyOf(JAVA);
    if (!applicable) {
      return scoreValue.makeNotApplicable();
    }

    return usesFindSecBugs.orElse(FALSE) ? scoreValue.set(MAX) : scoreValue.set(MIN);
  }

  /**
   * This class implements a verification procedure for {@link FindSecBugsScore}.
   * The class loads test vectors,
   * and provides methods to verify a {@link FindSecBugsScore}
   * against those test vectors.
   */
  public static class Verification extends ScoreVerification {

    /**
     * A name of a resource which contains the test vectors.
     */
    private static final String TEST_VECTORS_YAML = "FindSecBugsScoreTestVectors.yml";

    /**
     * Initializes a {@link Verification} for a {@link FindSecBugsScore}.
     *
     * @param score A score to be verified.
     * @param vectors A list of test vectors.
     */
    public Verification(FindSecBugsScore score, TestVectors vectors) {
      super(score, vectors);
    }

    /**
     * Creates an instance of {@link Verification} for a specified score. The method loads test
     * vectors from a default resource.
     *
     * @param score The score to be verified.
     * @return An instance of {@link FindSecBugsScore}.
     */
    static Verification createFor(FindSecBugsScore score) throws IOException {
      try (InputStream is = Verification.class.getResourceAsStream(TEST_VECTORS_YAML)) {
        return new Verification(score, TestVectors.loadFromYaml(is));
      }
    }
  }
}
