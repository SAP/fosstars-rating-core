package com.sap.oss.phosphor.fosstars.model.value;

import static com.sap.oss.phosphor.fosstars.model.other.Utils.setOf;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * A set versions for an artifact.
 */
public class ArtifactVersions implements Iterable<ArtifactVersion> {

  /**
   * A set of versions.
   */
  private final Set<ArtifactVersion> elements;

  /**
   * Creates a collection of versions.
   *
   * @param versions The versions.
   * @return A collection of the specified versions.
   */
  public static ArtifactVersions of(ArtifactVersion... versions) {
    return new ArtifactVersions(setOf(versions));
  }

  /**
   * Creates an empty set of versions.
   *
   * @return An empty set of versions.
   */
  public static ArtifactVersions empty() {
    return new ArtifactVersions();
  }

  /**
   * Initializes a set of versions.
   *
   * @param versions A set of versions.
   */
  @JsonCreator
  public ArtifactVersions(@JsonProperty("elements") Set<ArtifactVersion> versions) {
    Objects.requireNonNull(versions, "versions can't be null!");
    this.elements = new TreeSet<>(versions);
  }

  /**
   * Initializes a set of versions.
   *
   * @param versions A number of versions.
   */
  public ArtifactVersions(ArtifactVersion... versions) {
    this(setOf(versions));
  }

  /**
   * Get a size of the set.
   *
   * @return A number of versions in the set.
   */
  public int size() {
    return elements.size();
  }

  /**
   * Checks if the collection contains one of the other versions.
   *
   * @param versions The other versions.
   * @return True if at least one of the other versions is present in the collection,
   *         false otherwise.
   */
  public boolean containsAnyOf(ArtifactVersions versions) {
    for (ArtifactVersion version : versions) {
      if (this.elements.contains(version)) {
        return true;
      }
    }

    return false;
  }

  /**
   * Checks if the collection contains one of the other versions.
   *
   * @param versions The other versions.
   * @return True if at least one of the other versions is present in the collection,
   *         false otherwise.
   */
  public boolean containsAnyOf(ArtifactVersion... versions) {
    return containsAnyOf(ArtifactVersions.of(versions));
  }

  /**
   * Get a set of versions.
   *
   * @return The versions.
   */
  @JsonGetter("elements")
  public Set<ArtifactVersion> get() {
    return new TreeSet<>(elements);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o instanceof ArtifactVersions == false) {
      return false;
    }
    ArtifactVersions other = (ArtifactVersions) o;
    return Objects.equals(elements, other.elements);
  }

  @Override
  public int hashCode() {
    return Objects.hash(elements);
  }

  @Override
  public String toString() {
    return elements.stream().map(ArtifactVersion::toString).collect(Collectors.joining(", "));
  }

  @Override
  public Iterator<ArtifactVersion> iterator() {
    return elements.iterator();
  }
}
