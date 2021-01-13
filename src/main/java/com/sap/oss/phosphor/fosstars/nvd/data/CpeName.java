package com.sap.oss.phosphor.fosstars.nvd.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * CPE name.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "cpe22Uri",
    "cpe23Uri",
    "lastModifiedDate"
})
public class CpeName {

  @JsonProperty("cpe22Uri")
  private String cpe22Uri;

  @JsonProperty("cpe23Uri")
  private String cpe23Uri;

  @JsonProperty("lastModifiedDate")
  private String lastModifiedDate;
}
