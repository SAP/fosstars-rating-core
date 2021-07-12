package com.sap.oss.phosphor.fosstars.tool.format;

import java.util.List;

/**
 * An ordered Markdown list.
 */
public class OrderedMarkdownList extends MarkdownList {

  /**
   * Create an ordered Markdown list.
   *
   * @param elements Elements in the list.
   */
  OrderedMarkdownList(List<MarkdownElement> elements) {
    super(elements, "1.  ");
  }
}
