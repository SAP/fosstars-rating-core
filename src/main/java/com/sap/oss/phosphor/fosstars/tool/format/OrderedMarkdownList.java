package com.sap.oss.phosphor.fosstars.tool.format;

import java.util.List;

public class OrderedMarkdownList extends MarkdownList {

  OrderedMarkdownList(List<MarkdownElement> elements) {
    super(elements, "1.  ");
  }
}
