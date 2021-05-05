package com.sap.oss.phosphor.fosstars.model.value;

import static com.sap.oss.phosphor.fosstars.model.other.Utils.setOf;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * A set of package managers.
 */
public class PackageManagers implements Iterable<PackageManager> {

  /**
   * A set of package managers.
   */
  private final Set<PackageManager> packageManagers;

  /**
   * Creates an empty set of package managers.
   *
   * @return An empty set of package managers.
   */
  public static PackageManagers empty() {
    return new PackageManagers();
  }

  /**
   * Initializes a set of package managers.
   *
   * @param entries A number of package managers.
   * @return A new set of package managers.
   */
  public static PackageManagers from(PackageManager... entries) {
    return new PackageManagers(entries);
  }

  /**
   * Initializes a set of package managers.
   *
   * @param packageManagers A set of package managers.
   */
  @JsonCreator
  public PackageManagers(
      @JsonProperty("packageManagers") Set<PackageManager> packageManagers) {

    Objects.requireNonNull(packageManagers, "Package managers can't be null!");
    this.packageManagers = new TreeSet<>(packageManagers);
  }

  /**
   * Initializes a set of package managers.
   *
   * @param packageManagers A number of package managers.
   */
  public PackageManagers(PackageManager... packageManagers) {
    this(setOf(packageManagers));
  }

  /**
   * Get a size of the collection.
   *
   * @return A number of package managers in the set.
   */
  public int size() {
    return packageManagers.size();
  }

  /**
   * Converts to a list.
   *
   * @return A list of the package managers.
   */
  public List<PackageManager> list() {
    return new ArrayList<>(packageManagers);
  }

  /**
   * This getter is for serialization with Jackson.
   *
   * @return A set of package managers.
   */
  @JsonGetter("packageManagers")
  private Set<PackageManager> packageManagers() {
    return new TreeSet<>(packageManagers);
  }

  /**
   * Adds a collection of package managers.
   *
   * @param others The package managers.
   */
  public void add(PackageManagers others) {
    Objects.requireNonNull(others, "Package managers can't be null!");
    packageManagers.addAll(others.list());
  }

  /**
   * Add a package managers.
   *
   * @param packageManager The package manager.
   */
  public void add(PackageManager packageManager) {
    Objects.requireNonNull(packageManager, "Package manager can't be null!");
    packageManagers.add(packageManager);
  }

  /**
   * Check if the collection has at least one of the specified package managers.
   *
   * @param packageManagers The package managers.
   * @return True if the collection contains at least one of the package managers, false otherwise.
   */
  public boolean containsAny(PackageManager... packageManagers) {
    return containsAny(from(packageManagers));
  }

  /**
   * Check if the collection has at least one of the specified package managers.
   *
   * @param packageManagers The package managers.
   * @return True if the collection contains at least one of the package managers, false otherwise.
   */
  public boolean containsAny(PackageManagers packageManagers) {
    Objects.requireNonNull(packageManagers, "Package manager can't be null!");

    for (PackageManager packageManager : packageManagers) {
      if (this.packageManagers.contains(packageManager)) {
        return true;
      }
    }

    return false;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o instanceof PackageManagers == false) {
      return false;
    }
    PackageManagers that = (PackageManagers) o;
    return Objects.equals(packageManagers, that.packageManagers);
  }

  @Override
  public int hashCode() {
    return Objects.hash(packageManagers);
  }

  @Override
  public String toString() {
    if (packageManagers.isEmpty()) {
      return "None";
    }
    return packageManagers.stream().map(Enum::toString).collect(Collectors.joining(", "));
  }

  @Override
  public Iterator<PackageManager> iterator() {
    return packageManagers.iterator();
  }
}
