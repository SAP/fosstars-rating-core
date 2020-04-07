package com.sap.sgs.phosphor.fosstars.data;

import com.sap.sgs.phosphor.fosstars.model.ValueSet;
import java.io.IOException;
import java.util.Objects;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class AbstractDataProvider<T> implements DataProvider<T> {

  /**
   * A logger.
   */
  protected final Logger logger = LogManager.getLogger(getClass());

  /**
   * A cache of values.
   */
  protected ValueCache<T> cache = NoCache.create();

  /**
   * An interface for interacting with a user.
   */
  protected UserCallback callback = NoUserCallback.INSTANCE;

  @Override
  public AbstractDataProvider<T> update(T object, ValueSet values) throws IOException {
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

}
