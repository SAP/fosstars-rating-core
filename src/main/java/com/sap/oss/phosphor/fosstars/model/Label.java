package com.sap.oss.phosphor.fosstars.model;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.sap.oss.phosphor.fosstars.model.rating.NotApplicableLabel;

/**
 * An interface for a label.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
public interface Label {

  /**
   * Get the label's name.
   *
   * @return The label's name.
   */
  String name();

  /**
   * Tells if the label is not applicable.
   *
   * @return True if the value is not applicable in the current context, false otherwise.
   */
  default boolean isNotApplicable() {
    return false;
  }

  /**
   * This is a label for a score value that is marked as not-applicable.
   */
  Label NOT_APPLICABLE = new NotApplicableLabel();
}
