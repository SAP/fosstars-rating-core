package com.sap.oss.phosphor.fosstars.model.owasp.experimental;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.io.Serializable;
import java.util.List;
import javax.annotation.Generated;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"reportSchema", "scanInfo", "projectInfo", "dependencies"})
@Generated("jsonschema2pojo")
public class OwaspDependencyCheckEntry implements Serializable {

  @JsonProperty("reportSchema")
  private String reportSchema;

  @JsonProperty("scanInfo")
  private ScanInfo scanInfo;

  @JsonProperty("projectInfo")
  private ProjectInfo projectInfo;

  @JsonProperty("dependencies")
  private List<Dependency> dependencies = null;

  @JsonProperty("reportSchema")
  public String getReportSchema() {
    return reportSchema;
  }

  @JsonProperty("reportSchema")
  public void setReportSchema(String reportSchema) {
    this.reportSchema = reportSchema;
  }

  public OwaspDependencyCheckEntry withReportSchema(String reportSchema) {
    this.reportSchema = reportSchema;
    return this;
  }

  @JsonProperty("scanInfo")
  public ScanInfo getScanInfo() {
    return scanInfo;
  }

  @JsonProperty("scanInfo")
  public void setScanInfo(ScanInfo scanInfo) {
    this.scanInfo = scanInfo;
  }

  public OwaspDependencyCheckEntry withScanInfo(ScanInfo scanInfo) {
    this.scanInfo = scanInfo;
    return this;
  }

  @JsonProperty("projectInfo")
  public ProjectInfo getProjectInfo() {
    return projectInfo;
  }

  @JsonProperty("projectInfo")
  public void setProjectInfo(ProjectInfo projectInfo) {
    this.projectInfo = projectInfo;
  }

  public OwaspDependencyCheckEntry withProjectInfo(ProjectInfo projectInfo) {
    this.projectInfo = projectInfo;
    return this;
  }

  @JsonProperty("dependencies")
  public List<Dependency> getDependencies() {
    return dependencies;
  }

  @JsonProperty("dependencies")
  public void setDependencies(List<Dependency> dependencies) {
    this.dependencies = dependencies;
  }

  public OwaspDependencyCheckEntry withDependencies(List<Dependency> dependencies) {
    this.dependencies = dependencies;
    return this;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(OwaspDependencyCheckEntry.class.getName()).append('@')
        .append(Integer.toHexString(System.identityHashCode(this))).append('[');
    sb.append("reportSchema");
    sb.append('=');
    sb.append(((this.reportSchema == null) ? "<null>" : this.reportSchema));
    sb.append(',');
    sb.append("scanInfo");
    sb.append('=');
    sb.append(((this.scanInfo == null) ? "<null>" : this.scanInfo));
    sb.append(',');
    sb.append("projectInfo");
    sb.append('=');
    sb.append(((this.projectInfo == null) ? "<null>" : this.projectInfo));
    sb.append(',');
    sb.append("dependencies");
    sb.append('=');
    sb.append(((this.dependencies == null) ? "<null>" : this.dependencies));
    sb.append(']');
    return sb.toString();
  }
}
