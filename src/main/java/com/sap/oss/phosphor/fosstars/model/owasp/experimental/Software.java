package com.sap.oss.phosphor.fosstars.model.owasp.experimental;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.io.Serializable;
import javax.annotation.Generated;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"id", "versionStartIncluding", "versionStartExcluding", "versionEndExcluding",
    "vulnerabilityIdMatched", "versionEndIncluding"})
@Generated("jsonschema2pojo")
public class Software implements Serializable {

  @JsonProperty("id")
  private String id;

  @JsonProperty("versionStartIncluding")
  private String versionStartIncluding;

  @JsonProperty("versionStartExcluding")
  private String versionStartExcluding;

  @JsonProperty("versionEndExcluding")
  private String versionEndExcluding;

  @JsonProperty("vulnerabilityIdMatched")
  private String vulnerabilityIdMatched;

  @JsonProperty("versionEndIncluding")
  private String versionEndIncluding;

  @JsonProperty("vulnerable")
  private boolean vulnerable;

  @JsonProperty("id")
  public String getId() {
    return id;
  }

  @JsonProperty("id")
  public void setId(String id) {
    this.id = id;
  }

  public Software withId(String id) {
    this.id = id;
    return this;
  }

  @JsonProperty("versionStartIncluding")
  public String getVersionStartIncluding() {
    return versionStartIncluding;
  }

  @JsonProperty("versionStartIncluding")
  public void setVersionStartIncluding(String versionStartIncluding) {
    this.versionStartIncluding = versionStartIncluding;
  }

  public Software withVersionStartIncluding(String versionStartIncluding) {
    this.versionStartIncluding = versionStartIncluding;
    return this;
  }

  @JsonProperty("versionStartExcluding")
  public String getVersionStartExcluding() {
    return versionStartExcluding;
  }

  @JsonProperty("versionStartExcluding")
  public void setVersionStartExcluding(String versionStartExcluding) {
    this.versionStartExcluding = versionStartExcluding;
  }

  public Software withVersionStartExcluding(String versionStartExcluding) {
    this.versionStartExcluding = versionStartExcluding;
    return this;
  }

  @JsonProperty("versionEndExcluding")
  public String getVersionEndExcluding() {
    return versionEndExcluding;
  }

  @JsonProperty("versionEndExcluding")
  public void setVersionEndExcluding(String versionEndExcluding) {
    this.versionEndExcluding = versionEndExcluding;
  }

  public Software withVersionEndExcluding(String versionEndExcluding) {
    this.versionEndExcluding = versionEndExcluding;
    return this;
  }

  @JsonProperty("vulnerabilityIdMatched")
  public String getVulnerabilityIdMatched() {
    return vulnerabilityIdMatched;
  }

  @JsonProperty("vulnerabilityIdMatched")
  public void setVulnerabilityIdMatched(String vulnerabilityIdMatched) {
    this.vulnerabilityIdMatched = vulnerabilityIdMatched;
  }

  public Software withVulnerabilityIdMatched(String vulnerabilityIdMatched) {
    this.vulnerabilityIdMatched = vulnerabilityIdMatched;
    return this;
  }

  @JsonProperty("versionEndIncluding")
  public String getVersionEndIncluding() {
    return versionEndIncluding;
  }

  @JsonProperty("versionEndIncluding")
  public void setVersionEndIncluding(String versionEndIncluding) {
    this.versionEndIncluding = versionEndIncluding;
  }

  public Software withVersionEndIncluding(String versionEndIncluding) {
    this.versionEndIncluding = versionEndIncluding;
    return this;
  }

  @JsonProperty("vulnerable")
  public boolean getVulnerable() {
    return vulnerable;
  }

  @JsonProperty("vulnerable")
  public void setVulnerable(boolean vulnerable) {
    this.vulnerable = vulnerable;
  }

  public Software withVulnerable(boolean vulnerable) {
    this.vulnerable = vulnerable;
    return this;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(Software.class.getName()).append('@')
        .append(Integer.toHexString(System.identityHashCode(this))).append('[');
    sb.append("id");
    sb.append('=');
    sb.append(((this.id == null) ? "<null>" : this.id));
    sb.append(',');
    sb.append("versionStartIncluding");
    sb.append('=');
    sb.append(((this.versionStartIncluding == null) ? "<null>" : this.versionStartIncluding));
    sb.append(',');
    sb.append("versionStartExcluding");
    sb.append('=');
    sb.append(((this.versionStartExcluding == null) ? "<null>" : this.versionStartExcluding));
    sb.append(',');
    sb.append("versionEndExcluding");
    sb.append('=');
    sb.append(((this.versionEndExcluding == null) ? "<null>" : this.versionEndExcluding));
    sb.append(',');
    sb.append("vulnerabilityIdMatched");
    sb.append('=');
    sb.append(((this.vulnerabilityIdMatched == null) ? "<null>" : this.vulnerabilityIdMatched));
    sb.append(',');
    sb.append("versionEndIncluding");
    sb.append('=');
    sb.append(((this.versionEndIncluding == null) ? "<null>" : this.versionEndIncluding));
    sb.append(',');
    sb.append("vulnerable");
    sb.append('=');
    sb.append(this.vulnerable);
    sb.append(']');
    return sb.toString();
  }
}