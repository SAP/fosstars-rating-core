package com.sap.sgs.phosphor.fosstars.data;

import com.sap.sgs.phosphor.fosstars.model.Feature;
import com.sap.sgs.phosphor.fosstars.model.Value;
import java.util.Date;
import java.util.Optional;

/**
 * This is an interface of a cache that stores feature values
 * for a specific type of objects.
 *
 * @param <T> The type of objects.
 */
public interface ValueCache<T> {

  /**
   * Returns a value of a feature for a key.
   *
   * @param key The key.
   * @param feature The feature.
   * @return An {@link Optional} with the value if the cache has it.
   */
  Optional<Value> get(T key, Feature feature);

  /**
   * Updates a value of a feature for a key.
   *
   * @param key The key.
   * @param value The value
   */
  void put(T key, Value value);

  /**
   * Updates a value of feature for a key.
   *
   * @param key The key.
   * @param value The value.
   * @param expiration When the value expires.
   */
  void put(T key, Value value, Date expiration);

}
