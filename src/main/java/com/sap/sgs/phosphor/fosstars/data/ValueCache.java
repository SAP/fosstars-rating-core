package com.sap.sgs.phosphor.fosstars.data;

import com.sap.sgs.phosphor.fosstars.model.Feature;
import com.sap.sgs.phosphor.fosstars.model.Value;
import com.sap.sgs.phosphor.fosstars.model.ValueSet;
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
   * @return An {@link Optional} with the value if the cache has it.
   */
  Optional<Value> get(K key, Feature feature);
}
