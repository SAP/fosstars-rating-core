package com.sap.oss.phosphor.fosstars.data.owasp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.io.Serializable;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"name", "cvssv2", "cvssv3", "description", "references", "vulnerableSoftware"})
@JsonIgnoreProperties(ignoreUnknown = true)
public class OwaspDependencyCheckVuln implements Serializable {

  @JsonProperty("name")
  private String name;

  @JsonProperty("cvssv2")
  private Cvssv2 cvssv2;

  @JsonProperty("cvssv3")
  private Cvssv3 cvssv3;

  @JsonProperty("description")
  private String description;

  @JsonProperty("references")
  private List<OwaspDependencyCheckReference> references = null;

  @JsonProperty("vulnerableSoftware")
  private List<VulnerableSoftware> vulnerableSoftware = null;

  @JsonProperty("name")
  public String getName() {
    return name;
  }

  @JsonProperty("cvssv2")
  public Cvssv2 getCvssv2() {
    return cvssv2;
  }

  @JsonProperty("cvssv3")
  public Cvssv3 getCvssv3() {
    return cvssv3;
  }

  @JsonProperty("description")
  public String getDescription() {
    return description;
  }

  @JsonProperty("references")
  public List<OwaspDependencyCheckReference> getReferences() {
    return references;
  }

  @JsonProperty("vulnerableSoftware")
  public List<VulnerableSoftware> getVulnerableSoftware() {
    return vulnerableSoftware;
  }
}
