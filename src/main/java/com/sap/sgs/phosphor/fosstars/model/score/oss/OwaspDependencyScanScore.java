package com.sap.sgs.phosphor.fosstars.model.score.oss;

import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.OWASP_DEPENDENCY_CHECK_FAIL_CVSS_THRESHOLD;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.OWASP_DEPENDENCY_CHECK_SCAN_AVAILABILITY;
import static com.sap.sgs.phosphor.fosstars.model.value.Status.MANDATORY;
import static com.sap.sgs.phosphor.fosstars.model.value.Status.NOTFOUND;
import static com.sap.sgs.phosphor.fosstars.model.value.Status.OPTIONAL;

import com.sap.sgs.phosphor.fosstars.model.Score;
import com.sap.sgs.phosphor.fosstars.model.Value;
import com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures;
import com.sap.sgs.phosphor.fosstars.model.qa.ScoreVerification;
import com.sap.sgs.phosphor.fosstars.model.qa.TestVectors;
import com.sap.sgs.phosphor.fosstars.model.score.FeatureBasedScore;
import com.sap.sgs.phosphor.fosstars.model.value.ScoreValue;
import com.sap.sgs.phosphor.fosstars.model.value.Status;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.lang3.Range;

/**
 * The scores assesses how well an open-source project uses OWASP dependency check to scan
 * dependencies for known vulnerabilities. It is based on the following features:
 * <ul>
 *   <li>{@link OssFeatures#OWASP_DEPENDENCY_CHECK_SCAN_AVAILABILITY}</li>
 *   <li>{@link OssFeatures#OWASP_DEPENDENCY_CHECK_FAIL_CVSS_THRESHOLD}</li>
 * </ul>
 */
public class OwaspDependencyScanScore extends FeatureBasedScore {
  
  /**
   * The maximum CVSS score.
   * 
   * @see <a href="https://nvd.nist.gov/vuln-metrics/cvss">CVSS</a> 
   */
  private static final double MAX_CVSS_SCORE = 10.0;
  
  /**
   * The minimum CVSS score.
   * 
   * @see <a href="https://nvd.nist.gov/vuln-metrics/cvss">CVSS</a> 
   */
  private static final double MIN_CVSS_SCORE = 0.0;
  
  /**
   * Initializes a new {@link OwaspDependencyScanScore}.
   */
  OwaspDependencyScanScore() {
    super("How a project uses Owasp Dependency to scan dependencies for vulnerabilities",
        OWASP_DEPENDENCY_CHECK_SCAN_AVAILABILITY,
        OWASP_DEPENDENCY_CHECK_FAIL_CVSS_THRESHOLD);
  }

  @Override
  public ScoreValue calculate(Value... values) {
    Value<Status> availabilityValue =
        find(OWASP_DEPENDENCY_CHECK_SCAN_AVAILABILITY, values);
    Value<Double> cvssScore =
        find(OWASP_DEPENDENCY_CHECK_FAIL_CVSS_THRESHOLD, values);

    ScoreValue scoreValue = scoreValue(Score.MIN, availabilityValue, cvssScore);

    // If the project does not use OWASP Dependency scans.
    Status availability = availabilityValue.orElse(NOTFOUND);
    if (availability.equals(NOTFOUND)) {
      return scoreValue;
    }

    // If the project uses OWASP Dependency scans to identify vulnerable dependencies, then we are
    // happy.
    if (availability.equals(MANDATORY)) {
      scoreValue.set(7);
    } else if (availability.equals(OPTIONAL)) {
      scoreValue.set(3);
    }

    // If the project fails build based on certain CVSS threshold. If CVSS of a vulnerability is
    // greater than threshold, then the build fails. Lower the threshold score means most
    // vulnerabilities are considered, then we are happy.
    double failThreshold = cvssScore.orElse(11.0);
    if (failThreshold == MIN_CVSS_SCORE) {
      scoreValue.increase(3);
    } else if (inBetween(failThreshold)) {
      scoreValue.increase(MAX_CVSS_SCORE / (3 * failThreshold));
    }

    return scoreValue;
  }

  /**
   * Checks if the input number is in between the given range. The minimum score is greater than 0.
   * 
   * @param number The input to be checked.
   * @return True if the number lies within the range, false otherwise.
   */
  public static boolean inBetween(double number) {
    return Range.between(MIN_CVSS_SCORE + 1, MAX_CVSS_SCORE).contains(number);
  }

  /**
   * This class implements a verification procedure for {@link OwaspDependencyScanScore}.
   * The class loads test vectors, and provides methods to verify a {@link OwaspDependencyScanScore}
   * against those test vectors.
   */
  public static class Verification extends ScoreVerification {

    /**
     * A name of a resource which contains the test vectors.
     */
    private static final String TEST_VECTORS_YAML = "OwaspDependencyScanScoreTestVectors.yml";

    /**
     * Initializes a {@link Verification} for a {@link OwaspDependencyScanScore}.
     *
     * @param score A score to be verified.
     * @param vectors A list of test vectors.
     */
    public Verification(OwaspDependencyScanScore score, TestVectors vectors) {
      super(score, vectors);
    }

    /**
     * Creates an instance of {@link Verification} for a specified score. The method loads test
     * vectors from a default resource.
     *
     * @param score The score to be verified.
     * @return An instance of {@link Verification}.
     */
    static Verification createFor(OwaspDependencyScanScore score) throws IOException {
      try (InputStream is = Verification.class.getResourceAsStream(TEST_VECTORS_YAML)) {
        return new Verification(score, TestVectors.loadFromYaml(is));
      }
    }
  }
}
