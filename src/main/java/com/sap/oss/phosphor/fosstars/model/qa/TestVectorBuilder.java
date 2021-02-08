package com.sap.oss.phosphor.fosstars.model.qa;

import com.sap.oss.phosphor.fosstars.model.Feature;
import com.sap.oss.phosphor.fosstars.model.Interval;
import com.sap.oss.phosphor.fosstars.model.Label;
import com.sap.oss.phosphor.fosstars.model.Value;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A builder for test vectors.
 *
 * @see StandardTestVector
 */
public class TestVectorBuilder {

  /**
   * A set of feature values.
   */
  private final Set<Value<?>> values = new HashSet<>();

  /**
   * An interval for an expected score.
   */
  private Interval expectedScore;

  /**
   * If it's set to true, then an unknown score value is expected.
   */
  private boolean expectUnknownScore = false;

  /**
   * If it's set to true, then a not-applicable score value is expected.
   */
  private boolean expectNotApplicableScore = false;

  /**
   * An expected label.
   */
  private Label expectedLabel = TestVector.NO_LABEL;

  /**
   * An alias.
   */
  private String alias;

  /**
   * Creates a new test vector builder.
   *
   * @return A new builder.
   */
  public static TestVectorBuilder newTestVector() {
    return new TestVectorBuilder();
  }

  /**
   * Creates a new test vector builder.
   *
   * @param alias An alias for the test vector.
   * @return A new builder.
   */
  public static TestVectorBuilder newTestVector(String alias) {
    return newTestVector().alias(alias);
  }

  /**
   * Private constructor. Please use the {@link #newTestVector()} method to create an instance.
   */
  private TestVectorBuilder() {

  }

  /**
   * Set an expected interval for a score.
   *
   * @param interval An expected interval to be set.
   * @return This instance of TestVectorBuilder.
   */
  public TestVectorBuilder expectedScore(Interval interval) {
    expectedScore = interval;
    return this;
  }

  /**
   * Makes a test vector to expect an unknown value.
   *
   * @return This instance of TestVectorBuilder.
   */
  public TestVectorBuilder expectUnknownScore() {
    expectUnknownScore = true;
    return this;
  }

  /**
   * Makes a test vector to expect a not-applicable score value.
   *
   * @return This instance of TestVectorBuilder.
   */
  public TestVectorBuilder expectNotApplicableScore() {
    expectNotApplicableScore = true;
    return this;
  }

  /**
   * Set an expected label.
   *
   * @param label A label to be set.
   * @return This instance of TestVectorBuilder.
   */
  public TestVectorBuilder expectedLabel(Label label) {
    expectedLabel = label;
    return this;
  }

  /**
   * Set a value for a feature.
   *
   * @param feature The feature.
   * @param object The value.
   * @param <T> Type of the feature.
   * @return This instance of TestVectorBuilder.
   */
  public <T> TestVectorBuilder set(Feature<T> feature, T object) {
    Objects.requireNonNull(feature, "Hey! Feature can't be null!");
    Objects.requireNonNull(object, "Hey! Feature can't be null!");
    return set(feature.value(object));
  }

  /**
   * Set a set of values.
   *
   * @param values A set of values to be set.
   * @return This instance of TestVectorBuilder.
   */
  public TestVectorBuilder set(Set<Value<?>> values) {
    Objects.requireNonNull(values, "Hey! You have to give me a set of values but not a null!");
    for (Value<?> value : values) {
      set(value);
    }
    return this;
  }

  /**
   * Set a feature value.
   *
   * @param value A value to be set.
   * @return This instance of TestVectorBuilder.
   */
  public TestVectorBuilder set(Value<?> value) {
    Objects.requireNonNull(value, "Hey! You have to give me a feature value but not null");
    boolean added = values.add(value);
    if (!added) {
      throw new IllegalArgumentException(String.format(
          "Hey! You set the same value for the feature '%s'", value.feature().name()));
    }
    return this;
  }

  /**
   * Set an alias.
   *
   * @param alias The alias.
   * @return This instance of TestVectorBuilder.
   */
  public TestVectorBuilder alias(String alias) {
    Objects.requireNonNull(alias, "Hey! You have to give me an alias but not null!");
    this.alias = alias;
    return this;
  }

  /**
   * Create an instance of {@link StandardTestVector} with specified parameters.
   *
   * @return An instance of TestVector.
   */
  public StandardTestVector make() {
    if (expectedScore == null && !expectNotApplicableScore && !expectUnknownScore) {
      throw new IllegalArgumentException("Hey! Expected score can't be null "
          + "unless a not-applicable or unknown value is expected!");
    }

    if (values.isEmpty()) {
      throw new IllegalArgumentException(
          "Oh no! Looks like you forgot to give me features values!");
    }

    return new StandardTestVector(Collections.unmodifiableSet(values), expectedScore, expectedLabel,
        alias, expectUnknownScore, expectNotApplicableScore);
  }

}
