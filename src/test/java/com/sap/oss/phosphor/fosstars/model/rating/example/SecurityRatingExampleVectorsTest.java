package com.sap.oss.phosphor.fosstars.model.rating.example;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.sap.oss.phosphor.fosstars.model.RatingRepository;
import com.sap.oss.phosphor.fosstars.model.qa.VerificationFailedException;
import org.junit.jupiter.api.Test;

public class SecurityRatingExampleVectorsTest {

  @Test
  public void verify() throws VerificationFailedException {
    SecurityRatingExample rating = RatingRepository.INSTANCE.rating(SecurityRatingExample.class);
    assertNotNull(rating);
    new SecurityRatingExampleVerification(rating).run();
  }
}
