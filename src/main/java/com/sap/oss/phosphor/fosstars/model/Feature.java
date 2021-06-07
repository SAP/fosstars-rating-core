package com.sap.oss.phosphor.fosstars.model;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * An interface for a feature.
 * All implementations must:
 * <ul>
 *   <li>provide equals() and hashCode() methods.</li>
 *   <li>be stateless</li>
 *   <li>support serialization to JSON with Jackson</li>
 * </ul>
 *
 * @param <T> Type of data of a feature
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
public interface Feature<T> {

  /**
   * Get a name of the feature.
   *
   * @return A name of the feature.
   */
  String name();

  /**
   * Tries to convert an object to a feature value.
   * Throws an exception if the object can't be converted.
   *
   * @param object The object to be converted to a value.
   * @return An instance of {@link Value} initialized with the object.
   * @throws IllegalArgumentException If a value can't be initialized with the object.
   */
  Value<T> value(T object);

  /**
   * Tries to convert a string to a feature value.
   * Throws an exception if the object can't be converted.
   *
   * @param string The string to be converted.
   * @return An instance of {@link Value} initialized with the object.
   * @throws IllegalArgumentException If a value can't be initialized with the object.
   */
  Value<T> parse(String string);

  /**
   * Get an unknown value of the feature.
   *
   * @return An unknown value of the feature.
   */
  Value<T> unknown();

  /**
   * Accept a visitor.
   *
   * @param visitor The visitor.
   */
  void accept(Visitor visitor);
}
