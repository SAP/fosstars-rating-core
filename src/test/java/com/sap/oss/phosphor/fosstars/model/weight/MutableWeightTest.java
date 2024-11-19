package com.sap.oss.phosphor.fosstars.model.weight;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.sap.oss.phosphor.fosstars.util.Json;
import java.io.IOException;
import org.junit.jupiter.api.Test;

public class MutableWeightTest {

  @Test
  public void testGood() {
    assertEquals(0.45, new MutableWeight(0.45).value(), 0.0);
    assertEquals(1, new MutableWeight(1).value(), 0.0);
  }

  @Test
  public void testZero() {
    assertThrows(
        IllegalArgumentException.class,
        () -> {
          new MutableWeight(0);
        });
  }

  @Test
  public void testNegative() {
    assertThrows(
        IllegalArgumentException.class,
        () -> {
          new MutableWeight(-1);
        });
  }

  @Test
  public void testTooBig() {
    assertThrows(
        IllegalArgumentException.class,
        () -> {
          new MutableWeight(2);
        });
  }

  @Test
  public void testUpdate() {
    MutableWeight weight = new MutableWeight(0.5);
    assertFalse(weight.isImmutable());
    assertEquals(0.5, weight.value(), 0.001);
    MutableWeight link = weight.value(0.9);
    assertEquals(0.9, weight.value(), 0.001);
    assertSame(weight, link);
  }

  @Test
  public void testSerializeAndDeserialize() throws IOException {
    MutableWeight weight = new MutableWeight(0.5);
    MutableWeight clone = Json.read(Json.toBytes(weight), MutableWeight.class);
    assertEquals(weight, clone);
  }

  @Test
  public void testBoundaries() {
    MutableWeight weight = new MutableWeight(0.5);
    assertTrue(weight.boundaries().contains(weight.value()));
  }
}
