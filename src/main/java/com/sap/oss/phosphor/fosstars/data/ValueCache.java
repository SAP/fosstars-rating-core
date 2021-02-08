package com.sap.oss.phosphor.fosstars.data;

import com.sap.oss.phosphor.fosstars.model.Feature;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.ValueSet;
import java.util.Optional;

/**
 * This is an interface of a cache that stores feature values
 * for a specific type of objects.
 *
 * @param <K> The type of objects.
 */
public interface ValueCache<K> extends Cache<K, ValueSet> {

  /**
   * Return a value of a feature for a key.
   *
   * @param key The key.
   * @param feature The feature.
   * @param <T> Type of data held by the feature.
   * @return An {@link Optional} with the value if the cache has it.
   */
  <T> Optional<Value<T>> get(K key, Feature<T> feature);
}
