package com.sap.oss.phosphor.fosstars.model.math;

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
    return invert(func::value, a, b, value, precision);
  }

  /**
   * Invert a value of a monotonic function on a specified interval.
   * Note that the method expects a caller to pass a valid monotonic function.
   * The method doesn't check whether the specified function is monotonic or not.
   * If the function is not monotonic, the method may not produce a correct result.
   *
   * @param func The function to be inverted.
   * @param a The beginning of the interval.
   * @param b The end of the interval.
   * @param value The value to be inverted.
   * @param precision The precision for comparison of doubles.
   * @return The argument of the function, which belongs to the specified interval [a, b],
   *         which produces the closest value to the specified value.
   * @throws IllegalArgumentException If the parameters are incorrect.
   */
  public static int invert(
      Function<Integer, Double> func, int a, int b, double value, double precision) {

    if (a > b) {
      throw new IllegalArgumentException("a > b");
    }
    double funcOfA = func.apply(a);
    if (equals(funcOfA, value, precision)) {
      return a;
    }
    if (a == b) {
      throw new IllegalArgumentException(String.format("a == b and func(a) != %2.2f", value));
    }
    if (Double.compare(funcOfA, value) > 0) {
      throw new IllegalArgumentException(String.format(
          "func(%d) is %2.2f which is greater than %2.2f", a, funcOfA, value));
    }
    double funcOfB = func.apply(b);
    if (equals(funcOfB, value, precision)) {
      return b;
    }
    if (Double.compare(funcOfB, value) < 0) {
      throw new IllegalArgumentException(String.format(
          "func(%d) is %2.2f which is less than %2.2f", b, funcOfB, value));
    }

    return binarySearch(func, a, b, value, precision);
  }

  /**
   * Inverses a monotonic function on a specified interval using binary search.
   * Note that the method expects a caller to pass a valid monotonic function.
   * The method doesn't check whether the specified function is monotonic or not.
   * If the function is not monotonic, the method may not produce a correct result.
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
      double funcOfM = func.apply(m);
      if (equals(funcOfM, value, precision)) {
        return m;
      }
      if (Double.compare(value, funcOfM) > 0) {
        a = m;
      } else {
        b = m;
      }
    }

    double funcOfA = func.apply(a);
    double funcOfB = func.apply(b);

    if (Math.abs(funcOfA - value) < Math.abs(funcOfB - value)) {
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
