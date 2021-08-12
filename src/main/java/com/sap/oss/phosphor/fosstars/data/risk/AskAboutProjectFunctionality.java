package com.sap.oss.phosphor.fosstars.data.risk;

import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssRiskFeatures.FUNCTIONALITY;
import static java.util.Collections.singleton;

import com.sap.oss.phosphor.fosstars.data.interactive.AbstractInteractiveDataProvider;
import com.sap.oss.phosphor.fosstars.data.interactive.SelectFromEnum;
import com.sap.oss.phosphor.fosstars.model.Feature;
import com.sap.oss.phosphor.fosstars.model.Subject;
import com.sap.oss.phosphor.fosstars.model.ValueSet;
import com.sap.oss.phosphor.fosstars.model.feature.oss.Functionality;
import java.util.Set;

/**
 * This data provider asks a user about functionality that a subject provides.
 */
public class AskAboutProjectFunctionality extends AbstractInteractiveDataProvider {

  @Override
  public Set<Feature<?>> supportedFeatures() {
    return singleton(FUNCTIONALITY);
  }

  @Override
  public boolean supports(Subject subject) {
    return true;
  }

  @Override
  protected AbstractInteractiveDataProvider ask(Subject subject, ValueSet values) {
    SelectFromEnum<Functionality> question = new SelectFromEnum<>(
        callback, "What kind of functionality does it provide?", Functionality.class);
    Functionality reply = question.ask();
    values.update(FUNCTIONALITY.value(reply));
    return this;
  }
}
