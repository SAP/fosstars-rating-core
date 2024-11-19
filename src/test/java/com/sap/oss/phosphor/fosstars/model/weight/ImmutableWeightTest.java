package com.sap.oss.phosphor.fosstars.model.weight;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.sap.oss.phosphor.fosstars.util.Json;
import java.io.IOException;
import org.junit.jupiter.api.Test;

public class ImmutableWeightTest {

  @Test
  public void testGood() {
    assertEquals(0.45, new ImmutableWeight(0.45).value(), 0.0);
    assertEquals(1, new ImmutableWeight(1).value(), 0.0);
  }

  @Test
  public void testZero() {
    assertThrows(
        IllegalArgumentException.class,
        () -> {
          new ImmutableWeight(0);
        });
  }

  @Test
  public void testNegative() {
    assertThrows(
        IllegalArgumentException.class,
        () -> {
          new ImmutableWeight(-1);
        });
  }

  @Test
  public void testTooBig() {
    assertThrows(
        IllegalArgumentException.class,
        () -> {
          new ImmutableWeight(2);
        });
  }

  @Test
  public void testUpdate() {
    assertThrows(
        UnsupportedOperationException.class,
        () -> {
          ImmutableWeight weight = new ImmutableWeight(0.5);
          assertTrue(weight.isImmutable());
          assertEquals(0.5, weight.value(), 0.001);
          weight.value(0.9);
        });
  }

  @Test
  public void testSerializeAndDeserialize() throws IOException {
    ImmutableWeight weight = new ImmutableWeight(0.5);
    ImmutableWeight clone = Json.read(Json.toBytes(weight), ImmutableWeight.class);
    assertEquals(weight, clone);
  }

  @Test
  public void testBoundaries() {
    MutableWeight weight = new MutableWeight(0.5);
    assertTrue(weight.boundaries().contains(weight.value()));
  }
}
