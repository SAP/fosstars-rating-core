package com.sap.sgs.phosphor.fosstars.data;

import com.sap.sgs.phosphor.fosstars.model.Feature;
import com.sap.sgs.phosphor.fosstars.model.Value;
import com.sap.sgs.phosphor.fosstars.model.ValueSet;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * This is a composite data provider which runs multiple data providers.
 * The composite data provider may be configured with a stop condition.
 * While running the underlying data providers, if the stop condition is satisfied,
 * then the execution of the data providers stops.
 */
public class CompositeDataProvider implements DataProvider {

  public static final StopCondition NO_STOP_CONDITION = values -> false;

  /**
   * A list of underlying data providers.
   */
  private final List<DataProvider> providers = new ArrayList<>();

  /**
   * A stop condition for the composite data provider.
   */
  private StopCondition stopCondition = NO_STOP_CONDITION;

  /**
   * @param providers Underlying data providers.
   */
  public CompositeDataProvider(DataProvider... providers) {
    Objects.requireNonNull(providers, "Providers can't be null!");
    if (providers.length == 0) {
      throw new IllegalArgumentException("No providers specified!");
    }
    this.providers.addAll(Arrays.asList(providers));
  }

  /**
   * Add an underlying data provider.
   *
   * @param providers The data provider to be added.
   * @return This provider.
   */
  public CompositeDataProvider add(DataProvider... providers) {
    Objects.requireNonNull(providers, "Providers can't be null!");
    if (providers.length == 0) {
      throw new IllegalArgumentException("No providers specified!");
    }
    this.providers.addAll(Arrays.asList(providers));
    return this;
  }

  @Override
  public CompositeDataProvider update(ValueSet values) throws IOException {
    Objects.requireNonNull(values, "Values can't be null!");
    for (DataProvider provider : providers) {
      if (stopCondition.satisfied(values)) {
        break;
      }
      provider.update(values);
    }
    return this;
  }

  @Override
  public CompositeDataProvider set(UserCallback callback) {
    Objects.requireNonNull(callback, "Callback can't be null!");
    for (DataProvider provider : providers) {
      provider.set(callback);
    }
    return this;
  }

  /**
   * Configure the composite data provider to stop when values for the specified features are found.
   *
   * @param features The features.
   * @return This data provider.
   */
  public CompositeDataProvider stopWhenFilledOut(Feature... features) {
    stopCondition(new AllFeaturesFilledOut(features));
    return this;
  }

  /**
   * Sets a stop condition for the data composite data provider.
   *
   * @param condition The stop condition.
   * @return This provider.
   */
  public DataProvider stopCondition(StopCondition condition) {
    this.stopCondition = condition;
    return this;
  }

  public interface StopCondition {
    boolean satisfied(ValueSet values);
  }

  /**
   * Returns an unmodifiable list of underlying data providers.
   *
   * @return A list of underlying data providers.
   */
  List<DataProvider> providers() {
    return Collections.unmodifiableList(providers);
  }

  public static class AllFeaturesFilledOut implements StopCondition {

    private final List<Feature> features = new ArrayList<>();

    public AllFeaturesFilledOut(Feature... features) {
      Objects.requireNonNull(features, "Features can't be null!");
      if (features.length == 0) {
        throw new IllegalArgumentException("No features provided!");
      }
      this.features.addAll(Arrays.asList(features));
    }

    @Override
    public boolean satisfied(ValueSet values) {
      for (Feature feature : features) {
        Optional<Value> something = values.of(feature);
        if (!something.isPresent()) {
          return false;
        }
        if (something.get().isUnknown()) {
          return false;
        }
      }
      return true;
    }
  }
}
