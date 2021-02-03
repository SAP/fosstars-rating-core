package com.sap.oss.phosphor.fosstars.data;

import static com.sap.oss.phosphor.fosstars.model.value.ExpiringValue.NO_EXPIRATION;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.sap.oss.phosphor.fosstars.model.Feature;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.ValueSet;
import com.sap.oss.phosphor.fosstars.model.value.ExpiringValue;
import com.sap.oss.phosphor.fosstars.model.value.ValueHashSet;
import com.sap.oss.phosphor.fosstars.util.Json;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * This is a cache of feature values which use string as keys.
 */
public class StandardValueCache implements Cache<String, ValueSet> {

  /**
   * A map of cache entries.
   */
  private final Map<String, ValueSet> entries;

  /**
   * The default constructor.
   */
  public StandardValueCache() {
    this(new HashMap<>());
  }

  /**
   * Initializes a value cache.
   *
   * @param entries The cache entries.
   */
  private StandardValueCache(Map<String, ValueSet> entries) {
    Objects.requireNonNull(entries, "Entries can't be null!");
    this.entries = entries;
  }

  /*
   * This getter is here to make Jackson happy.
   */
  @JsonGetter("entries")
  private Map<String, ValueSet> entries() {
    return entries;
  }

  /**
   * Get a cached value for a particular feature.
   *
   * @param key The key
   * @param feature The feature.
   * @param <T> Type of data that the feature holds.
   * @return An {@link Optional} with a cached value if it's available.
   */
  public <T> Optional<Value<T>> get(String key, Feature<T> feature) {
    ValueSet values = entries.get(key);
    if (values == null) {
      return Optional.empty();
    }
    Optional<Value<T>> something = values.of(feature);
    if (!something.isPresent()) {
      return Optional.empty();
    }

    return unwrapExpiring(something.get());
  }

  @Override
  public Optional<ValueSet> get(String key) {
    ValueSet set = entries.get(key);
    if (set == null) {
      return Optional.empty();
    }

    ValueSet result = new ValueHashSet();
    for (Value<?> value : set) {
      unwrapExpiring(value).ifPresent(result::update);
    }

    return Optional.of(result);
  }

  /**
   * The method extracts an original value from a ExpiringValue if it's not expired.
   *
   * @param value The ExpiringValue.
   * @param <T> Type of data that the value holds.
   * @return The original value if it's not expired.
   * @throws IllegalStateException If the value is not an instance of ExpiringValue.
   */
  private static <T> Optional<Value<T>> unwrapExpiring(Value<T> value) {
    if (value instanceof ExpiringValue == false) {
      throw new IllegalStateException("It should be an expiring value!");
    }
    ExpiringValue<T> expiringValue = (ExpiringValue<T>) value;

    if (expiringValue.neverExpires() || !expiringValue.expired()) {
      return Optional.of(expiringValue.original());
    }

    return Optional.empty();
  }

  @Override
  public int size() {
    return entries.size();
  }

  @Override
  public void put(String key, ValueSet value) {
    put(key, value, NO_EXPIRATION);
  }

  @Override
  public void put(String key, ValueSet set, Date expiration) {
    for (Value<?> value : set) {
      put(key, value, expiration);
    }
  }

  /**
   * Store a value in the cache without expiration date.
   *
   * @param key The key.
   * @param value The value to store to the cache.
   * @param <T> A type of the value.
   */
  public <T> void put(String key, Value<T> value) {
    put(key, value, NO_EXPIRATION);
  }

  /**
   * Store a value in the cache with an expiration date.
   *
   * @param key The key.
   * @param value The value to store in the cache.
   * @param <T> Type of data that the value holds
   * @param expiration The expiration date.
   */
  public <T> void put(String key, Value<T> value, Date expiration) {
    ValueSet set = entries.get(key);
    if (set == null) {
      set = new ValueHashSet();
      entries.put(key, set);
    }
    ExpiringValue<T> wrapper = new ExpiringValue<>(value, expiration);
    set.update(wrapper);
  }

  /**
   * Loads a cache from a specified file.
   *
   * @param filename A path to the file.
   * @throws IOException If something went wrong.
   */
  public void store(String filename) throws IOException {
    Path path = Paths.get(filename);
    Path dir = path.getParent();
    if (!Files.exists(dir)) {
      Files.createDirectories(dir);
    }
    Files.write(path, Json.toBytes(this));
  }

  /**
   * Loads a cache from a specified file.
   *
   * @param path A path to the file.
   * @return The loaded cache.
   * @throws IOException If something went wrong.
   */
  public static StandardValueCache load(String path) throws IOException {
    File file = new File(path);
    if (!file.exists()) {
      throw new FileNotFoundException(String.format("Can't find %s", path));
    }
    return Json.mapper().readValue(file, StandardValueCache.class);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o instanceof StandardValueCache == false) {
      return false;
    }
    StandardValueCache cache = (StandardValueCache) o;
    return Objects.equals(entries, cache.entries);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(entries);
  }

}
