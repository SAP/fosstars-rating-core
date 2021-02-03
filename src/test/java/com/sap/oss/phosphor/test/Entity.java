package com.sap.oss.phosphor.test;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

public class Entity {

  @JsonTypeInfo(use = Id.CLASS)
  private final Object data;

  @JsonCreator
  public Entity(@JsonProperty("data") Object data) {
    this.data = data;
  }

  public Object getData() {
    return data;
  }
}
