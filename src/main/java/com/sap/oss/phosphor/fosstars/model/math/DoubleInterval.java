package com.sap.oss.phosphor.fosstars.model.math;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sap.oss.phosphor.fosstars.model.Interval;
import java.util.Objects;

/**
 * An interval with boundaries represented by double numbers or infinities.
 */
public class DoubleInterval implements Interval {

  static final double PRECISION = 0.0001;

  private final double from;
  private final boolean openLeft;
  private final boolean negativeInfinity;

  private final double to;
  private final boolean openRight;
  private final boolean positiveInfinity;

  /**
   * Initializes an interval.
   *
   * @param from A left boundary.
   * @param openLeft If a left boundary is open.
   * @param negativeInfinity If a left boundary is infinity.
   * @param to A right boundary.
   * @param openRight If a right boundary is open.
   * @param positiveInfinity If a right boundary is infinity.
   */
  private DoubleInterval(
      @JsonProperty("from") double from,
      @JsonProperty("openLeft") boolean openLeft,
      @JsonProperty("negativeInfinity") boolean negativeInfinity,
      @JsonProperty("to") double to,
      @JsonProperty("openRight") boolean openRight,
      @JsonProperty("positiveInfinity") boolean positiveInfinity) {

    this.from = from;
    this.openLeft = openLeft;
    this.negativeInfinity = negativeInfinity;
    this.to = to;
    this.openRight = openRight;
    this.positiveInfinity = positiveInfinity;
  }

  /**
   * Get a left boundary of the interval.
   *
   * @return The left boundary.
   */
  @JsonGetter("from")
  private double from() {
    return from;
  }

  /**
   * Tells if the interval includes its left boundary.
   *
   * @return True if the left boundary is not included to the interval, false otherwise.
   */
  @JsonGetter("openLeft")
  private boolean openLeft() {
    return openLeft;
  }

  /**
   * Tells if the left boundary is the negative infinity.
   *
   * @return True if the left boundary is the negative infinity, false otherwise.
   */
  @JsonGetter("negativeInfinity")
  private boolean negativeInfinity() {
    return negativeInfinity;
  }

  /**
   * Gets the right boundary of the interval.
   *
   * @return The right boundary.
   */
  @JsonGetter("to")
  private double to() {
    return to;
  }

  /**
   * Tells if the right boundary is included to the interval.
   *
   * @return True if the right boundary is not included to the interval, false otherwise.
   */
  @JsonGetter("openRight")
  private boolean openRight() {
    return openRight;
  }

  /**
   * Tells if the right boundary is the positive infinity.
   *
   * @return True if the right boundary is the negative infinity, false otherwise.
   */
  @JsonGetter("positiveInfinity")
  private boolean positiveInfinity() {
    return positiveInfinity;
  }

  @Override
  public boolean contains(double x) {
    if (!negativeInfinity) {
      if (Double.compare(x, from) == 0) {
        if (openLeft) {
          return false;
        }
      } else if (Double.compare(x, from) < 0) {
        return false;
      }
    }

    if (!positiveInfinity) {
      if (equals(x, to)) {
        if (openRight) {
          return false;
        }
      } else if (Double.compare(x, to) > 0) {
        return false;
      }
    }

    return true;
  }

  /**
   * Checks if the interval contains a number.
   *
   * @param x The number to be checked.
   * @return True if the interval contains the number, false otherwise.
   */
  @Override
  public boolean contains(int x) {
    return contains((double) x);
  }

