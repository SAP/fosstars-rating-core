package com.sap.oss.phosphor.fosstars.data.risk;

import static com.sap.oss.phosphor.fosstars.model.feature.oss.Functionality.OTHER;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssRiskFeatures.FUNCTIONALITY;
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
 * that tries to figure out what kind of functionality a subject provides.
 * It calls underlying data providers to fill out the feature
 * {@link com.sap.oss.phosphor.fosstars.model.feature.oss.OssRiskFeatures#FUNCTIONALITY}.
 * If they can't do that, the provider assigns a default value.
 */
public class ProjectFunctionality extends AbstractCachingDataProvider {

  /**
   * A data provider that can ask a user about functionality.
   */
  private final AskAboutProjectFunctionality askAboutProjectFunctionality
      = new AskAboutProjectFunctionality();

  @Override
  public Set<Feature<?>> supportedFeatures() {
    return singleton(FUNCTIONALITY);
  }

  @Override
  protected ValueSet fetchValuesFor(Subject subject) throws IOException {
    ValueSet values = new ValueHashSet();

    if (callback.canTalk()) {
      askAboutProjectFunctionality.update(subject, values);
    } else {
      values.update(FUNCTIONALITY.value(OTHER).explain("Just an assumption"));
    }

    return values;
  }

  @Override
  public ProjectFunctionality set(UserCallback callback) {
    super.set(callback);
    askAboutProjectFunctionality.set(callback);
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
