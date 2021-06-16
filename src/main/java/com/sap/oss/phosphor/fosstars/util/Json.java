package com.sap.oss.phosphor.fosstars.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

/**
 * This is a helper class that offers methods for serialization and deserialization using JSON.
 */
public class Json extends Deserialization {

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
    Objects.requireNonNull(clazz, "Oh no! Class is null!");
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
   * Returns a shared {@link ObjectMapper} for JSON.
   *
   * @return A shared {@link ObjectMapper} for JSON.
   */
  public static ObjectMapper mapper() {
    return registerSubTypesIn(
        JsonMapper.builder()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .polymorphicTypeValidator(validator()).build());
  }
}
