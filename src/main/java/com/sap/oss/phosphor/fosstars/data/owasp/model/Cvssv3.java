package com.sap.oss.phosphor.fosstars.data.owasp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Cvssv3 implements Serializable {

  @JsonProperty("baseScore")
  private float baseScore;

  @JsonProperty("baseScore")
  public double getBaseScore() {
    return baseScore;
  }
}
