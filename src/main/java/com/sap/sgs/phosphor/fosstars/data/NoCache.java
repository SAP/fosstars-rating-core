package com.sap.sgs.phosphor.fosstars.data;

import com.sap.sgs.phosphor.fosstars.model.Feature;
import com.sap.sgs.phosphor.fosstars.model.Value;
import java.util.Date;
import java.util.Optional;

public class NoCache<T> implements ValueCache<T> {

  /**
   * Creates an instance of {@link NoCache}.
   *
   * @param <T> Type of keys.
   * @return A new cache.
   */
  public static <T> NoCache<T> create() {
    return new NoCache<>();
  }

  @Override
  public Optional<Value> get(T key, Feature feature) {
    return Optional.empty();
  }

  @Override
  public void put(T key, Value value) {
    // do nothing
  }

  @Override
  public void put(T key, Value value, Date expiration) {
    // do nothing
  }
}
