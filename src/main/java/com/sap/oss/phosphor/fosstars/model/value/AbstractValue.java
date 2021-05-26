package com.sap.oss.phosphor.fosstars.model.value;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import com.sap.oss.phosphor.fosstars.model.Feature;
import com.sap.oss.phosphor.fosstars.model.Value;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * A base class for feature values.
 *
 * @param <T> Type of data the the feature holds.
 * @param <V> Type of value.
 */
public abstract class AbstractValue<T, V extends AbstractValue<T, V>> implements Value<T>  {

  /**
   * A corresponding feature.
   */
  private final Feature<T> feature;

  /**
   * A list of notes that explain the value.
   */
  private final List<String> explanation;

  /**
   * Initializes a value for a feature.
   *
   * @param feature The feature.
   */
  AbstractValue(Feature<T> feature) {
    this(feature, Collections.emptyList());
  }

  /**
   * Initializes a value for a feature.
   *
   * @param feature The feature.
   * @param explanation A list of notes that explain the value.
   */
  AbstractValue(Feature<T> feature, List<String> explanation) {
    Objects.requireNonNull(feature, "Feature can't be null!");
    Objects.requireNonNull(explanation, "Explanation can't be null!");

    this.feature = feature;
    this.explanation = new ArrayList<>(explanation);
  }

  @Override
  @JsonGetter("feature")
  public final Feature<T> feature() {
    return feature;
  }

  /**
   * Set a list of explanations.
   *
   * @param explanation The explanations.
   * @return The same value.
   */
  @JsonSetter(value = "explanation", nulls = Nulls.AS_EMPTY)
  public V explanation(List<String> explanation) {
    this.explanation.clear();
    this.explanation.addAll(explanation);
    return (V) this;
  }

  @Override
  @JsonGetter("explanation")
  public List<String> explanation() {
    return new ArrayList<>(explanation);
  }

  @Override
  public V explain(String note, Object... params) {
    Objects.requireNonNull(note, "Note can't be null!");
    note = note.trim();
    if (note.isEmpty()) {
      throw new IllegalArgumentException("Note can't be empty!");
    }
    explanation.add(String.format(note, params));
    return (V) this;
  }

  @Override
  public Value<T> explainIf(Predicate<T> condition, String note, Object... params) {
    Objects.requireNonNull(condition, "Condition can't be null!");
    if (!isUnknown() && condition.test(get())) {
      explain(note, params);
    }
    return this;
  }

  @Override
  public Value<T> explainIf(T value, String note, Object... params) {
    Objects.requireNonNull(value, "Value can't be null!");
    if (!isUnknown() && get().equals(value)) {
      explain(note, params);
    }
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || !AbstractValue.class.isAssignableFrom(o.getClass())) {
      return false;
    }
    AbstractValue<?, ?> that = (AbstractValue<?, ?>) o;
    return Objects.equals(feature, that.feature) && Objects.equals(explanation, that.explanation);
  }

  @Override
  public int hashCode() {
    return Objects.hash(feature, explanation);
  }
}
