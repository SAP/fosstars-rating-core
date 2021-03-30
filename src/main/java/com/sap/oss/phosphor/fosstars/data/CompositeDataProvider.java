package com.sap.oss.phosphor.fosstars.data;

import com.sap.oss.phosphor.fosstars.model.Feature;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.ValueSet;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * This is a composite data provider which runs multiple data providers.
 * The composite data provider may be configured with a stop condition.
 * While running the underlying data providers, if the stop condition is satisfied,
 * then the execution of the data providers stops.
 */
public class CompositeDataProvider<T> implements DataProvider<T> {

  /**
   * This stop condition is always false.
   */
  public static final StopCondition NO_STOP_CONDITION = values -> false;

  /**
   * A cache of values.
   */
  protected ValueCache<T> cache = NoValueCache.create();

  /**
   * A list of underlying data providers.
   */
  private final List<DataProvider<T>> providers = new ArrayList<>();

  /**
   * A stop condition for the composite data provider.
   */
  private StopCondition stopCondition = NO_STOP_CONDITION;

  /**
   * Initializes a {@link CompositeDataProvider}.
   *
   * @param providers A number of underlying data providers. It can't be null or empty.
   */
  public CompositeDataProvider(DataProvider<T>... providers) {
    Objects.requireNonNull(providers, "Providers can't be null!");
    if (providers.length == 0) {
      throw new IllegalArgumentException("No providers specified!");
    }
    add(providers);
  }

  /**
   * Add an underlying data provider.
   *
   * @param providers The data provider to be added.
   * @return This provider.
   */
  public final CompositeDataProvider<T> add(DataProvider<T>... providers) {
    Objects.requireNonNull(providers, "Providers can't be null!");
    if (providers.length == 0) {
      throw new IllegalArgumentException("No providers specified!");
    }
    for (DataProvider<T> provider : providers) {
      if (provider.interactive()) {
        throw new IllegalArgumentException("Unfortunately interactive providers are not supported");
      }
      this.providers.add(provider);
    }
    return this;
  }

  @Override
  public CompositeDataProvider<T> update(T object, ValueSet values) throws IOException {
    Objects.requireNonNull(values, "Values can't be null!");
    for (DataProvider<T> provider : providers) {
      if (stopCondition.satisfied(values)) {
        break;
      }
      provider.update(object, values);
    }
    return this;
  }

  @Override
  public final boolean interactive() {
    return false;
  }

  @Override
  public ValueCache<T> cache() {
    return cache;
  }

  @Override
  public CompositeDataProvider<T> set(UserCallback callback) {
    Objects.requireNonNull(callback, "Callback can't be null!");
    for (DataProvider<?> provider : providers) {
      provider.set(callback);
    }
    return this;
  }

  @Override
  public CompositeDataProvider<T> set(ValueCache<T> cache) {
    Objects.requireNonNull(cache, "Hey! Cache can't be null!");
    for (DataProvider<T> provider : providers) {
      provider.set(cache);
    }
    return this;
  }

  @Override
  public Set<Feature<?>> supportedFeatures() {
    return providers.stream()
        .map(DataProvider::supportedFeatures)
        .flatMap(Set::stream)
        .collect(Collectors.toSet());
  }

  @Override
  public DataProvider<T> configure(Path config) {
    throw new UnsupportedOperationException("Oops! I don't support it!");
  }

  /**
   * Configure the composite data provider to stop when values for the specified features are found.
   *
   * @param features The features.
   * @return This data provider.
   */
  public CompositeDataProvider<T> stopWhenFilledOut(Feature<?>... features) {
    stopCondition(new AllFeaturesFilledOut(features));
    return this;
  }

  /**
   * Sets a stop condition for the data composite data provider.
   *
   * @param condition The stop condition.
   * @return This provider.
   */
  public CompositeDataProvider<T> stopCondition(StopCondition condition) {
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
  List<DataProvider<T>> providers() {
    return Collections.unmodifiableList(providers);
  }

  /**
   * This stop condition checks if all specified features have values.
   */
  public static class AllFeaturesFilledOut implements StopCondition {

    /**
     * A list of features.
     */
    private final List<Feature<?>> features = new ArrayList<>();

    /**
     * Initializes a stop condition with a number of features.
     *
     * @param features The features. It can't be null or empty.
     */
    AllFeaturesFilledOut(Feature<?>... features) {
      Objects.requireNonNull(features, "Features can't be null!");
      if (features.length == 0) {
        throw new IllegalArgumentException("No features provided!");
      }
      this.features.addAll(Arrays.asList(features));
    }

    @Override
    public boolean satisfied(ValueSet values) {
      for (Feature<?> feature : features) {
        Optional<? extends Value<?>> something = values.of(feature);
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
