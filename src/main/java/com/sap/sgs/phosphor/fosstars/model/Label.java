package com.sap.sgs.phosphor.fosstars.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.sap.sgs.phosphor.fosstars.model.rating.example.SecurityRatingExample;
import com.sap.sgs.phosphor.fosstars.model.rating.oss.OssSecurityRating;

/**
 * An interface for a label.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = OssSecurityRating.SecurityLabel.class),
    @JsonSubTypes.Type(value = SecurityRatingExample.SecurityLabelExample.class)
})
public interface Label {

  /**
   * Return the label's name.
   */
  String name();
}
