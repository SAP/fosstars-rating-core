package com.sap.oss.phosphor.fosstars.data.interactive;

import com.sap.oss.phosphor.fosstars.data.AbstractDataProvider;
import com.sap.oss.phosphor.fosstars.model.ValueSet;
import java.io.IOException;

/**
 * This is a base class for data providers which talk to a user.
 *
 * @param <T> A type of objects for which a data provider gathers info.
 */
public abstract class AbstractInteractiveDataProvider<T> extends AbstractDataProvider<T> {

  @Override
  public final boolean interactive() {
    return true;
  }

  @Override
  protected final AbstractInteractiveDataProvider<T> doUpdate(T object, ValueSet values)
      throws IOException {

    if (!callback.canTalk()) {
      throw new IOException("Oh no! Callback can't talk!");
    }

    return ask(object, values);
  }

  /**
   * Asks a user a number of questions about an object and put corresponding feature values
   * to a specified values set.
   *
   * @param object The object to ask about.
   * @param values The value set to be updated.
   * @return The same data provider.
   */
  protected abstract AbstractInteractiveDataProvider<T> ask(T object, ValueSet values);
}
