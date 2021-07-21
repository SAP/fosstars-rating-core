package com.sap.oss.phosphor.fosstars.tool.format;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;
import static org.apache.commons.lang3.StringUtils.EMPTY;

/**
 * A formatted identifier of a rule of play from the OSS RoP rating.
 */
public class MarkdownRuleIdentifier extends AbstractMarkdownElement {

  /**
   * A raw identifier.
   */
  private final MarkdownElement identifier;

  /**
   * Create an identifier for a rule.
   *
   * @param identifier A rule's raw identifier.
   */
  MarkdownRuleIdentifier(MarkdownElement identifier) {
    this.identifier = requireNonNull(identifier, "Oops! Identifier is null!");
  }

  @Override
  public String make() {
    String string = identifier.make();
    return Markdown.isEmpty(string) ? EMPTY : format("**[%s]**", string);
  }
}
