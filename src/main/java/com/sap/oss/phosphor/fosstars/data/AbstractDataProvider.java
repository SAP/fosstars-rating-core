package com.sap.oss.phosphor.fosstars.data;

import com.sap.oss.phosphor.fosstars.model.ValueSet;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Objects;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This is a base class for data providers that holds commons stuff such as a logger, a cache,
 * a callback, etc.
 *
 * @param <T> A type of the objects for which the data provider can fetch data.
 */
public abstract class AbstractDataProvider<T> implements DataProvider<T> {

  /**
   * A logger.
   */
  protected final Logger logger = LogManager.getLogger(getClass());

  /**
   * A cache of values.
   */
  protected ValueCache<T> cache = NoValueCache.create();

  /**
   * An interface for interacting with a user.
   */
  protected UserCallback callback = NoUserCallback.INSTANCE;

  /**
   * Looks for default YAML configuration files and load them if found.
   * Default config fine names are based on the class name. If a canonical name of a data provider
   * is "com.sap.CustomDataProvider", then the method will try to load the following configs:
   * <ul>
   *   <li>com.sap.CustomDataProvider.config.yml</li>
   *   <li>com.sap.CustomDataProvider.config.yaml</li>
   *   <li>CustomDataProvider.config.yml</li>
   *   <li>CustomDataProvider.config.yaml</li>
   * </ul>
   *
   * @throws IOException If something went wrong.
   */
  protected void loadDefaultConfigIfAvailable() throws IOException {
    Class<?> clazz = getClass();
    for (String name : Arrays.asList(clazz.getSimpleName(), clazz.getCanonicalName())) {
      for (String suffix : Arrays.asList("yml", "yaml")) {
        Path path = Paths.get(String.format("%s.config.%s", name, suffix));
        if (Files.isRegularFile(path)) {
          logger.info("Found config for {} data provider: {}", clazz.getSimpleName(), path);
          configure(path);
        }
      }
    }
  }

  /**
   * <p>This is a template method that does a couple of checks for the parameters and then
   * calls the {@link #doUpdate(Object, ValueSet)} method.</p>
   *
   * <p>The {@link #doUpdate(Object, ValueSet)} method fetched the data and adds it
   * to the resulting set of feature values. The method has to be implemented
   * by the child classes.</p>
   *
   * @param object The object.
   * @param values The resulting set of values.
   * @return The same data provider.
   * @throws IOException If something went wrong.
   */
  @Override
  public final AbstractDataProvider<T> update(T object, ValueSet values) throws IOException {
    Objects.requireNonNull(object, "Hey! Object can't be null!");
    Objects.requireNonNull(values, "Hey! Values can't be null!");
    return doUpdate(object, values);
  }

  /**
   * Gathers data about an object and updates a specified set of values.
   *
   * @param object The object.
   * @param values The set of values.
   * @return The same data provider.
   * @throws IOException If something went wrong.
   */
  protected abstract AbstractDataProvider<T> doUpdate(T object, ValueSet values) throws IOException;

  @Override
  public ValueCache<T> cache() {
    return cache;
  }

  @Override
  public AbstractDataProvider<T> set(UserCallback callback) {
    Objects.requireNonNull(callback, "Hey! Callback can't be null!");
    this.callback = callback;
    return this;
  }

  @Override
  public AbstractDataProvider<T> set(ValueCache<T> cache) {
    this.cache = Objects.requireNonNull(cache, "Hey! Cache can't be null!");
    return this;
  }

  @Override
  public DataProvider<T> configure(Path config) throws IOException {
    return this;
  }
}
