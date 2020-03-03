package com.sap.sgs.phosphor.fosstars.model.score.oss;

import static org.junit.Assert.assertTrue;

import com.sap.sgs.phosphor.fosstars.model.RatingRepository;
import com.sap.sgs.phosphor.fosstars.model.qa.VerificationFailedException;
import com.sap.sgs.phosphor.fosstars.model.rating.oss.OssSecurityRating;
import java.io.IOException;
import java.util.Optional;
import org.junit.Test;

public class ProjectPopularityScoreVerification {

  @Test
  public void verify() throws IOException, VerificationFailedException {
    OssSecurityRating rating = RatingRepository.INSTANCE.rating(OssSecurityRating.class);
    Optional<ProjectPopularityScore> something
        = rating.score().subScore(ProjectPopularityScore.class);
    assertTrue(something.isPresent());
    ProjectPopularityScore.Verification.createFor(something.get()).run();
  }

}