package com.sap.oss.phosphor.fosstars.tool.format;

import static com.sap.oss.phosphor.fosstars.model.score.oss.OssRulesOfPlayScore.findViolatedRulesIn;
import static com.sap.oss.phosphor.fosstars.model.score.oss.OssRulesOfPlayScore.findWarningsIn;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyMap;
import static java.util.Collections.unmodifiableMap;

import com.fasterxml.jackson.databind.JsonNode;
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
    this(advisor, defaultRuleIds(), DEFAULT_RATING_VALUE_TEMPLATE);
  }

  /**
   * Initializes a new formatter.
   *
   * @param path A path to a file with rule IDs.
   * @param advisor An advisor for calculated ratings.
   * @throws IOException If something went wrong.
   */
  public OssRulesOfPlayRatingMarkdownFormatter(Path path, Advisor advisor) throws IOException {
    this(advisor, loadRuleIdsFrom(path), DEFAULT_RATING_VALUE_TEMPLATE);
  }

  /**
   * Initializes a new formatter. The constructor looks for a default file with rule IDs
   * and tries to load it.
   *
   * @param advisor An advisor for calculated ratings.
   * @param featureToRuleId Maps a rule to its identifier.
   * @param template A Markdown template for reports.
   */
  public OssRulesOfPlayRatingMarkdownFormatter(
      Advisor advisor, Map<Feature<Boolean>, String> featureToRuleId, String template) {

    super(advisor);

    Objects.requireNonNull(featureToRuleId, "Oh no! Rule IDs can't be null!");
    Objects.requireNonNull(template, "Oh no! Template can't be null!");

    this.featureToRuleId = unmodifiableMap(featureToRuleId);
    this.template = template;
  }

  protected String print(RatingValue ratingValue, String advice) {
    Objects.requireNonNull(ratingValue, "Hey! Rating can't be null!");

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
      return StringUtils.EMPTY;
    }

    String content = violatedRules.stream()
        .map(this::formatRule)
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
      return StringUtils.EMPTY;
    }

    String content = warnings.stream()
        .map(this::formatRule)
        .sorted()
        .collect(Collectors.joining("\n"));

    return String.format("## Warnings%n%n%s%n", content);
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
        .map(this::formatRule)
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
        .map(this::formatRule)
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
      return StringUtils.EMPTY;
    }

    return String.format("## Explanation%n%n%s%n", content);
  }

  /**
   * Prints a formatted violated rule.
   *
   * @param value The rule.
   * @return A formatted violated rule.
   */
  private String formatRule(Value<?> value) {
    String answer = "unknown";
    if (!value.isUnknown()) {
      if (!BooleanValue.class.equals(value.getClass())) {
        throw new IllegalArgumentException("Oh no! Expected a boolean value!");
      }
      answer = ((BooleanValue) value).get() ? "Yes" : "No";
    }
    return String.format("1.  %s %s **%s**",
        identifierOf(value.feature()),
        nameOf(value.feature()),
        answer);
  }

  /**
   * Search for ID for a rule.
   *
   * @param rule The rule.
   * @return ID of the rule if available, an empty string otherwise.
   */
  private String identifierOf(Feature<?> rule) {
    return Optional.ofNullable(featureToRuleId.get(rule))
        .map(id -> String.format("**[%s]**", id))
        .orElse(StringUtils.EMPTY);
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
   * Looks for a configuration file and loads rule IDs if found.
   *
   * @return A map with rule IDs.
   * @throws UncheckedIOException If something went wrong.
   */
  private static Map<Feature<Boolean>, String> defaultRuleIds() throws IOException {
    Class<?> clazz = OssRulesOfPlayRatingMarkdownFormatter.class;
    for (String name : asList(clazz.getSimpleName(), clazz.getCanonicalName())) {
      for (String suffix : asList("yml", "yaml")) {
        Path path = Paths.get(String.format("%s.config.%s", name, suffix));
        if (Files.isRegularFile(path)) {
          return loadRuleIdsFrom(path);
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
}
