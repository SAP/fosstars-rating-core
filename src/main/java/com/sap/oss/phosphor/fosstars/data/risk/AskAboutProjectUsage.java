package com.sap.oss.phosphor.fosstars.data.risk;

import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssRiskFeatures.PROJECT_USAGE;
import static java.util.Collections.singleton;

import com.sap.oss.phosphor.fosstars.data.interactive.AbstractInteractiveDataProvider;
import com.sap.oss.phosphor.fosstars.data.interactive.SelectFromEnum;
import com.sap.oss.phosphor.fosstars.model.Feature;
import com.sap.oss.phosphor.fosstars.model.Subject;
import com.sap.oss.phosphor.fosstars.model.ValueSet;
import com.sap.oss.phosphor.fosstars.model.feature.Quantity;
import java.util.Set;

/**
 * This data provider asks a user how many components uses a subject.
 */
public class AskAboutProjectUsage extends AbstractInteractiveDataProvider {

  @Override
  public Set<Feature<?>> supportedFeatures() {
    return singleton(PROJECT_USAGE);
  }

  @Override
  public boolean supports(Subject subject) {
    return true;
  }

  @Override
  protected AbstractInteractiveDataProvider ask(Subject subject, ValueSet values) {
    SelectFromEnum<Quantity> question
        = new SelectFromEnum<>(callback, "How many components use it?", Quantity.class);
    Quantity reply = question.ask();
    values.update(PROJECT_USAGE.value(reply));
    return this;
  }
}
