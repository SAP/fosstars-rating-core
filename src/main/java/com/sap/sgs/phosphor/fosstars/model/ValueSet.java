package com.sap.sgs.phosphor.fosstars.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.sap.sgs.phosphor.fosstars.model.value.ValueHashSet;
import java.util.Optional;

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
   * Converts the set to an array of values.
   *
   * @return An array of values.
   */
  Value[] toArray();

  /**
   * Return a number of values in the set.
   */
  int size();

  /**
   * Returns a value of a particular feature if it's present in the set.
   *
   * @param feature A feature to search for.
   * @return A value of the specified feature.
   */
  Optional<Value> of(Feature feature);
}
