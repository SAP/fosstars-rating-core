package com.sap.oss.phosphor.fosstars.model.tuning;

import static com.sap.oss.phosphor.fosstars.model.feature.example.ExampleFeatures.NUMBER_OF_COMMITS_LAST_MONTH_EXAMPLE;
import static com.sap.oss.phosphor.fosstars.model.feature.example.ExampleFeatures.NUMBER_OF_CONTRIBUTORS_LAST_MONTH_EXAMPLE;
import static com.sap.oss.phosphor.fosstars.model.feature.example.ExampleFeatures.SECURITY_REVIEW_DONE_EXAMPLE;
import static com.sap.oss.phosphor.fosstars.model.feature.example.ExampleFeatures.STATIC_CODE_ANALYSIS_DONE_EXAMPLE;
import static com.sap.oss.phosphor.fosstars.model.qa.TestVectorBuilder.newTestVector;
import static org.junit.Assert.assertNotNull;

import com.sap.oss.phosphor.fosstars.model.math.DoubleInterval;
import com.sap.oss.phosphor.fosstars.model.qa.ScoreVerification;
import com.sap.oss.phosphor.fosstars.model.qa.ScoreVerifier;
import com.sap.oss.phosphor.fosstars.model.qa.TestVectors;
import com.sap.oss.phosphor.fosstars.model.score.example.SecurityScoreExample;
import org.junit.Test;

public class TuningWithCMAESTest {

  private static final TestVectors SIMPLE_TEST_VECTORS = new TestVectors(

      // bad project
      newTestVector()
          .set(NUMBER_OF_COMMITS_LAST_MONTH_EXAMPLE.value(5))
          .set(NUMBER_OF_CONTRIBUTORS_LAST_MONTH_EXAMPLE.value(1))
          .set(SECURITY_REVIEW_DONE_EXAMPLE.value(false))
          .set(STATIC_CODE_ANALYSIS_DONE_EXAMPLE.value(false))
          .expectedScore(DoubleInterval.closed(0.0, 2.0))
          .alias("bad")
          .make(),

      // average project
      newTestVector()
          .set(NUMBER_OF_COMMITS_LAST_MONTH_EXAMPLE.value(200))
          .set(NUMBER_OF_CONTRIBUTORS_LAST_MONTH_EXAMPLE.value(10))
          .set(SECURITY_REVIEW_DONE_EXAMPLE.value(false))
          .set(STATIC_CODE_ANALYSIS_DONE_EXAMPLE.value(true))
          .expectedScore(DoubleInterval.closed(3.0, 7.0))
          .alias("bad")
          .make(),

      // good project
      newTestVector()
          .set(NUMBER_OF_COMMITS_LAST_MONTH_EXAMPLE.value(1000))
          .set(NUMBER_OF_CONTRIBUTORS_LAST_MONTH_EXAMPLE.value(50))
          .set(SECURITY_REVIEW_DONE_EXAMPLE.value(true))
          .set(STATIC_CODE_ANALYSIS_DONE_EXAMPLE.value(true))
          .expectedScore(DoubleInterval.closed(9.0, 10.0))
          .alias("good")
          .make()
  );

  @Test
  public void testWithSimpleTestVectors() throws Exception {
    SecurityScoreExample score = new SecurityScoreExample();

    ScoreVerification verification = new ScoreVerification(score, SIMPLE_TEST_VECTORS);
    assertNotNull(verification);

    new TuningWithCMAES(score, new ScoreVerifier(score, SIMPLE_TEST_VECTORS)).run();

    verification.run();
  }
}