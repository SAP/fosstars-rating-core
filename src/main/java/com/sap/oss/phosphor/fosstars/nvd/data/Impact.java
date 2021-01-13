package com.sap.oss.phosphor.fosstars.nvd.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Impact scores for a vulnerability as found on NVD.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "baseMetricV3",
    "baseMetricV2"
})
public class Impact {

  @JsonProperty("baseMetricV3")
  private BaseMetricV3 baseMetricV3;

  @JsonProperty("baseMetricV2")
  private BaseMetricV2 baseMetricV2;

  @JsonProperty("baseMetricV2")
  public BaseMetricV2 getBaseMetricV2() {
    return baseMetricV2;
  }

  @JsonProperty("baseMetricV3")
  public BaseMetricV3 getBaseMetricV3() {
    return baseMetricV3;
  }
}
