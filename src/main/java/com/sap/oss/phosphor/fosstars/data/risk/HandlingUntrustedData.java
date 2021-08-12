package com.sap.oss.phosphor.fosstars.data.risk;

import static com.sap.oss.phosphor.fosstars.model.feature.Likelihood.HIGH;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssRiskFeatures.HANDLING_UNTRUSTED_DATA_LIKELIHOOD;
import static java.util.Collections.singleton;

import com.sap.oss.phosphor.fosstars.data.AbstractCachingDataProvider;
import com.sap.oss.phosphor.fosstars.data.UserCallback;
import com.sap.oss.phosphor.fosstars.model.Feature;
import com.sap.oss.phosphor.fosstars.model.Subject;
import com.sap.oss.phosphor.fosstars.model.ValueSet;
import com.sap.oss.phosphor.fosstars.model.feature.oss.OssRiskFeatures;
import com.sap.oss.phosphor.fosstars.model.value.ValueHashSet;
import java.io.IOException;
import java.util.Set;

/**
 * This data provider is a composite data provider
 * that tries to figure out how likely a subject handles untrusted data.
 * It calls underlying data providers to fill out the feature
 * {@link OssRiskFeatures#HANDLING_UNTRUSTED_DATA_LIKELIHOOD}.
 * If they can't do that, the provider assigns the worst case.
 */
public class HandlingUntrustedData extends AbstractCachingDataProvider {

  /**
   * A data provider that can ask a user how likely a subject handles untrusted data.
   */
  private final AskAboutHandlingUntrustedData askAboutHandlingUntrustedData
      = new AskAboutHandlingUntrustedData();

  @Override
  public Set<Feature<?>> supportedFeatures() {
    return singleton(HANDLING_UNTRUSTED_DATA_LIKELIHOOD);
  }

  @Override
  protected ValueSet fetchValuesFor(Subject subject) throws IOException {
    ValueSet values = new ValueHashSet();

    if (callback.canTalk()) {
      askAboutHandlingUntrustedData.update(subject, values);
    } else {
      values.update(HANDLING_UNTRUSTED_DATA_LIKELIHOOD.value(HIGH)
          .explain("Assumed the worst case"));
    }

    return values;
  }

  @Override
  public HandlingUntrustedData set(UserCallback callback) {
    super.set(callback);
    askAboutHandlingUntrustedData.set(callback);
    return this;
  }

  @Override
  public boolean interactive() {
    return false;
  }

  @Override
  public boolean supports(Subject subject) {
    return true;
  }
}
