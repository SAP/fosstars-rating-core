package com.sap.oss.phosphor.fosstars.model.score.oss.risk;

import static com.sap.oss.phosphor.fosstars.TestUtils.DELTA;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssRiskFeatures.IS_ADOPTED;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.sap.oss.phosphor.fosstars.model.Confidence;
import com.sap.oss.phosphor.fosstars.model.Score;
import com.sap.oss.phosphor.fosstars.model.other.Utils;
import com.sap.oss.phosphor.fosstars.model.score.oss.OssSecurityScore;
import com.sap.oss.phosphor.fosstars.model.score.oss.OssSecurityScoreTest;
import com.sap.oss.phosphor.fosstars.model.value.ScoreValue;
import com.sap.oss.phosphor.fosstars.util.Json;
import com.sap.oss.phosphor.fosstars.util.Yaml;
import java.io.IOException;
import org.junit.Test;

public class RiskLikelihoodCoefficientTest {

  private static final RiskLikelihoodCoefficient SCORE = new RiskLikelihoodCoefficient();

  @Test
  public void testJsonSerialization() throws IOException {
    RiskLikelihoodCoefficient clone
        = Json.read(Json.toBytes(SCORE), RiskLikelihoodCoefficient.class);
    assertTrue(SCORE.equals(clone) && clone.equals(SCORE));
    assertEquals(SCORE.hashCode(), clone.hashCode());
  }

  @Test
  public void testYamlSerialization() throws IOException {
    RiskLikelihoodCoefficient clone
        = Yaml.read(Yaml.toBytes(SCORE), RiskLikelihoodCoefficient.class);
    assertEquals(clone, SCORE);
  }

  @Test
  public void testCalculate() {
    Score ossSecurityScore = new OssSecurityScore();
    Score adoptedRiskLikelihoodFactor = new AdoptedRiskLikelihoodFactor();
    ScoreValue scoreValue = SCORE.calculate(
        ossSecurityScore.calculate(OssSecurityScoreTest.defaultValues()),
        adoptedRiskLikelihoodFactor.calculate(IS_ADOPTED.no()));
    assertFalse(scoreValue.isUnknown());
    assertFalse(scoreValue.isNotApplicable());
    assertTrue(Score.INTERVAL.contains(scoreValue.get()));
    assertEquals(scoreValue.confidence(), Confidence.MAX, DELTA);
  }

  @Test
  public void testCalculateWithAllUnknown() {
    assertTrue(SCORE.calculate(Utils.allUnknown(SCORE.allFeatures())).isUnknown());
  }

  @Test
  public void testScoreValueSerialization() throws IOException {
    Score ossSecurityScore = new OssSecurityScore();
    Score adoptedRiskLikelihoodFactor = new AdoptedRiskLikelihoodFactor();
    ScoreValue scoreValue = SCORE.calculate(
        ossSecurityScore.calculate(OssSecurityScoreTest.defaultValues()),
        adoptedRiskLikelihoodFactor.calculate(IS_ADOPTED.no()));
    ScoreValue clone = Json.read(Json.toBytes(scoreValue), ScoreValue.class);
    assertTrue(scoreValue.equals(clone) && clone.equals(scoreValue));
    assertEquals(scoreValue.hashCode(), clone.hashCode());
  }

}