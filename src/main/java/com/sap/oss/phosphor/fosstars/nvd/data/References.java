package com.sap.oss.phosphor.fosstars.nvd.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "reference_data"
})
public class References {

  @JsonProperty("reference_data")
  private List<ReferenceLink> referenceData = new ArrayList<>();

  @JsonProperty("reference_data")
  public List<ReferenceLink> getReferenceData() {
    return referenceData;
  }
}
