package com.sap.oss.phosphor.fosstars.nvd.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * CVSS V3.x score.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "cvssV3",
    "exploitabilityScore",
    "impactScore"
})
public class BaseMetricV3 {

  @JsonProperty("cvssV3")
  private CVSSv3 cvssV3;

  @JsonProperty("exploitabilityScore")
  private Double exploitabilityScore;

  @JsonProperty("impactScore")
  private Double impactScore;

  @JsonProperty("cvssV3")
  public CVSSv3 getCVSSv3() {
    return cvssV3;
  }
}
