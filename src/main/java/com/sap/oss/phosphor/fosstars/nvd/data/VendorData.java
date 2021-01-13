package com.sap.oss.phosphor.fosstars.nvd.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "vendor_name",
    "product"
})
public class VendorData {

  @JsonProperty("vendor_name")
  private String vendorName;

  @JsonProperty("product")
  private Product product;

  @JsonProperty("vendor_name")
  public String getVendorName() {
    return vendorName;
  }

  @JsonProperty("product")
  public Product getProduct() {
    return product;
  }
}
