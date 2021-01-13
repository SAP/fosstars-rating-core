package com.sap.oss.phosphor.fosstars.nvd.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "vendor"
})
public class Affects {

  @JsonProperty("vendor")
  private Vendor vendor;

  @JsonProperty("vendor")
  public Vendor getVendor() {
    return vendor;
  }
}
