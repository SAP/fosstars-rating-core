package com.sap.sgs.phosphor.fosstars.model.value;

import static com.sap.sgs.phosphor.fosstars.model.other.Utils.setOf;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;

/**
 * A collection of security reviews.
 */
public final class SecurityReviews {

  /**
   * An empty set of security reviews.
   */
  public static final SecurityReviews NO_REVIEWS = new SecurityReviews();

  /**
   * A set of security reviews.
   */
  private final Set<SecurityReview> reviews;

  /**
   * Initializes a new collection of security reviews.
   *
   * @param reviews An array of security reviews.
   */
  public SecurityReviews(SecurityReview... reviews) {
    this(setOf("You gave me a duplicate security review!", reviews));
  }

  /**
   * Initializes a {@link SecurityReviews} with a number of security reviews.
   *
   * @param reviews A set of security reviews.
   */
  public SecurityReviews(@JsonProperty("reviews") Set<SecurityReview> reviews) {
    Objects.requireNonNull(reviews, "Hey! Reviews can't be null!");
    this.reviews = Collections.unmodifiableSet(reviews);
  }

  /**
   * Returns a set of security reviews.
   */

  @JsonGetter("reviews")
  public Set<SecurityReview> get() {
    return reviews;
  }

  /**
   * Returns true if at least one security review has been done, false otherwise.
   */
  @JsonIgnore
  public boolean done() {
    return !reviews.isEmpty();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o instanceof SecurityReviews == false) {
      return false;
    }
    SecurityReviews that = (SecurityReviews) o;
    return Objects.equals(reviews, that.reviews);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(reviews);
  }

  @Override
  public String toString() {
    int n = reviews.size();
    return String.format("%d security %s", n, n == 1 ? "review" : "reviews");
  }
}
