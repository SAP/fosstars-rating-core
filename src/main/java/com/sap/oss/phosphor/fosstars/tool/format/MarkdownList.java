package com.sap.oss.phosphor.fosstars.tool.format;

import static java.lang.String.format;
import static java.util.stream.Collectors.joining;
import static org.apache.commons.lang3.StringUtils.repeat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public abstract class MarkdownList implements MarkdownElement {

  private final List<MarkdownElement> elements;
  private final String prefix;

  MarkdownList(List<MarkdownElement> elements, String prefix) {
    this.elements = new ArrayList<>(elements);
    this.prefix = prefix;
  }

  @Override
  public String make() {
    StringBuilder content = new StringBuilder();
    String indent = repeat(" ", prefix.length());
    for (MarkdownElement element : elements) {
      if (element instanceof GroupedMarkdownElements) {
        GroupedMarkdownElements nestedElements = (GroupedMarkdownElements) element;
        Iterator<MarkdownElement> iterator = nestedElements.get().iterator();
        if (!iterator.hasNext()) {
          continue;
        }
        content.append(format("%s%s\n", prefix, iterator.next().make()));
        while (iterator.hasNext()) {
          String indentedContent = Arrays
              .stream(iterator.next().make().split(Markdown.NEW_LINE))
              .map(line -> format("%s%s", indent, line))
              .collect(joining(Markdown.NEW_LINE));
          content.append(format("%s\n", indentedContent));
        }
      } else {
        content.append(format("%s%s\n", prefix, element.make()));
      }
    }

    return content.toString();
  }
}
