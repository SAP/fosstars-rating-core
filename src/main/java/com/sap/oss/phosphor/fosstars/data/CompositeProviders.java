package com.sap.oss.phosphor.fosstars.data;

import static com.sap.oss.phosphor.fosstars.model.feature.Likelihood.HIGH;
import static com.sap.oss.phosphor.fosstars.model.feature.Quantity.A_LOT;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.Functionality.OTHER;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssRiskFeatures.FUNCTIONALITY;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssRiskFeatures.HANDLING_UNTRUSTED_DATA_LIKELIHOOD;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssRiskFeatures.PROJECT_USAGE;

import com.sap.oss.phosphor.fosstars.data.interactive.AskOptions;
import com.sap.oss.phosphor.fosstars.model.feature.Likelihood;
import com.sap.oss.phosphor.fosstars.model.feature.Quantity;
import com.sap.oss.phosphor.fosstars.model.feature.oss.Functionality;

/**
 * The class holds composite data providers for various features.
 */
public class CompositeProviders {

  /**
   * This data provider is a composite data provider
   * that tries to figure out how much a subject is used.
   */
  private static final SimpleCompositeDataProvider PROJECT_USAGE_PROVIDER
      = SimpleCompositeDataProvider.forFeature(PROJECT_USAGE)
          .withInteractiveProvider(
              AskOptions.forFeature(PROJECT_USAGE)
                  .withQuestion("How many components use it?")
                  .withOptions(Quantity.class))
          .withDefaultValue(PROJECT_USAGE.value(A_LOT)
              .explain("Assume that the subject is used a lot (the worst case)"));

  /**
   * This data provider is a composite data provider
   * that tries to figure out what kind of functionality a subject provides.
   */
  private static final SimpleCompositeDataProvider FUNCTIONALITY_PROVIDER
      = SimpleCompositeDataProvider.forFeature(FUNCTIONALITY)
          .withInteractiveProvider(
              AskOptions.forFeature(FUNCTIONALITY)
                  .withQuestion("What kind of functionality does it provide?")
                  .withOptions(Functionality.class))
          .withDefaultValue(FUNCTIONALITY.value(OTHER).explain("Just an assumption"));

  /**
   * This data provider is a composite data provider
   * that tries to figure out what kind of functionality a subject provides.
   */
  private static final SimpleCompositeDataProvider HANDLING_UNTRUSTED_DATA_LIKELIHOOD_PROVIDER
      = SimpleCompositeDataProvider.forFeature(HANDLING_UNTRUSTED_DATA_LIKELIHOOD)
      .withInteractiveProvider(
          AskOptions.forFeature(HANDLING_UNTRUSTED_DATA_LIKELIHOOD)
              .withQuestion("How likely does it handle untrusted data?")
              .withOptions(Likelihood.class))
      .withDefaultValue(HANDLING_UNTRUSTED_DATA_LIKELIHOOD.value(HIGH)
          .explain("Assumed the worst case"));
}
