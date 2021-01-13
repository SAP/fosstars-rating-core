package com.sap.oss.phosphor.fosstars.data.github.experimental.graphql.data;

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
@JsonPropertyOrder({"pageInfo", "nodes"})
public class SecurityVulnerabilities {

  @JsonProperty("pageInfo")
  private PageInfo pageInfo;

  @JsonProperty("nodes")
  private List<Node> nodes = null;

  @JsonIgnore
  private Map<String, Object> additionalProperties = new HashMap<String, Object>();

  @JsonProperty("pageInfo")
  public PageInfo getPageInfo() {
    return pageInfo;
  }

  @JsonProperty("pageInfo")
  public void setPageInfo(PageInfo pageInfo) {
    this.pageInfo = pageInfo;
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
