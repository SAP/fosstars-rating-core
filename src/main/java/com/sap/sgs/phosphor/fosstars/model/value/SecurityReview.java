package com.sap.sgs.phosphor.fosstars.model.value;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.net.URL;
import java.util.Date;
import java.util.Objects;

/**
 * Holds an information about a security review.
 */
public final class SecurityReview {

  /**
   * A URL to a document which describes the security review.
   */
  public final URL link;

  /**
   * When the review was done.
   */
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  public final Date when;

  /**
   * Who did the review.
   */
  public final String who;

  public SecurityReview(
      @JsonProperty("link") URL link,
      @JsonProperty("when") Date when,
      @JsonProperty("who") String who) {

    this.link = link;
    this.when = when;
    this.who = who;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null) {
      return false;
    }
    if (o.getClass() != SecurityReview.class) {
      return false;
    }
    SecurityReview that = (SecurityReview) o;
    return Objects.equals(link, that.link) &&
        Objects.equals(when, that.when) &&
        Objects.equals(who, that.who);
  }

  @Override
  public int hashCode() {
    return Objects.hash(link, when, who);
  }
}
