package com.sap.sgs.phosphor.fosstars.nvd.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "url",
    "name",
    "refsource",
    "tags"
})
public class ReferenceLink {

  @JsonProperty("url")
  private String url;

  @JsonProperty("name")
  private String name;

  @JsonProperty("refsource")
  private String refsource;

  @JsonProperty("tags")
  private List<String> tags;

  @JsonProperty("url")
  public String getUrl() {
    return url;
  }

  @JsonProperty("name")
  public String getName() {
    return url;
  }
}
