package com.sap.sgs.phosphor.fosstars.model.math;

import java.util.function.Function;
import org.apache.commons.math3.analysis.function.Logistic;

/**
 * This is a helper class math.
 */
public class MathHelper {

  /**
   * Invert a value of a logistic function on a specified interval.
   *
   * @param func The logistic function to be inverted.
   * @param a The beginning of the interval.
   * @param b The end of the interval.
   * @param value The value to be inverted.
   * @param precision The precision for comparison of doubles.
   * @return The argument of the function, which belongs to the specified interval [a, b],
   *         which produces the closest value to the specified value.
   * @throws IllegalArgumentException If the parameters are incorrect.
   */
  public static int invert(Logistic func, int a, int b, double value, double precision) {
    if (a > b) {
      throw new IllegalArgumentException("a > b");
    }
    double funcA = func.value(a);
    if (equals(funcA, value, precision)) {
      return a;
    }
    if (a == b) {
      throw new IllegalArgumentException(String.format("a == b and func(a) != %2.2f", value));
    }
    if (Double.compare(funcA, value) > 0) {
      throw new IllegalArgumentException(String.format(
          "func(%d) is %2.2f which is greater than %2.2f", a, funcA, value));
    }
    double funcB = func.value(b);
    if (equals(funcB, value, precision)) {
      return b;
    }
    if (Double.compare(funcB, value) < 0) {
      throw new IllegalArgumentException(String.format(
          "func(%d) is %2.2f which is less than %2.2f", b, funcB, value));
    }

    return binarySearch(func::value, a, b, value, precision);
  }

  /**
   * Inverses a monotonic function on a specified interval using binary search.
   *
   * @param func The function to be inverted.
   * @param a The beginning of the interval.
   * @param b The end of the interval.
   * @param value The value of the function to be inverted.
   * @param precision The precision for comparison of doubles.
   * @return The argument of the function, which belongs to the specified interval [a, b],
   *         which produces the closest value to the specified value.
   */
  private static int binarySearch(
      Function<Integer, Double> func, int a, int b, double value, double precision) {

    while (b - a > 1) {
      int m = a + (Math.abs(b - a)) / 2;
      double funcM = func.apply(m);
      if (equals(funcM, value, precision)) {
        return m;
      }
      if (Double.compare(value, funcM) > 0) {
        a = m;
      } else {
        b = m;
      }
    }

    double funcA = func.apply(a);
    double funcB = func.apply(b);

    if (Math.abs(funcA - value) < Math.abs(funcB - value)) {
      return a;
    }

    return b;
  }

  /**
   * Checks if two doubles are equal with the specified precision.
   *
   * @param a The first double.
   * @param b The second double.
   * @param precision The precision.
   * @return True if the doubles are equal, false otherwise.
   */
  private static boolean equals(double a, double b, double precision) {
    return Math.abs(a - b) < precision;
  }
}
