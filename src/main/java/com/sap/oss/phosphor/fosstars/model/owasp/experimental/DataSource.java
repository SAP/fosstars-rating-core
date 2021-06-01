package com.sap.oss.phosphor.fosstars.model.owasp.experimental;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.io.Serializable;
import javax.annotation.Generated;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"name", "timestamp"})
@Generated("jsonschema2pojo")
public class DataSource implements Serializable {

  @JsonProperty("name")
  private String name;

  @JsonProperty("timestamp")
  private String timestamp;

  @JsonProperty("name")
  public String getName() {
    return name;
  }

  @JsonProperty("name")
  public void setName(String name) {
    this.name = name;
  }

  public DataSource withName(String name) {
    this.name = name;
    return this;
  }

  @JsonProperty("timestamp")
  public String getTimestamp() {
    return timestamp;
  }

  @JsonProperty("timestamp")
  public void setTimestamp(String timestamp) {
    this.timestamp = timestamp;
  }

  public DataSource withTimestamp(String timestamp) {
    this.timestamp = timestamp;
    return this;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(DataSource.class.getName()).append('@')
        .append(Integer.toHexString(System.identityHashCode(this))).append('[');
    sb.append("name");
    sb.append('=');
    sb.append(((this.name == null) ? "<null>" : this.name));
    sb.append(',');
    sb.append("timestamp");
    sb.append('=');
    sb.append(((this.timestamp == null) ? "<null>" : this.timestamp));
    sb.append(']');
    return sb.toString();
  }
}
