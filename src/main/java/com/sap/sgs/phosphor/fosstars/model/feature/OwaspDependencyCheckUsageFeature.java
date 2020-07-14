package com.sap.sgs.phosphor.fosstars.model.feature;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sap.sgs.phosphor.fosstars.model.Value;
import com.sap.sgs.phosphor.fosstars.model.value.OwaspDependencyCheckUsage;
import com.sap.sgs.phosphor.fosstars.model.value.OwaspDependencyCheckUsageValue;
import java.util.Objects;

/**
 * A feature which holds a {@link OwaspDependencyCheckUsage} value. Usage status mandatory,
 * optional or not found.
 */
public class OwaspDependencyCheckUsageFeature extends AbstractFeature<OwaspDependencyCheckUsage> {

  /**
   * Initializes a feature.
   *
   * @param name The feature name.
   */
  @JsonCreator
  public OwaspDependencyCheckUsageFeature(@JsonProperty("name") String name) {
    super(name);
  }

  /**
   * Creates a value of the features.
   *
   * @param value of type {@link OwaspDependencyCheckUsage}.
   * @return A value of the feature.
   */
  public Value<OwaspDependencyCheckUsage> value(OwaspDependencyCheckUsage value) {
    return new OwaspDependencyCheckUsageValue(this, value);
  }

  @Override
  public Value<OwaspDependencyCheckUsage> parse(String string) {
    Objects.requireNonNull(string, "String can't be null");
    return value(OwaspDependencyCheckUsage.valueOf(string));
  }
}