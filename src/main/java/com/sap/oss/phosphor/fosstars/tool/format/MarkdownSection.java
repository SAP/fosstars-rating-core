package com.sap.oss.phosphor.fosstars.tool.format;

import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.EMPTY;

/**
 * A Markdown section.
 */
public class MarkdownSection extends AbstractMarkdownElement {

  /**
   * A header of the section.
   */
  private final MarkdownHeader header;

  /**
   * A text in the section.
   */
  private final MarkdownElement text;

  /**
   * Create a new Markdown section.
   *
   * @param header A header of the section.
   * @param text A text in the section.
   */
  MarkdownSection(MarkdownHeader header, MarkdownElement text) {
    this.header = header;
    this.text = text;
  }

  /**
   * Get the section's header.
   *
   * @return The section's header.
   */
  MarkdownHeader header() {
    return header;
  }

  /**
   * Get the section's text.
   *
   * @return The section's text.
   */
  MarkdownElement text() {
    return text;
  }

  @Override
  public String make() {
    String headerString = header.make();
    return Markdown.isEmpty(headerString) ? EMPTY : format("%s\n\n%s\n", headerString, text.make());
  }
}
