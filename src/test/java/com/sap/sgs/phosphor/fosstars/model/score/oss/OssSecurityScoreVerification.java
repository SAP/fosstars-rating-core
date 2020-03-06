package com.sap.sgs.phosphor.fosstars.model.score.oss;

import static org.junit.Assert.assertFalse;

import com.sap.sgs.phosphor.fosstars.model.RatingRepository;
import com.sap.sgs.phosphor.fosstars.model.qa.VerificationFailedException;
import com.sap.sgs.phosphor.fosstars.model.rating.oss.OssSecurityRating;
import java.io.IOException;
import org.junit.Test;

public class OssSecurityScoreVerification {

  @Test
  public void verify() throws IOException, VerificationFailedException {
    OssSecurityRating rating = RatingRepository.INSTANCE.rating(OssSecurityRating.class);
    OssSecurityScore.Verification.createFor(rating.score()).run();
  }

  @Test
  public void manyVerifications() throws IOException {
    boolean failed = false;
    for (int i = 0; i < 100; i++) {
      try {
        OssSecurityScore.Verification.createFor(new OssSecurityScore()).run();
      } catch (VerificationFailedException e) {
        failed = true;
      }
    }
    assertFalse(failed);
  }

}
