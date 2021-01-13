package com.sap.oss.phosphor.fosstars.advice;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sap.oss.phosphor.fosstars.model.Feature;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A content of an advice for a feature.
 */
public class AdviceContent {

  /**
   * A feature.
   */
  private final Feature<?> feature;

  /**
   * A text of the advice.
   */
  private final String text;

  /**
   * A number of links that point to additional info.
   */
  private final List<Link> links;

  /**
   * Create a new advice content.
   *
   * @param feature A feature.
   * @param text A text of the advice. It must not be empty.
   * @param links A list of links to additional info.
   */
  @JsonCreator
  public AdviceContent(
      @JsonProperty("feature") Feature<?> feature,
      @JsonProperty("text") String text,
      @JsonProperty("links") List<Link> links) {

    Objects.requireNonNull(feature, "Oh no! Feature is null!");
    Objects.requireNonNull(text, "Oh no! Text is null!");
    Objects.requireNonNull(links, "Oh no! Links is null!");

    text = text.trim();
    if (text.isEmpty()) {
      throw new IllegalArgumentException("Oh no! Text seems to be empty!");
    }

    this.feature = feature;
    this.text = text;
    this.links = links;
  }

  /**
   * Get the feature.
   *
   * @return The feature.
   */
  @JsonGetter("feature")
  public Feature<?> feature() {
    return feature;
  }

  /**
   * Get the text of the advice.
   *
   * @return The text.
   */
  @JsonGetter("text")
  public String text() {
    return text;
  }

  /**
   * Get the links to additional info.
   *
   * @return A list of links.
   */
  @JsonGetter("links")
  public List<Link> links() {
    return new ArrayList<>(links);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (o instanceof AdviceContent) {
      AdviceContent that = (AdviceContent) o;
      return Objects.equals(feature, that.feature)
          && Objects.equals(text, that.text)
          && Objects.equals(links, that.links);
    }

    return false;
  }

  @Override
  public int hashCode() {
    return Objects.hash(feature, text, links);
  }

}
