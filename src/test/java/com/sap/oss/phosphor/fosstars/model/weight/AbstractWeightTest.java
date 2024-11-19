package com.sap.oss.phosphor.fosstars.model.weight;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class AbstractWeightTest {

  @Test
  public void equalBoundaries() {
    MutableWeight mutableWeight = new MutableWeight(0.3);
    ImmutableWeight immutableWeight = new ImmutableWeight(0.7);
    assertEquals(mutableWeight.boundaries(), immutableWeight.boundaries());
  }
}
