package com.sap.oss.phosphor.fosstars.tool.format;

/**
 * An interface of a Markdown element.
 */
public interface MarkdownElement {

  /**
   * Renders the Markdown element.
   *
   * @return A Markdown-formatted text that represents the element.
   */
  String make();
}
