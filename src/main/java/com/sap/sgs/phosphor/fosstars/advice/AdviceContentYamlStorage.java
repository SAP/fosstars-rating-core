package com.sap.sgs.phosphor.fosstars.advice;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.sap.sgs.phosphor.fosstars.model.Feature;
import com.sap.sgs.phosphor.fosstars.model.Rating;
import com.sap.sgs.phosphor.fosstars.util.Yaml;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * <p>The class provides advices for features. In other words, it maps a feature to a list of
 * {@link AdviceContent}.</p>
 *
 * <p>The storage stores advices in YAML.</p>
 *
 * <p>Advices in a storage may be generic meaning that they don't contain
 * details that are specific for a particular {@link com.sap.sgs.phosphor.fosstars.model.Subject}.
 * To make advices more concrete, the storage takes an {@link AdviceContent} that provides details
 * about a specific subject.</p>
 */
public class AdviceContentYamlStorage {

  /**
   * A logger.
   */
  private static final Logger LOGGER = LogManager.getLogger(AdviceContentYamlStorage.class);

  /**
   * A type reference for deserialization.
   */
  private static final TypeReference<Map<String, List<RawAdviceContent>>> TYPE_REFERENCE
      = new TypeReference<Map<String, List<RawAdviceContent>>>() {};

  /**
   * Maps a feature to a list of raw advices.
   */
  private final Map<Feature<?>, List<RawAdviceContent>> featureToContent;

  /**
   * Initializes a new advice storage.
   *
   * @param featureToContent Advices mapped to features.
   */
  protected AdviceContentYamlStorage(Map<Feature<?>, List<RawAdviceContent>> featureToContent) {
    Objects.requireNonNull(featureToContent, "Oh no! Content is null!");
    this.featureToContent = new HashMap<>(featureToContent);
  }

  /**
   * Returns advices for a feature in a specified context.
   *
   * @param feature The feature.
   * @param context The context.
   * @return A list of advices.
   */
  public List<AdviceContent> advicesFor(Feature<?> feature, AdviceContext context) {
    return featureToContent.getOrDefault(feature, Collections.emptyList())
        .stream()
        .map(rawAdvice -> rawAdvice.transformFor(feature, context))
        .collect(Collectors.toList());
  }

  /**
   * Loads advices from a resource for a specified rating.
   *
   * @param path A path to the resource.
   * @param rating The rating.
   * @return An instance of {@link AdviceContentYamlStorage}.
   * @throws IOException If the advices couldn't be loaded.
   */
  public static AdviceContentYamlStorage loadFromResource(String path, Rating rating)
      throws IOException {

    InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
    if (is != null) {
      try {
        Map<String, Feature<?>> nameToFeature = new HashMap<>();
        for (Feature<?> feature : rating.score().allFeatures()) {
          nameToFeature.put(feature.name(), feature);
        }

        Map<String, List<RawAdviceContent>> map = Yaml.mapper().readValue(is, TYPE_REFERENCE);
        Map<Feature<?>, List<RawAdviceContent>> featureToContent = new HashMap<>();
        for (Map.Entry<String, List<RawAdviceContent>> entry : map.entrySet()) {
          String name = entry.getKey();
          Feature<?> feature = nameToFeature.get(name);
          if (feature == null) {
            throw new IOException(String.format("Could not find feature in the rating: %s", name));
          }

          featureToContent.put(feature, entry.getValue());
        }

        return new AdviceContentYamlStorage(featureToContent);
      } finally {
        is.close();
      }
    }

    throw new IOException(String.format("Resource '%s' not found!", path));
  }

  /**
   * A link to additional info for an advice.
   */
  static class RawLink {

    /**
     * A name of the link.
     */
    public final String name;

    /**
     * A URL of the link.
     */
    public final String url;

