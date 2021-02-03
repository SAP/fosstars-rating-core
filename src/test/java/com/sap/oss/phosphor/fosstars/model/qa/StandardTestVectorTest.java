package com.sap.oss.phosphor.fosstars.model.qa;

import static com.sap.oss.phosphor.fosstars.model.other.Utils.allUnknown;
import static com.sap.oss.phosphor.fosstars.model.qa.TestVectorBuilder.newTestVector;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.sap.oss.phosphor.fosstars.model.Interval;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.feature.example.ExampleFeatures;
import com.sap.oss.phosphor.fosstars.model.math.DoubleInterval;
import com.sap.oss.phosphor.fosstars.model.rating.example.SecurityRatingExample.SecurityLabelExample;
import com.sap.oss.phosphor.fosstars.model.value.BooleanValue;
import com.sap.oss.phosphor.fosstars.model.value.IntegerValue;
import com.sap.oss.phosphor.fosstars.util.Json;
import com.sap.oss.phosphor.fosstars.util.Yaml;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import org.junit.Test;

public class StandardTestVectorTest {

  private static final DoubleInterval NO_EXPECTED_SCORE = null;

  private static final boolean NOT_APPLICABLE = true;

  private static final boolean IS_KNOWN = false;

  @Test
  public void smoke() {
    Set<Value<?>> values = new HashSet<>();
    values.add(new IntegerValue(ExampleFeatures.NUMBER_OF_COMMITS_LAST_MONTH_EXAMPLE, 1));
    Interval expectedScore = DoubleInterval.init().from(4.0).to(6.4).closed().make();
    StandardTestVector vector = new StandardTestVector(
        values, expectedScore, SecurityLabelExample.OKAY, "test");

    assertFalse(vector.expectsNotApplicableScore());
    assertFalse(vector.values().isEmpty());
    assertTrue(vector.values().contains(
        new IntegerValue(ExampleFeatures.NUMBER_OF_COMMITS_LAST_MONTH_EXAMPLE, 1)));
    assertFalse(vector.values().contains(
        new IntegerValue(ExampleFeatures.NUMBER_OF_COMMITS_LAST_MONTH_EXAMPLE, 2)));
    assertFalse(vector.values().contains(
        new IntegerValue(ExampleFeatures.NUMBER_OF_CONTRIBUTORS_LAST_MONTH_EXAMPLE, 1)));
    assertEquals(DoubleInterval.init().from(4.0).to(6.4).closed().make(), vector.expectedScore());
    assertNotEquals(DoubleInterval.init().from(4.0).to(6.4).open().make(), vector.expectedScore());
    assertEquals(SecurityLabelExample.OKAY, vector.expectedLabel());
  }

  @Test(expected = NullPointerException.class)
  public void noValues() {
    Interval expectedScore = DoubleInterval.init().from(4.0).to(6.4).closed().make();
    new StandardTestVector(null, expectedScore, SecurityLabelExample.OKAY, "test");
  }

  @Test(expected = IllegalArgumentException.class)
  public void emptyValues() {
    Set<Value<?>> values = new HashSet<>();
    Interval expectedScore = DoubleInterval.init().from(4.0).to(6.4).closed().make();
    new StandardTestVector(values, expectedScore, SecurityLabelExample.OKAY, "test");
  }

  @Test(expected = IllegalArgumentException.class)
  public void noExpectedScore() {
    Set<Value<?>> values = new HashSet<>();
    values.add(new IntegerValue(ExampleFeatures.NUMBER_OF_COMMITS_LAST_MONTH_EXAMPLE, 1));
    new StandardTestVector(values, NO_EXPECTED_SCORE, SecurityLabelExample.OKAY, "test");
  }

  @Test
  public void notApplicableScore() {
    Set<Value<?>> values = new HashSet<>();
    values.add(new IntegerValue(ExampleFeatures.NUMBER_OF_COMMITS_LAST_MONTH_EXAMPLE, 1));
    new StandardTestVector(
        values, NO_EXPECTED_SCORE, SecurityLabelExample.OKAY, "test", IS_KNOWN, NOT_APPLICABLE);
  }

  @Test
  public void noLabel() {
    Set<Value<?>> values = new HashSet<>();
    values.add(new IntegerValue(ExampleFeatures.NUMBER_OF_COMMITS_LAST_MONTH_EXAMPLE, 1));
    Interval expectedScore = DoubleInterval.init().from(4.0).to(6.4).closed().make();
    StandardTestVector vector = new StandardTestVector(
        values, expectedScore, null, "test");
    assertNull(vector.expectedLabel());
  }

