package com.sap.oss.phosphor.fosstars.tool.format;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;
import static org.apache.commons.lang3.StringUtils.EMPTY;

/**
 * A reference (link) to a Markdown header.
 */
public class MarkdownHeaderReference extends AbstractMarkdownElement {

  /**
   * A caption of the reference.
   */
  private final MarkdownElement caption;

  /**
   * A target header.
   */
  private final MarkdownHeader header;

  /**
   * Create a new reference to a header.
   *
   * @param caption A caption of the reference.
   * @param header The target header.
   */
  MarkdownHeaderReference(MarkdownElement caption, MarkdownHeader header) {
    this.caption = requireNonNull(caption, "Oops! Caption is null!");
    this.header = requireNonNull(header, "Oops! Header is null!");
  }

  @Override
  public String make() {
    String captionString = caption.make();
    if (Markdown.isEmpty(captionString)) {
      return EMPTY;
    }

    String headerString = header.caption().make();
    if (Markdown.isEmpty(headerString)) {
      return EMPTY;
    }

    String anchor = headerString.toLowerCase().replaceAll(" ", "-");
    return format("[%s](#%s)", captionString, anchor);
  }
}
