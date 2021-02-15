package com.sap.oss.phosphor.fosstars.model.value;

import static com.sap.oss.phosphor.fosstars.model.other.Utils.setOf;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * A set versions for an artifact.
 */
public class ArtifactVersions implements Iterable<ArtifactVersion> {

  /**
   * Comparator for artifact versions release date.
   */
  private static final Comparator<ArtifactVersion> RELEASE_DATE_COMPARISON =
      (a, b) -> b.getReleaseDate().compareTo(a.getReleaseDate());

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
    this.elements = new HashSet<>(versions);
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
   *          false otherwise.
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
   * Get versions sorted by date.
   * First entry is the latest release.
   *
   * @return versions sorted by date
   */
  public Collection<ArtifactVersion> getSortByReleaseDate() {
    SortedSet<ArtifactVersion> sortedArtifacts = new TreeSet<>(RELEASE_DATE_COMPARISON);
    sortedArtifacts.addAll(elements);
    return sortedArtifacts;
  }

  /**
   * Returns Artifact version with given version.
   * If the version is not available an empty optional is returned.
   *
   * @param version to be searched version
   * @return found version or empty optional
   */
  public Optional<ArtifactVersion> getArtifactVersion(String version) {
    return elements.parallelStream()
        .filter(v -> v.getVersion().equals(version))
        .findFirst();
  }

  /**
   * Get a set of versions.
   *
   * @return The versions.
   */
  @JsonGetter("elements")
  public Set<ArtifactVersion> get() {
    return new HashSet<>(elements);
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
    return getSortByReleaseDate().stream()
        .map(v -> v.getVersion() + ":" + v.getReleaseDate())
        .collect(Collectors.joining(", "));
    // return elements.stream().map(ArtifactVersion::toString).collect(Collectors.joining(", "));
  }

  @Override
  public Iterator<ArtifactVersion> iterator() {
    return elements.iterator();
  }
}
