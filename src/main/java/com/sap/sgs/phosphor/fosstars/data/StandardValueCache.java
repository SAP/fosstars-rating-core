package com.sap.sgs.phosphor.fosstars.data;

import static com.sap.sgs.phosphor.fosstars.model.value.ExpiringValue.NO_EXPIRATION;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sap.sgs.phosphor.fosstars.model.Feature;
import com.sap.sgs.phosphor.fosstars.model.Value;
import com.sap.sgs.phosphor.fosstars.model.ValueSet;
import com.sap.sgs.phosphor.fosstars.model.value.ExpiringValue;
import com.sap.sgs.phosphor.fosstars.model.value.ValueHashSet;
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
public class StandardValueCache implements ValueCache<String> {

  /**
   * An ObjectMapper for serialization and deserialization.
   */
  private static final ObjectMapper MAPPER = new ObjectMapper();

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
   * @return An {@link Optional} with a cached value if it's available.
   */
  @Override
  public Optional<Value> get(String key, Feature feature) {
    ValueSet values = entries.get(key);
    if (values == null) {
      return Optional.empty();
    }
    Optional<Value> something = values.of(feature);
    if (!something.isPresent()) {
      return Optional.empty();
    }
    Value value = something.get();
    if (value instanceof ExpiringValue == false) {
      throw new IllegalStateException("It should be an expiring value!");
    }
    ExpiringValue expiringValue = (ExpiringValue) value;
    if (expiringValue.neverExpires()) {
      return something;
    }
    if (expiringValue.expired()) {
      return Optional.empty();
    }
    return something;
  }

  /**
   * Store a value in the cache without expiration date.
   *
   * @param key The key.
   * @param value The value to store to the cache.
   */
  @Override
  public void put(String key, Value value) {
    put(key, value, NO_EXPIRATION);
  }

  /**
   * Store a value in the cache with an expiration date.
   *
   * @param key The key.
   * @param value The value to store in the cache.
   * @param expiration The expiration date.
   */
  @Override
  public void put(String key, Value value, Date expiration) {
    ValueSet set = entries.get(key);
    if (set == null) {
      set = new ValueHashSet();
      entries.put(key, set);
    }
    ExpiringValue wrapper = new ExpiringValue(value, expiration);
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
    Files.write(path, MAPPER.writerWithDefaultPrettyPrinter().writeValueAsBytes(this));
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
    return MAPPER.readValue(file, StandardValueCache.class);
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
