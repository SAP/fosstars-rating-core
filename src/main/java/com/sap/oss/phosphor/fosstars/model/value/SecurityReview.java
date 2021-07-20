package com.sap.oss.phosphor.fosstars.model.value;

import static java.lang.String.format;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sap.oss.phosphor.fosstars.model.Interval;
import com.sap.oss.phosphor.fosstars.model.math.DoubleInterval;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import javax.annotation.Nullable;

/**
 * A security review.
 */
public class SecurityReview {

  /**
   * No info about changes since a review.
   */
  public static final Double NO_INFO_ABOUT_CHANGES = null;

  /**
   * A valid interval for amount of changes returned by {@link #changes()}.
   */
  private static final Interval CHANGES_INTERVAL = DoubleInterval.closed(0.0, 1.0);

  /**
   * A parser for dates.
   */
  static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

  /**
   * When the review was done.
   */
  private final Date date;

  /**
   * An amount of changes in the project since the review date.
   */
  private final Double changes;

  /**
   * Create a new review.
   *
   * @param date When the review was done.
   * @param changes An amount of changes in the project since the review date.
   */
  public SecurityReview(
      @JsonProperty("date") Date date,
      @Nullable @JsonProperty("changes") Double changes) {

    if (changes != null && !CHANGES_INTERVAL.contains(changes)) {
      throw new IllegalArgumentException(format("Oops! Wrong value for changes %s!", changes));
    }

    this.date = Objects.requireNonNull(date, "Date can't be null!");
    this.changes = changes;
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

  /**
   * Get an amount of changes in the project since the review date.
   *
   * @return An amount of changes in the project since the review date if available.
   */
  public Optional<Double> projectChanged() {
    return Optional.ofNullable(changes);
  }

  /**
   * An amount of changes in the project since the review date.
   * This method is necessary for serialization with Jackson.
   *
   * @return An amount of changes in the project since the review date.
   */
  @JsonGetter
  private Double changes() {
    return changes;
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
    return Objects.equals(date, that.date)
        && Objects.equals(changes, that.changes);
  }

  @Override
  public int hashCode() {
    return Objects.hash(date, changes);
  }

  @Override
  public String toString() {
    return format("Security review at %s, %s of the project files has changed since than",
        DATE_FORMAT.format(date),
        projectChanged().map(n -> format("%d%%", (int) (n * 100.0))).orElse("unknown part"));
  }
}
