package com.sap.oss.phosphor.fosstars.data.interactive;

import com.sap.oss.phosphor.fosstars.data.AbstractDataProvider;
import com.sap.oss.phosphor.fosstars.model.Subject;
import com.sap.oss.phosphor.fosstars.model.ValueSet;
import java.io.IOException;

/**
 * This is a base class for data providers which talk to a user.
 */
public abstract class AbstractInteractiveDataProvider extends AbstractDataProvider {

  @Override
  public final boolean interactive() {
    return true;
  }

  @Override
  protected final AbstractInteractiveDataProvider doUpdate(Subject subject, ValueSet values)
      throws IOException {

    if (!callback.canTalk()) {
      throw new IOException("Oh no! Callback can't talk!");
    }

    return ask(subject, values);
  }

  /**
   * Asks a user a number of questions about an subject and put corresponding feature values
   * to a specified values set.
   *
   * @param subject The subject to ask about.
   * @param values The value set to be updated.
   * @return The same data provider.
   */
  protected abstract AbstractInteractiveDataProvider ask(Subject subject, ValueSet values);
}
