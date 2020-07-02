package com.sap.sgs.phosphor.fosstars.data;

import com.sap.sgs.phosphor.fosstars.model.Feature;
import com.sap.sgs.phosphor.fosstars.model.Value;
import com.sap.sgs.phosphor.fosstars.model.ValueSet;
import com.sap.sgs.phosphor.fosstars.model.value.UnknownValue;
import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * <p>This is a base class for data providers that would like to check the cache
 * before trying to fetch values for a number of features.</p>
 *
 * @param <T> A type of the objects for which the data provider can fetch data.
 */
public abstract class AbstractCachingDataProvider<T> extends AbstractDataProvider<T> {

  /**
   * This is a template method that checks for cached values
   * before asking the child classes to fetch the data.
   * If all supported features are already available in the cache, then the method just
   * adds them to the resulting value set and exits. Otherwise, the method calls
   * the {@link #fetchValuesFor(Object)} method to fetch the data.
   *
   * @param object The object for which the values need to be fetched.
   * @param values The resulting set of values to be updated.
   * @return The same data provider.
   * @throws IOException If something went wrong.
   */
  @Override
  public final AbstractCachingDataProvider<T> doUpdate(T object, ValueSet values)
      throws IOException {

    // get a list of features which are supported by the data provider
    Set<Feature> features = supportedFeatures();

    // look for values for the supported features in the cache
    // unknown values don't count
    Set<Value> cachedValues = new HashSet<>();
    for (Feature feature : features) {
      Optional<Value> something = cache.get(object, feature);
      Value value = something.orElse(UnknownValue.of(feature));
      if (!value.isUnknown()) {
        cachedValues.add(value);
      }
    }

    // put the cached values to the resulting set of values
    for (Value value : cachedValues) {
      values.update(value);
    }

    // if there is a cached known value for each feature, then we're done
    if (cachedValues.size() == features.size()) {
      return this;
    }

    // otherwise, try to find values for all features
    ValueSet updatedValues = fetchValuesFor(object);

    // put the fetched values to the cache
    cache.put(object, updatedValues, expiration());

    // and update the resulting set of values
    values.update(updatedValues);

    return this;
  }

  /**
   * Fetch values for an object.
   *
   * @param object The object
   * @return A set of values for the object.
   * @throws IOException If something went wrong.
   */
  protected abstract ValueSet fetchValuesFor(T object) throws IOException;

  /**
   * Get an expiration date for cache entries.
   *
   * @return An expiration date for cache entries.
   */
  protected Date expiration() {
    return Date.from(Instant.now().plus(1, ChronoUnit.DAYS)); // tomorrow
  }
}
