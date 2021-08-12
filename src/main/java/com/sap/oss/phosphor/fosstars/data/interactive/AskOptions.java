package com.sap.oss.phosphor.fosstars.data.interactive;

import static java.util.Collections.singleton;
import static java.util.Objects.requireNonNull;

import com.sap.oss.phosphor.fosstars.model.Feature;
import com.sap.oss.phosphor.fosstars.model.Subject;
import com.sap.oss.phosphor.fosstars.model.ValueSet;
import java.util.Set;

/**
 * This data provider asks a question and offers options.
 */
public class AskOptions<T extends Enum<T>> extends AbstractInteractiveDataProvider {

  /**
   * A feature that the data provider supports.
   */
  private final Feature<T> feature;

  /**
   * A question that the data provider asks.
   */
  private final String question;

  /**
   * A class of an enum that contains possible answers.
   */
  private final Class<T> enumClass;

  /**
   * Initialize a new data provider.
   *
   * @param feature A feature that the data provider supports.
   * @param question A question that the data provider asks.
   * @param enumClass A class of an enum that contains possible answers.
   */
  public AskOptions(Feature<T> feature, String question, Class<T> enumClass) {
    this.feature = requireNonNull(feature, "Oops! Feature is null!");
    this.question = requireNonNull(question, "Oops! Question is null!");
    this.enumClass = requireNonNull(enumClass, "Oops! Options is null!");
  }

  @Override
  public Set<Feature<?>> supportedFeatures() {
    return singleton(feature);
  }

  @Override
  public boolean supports(Subject subject) {
    return true;
  }

  @Override
  protected AbstractInteractiveDataProvider ask(Subject subject, ValueSet values) {
    SelectFromEnum<T> select = new SelectFromEnum<>(callback, question, enumClass);
    T reply = select.ask();
    values.update(feature.value(reply));
    return this;
  }

  /**
   * Create a builder for configuring a data provider.
   *
   * @param feature A feature that the provider should support.
   * @param <T> A type of data that the feature holds.
   * @return A builder.
   */
  public static <T extends Enum<T>> Builder<T> forFeature(Feature<T> feature) {
    return new Builder<>(feature);
  }

  /**
   * A builder for configuring a data provider.
   *
   * @param <T> A type of data that the feature holds.
   */
  public static class Builder<T extends Enum<T>> {

    /**
     * A feature that the provider should support.
     */
    private final Feature<T> feature;

    /**
     * A question that the provider asks.
     */
    private String question;

    /**
     * Create a new builder.
     *
     * @param feature A feature that the provider should support.
     */
    private Builder(Feature<T> feature) {
      this.feature = feature;
    }

    /**
     * Set a question for the provider.
     *
     * @param question The question.
     * @return This builder.
     */
    public Builder<T> withQuestion(String question) {
      this.question = question;
      return this;
    }

    /**
     * Set possible answers and create a data provider.
     *
     * @param options The answers.
     * @return A configured data provider.
     */
    public AskOptions<T> withOptions(Class<T> options) {
      return new AskOptions<>(feature, question, options);
    }
  }
}
