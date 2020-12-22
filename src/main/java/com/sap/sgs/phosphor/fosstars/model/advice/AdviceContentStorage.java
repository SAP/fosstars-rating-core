package com.sap.sgs.phosphor.fosstars.model.advice;

import com.fasterxml.jackson.core.type.TypeReference;
import com.sap.sgs.phosphor.fosstars.model.Feature;
import com.sap.sgs.phosphor.fosstars.util.Yaml;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The class stores advices for features. In other words, it just maps a feature to a list of
 * {@link AdviceContent}.
 */
public class AdviceContentStorage {

  /**
   * A type reference for deserialization.
   */
  private static final TypeReference<List<AdviceContent>> TYPE_REFERENCE
      = new TypeReference<List<AdviceContent>>() {};

  /**
   * Maps a feature to a list of advices.
   */
  private final Map<Feature<?>, List<AdviceContent>> featureToContent;

  /**
   * Loads advices from a resource.
   *
   * @param path A path the the resource.
   * @throws IOException If the advices couldn't be loaded.
   */
  public AdviceContentStorage(String path) throws IOException {
    this(loadFromResource(path));
  }

  /**
   * Creates a new instance.
   *
   * @param adviceContents A list of advices.
   */
  protected AdviceContentStorage(List<AdviceContent> adviceContents) {
    featureToContent = new HashMap<>();
    for (AdviceContent content : adviceContents) {
      featureToContent.computeIfAbsent(content.feature(), k -> new ArrayList<>()).add(content);
    }
  }

  /**
   * Searches for advices for a specified feature.
   *
   * @param feature The feature.
   * @return A list of advices for the feature.
   */
  public List<AdviceContent> advicesFor(Feature<?> feature) {
    return new ArrayList<>(featureToContent.getOrDefault(feature, Collections.emptyList()));
  }

  /**
   * Loads advices from a resource.
   *
   * @param path A path to the resource.
   * @return A list of loaded advices.
   * @throws IOException If the advices couldn't be loaded.
   */
  protected static List<AdviceContent> loadFromResource(String path) throws IOException {
    InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
    if (is != null) {
      try {
        return Yaml.mapper().readValue(is, TYPE_REFERENCE);
      } finally {
        is.close();
      }
    }

    throw new IOException(String.format("Resource '%s' not found!", path));
  }

}
