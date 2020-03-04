package com.sap.sgs.phosphor.fosstars.model.qa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.sap.sgs.phosphor.fosstars.model.RatingRepository;
import com.sap.sgs.phosphor.fosstars.model.feature.example.ExampleFeatures;
import com.sap.sgs.phosphor.fosstars.model.math.DoubleInterval;
import com.sap.sgs.phosphor.fosstars.model.qa.TestVectorResult.Status;
import com.sap.sgs.phosphor.fosstars.model.rating.example.SecurityRatingExample;
import com.sap.sgs.phosphor.fosstars.model.rating.example.SecurityRatingExample.SecurityLabelExample;
import com.sap.sgs.phosphor.fosstars.model.rating.example.SecurityRatingExampleVerification;
import com.sap.sgs.phosphor.fosstars.model.value.BooleanValue;
import com.sap.sgs.phosphor.fosstars.model.value.IntegerValue;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;

public class RatingVerifierTest {

  // an extra test vector which is supposed to fail
  private static final TestVector FAILING_TEST_VECTOR = TestVectorBuilder.newTestVector()
      .set(new IntegerValue(ExampleFeatures.NUMBER_OF_COMMITS_LAST_MONTH_EXAMPLE, 1))
      .set(new IntegerValue(ExampleFeatures.NUMBER_OF_CONTRIBUTORS_LAST_MONTH_EXAMPLE, 1))
      .set(new BooleanValue(ExampleFeatures.SECURITY_REVIEW_DONE_EXAMPLE, false))
      .set(new BooleanValue(ExampleFeatures.STATIC_CODE_ANALYSIS_DONE_EXAMPLE, false))
      .expectedScore(DoubleInterval.init().from(9).to(10).make())
      .expectedLabel(SecurityLabelExample.AWESOME)
      .make();

  private static final List<TestVector> TEST_VECTORS = new ArrayList<>();

  static {
    TEST_VECTORS.addAll(SecurityRatingExampleVerification.TEST_VECTORS);
    TEST_VECTORS.add(FAILING_TEST_VECTOR);
  }

  @Test
  public void failedVectors() {
    RatingVerifier verifier = new RatingVerifier(
        RatingRepository.INSTANCE.rating(SecurityRatingExample.class),
        TEST_VECTORS);

    List<TestVectorResult> results = verifier.run();

    assertEquals(7, results.size());
    for (TestVectorResult result : results) {
      assertNotNull(result);
      if (result.failed()) {
        assertEquals(FAILING_TEST_VECTOR, result.vector);
        assertEquals(Status.FAILED, result.status);
        assertFalse(result.vector.expectedScore().contains(result.scoreValue));
      } else {
        assertNotEquals(FAILING_TEST_VECTOR, result.vector);
        assertEquals(Status.PASSED, result.status);
        assertTrue(result.vector.expectedScore().contains(result.scoreValue));
      }
      assertNotNull(result.message);
      assertFalse(result.message.isEmpty());
    }
  }

  @Test(expected = VerificationFailedException.class)
  public void run() throws VerificationFailedException {
    RatingVerifier verifier = new RatingVerifier(
        RatingRepository.INSTANCE.rating(SecurityRatingExample.class),
        TEST_VECTORS);

    verifier.verify();
  }

}