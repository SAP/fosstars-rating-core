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
@JsonPropertyOrder({"pageInfo", "totalCount", "nodes"})
public class SecurityAdvisories {

  @JsonProperty("pageInfo")
  private PageInfo pageInfo;

  @JsonProperty("totalCount")
  private Integer totalCount;

  @JsonProperty("nodes")
  private List<Advisory> nodes = null;

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

  @JsonProperty("totalCount")
  public Integer getTotalCount() {
    return totalCount;
  }

  @JsonProperty("totalCount")
  public void setTotalCount(Integer totalCount) {
    this.totalCount = totalCount;
  }

  @JsonProperty("nodes")
  public List<Advisory> getNodes() {
    return nodes;
  }

  @JsonProperty("nodes")
  public void setNodes(List<Advisory> nodes) {
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
