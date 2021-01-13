package com.sap.oss.phosphor.fosstars.model.value;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.net.URL;
import java.util.Objects;

/**
 * The class holds a link to additional information about vulnerability.
 */
public class Reference {

  /**
   * A URL to a document which contains information about a vulnerability.
   */
  private final URL url;

  /**
   * A description for the reference.
   */
  private final String description;

  /**
   * Initializes a reference.
   *
   * @param description A description for the reference.
   * @param url A URL to a document which contains information about a vulnerability.
   */
  public Reference(
      @JsonProperty("description") String description, @JsonProperty("url") URL url) {
    Objects.requireNonNull(url, "Hey! URL must not be null!");
    this.description = description;
    this.url = url;
  }

  /**
   * Get a description of the reference.
   *
   * @return The description for the reference.
   */
  @JsonGetter("description")
  public String description() {
    return description;
  }

  /**
   * Get a URL to the additional info.
   *
   * @return The URL to a document which contains information about a vulnerability.
   */
  @JsonGetter("url")
  public URL url() {
    return url;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o instanceof Reference == false) {
      return false;
    }
    Reference reference = (Reference) o;
    return Objects.equals(description, reference.description)
        && Objects.equals(url, reference.url);
  }

  @Override
  public int hashCode() {
    return Objects.hash(description, url);
  }
}
