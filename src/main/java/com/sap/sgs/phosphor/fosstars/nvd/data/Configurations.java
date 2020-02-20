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
    "CVE_data_version",
    "nodes"
})
public class Configurations {

  @JsonProperty("CVE_data_version")
  private String cveDataVersion;

  @JsonProperty("nodes")
  private List<Node> nodes = null;

  @JsonIgnore
  private Map<String, Object> additionalProperties = new HashMap<>();

  @JsonProperty("CVE_data_version")
  public String getCveDataVersion() {
    return cveDataVersion;
  }

  @JsonProperty("CVE_data_version")
  public void setCveDataVersion(String cveDataVersion) {
    this.cveDataVersion = cveDataVersion;
  }

  @JsonProperty("nodes")
  public List<Node> getNodes() {
    return nodes;
  }

  @JsonProperty("nodes")
  public void setNodes(List<Node> nodes) {
    this.nodes = nodes;
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
