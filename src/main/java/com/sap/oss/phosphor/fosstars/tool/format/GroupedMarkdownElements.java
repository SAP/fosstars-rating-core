package com.sap.oss.phosphor.fosstars.tool.format;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.joining;

import java.util.ArrayList;
import java.util.List;

public class GroupedMarkdownElements implements MarkdownElement {

  private final List<MarkdownElement> elements;

  GroupedMarkdownElements(MarkdownElement... elements) {
    this(asList(elements));
  }

  GroupedMarkdownElements(List<MarkdownElement> elements) {
    this.elements = new ArrayList<>(elements);
  }

  List<MarkdownElement> get() {
    return new ArrayList<>(elements);
  }

  @Override
  public String make() {
    return elements.stream().map(MarkdownElement::make).collect(joining(Markdown.NEW_LINE));
  }
}
