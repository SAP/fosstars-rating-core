package com.sap.sgs.phosphor.fosstars.model.qa;

import com.sap.sgs.phosphor.fosstars.model.Score;
import java.util.Objects;

/**
 * A holder for a result of executing a test vector.
 */
public class TestVectorResult {

  public enum Status {
    PASSED, FAILED
  }

  /**
   * A test vector.
   */
  public final TestVector vector;

  /**
   * An index of the test vector.
   */
  public final int index;

  /**
   * A calculated score value.
   */
  public final double scoreValue;

  /**
   * Passed or failed.
   */
  public final Status status;

  /**
   * A message which provides more details about the result.
   */
  public final String message;

  /**
   * Initializes a new {@link TestVectorResult}.
   *
   * @param vector A test vector.
   * @param index An index of the test vector.
   * @param scoreValue A calculated score value.
   * @param status Passed or failed.
   * @param message A message which provides more details about the result.
   */
  TestVectorResult(TestVector vector, int index, double scoreValue, Status status, String message) {
    if (index < 0) {
      throw new IllegalArgumentException("Hey! Index can't be negative!");
    }
    if (message.isEmpty()) {
      throw new IllegalArgumentException("Hey! Reason can't be empty!");
    }
    this.vector = Objects.requireNonNull(vector, "Hey! Vector can't be null!");
    this.status = Objects.requireNonNull(status, "Hey! Status can't be null!");
    this.message = Objects.requireNonNull(message, "Hey! Reason can't be null!");
    this.scoreValue = Score.check(scoreValue);
    this.index = index;
  }

  /**
   * Returns true if the test vector failed, false otherwise.
   */
  public boolean failed() {
    return status != Status.PASSED;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o instanceof TestVectorResult == false) {
      return false;
    }
    TestVectorResult that = (TestVectorResult) o;
    return index == that.index
        && Objects.equals(vector, that.vector)
        && status == that.status
        && Objects.equals(message, that.message);
  }

  @Override
  public int hashCode() {
    return Objects.hash(vector, index, status, message);
  }
}
