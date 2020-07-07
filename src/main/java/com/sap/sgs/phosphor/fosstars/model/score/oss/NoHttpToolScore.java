package com.sap.sgs.phosphor.fosstars.model.score.oss;

import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.PACKAGE_MANAGERS;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.USES_NOHTTP;
import static com.sap.sgs.phosphor.fosstars.model.other.Utils.findValue;
import static com.sap.sgs.phosphor.fosstars.model.value.PackageManager.GRADLE;
import static com.sap.sgs.phosphor.fosstars.model.value.PackageManager.MAVEN;

import com.sap.sgs.phosphor.fosstars.model.Value;
import com.sap.sgs.phosphor.fosstars.model.qa.ScoreVerification;
import com.sap.sgs.phosphor.fosstars.model.qa.TestVectors;
import com.sap.sgs.phosphor.fosstars.model.score.FeatureBasedScore;
import com.sap.sgs.phosphor.fosstars.model.value.PackageManagers;
import com.sap.sgs.phosphor.fosstars.model.value.ScoreValue;
import java.io.IOException;
import java.io.InputStream;

/**
 * <p>The score shows if an open-source project uses nohttp tool
 * to catch usage of insecure HTTP protocol.</p>
 *
 * <p>The score is based on the following features.</p>
 * <ul>
 *   <li>{@link com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures#USES_NOHTTP}</li>
 *   <li>{@link com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures#PACKAGE_MANAGERS}</li>
 * </ul>
 *
 * @see <a href="https://github.com/spring-io/nohttp">nohttp</a>
 */
public class NoHttpToolScore extends FeatureBasedScore {

  /**
   * Initializes a new score.
   */
  NoHttpToolScore() {
    super("If a project uses nohttp tool", USES_NOHTTP, PACKAGE_MANAGERS);
  }

  @Override
  public ScoreValue calculate(Value... values) {
    Value<Boolean> usesNoHttp = findValue(values, USES_NOHTTP,
        "Hey! You have to tell me if the project uses nohttp tool!");
    Value<PackageManagers> packageManagers = findValue(values, PACKAGE_MANAGERS,
        "Hey! You have to tell me which package managers the project uses!");

    ScoreValue scoreValue = scoreValue(MIN, usesNoHttp, packageManagers);

    // if nohttp is used, then return the max score value, and the min value otherwise
    if (!usesNoHttp.isUnknown()) {
      return usesNoHttp.get() ? scoreValue.set(MAX) : scoreValue;
    }

    // now we don't know if nohttp is used or not
    // if we don't know anything about package managers, then return the min value
    if (packageManagers.isUnknown()) {
      return scoreValue;
    }

    // the can be integrated with Maven or Gradle
    // return N/A if other package managers are used
    if (!packageManagers.get().containsAny(MAVEN, GRADLE)) {
      return scoreValue.makeNotApplicable();
    }

    // otherwise, return the min value
    return scoreValue;
  }

  /**
   * This class implements a verification procedure for {@link NoHttpToolScore}.
   * The class loads test vectors,
   * and provides methods to verify a {@link NoHttpToolScore}
   * against those test vectors.
   */
  public static class Verification extends ScoreVerification {

    /**
     * A name of a resource which contains the test vectors.
     */
    private static final String TEST_VECTORS_YAML = "NoHttpToolScoreTestVectors.yml";

    /**
     * Initializes a {@link Verification}
     * for a {@link NoHttpToolScore}.
     *
     * @param score A score to be verified.
     * @param vectors A list of test vectors.
     */
    public Verification(NoHttpToolScore score, TestVectors vectors) {
      super(score, vectors);
    }

    /**
     * Creates an instance of {@link Verification} for a specified score. The method loads test
     * vectors from a default resource.
     *
     * @param score The score to be verified.
     * @return An instance of {@link NoHttpToolScore}.
     */
    static Verification createFor(NoHttpToolScore score) throws IOException {
      try (InputStream is = Verification.class.getResourceAsStream(TEST_VECTORS_YAML)) {
        return new Verification(score, TestVectors.loadFromYaml(is));
      }
    }
  }
}
