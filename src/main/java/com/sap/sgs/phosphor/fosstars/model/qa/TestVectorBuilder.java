package com.sap.sgs.phosphor.fosstars.model.qa;

import com.sap.sgs.phosphor.fosstars.model.Feature;
import com.sap.sgs.phosphor.fosstars.model.Interval;
import com.sap.sgs.phosphor.fosstars.model.Label;
import com.sap.sgs.phosphor.fosstars.model.Value;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A builder for test vectors.
 *
 * @see TestVector
 */
public class TestVectorBuilder {

  private final Set<Value> values = new HashSet<>();
  private Interval expectedScore;
  private Label expectedLabel;
  private String description = "unknown";

  /**
   * Creates a new test vector.
   */
  public static TestVectorBuilder newTestVector() {
    return new TestVectorBuilder();
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
    Objects.requireNonNull(interval, "Hey! You have to give me an internal but not null!");
    expectedScore = interval;
    return this;
  }

  /**
   * Set an expected label.
   *
   * @param label A label to be set.
   * @return This instance of TestVectorBuilder.
   */
  public TestVectorBuilder expectedLabel(Label label) {
    Objects.requireNonNull(label, "Hey! You have to give me a label but not null!");
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
  public TestVectorBuilder set(Set<Value> values) {
    Objects.requireNonNull(values, "Hey! You have to give me a set of values but not a null!");
    for (Value value : values) {
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
  public TestVectorBuilder set(Value value) {
    Objects.requireNonNull(value, "Hey! You have to give me a feature value but not null");
    boolean added = values.add(value);
    if (!added) {
      throw new IllegalArgumentException(String.format(
          "Hey! You set the same value for the feature '%s'", value.feature().name()));
    }
    return this;
  }

  /**
   * Set a description.
   *
   * @param text The description.
   * @return This instance of TestVectorBuilder.
   */
  public TestVectorBuilder description(String text) {
    Objects.requireNonNull(text, "Hey! You have to give me a description but not null!");
    description = text;
    return this;
  }

  /**
   * Create an instance of {@link TestVector} with specified parameters.
   *
   * @return An instance of TestVector.
   */
  public TestVector make() {
    Objects.requireNonNull(expectedScore,
        "Oh no! Looks like you forgot to tell me about an expected score!");
    if (values.isEmpty()) {
      throw new IllegalArgumentException(
          "Oh no! Looks like you forgot to give me features values!");
    }
    return new TestVector(
        Collections.unmodifiableSet(values), expectedScore, expectedLabel, description);
  }

}
