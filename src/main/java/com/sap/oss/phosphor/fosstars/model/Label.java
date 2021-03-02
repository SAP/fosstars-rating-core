package com.sap.oss.phosphor.fosstars.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.sap.oss.phosphor.fosstars.model.rating.NotApplicableLabel;
import com.sap.oss.phosphor.fosstars.model.rating.example.SecurityRatingExample;
import com.sap.oss.phosphor.fosstars.model.rating.oss.OssRulesOfPlayRating;
import com.sap.oss.phosphor.fosstars.model.rating.oss.OssSecurityRating;

/**
 * An interface for a label.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = OssSecurityRating.SecurityLabel.class),
    @JsonSubTypes.Type(value = SecurityRatingExample.SecurityLabelExample.class),
    @JsonSubTypes.Type(value = OssRulesOfPlayRating.OssRulesOfPlayLabel.class),
    @JsonSubTypes.Type(value = NotApplicableLabel.class),
})
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
