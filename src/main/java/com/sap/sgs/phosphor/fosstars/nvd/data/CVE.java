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
    "data_type",
    "data_format",
    "data_version",
    "CVE_data_meta",
    "affects",
    "problemtype",
    "references",
    "description"
})
public class CVE {

  @JsonProperty("data_type")
  private String dataType;

  @JsonProperty("data_format")
  private String dataFormat;

  @JsonProperty("data_version")
  private String dataVersion;

  @JsonProperty("CVE_data_meta")
  private CVEDataMeta cveDataMeta;

  @JsonProperty("affects")
  private Affects affects;

  @JsonProperty("problemtype")
  private ProblemType problemType;

  @JsonProperty("references")
  private References references;

  @JsonProperty("description")
  private Description description;

  @JsonIgnore
  private Map<String, Object> additionalProperties = new HashMap<>();

  @JsonProperty("data_type")
  public String getDataType() {
    return dataType;
  }

  @JsonProperty("data_type")
  public void setDataType(String dataType) {
    this.dataType = dataType;
  }

  @JsonProperty("data_format")
  public String getDataFormat() {
    return dataFormat;
  }

  @JsonProperty("data_format")
  public void setDataFormat(String dataFormat) {
    this.dataFormat = dataFormat;
  }

  @JsonProperty("data_version")
  public String getDataVersion() {
    return dataVersion;
  }

  @JsonProperty("data_version")
  public void setDataVersion(String dataVersion) {
    this.dataVersion = dataVersion;
  }

  @JsonProperty("CVE_data_meta")
  public CVEDataMeta getCveDataMeta() {
    return cveDataMeta;
  }

  @JsonProperty("CVE_data_meta")
  public void setCveDataMeta(CVEDataMeta cveDataMeta) {
    this.cveDataMeta = cveDataMeta;
  }

  @JsonProperty("affects")
  public Affects getAffects() {
    return affects;
  }

  @JsonProperty("affects")
  public void setAffects(Affects affects) {
    this.affects = affects;
  }

  @JsonProperty("problemtype")
  public ProblemType getProblemType() {
    return problemType;
  }

  @JsonProperty("problemtype")
  public void setProblemType(ProblemType problemType) {
    this.problemType = problemType;
  }

  @JsonProperty("references")
  public References getReferences() {
    return references;
  }

  @JsonProperty("references")
  public void setReferences(References references) {
    this.references = references;
  }

  @JsonProperty("description")
  public Description getDescription() {
    return description;
  }

  @JsonProperty("description")
  public void setDescription(Description description) {
    this.description = description;
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
