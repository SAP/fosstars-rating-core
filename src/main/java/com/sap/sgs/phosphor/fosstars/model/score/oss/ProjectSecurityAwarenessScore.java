package com.sap.sgs.phosphor.fosstars.model.score.oss;

import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.HAS_SECURITY_POLICY;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.HAS_SECURITY_TEAM;
import static com.sap.sgs.phosphor.fosstars.model.other.Utils.findValue;

import com.sap.sgs.phosphor.fosstars.model.Confidence;
import com.sap.sgs.phosphor.fosstars.model.Score;
import com.sap.sgs.phosphor.fosstars.model.ScoreValue;
import com.sap.sgs.phosphor.fosstars.model.Value;
import com.sap.sgs.phosphor.fosstars.model.qa.ScoreVerification;
import com.sap.sgs.phosphor.fosstars.model.qa.TestVector;
import com.sap.sgs.phosphor.fosstars.model.score.FeatureBasedScore;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * The security awareness score is currently based on the following features:
 * <ul>
 *   <li>if a project publishes security advisories</li>
 *   <li>if a project has a security team</li>
 * </ul>
 * A project gets the maximum score if it has both a security policy and a security team.
 */
public class ProjectSecurityAwarenessScore extends FeatureBasedScore {

  /**
   * Initializes a new {@link ProjectSecurityAwarenessScore}.
   */
  ProjectSecurityAwarenessScore() {
    super("How well open-source community is aware about security",
        HAS_SECURITY_POLICY, HAS_SECURITY_TEAM);
  }

  @Override
  public ScoreValue calculate(Value... values) {
    Value<Boolean> securityAdvisories = findValue(values, HAS_SECURITY_POLICY,
        "Hey! You have to tell me if the project publishes security advisories!");
    Value<Boolean> securityTeam = findValue(values, HAS_SECURITY_TEAM,
        "Hey! You have to tell me if the project has a security team!");

    double points = 0.0;

    if (!securityAdvisories.isUnknown() && securityAdvisories.get().equals(true)) {
      points += 5.0;
    }
    if (!securityTeam.isUnknown() && securityTeam.get().equals(true)) {
      points += 8.0;
    }

    return new ScoreValue(Score.adjust(points), Confidence.make(securityAdvisories, securityTeam));
  }

  /**
   * This class implements a verification procedure for {@link ProjectSecurityAwarenessScore}.
   * The class loads test vectors,
   * and provides methods to verify a {@link ProjectSecurityAwarenessScore}
   * against those test vectors.
   */
  public static class Verification extends ScoreVerification {

    /**
     * A name of a resource which contains the test vectors.
     */
    private static final String TEST_VECTORS_CSV = "ProjectSecurityAwarenessScoreTestVectors.csv";

    /**
     * Initializes a {@link Verification}
     * for a {@link ProjectSecurityAwarenessScore}.
     *
     * @param score A score to be verified.
     * @param vectors A list of test vectors.
     */
    public Verification(ProjectSecurityAwarenessScore score, List<TestVector> vectors) {
      super(score, vectors);
    }

    /**
     * Creates an instance of {@link Verification} for a specified score. The method loads test
     * vectors from a default resource.
     *
     * @param score The score to be verified.
     * @return An instance of {@link ProjectSecurityAwarenessScore}.
     */
    static Verification createFor(ProjectSecurityAwarenessScore score) throws IOException {
      try (InputStream is = ProjectSecurityAwarenessScore.Verification.class
          .getResourceAsStream(TEST_VECTORS_CSV)) {

        return new Verification(score, loadTestVectorsFromCsvResource(score.features(), is));
      }
    }
  }
}
