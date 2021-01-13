package com.sap.oss.phosphor.fosstars.model.rating.oss;

import static org.junit.Assert.assertNotNull;

import com.sap.oss.phosphor.fosstars.model.RatingRepository;
import com.sap.oss.phosphor.fosstars.model.qa.RatingVerification;
import com.sap.oss.phosphor.fosstars.model.qa.TestVectors;
import com.sap.oss.phosphor.fosstars.model.qa.VerificationFailedException;
import java.io.IOException;
import java.io.InputStream;
import org.junit.Test;

public class OssSecurityRatingVerificationTest {

  @Test
  public void testVerification() throws VerificationFailedException, IOException {
    OssSecurityRating rating = RatingRepository.INSTANCE.rating(OssSecurityRating.class);
    assertNotNull(rating);
    OssSecurityRatingVerification.createFor(rating).run();
  }

  /**
   * This class implements a verification procedure for {@link OssSecurityRating}.
   * The class loads test vectors, and provides methods to verify a {@link OssSecurityRating}
   * against those test vectors.
   */
  private static class OssSecurityRatingVerification extends RatingVerification {

    private static final String TEST_VECTORS_YAML = "OssSecurityRatingTestVectors.yml";

    /**
     * Initializes a {@link OssSecurityRatingVerification} for an {@link OssSecurityRating}.
     *
     * @param rating A rating to be verified.
     * @param vectors A list of test vectors.
     */
    OssSecurityRatingVerification(OssSecurityRating rating, TestVectors vectors) {
      super(rating, vectors);
    }

    /**
     * Creates an instance of {@link OssSecurityRatingVerification} for a specified rating.
     * The method loads test vectors from a default resource.
     *
     * @param rating The rating to be verified.
     * @return An instance of {@link OssSecurityRatingVerification}.
     */
    static OssSecurityRatingVerification createFor(OssSecurityRating rating) throws IOException {
      try (InputStream is = OssSecurityRatingVerification.class
          .getResourceAsStream(TEST_VECTORS_YAML)) {

        return new OssSecurityRatingVerification(rating, TestVectors.loadFromYaml(is));
      }
    }
  }

}