package com.sap.oss.phosphor.fosstars.data.interactive;

import static java.lang.String.format;
import static java.util.Collections.singleton;
import static java.util.Objects.requireNonNull;

import com.sap.oss.phosphor.fosstars.model.Feature;
import com.sap.oss.phosphor.fosstars.model.Subject;
import com.sap.oss.phosphor.fosstars.model.ValueSet;
import com.sap.oss.phosphor.fosstars.model.value.BooleanValue;
import com.sap.oss.phosphor.fosstars.tool.YesNoQuestion;
import com.sap.oss.phosphor.fosstars.tool.YesNoQuestion.Answer;
import java.util.Set;

/**
 * This data provider asks a user about a boolean feature.
 */
public class AskYesOrNo extends AbstractInteractiveDataProvider {

  /**
   * A feature that the data provider supports.
   */
  private final Feature<Boolean> feature;

  /**
   * A question that the data provider asks.
   */
  private final String question;

  /**
   * Initialize a new data provider.
   *
   * @param feature A feature that the data provider supports.
   * @param question A question that the data provider asks.
   */
  public AskYesOrNo(Feature<Boolean> feature, String question) {
    this.feature = requireNonNull(feature, "Oops! Feature is null!");
    this.question = requireNonNull(question, "Oops! Question is null!");
  }

  @Override
  protected AskYesOrNo ask(Subject subject, ValueSet values) {
    Answer answer = new YesNoQuestion(callback, question).ask();
    switch (answer) {
      case YES:
        values.update(new BooleanValue(feature, true));
        break;
      case NO:
        values.update(new BooleanValue(feature, false));
        break;
      default:
        throw new IllegalArgumentException(
            format("Hey! You gave me an unexpected answer: %s", answer));
    }

    return this;
  }

  @Override
  public boolean supports(Subject subject) {
    return true;
  }

  @Override
  public Set<Feature<?>> supportedFeatures() {
    return singleton(feature);
  }
}
