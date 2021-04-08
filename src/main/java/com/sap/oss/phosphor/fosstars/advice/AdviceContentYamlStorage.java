package com.sap.oss.phosphor.fosstars.advice;

import static java.nio.file.LinkOption.NOFOLLOW_LINKS;
import static java.util.Collections.emptyList;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.sap.oss.phosphor.fosstars.model.Feature;
import com.sap.oss.phosphor.fosstars.util.Yaml;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * <p>The class provides advice for features. In other words, it maps a feature to a list of
 * {@link AdviceContent}.</p>
 *
 * <p>The storage stores advice in YAML.</p>
 *
 * <p>Advice in a storage may be generic meaning that they don't contain
 * details that are specific for a particular {@link com.sap.oss.phosphor.fosstars.model.Subject}.
 * To make advice more concrete, the storage takes an {@link AdviceContent} that provides details
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
   * Maps a feature name to a list of raw advice.
   */
  private final Map<String, List<RawAdviceContent>> featureToContent;

  /**
   * Initializes a new advice storage.
   *
   * @param featureToContent Advice mapped to features.
   */
  protected AdviceContentYamlStorage(Map<String, List<RawAdviceContent>> featureToContent) {
    Objects.requireNonNull(featureToContent, "Oh no! Content is null!");
    this.featureToContent = new HashMap<>(featureToContent);
  }

  /**
   * Returns advice for a feature in a specified context.
   *
   * @param feature The feature.
   * @param context The context.
   * @return A list of advice.
   * @throws MalformedURLException If the method couldn't parse URLs.
   */
  public List<AdviceContent> adviceFor(Feature<?> feature, AdviceContext context)
      throws MalformedURLException {

    List<AdviceContent> adviceContents = new ArrayList<>();
    for (RawAdviceContent rawAdvice : featureToContent.getOrDefault(feature.name(), emptyList())) {
      adviceContents.add(rawAdvice.transformFor(feature, context));
    }

    return adviceContents;
  }

  /**
   * Loads advice from a resource.
   *
   * @param path A path to the resource.
   * @return An instance of {@link AdviceContentYamlStorage}.
   * @throws IOException If the advice couldn't be loaded.
   */
  public static AdviceContentYamlStorage loadFrom(String path) throws IOException {
    InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
    if (is == null && Files.isRegularFile(Paths.get(path), NOFOLLOW_LINKS)) {
      is = Files.newInputStream(Paths.get(path));
    }

    if (is != null) {
      try {
        return new AdviceContentYamlStorage(Yaml.mapper().readValue(is, TYPE_REFERENCE));
      } finally {
        is.close();
      }
    }

    throw new IOException(String.format("'%s' not found!", path));
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

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (o.getClass() != getClass()) {
        return false;
      }
      RawLink rawLink = (RawLink) o;
      return Objects.equals(name, rawLink.name) && Objects.equals(url, rawLink.url);
    }

    @Override
    public int hashCode() {
      return Objects.hash(name, url);
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
        @JsonProperty("advice") String advice,
        @JsonProperty(value = "links") List<RawLink> links) {

      Objects.requireNonNull(advice, "Oh no! Advice is null!");

      advice = advice.trim();
      if (advice.isEmpty()) {
        throw new IllegalArgumentException("Oh no! Looks like the advice is empty!");
      }

      this.advice = advice;
      this.links = new ArrayList<>(links != null ? links : emptyList());
    }

    @JsonGetter("advice")
    private String advice() {
      return advice;
    }

    @JsonGetter("links")
    private List<RawLink> links() {
      return new ArrayList<>(links);
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
    AdviceContent transformFor(Feature<?> feature, AdviceContext context)
        throws MalformedURLException {

      // first, look for all variables in the advice and resolve them using the context
      Map<String, Optional<String>> values = new HashMap<>();
      for (String variable : variables()) {
        values.put(variable, context.resolve(variable));
      }

      // then, replace all variables in the advice
      String advice = resolve(this.advice, values);
      List<Link> links = new ArrayList<>();
      for (RawLink link : this.links) {
        String url = resolve(link.url, values);
        if (VARIABLE_PATTERN.matcher(url).matches()) {
          LOGGER.warn("Skip link because it still contains unresolved variables: {}", url);
          continue;
        }
        links.add(new Link(resolve(link.name, values), new URL(url)));
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
    private static String resolve(String string, Map<String, Optional<String>> values) {
      for (Map.Entry<String, Optional<String>> entry : values.entrySet()) {
        if (entry.getValue().isPresent()) {
          string = string.replaceAll(
              String.format("\\$\\{%s\\}", entry.getKey()),
              entry.getValue().get());
        }
      }
      return string;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (o.getClass() != getClass()) {
        return false;
      }
      RawAdviceContent content = (RawAdviceContent) o;
      return Objects.equals(advice, content.advice)
          && Objects.equals(links, content.links);
    }

    @Override
    public int hashCode() {
      return Objects.hash(advice, links);
    }
  }
}
