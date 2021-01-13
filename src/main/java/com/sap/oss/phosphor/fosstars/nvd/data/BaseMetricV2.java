package com.sap.oss.phosphor.fosstars.nvd.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * CVSS V2.0 score.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "cvssV2",
    "severity",
    "exploitabilityScore",
    "impactScore",
    "acInsufInfo",
    "obtainAllPrivilege",
    "obtainUserPrivilege",
    "obtainOtherPrivilege",
    "userInteractionRequired"
})
public class BaseMetricV2 {

  @JsonProperty("cvssV2")
  private CVSSv2 cvssV2;

  @JsonProperty("severity")
  private String severity;

  @JsonProperty("exploitabilityScore")
  private Double exploitabilityScore;

  @JsonProperty("impactScore")
  private Double impactScore;

  @JsonProperty("acInsufInfo")
  private Boolean acInsufInfo;

  @JsonProperty("obtainAllPrivilege")
  private Boolean obtainAllPrivilege;

  @JsonProperty("obtainUserPrivilege")
  private Boolean obtainUserPrivilege;

  @JsonProperty("obtainOtherPrivilege")
  private Boolean obtainOtherPrivilege;

  @JsonProperty("userInteractionRequired")
  private Boolean userInteractionRequired;

  @JsonProperty("cvssV2")
  public CVSSv2 getCVSSv2() {
    return cvssV2;
  }
}
