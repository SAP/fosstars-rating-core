package com.sap.oss.phosphor.fosstars.tool.format;

import static java.util.Arrays.asList;
import static org.apache.commons.lang3.StringUtils.isBlank;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BooleanSupplier;
import org.apache.commons.lang3.StringUtils;

/**
 * A helper class for creating Markdown.
 */
public class Markdown {

  /**
   * A new line.
   */
  static final String NEW_LINE = "\n";

  /**
   * A double new line.
   */
  static final String DOUBLE_NEW_LINE = NEW_LINE + NEW_LINE;

  /**
   * Checks whether a string is empty or not.
   *
   * @param string The string.
   * @return True if the string is null or blank, false otherwise.
   */
  static boolean isEmpty(String string) {
    return StringUtils.isEmpty(string) || isBlank(string.trim());
  }

  /**
   * Create a Markdown string from a usual string.
   *
   * @param string The string.
   * @return A Markdown string.
   */
  static MarkdownString string(String string) {
    return new MarkdownString(string);
  }

  /**
   * Create a group of Markdown elements.
   *
   * @param elements The elements.
   * @return A new group.
   */
  static GroupedMarkdownElements group(MarkdownElement... elements) {
    return new GroupedMarkdownElements(elements);
  }

  /**
   * Create a Markdown identifier for a open source rule of play.
   *
   * @param id The rule's identifier.
   * @return A new Markdown identifier.
   */
  static MarkdownRuleIdentifier rule(MarkdownString id) {
    return new MarkdownRuleIdentifier(id);
  }

  /**
   * Create an ordered Markdown list.
   *
   * @param elements Elements in the list.
   * @return An ordered Markdown list.
   */
  static OrderedMarkdownList orderedListOf(List<MarkdownElement> elements) {
    return new OrderedMarkdownList(elements);
  }

  /**
   * Create an unordered Markdown list.
   *
   * @param elements Elements in the list.
   * @return An unordered Markdown list.
   */
  static UnorderedMarkdownList unorderedListOf(List<MarkdownElement> elements) {
    return new UnorderedMarkdownList(elements);
  }

  /**
   * Create a builder for joined elements.
   *
   * @param elements The elements to be joined.
   * @return A new builder.
   */
  static JoinedMarkdownBuilder join(MarkdownElement... elements) {
    return new JoinedMarkdownBuilder(elements);
  }

  /**
   * Create a builder for joined elements.
   *
   * @param elements A list of elements to be joined.
   * @return A new builder.
   */
  static JoinedMarkdownBuilder join(List<MarkdownElement> elements) {
    return new JoinedMarkdownBuilder(elements);
  }

  /**
   * Create a builder for a conditional Markdown element.
   *
   * @param element The first option that can be selected in the conditional element.
   * @return A new conditional Markdown element.
   */
  static MarkdownChoiceBuilder choose(MarkdownElement element) {
    return new MarkdownChoiceBuilder().firstOption(element);
  }

  /**
   * Create a builder for a Markdown header.
   *
   * @return A new builder.
   */
  static MarkdownHeaderBuilder header() {
    return new MarkdownHeaderBuilder();
  }

  /**
   * Create a builder for a Markdown section.
   *
   * @return A new builder.
   */
  static MarkdownSectionBuilder section() {
    return new MarkdownSectionBuilder();
  }

  /**
   * Create a builder for a reference to a Markdown section.
   *
   * @return A new builder.
   */
  static MarkdownSectionReferenceBuilder reference() {
    return new MarkdownSectionReferenceBuilder();
  }

  /**
   * A builder for joined Markdown elements.
   */
  public static class JoinedMarkdownBuilder {

    /**
     * A list of elements to be joined.
     */
    private final List<MarkdownElement> elements = new ArrayList<>();

    /**
     * Create a new builder.
     *
     * @param elements Elements to be joined.
     */
    JoinedMarkdownBuilder(MarkdownElement... elements) {
      this(asList(elements));
    }

