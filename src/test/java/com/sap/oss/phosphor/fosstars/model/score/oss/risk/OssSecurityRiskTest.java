package com.sap.oss.phosphor.fosstars.model.score.oss.risk;

import static com.sap.oss.phosphor.fosstars.TestUtils.DELTA;
import static com.sap.oss.phosphor.fosstars.model.feature.DataConfidentialityType.INTERNAL;
import static com.sap.oss.phosphor.fosstars.model.feature.DataConfidentialityType.PERSONAL;
import static com.sap.oss.phosphor.fosstars.model.feature.Impact.HIGH;
import static com.sap.oss.phosphor.fosstars.model.feature.Impact.LOW;
import static com.sap.oss.phosphor.fosstars.model.feature.Likelihood.MEDIUM;
import static com.sap.oss.phosphor.fosstars.model.feature.Likelihood.NEGLIGIBLE;
import static com.sap.oss.phosphor.fosstars.model.feature.Quantity.QUITE_A_LOT;
import static com.sap.oss.phosphor.fosstars.model.feature.Quantity.SOME;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.Functionality.LOGGER;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.Functionality.NETWORKING;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.risk.OssRiskFeatures.AVAILABILITY_IMPACT;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.risk.OssRiskFeatures.CONFIDENTIALITY_IMPACT;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.risk.OssRiskFeatures.DATA_CONFIDENTIALITY;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.risk.OssRiskFeatures.FUNCTIONALITY;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.risk.OssRiskFeatures.HANDLING_UNTRUSTED_DATA_LIKELIHOOD;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.risk.OssRiskFeatures.HOW_MANY_COMPONENTS_USE_OSS_PROJECT;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.risk.OssRiskFeatures.INTEGRITY_IMPACT;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.risk.OssRiskFeatures.IS_ADOPTED;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.sap.oss.phosphor.fosstars.model.Confidence;
import com.sap.oss.phosphor.fosstars.model.Score;
import com.sap.oss.phosphor.fosstars.model.ValueSet;
import com.sap.oss.phosphor.fosstars.model.feature.Impact;
import com.sap.oss.phosphor.fosstars.model.other.Utils;
import com.sap.oss.phosphor.fosstars.model.score.oss.OssSecurityScoreTest;
import com.sap.oss.phosphor.fosstars.model.value.ScoreValue;
import com.sap.oss.phosphor.fosstars.model.value.ValueHashSet;
import com.sap.oss.phosphor.fosstars.util.Json;
import com.sap.oss.phosphor.fosstars.util.Yaml;
import java.io.IOException;
import org.junit.Test;

public class OssSecurityRiskTest {

  private static final OssSecurityRisk SCORE = new OssSecurityRisk();

  @Test
  public void testJsonSerialization() throws IOException {
    OssSecurityRisk clone = Json.read(Json.toBytes(SCORE), OssSecurityRisk.class);
    assertTrue(SCORE.equals(clone) && clone.equals(SCORE));
    assertEquals(SCORE.hashCode(), clone.hashCode());
  }

  @Test
  public void testYamlSerialization() throws IOException {
    OssSecurityRisk clone = Yaml.read(Yaml.toBytes(SCORE), OssSecurityRisk.class);
    assertEquals(clone, SCORE);
  }

  @Test
  public void testCalculate() {
    ValueSet values = new ValueHashSet();
    values.update(OssSecurityScoreTest.defaultValues());
    values.update(HOW_MANY_COMPONENTS_USE_OSS_PROJECT.value(SOME));
    values.update(FUNCTIONALITY.value(LOGGER));
    values.update(HANDLING_UNTRUSTED_DATA_LIKELIHOOD.value(NEGLIGIBLE));
    values.update(IS_ADOPTED.yes());
    values.update(DATA_CONFIDENTIALITY.value(PERSONAL));
    values.update(CONFIDENTIALITY_IMPACT.value(Impact.NEGLIGIBLE));
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
    values.update(OssSecurityScoreTest.defaultValues());
    values.update(HOW_MANY_COMPONENTS_USE_OSS_PROJECT.value(QUITE_A_LOT));
    values.update(FUNCTIONALITY.value(NETWORKING));
    values.update(HANDLING_UNTRUSTED_DATA_LIKELIHOOD.value(MEDIUM));
    values.update(IS_ADOPTED.no());
    values.update(DATA_CONFIDENTIALITY.value(INTERNAL));
    values.update(CONFIDENTIALITY_IMPACT.value(LOW));
    values.update(INTEGRITY_IMPACT.value(LOW));
    values.update(AVAILABILITY_IMPACT.value(HIGH));
    ScoreValue scoreValue = SCORE.calculate(values);
    ScoreValue clone = Json.read(Json.toBytes(scoreValue), ScoreValue.class);
    assertTrue(scoreValue.equals(clone) && clone.equals(scoreValue));
    assertEquals(scoreValue.hashCode(), clone.hashCode());
  }
}