package com.sap.oss.phosphor.fosstars.data.risk;

import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssRiskFeatures.HANDLING_UNTRUSTED_DATA_LIKELIHOOD;
import static java.util.Collections.singleton;

import com.sap.oss.phosphor.fosstars.data.interactive.AbstractInteractiveDataProvider;
import com.sap.oss.phosphor.fosstars.data.interactive.SelectFromEnum;
import com.sap.oss.phosphor.fosstars.model.Feature;
import com.sap.oss.phosphor.fosstars.model.Subject;
import com.sap.oss.phosphor.fosstars.model.ValueSet;
import com.sap.oss.phosphor.fosstars.model.feature.Likelihood;
import java.util.Set;

/**
 * This data provider asks a user how likely a subject handles untrusted data.
 */
public class AskAboutHandlingUntrustedData extends AbstractInteractiveDataProvider {

  @Override
  public Set<Feature<?>> supportedFeatures() {
    return singleton(HANDLING_UNTRUSTED_DATA_LIKELIHOOD);
  }

  @Override
  public boolean supports(Subject subject) {
    return true;
  }

  @Override
  protected AbstractInteractiveDataProvider ask(Subject subject, ValueSet values) {
    SelectFromEnum<Likelihood> question = new SelectFromEnum<>(
        callback, "How likely does it handle untrusted data?", Likelihood.class);
    Likelihood reply = question.ask();
    values.update(HANDLING_UNTRUSTED_DATA_LIKELIHOOD.value(reply));
    return this;
  }
}
