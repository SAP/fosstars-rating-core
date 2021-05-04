package com.sap.oss.phosphor.fosstars.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Objects;

/**
 * This is a helper class that offers methods for serialization and deserialization using YAML.
 */
public class Yaml extends Deserialization {

  /**
   * A factory for parsing YAML.
   */
  private static final YAMLFactory YAML_FACTORY;

  static {
    YAML_FACTORY = new YAMLFactory();
    YAML_FACTORY.disable(YAMLGenerator.Feature.USE_NATIVE_TYPE_ID);
  }

  /**
   * Reads YAML from an input stream and parses it ot a map.
   *
   * @param is The input stream to process.
   * @return A map with the parsed YAML.
   * @throws IOException If something went wrong.
   */
  public static Map<String, Object> readMap(InputStream is) throws IOException {
    Objects.requireNonNull(is, "Oh no! Input stream is null!");
    return mapper().readValue(is, MAP_TYPE_REFERENCE);
  }

  /**
   * Deserializes an instance of a specified type.
   *
   * @param is An input stream with serialized object.
   * @param clazz The type.
   * @param <T> The type.
   * @return A deserialized object.
   * @throws IOException If deserialization failed.
   */
  public static <T> T read(InputStream is, Class<T> clazz) throws IOException {
    Objects.requireNonNull(is, "Oh no! Input stream is null!");
    return mapper().readValue(is, clazz);
  }

  /**
   * Deserializes an instance of a specified type.
   *
   * @param bytes Serialized object.
   * @param clazz The type.
   * @param <T> The type.
   * @return A deserialized object.
   * @throws IOException If deserialization failed.
   */
  public static <T> T read(byte[] bytes, Class<T> clazz) throws IOException {
    Objects.requireNonNull(bytes, "Oh no! Bytes is null!");
    Objects.requireNonNull(clazz, "Oh no! Class is null!");
    return mapper().readValue(bytes, clazz);
  }

  /**
   * Serializes an object.
   *
   * @param object The object.
   * @return A byte array.
   * @throws JsonProcessingException If serialization failed.
   */
  public static byte[] toBytes(Object object) throws JsonProcessingException {
    Objects.requireNonNull(object, "Oh no! Object is null!");
    return mapper().writerWithDefaultPrettyPrinter().writeValueAsBytes(object);
  }

  /**
   * Returns a shared {@link ObjectMapper} for YAML.
   *
   * @return A shared {@link ObjectMapper} for YAML.
   */
  public static ObjectMapper mapper() {
    ObjectMapper mapper = JsonMapper.builder(YAML_FACTORY)
        .polymorphicTypeValidator(validator()).build();
    mapper.findAndRegisterModules();
    return registerSubTypesIn(mapper);
  }
}
