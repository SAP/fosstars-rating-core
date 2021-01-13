package com.sap.oss.phosphor.fosstars.model.weight;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import org.junit.Test;

public class ImmutableWeightTest {

  @Test
  public void good() {
    assertEquals(0.45, new ImmutableWeight(0.45).value(), 0.0);
    assertEquals(1, new ImmutableWeight(1).value(), 0.0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void zero() {
    new ImmutableWeight(0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void negative() {
    new ImmutableWeight(-1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void tooBig() {
    new ImmutableWeight(2);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void update() {
    ImmutableWeight weight = new ImmutableWeight(0.5);
    assertTrue(weight.isImmutable());
    assertEquals(0.5, weight.value(), 0.001);
    weight.value(0.9);
  }

  @Test
  public void serializeAndDeserialize() throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    ImmutableWeight weight = new ImmutableWeight(0.5);
    byte[] bytes = mapper.writeValueAsBytes(weight);
    ImmutableWeight clone = mapper.readValue(bytes, ImmutableWeight.class);
    assertEquals(weight, clone);
  }

  @Test
  public void boundaries() {
    MutableWeight weight = new MutableWeight(0.5);
    assertTrue(weight.boundaries().contains(weight.value()));
  }
}