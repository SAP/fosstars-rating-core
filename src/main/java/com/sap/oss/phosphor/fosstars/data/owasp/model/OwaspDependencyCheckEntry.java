package com.sap.oss.phosphor.fosstars.data.owasp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OwaspDependencyCheckEntry implements Serializable {

  @JsonProperty("dependencies")
  private List<Dependency> dependencies = null;

  @JsonProperty("dependencies")
  public List<Dependency> getDependencies() {
    return dependencies;
  }
}