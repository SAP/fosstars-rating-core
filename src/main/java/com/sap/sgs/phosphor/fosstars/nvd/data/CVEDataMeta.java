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
    "ID",
    "ASSIGNER"
})
public class CVEDataMeta {

  @JsonProperty("ID")
  private String id;

  @JsonProperty("ASSIGNER")
  private String assigner;

  @JsonIgnore
  private Map<String, Object> additionalProperties = new HashMap<>();

  @JsonProperty("ID")
  public String getID() {
    return id;
  }

  @JsonProperty("ID")
  public void setID(String iD) {
    this.id = iD;
  }

  @JsonProperty("ASSIGNER")
  public String getAssigner() {
    return assigner;
  }

  @JsonProperty("ASSIGNER")
  public void setAssigner(String assigner) {
    this.assigner = assigner;
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
