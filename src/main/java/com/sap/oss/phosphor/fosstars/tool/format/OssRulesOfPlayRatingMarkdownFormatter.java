package com.sap.oss.phosphor.fosstars.tool.format;

import static com.sap.oss.phosphor.fosstars.model.score.oss.OssRulesOfPlayScore.findViolatedRulesIn;
import static com.sap.oss.phosphor.fosstars.model.score.oss.OssRulesOfPlayScore.findWarningsIn;
import static com.sap.oss.phosphor.fosstars.tool.format.Markdown.DOUBLE_NEW_LINE;
import static com.sap.oss.phosphor.fosstars.tool.format.Markdown.NEW_LINE;
import static com.sap.oss.phosphor.fosstars.tool.format.Markdown.SPACE;
import static java.lang.String.format;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.emptyMap;
import static java.util.Collections.unmodifiableMap;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.isEmpty;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.NullNode;
import com.sap.oss.phosphor.fosstars.advice.Advice;
import com.sap.oss.phosphor.fosstars.advice.Advisor;
import com.sap.oss.phosphor.fosstars.model.Confidence;
import com.sap.oss.phosphor.fosstars.model.Feature;
import com.sap.oss.phosphor.fosstars.model.Label;
import com.sap.oss.phosphor.fosstars.model.RatingRepository;
import com.sap.oss.phosphor.fosstars.model.Subject;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.feature.BooleanFeature;
import com.sap.oss.phosphor.fosstars.model.rating.oss.OssRulesOfPlayRating;
import com.sap.oss.phosphor.fosstars.model.value.BooleanValue;
import com.sap.oss.phosphor.fosstars.model.value.RatingValue;
import com.sap.oss.phosphor.fosstars.util.Yaml;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.BooleanSupplier;
import javax.annotation.Nullable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
   * A logger.
   */
  private static final Logger LOGGER
      = LogManager.getLogger(OssRulesOfPlayRatingMarkdownFormatter.class);

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
    return isEmpty(ruleDocumentationUrl)
        ? Optional.empty()
        : Optional.of(ruleDocumentationUrl);
  }

  @Override
  public String print(Subject subject) {
    if (!subject.ratingValue().isPresent()) {
      return EMPTY;
    }

    return print(subject.ratingValue().get(), adviceFor(subject));
  }

  /**
   * Print a rating value with a list of advice.
   *
   * @param ratingValue The rating value.
   * @param advice The advice.
   * @return A formatted rating value with advice.
   */
  String print(RatingValue ratingValue, List<Advice> advice) {
    List<FormattedRule> violations = formatted(violationsIn(ratingValue), advice);
    List<FormattedRule> warnings = formatted(warningsIn(ratingValue), advice);
    List<FormattedRule> passedRules = formatted(passedRulesIn(ratingValue), advice);
    List<FormattedRule> unclearRules = formatted(unclearRulesIn(ratingValue), advice);

    return template
        .replace("%MAX_CONFIDENCE%", formatted(Confidence.MAX))
        .replace("%STATUS%", formatted(ratingValue.label()))
        .replace("%CONFIDENCE_LABEL%", confidenceLabelFor(ratingValue.confidence()))
        .replace("%CONFIDENCE_VALUE%", formatted(ratingValue.confidence()))
        .replace("%VIOLATED_RULES%", makeListFrom(violations, "Violated rules"))
        .replace("%WARNINGS%", makeListFrom(warnings, "Warnings"))
        .replace("%PASSED_RULES%", makeListFrom(passedRules, "Passed rules"))
        .replace("%UNCLEAR_RULES%", makeListFrom(unclearRules, "Unclear"))
        .replace("%ADVICE%", makeAdviceFrom(violations, warnings, passedRules, unclearRules));
  }

  /**
   * Looks for advice for a subject.
   *
   * @param subject The subject.
   * @return A list of advice.
   */
  private List<Advice> adviceFor(Subject subject) {
    try {
      return advisor.adviceFor(subject);
    } catch (IOException e) {
      LOGGER.warn("Oops! Could not print advice!", e);
      return emptyList();
    }
  }

  /**
   * Convert a list of formatted rules to Markdown-formatted advice.
   *
   * @param rules The rules.
   * @return A list of Markdown-formatted advice.
   */
  private List<MarkdownElement> adviceFor(List<FormattedRule> rules) {
    return rules.stream()
        .filter(FormattedRule::hasAdvice)
        .map(rule -> rule.adviceSection)
        .collect(toList());
  }

  /**
   * Looks for advice for a specified rule.
   *
   * @param rule The rule.
   * @param adviceList A list of advice.
   * @return A sub-list of advice that apply to the rule.
   */
  private static List<Advice> selectAdviceFor(Value<Boolean> rule, List<Advice> adviceList) {
    return adviceList.stream().filter(advice -> advice.value().equals(rule)).collect(toList());
  }

  /**
   * Make a Markdown-formatted document that contains advice for specified rules.
   *
   * @param violations A list of violated rules.
   * @param warnings A list of warnings.
   * @param passedRules A list of passed rules.
   * @param unclearRules A list of unclear rules.
   * @return A Markdown-formatted document with advice.
   */
  private String makeAdviceFrom(List<FormattedRule> violations, List<FormattedRule> warnings,
      List<FormattedRule> passedRules, List<FormattedRule> unclearRules) {

    MarkdownElement advice = Markdown.join()
        .of(adviceFor(violations))
        .of(adviceFor(warnings))
        .of(adviceFor(passedRules))
        .of(adviceFor(unclearRules))
        .delimitedBy(NEW_LINE);

    MarkdownHeader header = Markdown.header().level(2)
        .withCaption("What is wrong, and how to fix it");
    MarkdownSection section = Markdown.section().with(header).thatContains(advice);
    BooleanSupplier sectionIsNotEmpty = () -> !Markdown.isEmpty(section.text().make());

    return Markdown.choose(section).when(sectionIsNotEmpty).otherwise(MarkdownString.EMPTY).make();
  }

  /**
   * Make a Markdown document that contains a header and a list of rules.
   *
   * @param rules The rules.
   * @param title The header.
   * @return A Markdown document.
   */
  private String makeListFrom(List<FormattedRule> rules, String title) {
    if (rules.isEmpty()) {
      return EMPTY;
    }

    List<MarkdownElement> elements = rules.stream().map(rule -> rule.listText).collect(toList());
    MarkdownList list = Markdown.orderedListOf(elements);
    MarkdownHeader header = Markdown.header().level(2).withCaption(title);
    return Markdown.join(header, list).delimitedBy(NEW_LINE).make();
  }

  /**
   * Create a Markdown-formatted advice for a rule.
   *
   * @param rule The rule.
   * @param adviceList A list of advice.
   * @return A Markdown-formatted advice.
   */
  private String adviceTextFor(Value<Boolean> rule, List<Advice> adviceList) {
    List<MarkdownElement> elements = new ArrayList<>();

    for (String note : rule.explanation()) {
      elements.add(Markdown.string(note));
    }

    for (Advice advice : adviceList) {
      MarkdownString text = Markdown.string(advice.content().text());
      UnorderedMarkdownList links = Markdown.unorderedListOf(linksIn(advice));
      MarkdownElement adviceContent = Markdown.group(text, links);
      elements.add(adviceContent);
    }

    return Markdown.join(elements).delimitedBy(DOUBLE_NEW_LINE).make();
  }

  /**
   * Looks for violations in a rating value.
   *
   * @param ratingValue The rating value.
   * @return A list of violations.
   */
  private static List<Value<Boolean>> violationsIn(RatingValue ratingValue) {
    return findViolatedRulesIn(ratingValue.scoreValue().usedValues());
  }

  /**
   * Looks for warnings in a rating value.
   *
   * @param ratingValue The rating value.
   * @return A list of warnings.
   */
  private static List<Value<Boolean>> warningsIn(RatingValue ratingValue) {
    return findWarningsIn(ratingValue.scoreValue().usedValues());
  }

  /**
   * Looks for passed rules in a rating value.
   *
   * @param ratingValue The raging value.
   * @return A list of passed rules.
   */
  private static List<Value<Boolean>> passedRulesIn(RatingValue ratingValue) {
    List<Value<Boolean>> violatedRules = violationsIn(ratingValue);
    List<Value<Boolean>> warnings = warningsIn(ratingValue);

    return ratingValue.scoreValue().usedValues().stream()
        .filter(rule -> !violatedRules.contains(rule))
        .filter(rule -> !warnings.contains(rule))
        .filter(rule -> !rule.isUnknown())
        .map(value -> (Value<Boolean>) value)
        .collect(toList());
  }

  /**
   * Looks for unclear rules in a rating value.
   *
   * @param ratingValue The rating value.
   * @return A list of unclear rules.
   */
  private static List<Value<Boolean>> unclearRulesIn(RatingValue ratingValue) {
    return ratingValue.scoreValue().usedValues().stream()
        .filter(Value::isUnknown)
        .map(value -> (Value<Boolean>) value)
        .collect(toList());
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
   * Search for ID for a rule.
   *
   * @param rule The rule.
   * @return ID of the rule if available.
   */
  public Optional<String> identifierOf(Feature<?> rule) {
    return Optional.ofNullable(featureToRuleId.get(rule)).map(id -> format("[%s]", id));
  }

  /**
   * Format a rule with advice.
   *
   * @param rule The rule.
   * @param adviceList The advice.
   * @return A Markdown-formatted rule.
   */
  private FormattedRule formatted(Value<Boolean> rule, List<Advice> adviceList) {
    String advice = adviceTextFor(rule, selectAdviceFor(rule, adviceList));
    BooleanSupplier weHaveAdvice = () -> !Markdown.isEmpty(advice);

    String rawId = featureToRuleId.getOrDefault(rule.feature(), EMPTY);
    BooleanSupplier weHaveId = () -> !Markdown.isEmpty(rawId);

    MarkdownElement id = Markdown.choose(Markdown.string(rawId))
        .when(weHaveId)
        .otherwise(Markdown.string(rule.feature().name()));
    MarkdownHeader header = Markdown.header().level(3).withCaption(id);
    MarkdownSection adviceSection = Markdown.section().with(header).thatContains(advice);
    MarkdownRuleIdentifier ruleId = Markdown.rule(id);
    MarkdownHeaderReference identifierWithReference
        = Markdown.reference().to(adviceSection).withCaption(ruleId);
    MarkdownChoice identifier
        = Markdown.choose(identifierWithReference).when(weHaveAdvice).otherwise(ruleId);
    MarkdownString formattedRule = Markdown.string(formatted(rule));
    JoinedMarkdownElements listText = Markdown.join(identifier, formattedRule).delimitedBy(SPACE);

    return new FormattedRule(listText, adviceSection);
  }

  /**
   * Format a list of rules with advice.
   *
   * @param rules The rules.
   * @param advice The advice.
   * @return A list of formatted rules.
   */
  private List<FormattedRule> formatted(List<Value<Boolean>> rules, List<Advice> advice) {
    return rules.stream().map(rule -> formatted(rule, advice)).collect(toList());
  }

  /**
   * Prints a formatted violated rule.
   *
   * @param value The rule.
   * @return A formatted violated rule.
   */
  private String formatted(Value<?> value) {
    return format("%s **%s**", nameOf(value.feature()), printValueAnswer(value));
  }

  /**
   * Prints our a label of the OSS Rules of Play rating.
   *
   * @param label The label to be printed.
   * @return A formatted label.
   * @throws IllegalArgumentException In case of unknown label.
   */
  public static String formatted(Label label) {
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
        throw new IllegalArgumentException(format("Oops! Unexpected label: %s", label));
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
        Path path = Paths.get(format("%s.config.%s", name, suffix));
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
        throw new IOException(format("Oops! Duplicate rule ID: %s", ruleId));
      }

      JsonNode node = config.get("ruleIds").get(ruleId);
      if (!node.isTextual()) {
        throw new IOException(format("Oops! '%s' is not a string!", ruleId));
      }

      String featureName = node.asText();
      boolean found = false;
      for (Feature<?> feature : rating.allFeatures()) {
        if (!BooleanFeature.class.equals(feature.getClass())) {
          throw new IOException(format("Oops! Not a boolean feature: %s", featureName));
        }

        if (!feature.name().equals(featureName)) {
          continue;
        }

        if (map.containsKey(feature)) {
          throw new IOException(format("Oops! Duplicate feature: %s", featureName));
        }

        map.put((BooleanFeature) feature, ruleId);
        ruleIds.add(ruleId);
        found = true;
        break;
      }

      if (!found) {
        throw new IOException(format("Oops! Could not find this feature: %s", featureName));
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

  /**
   * This class holds Markdown elements for one rule.
   * The elements may be rendered in different parts of the report.
   */
  private static class FormattedRule {

    /**
     * A text for the rule in the list of passed, failed, unclear rules or warnings.
     */
    final MarkdownElement listText;

    /**
     * A Markdown section with advice for the rule.
     */
    final MarkdownSection adviceSection;

    /**
     * Create a formatted rule.
     *
     * @param listText A text for the rule in the list of rules.
     * @param adviceSection A Markdown section with advice for the rule.
     */
    private FormattedRule(MarkdownElement listText, MarkdownSection adviceSection) {
      this.listText = listText;
      this.adviceSection = adviceSection;
    }

    /**
     * Checks whether or not the rule has advice.
     *
     * @return True if th rule has advice, false otherwise.
     */
    boolean hasAdvice() {
      return !Markdown.isEmpty(adviceSection.text().make());
    }
  }
}
