package com.sap.oss.phosphor.fosstars.model.qa;

import com.sap.oss.phosphor.fosstars.model.Interval;
import com.sap.oss.phosphor.fosstars.model.Label;
import com.sap.oss.phosphor.fosstars.model.Rating;
import com.sap.oss.phosphor.fosstars.model.Score;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.value.ValueHashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A wrapper for a test vector.
 * The wrapper extends the original test vector with default values it doesn't have them.
 */
public class TestVectorWithDefaults implements TestVector {

  /**
   * An original test vector.
   */
  private final TestVector vector;

  /**
   * A set of default values.
   */
  private final Set<Value<?>> defaults;

  /**
   * Initializes a new wrapper.
   *
   * @param vector A test vector to be wrapped.
   * @param defaults A set of default values.
   */
  TestVectorWithDefaults(TestVector vector, Set<Value<?>> defaults) {
    Objects.requireNonNull(vector, "Hey! Vector can't be null!");
    Objects.requireNonNull(defaults, "Hey! Defaults can't be null!");
    this.vector = vector;
    this.defaults = defaults;
  }

  /**
   * Get the original test vector.
   *
   * @return The original test vector.
   */
  TestVector originalVector() {
    return vector;
  }

  @Override
  public Set<Value<?>> valuesFor(Rating rating) {
    return valuesFor(rating.score());
  }

  @Override
  public Set<Value<?>> valuesFor(Score score) {
    ValueHashSet values = new ValueHashSet(vector.valuesFor(score));
    return extendedWithDefaults(values);
  }

  /**
   * Extend a value set with the default values if necessary.
   *
   * @param values The value set to be extended.
   * @return An extended set of values.
   */
  private Set<Value<?>> extendedWithDefaults(ValueHashSet values) {
    for (Value<?> defaultValue : defaults) {
      if (!values.has(defaultValue.feature())) {
        values.update(defaultValue);
      }
    }
    return values.toSet();
  }

  @Override
  public Interval expectedScore() {
    return vector.expectedScore();
  }

  @Override
  public boolean hasLabel() {
    return vector.hasLabel();
  }

  @Override
  public Label expectedLabel() {
    return vector.expectedLabel();
  }

  @Override
  public String alias() {
    return vector.alias();
  }

  @Override
  public boolean expectsUnknownScore() {
    return vector.expectsUnknownScore();
  }

  @Override
  public boolean expectsNotApplicableScore() {
    return vector.expectsNotApplicableScore();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o instanceof TestVectorWithDefaults == false) {
      return false;
    }
    TestVectorWithDefaults that = (TestVectorWithDefaults) o;
    return Objects.equals(vector, that.vector)
        && Objects.equals(defaults, that.defaults);
  }

  @Override
  public int hashCode() {
    return Objects.hash(vector, defaults);
  }
}
