package com.sap.oss.phosphor.fosstars.tool.format;

import static java.util.Arrays.asList;

import java.util.List;

/**
 * A Markdown template with parameters.
 * The template is filled out with the parameters while rendering.
 *
 * @see String#format(String, Object...)
 */
public class MarkdownTemplate extends AbstractMarkdownElement {

  /**
   * A template.
   */
  private final String template;

  /**
   * A list of parameters.
   */
  private final List<MarkdownElement> parameters;

  /**
   * Create a new Markdown template.
   *
   * @param template A template string.
   * @param parameters Parameters.
   */
  public MarkdownTemplate(String template, MarkdownElement... parameters) {
    this.template = template;
    this.parameters = asList(parameters);
  }

  @Override
  public String make() {
    String[] renderedParameters = new String[parameters.size()];
    for (int i = 0; i < parameters.size(); i++) {
      renderedParameters[i] = parameters.get(i).make();
    }
    return String.format(template, renderedParameters);
  }
}
