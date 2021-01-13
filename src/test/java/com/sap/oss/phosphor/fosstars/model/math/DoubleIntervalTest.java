package com.sap.oss.phosphor.fosstars.model.math;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertNotEquals;

import com.sap.oss.phosphor.fosstars.model.Interval;
import org.junit.Test;

public class DoubleIntervalTest {

  @Test
  public void open() {
    DoubleInterval interval = DoubleInterval.init().from(0).to(1).open().make();
    assertTrue(interval.contains(0.5));
    assertFalse(interval.contains(-1));
    assertFalse(interval.contains(0));
    assertFalse(interval.contains(1));
    assertFalse(interval.contains(2));
    assertEquals("(0.0, 1.0)", interval.toString());
  }

  @Test
  public void closed() {
    DoubleInterval interval = DoubleInterval.init().from(0).to(1).closed().make();
    assertTrue(interval.contains(0.5));
    assertFalse(interval.contains(-1));
    assertTrue(interval.contains(0));
    assertTrue(interval.contains(1));
    assertFalse(interval.contains(2));
    assertEquals("[0.0, 1.0]", interval.toString());
  }

  @Test
  public void infinity() {
    Interval interval = DoubleInterval.init().negativeInfinity().positiveInfinity().make();
    assertTrue(interval.contains(0));
    assertTrue(interval.contains(Double.MAX_VALUE));
    assertTrue(interval.contains(Double.MIN_VALUE));
    assertEquals("(inf, inf)", interval.toString());
  }

  @Test
  public void equalsAndHashCode() {
    Interval one = DoubleInterval.init().from(0).to(1).open().make();
    Interval two = DoubleInterval.init().from(0).to(1).open().make();
    assertEquals(one, one);
    assertEquals(one.hashCode(), one.hashCode());
    assertEquals(one, two);
    assertEquals(one.hashCode(), two.hashCode());

    Interval three = DoubleInterval.init().from(0).to(2).open().make();
    assertNotEquals(one, three);
    assertNotEquals(one.hashCode(), three.hashCode());

    Interval inf = DoubleInterval.init().negativeInfinity().positiveInfinity().make();
    assertNotEquals(inf, three);
    assertNotEquals(inf.hashCode(), three.hashCode());

    Interval four = DoubleInterval.init().from(0).to(2).closed().make();
    assertNotEquals(four, three);
    assertNotEquals(four.hashCode(), three.hashCode());
  }

  @Test
  public void mean() {
    Interval one = DoubleInterval.init().from(0).to(1).open().make();
    assertEquals(0.5, one.mean(), DoubleInterval.PRECISION);
    Interval two = DoubleInterval.init().from(5).to(8).closed().make();
    assertEquals(6.5, two.mean(), DoubleInterval.PRECISION);
  }

  @Test(expected = IllegalArgumentException.class)
  public void meanForPositiveInfinity() {
    DoubleInterval.init().from(1).positiveInfinity().make().mean();
  }

  @Test(expected = IllegalArgumentException.class)
  public void meanForNegativeInfinity() {
    DoubleInterval.init().to(10).negativeInfinity().make().mean();
  }

}
