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
    "version_value",
    "version_affected"
})
public class VersionData {

  @JsonProperty("version_value")
  private String versionValue;

  @JsonProperty("version_affected")
  private String versionAffected;

  @JsonIgnore
  private Map<String, Object> additionalProperties = new HashMap<>();

  @JsonProperty("version_value")
  public String getVersionValue() {
    return versionValue;
  }

  @JsonProperty("version_value")
  public void setVersionValue(String versionValue) {
    this.versionValue = versionValue;
  }

  @JsonProperty("version_affected")
  public String getVersionAffected() {
    return versionAffected;
  }

  @JsonProperty("version_affected")
  public void setVersionAffected(String versionAffected) {
    this.versionAffected = versionAffected;
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
