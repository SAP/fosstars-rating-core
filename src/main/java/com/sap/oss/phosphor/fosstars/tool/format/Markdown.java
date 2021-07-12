package com.sap.oss.phosphor.fosstars.tool.format;

import static java.util.Arrays.asList;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isEmpty;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BooleanSupplier;

public class Markdown {

  /**
   * A new line.
   */
  static final String NEW_LINE = "\n";

  /**
   * A double new line.
   */
  static final String DOUBLE_NEW_LINE = NEW_LINE + NEW_LINE;

  static boolean empty(String string) {
    return isEmpty(string) || isBlank(string.trim());
  }

  static MarkdownString string(String value) {
    return new MarkdownString(value);
  }

  static JoinedMarkdownBuilder join(MarkdownElement... elements) {
    return new JoinedMarkdownBuilder(elements);
  }

  static JoinedMarkdownBuilder join(List<MarkdownElement> elements) {
    return new JoinedMarkdownBuilder(elements);
  }

  static GroupedMarkdownElements group(MarkdownElement... elements) {
    return new GroupedMarkdownElements(elements);
  }

  static MarkdownRuleIdentifier rule(MarkdownString id) {
    return new MarkdownRuleIdentifier(id);
  }

  static MarkdownChoiceBuilder choose(MarkdownElement element) {
    return new MarkdownChoiceBuilder().firstOption(element);
  }

  static MarkdownHeaderBuilder header() {
    return new MarkdownHeaderBuilder();
  }

  static MarkdownSectionBuilder section() {
    return new MarkdownSectionBuilder();
  }

  static MarkdownHeaderReferenceBuilder reference() {
    return new MarkdownHeaderReferenceBuilder();
  }

  static OrderedMarkdownList orderedListOf(List<MarkdownElement> elements) {
    return new OrderedMarkdownList(elements);
  }

  static UnorderedMarkdownList unorderedListOf(List<MarkdownElement> elements) {
    return new UnorderedMarkdownList(elements);
  }

  public static class JoinedMarkdownBuilder {

    private final List<MarkdownElement> elements = new ArrayList<>();

    JoinedMarkdownBuilder(MarkdownElement... elements) {
      this(asList(elements));
    }

    JoinedMarkdownBuilder(List<MarkdownElement> elements) {
      this.elements.addAll(elements);
    }

    JoinedMarkdownBuilder of(List<MarkdownElement> elements) {
      this.elements.addAll(elements);
      return this;
    }

    JoinedMarkdownElements delimitedBy(String delimiter) {
      return new JoinedMarkdownElements(delimiter, elements);
    }
  }

  public static class MarkdownHeaderReferenceBuilder {

    private MarkdownSection section;

    MarkdownHeaderReferenceBuilder to(MarkdownSection section) {
      this.section = section;
      return this;
    }

    MarkdownHeaderReference withCaption(MarkdownElement caption) {
      return new MarkdownHeaderReference(caption, section);
    }
  }

  public static class MarkdownSectionBuilder {

    private MarkdownHeader header;

    MarkdownSectionBuilder with(MarkdownHeader header) {
      this.header = header;
      return this;
    }

    MarkdownSection thatContains(String text) {
      return new MarkdownSection(header, new MarkdownString(text));
    }

    MarkdownSection thatContains(MarkdownElement text) {
      return new MarkdownSection(header, text);
    }
  }

  public static class MarkdownHeaderBuilder {

    private int level;

    MarkdownHeader withCaption(String caption) {
      return new MarkdownHeader(new MarkdownString(caption), level);
    }

    MarkdownHeader withCaption(MarkdownElement caption) {
      return new MarkdownHeader(caption, level);
    }

    MarkdownHeaderBuilder level(int n) {
      this.level = n;
      return this;
    }
  }

  public static class MarkdownChoiceBuilder {

    private MarkdownElement firstOption;
    private BooleanSupplier condition;

    MarkdownChoiceBuilder firstOption(MarkdownElement firstOption) {
      this.firstOption = firstOption;
      return this;
    }

    MarkdownChoiceBuilder when(BooleanSupplier condition) {
      this.condition = condition;
      return this;
    }

    MarkdownChoice otherwise(MarkdownElement element) {
      return new MarkdownChoice(condition, firstOption, element);
    }
  }
}
