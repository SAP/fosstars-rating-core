package com.sap.sgs.phosphor.fosstars.model.math;

import static org.junit.Assert.assertEquals;

import org.apache.commons.math3.analysis.function.Logistic;
import org.junit.Test;

public class MathHelperTest {

  @Test
  public void reverse() {
    // y(x) = 1 / (1 + e ^ (1 * (0.5 - x)))
    Logistic func = new Logistic(1, 0.5, 1, 1, 0, 1);
    final double precision = 0.01;

    assertEquals(0, MathHelper.invert(func, -100, 100, 0.37, precision));
    assertEquals(0, MathHelper.invert(func, -100, 0, 0.37, precision));
    assertEquals(0, MathHelper.invert(func, 0, 100, 0.37, precision));
    assertEquals(0, MathHelper.invert(func, -100, 1000, 0.37, precision));
    assertEquals(0, MathHelper.invert(func, -1000, 100, 0.37, precision));

    assertEquals(0, MathHelper.invert(func, -100, 100, 0.4, precision));
    assertEquals(0, MathHelper.invert(func, 0, 100, 0.4, precision));
    assertEquals(0, MathHelper.invert(func, -100, 1000, 0.4, precision));
    assertEquals(0, MathHelper.invert(func, -1000, 100, 0.4, precision));

    assertEquals(3, MathHelper.invert(func, -30, 200, 0.92, precision));
  }

}