package com.sap.oss.phosphor.fosstars.model.qa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.sap.oss.phosphor.fosstars.model.RatingRepository;
import com.sap.oss.phosphor.fosstars.model.Score;
import com.sap.oss.phosphor.fosstars.model.feature.example.ExampleFeatures;
import com.sap.oss.phosphor.fosstars.model.math.DoubleInterval;
import com.sap.oss.phosphor.fosstars.model.qa.TestVectorResult.Status;
import com.sap.oss.phosphor.fosstars.model.rating.example.SecurityRatingExample;
import com.sap.oss.phosphor.fosstars.model.rating.example.SecurityRatingExampleVerification;
import com.sap.oss.phosphor.fosstars.model.value.BooleanValue;
import com.sap.oss.phosphor.fosstars.model.value.IntegerValue;
import com.sap.oss.phosphor.fosstars.model.value.ScoreValue;
import java.util.List;
import java.util.Set;
import org.junit.Test;

public class ScoreVerifierTest {

  // an extra test vector which is supposed to fail
  private static final TestVector FAILING_TEST_VECTOR = TestVectorBuilder.newTestVector()
      .set(new IntegerValue(ExampleFeatures.NUMBER_OF_COMMITS_LAST_MONTH_EXAMPLE, 1))
      .set(new IntegerValue(ExampleFeatures.NUMBER_OF_CONTRIBUTORS_LAST_MONTH_EXAMPLE, 1))
      .set(new BooleanValue(ExampleFeatures.SECURITY_REVIEW_DONE_EXAMPLE, false))
      .set(new BooleanValue(ExampleFeatures.STATIC_CODE_ANALYSIS_DONE_EXAMPLE, false))
      .expectedScore(DoubleInterval.init().from(9).to(10).make())
      .alias("test")
      .make();

  private static final TestVectors TEST_VECTORS = new TestVectors();

  static {
    TEST_VECTORS.add(SecurityRatingExampleVerification.TEST_VECTORS);
    TEST_VECTORS.add(FAILING_TEST_VECTOR);
  }

  @Test
  public void testWithFailedTestVectors() {
    ScoreVerifier verifier = new ScoreVerifier(
        RatingRepository.INSTANCE.rating(SecurityRatingExample.class).score(),
        TEST_VECTORS);

    List<TestVectorResult> results = verifier.run();

    assertEquals(7, results.size());
    for (TestVectorResult result : results) {
      assertNotNull(result);
      if (result.failed()) {
        assertEquals(FAILING_TEST_VECTOR.alias(), result.vector.alias());
        assertEquals(Status.FAILED, result.status);
        assertFalse(result.vector.expectedScore().contains(result.scoreValue.get()));
      } else {
        assertNotEquals(FAILING_TEST_VECTOR, result.vector);
        assertEquals(Status.PASSED, result.status);
        assertTrue(result.vector.expectedScore().contains(result.scoreValue.get()));
      }
      assertNotNull(result.message);
      assertFalse(result.message.isEmpty());
    }
  }

  @Test
  public void testWithNotApplicableScoreValue() {
    TestVectors vectors = new TestVectors(
        TestVectorBuilder.newTestVector()
            .alias("1")
            .set(new IntegerValue(ExampleFeatures.NUMBER_OF_COMMITS_LAST_MONTH_EXAMPLE, 1))
            .set(new IntegerValue(ExampleFeatures.NUMBER_OF_CONTRIBUTORS_LAST_MONTH_EXAMPLE, 1))
            .set(new BooleanValue(ExampleFeatures.SECURITY_REVIEW_DONE_EXAMPLE, false))
            .set(new BooleanValue(ExampleFeatures.STATIC_CODE_ANALYSIS_DONE_EXAMPLE, false))
            .expectNotApplicableScore()
            .make(),
        TestVectorBuilder.newTestVector()
            .alias("2")
            .set(new IntegerValue(ExampleFeatures.NUMBER_OF_COMMITS_LAST_MONTH_EXAMPLE, 1))
            .set(new IntegerValue(ExampleFeatures.NUMBER_OF_CONTRIBUTORS_LAST_MONTH_EXAMPLE, 1))
            .set(new BooleanValue(ExampleFeatures.SECURITY_REVIEW_DONE_EXAMPLE, false))
            .set(new BooleanValue(ExampleFeatures.STATIC_CODE_ANALYSIS_DONE_EXAMPLE, false))
            .expectedScore(DoubleInterval.init().from(0.0).to(2.0).make())
            .make(),
        TestVectorBuilder.newTestVector()
            .alias("3")
            .set(new IntegerValue(ExampleFeatures.NUMBER_OF_COMMITS_LAST_MONTH_EXAMPLE, 1))
            .set(new IntegerValue(ExampleFeatures.NUMBER_OF_CONTRIBUTORS_LAST_MONTH_EXAMPLE, 1))
            .set(new BooleanValue(ExampleFeatures.SECURITY_REVIEW_DONE_EXAMPLE, false))
            .set(new BooleanValue(ExampleFeatures.STATIC_CODE_ANALYSIS_DONE_EXAMPLE, false))
            .expectNotApplicableScore()
            .make(),
        TestVectorBuilder.newTestVector()
            .alias("3")
            .set(new IntegerValue(ExampleFeatures.NUMBER_OF_COMMITS_LAST_MONTH_EXAMPLE, 1))
            .set(new IntegerValue(ExampleFeatures.NUMBER_OF_CONTRIBUTORS_LAST_MONTH_EXAMPLE, 1))
            .set(new BooleanValue(ExampleFeatures.SECURITY_REVIEW_DONE_EXAMPLE, false))
            .set(new BooleanValue(ExampleFeatures.STATIC_CODE_ANALYSIS_DONE_EXAMPLE, false))
            .expectedScore(DoubleInterval.init().from(0.0).to(2.0).make())
            .make()
    );

    Score score = mock(Score.class);
    when(score.calculate(any(Set.class)))
        .thenReturn(
            new ScoreValue(score).set(5.0),
            new ScoreValue(score).makeNotApplicable(),
            new ScoreValue(score).makeNotApplicable(),
            new ScoreValue(score).set(1.0));

    ScoreVerifier verifier = new ScoreVerifier(score, vectors);

    List<TestVectorResult> results = verifier.run();

    assertEquals(4, results.size());
    assertEquals(vectors.get(0), results.get(0).vector);
    assertEquals(Status.FAILED, results.get(0).status);
    assertFalse(results.get(0).scoreValue.isNotApplicable());
    assertEquals(vectors.get(1), results.get(1).vector);
    assertEquals(Status.FAILED, results.get(1).status);
    assertTrue(results.get(1).scoreValue.isNotApplicable());
    assertEquals(vectors.get(2), results.get(2).vector);
    assertEquals(Status.PASSED, results.get(2).status);
    assertTrue(results.get(2).scoreValue.isNotApplicable());
    assertEquals(vectors.get(3), results.get(3).vector);
    assertEquals(Status.PASSED, results.get(3).status);
    assertFalse(results.get(3).scoreValue.isNotApplicable());
  }

  @Test(expected = VerificationFailedException.class)
  public void testThatVerificationFails() throws VerificationFailedException {
    ScoreVerifier verifier = new ScoreVerifier(
        RatingRepository.INSTANCE.rating(SecurityRatingExample.class).score(),
        TEST_VECTORS);

    verifier.verify();
  }

}