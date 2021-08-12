package com.sap.oss.phosphor.fosstars.model.score.oss.risk;

import static com.sap.oss.phosphor.fosstars.TestUtils.DELTA;
import static com.sap.oss.phosphor.fosstars.model.feature.Likelihood.HIGH;
import static com.sap.oss.phosphor.fosstars.model.feature.Likelihood.LOW;
import static com.sap.oss.phosphor.fosstars.model.feature.Quantity.A_LOT;
import static com.sap.oss.phosphor.fosstars.model.feature.Quantity.SOME;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.Functionality.OTHER;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.Functionality.SECURITY;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssRiskFeatures.FUNCTIONALITY;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssRiskFeatures.HANDLING_UNTRUSTED_DATA_LIKELIHOOD;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssRiskFeatures.IS_ADOPTED;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssRiskFeatures.PROJECT_USAGE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.sap.oss.phosphor.fosstars.model.Confidence;
import com.sap.oss.phosphor.fosstars.model.Score;
import com.sap.oss.phosphor.fosstars.model.ValueSet;
import com.sap.oss.phosphor.fosstars.model.other.Utils;
import com.sap.oss.phosphor.fosstars.model.score.oss.OssSecurityScoreTest;
import com.sap.oss.phosphor.fosstars.model.value.ScoreValue;
import com.sap.oss.phosphor.fosstars.model.value.ValueHashSet;
import com.sap.oss.phosphor.fosstars.util.Json;
import com.sap.oss.phosphor.fosstars.util.Yaml;
import java.io.IOException;
import org.junit.Test;

public class RiskLikelihoodFactorsTest {

  private static final RiskLikelihoodFactors SCORE = new RiskLikelihoodFactors();

  @Test
  public void testJsonSerialization() throws IOException {
    RiskLikelihoodFactors clone = Json.read(Json.toBytes(SCORE), RiskLikelihoodFactors.class);
    assertTrue(SCORE.equals(clone) && clone.equals(SCORE));
    assertEquals(SCORE.hashCode(), clone.hashCode());
  }

  @Test
  public void testYamlSerialization() throws IOException {
    RiskLikelihoodFactors clone = Yaml.read(Yaml.toBytes(SCORE), RiskLikelihoodFactors.class);
    assertEquals(clone, SCORE);
  }

  @Test
  public void testCalculate() {
    ValueSet values = new ValueHashSet();
    values.update(OssSecurityScoreTest.defaultValues());
    values.update(PROJECT_USAGE.value(A_LOT));
    values.update(FUNCTIONALITY.value(SECURITY));
    values.update(HANDLING_UNTRUSTED_DATA_LIKELIHOOD.value(HIGH));
    values.update(IS_ADOPTED.yes());
    ScoreValue scoreValue = SCORE.calculate(values);
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
    ValueSet values = new ValueHashSet();
    values.update(OssSecurityScoreTest.defaultValues());
    values.update(PROJECT_USAGE.value(SOME));
    values.update(FUNCTIONALITY.value(OTHER));
    values.update(HANDLING_UNTRUSTED_DATA_LIKELIHOOD.value(LOW));
    values.update(IS_ADOPTED.no());
    ScoreValue scoreValue = SCORE.calculate(values);
    ScoreValue clone = Json.read(Json.toBytes(scoreValue), ScoreValue.class);
    assertTrue(scoreValue.equals(clone) && clone.equals(scoreValue));
    assertEquals(scoreValue.hashCode(), clone.hashCode());
  }

}