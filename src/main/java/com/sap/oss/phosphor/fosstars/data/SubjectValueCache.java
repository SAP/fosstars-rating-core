package com.sap.oss.phosphor.fosstars.data;

import com.sap.oss.phosphor.fosstars.model.Feature;
import com.sap.oss.phosphor.fosstars.model.Subject;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.ValueSet;
import java.io.IOException;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;

/**
 * This is a cache of feature values for subjects.
 * The cache just wraps {@link StandardValueCache}.
 */
public class SubjectValueCache implements ValueCache<Subject> {

  /**
   * An underlying cache.
   */
  private final StandardValueCache cache;

  /**
   * Initializes a new cache.
   */
  public SubjectValueCache() {
    this(new StandardValueCache());
  }

  /**
   * Initializes a new cache with a {@link StandardValueCache}.
   *
   * @param cache The {@link StandardValueCache}.
   */
  public SubjectValueCache(StandardValueCache cache) {
    this.cache = Objects.requireNonNull(cache, "Unfortunately cache can't be null");
  }

  /**
   * Looks for a feature value for a specified subject.
   *
   * @param subject The subject.
   * @param feature The feature.
   * @param <T> Type of data.
   * @return An {@link Optional} with the feature value if it's in the cache.
   */
  public <T> Optional<Value<T>> get(Subject subject, Feature<T> feature) {
    return cache.get(subject.purl(), feature);
  }

  @Override
  public Optional<ValueSet> get(Subject subject) {
    return cache.get(subject.purl());
  }

  /**
   * Store a value for a subject.
   *
   * @param subject The subject.
   * @param value The value.
   * @param <T> A type of the value.
   */
  public <T> void put(Subject subject, Value<T> value) {
    cache.put(subject.purl(), value);
  }

  /**
   * Store a value with an expiration data for a subject.
   *
   * @param subject The subject.
   * @param value The value.
   * @param expiration The expiration date.
   * @param <T> A type of the value.
   */
  public <T> void put(Subject subject, Value<T> value, Date expiration) {
    cache.put(subject.purl(), value, expiration);
  }

  @Override
  public void put(Subject subject, ValueSet set) {
    cache.put(subject.purl(), set);
  }

  @Override
  public void put(Subject subject, ValueSet set, Date expiration) {
    cache.put(subject.purl(), set, expiration);
  }

  /**
   * Stores the cache to a file.
   *
   * @param filename A name of the file.
   * @throws IOException If something went wrong.
   */
  public void store(String filename) throws IOException {
    cache.store(filename);
  }

  @Override
  public int size() {
    return cache.size();
  }
}
