package com.sap.sgs.phosphor.fosstars.model.score.oss;

import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.HAS_SECURITY_POLICY;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.HAS_SECURITY_TEAM;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.USES_VERIFIED_SIGNED_COMMITS;
import static com.sap.sgs.phosphor.fosstars.model.other.Utils.findValue;

import com.sap.sgs.phosphor.fosstars.model.Confidence;
import com.sap.sgs.phosphor.fosstars.model.Value;
import com.sap.sgs.phosphor.fosstars.model.qa.ScoreVerification;
import com.sap.sgs.phosphor.fosstars.model.qa.TestVector;
import com.sap.sgs.phosphor.fosstars.model.score.FeatureBasedScore;
import com.sap.sgs.phosphor.fosstars.model.value.ScoreValue;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * The security awareness score is currently based on the following features:
 * <ul>
 *   <li>if a project publishes security advisories</li>
 *   <li>if a project has a security team</li>
 *   <li>if a project uses verified signed commits</li>
 * </ul>
 * A project gets the maximum score if it has both a security policy and a security team.
 */
public class ProjectSecurityAwarenessScore extends FeatureBasedScore {

  /**
   * A number of points which are added to a score value if a project has a security policy.
   */
  private static final double SECURITY_POLICY_POINTS = 3.0;

  /**
   * A number of points which are added to a score value if a project has a security team.
   */
  private static final double SECURITY_TEAM_POINTS = 5.0;

  /**
   * A number of points which are added to a score value if a project uses verified signed commits.
   */
  private static final double SIGNED_COMMITS_POINTS = 2.0;

  private static final String DESCRIPTION = String.format(
      "The score checks if a project has a security policy and a security team.\n"
          + "If the project has a security policy, then the score adds %2.2f.\n"
          + "If the project has a security team, then the score adds %2.2f.\n"
          + "If the project uses verified signed commits, then the score adds %2.2f.",
      SECURITY_POLICY_POINTS, SECURITY_TEAM_POINTS, SIGNED_COMMITS_POINTS);

  /**
   * Initializes a new {@link ProjectSecurityAwarenessScore}.
   */
  ProjectSecurityAwarenessScore() {
    super("How well open-source community is aware about security", DESCRIPTION,
        HAS_SECURITY_POLICY, HAS_SECURITY_TEAM, USES_VERIFIED_SIGNED_COMMITS);
  }

  @Override
  public ScoreValue calculate(Value... values) {
    Value<Boolean> securityPolicy = findValue(values, HAS_SECURITY_POLICY,
        "Hey! You have to tell me if the project has a security policy!");
    Value<Boolean> securityTeam = findValue(values, HAS_SECURITY_TEAM,
        "Hey! You have to tell me if the project has a security team!");
    Value<Boolean> signedCommits = findValue(values, USES_VERIFIED_SIGNED_COMMITS,
        "Hey! You have to tell me if the project uses verified signed commits!");

    ScoreValue scoreValue = new ScoreValue(this);
    securityPolicy.processIfKnown(exist -> {
      if (exist) {
        scoreValue.increase(SECURITY_POLICY_POINTS);
      }
    });

    securityTeam.processIfKnown(exist -> {
      if (exist) {
        scoreValue.increase(SECURITY_TEAM_POINTS);
      }
    });

    signedCommits.processIfKnown(exist -> {
      if (exist) {
        scoreValue.increase(SIGNED_COMMITS_POINTS);
      }
    });

    scoreValue.usedValues(securityPolicy, securityTeam, signedCommits);
    scoreValue.confidence(Confidence.make(securityPolicy, securityTeam, signedCommits));

    return scoreValue;
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
    private static final String TEST_VECTORS_YAML = "ProjectSecurityAwarenessScoreTestVectors.yml";

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
      try (InputStream is = Verification.class.getResourceAsStream(TEST_VECTORS_YAML)) {
        return new Verification(score, loadTestVectorsFromYaml(is));
      }
    }
  }
}