    /**
     * Create a new builder.
     *
     * @param elements A list of elements to be joined.
     */
    JoinedMarkdownBuilder(List<MarkdownElement> elements) {
      this.elements.addAll(elements);
    }

    /**
     * Set elements.
     *
     * @param elements A list of elements to be joined.
     * @return The same builder.
     */
    JoinedMarkdownBuilder of(List<MarkdownElement> elements) {
      this.elements.addAll(elements);
      return this;
    }

    /**
     * Create a joined Markdown elements with a delimiter.
     *
     * @param delimiter The delimiter.
     * @return The same builder.
     */
    JoinedMarkdownElements delimitedBy(String delimiter) {
      return new JoinedMarkdownElements(delimiter, elements);
    }
  }

  /**
   * A builder for a reference to a Markdown section.
   */
  public static class MarkdownSectionReferenceBuilder {

    /**
     * A section.
     */
    private MarkdownSection section;

    /**
     * Set a section for the reference.
     *
     * @param section The section.
     * @return The same builder.
     */
    MarkdownSectionReferenceBuilder to(MarkdownSection section) {
      this.section = section;
      return this;
    }

    /**
     * Create a reference with a caption.
     *
     * @param caption The caption.
     * @return A Markdown reference.
     */
    MarkdownSectionReference withCaption(MarkdownElement caption) {
      return new MarkdownSectionReference(caption, section);
    }
  }

  /**
   * A builder for a Markdown section.
   */
  static class MarkdownSectionBuilder {

    /**
     * A section's header.
     */
    private MarkdownHeader header;

    /**
     * Set a header.
     *
     * @param header The reader.
     * @return The same builder.
     */
    MarkdownSectionBuilder with(MarkdownHeader header) {
      this.header = header;
      return this;
    }

    /**
     * Create a Markdown section with a specified text.
     *
     * @param text The text.
     * @return A Markdown section.
     */
    MarkdownSection thatContains(String text) {
      return new MarkdownSection(header, new MarkdownString(text));
    }

    /**
     * Create a Markdown section with a specified Markdown element inside.
     *
     * @param text The Markdown element.
     * @return A Markdown section.
     */
    MarkdownSection thatContains(MarkdownElement text) {
      return new MarkdownSection(header, text);
    }
  }

  /**
   * A builder for a Markdown header.
   */
  static class MarkdownHeaderBuilder {

    /**
     * A header's level.
     */
    private int level;

    /**
     * Create a Markdown header with a specified caption.
     *
     * @param caption The caption.
     * @return A new Markdown header.
     */
    MarkdownHeader withCaption(String caption) {
      return new MarkdownHeader(new MarkdownString(caption), level);
    }

    /**
     * Create a Markdown header with a specified caption.
     *
     * @param caption The caption.
     * @return A new Markdown header.
     */
    MarkdownHeader withCaption(MarkdownElement caption) {
      return new MarkdownHeader(caption, level);
    }

    /**
     * Set a header's level.
     *
     * @param n The level.
     * @return The same builder.
     */
    MarkdownHeaderBuilder level(int n) {
      this.level = n;
      return this;
    }
  }

  /**
   * A builder for a conditional Markdown element.
   */
  static class MarkdownChoiceBuilder {

    /**
     * The first option.
     */
    private MarkdownElement firstOption;

    /**
     * A condition.
     */
    private BooleanSupplier condition;

    /**
     * Set the first option for the conditional Markdown element.
     *
     * @param firstOption The first option.
     * @return The same builder.
     */
    MarkdownChoiceBuilder firstOption(MarkdownElement firstOption) {
      this.firstOption = firstOption;
      return this;
    }

    /**
     * Set a condition for the conditional Markdown element.
     *
     * @param condition The condition.
     * @return The same builder.
     */
    MarkdownChoiceBuilder when(BooleanSupplier condition) {
      this.condition = condition;
      return this;
    }

    /**
     * Create a conditional Markdown element.
     *
     * @param element The second option.
     * @return A new conditional Markdown element.
     */
    MarkdownChoice otherwise(MarkdownElement element) {
      return new MarkdownChoice(condition, firstOption, element);
    }
  }
}
