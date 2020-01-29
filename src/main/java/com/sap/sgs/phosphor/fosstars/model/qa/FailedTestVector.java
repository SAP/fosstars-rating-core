package com.sap.sgs.phosphor.fosstars.model.qa;

import java.util.Objects;

/**
 * A holder for a failed test vector with a reason why it failed.
 */
public class FailedTestVector {

  /**
   * A failed test vector.
   */
  public final TestVector vector;

  /**
   * An index of the failed test vector.
   */
  public final int index;

  /**
   * A message which explains why the test vector failed.
   */
  public final String reason;

  FailedTestVector(TestVector vector, int index, String reason) {
    Objects.requireNonNull(vector, "Hey! Vector can't be null!");
    Objects.requireNonNull(reason, "Hey! Reason can't be null!");

    if (index < 0) {
      throw new IllegalArgumentException("Hey! Index can't be negative!");
    }

    if (reason.isEmpty()) {
      throw new IllegalArgumentException("Hey! Reason can't be empty!");
    }

    this.vector = vector;
    this.index = index;
    this.reason = reason;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o instanceof FailedTestVector == false) {
      return false;
    }
    FailedTestVector other = (FailedTestVector) o;
    return Objects.equals(vector, other.vector);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(vector);
  }
}
