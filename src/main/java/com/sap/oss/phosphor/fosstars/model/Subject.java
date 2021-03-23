package com.sap.oss.phosphor.fosstars.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.sap.oss.phosphor.fosstars.model.subject.oss.GitHubProject;
import com.sap.oss.phosphor.fosstars.model.value.RatingValue;
import java.util.Date;
import java.util.Optional;

/**
 * A subject for which a rating or a score may be calculated.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = GitHubProject.class)
})
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
