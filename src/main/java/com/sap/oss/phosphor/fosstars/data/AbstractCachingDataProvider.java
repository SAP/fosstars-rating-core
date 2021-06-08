package com.sap.oss.phosphor.fosstars.data;

import com.sap.oss.phosphor.fosstars.model.Feature;
import com.sap.oss.phosphor.fosstars.model.Subject;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.ValueSet;
import com.sap.oss.phosphor.fosstars.model.value.UnknownValue;
import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * This is a base class for data providers that would like to check the cache
 * before trying to fetch values for a number of features.
 */
public abstract class AbstractCachingDataProvider extends AbstractDataProvider {

  /**
   * This is a template method that checks for cached values
   * before asking the child classes to fetch the data.
   * If all supported features are already available in the cache, then the method just
   * adds them to the resulting value set and exits. Otherwise, the method calls
   * the {@link #fetchValuesFor(Subject)} method to fetch the data.
   *
   * @param subject The Subject for which the values need to be fetched.
   * @param values The resulting set of values to be updated.
   * @return The same data provider.
   * @throws IOException If something went wrong.
   */
  @Override
  public final AbstractCachingDataProvider doUpdate(Subject subject, ValueSet values)
      throws IOException {

    // get a list of features which are supported by the data provider
    Set<Feature<?>> features = supportedFeatures();

    // look for values for the supported features in the cache
    // unknown values don't count
    Set<Value<?>> cachedValues = new HashSet<>();
    for (Feature feature : features) {
      Optional<Value<?>> something = cache.get(subject, feature);
      Value<?> value = something.orElse(UnknownValue.of(feature));
      if (!value.isUnknown()) {
        cachedValues.add(value);
      }
    }

    // put the cached values to the resulting set of values
    for (Value<?> value : cachedValues) {
      values.update(value);
    }

    // if there is a cached known value for each feature, then we're done
    if (cachedValues.size() == features.size()) {
      return this;
    }

    // otherwise, try to find values for all features
    ValueSet updatedValues = fetchValuesFor(subject);

    // put the fetched values to the cache
    cache.put(subject, updatedValues, expiration());

    // and update the resulting set of values
    values.update(updatedValues);

    return this;
  }

  /**
   * Fetch values for a subject.
   *
   * @param subject The subject
   * @return A set of values for the subject.
   * @throws IOException If something went wrong.
   */
  protected abstract ValueSet fetchValuesFor(Subject subject) throws IOException;

  /**
   * Get an expiration date for cache entries.
   *
   * @return An expiration date for cache entries.
   */
  protected Date expiration() {
    return Date.from(Instant.now().plus(1, ChronoUnit.DAYS)); // tomorrow
  }
}
