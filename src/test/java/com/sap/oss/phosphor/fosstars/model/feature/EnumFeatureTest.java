package com.sap.oss.phosphor.fosstars.model.feature;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.sap.oss.phosphor.fosstars.model.value.EnumValue;
import com.sap.oss.phosphor.fosstars.util.Json;
import java.io.IOException;
import org.junit.jupiter.api.Test;

public class EnumFeatureTest {

  @Test
  public void testValue() {
    EnumFeature<TestEnum> feature = new EnumFeature<>(TestEnum.class, "test");
    EnumValue<TestEnum> value = feature.value(TestEnum.A);
    assertNotNull(value);
    assertSame(feature, value.feature());
    assertSame(TestEnum.A, value.get());
  }

  @Test
  public void testParseValid() {
    EnumFeature<TestEnum> feature = new EnumFeature<>(TestEnum.class, "test");
    EnumValue<TestEnum> value = feature.parse("A");
    assertNotNull(value);
    assertSame(feature, value.feature());
    assertSame(TestEnum.A, value.get());
  }

  @Test
  public void testParseInvalid() {
    assertThrows(
        IllegalArgumentException.class,
        () -> {
          EnumFeature<TestEnum> feature = new EnumFeature<>(TestEnum.class, "test");
          EnumValue<TestEnum> value = feature.parse("D");
          assertNotNull(value);
          assertSame(feature, value.feature());
          assertSame(TestEnum.A, value.get());
        });
  }

  @Test
  public void testEqualsAndHashCode() {
    EnumFeature<TestEnum> one = new EnumFeature<>(TestEnum.class, "test");
    assertEquals(one, one);

    EnumFeature<TestEnum> two = new EnumFeature<>(TestEnum.class, "test");
    assertEquals(one, two);
    assertEquals(one.hashCode(), two.hashCode());

    EnumFeature<TestEnum> three = new EnumFeature<>(TestEnum.class, "another");
    assertNotEquals(one, three);

    EnumFeature<AnotherEnum> four = new EnumFeature<>(AnotherEnum.class, "name");
    assertNotEquals(one, four);
  }

  @Test
  public void testSerializeAndDeserialize() throws IOException {
    EnumFeature<TestEnum> feature = new EnumFeature<>(TestEnum.class, "feature");
    byte[] bytes = Json.toBytes(feature);
    assertNotNull(bytes);
    assertTrue(bytes.length > 0);

    Object clone = Json.read(bytes, EnumFeature.class);
    assertNotNull(clone);
    assertEquals(feature, clone);
    assertEquals(feature.hashCode(), clone.hashCode());
  }

  private enum TestEnum {
    A,
    B,
    C
  }

  private enum AnotherEnum {
    A,
    B,
    C,
    D
  }
}
