package com.sap.oss.phosphor.fosstars.model.value;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.feature.EnumFeature;
import com.sap.oss.phosphor.fosstars.util.Json;
import java.io.IOException;
import org.junit.Test;

public class EnumValueTest {

  private enum TestEnum {
    A, B, C
  }

  @Test
  public void smokeTest() {
    EnumFeature<TestEnum> feature = new EnumFeature<>(TestEnum.class, "feature");
    EnumValue<TestEnum> value = feature.value(TestEnum.B);
    assertNotNull(value);
    assertFalse(value.isUnknown());
    assertEquals(TestEnum.B, value.get());
  }

  @Test
  public void testUnknown() {
    Value<?> value = new EnumFeature<>(TestEnum.class, "test").unknown();
    assertNotNull(value);
    assertTrue(value.isUnknown());
  }

  @Test
  public void testEqualsAndHashCode() {
    final EnumValue<TestEnum> a = new EnumFeature<>(TestEnum.class, "feature").value(TestEnum.A);
    final EnumValue<TestEnum> b = new EnumFeature<>(TestEnum.class, "feature").value(TestEnum.B);
    final EnumValue<TestEnum> aa = new EnumFeature<>(TestEnum.class, "feature").value(TestEnum.A);
    final Value unknown = new EnumFeature<>(TestEnum.class, "test").unknown();

    assertEquals(a, a);
    assertEquals(a, aa);
    assertNotEquals(a, b);
    assertNotEquals(a, unknown);
    assertNotEquals(b, unknown);
    assertEquals(unknown, unknown);

    assertEquals(a.hashCode(), a.hashCode());
    assertNotEquals(a.hashCode(), b.hashCode());
    assertEquals(a.hashCode(), aa.hashCode());
    assertNotEquals(a.hashCode(), unknown.hashCode());
    assertEquals(unknown.hashCode(), unknown.hashCode());
  }

  @Test
  public void testSerializeAndDeserialize() throws IOException {
    EnumFeature<TestEnum> feature = new EnumFeature<>(TestEnum.class, "feature");
    EnumValue<TestEnum> a = feature.value(TestEnum.A);
    byte[] bytes = Json.toBytes(a);
    assertNotNull(bytes);
    assertTrue(bytes.length > 0);

    Object clone = Json.read(bytes, EnumValue.class);
    assertNotNull(clone);
    assertEquals(a, clone);
    assertEquals(a.hashCode(), clone.hashCode());
  }

}