package com.sap.oss.phosphor.fosstars.advice;

import com.sap.oss.phosphor.fosstars.model.Subject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * This advisor combines advises from multiple advisors.
 */
public class CompositeAdvisor implements Advisor {

  /**
   * A list of underlying advisors.
   */
  private final List<Advisor> advisors;

  /**
   * Create a new advisor that are based on a number of other ones.
   *
   * @param advisors The underlying advisors.
   */
  public CompositeAdvisor(Advisor... advisors) {
    Objects.requireNonNull(advisors, "Oh no! Advisors are null!");

    if (advisors.length == 0) {
      throw new IllegalArgumentException("Oh no! No advisors provided!");
    }

    this.advisors = Arrays.asList(advisors);
  }

  @Override
  public List<Advice> adviceFor(Subject subject) throws IOException {
    List<Advice> advice = new ArrayList<>();
    for (Advisor advisor : advisors) {
      advice.addAll(advisor.adviceFor(subject));
    }

    return advice;
  }
}
