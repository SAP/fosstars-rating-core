package com.sap.oss.phosphor.fosstars.model.weight;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class AbstractWeightTest {

  @Test
  public void equalBoundaries() {
    MutableWeight mutableWeight = new MutableWeight(0.3);
    ImmutableWeight immutableWeight = new ImmutableWeight(0.7);
    assertEquals(mutableWeight.boundaries(), immutableWeight.boundaries());
  }
}