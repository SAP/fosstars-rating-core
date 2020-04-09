package com.sap.sgs.phosphor.fosstars.model.value;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A set of package managers.
 */
public class PackageManagers {

  /**
   * A set of package managers.
   */
  private final Set<PackageManager> packageManagers;

  /**
   * Initializes a set of package managers.
   *
   * @param packageManagers A set of package managers.
   */
  @JsonCreator
  public PackageManagers(
      @JsonProperty("packageManagers") Set<PackageManager> packageManagers) {

    Objects.requireNonNull(packageManagers, "Package managers can't be null!");
    this.packageManagers = packageManagers;
  }

  /**
   * Initializes a set of package managers.
   *
   * @param packageManagers A number of package managers.
   */
  public PackageManagers(PackageManager... packageManagers) {
    this(EnumSet.noneOf(PackageManager.class));
    this.packageManagers.addAll(Arrays.asList(packageManagers));
  }

  /**
   * Returns a number of package managers in the set.
   */
  public int size() {
    return packageManagers.size();
  }

  /**
   * Returns a list of the package managers.
   */
  public List<PackageManager> list() {
    return new ArrayList<>(packageManagers);
  }

  /*
   * This getter is for serialization with Jackson.
   */
  @JsonGetter("packageManagers")
  private Set<PackageManager> packageManagers() {
    return packageManagers;
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
    return packageManagers.stream().map(Enum::toString).collect(Collectors.joining(", "));
  }
}
