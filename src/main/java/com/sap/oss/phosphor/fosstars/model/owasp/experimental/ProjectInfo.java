package com.sap.oss.phosphor.fosstars.model.owasp.experimental;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.io.Serializable;
import javax.annotation.Generated;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"name", "reportDate", "credits"})
@Generated("jsonschema2pojo")
public class ProjectInfo implements Serializable {

  @JsonProperty("name")
  private String name;

  @JsonProperty("reportDate")
  private String reportDate;

  @JsonProperty("credits")
  private Credits credits;

  @JsonProperty("name")
  public String getName() {
    return name;
  }

  @JsonProperty("name")
  public void setName(String name) {
    this.name = name;
  }

  public ProjectInfo withName(String name) {
    this.name = name;
    return this;
  }

  @JsonProperty("reportDate")
  public String getReportDate() {
    return reportDate;
  }

  @JsonProperty("reportDate")
  public void setReportDate(String reportDate) {
    this.reportDate = reportDate;
  }

  public ProjectInfo withReportDate(String reportDate) {
    this.reportDate = reportDate;
    return this;
  }

  @JsonProperty("credits")
  public Credits getCredits() {
    return credits;
  }

  @JsonProperty("credits")
  public void setCredits(Credits credits) {
    this.credits = credits;
  }

  public ProjectInfo withCredits(Credits credits) {
    this.credits = credits;
    return this;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(ProjectInfo.class.getName()).append('@')
        .append(Integer.toHexString(System.identityHashCode(this))).append('[');
    sb.append("name");
    sb.append('=');
    sb.append(((this.name == null) ? "<null>" : this.name));
    sb.append(',');
    sb.append("reportDate");
    sb.append('=');
    sb.append(((this.reportDate == null) ? "<null>" : this.reportDate));
    sb.append(',');
    sb.append("credits");
    sb.append('=');
    sb.append(((this.credits == null) ? "<null>" : this.credits));
    sb.append(']');
    return sb.toString();
  }
}
