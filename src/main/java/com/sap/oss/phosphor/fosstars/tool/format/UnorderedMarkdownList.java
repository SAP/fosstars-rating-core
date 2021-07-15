package com.sap.oss.phosphor.fosstars.tool.format;

import java.util.List;

/**
 * An unordered Markdown list with bullets.
 */
public class UnorderedMarkdownList extends MarkdownList {

  /**
   * Create an unordered Markdown list.
   *
   * @param elements Elements in the list.
   */
  UnorderedMarkdownList(List<MarkdownElement> elements) {
    super(elements, "*  ");
  }
}
