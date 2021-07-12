package com.sap.oss.phosphor.fosstars.tool.format;

import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.EMPTY;

public class MarkdownHeaderReference implements MarkdownElement {

  private final MarkdownElement caption;
  private final MarkdownSection section;

  MarkdownHeaderReference(MarkdownElement caption, MarkdownSection section) {
    this.caption = caption;
    this.section = section;
  }

  @Override
  public String make() {
    String captionString = caption.make();
    if (Markdown.empty(captionString)) {
      return EMPTY;
    }

    String headerString = section.header().caption().make();
    if (Markdown.empty(headerString)) {
      return EMPTY;
    }

    String anchor = headerString.toLowerCase().replaceAll(" ", "-");
    return format("[%s](#%s)", captionString, anchor);
  }
}
