package com.sap.oss.phosphor.fosstars.tool.format;

import static com.sap.oss.phosphor.fosstars.model.score.oss.OssRulesOfPlayScore.findViolatedRulesIn;
import static com.sap.oss.phosphor.fosstars.model.score.oss.OssRulesOfPlayScore.findWarningsIn;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyMap;
import static java.util.Collections.unmodifiableMap;
import static java.util.Objects.requireNonNull;
import static org.apache.commons.lang3.StringUtils.EMPTY;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.NullNode;
import com.sap.oss.phosphor.fosstars.advice.Advisor;
import com.sap.oss.phosphor.fosstars.model.Confidence;
import com.sap.oss.phosphor.fosstars.model.Feature;
import com.sap.oss.phosphor.fosstars.model.Label;
import com.sap.oss.phosphor.fosstars.model.RatingRepository;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.feature.BooleanFeature;
import com.sap.oss.phosphor.fosstars.model.rating.oss.OssRulesOfPlayRating;
import com.sap.oss.phosphor.fosstars.model.value.BooleanValue;
import com.sap.oss.phosphor.fosstars.model.value.RatingValue;
import com.sap.oss.phosphor.fosstars.model.value.ScoreValue;
import com.sap.oss.phosphor.fosstars.util.Yaml;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import org.apache.commons.lang3.StringUtils;

/**
 * The class prints a rating value
 * for {@link com.sap.oss.phosphor.fosstars.model.rating.oss.OssRulesOfPlayRating} in Markdown.
 */
public class OssRulesOfPlayRatingMarkdownFormatter extends AbstractMarkdownFormatter {

  /**
   * A resource with a default Markdown template.
   */
  private static final String RATING_VALUE_TEMPLATE_RESOURCE
      = "OssRulesOfPlayMarkdownRatingValueTemplate.md";

  /**
   * A default Markdown template for a rating value.
   */
  private static final String DEFAULT_RATING_VALUE_TEMPLATE
      = loadFrom(RATING_VALUE_TEMPLATE_RESOURCE, OssRulesOfPlayRatingMarkdownFormatter.class);

  /**
   * Maps a rule to its identifier.
   */
  private final Map<Feature<Boolean>, String> featureToRuleId;
  
  /**
   * The rule documentation URL.
   */
  private final String ruleDocumentationUrl;

  /**
   * A Markdown template for reports.
   */
  private final String template;

  /**
   * Initializes a new formatter. The constructor looks for a default file with rule IDs
   * and tries to load it.
   *
   * @param advisor An advisor for calculated ratings.
   * @throws IOException If something went wrong.
   */
  public OssRulesOfPlayRatingMarkdownFormatter(Advisor advisor) throws IOException {
    this(loadConfig().orElse(NullNode.getInstance()), advisor);
  }

  /**
   * Initializes a new formatter.
   *
   * @param path A path to a config file.
   * @param advisor An advisor for calculated ratings.
   * @throws IOException If something went wrong.
   */
  public OssRulesOfPlayRatingMarkdownFormatter(Path path, Advisor advisor) throws IOException {
    this(loadConfigFrom(path), advisor);
  }

  /**
   * Create a new formatter.
   *
   * @param config A config.
   * @param advisor An advisor.
   * @throws IOException If something went wrong.
   */
  private OssRulesOfPlayRatingMarkdownFormatter(JsonNode config, Advisor advisor)
      throws IOException {

    this(advisor, readRuleIdsFrom(config), readRuleDocumentationUrlFrom(config),
        DEFAULT_RATING_VALUE_TEMPLATE);
  }

  /**
   * Initializes a new formatter. The constructor looks for a default file with rule IDs
   * and tries to load it.
   *
   * @param advisor An advisor for calculated ratings.
   * @param featureToRuleId Maps a rule to its identifier.
   * @param ruleDocumentationUrl The rule documentation URL. Maybe be null.
   * @param template A Markdown template for reports.
   */
  public OssRulesOfPlayRatingMarkdownFormatter(
      Advisor advisor, Map<Feature<Boolean>, String> featureToRuleId, 
      @Nullable String ruleDocumentationUrl, String template) {

    super(advisor);

    requireNonNull(featureToRuleId, "Oh no! Rule IDs can't be null!");
    requireNonNull(template, "Oh no! Template can't be null!");

    this.featureToRuleId = unmodifiableMap(featureToRuleId);
    this.ruleDocumentationUrl = ruleDocumentationUrl;
    this.template = template;
  }

  /**
   * Return the URL to docs if available.
   *
   * @return The URL to docs if available.
   */
  public Optional<String> ruleDocumentationUrl() {
    return StringUtils.isEmpty(ruleDocumentationUrl)
        ? Optional.empty()
        : Optional.of(ruleDocumentationUrl);
  }

