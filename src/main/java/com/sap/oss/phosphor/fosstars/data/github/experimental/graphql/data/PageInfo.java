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
@JsonPropertyOrder({"endCursor", "hasNextPage"})
public class PageInfo {

  @JsonProperty("endCursor")
  private String endCursor;

  @JsonProperty("hasNextPage")
  private Boolean hasNextPage;

  @JsonIgnore
  private Map<String, Object> additionalProperties = new HashMap<String, Object>();

  @JsonProperty("endCursor")
  public String getEndCursor() {
    return endCursor;
  }

  @JsonProperty("endCursor")
  public void setEndCursor(String endCursor) {
    this.endCursor = endCursor;
  }

  @JsonProperty("hasNextPage")
  public Boolean getHasNextPage() {
    return hasNextPage;
  }

  @JsonProperty("hasNextPage")
  public void setHasNextPage(Boolean hasNextPage) {
    this.hasNextPage = hasNextPage;
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
