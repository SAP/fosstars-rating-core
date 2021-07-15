package com.sap.oss.phosphor.fosstars.tool.format;

import static java.util.Objects.requireNonNull;

import org.apache.commons.lang3.StringUtils;

/**
 * A simple Markdown string. The class just wraps a usual string.
 */
public class MarkdownString extends AbstractMarkdownElement {

  /**
   * An empty string.
   */
  static final MarkdownString EMPTY = new MarkdownString(StringUtils.EMPTY);

  /**
   * The actual string.
   */
  private final String string;

  /**
   * Create a new Markdown string.
   *
   * @param string The actual string.
   */
  MarkdownString(String string) {
    this.string = requireNonNull(string, "Oops! String is null!");
  }

  @Override
  public String make() {
    return string;
  }
}
