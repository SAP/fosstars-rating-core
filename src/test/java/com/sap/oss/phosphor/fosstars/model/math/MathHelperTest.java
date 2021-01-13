package com.sap.oss.phosphor.fosstars.model.math;

import static org.junit.Assert.assertEquals;

import java.util.function.Function;
import org.apache.commons.math3.analysis.function.Logistic;
import org.junit.Test;

public class MathHelperTest {

  @Test
  public void invertLogistic() {
    final double precision = 0.01;

    // func(x) = 1 / (1 + e ^ (1 * (0.5 - x)))
    Logistic func = new Logistic(1, 0.5, 1, 1, 0, 1);

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

  @Test
  public void invertLinear() {
    final double precision = 0.01;

    // y(x) = 2x + 10
    Function<Integer, Double> y = x -> (double) (2 * x + 10);

    assertEquals(-5, MathHelper.invert(y, -10, 50, 0, precision));
    assertEquals(10, MathHelper.invert(y, -10, 50, 30, precision));

    // g(x) = 4x + 1
    Function<Integer, Double> g = x -> (double) (4 * x + 1);

    assertEquals(0, MathHelper.invert(g, -10, 50, 1, precision));
    assertEquals(10, MathHelper.invert(g, -10, 50, 41, precision));
  }

}