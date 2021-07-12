package com.sap.oss.phosphor.fosstars.tool.format;

import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.EMPTY;

/**
 * A reference (link) to a Markdown section.
 */
public class MarkdownSectionReference extends AbstractMarkdownElement {

  /**
   * A caption of the reference.
   */
  private final MarkdownElement caption;

  /**
   * A target section.
   */
  private final MarkdownSection section;

  /**
   * Create a new reference to a section.
   *
   * @param caption A caption of the reference.
   * @param section The target section.
   */
  MarkdownSectionReference(MarkdownElement caption, MarkdownSection section) {
    this.caption = caption;
    this.section = section;
  }

  @Override
  public String make() {
    String captionString = caption.make();
    if (Markdown.isEmpty(captionString)) {
      return EMPTY;
    }

    String headerString = section.header().caption().make();
    if (Markdown.isEmpty(headerString)) {
      return EMPTY;
    }

    String anchor = headerString.toLowerCase().replaceAll(" ", "-");
    return format("[%s](#%s)", captionString, anchor);
  }
}
