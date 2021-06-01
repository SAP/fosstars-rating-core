package com.sap.oss.phosphor.fosstars.model.owasp.experimental;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.io.Serializable;
import java.util.List;
import javax.annotation.Generated;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"engineVersion", "dataSource"})
@Generated("jsonschema2pojo")
public class ScanInfo implements Serializable {

  @JsonProperty("engineVersion")
  private String engineVersion;

  @JsonProperty("dataSource")
  private List<DataSource> dataSource = null;

  @JsonProperty("engineVersion")
  public String getEngineVersion() {
    return engineVersion;
  }

  @JsonProperty("engineVersion")
  public void setEngineVersion(String engineVersion) {
    this.engineVersion = engineVersion;
  }

  public ScanInfo withEngineVersion(String engineVersion) {
    this.engineVersion = engineVersion;
    return this;
  }

  @JsonProperty("dataSource")
  public List<DataSource> getDataSource() {
    return dataSource;
  }

  @JsonProperty("dataSource")
  public void setDataSource(List<DataSource> dataSource) {
    this.dataSource = dataSource;
  }

  public ScanInfo withDataSource(List<DataSource> dataSource) {
    this.dataSource = dataSource;
    return this;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(ScanInfo.class.getName()).append('@')
        .append(Integer.toHexString(System.identityHashCode(this))).append('[');
    sb.append("engineVersion");
    sb.append('=');
    sb.append(((this.engineVersion == null) ? "<null>" : this.engineVersion));
    sb.append(',');
    sb.append("dataSource");
    sb.append('=');
    sb.append(((this.dataSource == null) ? "<null>" : this.dataSource));
    sb.append(']');
    return sb.toString();
  }
}