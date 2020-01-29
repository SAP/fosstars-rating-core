package com.sap.sgs.phosphor.fosstars.nvd.data;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "cvssV2",
    "severity",
    "exploitabilityScore",
    "impactScore",
    "obtainAllPrivilege",
    "obtainUserPrivilege",
    "obtainOtherPrivilege",
    "userInteractionRequired"
})
public class BaseMetricV2 {

  @JsonProperty("cvssV2")
  private CvssV2 cvssV2;

  @JsonProperty("severity")
  private String severity;

  @JsonProperty("exploitabilityScore")
  private Double exploitabilityScore;

  @JsonProperty("impactScore")
  private Double impactScore;

  @JsonProperty("obtainAllPrivilege")
  private Boolean obtainAllPrivilege;

  @JsonProperty("obtainUserPrivilege")
  private Boolean obtainUserPrivilege;

  @JsonProperty("obtainOtherPrivilege")
  private Boolean obtainOtherPrivilege;

  @JsonProperty("userInteractionRequired")
  private Boolean userInteractionRequired;
  @JsonIgnore
  private Map<String, Object> additionalProperties = new HashMap<>();

  @JsonProperty("cvssV2")
  public CvssV2 getCvssV2() {
    return cvssV2;
  }

  @JsonProperty("cvssV2")
  public void setCvssV2(CvssV2 cvssV2) {
    this.cvssV2 = cvssV2;
  }

  @JsonProperty("severity")
  public String getSeverity() {
    return severity;
  }

  @JsonProperty("severity")
  public void setSeverity(String severity) {
    this.severity = severity;
  }

  @JsonProperty("exploitabilityScore")
  public Double getExploitabilityScore() {
    return exploitabilityScore;
  }

  @JsonProperty("exploitabilityScore")
  public void setExploitabilityScore(Double exploitabilityScore) {
    this.exploitabilityScore = exploitabilityScore;
  }

  @JsonProperty("impactScore")
  public Double getImpactScore() {
    return impactScore;
  }

  @JsonProperty("impactScore")
  public void setImpactScore(Double impactScore) {
    this.impactScore = impactScore;
  }

  @JsonProperty("obtainAllPrivilege")
  public Boolean getObtainAllPrivilege() {
    return obtainAllPrivilege;
  }

  @JsonProperty("obtainAllPrivilege")
  public void setObtainAllPrivilege(Boolean obtainAllPrivilege) {
    this.obtainAllPrivilege = obtainAllPrivilege;
  }

  @JsonProperty("obtainUserPrivilege")
  public Boolean getObtainUserPrivilege() {
    return obtainUserPrivilege;
  }

  @JsonProperty("obtainUserPrivilege")
  public void setObtainUserPrivilege(Boolean obtainUserPrivilege) {
    this.obtainUserPrivilege = obtainUserPrivilege;
  }

  @JsonProperty("obtainOtherPrivilege")
  public Boolean getObtainOtherPrivilege() {
    return obtainOtherPrivilege;
  }

  @JsonProperty("obtainOtherPrivilege")
  public void setObtainOtherPrivilege(Boolean obtainOtherPrivilege) {
    this.obtainOtherPrivilege = obtainOtherPrivilege;
  }

  @JsonProperty("userInteractionRequired")
  public Boolean getUserInteractionRequired() {
    return userInteractionRequired;
  }

  @JsonProperty("userInteractionRequired")
  public void setUserInteractionRequired(Boolean userInteractionRequired) {
    this.userInteractionRequired = userInteractionRequired;
  }

  @JsonAnyGetter
  public Map<String, Object> getAdditionalProperties() {
    return this.additionalProperties;
  }

  @JsonAnySetter
  public void setAdditionalProperty(String name, Object value) {
    this.additionalProperties.put(name, value);
  }

}