    /**
     * Create a new raw link.
     *
     * @param name A name of a link. It must not be empty.
     * @param url A URL.
     */
    @JsonCreator
    public RawLink(@JsonProperty("name") String name, @JsonProperty("url") String url) {
      Objects.requireNonNull(name, "Oh no! Name is null!");
      Objects.requireNonNull(url, "Oh no! URL is null!");

      name = name.trim();
      if (name.isEmpty()) {
        throw new IllegalArgumentException("Oh no! Name seems to be empty!");
      }

      this.name = name;
      this.url = url;
    }
  }

  /**
   * A generic advice that is stored in a storage.
   */
  static class RawAdviceContent {

    /**
     * A pattern for looking for variables in an advice.
     * The format of a variable is "${VARIABLE_NAME}".
     */
    private static final Pattern VARIABLE_PATTERN = Pattern.compile("\\$\\{([A-Za-z_]+)\\}");

    /**
     * A text of the advice.
     */
    private final String advice;

    /**
     * A list of additional links for the advice.
     */
    private final List<RawLink> links;

    /**
     * Creates a new instance.
     *
     * @param advice A text of the advice.
     * @param links A list of additional links for the advice.
     */
    @JsonCreator
    RawAdviceContent(
        @JsonProperty("advice") String advice, @JsonProperty("links") List<RawLink> links) {

      Objects.requireNonNull(advice, "Oh no! Advice is null!");
      Objects.requireNonNull(advice, "Oh no! Links is null!");

      advice = advice.trim();
      if (advice.isEmpty()) {
        throw new IllegalArgumentException("Oh no! Looks like the advice is empty!");
      }

      this.advice = advice;
      this.links = new ArrayList<>(links);
    }

    /**
     * Looks for all variables in the advice text, link names, link URLs.
     *
     * @return A set of variables in the advice.
     */
    Set<String> variables() {
      Set<String> variables = new HashSet<>(variablesIn(advice));
      links.forEach(link -> {
        variables.addAll(variablesIn(link.name));
        variables.addAll(variablesIn(link.url));
      });
      return variables;
    }

    /**
     * Looks for variable in a specified string.
     *
     * @param string The string to be checked for variables.
     * @return A set of variables in the string.
     */
    Set<String> variablesIn(String string) {
      Set<String> variables = new HashSet<>();
      Matcher matcher = VARIABLE_PATTERN.matcher(string);
      while (matcher.find()) {
        variables.add(matcher.group(1));
      }
      return variables;
    }

    /**
     * Make an {@link AdviceContent} from this {@link RawAdviceContent}.
     *
     * @param feature A feature for the {@link AdviceContent}.
     * @param context A context to put more details into the advice.
     * @return A new {@link AdviceContent}.
     */
    AdviceContent transformFor(Feature<?> feature, AdviceContext context) {

      // first, look for all variables in the advice and resolve them using the context
      Map<String, Optional<String>> values = new HashMap<>();
      for (String variable : variables()) {
        values.put(variable, context.resolve(variable));
      }

      // then, replace all variables in the advice
      String advice = apply(this.advice, values);
      List<Link> links = new ArrayList<>();
      for (RawLink link : this.links) {
        try {
          links.add(new Link(apply(link.name, values), new URL(apply(link.url, values))));
        } catch (MalformedURLException e) {
          LOGGER.warn("Could not create a link for an advice! Skip it!", e);
        }
      }

      return new AdviceContent(feature, advice, links);
    }

    /**
     * Replaces variables with their values in a string.
     *
     * @param string The string.
     * @param values Maps variable names to their values.
     * @return An updated string.
     */
    private static String apply(String string, Map<String, Optional<String>> values) {
      for (Map.Entry<String, Optional<String>> entry : values.entrySet()) {
        if (entry.getValue().isPresent()) {
          string = string.replaceAll(
              String.format("\\$\\{%s\\}", entry.getKey()),
              entry.getValue().get());
        }
      }
      return string;
    }

  }
}
