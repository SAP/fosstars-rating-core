package com.sap.oss.phosphor.fosstars.nvd.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "description_data"
})
public class Description {

  @JsonProperty("description_data")
  private List<LangString> descriptionData = new ArrayList<>();

  @JsonProperty("description_data")
  public List<LangString> getDescriptionData() {
    return descriptionData;
  }
}
