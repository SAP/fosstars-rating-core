package com.sap.oss.phosphor.fosstars.data.github.experimental.owasp.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.io.Serializable;
import java.util.List;
import javax.annotation.Generated;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"source", "name", "severity", "cvssv2", "cvssv3", "cwes", "description",
    "notes", "references", "vulnerableSoftware"})
@Generated("jsonschema2pojo")
public class OwaspDependencyCheckVuln implements Serializable {

  @JsonProperty("source")
  private String source;

  @JsonProperty("name")
  private String name;

  @JsonProperty("severity")
  private String severity;

  @JsonProperty("cvssv2")
  private Cvssv2 cvssv2;

  @JsonProperty("cvssv3")
  private Cvssv3 cvssv3;

  @JsonProperty("cwes")
  private List<String> cwes = null;

  @JsonProperty("description")
  private String description;

  @JsonProperty("notes")
  private String notes;

  @JsonProperty("references")
  private List<OwaspDependencyCheckReference> references = null;

  @JsonProperty("vulnerableSoftware")
  private List<VulnerableSoftware> vulnerableSoftware = null;

  @JsonProperty("source")
  public String getSource() {
    return source;
  }

  @JsonProperty("source")
  public void setSource(String source) {
    this.source = source;
  }

  public OwaspDependencyCheckVuln withSource(String source) {
    this.source = source;
    return this;
  }

  @JsonProperty("name")
  public String getName() {
    return name;
  }

  @JsonProperty("name")
  public void setName(String name) {
    this.name = name;
  }

  public OwaspDependencyCheckVuln withName(String name) {
    this.name = name;
    return this;
  }

  @JsonProperty("severity")
  public String getSeverity() {
    return severity;
  }

  @JsonProperty("severity")
  public void setSeverity(String severity) {
    this.severity = severity;
  }

  public OwaspDependencyCheckVuln withSeverity(String severity) {
    this.severity = severity;
    return this;
  }

  @JsonProperty("cvssv2")
  public Cvssv2 getCvssv2() {
    return cvssv2;
  }

  @JsonProperty("cvssv2")
  public void setCvssv2(Cvssv2 cvssv2) {
    this.cvssv2 = cvssv2;
  }

  public OwaspDependencyCheckVuln withCvssv2(Cvssv2 cvssv2) {
    this.cvssv2 = cvssv2;
    return this;
  }

  @JsonProperty("cvssv3")
  public Cvssv3 getCvssv3() {
    return cvssv3;
  }

  @JsonProperty("cvssv3")
  public void setCvssv3(Cvssv3 cvssv3) {
    this.cvssv3 = cvssv3;
  }

  public OwaspDependencyCheckVuln withCvssv3(Cvssv3 cvssv3) {
    this.cvssv3 = cvssv3;
    return this;
  }

  @JsonProperty("cwes")
  public List<String> getCwes() {
    return cwes;
  }

  @JsonProperty("cwes")
  public void setCwes(List<String> cwes) {
    this.cwes = cwes;
  }

  public OwaspDependencyCheckVuln withCwes(List<String> cwes) {
    this.cwes = cwes;
    return this;
  }

  @JsonProperty("description")
  public String getDescription() {
    return description;
  }

  @JsonProperty("description")
  public void setDescription(String description) {
    this.description = description;
  }

  public OwaspDependencyCheckVuln withDescription(String description) {
    this.description = description;
    return this;
  }

  @JsonProperty("notes")
  public String getNotes() {
    return notes;
  }

  @JsonProperty("notes")
  public void setNotes(String notes) {
    this.notes = notes;
  }

  public OwaspDependencyCheckVuln withNotes(String notes) {
    this.notes = notes;
    return this;
  }

  @JsonProperty("references")
  public List<OwaspDependencyCheckReference> getReferences() {
    return references;
  }

  @JsonProperty("references")
  public void setReferences(List<OwaspDependencyCheckReference> references) {
    this.references = references;
  }

  public OwaspDependencyCheckVuln withReferences(List<OwaspDependencyCheckReference> references) {
    this.references = references;
    return this;
  }

  @JsonProperty("vulnerableSoftware")
  public List<VulnerableSoftware> getVulnerableSoftware() {
    return vulnerableSoftware;
  }

  @JsonProperty("vulnerableSoftware")
  public void setVulnerableSoftware(List<VulnerableSoftware> vulnerableSoftware) {
    this.vulnerableSoftware = vulnerableSoftware;
  }

  public OwaspDependencyCheckVuln withVulnerableSoftware(
      List<VulnerableSoftware> vulnerableSoftware) {
    this.vulnerableSoftware = vulnerableSoftware;
    return this;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(OwaspDependencyCheckVuln.class.getName()).append('@')
        .append(Integer.toHexString(System.identityHashCode(this))).append('[');
    sb.append("source");
    sb.append('=');
    sb.append(((this.source == null) ? "<null>" : this.source));
    sb.append(',');
    sb.append("name");
    sb.append('=');
    sb.append(((this.name == null) ? "<null>" : this.name));
    sb.append(',');
    sb.append("severity");
    sb.append('=');
    sb.append(((this.severity == null) ? "<null>" : this.severity));
    sb.append(',');
    sb.append("cvssv2");
    sb.append('=');
    sb.append(((this.cvssv2 == null) ? "<null>" : this.cvssv2));
    sb.append(',');
    sb.append("cvssv3");
    sb.append('=');
    sb.append(((this.cvssv3 == null) ? "<null>" : this.cvssv3));
    sb.append(',');
    sb.append("cwes");
    sb.append('=');
    sb.append(((this.cwes == null) ? "<null>" : this.cwes));
    sb.append(',');
    sb.append("description");
    sb.append('=');
    sb.append(((this.description == null) ? "<null>" : this.description));
    sb.append(',');
    sb.append("notes");
    sb.append('=');
    sb.append(((this.notes == null) ? "<null>" : this.notes));
    sb.append(',');
    sb.append("references");
    sb.append('=');
    sb.append(((this.references == null) ? "<null>" : this.references));
    sb.append(',');
    sb.append("vulnerableSoftware");
    sb.append('=');
    sb.append(((this.vulnerableSoftware == null) ? "<null>" : this.vulnerableSoftware));
    sb.append(',');
    if (sb.charAt((sb.length() - 1)) == ',') {
      sb.setCharAt((sb.length() - 1), ']');
    } else {
      sb.append(']');
    }
    return sb.toString();
  }
}
