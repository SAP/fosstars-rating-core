package com.sap.oss.phosphor.fosstars.model.owasp.experimental;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.io.Serializable;
import javax.annotation.Generated;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"NVD", "NPM", "RETIREJS", "OSSINDEX"})
@Generated("jsonschema2pojo")
public class Credits implements Serializable {

  @JsonProperty("NVD")
  private String nvd;

  @JsonProperty("NPM")
  private String npm;

  @JsonProperty("RETIREJS")
  private String retirejs;

  @JsonProperty("OSSINDEX")
  private String ossindex;


  @JsonProperty("NVD")
  public String getNvd() {
    return nvd;
  }

  @JsonProperty("NVD")
  public void setNvd(String nvd) {
    this.nvd = nvd;
  }

  public Credits withNvd(String nvd) {
    this.nvd = nvd;
    return this;
  }

  @JsonProperty("NPM")
  public String getNpm() {
    return npm;
  }

  @JsonProperty("NPM")
  public void setNpm(String npm) {
    this.npm = npm;
  }

  public Credits withNpm(String npm) {
    this.npm = npm;
    return this;
  }

  @JsonProperty("RETIREJS")
  public String getRetirejs() {
    return retirejs;
  }

  @JsonProperty("RETIREJS")
  public void setRetirejs(String retirejs) {
    this.retirejs = retirejs;
  }

  public Credits withRetirejs(String retirejs) {
    this.retirejs = retirejs;
    return this;
  }

  @JsonProperty("OSSINDEX")
  public String getOssindex() {
    return ossindex;
  }

  @JsonProperty("OSSINDEX")
  public void setOssindex(String ossindex) {
    this.ossindex = ossindex;
  }

  public Credits withOssindex(String ossindex) {
    this.ossindex = ossindex;
    return this;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(Credits.class.getName()).append('@')
        .append(Integer.toHexString(System.identityHashCode(this))).append('[');
    sb.append("nvd");
    sb.append('=');
    sb.append(((this.nvd == null) ? "<null>" : this.nvd));
    sb.append(',');
    sb.append("npm");
    sb.append('=');
    sb.append(((this.npm == null) ? "<null>" : this.npm));
    sb.append(',');
    sb.append("retirejs");
    sb.append('=');
    sb.append(((this.retirejs == null) ? "<null>" : this.retirejs));
    sb.append(',');
    sb.append("ossindex");
    sb.append('=');
    sb.append(((this.ossindex == null) ? "<null>" : this.ossindex));
    sb.append(']');
    return sb.toString();
  }
}