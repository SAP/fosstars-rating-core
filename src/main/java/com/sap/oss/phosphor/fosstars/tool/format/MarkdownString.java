package com.sap.oss.phosphor.fosstars.tool.format;

import org.apache.commons.lang3.StringUtils;

public class MarkdownString implements MarkdownElement {

  static final MarkdownString EMPTY = new MarkdownString(StringUtils.EMPTY);

  private final String string;

  MarkdownString(String identifier) {
    this.string = identifier;
  }

  @Override
  public String make() {
    return string;
  }
}
