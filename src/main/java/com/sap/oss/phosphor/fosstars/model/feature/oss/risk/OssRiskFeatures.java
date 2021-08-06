package com.sap.oss.phosphor.fosstars.model.feature.oss.risk;

import com.sap.oss.phosphor.fosstars.model.feature.EnumFeature;
import com.sap.oss.phosphor.fosstars.model.feature.Quantity;

/**
 * The class contains features that are used in the security risk calculator
 * for open source projects.
 */
public class OssRiskFeatures {

  /**
   * Shows how many components such as applications, services, products, etc.
   * use an open source project.
   */
  public static final EnumFeature<Quantity> HOW_MANY_COMPONENTS_USE_OSS_PROJECT
      = new EnumFeature<>(Quantity.class, "How many components use an open source project");
}
