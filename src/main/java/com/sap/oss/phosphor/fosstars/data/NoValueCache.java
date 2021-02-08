package com.sap.oss.phosphor.fosstars.data;

import com.sap.oss.phosphor.fosstars.model.Feature;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.ValueSet;
import java.util.Date;
import java.util.Optional;

/**
 * This is a dummy cache which stores nothing.
 */
public class NoValueCache<K> implements ValueCache<K> {

  /**
   * Creates an instance of {@link NoValueCache}.
   *
   * @param <T> Type of keys.
   * @return A new cache.
   */
  public static <T> NoValueCache<T> create() {
    return new NoValueCache<>();
  }

  @Override
  public <T> Optional<Value<T>> get(K key, Feature<T> feature) {
    return Optional.empty();
  }

  @Override
  public Optional<ValueSet> get(K key) {
    return Optional.empty();
  }

  @Override
  public void put(K key, ValueSet value) {
    // do nothing
  }

  @Override
  public void put(K key, ValueSet value, Date expiration) {
    // do nothing
  }

  @Override
  public int size() {
    return 0;
  }
}
