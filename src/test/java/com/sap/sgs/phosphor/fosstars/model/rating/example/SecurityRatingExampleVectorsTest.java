package com.sap.sgs.phosphor.fosstars.model.rating.example;

import static org.junit.Assert.assertNotNull;

import com.sap.sgs.phosphor.fosstars.model.RatingRepository;
import com.sap.sgs.phosphor.fosstars.model.qa.VerificationFailedException;
import org.junit.Test;

public class SecurityRatingExampleVectorsTest {

  @Test
  public void verify() throws VerificationFailedException {
    SecurityRatingExample rating = RatingRepository.INSTANCE.get(SecurityRatingExample.class);
    assertNotNull(rating);
    new SecurityRatingExampleVerification(rating).run();
  }

}