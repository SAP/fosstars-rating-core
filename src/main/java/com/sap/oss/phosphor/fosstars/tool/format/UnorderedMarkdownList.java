package com.sap.oss.phosphor.fosstars.tool.format;

import java.util.List;

public class UnorderedMarkdownList extends MarkdownList {

  UnorderedMarkdownList(List<MarkdownElement> elements) {
    super(elements, "*  ");
  }
}
