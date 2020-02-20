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
 * This is a cache of feature values for an open-source project.
 * Open-source projects are identified by a URL to their source code repositories.
 * The cache is stored to a JSON file.
 */
public class ValueCache {

  /**
   * An ObjectMapper for serialization and deserialization.
   */
  private static final ObjectMapper MAPPER = new ObjectMapper();

  /**
   * A path to the cache.
   */
  private static final String DEFAULT_PATH_TO_CACHE = ".fosstars_model/value_cache.json";

  /**
   * A shared cache.
   */
  private static final ValueCache SHARED = initializeDefaultCache();

  /**
   * The cache entries.
   * The map maps a URL to a source code repository to a set of feature values.
   */
  private final Map<String, ValueSet> entries;

  /**
   * The default constructor.
   */
  public ValueCache() {
    this(new HashMap<>());
  }

  /**
   * Initializes a value cache.
   *
   * @param entries The cache entries.
   */
  private ValueCache(Map<String, ValueSet> entries) {
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
   * Get a cached value for a particular feature for a particular open-source project.
   *
   * @param url A URL to the source code repository of the open-source project.
   * @param feature The feature.
   * @return An {@link Optional} with a cached value if it's available.
   */
  public Optional<Value> get(String url, Feature feature) {
    ValueSet values = entries.get(url);
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
   * @param url A URL to the source code repository of the open-source project.
   * @param value The value to store to the cache.
   */
  public void put(String url, Value value) {
    put(url, value, NO_EXPIRATION);
  }

  /**
   * Store a value in the cache with an expiration date.
   *
   * @param url A URL to the source code repository of the open-source project.
   * @param value The value to store in the cache.
   * @param expiration The expiration date.
   */
  public void put(String url, Value value, Date expiration) {
    ValueSet set = entries.get(url);
    if (set == null) {
      set = new ValueHashSet();
      entries.put(url, set);
    }
    ExpiringValue wrapper = new ExpiringValue(value, expiration);
    set.update(wrapper);
  }

  /**
   * Stores the cache in the default location.
   *
   * @throws IOException If something went wrong.
   */
  public void store() throws IOException {
    store(DEFAULT_PATH_TO_CACHE);
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
   * Loads a cache from the default location.
   *
   * @return The loaded cache.
   * @throws IOException If something went wrong.
   */
  public static ValueCache load() throws IOException {
    return load(DEFAULT_PATH_TO_CACHE);
  }

  /**
   * Loads a cache from a specified file.
   *
   * @param path A path to the file.
   * @return The loaded cache.
   * @throws IOException If something went wrong.
   */
  public static ValueCache load(String path) throws IOException {
    File file = new File(path);
    if (!file.exists()) {
      throw new FileNotFoundException(String.format("Can't find %s", path));
    }
    return MAPPER.readValue(file, ValueCache.class);
  }

  /**
   * Returns the default cache.
   */
  public static ValueCache shared() {
    return SHARED;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o instanceof ValueCache == false) {
      return false;
    }
    ValueCache cache = (ValueCache) o;
    return Objects.equals(entries, cache.entries);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(entries);
  }

  /**
   * Initializes the default cache.
   *
   * @return The default cache.
   */
  private static ValueCache initializeDefaultCache() {
    try {
      return load();
    } catch (FileNotFoundException e) {
      System.out.println("[+] The default value cache doesn't exist yet.");
    } catch (IOException e) {
      System.out.println("[!] Could not load the default value cache!");
      e.printStackTrace(System.out);
    }
    return new ValueCache();
  }

}
