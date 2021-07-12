package com.sap.oss.phosphor.fosstars.tool.format;

import java.util.function.BooleanSupplier;

public class MarkdownChoice implements MarkdownElement {

  private final BooleanSupplier condition;
  private final MarkdownElement firstOption;
  private final MarkdownElement secondOption;

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
