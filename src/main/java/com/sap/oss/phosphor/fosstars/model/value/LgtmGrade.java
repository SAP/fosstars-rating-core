package com.sap.oss.phosphor.fosstars.model.value;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Objects;

/**
 * The enum represents LGTM grades.
 */
public enum LgtmGrade {

  A_PLUS("A+"),
  A("A"),
  B("B"),
  C("C"),
  D("D"),
  E("E");

  /**
   * LGTM grade.
   */
  private final String grade;

  /**
   * Initializes an {@link LgtmGrade}.
   *
   * @param grade The LGTM grade.
   */
  LgtmGrade(String grade) {
    this.grade = grade;
  }

  /**
   * Get the grade as a string.
   *
   * @return The LGTM grade as a string.
   */
  @JsonValue
  public String grade() {
    return grade;
  }

  /**
   * Converts a string to an LGTM grade.
   *
   * @param string The string to be parsed.
   * @return A LGTM grade.
   * @throws IllegalArgumentException If the string can't be converted to an LGTM grade.
   */
  @JsonCreator
  public static LgtmGrade parse(String string) {
    Objects.requireNonNull(string, "String can't be null!");

    String name = string.trim();
    if (name.isEmpty()) {
      throw new IllegalArgumentException("String can't be empty!");
    }

    for (LgtmGrade value : LgtmGrade.values()) {
      if (value.grade.equals(name)) {
        return value;
      }
    }

    throw new IllegalArgumentException(String.format("Unknown value: %s", string));
  }

  @Override
  public String toString() {
    return grade;
  }
}
