package com.sap.oss.phosphor.fosstars.tool.format;

import static java.util.Objects.requireNonNull;

import java.util.function.BooleanSupplier;

/**
 * A conditional Markdown element that selects between to options.
 */
public class MarkdownChoice extends AbstractMarkdownElement {

  /**
   * The condition.
   */
  private final BooleanSupplier condition;

  /**
   * The first option that is selected if the condition is true.
   */
  private final MarkdownElement firstOption;

  /**
   * The second option that is selected if the condition is false.
   */
  private final MarkdownElement secondOption;

  /**
   * Create a new conditional Markdown element.
   *
   * @param condition The condition.
   * @param firstOption The first option.
   * @param secondOption The second option.
   */
  public MarkdownChoice(
      BooleanSupplier condition, MarkdownElement firstOption, MarkdownElement secondOption) {

    this.condition = requireNonNull(condition, "Oops! Condition is null!");
    this.firstOption = requireNonNull(firstOption, "Oops! First option is null!");
    this.secondOption = requireNonNull(secondOption, "Oops! Second option is null!");
  }

  @Override
  public String make() {
    return condition.getAsBoolean() ? firstOption.make() : secondOption.make();
  }
}
