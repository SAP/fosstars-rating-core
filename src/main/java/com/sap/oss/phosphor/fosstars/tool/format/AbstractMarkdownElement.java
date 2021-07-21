package com.sap.oss.phosphor.fosstars.tool.format;

/**
 * A base class for Markdown elements.
 */
public abstract class AbstractMarkdownElement implements MarkdownElement {

  @Override
  public String toString() {
    return make();
  }
}
