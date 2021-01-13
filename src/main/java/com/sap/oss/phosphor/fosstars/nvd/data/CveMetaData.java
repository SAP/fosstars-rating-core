package com.sap.oss.phosphor.fosstars.nvd.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "ID",
    "ASSIGNER"
})
// the properties below are ignored because they are not used
// that saves a bit of memory
// when they become necessary, then can be enabled
@JsonIgnoreProperties({
    "assigner"
})
public class CveMetaData {

  @JsonProperty("ID")
  private String id;

  @JsonProperty("ASSIGNER")
  private String assigner;

  @JsonProperty("ID")
  public String getId() {
    return id;
  }

  @JsonProperty("ID")
  public void setId(String id) {
    this.id = id;
  }

}
