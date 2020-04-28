package com.sap.sgs.phosphor.fosstars.model.score.oss;

import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.SCANS_FOR_VULNERABLE_DEPENDENCIES;
import static com.sap.sgs.phosphor.fosstars.model.other.Utils.findValue;

import com.sap.sgs.phosphor.fosstars.model.Score;
import com.sap.sgs.phosphor.fosstars.model.Value;
import com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures;
import com.sap.sgs.phosphor.fosstars.model.qa.ScoreVerification;
import com.sap.sgs.phosphor.fosstars.model.qa.TestVectors;
import com.sap.sgs.phosphor.fosstars.model.score.FeatureBasedScore;
import com.sap.sgs.phosphor.fosstars.model.value.ScoreValue;
import java.io.IOException;
import java.io.InputStream;

/**
 * <p>The score just wraps the {@link OssFeatures#SCANS_FOR_VULNERABLE_DEPENDENCIES} feature.
 * If the feature is true, then it returns {@link Score#MAX}. otherwise {@link Score#MIN}.</p>
 * <p>The score needs to be extended with more features.</p>
 */
public class DependencyScanScore extends FeatureBasedScore {

  /**
   * Initializes a new {@link DependencyScanScore}.
   */
  DependencyScanScore() {
    super("How a project scans its dependencies for vulnerabilities",
        SCANS_FOR_VULNERABLE_DEPENDENCIES);
  }

  @Override
  public ScoreValue calculate(Value... values) {
    Value<Boolean> scansDependencies = findValue(values, SCANS_FOR_VULNERABLE_DEPENDENCIES,
        "Hey! You have to tell me if the project scans for vulnerable dependencies!");

    if (scansDependencies.isUnknown()) {
      return scoreValue(Score.MIN, scansDependencies);
    }

    return scoreValue(
        scansDependencies.get().equals(true) ? Score.MAX : Score.MIN,
        scansDependencies);
  }

  /**
   * This class implements a verification procedure for {@link DependencyScanScore}.
   * The class loads test vectors, and provides methods to verify a {@link DependencyScanScore}
   * against those test vectors.
   */
  public static class Verification extends ScoreVerification {

    /**
     * A name of a resource which contains the test vectors.
     */
    private static final String TEST_VECTORS_YAML = "DependencyScanScoreTestVectors.yml";

    /**
     * Initializes a {@link Verification} for a {@link DependencyScanScore}.
     *
     * @param score A score to be verified.
     * @param vectors A list of test vectors.
     */
    public Verification(DependencyScanScore score, TestVectors vectors) {
      super(score, vectors);
    }

    /**
     * Creates an instance of {@link Verification} for a specified score. The method loads test
     * vectors from a default resource.
     *
     * @param score The score to be verified.
     * @return An instance of {@link Verification}.
     */
    static Verification createFor(DependencyScanScore score) throws IOException {
      try (InputStream is = Verification.class.getResourceAsStream(TEST_VECTORS_YAML)) {
        return new Verification(score, TestVectors.loadFromYaml(is));
      }
    }
  }
}
