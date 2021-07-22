package com.sap.oss.phosphor.fosstars.tool.format;

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static java.util.Objects.requireNonNull;
import static org.apache.commons.lang3.StringUtils.isBlank;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BooleanSupplier;
import org.apache.commons.lang3.StringUtils;

/**
 * A helper class for creating Markdown.
 */
public class Markdown {

  /**
   * A whitespace.
   */
  static final String SPACE = " ";

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
   * Create a Markdown string from a template string with parameters.
   *
   * @param template The string.
   * @param elements The parameters.
   * @return A Markdown string.
   * @see String#format(String, Object...)
   */
  static MarkdownString string(String template, Object... elements) {
    requireNonNull(template, "Oops! String is null!");
    return new MarkdownString(
        elements != null && elements.length != 0 ? format(template, elements) : template);
  }

  /**
   * Make a text.
   *
   * @param text The text.
   * @return A bold text.
   */
  static BoldMarkdownString bold(String text) {
    return bold(string(text));
  }

  /**
   * Make a bold Markdown element.
   *
   * @param element The element.
   * @return A bold Markdown element.
   */
  static BoldMarkdownString bold(MarkdownElement element) {
    return new BoldMarkdownString(element);
  }

  /**
   * Create a Markdown template that is going to be filled out with elements while rendering.
   *
   * @param template The template.
   * @param elements The elements.
   * @return A Markdown template.
   */
  static MarkdownTemplate template(String template, MarkdownElement... elements) {
    return new MarkdownTemplate(template, elements);
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
  static MarkdownRuleIdentifier rule(MarkdownElement id) {
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
   * Create a builder for a reference to a Markdown header.
   *
   * @return A new builder.
   */
  static MarkdownHeaderReferenceBuilder reference() {
    return new MarkdownHeaderReferenceBuilder();
  }

  /**
   * Create a builder for a link.
   *
   * @return A new builder.
   */
  static MarkdownLinkBuilder link() {
    return new MarkdownLinkBuilder();
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
   * A builder for a reference to a Markdown header.
   */
  public static class MarkdownHeaderReferenceBuilder {

    /**
     * A header.
     */
    private MarkdownHeader header;

    /**
     * Set a section for the reference.
     *
     * @param section The section.
     * @return The same builder.
     */
    MarkdownHeaderReferenceBuilder to(MarkdownSection section) {
      return to(section.header());
    }

    /**
     * Set a header for the reference.
     *
     * @param header The header.
     * @return The same builder.
     */
    MarkdownHeaderReferenceBuilder to(MarkdownHeader header) {
      this.header = header;
      return this;
    }

    /**
     * Create a reference with a caption.
     *
     * @param caption The caption.
     * @return A Markdown reference.
     */
    MarkdownHeaderReference withCaption(MarkdownElement caption) {
      return new MarkdownHeaderReference(caption, header);
    }

    /**
     * Create a reference with a caption.
     *
     * @param caption The caption.
     * @return A Markdown reference.
     */
    MarkdownHeaderReference withCaption(String caption) {
      return new MarkdownHeaderReference(string(caption), header);
    }

    /**
     * Create a reference with the header's name as a caption.
     *
     * @return A Markdown reference.
     */
    MarkdownHeaderReference withHeaderName() {
      return new MarkdownHeaderReference(header.caption(), header);
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
    private int level = 1;

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

  /**
   * A builder for a Markdown link.
   */
  static class MarkdownLinkBuilder {

    /**
     * A link's target.
     */
    private String target;

    /**
     * Set a target of the link.
     *
     * @param target A target of the link.
     * @return The same builder.
     */
    public MarkdownLinkBuilder to(String target) {
      this.target = target;
      return this;
    }

    /**
     * Set a target of the link.
     *
     * @param target A target of the link.
     * @return The same builder.
     */
    public MarkdownLinkBuilder to(URL target) {
      this.target = target.toString();
      return this;
    }

    /**
     * Create a Markdown link with a specified caption.
     *
     * @param caption The caption.
     * @return A new Markdown link.
     */
    public MarkdownLink withCaption(MarkdownElement caption) {
      return new MarkdownLink(caption, target);
    }

    /**
     * Create a Markdown link with a specified caption.
     *
     * @param caption The caption.
     * @return A new Markdown link.
     */
    public MarkdownLink withCaption(String caption) {
      return new MarkdownLink(new MarkdownString(caption), target);
    }
  }
}
