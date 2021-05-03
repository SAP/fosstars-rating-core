package com.sap.oss.phosphor.fosstars.model.rating.oss;

import static org.junit.Assert.assertNotNull;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sap.oss.phosphor.fosstars.model.RatingRepository;
import com.sap.oss.phosphor.fosstars.model.qa.RatingVerification;
import com.sap.oss.phosphor.fosstars.model.qa.TestVectors;
import com.sap.oss.phosphor.fosstars.model.qa.VerificationFailedException;
import com.sap.oss.phosphor.fosstars.util.Yaml;
import java.io.IOException;
import java.io.InputStream;
import org.junit.Test;

public class OssArtifactSecurityRatingVerificationTest {

  @Test
  public void testVerification() throws VerificationFailedException, IOException {
    OssArtifactSecurityRating rating =
        RatingRepository.INSTANCE.rating(OssArtifactSecurityRating.class);
    assertNotNull(rating);
    OssArtifactSecurityRatingVerification.createFor(rating).run();
  }

  /**
   * This class implements a verification procedure for {@link OssArtifactSecurityRating}.
   * The class loads test vectors, and provides methods to verify a
   * {@link OssArtifactSecurityRating} against those test vectors.
   */
  private static class OssArtifactSecurityRatingVerification extends RatingVerification {

    private static final String TEST_VECTORS_YAML = "OssArtifactSecurityRatingTestVectors.yml";

    /**
     * Initializes a {@link OssArtifactSecurityRatingVerification} for an
     * {@link OssArtifactSecurityRating}.
     *
     * @param rating A rating to be verified.
     * @param vectors A list of test vectors.
     */
    OssArtifactSecurityRatingVerification(OssArtifactSecurityRating rating, TestVectors vectors) {
      super(rating, vectors);
    }

    /**
     * Creates an instance of {@link OssArtifactSecurityRatingVerification} for a specified rating.
     * The method loads test vectors from a default resource.
     *
     * @param rating The rating to be verified.
     * @return An instance of {@link OssArtifactSecurityRatingVerification}.
     */
    static OssArtifactSecurityRatingVerification createFor(OssArtifactSecurityRating rating)
        throws IOException {
      try (InputStream is = OssArtifactSecurityRatingVerification.class
          .getResourceAsStream(TEST_VECTORS_YAML)) {

        ObjectMapper mapper = Yaml.mapper();
        mapper.registerSubtypes(TestArtifactVersion.class);
        TestVectors testVectors = mapper.readValue(is, TestVectors.class);

        return new OssArtifactSecurityRatingVerification(rating, testVectors);
      }
    }
  }
}