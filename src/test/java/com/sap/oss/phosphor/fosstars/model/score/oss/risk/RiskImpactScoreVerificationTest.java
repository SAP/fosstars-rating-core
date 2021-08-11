package com.sap.oss.phosphor.fosstars.model.score.oss.risk;

import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssRiskFeatures.AVAILABILITY_IMPACT;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssRiskFeatures.CONFIDENTIALITY_IMPACT;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssRiskFeatures.DATA_CONFIDENTIALITY;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssRiskFeatures.INTEGRITY_IMPACT;
import static com.sap.oss.phosphor.fosstars.model.qa.TestVectorBuilder.newTestVector;
import static org.junit.Assert.fail;

import com.sap.oss.phosphor.fosstars.TestUtils;
import com.sap.oss.phosphor.fosstars.model.RatingRepository;
import com.sap.oss.phosphor.fosstars.model.feature.DataConfidentialityType;
import com.sap.oss.phosphor.fosstars.model.feature.Impact;
import com.sap.oss.phosphor.fosstars.model.math.DoubleInterval;
import com.sap.oss.phosphor.fosstars.model.qa.ScoreVerification;
import com.sap.oss.phosphor.fosstars.model.qa.TestVectors;
import com.sap.oss.phosphor.fosstars.model.qa.VerificationFailedException;
import com.sap.oss.phosphor.fosstars.model.rating.oss.SecurityRiskIntroducedByOss;
import java.util.Optional;
import org.junit.Test;

public class RiskImpactScoreVerificationTest {

  @Test
  public void testVerification() throws VerificationFailedException {
    TestVectors vectors = new TestVectors();

    vectors.add(
        newTestVector("low_impact")
            .set(DATA_CONFIDENTIALITY, DataConfidentialityType.TEST)
            .set(CONFIDENTIALITY_IMPACT, Impact.NEGLIGIBLE)
            .set(INTEGRITY_IMPACT, Impact.NEGLIGIBLE)
            .set(AVAILABILITY_IMPACT, Impact.NEGLIGIBLE)
            .expectedScore(DoubleInterval.closed(0, 1))
            .make());

    vectors.add(
        newTestVector("average_impact")
            .set(DATA_CONFIDENTIALITY, DataConfidentialityType.INTERNAL)
            .set(CONFIDENTIALITY_IMPACT, Impact.MEDIUM)
            .set(INTEGRITY_IMPACT, Impact.MEDIUM)
            .set(AVAILABILITY_IMPACT, Impact.MEDIUM)
            .expectedScore(DoubleInterval.closed(4, 6))
            .make());

    vectors.add(
        newTestVector("high_impact")
            .set(DATA_CONFIDENTIALITY, DataConfidentialityType.PERSONAL)
            .set(CONFIDENTIALITY_IMPACT, Impact.HIGH)
            .set(INTEGRITY_IMPACT, Impact.HIGH)
            .set(AVAILABILITY_IMPACT, Impact.HIGH)
            .expectedScore(DoubleInterval.closed(9, 10))
            .make());

    Optional<RiskImpactScore> score = TestUtils.find(
        RiskImpactScore.class,
        RatingRepository.INSTANCE.rating(SecurityRiskIntroducedByOss.class));

    if (!score.isPresent()) {
      fail("Could not find the score!");
    }

    new ScoreVerification(score.get(), vectors).run();
  }
}