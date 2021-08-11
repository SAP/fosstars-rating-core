package com.sap.oss.phosphor.fosstars.model.rating.oss;

import static com.sap.oss.phosphor.fosstars.model.rating.oss.SecurityRiskIntroducedByOss.OssSecurityRiskLabel.UNCLEAR;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.sap.oss.phosphor.fosstars.model.Score;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.ValueSet;
import com.sap.oss.phosphor.fosstars.model.other.Utils;
import com.sap.oss.phosphor.fosstars.model.score.oss.risk.CalculatedSecurityRiskIntroducedByOssTest;
import com.sap.oss.phosphor.fosstars.model.value.RatingValue;
import com.sap.oss.phosphor.fosstars.util.Json;
import com.sap.oss.phosphor.fosstars.util.Yaml;
import java.io.IOException;
import java.util.Set;
import org.junit.Test;

public class SecurityRiskIntroducedByOssTest {

  private static final SecurityRiskIntroducedByOss RATING
      = new SecurityRiskIntroducedByOss();

  @Test
  public void testJsonSerialization() throws IOException {
    SecurityRiskIntroducedByOss clone
        = Json.read(Json.toBytes(RATING), SecurityRiskIntroducedByOss.class);
    assertEquals(RATING, clone);
  }

  @Test
  public void testYamlSerialization() throws IOException {
    SecurityRiskIntroducedByOss clone
        = Yaml.read(Yaml.toBytes(RATING), SecurityRiskIntroducedByOss.class);
    assertEquals(RATING, clone);
  }

  @Test
  public void testRatingValueJsonSerialization() throws IOException {
    ValueSet values = CalculatedSecurityRiskIntroducedByOssTest.defaultValues();
    RatingValue ratingValue = RATING.calculate(values);
    RatingValue clone = Json.read(Json.toBytes(ratingValue), RatingValue.class);
    assertEquals(ratingValue, clone);
    assertEquals(ratingValue.hashCode(), clone.hashCode());
  }

  @Test
  public void testCalculate() {
    ValueSet values = CalculatedSecurityRiskIntroducedByOssTest.defaultValues();
    RatingValue ratingValue = RATING.calculate(values);
    assertTrue(Score.INTERVAL.contains(ratingValue.score()));
    assertNotNull(ratingValue.label());
    assertNotEquals(UNCLEAR, ratingValue.label());
  }

  @Test
  public void testCalculateWitAllUnknown() {
    Set<Value<?>> values = Utils.allUnknown(RATING.allFeatures());
    RatingValue ratingValue = RATING.calculate(values);
    assertTrue(ratingValue.scoreValue().isUnknown());
  }

  @Test
  public void testEqualsAndHashCode() {
    SecurityRiskIntroducedByOss one = new SecurityRiskIntroducedByOss();
    SecurityRiskIntroducedByOss two = new SecurityRiskIntroducedByOss();
    assertTrue(one.equals(two) && two.equals(one));
    assertEquals(one.hashCode(), two.hashCode());
  }
}