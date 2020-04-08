package com.sap.sgs.phosphor.fosstars.model.value;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A set of programming languages.
 */
public class Languages {

  /**
   * A set of languages.
   */
  private final Set<Language> languages;

  /**
   * Initializes a set of languages.
   *
   * @param languages A set of languages.
   */
  @JsonCreator
  public Languages(@JsonProperty("languages") Set<Language> languages) {
    Objects.requireNonNull(languages, "Languages can't be null!");
    this.languages = EnumSet.copyOf(languages);
  }

  /**
   * Initializes a set of languages.
   *
   * @param languages A number of languages.
   */
  public Languages(Language... languages) {
    this(EnumSet.copyOf(Arrays.asList(languages)));
  }

  /**
   * Returns a number of languages in the set.
   */
  public int size() {
    return languages.size();
  }

  /**
   * Returns the languages.
   */
  @JsonGetter("languages")
  public Set<Language> languages() {
    return EnumSet.copyOf(languages);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o instanceof Languages == false) {
      return false;
    }
    Languages other = (Languages) o;
    return Objects.equals(languages, other.languages);
  }

  @Override
  public int hashCode() {
    return Objects.hash(languages);
  }

  @Override
  public String toString() {
    return languages.stream().map(Enum::toString).collect(Collectors.joining(", "));
  }
}
