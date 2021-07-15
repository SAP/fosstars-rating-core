package com.sap.oss.phosphor.fosstars.tool.format;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.joining;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>A group of Markdown elements.</p>
 * <p>A group may be used in a multiline element of a Markdown list.
 * For example:</p>
 * <pre>
 *   1.  First element of the group.
 *       Second element of the group.
 *       Third element of the group.
 *   2.  Another element of the list.
 * </pre>
 */
public class GroupedMarkdownElements extends AbstractMarkdownElement {

  /**
   * Elements of the group.
   */
  private final List<MarkdownElement> elements;

  /**
   * Create a group of Markdown elements.
   *
   * @param elements The elements.
   */
  GroupedMarkdownElements(MarkdownElement... elements) {
    this(elements != null ? asList(elements) : emptyList());
  }

  /**
   * Create a group of Markdown elements.
   *
   * @param elements A list of elements.
   */
  GroupedMarkdownElements(List<MarkdownElement> elements) {
    requireNonNull(elements, "Oops! Elements is null!");
    this.elements = new ArrayList<>(elements);
  }

  /**
   * Get elements in the group.
   *
   * @return A list of elements in the group.
   */
  List<MarkdownElement> get() {
    return new ArrayList<>(elements);
  }

  @Override
  public String make() {
    return elements.stream().map(MarkdownElement::make).collect(joining(Markdown.NEW_LINE));
  }
}
