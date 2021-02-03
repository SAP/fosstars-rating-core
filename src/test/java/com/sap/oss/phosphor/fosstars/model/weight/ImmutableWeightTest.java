package com.sap.oss.phosphor.fosstars.model.weight;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.sap.oss.phosphor.fosstars.util.Json;
import java.io.IOException;
import org.junit.Test;

public class ImmutableWeightTest {

  @Test
  public void testGood() {
    assertEquals(0.45, new ImmutableWeight(0.45).value(), 0.0);
    assertEquals(1, new ImmutableWeight(1).value(), 0.0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testZero() {
    new ImmutableWeight(0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNegative() {
    new ImmutableWeight(-1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testTooBig() {
    new ImmutableWeight(2);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testUpdate() {
    ImmutableWeight weight = new ImmutableWeight(0.5);
    assertTrue(weight.isImmutable());
    assertEquals(0.5, weight.value(), 0.001);
    weight.value(0.9);
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