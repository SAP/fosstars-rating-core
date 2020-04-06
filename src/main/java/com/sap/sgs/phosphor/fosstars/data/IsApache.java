package com.sap.sgs.phosphor.fosstars.data;

import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.IS_APACHE;

import com.sap.sgs.phosphor.fosstars.model.ValueSet;
import com.sap.sgs.phosphor.fosstars.model.value.BooleanValue;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The data provider tries to figure out if an open-source project belongs to the Apache Software
 * Foundation.
 */
public class IsApache implements DataProvider {

  /**
   * A logger.
   */
  private static final Logger LOGGER = LogManager.getLogger(IsApache.class);

  /**
   * An organization or username on GitHub.
   */
  private final String where;

  /**
   * Initializes a data provider.
   *
   * @param where An organization or username on GitHub.
   */
  public IsApache(String where) {
    this.where = where;
  }

  @Override
  public IsApache update(ValueSet values) {
    LOGGER.info("Figuring out if the project belongs to the Apache Software Foundation ...");
    values.update(new BooleanValue(IS_APACHE, "apache".equalsIgnoreCase(where)));
    return this;
  }

  @Override
  public DataProvider set(UserCallback callback) {
    return this;
  }

}
