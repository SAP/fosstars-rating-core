package com.sap.sgs.phosphor.fosstars.model;

import com.sap.sgs.phosphor.fosstars.model.value.RatingValue;
import java.util.Date;
import java.util.Optional;

/**
 * A subject for which a rating or a score may be calculated.
 */
public interface Subject {

  /**
   * Shows that there is no rating value assigned to a subject.
   */
  RatingValue NO_RATING_VALUE = null;

  /**
   * Shows that the date when a rating value was assigned is unknown.
   */
  Date NO_RATING_DATE = null;

  /**
   * Returns a date when the rating value was calculated if it's set.
   *
   * @return An {@link Optional} with the date.
   */
  Optional<Date> ratingValueDate();

  /**
   * Returns a rating value if it's set.
   *
   * @return An {@link Optional} with the rating value.
   */
  Optional<RatingValue> ratingValue();

  /**
   * Set a rating value.
   *
   * @param value The rating value.
   */
  void set(RatingValue value);
}
