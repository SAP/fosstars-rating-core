package com.sap.sgs.phosphor.fosstars.nvd.data;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.HashMap;
import java.util.Map;

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

  @JsonIgnore
  private Map<String, Object> additionalProperties = new HashMap<>();

  @JsonProperty("vendor_name")
  public String getVendorName() {
    return vendorName;
  }

  @JsonProperty("vendor_name")
  public void setVendorName(String vendorName) {
    this.vendorName = vendorName;
  }

  @JsonProperty("product")
  public Product getProduct() {
    return product;
  }

  @JsonProperty("product")
  public void setProduct(Product product) {
    this.product = product;
  }

  @JsonAnyGetter
  public Map<String, Object> getAdditionalProperties() {
    return this.additionalProperties;
  }

  @JsonAnySetter
  public void setAdditionalProperty(String name, Object value) {
    this.additionalProperties.put(name, value);
  }

}
