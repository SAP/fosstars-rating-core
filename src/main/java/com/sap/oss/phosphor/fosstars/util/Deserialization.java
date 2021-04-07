package com.sap.oss.phosphor.fosstars.util;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator.Builder;
import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * The class holds common functionality for JSON and YAML serialization/deserialization.
 */
public abstract class Deserialization {

  /**
   * A type reference for deserialization to a Map.
   */
  static final TypeReference<Map<String,Object>> MAP_TYPE_REFERENCE
      = new TypeReference<Map<String, Object>>() {};

  /**
   * A list of types that are allowed for deserialization by default.
   */
  private static final List<String> DEFAULT_ALLOWED_SUB_TYPES = Collections.singletonList(
      "com.sap.oss.phosphor.fosstars");

  /**
   * A list of classes that are allowed for deserialization.
   */
  private static final List<String> ALLOWED_SUB_TYPES = new ArrayList<>(DEFAULT_ALLOWED_SUB_TYPES);

  /**
   * Allows deserialization of a specified packages or classes.
   *
   * @param patterns The packages or classes.
   */
  public static void allow(String... patterns) {
    Objects.requireNonNull(patterns, "Oh no! Patterns are null!");
    ALLOWED_SUB_TYPES.addAll(Arrays.asList(patterns));
  }

  /**
   * Creates a validator for deserialization.
   *
   * @return A validator for deserialization.
   */
  static PolymorphicTypeValidator validator() {
    Builder builder = BasicPolymorphicTypeValidator.builder();
    ALLOWED_SUB_TYPES.forEach(builder::allowIfSubType);
    return builder.build();
  }

  /**
   * Reads a list from a node.
   *
   * @param root The node.
   * @param property A field that has the list.
   * @return A list of elements.
   * @throws IOException If something went wrong.
   */
  public static List<String> readListFrom(JsonNode root, String property) throws IOException {
    if (!root.has(property)) {
      return emptyList();
    }

    JsonNode node = root.get(property);
    if (node.isTextual()) {
      return singletonList(node.asText());
    }

    if (!node.isArray()) {
      throw new IOException(
          String.format("Oops! '%s' is not an array and not a string!", property));
    }

    List<String> list = new ArrayList<>();
    Iterator<JsonNode> iterator = node.elements();
    while (iterator.hasNext()) {
      JsonNode element = iterator.next();
      if (!element.isTextual()) {
        throw new IOException(String.format("Oops! Element of '%s' is not a string!", property));
      }
      list.add(element.asText());
    }

    return list;
  }
}
