package com.sap.oss.phosphor.fosstars.tool.format;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;

import org.apache.commons.lang3.StringUtils;

/**
 * A bold string.
 */
public class BoldMarkdownString extends AbstractMarkdownElement {

  /**
   * A wrapped Markdown element.
   */
  private final MarkdownElement element;

  /**
   * Make an element bold.
   *
   * @param element The element.
   */
  public BoldMarkdownString(MarkdownElement element) {
    this.element = requireNonNull(element, "Oops! Element is null!");
  }

  @Override
  public String make() {
    String string = element.make();
    return Markdown.isEmpty(string) ? StringUtils.EMPTY : format("**%s**", string);
  }
}
