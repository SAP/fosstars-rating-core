package com.sap.oss.phosphor.fosstars.model.score.oss.risk;

import static com.sap.oss.phosphor.fosstars.TestUtils.DELTA;
import static com.sap.oss.phosphor.fosstars.model.feature.DataConfidentialityType.INTERNAL;
import static com.sap.oss.phosphor.fosstars.model.feature.DataConfidentialityType.PERSONAL;
import static com.sap.oss.phosphor.fosstars.model.feature.Impact.HIGH;
import static com.sap.oss.phosphor.fosstars.model.feature.Impact.LOW;
import static com.sap.oss.phosphor.fosstars.model.feature.Impact.MEDIUM;
import static com.sap.oss.phosphor.fosstars.model.feature.Impact.NEGLIGIBLE;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssRiskFeatures.AVAILABILITY_IMPACT;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssRiskFeatures.CONFIDENTIALITY_IMPACT;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssRiskFeatures.DATA_CONFIDENTIALITY;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssRiskFeatures.INTEGRITY_IMPACT;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.sap.oss.phosphor.fosstars.model.Confidence;
import com.sap.oss.phosphor.fosstars.model.Score;
import com.sap.oss.phosphor.fosstars.model.ValueSet;
import com.sap.oss.phosphor.fosstars.model.other.Utils;
import com.sap.oss.phosphor.fosstars.model.value.ScoreValue;
import com.sap.oss.phosphor.fosstars.model.value.ValueHashSet;
import com.sap.oss.phosphor.fosstars.util.Json;
import com.sap.oss.phosphor.fosstars.util.Yaml;
import java.io.IOException;
import org.junit.Test;

public class RiskImpactScoreTest {

  private static final RiskImpactScore SCORE = new RiskImpactScore();

  @Test
  public void testJsonSerialization() throws IOException {
    RiskImpactScore clone = Json.read(Json.toBytes(SCORE), RiskImpactScore.class);
    assertTrue(SCORE.equals(clone) && clone.equals(SCORE));
    assertEquals(SCORE.hashCode(), clone.hashCode());
  }

  @Test
  public void testYamlSerialization() throws IOException {
    RiskImpactScore clone = Yaml.read(Yaml.toBytes(SCORE), RiskImpactScore.class);
    assertEquals(clone, SCORE);
  }

  @Test
  public void testCalculate() {
    ValueSet values = new ValueHashSet();
    values.update(DATA_CONFIDENTIALITY.value(PERSONAL));
    values.update(CONFIDENTIALITY_IMPACT.value(NEGLIGIBLE));
    values.update(INTEGRITY_IMPACT.value(LOW));
    values.update(AVAILABILITY_IMPACT.value(HIGH));
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
    values.update(DATA_CONFIDENTIALITY.value(INTERNAL));
    values.update(CONFIDENTIALITY_IMPACT.value(MEDIUM));
    values.update(INTEGRITY_IMPACT.value(HIGH));
    values.update(AVAILABILITY_IMPACT.value(LOW));
    ScoreValue scoreValue = SCORE.calculate(values);
    ScoreValue clone = Json.read(Json.toBytes(scoreValue), ScoreValue.class);
    assertTrue(scoreValue.equals(clone) && clone.equals(scoreValue));
    assertEquals(scoreValue.hashCode(), clone.hashCode());
  }
}