package com.sap.oss.phosphor.fosstars.tool.format;

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

    this.condition = condition;
    this.firstOption = firstOption;
    this.secondOption = secondOption;
  }

  @Override
  public String make() {
    return condition.getAsBoolean() ? firstOption.make() : secondOption.make();
  }
}