  protected String print(RatingValue ratingValue, String advice) {
    requireNonNull(ratingValue, "Hey! Rating can't be null!");

    ScoreValue scoreValue = ratingValue.scoreValue();
    return template
        .replaceAll("%MAX_CONFIDENCE%", formatted(Confidence.MAX))
        .replace("%STATUS%", format(ratingValue.label()))
        .replace("%CONFIDENCE_LABEL%", confidenceLabelFor(ratingValue.confidence()))
        .replace("%CONFIDENCE_VALUE%", formatted(ratingValue.confidence()))
        .replace("%VIOLATED_RULES%", violatedRulesFrom(scoreValue))
        .replace("%WARNINGS%", warningsFrom(scoreValue))
        .replace("%PASSED_RULES%", passedRulesFrom(scoreValue))
        .replace("%UNCLEAR_RULES%", unclearRulesFrom(scoreValue))
        .replace("%EXPLANATION%", valueExplanationsIn(scoreValue))
        .replace("%ADVICE%", advice);
  }

  @Override
  protected String markdownAdviceHeader() {
    return "## How to fix it";
  }

  /**
   * Prints out violated rules.
   *
   * @param scoreValue A score value.
   * @return A formatted list of violated rules.
   */
  private String violatedRulesFrom(ScoreValue scoreValue) {
    List<Value<Boolean>> violatedRules = findViolatedRulesIn(scoreValue.usedValues());

    if (violatedRules.isEmpty()) {
      return EMPTY;
    }

    String content = violatedRules.stream()
        .map(this::formatRuleForList)
        .sorted()
        .collect(Collectors.joining("\n"));

    return String.format("## Violated rules%n%n%s%n", content);
  }

  /**
   * Prints out warnings.
   *
   * @param scoreValue A score value.
   * @return A formatted list of violated rules.
   */
  private String warningsFrom(ScoreValue scoreValue) {
    List<Value<Boolean>> warnings = findWarningsIn(scoreValue.usedValues());

    if (warnings.isEmpty()) {
      return EMPTY;
    }

    String content = warnings.stream()
        .map(this::formatRuleForList)
        .sorted()
        .collect(Collectors.joining("\n"));

    return String.format("## Warnings%n%n%s%n", content);
  }
  
  private String formatRuleForList(Value<?> value) {
    return String.format("1.  **%s** %s", identifierOf(value.feature()), formatRule(value));
  }

  /**
   * Prints a formatted violated rule.
   *
   * @param value The rule.
   * @return A formatted violated rule.
   */
  private String formatRule(Value<?> value) {
    return String.format("%s **%s**", nameOf(value.feature()), printValueAnswer(value));
  }

  /**
   * Format a boolean value.
   *
   * @param value The value.
   * @return A formatted value.
   * @throws IllegalArgumentException If the value is not boolean.
   */
  public String printValueAnswer(Value<?> value) {
    String answer = "unknown";
    if (!value.isUnknown()) {
      if (!BooleanValue.class.equals(value.getClass())) {
        throw new IllegalArgumentException("Oh no! Expected a boolean value!");
      }
      answer = ((BooleanValue) value).get() ? "Yes" : "No";
    }
    return answer;
  }

  /**
   * Prints out passed rules.
   *
   * @param scoreValue A score value.
   * @return A formatted list of passed rules.
   */
  private String passedRulesFrom(ScoreValue scoreValue) {
    List<Value<Boolean>> violatedRules = findViolatedRulesIn(scoreValue.usedValues());
    List<Value<Boolean>> warnings = findWarningsIn(scoreValue.usedValues());

    String content = scoreValue.usedValues().stream()
        .filter(rule -> !violatedRules.contains(rule))
        .filter(rule -> !warnings.contains(rule))
        .filter(rule -> !rule.isUnknown())
        .map(BooleanValue.class::cast)
        .map(this::formatRuleForList)
        .sorted()
        .collect(Collectors.joining("\n"));

    if (content.trim().isEmpty()) {
      return EMPTY;
    }

    return String.format("## Passed rules%n%n%s%n", content);
  }

  /**
   * Prints out unclear rules.
   *
   * @param scoreValue A score value.
   * @return A formatted list of unclear rules.
   */
  private String unclearRulesFrom(ScoreValue scoreValue) {
    List<Value<?>> unclearRules = scoreValue.usedValues().stream()
        .filter(Value::isUnknown).collect(Collectors.toList());

    if (unclearRules.isEmpty()) {
      return EMPTY;
    }

    String content = unclearRules.stream()
        .map(this::formatRuleForList)
        .sorted()
        .collect(Collectors.joining("\n"));

    return String.format("## Unclear rules%n%n%s%n", content);
  }

  /**
   * Print out a list of explanations for a score value.
   *
   * @param scoreValue The score value.
   * @return A formatted list of explanations.
   */
  private String valueExplanationsIn(ScoreValue scoreValue) {
    StringBuilder content = new StringBuilder();
    for (Value<?> value : scoreValue.usedFeatureValues()) {
      for (String note : value.explanation()) {
        content.append(String.format("1.  %s%n", note));
      }
    }

    if (content.length() == 0) {
      return EMPTY;
    }

    return String.format("## Explanation%n%n%s%n", content);
  }

