package com.sap.oss.phosphor.fosstars.model;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import java.util.Optional;
import java.util.Set;

/**
 * A set of feature values. The set contains only unique features.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
public interface ValueSet extends Iterable<Value<?>> {

  /**
   * Checks if the set of values contains a value of a particular feature.
   *
   * @param feature A feature to search for.
   * @param <T> A type of data.
   * @return True if the set contains a value for the specified feature, false otherwise.
   */
  <T> boolean has(Feature<T> feature);

  /**
   * Updates values in the set.
   *
   * @param values Values to be updated.
   * @return This ValueSet.
   */
  ValueSet update(Value<?>... values);

  /**
   * Updates a number of values in the set.
   *
   * @param values The new values.
   * @return This ValueSet.
   */
  ValueSet update(ValueSet values);

  /**
   * Updates a number of values in the set.
   *
   * @param values The new values.
   * @return This ValueSet.
   */
  ValueSet update(Set<Value<?>> values);

  /**
   * Get a number of values in the value set.
   *
   * @return A number of values in the set.
   */
  int size();

  /**
   * Returns a value of a particular feature if it's present in the set.
   *
   * @param feature A feature to search for.
   * @param <T> A type of data.
   * @return A value of the specified feature.
   */
  <T> Optional<Value<T>> of(Feature<T> feature);

  /**
   * Tells whether the set is empty or not.
   *
   * @return True if the set is empty, false otherwise.
   */
  boolean isEmpty();

  /**
   * Checks if the set of values contains all the features.
   *
   * @param features A Set of features.
   * @return True if the set contains all the specified features, false otherwise.
   */
  boolean containsAll(Set<Feature<?>> features);

  /**
   * Converts a value set to a regular {@link Set}.
   *
   * @return A set with values.
   */
  Set<Value<?>> toSet();
}
