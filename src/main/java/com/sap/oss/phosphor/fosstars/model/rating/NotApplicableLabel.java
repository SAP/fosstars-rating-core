package com.sap.oss.phosphor.fosstars.model.rating;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sap.oss.phosphor.fosstars.model.Label;

/**
 * This is a label for a score value that is marked as not-applicable.
 */
public class NotApplicableLabel implements Label {

  @Override
  @JsonIgnore
  public final String name() {
    return "N/A";
  }

  @Override
  @JsonIgnore
  public final boolean isNotApplicable() {
    return true;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (obj == this) {
      return true;
    }
    return obj instanceof NotApplicableLabel;
  }

  @Override
  public int hashCode() {
    return 42;
  }
}
