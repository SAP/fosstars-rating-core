package com.sap.sgs.phosphor.fosstars.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.sap.sgs.phosphor.fosstars.model.value.ValueHashSet;
import java.util.Optional;
import java.util.Set;

/**
 * A set of feature values. The set contains only unique features.
 */
@JsonSubTypes({
    @JsonSubTypes.Type(value = ValueHashSet.class, name = "ValueHashSet")
})
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
public interface ValueSet {

  /**
   * Checks if the set of values contains a value of a particular feature.
   *
   * @param feature A feature to search for.
   * @return True if the set contains a value for the specified feature, false otherwise.
   */
  boolean has(Feature feature);

  /**
   * Updates a value in the set.
   *
   * @param value A value to be updated.
   * @return This ValueSet,
   */
  ValueSet update(Value value);

  /**
   * Updates a number of values in the set.
   *
   * @param values The new values.
   * @return This ValueSet,
   */
  ValueSet update(ValueSet values);

  /**
   * Converts the set to an array of values.
   *
   * @return An array of values.
   */
  Value[] toArray();

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
   * @return A value of the specified feature.
   */
  Optional<Value> of(Feature feature);

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
  boolean containsAll(Set<Feature> features);
}
