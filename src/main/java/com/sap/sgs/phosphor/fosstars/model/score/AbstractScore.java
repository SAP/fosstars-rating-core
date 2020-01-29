package com.sap.sgs.phosphor.fosstars.model.score;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.sap.sgs.phosphor.fosstars.model.Score;
import com.sap.sgs.phosphor.fosstars.model.ScoreValue;
import com.sap.sgs.phosphor.fosstars.model.Value;
import com.sap.sgs.phosphor.fosstars.model.ValueSet;
import com.sap.sgs.phosphor.fosstars.model.Visitor;
import java.util.Objects;
import java.util.Set;

/**
 * A base class for scores.
 */
public abstract class AbstractScore implements Score {

  /**
   * Score name.
   */
  private final String name;

  AbstractScore(String name) {
    Objects.requireNonNull(name, "Hey! Score name can't be null!");
    this.name = name;
  }

  @Override
  @JsonGetter("name")
  public final String name() {
    return name;
  }

  @Override
  public ScoreValue calculate(Set<Value> values) {
    Objects.requireNonNull(values, "Hey! Values can't be null!");
    return calculate(values.toArray(new Value[0]));
  }

  @Override
  public ScoreValue calculate(ValueSet values) {
    Objects.requireNonNull(values, "Hey! Value set can't be null!");
    return calculate(values.toArray());
  }

  @Override
  public void accept(Visitor visitor) {
    visitor.visit(this);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o instanceof AbstractScore == false) {
      return false;
    }
    AbstractScore that = (AbstractScore) o;
    return Objects.equals(name, that.name);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(name);
  }
}
