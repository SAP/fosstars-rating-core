package com.sap.oss.phosphor.fosstars.model;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import java.util.List;
import java.util.function.Predicate;

/**
 * An interface for a feature value of specific type.
 *
 * @param <T> Type of data that the feature holds.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
public interface Value<T> {

  /**
   * Get a feature which the value is for.
   *
   * @return A feature which the value is for.
   */
  Feature<T> feature();

  /**
   * Tells if the value is unknown.
   *
   * @return True if the value is unknown, false otherwise.
   */
  boolean isUnknown();

  /**
   * Tells if the value is N/A.
   *
   * @return True if the value is not applicable in the current context, false otherwise.
   */
  boolean isNotApplicable();

  /**
   * Get explanations for the value.
   *
   * @return A list of notes that explain the value.
   */
  List<String> explanation();

  /**
   * Add a note that explains the value.
   *
   * @param note The note to be added. It may be a format string.
   * @param params A number of parameters if a format string is passed.
   * @return The same value.
   */
  Value<T> explain(String note, Object... params);

  /**
   * Add a note that explains the value if a condition is satisfied.
   *
   * @param condition The condition.
   * @param note The note to be added if the condition is satisfied. It may be a format string.
   * @param params A number of parameters if a format string is passed.
   * @return The same value.
   */
  Value<T> explainIf(Predicate<T> condition, String note, Object... params);

  /**
   * Add a note that explains the value if it is equal to the expected data.
   *
   * @param value The expected data.
   * @param note The note to be added if the value is equal to the specified one.
   *             It may be a format string.
   * @param params A number of parameters if a format string is passed.
   * @return The same value.
   */
  Value<T> explainIf(T value, String note, Object... params);

  /**
   * Get the value.
   *
   * @return The value.
   * @throws IllegalStateException If the value is unknown.
   */
  T get();

  /**
   * Returns the value if it is known and applicable,
   * otherwise a specified default value.
   *
   * @param other The default value.
   * @return The value of the default value.
   */
  T orElse(T other);

  /**
   * Call a processor to process the value if it's known.
   *
   * @param processor The processor to be called.
   * @return This Value instance.
   */
  Value<T> processIfKnown(Processor<T> processor);

  /**
   * Call a processor to process the value if it's unknown.
   *
   * @param processor The processor to be called.
   * @return This Value instance.
   */
  Value<T> processIfUnknown(Runnable processor);

  /**
   * An interface of a processor which can process a value.
   *
   * @param <T> A type of a value.
   */
  @FunctionalInterface
  interface Processor<T> {

    /**
     * Process a value.
     *
     * @param value The value to be processed.
     */
    void process(T value);
  }
}
