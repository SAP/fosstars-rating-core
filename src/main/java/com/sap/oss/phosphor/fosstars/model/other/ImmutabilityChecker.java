package com.sap.oss.phosphor.fosstars.model.other;

import com.sap.oss.phosphor.fosstars.model.Feature;
import com.sap.oss.phosphor.fosstars.model.Parameter;
import com.sap.oss.phosphor.fosstars.model.Rating;
import com.sap.oss.phosphor.fosstars.model.Score;
import com.sap.oss.phosphor.fosstars.model.Tunable;
import com.sap.oss.phosphor.fosstars.model.Visitor;

/**
 * The visitor checks if objects are immutable if they implement the {@link Tunable} interface.
 */
public class ImmutabilityChecker implements Visitor {

  private boolean isImmutable = true;

  /**
   * Tells whether all checked objects were immutable or not.
   *
   * @return True if all checked objects were immutable, false otherwise.
   */
  public boolean allImmutable() {
    return isImmutable;
  }

  @Override
  public void visit(Rating rating) {
    checkImmutability(rating);
  }

  @Override
  public void visit(Score score) {
    checkImmutability(score);
  }

  @Override
  public void visit(Feature<?> feature) {
    checkImmutability(feature);
  }

  @Override
  public void visit(Parameter parameter) {
    checkImmutability(parameter);
  }

  /**
   * Check if an object is immutable.
   *
   * @param object The object to be checked.
   */
  private void checkImmutability(Object object) {
    if (object instanceof Tunable) {
      Tunable tunable = (Tunable) object;
      if (!tunable.isImmutable()) {
        isImmutable = false;
      }
    }
  }
}
