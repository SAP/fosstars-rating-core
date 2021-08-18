package com.sap.oss.phosphor.fosstars.data;

import static java.util.Collections.singleton;
import static java.util.Objects.requireNonNull;

import com.sap.oss.phosphor.fosstars.model.Feature;
import com.sap.oss.phosphor.fosstars.model.Subject;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.ValueSet;
import com.sap.oss.phosphor.fosstars.model.value.ValueHashSet;
import java.io.IOException;
import java.util.Set;
import java.util.function.Predicate;

/**
 * A composite data provider that contains a non-interactive and an interactive data providers.
 * First, it looks for a value in a cache. If no value found in the cache,
 * it calls a non-interactive provider. If the provider could not obtain the value,
 * it calls the interactive provider. If it could not obtain the value either,
 * it returns a default value.
 */
public class SimpleCompositeDataProvider extends AbstractCachingDataProvider {

  /**
   * This predicate return true for all subject.
   */
  private static final Predicate<Subject> SUPPORTS_ALL_SUBJECT = subject -> true;

  /**
   * A feature that the provider supports.
   */
  private final Feature<?> feature;

  /**
   * A predicate the implements the {@link DataProvider#supports(Subject)} method.
   */
  private final Predicate<Subject> support;

  /**
   * A non-interactive data provider.
   */
  private final DataProvider nonInteractiveProvider;

  /**
   * An interactive data provider.
   */
  private final DataProvider interactiveProvider;

  /**
   * A default value.
   */
  private final Value<?> defaultValue;

  /**
   * Create a new data provider.
   *
   * @param feature A feature that the provider supports.
   * @param support A predicate the implements the {@link DataProvider#supports(Subject)} method.
   * @param nonInteractiveProvider A non-interactive data provider.
   * @param interactiveProvider An interactive data provider.
   * @param defaultValue A default value.
   */
  public SimpleCompositeDataProvider(Feature<?> feature, Predicate<Subject> support,
      DataProvider nonInteractiveProvider, DataProvider interactiveProvider,
      Value<?> defaultValue) {

    requireNonNull(feature, "On no! Supported feature can't be null!");
    requireNonNull(defaultValue, "On no! Default value can't be null!");

    if (!defaultValue.feature().equals(feature)) {
      throw new IllegalArgumentException(
          "Oh no! The default value doesn't match with the supported feature!");
    }

    if (nonInteractiveProvider != null) {
      if (!nonInteractiveProvider.supportedFeatures().contains(feature)) {
        throw new IllegalArgumentException(
            "Oh no! The non-interactive data provider supports unexpected features!");
      }

      if (nonInteractiveProvider.interactive()) {
        throw new IllegalArgumentException("Oh no! Non-interactive provider is interactive!");
      }
    }

    if (interactiveProvider != null) {
      if (!interactiveProvider.supportedFeatures().contains(feature)) {
        throw new IllegalArgumentException(
            "Oh no! The interactive data provider supports unexpected features!");
      }

      if (!interactiveProvider.interactive()) {
        throw new IllegalArgumentException("Oh no! Interactive provider is not interactive!");
      }
    }

    this.feature = feature;
    this.support = support != null ? support : SUPPORTS_ALL_SUBJECT;
    this.nonInteractiveProvider = nonInteractiveProvider;
    this.interactiveProvider = interactiveProvider;
    this.defaultValue = defaultValue;
  }

  @Override
  protected ValueSet fetchValuesFor(Subject subject) throws IOException {
    ValueSet values = new ValueHashSet();

    if (nonInteractiveProvider != null) {
      nonInteractiveProvider.update(subject, values);
      if (weAreHappyWith(values)) {
        return values;
      }
    }

    if (interactiveProvider != null && callback.canTalk()) {
      interactiveProvider.update(subject, values);
      if (weAreHappyWith(values)) {
        return values;
      }
    }

    values.update(defaultValue);
    return values;
  }

  @Override
  public boolean interactive() {
    return false;
  }

  @Override
  public Set<Feature<?>> supportedFeatures() {
    return singleton(feature);
  }

  @Override
  public boolean supports(Subject subject) {
    return support.test(subject);
  }

  @Override
  public SimpleCompositeDataProvider set(UserCallback callback) {
    super.set(callback);
    interactiveProvider.set(callback);
    return this;
  }

  /**
   * Check if a value set contains a known value for the supported feature.
   *
   * @param values The value set.
   * @return True if the value set contains a known value for the supported feature,
   *         false otherwise.
   */
  private boolean weAreHappyWith(ValueSet values) {
    return values.of(feature).map(value -> !value.isUnknown()).orElse(false);
  }

  /**
   * Crete a builder for configuring a new data provider.
   *
   * @param feature A feature that the provider should support.
   * @return A builder.
   */
  public static Builder forFeature(Feature<?> feature) {
    return new Builder(feature);
  }

  /**
   * A builder for configuring a data provider.
   */
  public static class Builder {

    /**
     * A feature that the provider should support.
     */
    private final Feature<?> feature;

    /**
     * A non-interactive data provider.
     */
    private DataProvider nonInteractiveProvider;

    /**
     * An interactive data provider.
     */
    private DataProvider interactiveProvider;

    /**
     * Create a new builder.
     *
     * @param feature A feature that the provider should support.
     */
    private Builder(Feature<?> feature) {
      this.feature = requireNonNull(feature, "Oops! Feature is nul!");
    }

    /**
     * Set an interactive data provider.
     *
     * @param provider The provider.
     * @return This builder.
     */
    public Builder withInteractiveProvider(DataProvider provider) {
      this.interactiveProvider
          = requireNonNull(provider, "Hey! If you set a provider, it has to be non-null!");
      return this;
    }

    /**
     * Set a non-interactive data provider.
     *
     * @param provider The provider.
     * @return This builder.
     */
    public Builder withNonInteractiveProvider(DataProvider provider) {
      this.nonInteractiveProvider
          = requireNonNull(provider, "Hey! If you set a provider, it should not be null!");
      return this;
    }

    /**
     * Create a configured data provider with a default value.
     *
     * @param value The default value.
     * @return A configured data provider.
     */
    public SimpleCompositeDataProvider withDefaultValue(Value<?> value) {
      requireNonNull(value, "Hey! If you set a default value, it should not be null!");
      return new SimpleCompositeDataProvider(
          feature, SUPPORTS_ALL_SUBJECT, nonInteractiveProvider, interactiveProvider, value);
    }
  }
}
