package com.sap.oss.phosphor.fosstars.data.artifact.experimental.owasp.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Cvssv2 implements Serializable {

  @JsonProperty("score")
  private float score;

  @JsonProperty("score")
  public float getScore() {
    return score;
  }
}
