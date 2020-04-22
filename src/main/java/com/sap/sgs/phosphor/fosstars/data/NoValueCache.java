package com.sap.sgs.phosphor.fosstars.data;

import com.sap.sgs.phosphor.fosstars.model.Feature;
import com.sap.sgs.phosphor.fosstars.model.Value;
import com.sap.sgs.phosphor.fosstars.model.ValueSet;
import java.util.Date;
import java.util.Optional;

/**
 * This is a dummy cache which stores nothing.
 */
public class NoValueCache<T> implements ValueCache<T> {

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
  public Optional<Value> get(T key, Feature feature) {
    return Optional.empty();
  }

  @Override
  public Optional<ValueSet> get(T key) {
    return Optional.empty();
  }

  @Override
  public void put(T key, ValueSet value) {
    // do nothing
  }

  @Override
  public void put(T key, ValueSet value, Date expiration) {
    // do nothing
  }

  @Override
  public int size() {
    return 0;
  }
}
