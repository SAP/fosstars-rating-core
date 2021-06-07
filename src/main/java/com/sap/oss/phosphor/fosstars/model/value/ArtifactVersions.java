package com.sap.oss.phosphor.fosstars.model.value;

import static com.sap.oss.phosphor.fosstars.model.other.Utils.setOf;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sap.oss.phosphor.fosstars.model.Value;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import org.apache.maven.artifact.versioning.ComparableVersion;

/**
 * A set versions for an artifact.
 */
public class ArtifactVersions implements Iterable<ArtifactVersion> {

  /**
   * Major version template.
   */
  private static final String MAJOR_VERSION_TEMPLATE = "%s.0.0";

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
  public static ArtifactVersions ofNothing() {
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
    this.elements = new TreeSet<>(ArtifactVersion.RELEASE_DATE_VERSION_COMPARISON);
    this.elements.addAll(versions);
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
   * Check if artifact versions set is empty.
   *
   * @return true if empty otherwise false.
   */
  public boolean empty() {
    return elements.isEmpty();
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
   * Checks if the collection contains all the other versions.
   *
   * @param versions The other versions.
   * @return True if all the other versions is present in the collection,
   *         false otherwise.
   */
  public boolean containsAllOf(ArtifactVersions versions) {
    for (ArtifactVersion version : versions) {
      if (!this.elements.contains(version)) {
        return false;
      }
    }

    return true;
  }

  /**
   * Get versions sorted by date.
   * First entry is the latest release.
   * Creates a new collection with the sorted versions.
   *
   * @return versions sorted by date
   */
  public Collection<ArtifactVersion> sortByReleaseDate() {
    return Collections.unmodifiableCollection(elements);
  }

  /**
   * Sort artifact versions hold by ArtifactVersions by release date.
   *
   * @param artifactVersions the artifact versions
   * @return sorted collection of ArtifactVersion
   */
  public static Collection<ArtifactVersion> sortByReleaseDate(
      Value<ArtifactVersions> artifactVersions) {

    if (artifactVersions.isUnknown()) {
      return Collections.emptyList();
    }
    return artifactVersions.get().sortByReleaseDate();
  }

  /**
   * Filter {@link ArtifactVersions} by major version.
   *
   * @param version The {@link SemanticVersion}.
   * @return Filtered {@link ArtifactVersions}.
   */
  public ArtifactVersions filterArtifactsByMajorVersion(SemanticVersion version) {
    final int major = version.getMajor();
    ComparableVersion currentMajorVersion =
        new ComparableVersion(String.format(MAJOR_VERSION_TEMPLATE, major));
    ComparableVersion nextMajorVersion =
        new ComparableVersion(String.format(MAJOR_VERSION_TEMPLATE, major + 1));

    return new ArtifactVersions(
        elements.stream()
          .filter(v -> v.hasValidSemanticVersion())
          .filter(v -> {
            final ComparableVersion comparableVersion = new ComparableVersion(v.version());
            return comparableVersion.compareTo(currentMajorVersion) >= 0
                && comparableVersion.compareTo(nextMajorVersion) < 0;
          })
          .collect(Collectors.toSet()));
  }

  /**
   * Returns Artifact version with given version.
   * If the version is not available an empty optional is returned.
   *
   * @param version to be searched version
   * @return found version or empty optional
   */
  public Optional<ArtifactVersion> get(String version) {
    return elements.stream()
        .filter(artifact -> artifact.version().equals(version))
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
    if (elements.isEmpty()) {
      return "No versions";
    }
    Collection<ArtifactVersion> sortByReleaseDate = sortByReleaseDate();
    final int max = 5;
    String message = sortByReleaseDate.stream()
        .limit(max)
        .map(artifact -> String.format("%s:%s", artifact.version(), artifact.releaseDate()))
        .collect(Collectors.joining(", "));

    if (sortByReleaseDate.size() > max) {
      return String.format("%s (%s in total) ...", message, sortByReleaseDate.size());
    }
    return message;
  }

  @Override
  public Iterator<ArtifactVersion> iterator() {
    return elements.iterator();
  }
}
