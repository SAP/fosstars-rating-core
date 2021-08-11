package com.sap.oss.phosphor.fosstars.model.rating.oss;

import com.sap.oss.phosphor.fosstars.model.RatingRepository;
import com.sap.oss.phosphor.fosstars.model.qa.RatingVerification;
import com.sap.oss.phosphor.fosstars.model.qa.TestVectors;
import com.sap.oss.phosphor.fosstars.model.qa.VerificationFailedException;
import org.junit.Test;

public class SecurityRiskRatingIntroducedByOssVerificationTest {

  @Test
  public void testVerification() throws VerificationFailedException {
    TestVectors vectors = new TestVectors();

    SecurityRiskRatingIntroducedByOss rating
        = RatingRepository.INSTANCE.rating(SecurityRiskRatingIntroducedByOss.class);

    new RatingVerification(rating, vectors).run();
  }
}
