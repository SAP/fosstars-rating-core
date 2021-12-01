package com.sap.oss.phosphor.fosstars.tool.finder;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * A configuration for a GitHub organization to be scanned.
 */
public class OrganizationConfig {

  /**
   * An empty exclude list.
   */
  public static final List<String> EMPTY_EXCLUDE_LIST = Collections.emptyList();

  /**
   * Organization's name.
   */
  final String name;

  /**
   * A list of patterns that show which projects should be excluded.
   */
  final List<String> excludeList;

  /**
   * A minimal number of stars for a project.
   */
  final int stars;

  /**
   * Initializes a new config.
   *
   * @param name Organization's name.
   * @param excludeList A list of patterns that show which projects should be excluded.
   * @param stars A minimal number of stars for a project.
   */
  @JsonCreator
  public OrganizationConfig(
      @JsonProperty("name") String name,
      @JsonProperty("exclude") List<String> excludeList,
      @JsonProperty(value = "stars", defaultValue = "0") int stars) {

    Objects.requireNonNull(name, "Hey! Name can't be null!");
    if (stars < 0) {
      throw new IllegalArgumentException("Hey! Stars can't be negative!");
    }

    this.name = name;
    this.excludeList = excludeList != null ? excludeList : EMPTY_EXCLUDE_LIST;
    this.stars = stars;
  }

  /**
   * Checks if a specified project should be excluded.
   *
   * @param name The project's name.
   * @return True if the project should be excluded, false otherwise.
   */
  public boolean excluded(String name) {
    Objects.requireNonNull(name, "Hey! Name can't be null!");
    for (String string : excludeList) {
      if (name.contains(string)) {
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
    if (o instanceof OrganizationConfig == false) {
      return false;
    }
    OrganizationConfig that = (OrganizationConfig) o;
    return stars == that.stars
        && Objects.equals(name, that.name)
        && Objects.equals(excludeList, that.excludeList);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, excludeList, stars);
  }

  public int stars() {
    return stars;
  }

  public String name() {
    return name;
  }

  public List<String> excludeList() {
    return excludeList;
  }
}
