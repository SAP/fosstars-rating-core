package com.sap.oss.phosphor.fosstars.nvd.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "vendor_data"
})
public class Vendor {

  @JsonProperty("vendor_data")
  private List<VendorData> vendorData = new ArrayList<>();

  @JsonProperty("vendor_data")
  public List<VendorData> getVendorData() {
    return vendorData;
  }
}
