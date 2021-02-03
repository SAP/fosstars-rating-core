package com.sap.oss.phosphor.fosstars.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Objects;

/**
 * This is a helper class that offers methods for serialization and deserialization using JSON.
 */
public class Json {

  /**
   * For serialization and deserialization in JSON.
   */
  private static final ObjectMapper OBJECT_MAPPER;

  /**
   * A type reference for deserialization to a Map.
   */
  private static final TypeReference<Map<String, Object>> MAP_TYPE_REFERENCE
      = new TypeReference<Map<String, Object>>() {};

  static {
    OBJECT_MAPPER = new ObjectMapper();
  }

  /**
   * Reads JSON from an input stream and parses it ot a map.
   *
   * @param is The input stream to process.
   * @return A map with the parsed JSON.
   * @throws IOException If something went wrong.
   */
  public static Map<String, Object> readMap(InputStream is) throws IOException {
    return OBJECT_MAPPER.readValue(is, MAP_TYPE_REFERENCE);
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
    return OBJECT_MAPPER.readValue(is, clazz);
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
    return OBJECT_MAPPER.readValue(bytes, clazz);
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
    return OBJECT_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsBytes(object);
  }

  /**
   * Returns a shared {@link ObjectMapper} for JSON.
   *
   * @return A shared {@link ObjectMapper} for JSON.
   */
  public static ObjectMapper mapper() {
    return OBJECT_MAPPER;
  }

  /**
   * Creates a new {@link ObjectMapper} for JSON.
   *
   * @return A new {@link ObjectMapper} for JSON.
   */
  public static ObjectMapper newMapper() {
    return new ObjectMapper();
  }
}
