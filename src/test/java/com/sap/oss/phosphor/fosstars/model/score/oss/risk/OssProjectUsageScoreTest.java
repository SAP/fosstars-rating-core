package com.sap.oss.phosphor.fosstars.model.score.oss.risk;

import static com.sap.oss.phosphor.fosstars.TestUtils.DELTA;
import static com.sap.oss.phosphor.fosstars.model.feature.Quantity.FEW;
import static com.sap.oss.phosphor.fosstars.model.feature.Quantity.SOME;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.risk.OssRiskFeatures.HOW_MANY_COMPONENTS_USE_OSS_PROJECT;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.sap.oss.phosphor.fosstars.model.Confidence;
import com.sap.oss.phosphor.fosstars.model.Score;
import com.sap.oss.phosphor.fosstars.model.value.EnumValue;
import com.sap.oss.phosphor.fosstars.model.value.ScoreValue;
import com.sap.oss.phosphor.fosstars.util.Json;
import com.sap.oss.phosphor.fosstars.util.Yaml;
import java.io.IOException;
import org.junit.Test;

public class OssProjectUsageScoreTest {

  private static final OssProjectUsageScore SCORE = new OssProjectUsageScore();

  @Test
  public void testJsonSerialization() throws IOException {
    OssProjectUsageScore clone = Json.read(Json.toBytes(SCORE), OssProjectUsageScore.class);
    assertTrue(SCORE.equals(clone) && clone.equals(SCORE));
    assertEquals(SCORE.hashCode(), clone.hashCode());
  }

  @Test
  public void testYamlSerialization() throws IOException {
    OssProjectUsageScore clone = Yaml.read(Yaml.toBytes(SCORE), OssProjectUsageScore.class);
    assertEquals(clone, SCORE);
  }

  @Test
  public void testCalculate() {
    ScoreValue scoreValue = SCORE.calculate(
        new EnumValue<>(HOW_MANY_COMPONENTS_USE_OSS_PROJECT, FEW));
    assertFalse(scoreValue.isUnknown());
    assertFalse(scoreValue.isNotApplicable());
    assertTrue(Score.INTERVAL.contains(scoreValue.get()));
    assertEquals(scoreValue.confidence(), Confidence.MAX, DELTA);

    assertTrue(SCORE.calculate(HOW_MANY_COMPONENTS_USE_OSS_PROJECT.unknown()).isUnknown());
  }

  @Test
  public void testScoreValueSerialization() throws IOException {
    ScoreValue scoreValue = SCORE.calculate(
        new EnumValue<>(HOW_MANY_COMPONENTS_USE_OSS_PROJECT, SOME));
    ScoreValue clone = Json.read(Json.toBytes(scoreValue), ScoreValue.class);
    assertTrue(scoreValue.equals(clone) && clone.equals(scoreValue));
    assertEquals(scoreValue.hashCode(), clone.hashCode());
  }
}