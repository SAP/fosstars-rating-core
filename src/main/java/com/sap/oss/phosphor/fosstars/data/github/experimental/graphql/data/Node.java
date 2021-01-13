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
@JsonPropertyOrder({"advisory", "firstPatchedVersion", "package", "severity", "updatedAt",
    "vulnerableVersionRange"})
public class Node {

  @JsonProperty("advisory")
  private Advisory advisory;

  @JsonProperty("firstPatchedVersion")
  private Object firstPatchedVersion;

  @JsonProperty("package")
  private Package packageManager;

  @JsonProperty("severity")
  private String severity;

  @JsonProperty("updatedAt")
  private String updatedAt;

  @JsonProperty("vulnerableVersionRange")
  private String vulnerableVersionRange;

  @JsonIgnore
  private Map<String, Object> additionalProperties = new HashMap<String, Object>();

  @JsonProperty("advisory")
  public Advisory getAdvisory() {
    return advisory;
  }

  @JsonProperty("advisory")
  public void setAdvisory(Advisory advisory) {
    this.advisory = advisory;
  }

  @JsonProperty("firstPatchedVersion")
  public Object getFirstPatchedVersion() {
    return firstPatchedVersion;
  }

  @JsonProperty("firstPatchedVersion")
  public void setFirstPatchedVersion(Object firstPatchedVersion) {
    this.firstPatchedVersion = firstPatchedVersion;
  }

  @JsonProperty("package")
  public Package getPackage() {
    return packageManager;
  }

  @JsonProperty("package")
  public void setPackage(Package packageManager) {
    this.packageManager = packageManager;
  }

  @JsonProperty("severity")
  public String getSeverity() {
    return severity;
  }

  @JsonProperty("severity")
  public void setSeverity(String severity) {
    this.severity = severity;
  }

  @JsonProperty("updatedAt")
  public String getUpdatedAt() {
    return updatedAt;
  }

  @JsonProperty("updatedAt")
  public void setUpdatedAt(String updatedAt) {
    this.updatedAt = updatedAt;
  }

  @JsonProperty("vulnerableVersionRange")
  public String getVulnerableVersionRange() {
    return vulnerableVersionRange;
  }

  @JsonProperty("vulnerableVersionRange")
  public void setVulnerableVersionRange(String vulnerableVersionRange) {
    this.vulnerableVersionRange = vulnerableVersionRange;
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
