package com.sap.oss.phosphor.fosstars.model.score.oss.risk;

import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssRiskFeatures.IS_ADOPTED;
import static com.sap.oss.phosphor.fosstars.model.qa.TestScoreValue.testScoreValue;
import static com.sap.oss.phosphor.fosstars.model.qa.TestVectorBuilder.newTestVector;
import static org.junit.Assert.fail;

import com.sap.oss.phosphor.fosstars.TestUtils;
import com.sap.oss.phosphor.fosstars.model.RatingRepository;
import com.sap.oss.phosphor.fosstars.model.Score;
import com.sap.oss.phosphor.fosstars.model.math.DoubleInterval;
import com.sap.oss.phosphor.fosstars.model.qa.ScoreVerification;
import com.sap.oss.phosphor.fosstars.model.qa.TestVectors;
import com.sap.oss.phosphor.fosstars.model.qa.VerificationFailedException;
import com.sap.oss.phosphor.fosstars.model.rating.oss.SecurityRiskIntroducedByOss;
import com.sap.oss.phosphor.fosstars.model.score.oss.OssSecurityScore;
import java.util.Optional;
import org.junit.Test;

public class RiskLikelihoodCoefficientVerificationTest {

  @Test
  public void testVerification() throws VerificationFailedException {
    TestVectors vectors = new TestVectors();

    vectors.add(
        newTestVector("good_security|adopted")
            .set(testScoreValue(OssSecurityScore.class, Score.MAX))
            .set(IS_ADOPTED.yes())
            .expectedScore(DoubleInterval.closed(0, 0.5))
            .make());

    vectors.add(
        newTestVector("good_security|not_adopted")
            .set(testScoreValue(OssSecurityScore.class, Score.MAX))
            .set(IS_ADOPTED.no())
            .expectedScore(DoubleInterval.closed(1, 3))
            .make());

    vectors.add(
        newTestVector("bad_security|adopted")
            .set(testScoreValue(OssSecurityScore.class, Score.MIN))
            .set(IS_ADOPTED.yes())
            .expectedScore(DoubleInterval.closed(7, 9))
            .make());

    vectors.add(
        newTestVector("bad_security|not_adopted")
            .set(testScoreValue(OssSecurityScore.class, Score.MIN))
            .set(IS_ADOPTED.no())
            .expectedScore(DoubleInterval.closed(9.5, 10))
            .make());

    Optional<RiskLikelihoodCoefficient> score = TestUtils.find(
        RiskLikelihoodCoefficient.class,
        RatingRepository.INSTANCE.rating(SecurityRiskIntroducedByOss.class));

    if (!score.isPresent()) {
      fail("Could not find the score!");
    }

    new ScoreVerification(score.get(), vectors).run();
  }
}
