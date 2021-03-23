package com.sap.oss.phosphor.fosstars.advice;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sap.oss.phosphor.fosstars.model.Subject;
import com.sap.oss.phosphor.fosstars.model.Value;
import java.util.Objects;

/**
 * A simple advice for a feature value.
 */
public class SimpleAdvice implements Advice {

  /**
   * A subject of the advice.
   */
  private final Subject subject;

  /**
   * A value for which the advice was given.
   */
  private final Value<?> value;

  /**
   * A content of the advice.
   */
  private final AdviceContent content;

  /**
   * Initialize a new instance.
   *
   * @param subject A subject of the advice.
   * @param value A value for which the advice is given.
   * @param content A content of the advice.
   */
  @JsonCreator
  public SimpleAdvice(
      @JsonProperty("subject") Subject subject,
      @JsonProperty("value") Value<?> value,
      @JsonProperty("content") AdviceContent content) {

    Objects.requireNonNull(subject, "Oh no! Subject is null!");
    Objects.requireNonNull(value, "Oh no! Value is null!");
    Objects.requireNonNull(content, "Oh no! Content is null!");

    if (!value.feature().equals(content.feature())) {
      throw new IllegalArgumentException("Hey! Features from the value and content are not equal!");
    }

    this.subject = subject;
    this.value = value;
    this.content = content;
  }

  @Override
  @JsonGetter("subject")
  public Subject subject() {
    return subject;
  }

  @Override
  @JsonGetter("value")
  public Value<?> value() {
    return value;
  }

  @Override
  @JsonGetter("content")
  public AdviceContent content() {
    return content;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (o instanceof SimpleAdvice) {
      SimpleAdvice that = (SimpleAdvice) o;
      return Objects.equals(subject, that.subject)
          && Objects.equals(value, that.value)
          && Objects.equals(content, that.content);
    }

    return false;
  }

  @Override
  public int hashCode() {
    return Objects.hash(subject, value, content);
  }
}
