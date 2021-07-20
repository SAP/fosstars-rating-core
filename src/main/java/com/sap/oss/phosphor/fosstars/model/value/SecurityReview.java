package com.sap.oss.phosphor.fosstars.model.value;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sap.oss.phosphor.fosstars.model.Subject;
import java.util.Date;
import java.util.Objects;

/**
 * A security review.
 */
public class SecurityReview {

  /**
   * When the review was done.
   */
  private final Date date;

  /**
   * Create a new review.
   *
   * @param date When the review was done.
   */
  public SecurityReview(@JsonProperty("date") Date date) {
    this.date = Objects.requireNonNull(date, "Date can't be null!");
  }

  /**
   * Returns a date when the review was done.
   *
   * @return A date of review.
   */
  @JsonGetter("date")
  public Date date() {
    return date;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o != null && !SecurityReview.class.isAssignableFrom(o.getClass())) {
      return false;
    }
    SecurityReview that = (SecurityReview) o;
    return Objects.equals(date, that.date);
  }

  @Override
  public int hashCode() {
    return Objects.hash(date);
  }
}
