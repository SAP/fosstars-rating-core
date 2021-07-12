package com.sap.oss.phosphor.fosstars.tool.format;

import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.repeat;

public class MarkdownHeader implements MarkdownElement {

  private final MarkdownElement caption;
  private final int level;

  MarkdownHeader(MarkdownElement caption, int level) {
    this.caption = caption;
    this.level = level;
  }

  MarkdownElement caption() {
    return caption;
  }

  @Override
  public String make() {
    String captionString = caption.make();
    return Markdown.isEmpty(captionString)
        ? EMPTY
        : format("%s %s", repeat("#", level), captionString);
  }
}