  /**
   * Search for ID for a rule.
   *
   * @param rule The rule.
   * @return ID of the rule if available, an empty string otherwise.
   */
  public String identifierOf(Feature<?> rule) {
    return Optional.ofNullable(featureToRuleId.get(rule))
        .map(id -> String.format("[%s]", id))
        .orElse(EMPTY);
  }

  /**
   * Prints our a label of the OSS Rules of Play rating.
   *
   * @param label The label to be printed.
   * @return A formatted label.
   * @throws IllegalArgumentException In case of unknown label.
   */
  public static String format(Label label) {
    if (!OssRulesOfPlayRating.OssRulesOfPlayLabel.class.equals(label.getClass())) {
      throw new IllegalArgumentException("Oops! Unknown label!");
    }

    switch ((OssRulesOfPlayRating.OssRulesOfPlayLabel) label) {
      case FAILED:
        return "Failed";
      case PASSED:
        return "Passed";
      case PASSED_WITH_WARNING:
        return "Passed with warnings";
      case UNCLEAR:
        return "Not clear";
      default:
        throw new IllegalArgumentException(String.format("Oops! Unexpected label: %s", label));
    }
  }

  /**
   * Looks for a configuration file.
   *
   * @return The config if found.
   * @throws UncheckedIOException If something went wrong.
   */
  private static Optional<JsonNode> loadConfig() throws IOException {
    Class<?> clazz = OssRulesOfPlayRatingMarkdownFormatter.class;
    for (String name : asList(clazz.getSimpleName(), clazz.getCanonicalName())) {
      for (String suffix : asList("yml", "yaml")) {
        Path path = Paths.get(String.format("%s.config.%s", name, suffix));
        if (Files.isRegularFile(path)) {
          return Optional.of(loadConfigFrom(path));
        }
      }
    }

    return Optional.empty();
  }

  /**
   * Load a config from a file.
   *
   * @param path The file.
   * @return A config.
   * @throws IOException If the config could not be loaded.
   */
  private static JsonNode loadConfigFrom(Path path) throws IOException {
    try (BufferedReader reader = Files.newBufferedReader(path)) {
      return Yaml.mapper().readTree(reader);
    }
  }

  /**
   * Load rule IDs from a config.
   *
   * @param config The config.
   * @return A map with rule IDs.
   * @throws IOException If something went wrong.
   */
  private static Map<Feature<Boolean>, String> readRuleIdsFrom(JsonNode config) throws IOException {
    if (!config.has("ruleIds")) {
      return emptyMap();
    }

    if (!config.isObject()) {
      throw new IOException("Oops! ruleIds is not an object!");
    }

    OssRulesOfPlayRating rating = RatingRepository.INSTANCE.rating(OssRulesOfPlayRating.class);

    Map<Feature<Boolean>, String> map = new HashMap<>();
    Set<String> ruleIds = new HashSet<>();

    Iterator<String> ruleIdIterator = config.get("ruleIds").fieldNames();
    while (ruleIdIterator.hasNext()) {
      String ruleId = ruleIdIterator.next();

      if (ruleIds.contains(ruleId)) {
        throw new IOException(String.format("Oops! Duplicate rule ID: %s", ruleId));
      }

      JsonNode node = config.get("ruleIds").get(ruleId);
      if (!node.isTextual()) {
        throw new IOException(String.format("Oops! '%s' is not a string!", ruleId));
      }

      String featureName = node.asText();
      boolean found = false;
      for (Feature<?> feature : rating.allFeatures()) {
        if (!BooleanFeature.class.equals(feature.getClass())) {
          throw new IOException(String.format("Oops! Not a boolean feature: %s", featureName));
        }

        if (!feature.name().equals(featureName)) {
          continue;
        }

        if (map.containsKey(feature)) {
          throw new IOException(String.format("Oops! Duplicate feature: %s", featureName));
        }

        map.put((BooleanFeature) feature, ruleId);
        ruleIds.add(ruleId);
        found = true;
        break;
      }

      if (!found) {
        throw new IOException(String.format("Oops! Could not find this feature: %s", featureName));
      }
    }

    return map;
  }
  
  /**
   * Load rule documentation URI from a config.
   *
   * @param config The config.
   * @return The rule documentation URL.
   * @throws IOException If something went wrong.
   */
  private static String readRuleDocumentationUrlFrom(JsonNode config) throws IOException {
    if (!config.has("documentationUrl")) {
      return EMPTY;
    }

    if (!config.isObject()) {
      throw new IOException("Oops! Configuration is not an object!");
    }

    return config.get("documentationUrl").asText();
  }

}
