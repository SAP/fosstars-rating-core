package com.sap.oss.phosphor.fosstars.tool.format;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.repeat;

/**
 * A Markdown header.
 */
public class MarkdownHeader extends AbstractMarkdownElement {

  /**
   * A caption of the header.
   */
  private final MarkdownElement caption;

  /**
   * A level of the reader.
   */
  private final int level;

  MarkdownHeader(MarkdownElement caption, int level) {
    requireNonNull(caption, "Oops! Caption is null!");

    if (level <= 0) {
      throw new IllegalArgumentException("A header must be positive!");
    }

    this.caption = caption;
    this.level = level;
  }

  /**
   * Get the header's caption.
   *
   * @return The header's caption.
   */
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
