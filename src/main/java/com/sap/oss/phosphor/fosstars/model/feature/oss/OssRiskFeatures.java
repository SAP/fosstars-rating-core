package com.sap.oss.phosphor.fosstars.model.feature.oss;

import com.sap.oss.phosphor.fosstars.model.feature.BooleanFeature;
import com.sap.oss.phosphor.fosstars.model.feature.DataConfidentialityType;
import com.sap.oss.phosphor.fosstars.model.feature.EnumFeature;
import com.sap.oss.phosphor.fosstars.model.feature.Impact;
import com.sap.oss.phosphor.fosstars.model.feature.Likelihood;
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
  public static final EnumFeature<Quantity> PROJECT_USAGE
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

  /**
   * Describes confidentiality of data that an open source project likely processes.
   */
  public static final EnumFeature<DataConfidentialityType> DATA_CONFIDENTIALITY
      = new EnumFeature<>(DataConfidentialityType.class,
      "Confidentiality of data that an open source project likely processes");

  /**
   * Describes potential impact to confidentiality of data in case of a security problem
   * in an open source project.
   */
  public static final EnumFeature<Impact> CONFIDENTIALITY_IMPACT
      = new EnumFeature<>(Impact.class,
      "Potential impact to confidentiality in case of a security problem");

  /**
   * Describes potential impact to integrity of data in case of a security problem
   * in an open source project.
   */
  public static final EnumFeature<Impact> INTEGRITY_IMPACT
      = new EnumFeature<>(Impact.class,
      "Potential impact to integrity in case of a security problem");

  /**
   * Describes potential impact to availability in case of a security problem
   * in an open source project.
   */
  public static final EnumFeature<Impact> AVAILABILITY_IMPACT
      = new EnumFeature<>(Impact.class,
      "Potential impact to availability in case of a security problem");
}
