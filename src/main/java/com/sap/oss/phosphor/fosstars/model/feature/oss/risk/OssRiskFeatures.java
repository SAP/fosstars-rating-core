package com.sap.oss.phosphor.fosstars.model.feature.oss.risk;

import com.sap.oss.phosphor.fosstars.model.Feature;
import com.sap.oss.phosphor.fosstars.model.feature.BooleanFeature;
import com.sap.oss.phosphor.fosstars.model.feature.EnumFeature;
import com.sap.oss.phosphor.fosstars.model.feature.Likelihood;
import com.sap.oss.phosphor.fosstars.model.feature.Quantity;
import com.sap.oss.phosphor.fosstars.model.feature.oss.Functionality;

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

  /**
   * Shows what kind of functionality an open source project offers.
   */
  public static final EnumFeature<Functionality> FUNCTIONALITY
      = new EnumFeature<>(Functionality.class, "What kind of functionality an open project offers");

  /**
   * Shows how likely an open source project handles untrusted data.
   */
  public static final EnumFeature<Likelihood> HANDLING_UNTRUSTED_DATA_LIKELIHOOD
      = new EnumFeature<>(
          Likelihood.class, "How likely an open source project handles untrusted data");

  /**
   * Shows whether an open source project has been adopted by a team.
   */
  public static final BooleanFeature IS_ADOPTED
      = new BooleanFeature("An open source project has been adopted by a team");
}
