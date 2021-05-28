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
   * What was reviewed.
   */
  private final Subject subject;

  /**
   * When the review was done.
   */
  private final Date date;

  /**
   * Create a new review.
   *
   * @param subject What was reviewed.
   * @param date Whe the review wes done.
   */
  public SecurityReview(
      @JsonProperty("subject") Subject subject,
      @JsonProperty("date") Date date) {

    this.subject = Objects.requireNonNull(subject, "Subject can't be null!");
    this.date = Objects.requireNonNull(date, "Date can't be null!");
  }

  /**
   * Returns what was reviewed.
   *
   * @return What was reviewed.
   */
  @JsonGetter("subject")
  public Subject subject() {
    return subject;
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
    return Objects.equals(subject, that.subject) && Objects.equals(date, that.date);
  }

  @Override
  public int hashCode() {
    return Objects.hash(subject, date);
  }
}
