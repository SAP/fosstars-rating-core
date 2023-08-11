package com.sap.oss.phosphor.fosstars.tool.format.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The Feature class that encloses @link {@link com.sap.oss.phosphor.fosstars.model.Feature} to be
 * used in serialization.
 */
public class Feature {

  /**
   * The name of the feature.
   */
  private String name;

  /**
   * The value of the feature.
   */
  private String value;

  /**
   * Default constructor.
   */
  public Feature() {
  }

  /**
   * Initializes a Feature instance.
   *
   * @param name  the name of the feature.
   * @param value the value of the feature.
   */
  @JsonCreator
  public Feature(@JsonProperty("name") String name, @JsonProperty("value") String value) {
    this.name = name;
    this.value = value;
  }

  /**
   * Return the name of the feature.
   *
   * @return the name of the feature.
   */
  @JsonGetter("name")
  public String name() {
    return name;
  }

  /**
   * Set the name of the feature.
   *
   * @return the Feature instance.
   */
  public Feature name(String name) {
    this.name = name;
    return this;
  }

  /**
   * Return the value of the feature.
   *
   * @return the value of the feature.
   */
  @JsonGetter("value")
  public String value() {
    return value;
  }

  /**
   * Set the value of the feature.
   *
   * @return the Feature instance.
   */
  public Feature value(String value) {
    this.value = value;
    return this;
  }
}