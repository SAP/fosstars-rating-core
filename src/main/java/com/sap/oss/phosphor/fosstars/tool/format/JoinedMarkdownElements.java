package com.sap.oss.phosphor.fosstars.tool.format;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.joining;

import java.util.ArrayList;
import java.util.List;

/**
 * A collection of Markdown elements that should be joined using a specified delimiter.
 */
public class JoinedMarkdownElements extends AbstractMarkdownElement {

  /**
   * A delimiter for the elements.
   */
  private final String delimiter;

  /**
   * A list of elements.
   */
  private final List<MarkdownElement> elements;

  /**
   * Create a collection of joined elements.
   *
   * @param delimiter A delimiter.
   * @param elements A list of elements.
   */
  JoinedMarkdownElements(String delimiter, List<MarkdownElement> elements) {
    requireNonNull(delimiter, "Oops! Delimiter is null!");
    requireNonNull(elements, "Oops! Elements is null!");

    this.delimiter = delimiter;
    this.elements = new ArrayList<>(elements);
  }

  @Override
  public String make() {
    return elements.stream().map(MarkdownElement::make).collect(joining(delimiter));
  }
}
