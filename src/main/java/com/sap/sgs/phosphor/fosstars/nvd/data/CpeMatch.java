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
    "vulnerable",
    "cpe23Uri",
    "versionEndExcluding",
    "versionEndIncluding"
})
public class CpeMatch {

  @JsonProperty("vulnerable")
  private Boolean vulnerable;

  @JsonProperty("cpe23Uri")
  private String cpe23Uri;

  @JsonProperty("versionEndExcluding")
  private String versionEndExcluding;

  @JsonProperty("versionEndIncluding")
  private String versionEndIncluding;

  @JsonIgnore
  private Map<String, Object> additionalProperties = new HashMap<>();

  @JsonProperty("vulnerable")
  public Boolean getVulnerable() {
    return vulnerable;
  }

  @JsonProperty("vulnerable")
  public void setVulnerable(Boolean vulnerable) {
    this.vulnerable = vulnerable;
  }

  @JsonProperty("cpe23Uri")
  public String getCpe23Uri() {
    return cpe23Uri;
  }

  @JsonProperty("cpe23Uri")
  public void setCpe23Uri(String cpe23Uri) {
    this.cpe23Uri = cpe23Uri;
  }

  @JsonProperty("versionEndExcluding")
  public String getVersionEndExcluding() {
    return versionEndExcluding;
  }

  @JsonProperty("versionEndIncluding")
  public String getVersionEndIncluding() {
    return versionEndIncluding;
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
