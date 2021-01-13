package com.sap.oss.phosphor.fosstars.data;

import java.util.Date;
import java.util.Optional;

/**
 * This is an interface of a cache that stores different data types for a specific type of objects.
 */
public interface Cache<K, V> {

  /**
   * Returns a value of a object type for a key.
   *
   * @param key The key.
   * @return An {@link Optional} with the value if the cache has it.
   */
  Optional<V> get(K key);

  /**
   * Updates a value of an object type for a key.
   *
   * @param key The key.
   * @param value The value.
   */
  void put(K key, V value);

  /**
   * Updates a value of an object type for a key.
   *
   * @param key The key.
   * @param value The value.
   * @param expiration When the value expires.
   */
  void put(K key, V value, Date expiration);

  /**
   * Get a size of the cache.
   *
   * @return A number of elements in the cache.
   */
  int size();
}
