package com.sap.sgs.phosphor.fosstars.model.value;

import static com.sap.sgs.phosphor.fosstars.model.other.Utils.setOf;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A set of programming languages.
 */
public class Languages implements Iterable<Language> {

  /**
   * A set of languages.
   */
  private final Set<Language> elements;

  /**
   * Creates a collection of languages.
   *
   * @param languages The languages.
   * @return A collection of the specified languages.
   */
  public static Languages of(Language... languages) {
    return new Languages(setOf(languages));
  }

  /**
   * Returns an empty set of languages.
   */
  public static Languages empty() {
    return new Languages();
  }

  /**
   * Initializes a set of languages.
   *
   * @param languages A set of languages.
   */
  @JsonCreator
  public Languages(@JsonProperty("elements") Set<Language> languages) {
    Objects.requireNonNull(languages, "Languages can't be null!");
    this.elements = new HashSet<>(languages);
  }

  /**
   * Initializes a set of languages.
   *
   * @param languages A number of languages.
   */
  public Languages(Language... languages) {
    this(setOf(languages));
  }

  /**
   * Returns a number of languages in the set.
   */
  public int size() {
    return elements.size();
  }

  /**
   * Checks if the collection contains one of the other languages.
   *
   * @param languages The other languages.
   * @return True if at least one of the other languages is present in the collection,
   *         false otherwise.
   */
  public boolean containsAnyOf(Languages languages) {
    for (Language language : languages) {
      if (this.elements.contains(language)) {
        return true;
      }
    }

    return false;
  }

  /**
   * Checks if the collection contains one of the other languages.
   *
   * @param languages The other languages.
   * @return True if at least one of the other languages is present in the collection,
   *         false otherwise.
   */
  public boolean containsAnyOf(Language... languages) {
    return containsAnyOf(Languages.of(languages));
  }

  /**
   * Returns the languages.
   */
  @JsonGetter("elements")
  public Set<Language> get() {
    return new HashSet<>(elements);
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
    return Objects.equals(elements, other.elements);
  }

  @Override
  public int hashCode() {
    return Objects.hash(elements);
  }

  @Override
  public String toString() {
    return elements.stream().map(Enum::toString).collect(Collectors.joining(", "));
  }

  @Override
  public Iterator<Language> iterator() {
    return elements.iterator();
  }
}
