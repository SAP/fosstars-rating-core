package com.sap.sgs.phosphor.fosstars.nvd.data;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "operator",
    "cpe_match"
})
public class Node {

  @JsonProperty("operator")
  private String operator;

  @JsonProperty("cpe_match")
  private List<CpeMatch> cpeMatch = null;

  @JsonIgnore
  private Map<String, Object> additionalProperties = new HashMap<>();

  @JsonProperty("operator")
  public String getOperator() {
    return operator;
  }

  @JsonProperty("operator")
  public void setOperator(String operator) {
    this.operator = operator;
  }

  @JsonProperty("cpe_match")
  public List<CpeMatch> getCpeMatch() {
    return cpeMatch;
  }

  @JsonProperty("cpe_match")
  public void setCpeMatch(List<CpeMatch> cpeMatch) {
    this.cpeMatch = cpeMatch;
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
