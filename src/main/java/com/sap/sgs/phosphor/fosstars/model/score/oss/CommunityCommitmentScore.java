package com.sap.sgs.phosphor.fosstars.model.score.oss;

import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.IS_APACHE;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.IS_ECLIPSE;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.SUPPORTED_BY_COMPANY;
import static com.sap.sgs.phosphor.fosstars.model.other.Utils.findValue;

import com.sap.sgs.phosphor.fosstars.model.Score;
import com.sap.sgs.phosphor.fosstars.model.Value;
import com.sap.sgs.phosphor.fosstars.model.qa.ScoreVerification;
import com.sap.sgs.phosphor.fosstars.model.qa.TestVectors;
import com.sap.sgs.phosphor.fosstars.model.score.FeatureBasedScore;
import com.sap.sgs.phosphor.fosstars.model.value.ScoreValue;
import java.io.IOException;
import java.io.InputStream;

/**
 * <p>The community commitment score depends on the following features.</p>
 * <ul>
 *   <li>If a project is supported by a company</li>
 *   <li>If a project is part of the Apache Software Foundation</li>
 *   <li>If a project is part of the Eclipse Foundation</li>
 * </ul>
 * <p>A project gets the maximum score if it's supported by a company
 * and is part of one of the foundations.
 * A project gets the minimum score if it's not supported by a company
 * and doesn't belong to any foundation.</p>
 */
public class CommunityCommitmentScore extends FeatureBasedScore {

  /**
   * Initializes a {@link CommunityCommitmentScore}.
   */
  CommunityCommitmentScore() {
    super("How well open-source community commits to support an open-source project",
        SUPPORTED_BY_COMPANY, IS_APACHE, IS_ECLIPSE);
  }

  @Override
  public ScoreValue calculate(Value... values) {
    Value<Boolean> hasResponsibleCompany = findValue(values, SUPPORTED_BY_COMPANY,
        "Hey! You have to tell me if the project is supported by a company or not!");
    Value<Boolean> isApacheProject = findValue(values, IS_APACHE,
        "Hey! Tell me if the project belongs to the Apache Software Foundation!");
    Value<Boolean> isEclipseProject = findValue(values, IS_ECLIPSE,
        "Hey! Tell me if the project belongs to the Eclipse Foundation!");

    boolean belongsToApache = !isApacheProject.isUnknown() && isApacheProject.get();
    boolean belongsToEclipse = !isEclipseProject.isUnknown() && isEclipseProject.get();

    if (belongsToApache && belongsToEclipse) {
      throw new IllegalArgumentException(
          "How can the project belong to both Apache and Eclipse foundations?");
    }

    double scorePoints = Score.MIN;
    if (belongsToApache || belongsToEclipse) {
      scorePoints += 7.0;
    }

    if (!hasResponsibleCompany.isUnknown() && hasResponsibleCompany.get()) {
      scorePoints += 8.0;
    }

    return scoreValue(scorePoints, hasResponsibleCompany, isApacheProject, isEclipseProject);
  }

  /**
   * This class implements a verification procedure for {@link CommunityCommitmentScore}.
   * The class loads test vectors, and provides methods to verify a {@link CommunityCommitmentScore}
   * against those test vectors.
   */
  public static class Verification extends ScoreVerification {

    /**
     * A name of a resource which contains the test vectors.
     */
    private static final String TEST_VECTORS_YAML = "CommunityCommitmentScoreTestVectors.yml";

    /**
     * Initializes a {@link Verification} for a {@link CommunityCommitmentScore}.
     *
     * @param score A score to be verified.
     * @param vectors A list of test vectors.
     */
    public Verification(CommunityCommitmentScore score, TestVectors vectors) {
      super(score, vectors);
    }

    /**
     * Creates an instance of {@link Verification} for a specified score. The method loads test
     * vectors from a default resource.
     *
     * @param score The score to be verified.
     * @return An instance of {@link CommunityCommitmentScore}.
     */
    static Verification createFor(CommunityCommitmentScore score) throws IOException {
      try (InputStream is = Verification.class.getResourceAsStream(TEST_VECTORS_YAML)) {
        return new Verification(score, TestVectors.loadFromYaml(is));
      }
    }
  }
}
