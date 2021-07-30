package com.sap.oss.phosphor.fosstars.data.owasp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"url", "name"})
@JsonIgnoreProperties(ignoreUnknown = true)
public class OwaspDependencyCheckReference implements Serializable {

  @JsonProperty("url")
  private String url;

  @JsonProperty("name")
  private String name;

  @JsonProperty("url")
  public String getUrl() {
    return url;
  }

  @JsonProperty("name")
  public String getName() {
    return name;
  }
}
