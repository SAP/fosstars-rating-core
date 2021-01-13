package com.sap.oss.phosphor.fosstars.data.github.experimental.graphql.data;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"securityVulnerabilities"})
public class Data {

  @JsonProperty("securityVulnerabilities")
  private SecurityVulnerabilities securityVulnerabilities;

  @JsonIgnore
  private Map<String, Object> additionalProperties = new HashMap<String, Object>();

  @JsonProperty("securityVulnerabilities")
  public SecurityVulnerabilities getSecurityVulnerabilities() {
    return securityVulnerabilities;
  }

  @JsonProperty("securityVulnerabilities")
  public void setSecurityVulnerabilities(SecurityVulnerabilities securityVulnerabilities) {
    this.securityVulnerabilities = securityVulnerabilities;
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
