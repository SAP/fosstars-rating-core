package com.sap.oss.phosphor.fosstars.model.other;

import com.sap.oss.phosphor.fosstars.model.Feature;
import com.sap.oss.phosphor.fosstars.model.Parameter;
import com.sap.oss.phosphor.fosstars.model.Rating;
import com.sap.oss.phosphor.fosstars.model.Score;
import com.sap.oss.phosphor.fosstars.model.Tunable;
import com.sap.oss.phosphor.fosstars.model.Visitor;

/**
 * The visitor tries to make objects immutable if they implement the {@link Tunable} interface.
 */
public class MakeImmutable implements Visitor {

  @Override
  public void visit(Rating rating) {
    tryToMakeImmutable(rating);
  }

  @Override
  public void visit(Score score) {
    tryToMakeImmutable(score);
  }

  @Override
  public void visit(Feature<?> feature) {
    tryToMakeImmutable(feature);
  }

  @Override
  public void visit(Parameter parameter) {
    tryToMakeImmutable(parameter);
  }

  /**
   * Checks if an object implements the {@link Tunable} interface, and if so,
   * try to make it immutable.
   *
   * @param object The object to be examined.
   */
  private static void tryToMakeImmutable(Object object) {
    if (object instanceof Tunable) {
      Tunable tunable = (Tunable) object;
      tunable.makeImmutable();
    }
  }
}