  @Override
  public double mean() {
    if (negativeInfinity || positiveInfinity) {
      throw new IllegalArgumentException(
          "You are asking about a mean for an infinite interval!");
    }

    return from + (to - from) / 2;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    if (negativeInfinity) {
      builder.append("(inf");
    } else if (openLeft) {
      builder.append("(").append(from);
    } else {
      builder.append("[").append(from);
    }
    builder.append(", ");
    if (positiveInfinity) {
      builder.append("inf)");
    } else if (openRight) {
      builder.append(to).append(")");
    } else {
      builder.append(to).append("]");
    }
    return builder.toString();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o instanceof DoubleInterval == false) {
      return false;
    }
    DoubleInterval that = (DoubleInterval) o;
    return Double.compare(that.from, from) == 0
        && openLeft == that.openLeft
        && negativeInfinity == that.negativeInfinity
        && Double.compare(that.to, to) == 0
        && openRight == that.openRight
        && positiveInfinity == that.positiveInfinity;
  }

  /**
   * Check if two double numbers are equal.
   *
   * @param a The first number.
   * @param b The seconds number.
   * @return True if the numbers are equal, false otherwise.
   */
  private static boolean equals(double a, double b) {
    return Math.abs(a - b) < PRECISION;
  }

  @Override
  public int hashCode() {
    return Objects.hash(from, openLeft, negativeInfinity, to, openRight, positiveInfinity);
  }

  /**
   * Initializes a builder to build an interval.
   *
   * @return A new builder.
   */
  public static DoubleIntervalBuilder init() {
    return new DoubleIntervalBuilder();
  }

  /**
   * Creates a closed interval.
   *
   * @param from A left boundary.
   * @param to A right boundary.
   * @return A new interval.
   */
  public static DoubleInterval closed(double from, double to) {
    return new DoubleInterval(from, false, false, to, false, false);
  }

  /**
   * A builder for an interval.
   */
  public static class DoubleIntervalBuilder {

    private double from = 0.0;
    private boolean openLeft = false;
    private boolean negativeInfinity = false;

    private double to = 1.0;
    private boolean openRight = false;
    private boolean positiveInfinity = false;

    /**
     * Let's restrict access to the default constructor,
     * so that the builder can be only initialized via DoubleInterval.init() method
     * this may prevent of possible problems
     * if the init() method is updated with some more logic in the future.
     */
    DoubleIntervalBuilder() {

    }

    /**
     * Set the left boundary.
     *
     * @param a The left boundary.
     * @return The same builder.
     */
    public DoubleIntervalBuilder from(double a) {
      from = a;
      return this;
    }

    /**
     * Set the right boundary.
     *
     * @param b The right boundary.
     * @return The same builder.
     */
    public DoubleIntervalBuilder to(double b) {
      to = b;
      return this;
    }

    /**
     * Make the left boundary open (not included to the interval).
     *
     * @return The same builder.
     */
    public DoubleIntervalBuilder openLeft() {
      openLeft = true;
      return this;
    }

    /**
     * Make the right boundary open (not included to the interval).
     *
     * @return The same builder.
     */
    public DoubleIntervalBuilder openRight() {
      openRight = true;
      return this;
    }

    /**
     * Make the left boundary closed (included to the interval).
     *
     * @return The same builder.
     */
    public DoubleIntervalBuilder closedLeft() {
      openLeft = false;
      return this;
    }

    /**
     * Make the right boundary closed (included to the interval).
     *
     * @return The same builder.
     */
    public DoubleIntervalBuilder closedRight() {
      openRight = false;
      return this;
    }

    /**
     * Make both boundaries open (not included to the interval).
     *
     * @return The same builder.
     */
    public DoubleIntervalBuilder open() {
      return openLeft().openRight();
    }

    /**
     * Make both boundaries closed (included to the interval).
     *
     * @return The same builder.
     */
    public DoubleIntervalBuilder closed() {
      return closedLeft().closedRight();
    }

    /**
     * Set the left boundary to a negative infinity.
     *
     * @return The same builder.
     */
    public DoubleIntervalBuilder negativeInfinity() {
      negativeInfinity = true;
      openLeft = true;
      return this;
    }

    /**
     * Set the right boundary to a positive infinity.
     *
     * @return The same builder.
     */
    public DoubleIntervalBuilder positiveInfinity() {
      positiveInfinity = true;
      openRight = true;
      return this;
    }

    /**
     * Creates an instance of DoubleInterval with specified parameters.
     *
     * @return An interval.
     */
    public DoubleInterval make() {
      return new DoubleInterval(from, openLeft, negativeInfinity, to, openRight, positiveInfinity);
    }

  }
}
