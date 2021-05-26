package com.sap.oss.phosphor.fosstars.model.value;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sap.oss.phosphor.fosstars.model.Feature;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.feature.AbstractFeature;
import com.sap.oss.phosphor.fosstars.util.Json;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;

public class AbstractKnownValueTest {

  private static class FeatureImpl extends AbstractFeature<String> {

    FeatureImpl(@JsonProperty("name") String name) {
      super(name);
    }

    @Override
    public Value<String> value(String object) {
      return new ValueImpl(this, object);
    }

    @Override
    public Value<String> parse(String string) {
      throw new UnsupportedOperationException();
    }
  }

  private static class ValueImpl extends AbstractKnownValue<String> {

    final String value;

    ValueImpl(
        @JsonProperty("feature") Feature<String> feature,
        @JsonProperty("value") String value) {

      super(feature);
      this.value = value;
    }

    @Override
    @JsonGetter("value")
    public String get() {
      return value;
    }
  }

  @Test
  public void testProcessIfKnown() {
    ValueImpl value = new ValueImpl(new FeatureImpl("feature"), "test");
    assertFalse(value.isUnknown());

    List<String> processedValues = new ArrayList<>();

    value.processIfKnown(object -> {
      assertEquals("test", object);
      processedValues.add(object);
    }).processIfUnknown(() -> fail("This should not be called!"));

    Value<String> unknown = new FeatureImpl("feature").unknown();
    unknown.processIfKnown(object -> {
      fail("this should not be reached");
    }).processIfUnknown(() -> {
      processedValues.add("unknown");
    });

    assertEquals(2, processedValues.size());
    assertEquals("test", processedValues.get(0));
    assertEquals("unknown", processedValues.get(1));
  }

  @Test
  public void testOrElse() {
    FeatureImpl feature = new FeatureImpl("feature");
    assertEquals("value", new ValueImpl(feature, "value").orElse("another"));
    assertEquals("another", feature.unknown().orElse("another"));
  }

  @Test
  public void testJsonSerialization() throws IOException {
    ObjectMapper mapper = Json.mapper();
    mapper.registerSubtypes(FeatureImpl.class, ValueImpl.class);

    ValueImpl valueWithExplanation = new ValueImpl(new FeatureImpl("feature"), "test");
    valueWithExplanation.explain("this is an explanation");

    ValueImpl clone = mapper.readValue(
        mapper.writeValueAsBytes(valueWithExplanation), ValueImpl.class);

    assertEquals(valueWithExplanation, clone);
    assertEquals(valueWithExplanation.hashCode(), clone.hashCode());
    assertEquals(1, clone.explanation().size());
    assertEquals("this is an explanation", clone.explanation().get(0));

    ValueImpl valueWithoutExplanation
        = new ValueImpl(new FeatureImpl("feature"), "test");

    assertNotEquals(valueWithExplanation, valueWithoutExplanation);

    clone = mapper.readValue(mapper.writeValueAsBytes(valueWithoutExplanation), ValueImpl.class);

    assertEquals(valueWithoutExplanation, clone);
    assertEquals(valueWithoutExplanation.hashCode(), clone.hashCode());
    assertTrue(valueWithoutExplanation.explanation().isEmpty());
  }

  @Test
  public void testJsonDeserializationWithoutExplanations() throws IOException {
    ObjectMapper mapper = Json.mapper();
    mapper.registerSubtypes(FeatureImpl.class, ValueImpl.class);
    String json = "{"
        + "  \"type\":\"AbstractKnownValueTest$ValueImpl\","
        + "  \"feature\":{"
        + "    \"type\":\"AbstractKnownValueTest$FeatureImpl\","
        + "    \"name\":\"feature\""
        + "  },"
        + "  \"value\": \"test\""
        + "}";
    ValueImpl value = mapper.readValue(json, ValueImpl.class);
    assertEquals("feature", value.feature().name());
    assertEquals("test", value.get());
    assertTrue(value.explanation().isEmpty());
  }
}