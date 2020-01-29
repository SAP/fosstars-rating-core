package com.sap.sgs.phosphor.fosstars.data;

import com.sap.sgs.phosphor.fosstars.model.Value;
import java.io.IOException;

/**
 * An interface of a data provider which can get a value of a particular feature. A data provider
 * may be called with a {@link UserCallback} which allows asking a user for data. A data provider
 * may ignore {@link UserCallback} if it can gather data without a user.
 */
public interface DataProvider {

  /**
   * Gathers data and wraps it to a {@link Value}. The method can take a {@link UserCallback} which
   * allows asking a user for data.
   *
   * @param callback An interface for interacting with a user.
   * @return A feature value (maybe {@link com.sap.sgs.phosphor.fosstars.model.value.UnknownValue}).
   * @throws IOException If something went wrong.
   */
  Value get(UserCallback callback) throws IOException;

  /**
   * @return True if a data provider is allowed to interact with a user, false otherwise.
   */
  boolean mayTalk();
}
