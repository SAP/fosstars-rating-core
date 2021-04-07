package com.sap.oss.phosphor.fosstars.data;

import com.sap.oss.phosphor.fosstars.model.Feature;
import com.sap.oss.phosphor.fosstars.model.ValueSet;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Set;

/**
 * An interface of a data provider which knows
 * how to gather feature value for specific objects such as open-source projects.
 * A data provider may be configured with a {@link UserCallback}
 * which allows asking a user for data.
 * A data provider may ignore the specified {@link UserCallback}
 * if it's able to gather data without a user.
 *
 * @param <T> A type of the objects.
 */
public interface DataProvider<T> {

  /**
   * Gathers data about an object and updates a specified {@link ValueSet}.
   *
   * @param object The object.
   * @param values The set of values to be updated.
   * @return This data provider.
   * @throws IOException If something went wrong.
   */
  DataProvider<T> update(T object, ValueSet values) throws IOException;

  /**
   * Tells whether the provider talks to a user.
   *
   * @return True if the provider talks to a user via {@link UserCallback}, false otherwise.
   */
  boolean interactive();

  /**
   * Get a cache of values.
   *
   * @return A cache of values that is used by the data provider.
   */
  ValueCache<T> cache();

  /**
   * Sets an interface for interacting with a user.
   *
   * @param callback An interface for interacting with a user.
   * @return This data provider.
   */
  DataProvider<T> set(UserCallback callback);

  /**
   * Sets a cache of values that should be used by the data provider.
   *
   * @param cache The cache of values.
   * @return This data provider.
   */
  DataProvider<T> set(ValueCache<T> cache);

  /**
   * Get a set of supported features.
   *
   * @return A set of features that the provider can fill out.
   */
  Set<Feature<?>> supportedFeatures();

  /**
   * Load a configuration from a file.
   *
   * @param config The config file.
   * @return This data provider.
   * @throws IOException If something went wrong.
   */
  DataProvider<T> configure(Path config) throws IOException;
}
