package com.sap.sgs.phosphor.fosstars.model;

/**
 * An interface for a label.
 */

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.sap.sgs.phosphor.fosstars.model.rating.example.SecurityRatingExample;
import com.sap.sgs.phosphor.fosstars.model.rating.oss.OssSecurityRating;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = OssSecurityRating.SecurityLabel.class),
    @JsonSubTypes.Type(value = SecurityRatingExample.SecurityLabelExample.class)
})
public interface Label {

  /**
   * @return the label's name.
   */
  String name();
}
