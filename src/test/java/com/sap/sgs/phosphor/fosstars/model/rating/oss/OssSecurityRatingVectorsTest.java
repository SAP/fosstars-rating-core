package com.sap.sgs.phosphor.fosstars.model.rating.oss;

import static org.junit.Assert.assertNotNull;

import com.sap.sgs.phosphor.fosstars.model.RatingRepository;
import com.sap.sgs.phosphor.fosstars.model.qa.VerificationFailedException;
import java.io.IOException;
import org.junit.Test;

public class OssSecurityRatingVectorsTest {

  @Test
  public void verify() throws VerificationFailedException, IOException {
    OssSecurityRating rating = RatingRepository.INSTANCE.rating(OssSecurityRating.class);
    assertNotNull(rating);
    OssSecurityRatingVerification.createFor(rating).run();
  }

}