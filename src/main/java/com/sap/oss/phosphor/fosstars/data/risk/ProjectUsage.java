package com.sap.oss.phosphor.fosstars.data.risk;

import static com.sap.oss.phosphor.fosstars.model.feature.Quantity.A_LOT;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssRiskFeatures.PROJECT_USAGE;
import static java.util.Collections.singleton;

import com.sap.oss.phosphor.fosstars.data.AbstractCachingDataProvider;
import com.sap.oss.phosphor.fosstars.data.UserCallback;
import com.sap.oss.phosphor.fosstars.model.Feature;
import com.sap.oss.phosphor.fosstars.model.Subject;
import com.sap.oss.phosphor.fosstars.model.ValueSet;
import com.sap.oss.phosphor.fosstars.model.value.ValueHashSet;
import java.io.IOException;
import java.util.Set;

/**
 * This data provider is a composite data provider
 * that tries to figure out how much a subject is used.
 * It calls underlying data providers to fill out the feature
 * {@link com.sap.oss.phosphor.fosstars.model.feature.oss.OssRiskFeatures#PROJECT_USAGE}.
 * If they can't do that, the provider assigns a default value.
 */
public class ProjectUsage extends AbstractCachingDataProvider {

  /**
   * A data provider that can ask a user how much a subject is used.
   */
  private final AskAboutProjectUsage askAboutProjectUsage = new AskAboutProjectUsage();

  @Override
  public Set<Feature<?>> supportedFeatures() {
    return singleton(PROJECT_USAGE);
  }

  @Override
  protected ValueSet fetchValuesFor(Subject subject) throws IOException {
    ValueSet values = new ValueHashSet();

    if (callback.canTalk()) {
      askAboutProjectUsage.update(subject, values);
    } else {
      values.update(PROJECT_USAGE.value(A_LOT)
          .explain("Assume that the subject is used a lot (the worst case)"));
    }

    return values;
  }

  @Override
  public ProjectUsage set(UserCallback callback) {
    super.set(callback);
    askAboutProjectUsage.set(callback);
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
