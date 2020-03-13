package com.sap.sgs.phosphor.fosstars.model.score.oss;

import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.SCANS_FOR_VULNERABLE_DEPENDENCIES;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.SECURITY_REVIEWS_DONE;
import static com.sap.sgs.phosphor.fosstars.model.other.Utils.findValue;

import com.sap.sgs.phosphor.fosstars.model.Score;
import com.sap.sgs.phosphor.fosstars.model.Value;
import com.sap.sgs.phosphor.fosstars.model.qa.ScoreVerification;
import com.sap.sgs.phosphor.fosstars.model.qa.TestVector;
import com.sap.sgs.phosphor.fosstars.model.score.FeatureBasedScore;
import com.sap.sgs.phosphor.fosstars.model.value.ScoreValue;
import com.sap.sgs.phosphor.fosstars.model.value.SecurityReviews;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * The security testing score uses the following features:
 * <ul>
 *  <li>info about security reviews</li>
 *  <li>if an open-source project is scanned for vulnerable dependencies</li>
 * </ul>
 * Security reviews contribute to the score the most.
 * There is plenty room for improvements.
 * The score can take into account a lot of other information.
 */
public class ProjectSecurityTestingScore extends FeatureBasedScore {

  /**
   * A number of points which are added to a score value if a project has security reviews.
   */
  private static final double SECURITY_REVIEW_POINTS = 7.0;

  /**
   * A number of points which are added to a score value
   * if a project scans for vulnerable dependencies.
   */
  private static final double DEPENDENCY_SCAN_POINTS = 3.0;

  /**
   * Initializes a new score.
   */
  ProjectSecurityTestingScore() {
    super("How well security testing is done for an open-source project",
        SECURITY_REVIEWS_DONE,
        SCANS_FOR_VULNERABLE_DEPENDENCIES);
  }

  @Override
  public ScoreValue calculate(Value... values) {
    Value<SecurityReviews> securityReviewsDone = findValue(values, SECURITY_REVIEWS_DONE,
        "Hey! Tell me if any security review have been done for the project!");
    Value<Boolean> dependenciesScanned = findValue(values, SCANS_FOR_VULNERABLE_DEPENDENCIES,
        "Hey! Tell me if the project is scanned for vulnerable dependencies");

    ScoreValue scoreValue =  scoreValue(Score.MIN, securityReviewsDone, dependenciesScanned);

    securityReviewsDone.processIfKnown(reviews -> {
      if (reviews.done()) {
        scoreValue.increase(SECURITY_REVIEW_POINTS);
        scoreValue.explain(String.format(
            "The score increased on %2.2f because the project has security reviews",
            SECURITY_REVIEW_POINTS));
      } else {
        scoreValue.explain(String.format(
            "If the project had security reviews, the score would be higher (+%2.2f)",
            SECURITY_REVIEW_POINTS));
      }
    }).processIfUnknown(() -> scoreValue.explain(
        "If we knew that the project has security reviews, then the score might be higher"));

    dependenciesScanned.processIfKnown(scansDependencies -> {
      if (scansDependencies) {
        scoreValue.increase(DEPENDENCY_SCAN_POINTS);
        scoreValue.explain(String.format(
            "The score increased on %2.2f because the project scans dependencies",
            DEPENDENCY_SCAN_POINTS));
      } else {
        scoreValue.explain(String.format(
            "If the project scanned dependencies, the score would be higher (+%2.2f)",
            DEPENDENCY_SCAN_POINTS));
      }
    }).processIfUnknown(() -> scoreValue.explain(
        "If we knew that the project scans dependencies, then the score might be higher"));

    return scoreValue;
  }

  /**
   * This class implements a verification procedure for {@link ProjectSecurityTestingScore}.
   * The class loads test vectors,
   * and provides methods to verify a {@link ProjectSecurityTestingScore}
   * against those test vectors.
   */
  public static class Verification extends ScoreVerification {

    /**
     * A name of a resource which contains the test vectors.
     */
    private static final String TEST_VECTORS_CSV = "ProjectSecurityTestingScoreTestVectors.csv";

    /**
     * Initializes a {@link Verification}
     * for a {@link ProjectSecurityTestingScore}.
     *
     * @param score A score to be verified.
     * @param vectors A list of test vectors.
     */
    public Verification(ProjectSecurityTestingScore score, List<TestVector> vectors) {
      super(score, vectors);
    }

    /**
     * Creates an instance of {@link Verification} for a specified score. The method loads test
     * vectors from a default resource.
     *
     * @param score The score to be verified.
     * @return An instance of {@link ProjectSecurityTestingScore}.
     */
    static Verification createFor(ProjectSecurityTestingScore score) throws IOException {
      try (InputStream is = ProjectSecurityTestingScore.Verification.class
          .getResourceAsStream(TEST_VECTORS_CSV)) {

        return new Verification(score, loadTestVectorsFromCsvResource(score.features(), is));
      }
    }
  }
}
