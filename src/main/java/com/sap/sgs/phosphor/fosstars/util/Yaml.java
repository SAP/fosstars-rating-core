package com.sap.sgs.phosphor.fosstars.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * This is a helper class that offers methods for serialization and deserialization using YAML.
 */
public class Yaml {

  /**
   * A factory for parsing YAML.
   */
  private static final YAMLFactory YAML_FACTORY;

  /**
   * For serialization and deserialization in YAML.
   */
  private static final ObjectMapper OBJECT_MAPPER;

  /**
   * A type reference for deserialization to a Map.
   */
  private static final TypeReference<Map<String,Object>> MAP_TYPE_REFERENCE
      = new TypeReference<Map<String, Object>>() {};

  static {
    YAML_FACTORY = new YAMLFactory();
    YAML_FACTORY.disable(YAMLGenerator.Feature.USE_NATIVE_TYPE_ID);
    OBJECT_MAPPER = new ObjectMapper(YAML_FACTORY);
    OBJECT_MAPPER.findAndRegisterModules();
  }

  /**
   * Reads YAML from an input stream and parses it ot a map.
   *
   * @param is The input stream to process.
   * @return A map with the parsed YAML.
   * @throws IOException If something went wrong.
   */
  public static Map<String, Object> readMap(InputStream is) throws IOException {
    return OBJECT_MAPPER.readValue(is, MAP_TYPE_REFERENCE);
  }

}
