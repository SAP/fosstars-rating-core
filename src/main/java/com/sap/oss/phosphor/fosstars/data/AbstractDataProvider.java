package com.sap.oss.phosphor.fosstars.data;

import static com.sap.oss.phosphor.fosstars.util.Config.loadDefaultYamlConfigIfAvailable;

import com.sap.oss.phosphor.fosstars.model.Subject;
import com.sap.oss.phosphor.fosstars.model.ValueSet;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This is a base class for data providers that holds commons stuff such as a logger, a cache,
 * a callback, etc.
 */
public abstract class AbstractDataProvider implements DataProvider {

  /**
   * A logger.
   */
  protected final Logger logger = LogManager.getLogger(getClass());

  /**
   * A cache of values.
   */
  protected ValueCache<Subject> cache = NoValueCache.create();

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
    Optional<Path> path = loadDefaultYamlConfigIfAvailable(clazz);
    if (path.isPresent()) {
      logger.info("Found config for {} data provider: {}", clazz.getSimpleName(), path.get());
      configure(path.get());
    }
  }

  /**
   * <p>This is a template method that does a couple of checks for the parameters and then
   * calls the {@link #doUpdate(Subject, ValueSet)} method.</p>
   *
   * <p>The {@link #doUpdate(Subject, ValueSet)} method fetched the data and adds it
   * to the resulting set of feature values. The method has to be implemented
   * by the child classes.</p>
   *
   * @param subject The subject.
   * @param values The resulting set of values.
   * @return The same data provider.
   * @throws IOException If something went wrong.
   */
  @Override
  public final AbstractDataProvider update(Subject subject, ValueSet values) throws IOException {
    Objects.requireNonNull(subject, "Hey! Subject can't be null!");
    Objects.requireNonNull(values, "Hey! Values can't be null!");
    return doUpdate(subject, values);
  }

  /**
   * Gathers data about an subject and updates a specified set of values.
   *
   * @param subject The subject.
   * @param values The set of values.
   * @return The same data provider.
   * @throws IOException If something went wrong.
   */
  protected abstract AbstractDataProvider doUpdate(Subject subject, ValueSet values)
      throws IOException;

  @Override
  public ValueCache<Subject> cache() {
    return cache;
  }

  @Override
  public AbstractDataProvider set(UserCallback callback) {
    Objects.requireNonNull(callback, "Hey! Callback can't be null!");
    this.callback = callback;
    return this;
  }

  @Override
  public AbstractDataProvider set(ValueCache<Subject> cache) {
    this.cache = Objects.requireNonNull(cache, "Hey! Cache can't be null!");
    return this;
  }

  @Override
  public AbstractDataProvider configure(Path config) throws IOException {
    return this;
  }
}
