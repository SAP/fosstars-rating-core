package com.sap.oss.phosphor.fosstars.model;

import static java.lang.String.format;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.sap.oss.phosphor.fosstars.model.value.RatingValue;
import com.sap.oss.phosphor.fosstars.util.Json;
import java.io.IOException;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;

/**
 * A subject for which a rating or a score may be calculated.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
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

  /**
   * Returns a PURL.
   *
   * @return A PURL.
   */
  String purl();

  /**
   * Casts a subject to a specified type that implements {@link Subject}.
   *
   * @param subject The subject.
   * @param clazz The class.
   * @param <T> The type.
   * @return The subject.
   * @throws IllegalArgumentException If the subject can't be casted to the specified type.
   */
  static <T extends Subject> T cast(Subject subject, Class<T> clazz) {
    Objects.requireNonNull(subject, "Oh no! Subject is null!");
    Objects.requireNonNull(clazz, "Oh no! Class is null!");

    if (!clazz.isAssignableFrom(subject.getClass())) {
      throw new IllegalArgumentException(format("Oh no! Expected %s", clazz.getName()));
    }

    return clazz.cast(subject);
  }

  /**
   * Clones a subject to avoid JSON mapping infinite recursion loop.
   * The subject is cloned to a different entity.
   *
   * @param subject The subject.
   * @param clazz The class.
   * @param <T> The type.
   * @return The subject.
   * @throws IOException If subject is not cloneable.
   */
  static <T extends Subject> T copy(Subject subject, Class<T> clazz) throws IOException {
    return Json.read(Json.toBytes(subject), clazz);
  }
}
