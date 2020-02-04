package com.sap.sgs.phosphor.fosstars.model.value;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sap.sgs.phosphor.fosstars.model.Value;
import com.sap.sgs.phosphor.fosstars.model.feature.AbstractFeature;
import java.io.IOException;
import org.junit.Ignore;
import org.junit.Test;

public class EnumValueTest {

  private enum TestEnum {

    A, B, C;

    @JsonCreator
    public static TestEnum parse(String string) {
      return TestEnum.valueOf(string);
    }
  }

  private static class EnumFeatureImpl extends AbstractFeature<TestEnum> {

    EnumFeatureImpl(String name) {
      super(name);
    }

    @Override
    public EnumValue<TestEnum> value(TestEnum object) {
      return new EnumValue<>(this, object);
    }

    @Override
    public Value<TestEnum> parse(String string) {
      throw new UnsupportedOperationException();
    }
  }

  @Test
  public void smokeTest() {
    EnumValue value = new EnumFeatureImpl("feature").value(TestEnum.B);
    assertNotNull(value);
    assertFalse(value.isUnknown());
    assertEquals(TestEnum.B, value.get());
  }

  @Test
  public void testUnknown() {
    Value value = new EnumFeatureImpl("test").unknown();
    assertTrue(value.isUnknown());
  }

  @Test
  public void equalsAndHashCode() {
    Value a = new EnumFeatureImpl("feature").value(TestEnum.A);
    Value b = new EnumFeatureImpl("feature").value(TestEnum.B);
    Value aa = new EnumFeatureImpl("feature").value(TestEnum.A);
    Value unknown = new EnumFeatureImpl("test").unknown();

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
  @Ignore
  /*
   * This test fails with "Cannot deserialize Class java.lang.Enum (of type enum) as a Bean".
   * It looks like a problem with Jackson Databind, maybe it is related to
   * https://github.com/FasterXML/jackson-databind/issues/2605
   */
  public void serializeAndDeserialize() throws IOException {
    ObjectMapper mapper = new ObjectMapper();

    EnumFeatureImpl feature = new EnumFeatureImpl("feature");
    EnumValue a = feature.value(TestEnum.A);
    byte[] bytes = mapper.writeValueAsBytes(a);
    assertNotNull(bytes);
    assertTrue(bytes.length > 0);

    Object clone = mapper.readValue(bytes, EnumValue.class);
    assertNotNull(clone);
    assertEquals(a, clone);
    assertEquals(a.hashCode(), clone.hashCode());
  }

}