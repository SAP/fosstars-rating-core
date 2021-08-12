package com.sap.oss.phosphor.fosstars.model.score.oss.risk;

import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssRiskFeatures.FUNCTIONALITY;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssRiskFeatures.HANDLING_UNTRUSTED_DATA_LIKELIHOOD;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssRiskFeatures.IS_ADOPTED;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssRiskFeatures.PROJECT_USAGE;
import static com.sap.oss.phosphor.fosstars.model.qa.TestScoreValue.testScoreValue;
import static com.sap.oss.phosphor.fosstars.model.qa.TestVectorBuilder.newTestVector;
import static org.junit.Assert.fail;

import com.sap.oss.phosphor.fosstars.TestUtils;
import com.sap.oss.phosphor.fosstars.model.RatingRepository;
import com.sap.oss.phosphor.fosstars.model.Score;
import com.sap.oss.phosphor.fosstars.model.feature.Likelihood;
import com.sap.oss.phosphor.fosstars.model.feature.Quantity;
import com.sap.oss.phosphor.fosstars.model.feature.oss.Functionality;
import com.sap.oss.phosphor.fosstars.model.math.DoubleInterval;
import com.sap.oss.phosphor.fosstars.model.qa.ScoreVerification;
import com.sap.oss.phosphor.fosstars.model.qa.TestVectors;
import com.sap.oss.phosphor.fosstars.model.qa.VerificationFailedException;
import com.sap.oss.phosphor.fosstars.model.rating.oss.SecurityRiskIntroducedByOss;
import com.sap.oss.phosphor.fosstars.model.score.oss.OssSecurityScore;
import java.util.Optional;
import org.junit.Test;

public class RiskLikelihoodScoreVerificationTest {

  @Test
  public void testVerification() throws VerificationFailedException {
    TestVectors vectors = new TestVectors();

    vectors.add(
        newTestVector("good_security|adopted|low_likelihood")
            .set(testScoreValue(OssSecurityScore.class, Score.MAX))
            .set(IS_ADOPTED.yes())
            .set(PROJECT_USAGE, Quantity.FEW)
            .set(FUNCTIONALITY, Functionality.TESTING)
            .set(HANDLING_UNTRUSTED_DATA_LIKELIHOOD, Likelihood.NEGLIGIBLE)
            .expectedScore(DoubleInterval.closed(0, 1))
            .make());

    vectors.add(
        newTestVector("good_security|adopted|high_likelihood")
            .set(testScoreValue(OssSecurityScore.class, 9.0))
            .set(IS_ADOPTED.yes())
            .set(PROJECT_USAGE, Quantity.A_LOT)
            .set(FUNCTIONALITY, Functionality.SECURITY)
            .set(HANDLING_UNTRUSTED_DATA_LIKELIHOOD, Likelihood.HIGH)
            .expectedScore(DoubleInterval.closed(0, 2))
            .make());

    vectors.add(
        newTestVector("bad_security|not_adopted|high_likelihood")
            .set(testScoreValue(OssSecurityScore.class, 1.0))
            .set(IS_ADOPTED.no())
            .set(PROJECT_USAGE, Quantity.A_LOT)
            .set(FUNCTIONALITY, Functionality.APPLICATION_FRAMEWORK)
            .set(HANDLING_UNTRUSTED_DATA_LIKELIHOOD, Likelihood.HIGH)
            .expectedScore(DoubleInterval.closed(9, 10))
            .make());

    vectors.add(
        newTestVector("bad_security|not_adopted|low_likelihood")
            .set(testScoreValue(OssSecurityScore.class, Score.MIN))
            .set(IS_ADOPTED.no())
            .set(PROJECT_USAGE, Quantity.FEW)
            .set(FUNCTIONALITY, Functionality.ANNOTATIONS)
            .set(HANDLING_UNTRUSTED_DATA_LIKELIHOOD, Likelihood.NEGLIGIBLE)
            .expectedScore(DoubleInterval.closed(0, 1))
            .make());

    Optional<RiskLikelihoodScore> score = TestUtils.find(
        RiskLikelihoodScore.class,
        RatingRepository.INSTANCE.rating(SecurityRiskIntroducedByOss.class));

    if (!score.isPresent()) {
      fail("Could not find the score!");
    }

    new ScoreVerification(score.get(), vectors).run();
  }
}
