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
    "cve",
    "configurations",
    "impact",
    "publishedDate",
    "lastModifiedDate"
})
public class NvdEntry {

  @JsonProperty("cve")
  private CVE cve;

  @JsonProperty("configurations")
  private Configurations configurations;

  @JsonProperty("impact")
  private Impact impact;

  @JsonProperty("publishedDate")
  private String publishedDate;

  @JsonProperty("lastModifiedDate")
  private String lastModifiedDate;

  @JsonIgnore
  private Map<String, Object> additionalProperties = new HashMap<>();

  @JsonProperty("cve")
  public CVE getCve() {
    return cve;
  }

  @JsonProperty("cve")
  public void setCve(CVE cve) {
    this.cve = cve;
  }

  @JsonProperty("configurations")
  public Configurations getConfigurations() {
    return configurations;
  }

  @JsonProperty("configurations")
  public void setConfigurations(Configurations configurations) {
    this.configurations = configurations;
  }

  @JsonProperty("impact")
  public Impact getImpact() {
    return impact;
  }

  @JsonProperty("impact")
  public void setImpact(Impact impact) {
    this.impact = impact;
  }

  @JsonProperty("publishedDate")
  public String getPublishedDate() {
    return publishedDate;
  }

  @JsonProperty("publishedDate")
  public void setPublishedDate(String publishedDate) {
    this.publishedDate = publishedDate;
  }

  @JsonProperty("lastModifiedDate")
  public String getLastModifiedDate() {
    return lastModifiedDate;
  }

  @JsonProperty("lastModifiedDate")
  public void setLastModifiedDate(String lastModifiedDate) {
    this.lastModifiedDate = lastModifiedDate;
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
