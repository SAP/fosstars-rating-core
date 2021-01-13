package com.sap.oss.phosphor.fosstars.nvd.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "product_data"
})
public class Product {

  @JsonProperty("product_data")
  private List<ProductData> productData = new ArrayList<>();

  @JsonProperty("product_data")
  public List<ProductData> getProductData() {
    return productData;
  }
}
