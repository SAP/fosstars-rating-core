package com.sap.oss.phosphor.fosstars.nvd.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "product_name",
    "version"
})
public class ProductData {

  @JsonProperty("product_name")
  private String productName;

  @JsonProperty("version")
  private Version version;

  @JsonProperty("product_name")
  public String getProductName() {
    return productName;
  }
}