  @Test
  public void equalsAndHashcode() {
    final Interval expectedScoreOne = DoubleInterval.init().from(4.0).to(6.4).closed().make();
    final Interval expectedScoreTwo = DoubleInterval.init().from(1.0).to(3.2).closed().make();

    assertEquals(
        newTestVector()
            .set(allUnknown(ExampleFeatures.SECURITY_REVIEW_DONE_EXAMPLE))
            .expectedScore(expectedScoreOne)
            .expectedLabel(SecurityLabelExample.OKAY)
            .alias("test")
            .make(),
        newTestVector()
            .set(allUnknown(ExampleFeatures.SECURITY_REVIEW_DONE_EXAMPLE))
            .expectedScore(expectedScoreOne)
            .expectedLabel(SecurityLabelExample.OKAY)
            .alias("test")
            .make());
    assertEquals(
        newTestVector()
            .set(new BooleanValue(ExampleFeatures.SECURITY_REVIEW_DONE_EXAMPLE, true))
            .expectedScore(expectedScoreOne)
            .expectedLabel(SecurityLabelExample.OKAY)
            .alias("test")
            .make(),
        newTestVector()
            .set(new BooleanValue(ExampleFeatures.SECURITY_REVIEW_DONE_EXAMPLE, true))
            .expectedScore(expectedScoreOne)
            .expectedLabel(SecurityLabelExample.OKAY)
            .alias("test")
            .make());

    assertNotEquals(
        newTestVector()
            .set(new BooleanValue(ExampleFeatures.SECURITY_REVIEW_DONE_EXAMPLE, true))
            .expectedScore(expectedScoreOne)
            .expectedLabel(SecurityLabelExample.OKAY)
            .alias("test")
            .make(),
        newTestVector()
            .set(new BooleanValue(ExampleFeatures.SECURITY_REVIEW_DONE_EXAMPLE, false))
            .expectedScore(expectedScoreOne)
            .expectedLabel(SecurityLabelExample.OKAY)
            .alias("test")
            .make());
    assertNotEquals(
        newTestVector()
            .set(new BooleanValue(ExampleFeatures.SECURITY_REVIEW_DONE_EXAMPLE, true))
            .expectedScore(expectedScoreOne)
            .expectedLabel(SecurityLabelExample.OKAY)
            .alias("test")
            .make(),
        newTestVector()
            .set(new BooleanValue(ExampleFeatures.SECURITY_REVIEW_DONE_EXAMPLE, true))
            .expectedScore(expectedScoreTwo)
            .expectedLabel(SecurityLabelExample.OKAY)
            .alias("test")
            .make());
    assertNotEquals(
        newTestVector()
            .set(new BooleanValue(ExampleFeatures.SECURITY_REVIEW_DONE_EXAMPLE, true))
            .expectedScore(expectedScoreOne)
            .expectedLabel(SecurityLabelExample.OKAY)
            .alias("test")
            .make(),
        newTestVector()
            .set(new BooleanValue(ExampleFeatures.SECURITY_REVIEW_DONE_EXAMPLE, true))
            .expectedScore(expectedScoreOne)
            .expectedLabel(SecurityLabelExample.AWFUL)
            .alias("test")
            .make());
  }

  @Test
  public void yamlSerializeAndDeserialize() throws IOException {
    Set<Value<?>> values = new HashSet<>();
    values.add(new IntegerValue(ExampleFeatures.NUMBER_OF_COMMITS_LAST_MONTH_EXAMPLE, 1));
    Interval expectedScore = DoubleInterval.init().from(4.0).to(6.4).closed().make();
    StandardTestVector vector = new StandardTestVector(
        values, expectedScore, SecurityLabelExample.OKAY, "test");

    byte[] bytes = Yaml.toBytes(vector);
    assertNotNull(bytes);
    assertTrue(bytes.length > 0);
    StandardTestVector clone = Yaml.read(bytes, StandardTestVector.class);
    assertEquals(vector, clone);
    assertEquals(vector.hashCode(), clone.hashCode());
  }

  @Test
  public void jsonSerializeAndDeserialize() throws IOException {
    Set<Value<?>> values = new HashSet<>();
    values.add(new IntegerValue(ExampleFeatures.NUMBER_OF_COMMITS_LAST_MONTH_EXAMPLE, 1));
    Interval expectedScore = DoubleInterval.init().from(4.0).to(6.4).closed().make();
    StandardTestVector vector = new StandardTestVector(
        values, expectedScore, SecurityLabelExample.OKAY, "test");

    byte[] bytes = Json.toBytes(vector);
    assertNotNull(bytes);
    assertTrue(bytes.length > 0);
    StandardTestVector clone = Json.read(bytes, StandardTestVector.class);
    assertEquals(vector, clone);
    assertEquals(vector.hashCode(), clone.hashCode());
  }

  @Test
  public void jsonSerializeAndDeserializeWithNotApplicableScoreValue() throws IOException {
    Set<Value<?>> values = new HashSet<>();
    values.add(new IntegerValue(ExampleFeatures.NUMBER_OF_COMMITS_LAST_MONTH_EXAMPLE, 1));
    StandardTestVector vector = new StandardTestVector(
        values, null, SecurityLabelExample.OKAY, "test", false, true);

    byte[] bytes = Json.toBytes(vector);
    assertNotNull(bytes);
    assertTrue(bytes.length > 0);
    StandardTestVector clone = Json.read(bytes, StandardTestVector.class);
    assertEquals(vector, clone);
    assertEquals(vector.hashCode(), clone.hashCode());
  }

}
