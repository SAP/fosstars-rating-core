package com.sap.oss.phosphor.fosstars.tool.format;

import static java.lang.String.format;

/**
 * A Markdown link.
 */
public class MarkdownLink extends AbstractMarkdownElement {

  /**
   * A link's text.
   */
  private final MarkdownElement caption;

  /**
   * A link's target.
   */
  private final String target;

  /**
   * Create a new link.
   *
   * @param caption A link's text.
   * @param target A link's target.
   */
  public MarkdownLink(MarkdownElement caption, String target) {
    this.caption = caption;
    this.target = target;
  }

  @Override
  public String make() {
    return format("[%s](%s)", caption.make(), target);
  }
}
