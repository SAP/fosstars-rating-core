package com.sap.sgs.phosphor.fosstars.data;

import com.sap.sgs.phosphor.fosstars.model.ValueSet;
import java.io.IOException;

/**
 * An interface of a data provider which knows how to gather values for specific features.
 * A data provider may be configured with a {@link UserCallback}
 * which allows asking a user for data.
 * A data provider may ignore the specified {@link UserCallback}
 * if it's able to gather data without a user.
 */
public interface DataProvider {

  /**
   * Gathers data and updates the specified {@link ValueSet}.
   *
   * @param values The set of values to be updated.
   * @return This data provider.
   * @throws IOException If something went wrong.
   */
  DataProvider update(ValueSet values) throws IOException;

  /**
   * Sets an interface for interacting with a user.
   *
   * @param callback An interface for interacting with a user.
   * @return This data provider.
   */
  DataProvider set(UserCallback callback);
}
