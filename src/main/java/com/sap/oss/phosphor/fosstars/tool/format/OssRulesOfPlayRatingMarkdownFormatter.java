package com.sap.oss.phosphor.fosstars.tool.format;

import static com.sap.oss.phosphor.fosstars.model.score.oss.OssRulesOfPlayScore.findViolatedRulesIn;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyMap;

import com.fasterxml.jackson.databind.JsonNode;
import com.sap.oss.phosphor.fosstars.model.Confidence;
import com.sap.oss.phosphor.fosstars.model.Feature;
import com.sap.oss.phosphor.fosstars.model.RatingRepository;
import com.sap.oss.phosphor.fosstars.model.Subject;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.feature.BooleanFeature;
import com.sap.oss.phosphor.fosstars.model.rating.oss.OssRulesOfPlayRating;
import com.sap.oss.phosphor.fosstars.model.value.BooleanValue;
import com.sap.oss.phosphor.fosstars.model.value.RatingValue;
import com.sap.oss.phosphor.fosstars.model.value.ScoreValue;
import com.sap.oss.phosphor.fosstars.util.Yaml;
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
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;

/**
 * The class prints a rating value
 * for {@link com.sap.oss.phosphor.fosstars.model.rating.oss.OssRulesOfPlayRating} in Markdown.
 */
public class OssRulesOfPlayRatingMarkdownFormatter extends CommonFormatter {

  /**
   * A resource with a Markdown template.
   */
  private static final String RATING_VALUE_TEMPLATE_RESOURCE
      = "OssRulesOfPlayMarkdownRatingValueTemplate.md";

  /**
   * Maps a rule to its identifier.
   */
  private static final Map<Feature<Boolean>, String> DEFAULT_FEATURE_TO_RULE_ID = defaultRuleIds();

  /**
   * A Markdown template for a rating value.
   */
  private static final String RATING_VALUE_TEMPLATE
      = loadFrom(RATING_VALUE_TEMPLATE_RESOURCE, OssRulesOfPlayRatingMarkdownFormatter.class);

  @Override
  public String print(Subject subject) {
    if (!subject.ratingValue().isPresent()) {
      return StringUtils.EMPTY;
    }

    return print(subject.ratingValue().get());
  }

  /**
   * Print a rating value and advice.
   *
   * @param ratingValue The rating value.
   * @return A string to be displayed.
   */
  @Override
  public String print(RatingValue ratingValue) {
    Objects.requireNonNull(ratingValue, "Hey! Rating can't be null!");

    ScoreValue scoreValue = ratingValue.scoreValue();
    return RATING_VALUE_TEMPLATE
        .replaceAll("%MAX_CONFIDENCE%", formatted(Confidence.MAX))
        .replace("%STATUS%", ratingValue.label().name())
        .replace("%CONFIDENCE_LABEL%", confidenceLabelFor(ratingValue.confidence()))
        .replace("%CONFIDENCE_VALUE%", formatted(ratingValue.confidence()))
        .replace("%VIOLATED_RULES%", violatedRulesFrom(scoreValue))
        .replace("%PASSED_RULES%", passedRulesFrom(scoreValue))
        .replace("%UNCLEAR_RULES%", unclearRulesFrom(scoreValue));
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
      return StringUtils.EMPTY;
    }

    String content = violatedRules.stream()
        .map(OssRulesOfPlayRatingMarkdownFormatter::formatRule)
        .sorted()
        .collect(Collectors.joining("\n"));

    return String.format("## Violated rules%n%n%s%n", content);
  }

  /**
   * Prints out passed rules.
   *
   * @param scoreValue A score value.
   * @return A formatted list of passed rules.
   */
  private String passedRulesFrom(ScoreValue scoreValue) {
    List<Value<Boolean>> violatedRules = findViolatedRulesIn(scoreValue.usedValues());

    String content = scoreValue.usedValues().stream()
        .filter(rule -> !violatedRules.contains(rule))
        .filter(rule -> !rule.isUnknown())
        .map(BooleanValue.class::cast)
        .map(OssRulesOfPlayRatingMarkdownFormatter::formatRule)
        .sorted()
        .collect(Collectors.joining("\n"));

    if (content.trim().isEmpty()) {
      return StringUtils.EMPTY;
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
      return StringUtils.EMPTY;
    }

    String content = unclearRules.stream()
        .map(OssRulesOfPlayRatingMarkdownFormatter::formatRule)
        .sorted()
        .collect(Collectors.joining("\n"));

    return String.format("## Unclear rules%n%n%s%n", content);
  }

  /**
   * Prints a formatted violated rule.
   *
   * @param value The rule.
   * @return A formatted violated rule.
   */
  private static String formatRule(Value<?> value) {
    return String.format("1.  %s %s **%s**",
        identifierOf(value.feature()),
        nameOf(value.feature()),
        value.isUnknown() ? "unknown" : value.get().toString());
  }

  /**
   * Search for ID for a rule.
   *
   * @param rule The rule.
   * @return ID of the rule if available, an empty string otherwise.
   */
  private static String identifierOf(Feature<?> rule) {
    return Optional.ofNullable(DEFAULT_FEATURE_TO_RULE_ID.get(rule))
        .map(id -> String.format("**[%s]**", id))
        .orElse(StringUtils.EMPTY);
  }

  /**
   * Looks for a configuration file and loads rule IDs if found.
   *
   * @return A map with rule IDs.
   * @throws UncheckedIOException If something went wrong.
   */
  private static Map<Feature<Boolean>, String> defaultRuleIds() {
    Class<?> clazz = OssRulesOfPlayRatingMarkdownFormatter.class;
    for (String name : asList(clazz.getSimpleName(), clazz.getCanonicalName())) {
      for (String suffix : asList("yml", "yaml")) {
        Path path = Paths.get(String.format("%s.config.%s", name, suffix));
        if (Files.isRegularFile(path)) {
          try {
            return loadRuleIdsFrom(path);
          } catch (IOException e) {
            throw new UncheckedIOException(e);
          }
        }
      }
    }

    return emptyMap();
  }

  /**
   * Load rule IDs from a file.
   *
   * @param path A path to the file.
   * @return A map with rule IDs.
   * @throws IOException If something went wrong.
   */
  private static Map<Feature<Boolean>, String> loadRuleIdsFrom(Path path) throws IOException {
    JsonNode config = Yaml.mapper().readTree(Files.newBufferedReader(path));
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
        if (feature instanceof BooleanFeature == false) {
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
}
