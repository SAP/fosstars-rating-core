package com.sap.oss.phosphor.fosstars.model.feature.oss;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.feature.AbstractFeature;
import com.sap.oss.phosphor.fosstars.model.value.UnknownValue;
import com.sap.oss.phosphor.fosstars.model.value.Vulnerabilities;
import com.sap.oss.phosphor.fosstars.model.value.VulnerabilitiesValue;
import com.sap.oss.phosphor.fosstars.util.Json;
import java.io.IOException;

public class VulnerabilitiesFeature extends AbstractFeature<Vulnerabilities> {

  /**
   * For deserialization.
   */
  private static final ObjectMapper MAPPER = Json.mapper();

  static {
    MAPPER.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
  }

  /**
   * Initializes a new feature.
   *
   * @param name A name of the feature.
   */
  @JsonCreator
  public VulnerabilitiesFeature(@JsonProperty("name") String name) {
    super(name);
  }

  @Override
  public Value<Vulnerabilities> value(Vulnerabilities entries) {
    return new VulnerabilitiesValue(this, entries);
  }

  /**
   * Takes a JSON string and tries to convert it to a {@link Value} object.
   *
   * @param string The JSON string.
   * @return An instance of {@link Value}.
   * @throws IllegalArgumentException If something went wrong.
   */
  @Override
  public Value<Vulnerabilities> parse(String string) {
    try {
      return value(MAPPER.readValue(string, Vulnerabilities.class));
    } catch (IOException e) {
      throw new IllegalArgumentException("Could not parse vulnerabilities!", e);
    }
  }

  @Override
  public Value<Vulnerabilities> unknown() {
    return new UnknownValue<>(this);
  }
}
