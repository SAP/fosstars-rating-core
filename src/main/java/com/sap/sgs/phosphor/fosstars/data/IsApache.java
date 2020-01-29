package com.sap.sgs.phosphor.fosstars.data;

import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.IS_APACHE;

import com.sap.sgs.phosphor.fosstars.model.Value;
import com.sap.sgs.phosphor.fosstars.model.value.BooleanValue;

/**
 * The data provider tries to figure out if an open-source project belongs to the Apache Software
 * Foundation.
 */
public class IsApache implements DataProvider {

  /**
   * A GitHub organization.
   */
  private final String where;

  /**
   * @param where A GitHub organization.
   */
  public IsApache(String where) {
    this.where = where;
  }

  @Override
  public Value<Boolean> get(UserCallback callback) {
    System.out.println("[+] Figuring out if the project belongs to the Apache Software Foundation ...");
    return new BooleanValue(IS_APACHE, "apache".equalsIgnoreCase(where));
  }

  @Override
  public boolean mayTalk() {
    return false;
  }
}
