package com.sap.oss.phosphor.fosstars.advice;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.net.URL;
import java.util.Objects;

/**
 * A link to additional info for an advice.
 */
public class Link {

  /**
   * A name of a link.
   */
  public final String name;

  /**
   * A URL.
   */
  public final URL url;

  /**
   * Create a new link.
   *
   * @param name A name of a link. It must not be empty.
   * @param url A URL.
   */
  @JsonCreator
  public Link(@JsonProperty("name") String name, @JsonProperty("url") URL url) {
    Objects.requireNonNull(name, "Oh no! Name is null!");
    Objects.requireNonNull(url, "Oh no! URL is null!");

    name = name.trim();
    if (name.isEmpty()) {
      throw new IllegalArgumentException("Oh no! Name seems to be empty!");
    }

    this.name = name;
    this.url = url;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (o instanceof Link) {
      Link link = (Link) o;
      return Objects.equals(name, link.name) && Objects.equals(url, link.url);
    }

    return false;
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, url);
  }

}
