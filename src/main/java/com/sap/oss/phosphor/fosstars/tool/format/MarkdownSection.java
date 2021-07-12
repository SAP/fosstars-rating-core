package com.sap.oss.phosphor.fosstars.tool.format;

import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.EMPTY;

public class MarkdownSection implements MarkdownElement {

  private final MarkdownHeader header;
  private final MarkdownElement text;

  MarkdownSection(MarkdownHeader header, MarkdownElement text) {
    this.header = header;
    this.text = text;
  }

  MarkdownHeader header() {
    return header;
  }

  MarkdownElement text() {
    return text;
  }

  @Override
  public String make() {
    String headerString = header.make();
    return Markdown.isEmpty(headerString) ? EMPTY
        : format("%s\n\n%s\n", headerString, text.make());
  }
}
