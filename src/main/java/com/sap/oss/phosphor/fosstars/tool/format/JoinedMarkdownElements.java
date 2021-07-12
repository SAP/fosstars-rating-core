package com.sap.oss.phosphor.fosstars.tool.format;

import static java.util.stream.Collectors.joining;

import java.util.ArrayList;
import java.util.List;

public class JoinedMarkdownElements implements MarkdownElement {

  private final String delimiter;
  private final List<MarkdownElement> elements;

  JoinedMarkdownElements(String delimiter, List<MarkdownElement> elements) {
    this.delimiter = delimiter;
    this.elements = new ArrayList<>(elements);
  }

  @Override
  public String make() {
    return elements.stream().map(MarkdownElement::make).collect(joining(delimiter));
  }
}
