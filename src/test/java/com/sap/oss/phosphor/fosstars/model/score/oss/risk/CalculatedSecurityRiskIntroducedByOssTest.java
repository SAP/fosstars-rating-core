package com.sap.oss.phosphor.fosstars.model.score.oss.risk;

import static com.sap.oss.phosphor.fosstars.TestUtils.DELTA;
import static com.sap.oss.phosphor.fosstars.model.feature.DataConfidentialityType.INTERNAL;
import static com.sap.oss.phosphor.fosstars.model.feature.Impact.HIGH;
import static com.sap.oss.phosphor.fosstars.model.feature.Impact.LOW;
import static com.sap.oss.phosphor.fosstars.model.feature.Likelihood.MEDIUM;
import static com.sap.oss.phosphor.fosstars.model.feature.Quantity.QUITE_A_LOT;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.Functionality.NETWORKING;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssRiskFeatures.AVAILABILITY_IMPACT;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssRiskFeatures.CONFIDENTIALITY_IMPACT;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssRiskFeatures.DATA_CONFIDENTIALITY;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssRiskFeatures.FUNCTIONALITY;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssRiskFeatures.HANDLING_UNTRUSTED_DATA_LIKELIHOOD;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssRiskFeatures.INTEGRITY_IMPACT;
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

public class CalculatedSecurityRiskIntroducedByOssTest {

  private static final CalculatedSecurityRiskIntroducedByOss SCORE
      = new CalculatedSecurityRiskIntroducedByOss();

  @Test
  public void testJsonSerialization() throws IOException {
    CalculatedSecurityRiskIntroducedByOss clone
        = Json.read(Json.toBytes(SCORE), CalculatedSecurityRiskIntroducedByOss.class);
    assertTrue(SCORE.equals(clone) && clone.equals(SCORE));
    assertEquals(SCORE.hashCode(), clone.hashCode());
  }

  @Test
  public void testYamlSerialization() throws IOException {
    CalculatedSecurityRiskIntroducedByOss clone
        = Yaml.read(Yaml.toBytes(SCORE), CalculatedSecurityRiskIntroducedByOss.class);
    assertEquals(clone, SCORE);
  }

  @Test
  public void testCalculate() {
    ScoreValue scoreValue = SCORE.calculate(defaultValues());
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
    ScoreValue scoreValue = SCORE.calculate(defaultValues());
    ScoreValue clone = Json.read(Json.toBytes(scoreValue), ScoreValue.class);
    assertTrue(scoreValue.equals(clone) && clone.equals(scoreValue));
    assertEquals(scoreValue.hashCode(), clone.hashCode());
  }

  public static ValueSet defaultValues() {
    ValueSet values = new ValueHashSet();
    values.update(OssSecurityScoreTest.defaultValues());
    values.update(PROJECT_USAGE.value(QUITE_A_LOT));
    values.update(FUNCTIONALITY.value(NETWORKING));
    values.update(HANDLING_UNTRUSTED_DATA_LIKELIHOOD.value(MEDIUM));
    values.update(IS_ADOPTED.no());
    values.update(DATA_CONFIDENTIALITY.value(INTERNAL));
    values.update(CONFIDENTIALITY_IMPACT.value(LOW));
    values.update(INTEGRITY_IMPACT.value(LOW));
    values.update(AVAILABILITY_IMPACT.value(HIGH));
    return values;
  }
}