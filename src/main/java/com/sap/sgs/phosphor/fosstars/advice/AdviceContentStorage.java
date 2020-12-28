package com.sap.sgs.phosphor.fosstars.advice;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.sap.sgs.phosphor.fosstars.model.Feature;
import com.sap.sgs.phosphor.fosstars.model.Rating;
import com.sap.sgs.phosphor.fosstars.util.Yaml;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * The class stores advices for features. In other words, it just maps a feature to a list of
 * {@link AdviceContent}.
 */
public class AdviceContentStorage {

  /**
   * A type reference for deserialization.
   */
  private static final TypeReference<Map<String, List<InternalAdviceContent>>> TYPE_REFERENCE
      = new TypeReference<Map<String, List<InternalAdviceContent>>>() {};

  /**
   * Maps a feature to a list of advices.
   */
  private final Map<Feature<?>, List<AdviceContent>> featureToContent;

  /**
   * Initializes a new advice storage.
   *
   * @param featureToContent Advices mapped by a feature.
   */
  private AdviceContentStorage(Map<Feature<?>, List<AdviceContent>> featureToContent) {
    Objects.requireNonNull(featureToContent, "Oh no! Content is null!");
    this.featureToContent = featureToContent;
  }

  /**
   * Loads advices from a resource for a specified rating.
   *
   * @param path A path the the resource.
   * @param rating The rating.
   * @throws IOException If the advices couldn't be loaded.
   */
  public AdviceContentStorage(String path, Rating rating) throws IOException {
    this(loadFromResource(path, rating));
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
   * Loads advices from a resource for a specified rating.
   *
   * @param path A path to the resource.
   * @param rating The rating.
   * @return The advices mapped to features.
   * @throws IOException If the advices couldn't be loaded.
   */
  protected static Map<Feature<?>, List<AdviceContent>> loadFromResource(String path, Rating rating)
      throws IOException {

    InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
    if (is != null) {
      try {
        Map<String, Feature<?>> nameToFeature = new HashMap<>();
        for (Feature<?> feature : rating.score().allFeatures()) {
          nameToFeature.put(feature.name(), feature);
        }

        Map<String, List<InternalAdviceContent>> map = Yaml.mapper().readValue(is, TYPE_REFERENCE);
        Map<Feature<?>, List<AdviceContent>> featureToContent = new HashMap<>();
        for (Map.Entry<String, List<InternalAdviceContent>> entry : map.entrySet()) {
          String name = entry.getKey();
          Feature<?> feature = nameToFeature.get(name);
          if (feature == null) {
            throw new IOException(String.format("Could not find feature in the rating: %s", name));
          }

          List<AdviceContent> contents = entry.getValue().stream()
              .map(content -> new AdviceContent(feature, content.advice, content.links))
              .collect(Collectors.toList());

          featureToContent.put(feature, contents);
        }

        return featureToContent;
      } finally {
        is.close();
      }
    }

    throw new IOException(String.format("Resource '%s' not found!", path));
  }

  /**
   * A helper class for deserializing advices.
   */
  private static class InternalAdviceContent {

    /**
     * A text of the advice.
     */
    private final String advice;

    /**
     * A list of additional links for the advice.
     */
    private final List<Link> links;

    /**
     * Creates a new instance.
     *
     * @param advice A text of the advice.
     * @param links A list of additional links for the advice.
     */
    @JsonCreator
    private InternalAdviceContent(
        @JsonProperty("advice") String advice, @JsonProperty("links") List<Link> links) {

      this.advice = advice;
      this.links = links;
    }
  }

}
