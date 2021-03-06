package com.sap.oss.phosphor.fosstars.tool.format;

import static com.sap.oss.phosphor.fosstars.model.score.oss.OssRulesOfPlayScore.findViolatedRulesIn;
import static com.sap.oss.phosphor.fosstars.model.score.oss.OssRulesOfPlayScore.findWarningsIn;
import static java.lang.String.format;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.emptyMap;
import static java.util.Collections.unmodifiableMap;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.repeat;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.NullNode;
import com.sap.oss.phosphor.fosstars.advice.Advice;
import com.sap.oss.phosphor.fosstars.advice.Advisor;
import com.sap.oss.phosphor.fosstars.advice.Link;
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.BooleanSupplier;
import javax.annotation.Nullable;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The class prints a rating value
 * for {@link com.sap.oss.phosphor.fosstars.model.rating.oss.OssRulesOfPlayRating} in Markdown.
 */
public class OssRulesOfPlayRatingMarkdownFormatter extends CommonFormatter {

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
   * A whitespace.
   */
  private static final String SPACE = " ";

  /**
   * A new line.
   */
  private static final String NEW_LINE = "\n";

  /**
   * A double new line.
   */
  private static final String DOUBLE_NEW_LINE = NEW_LINE + NEW_LINE;

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
    BooleanSupplier sectionIsNotEmpty = () -> !empty(section.text.make());

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
   * Convert links from advice to Markdown elements.
   *
   * @param advice The advice.
   * @return A list of Markdown elements with links from the advice.
   */
  private List<MarkdownElement> linksIn(Advice advice) {
    return advice.content().links().stream()
        .map(this::formatted)
        .map(Markdown::string)
        .collect(toList());
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
    BooleanSupplier weHaveAdvice = () -> !empty(advice);

    MarkdownString id = Markdown.string(featureToRuleId.get(rule.feature()));
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
   * Format a link.
   *
   * @param link The link.
   * @return A formatted link.
   */
  String formatted(Link link) {
    return format("[%s](%s)", link.name, link.url);
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

  private static boolean empty(String string) {
    return isEmpty(string) || isBlank(string.trim());
  }

  private static class FormattedRule {

    final MarkdownElement listText;
    final MarkdownSection adviceSection;

    private FormattedRule(MarkdownElement listText, MarkdownSection adviceSection) {
      this.listText = listText;
      this.adviceSection = adviceSection;
    }

    boolean hasAdvice() {
      return !empty(adviceSection.text.make());
    }

  }

  interface MarkdownElement {

    String make();
  }

  static class MarkdownString implements MarkdownElement {

    static final MarkdownString EMPTY = new MarkdownString(StringUtils.EMPTY);

    private final String string;

    MarkdownString(String identifier) {
      this.string = identifier;
    }

    @Override
    public String make() {
      return string;
    }
  }

  static class MarkdownRuleIdentifier implements MarkdownElement {

    private final MarkdownElement identifier;

    MarkdownRuleIdentifier(MarkdownElement identifier) {
      this.identifier = identifier;
    }

    @Override
    public String make() {
      String string = identifier.make();
      return empty(string) ? EMPTY : format("**[%s]**", string);
    }
  }

  static class MarkdownHeader implements MarkdownElement {

    private final MarkdownElement caption;
    private final int level;

    private MarkdownHeader(MarkdownElement caption, int level) {
      this.caption = caption;
      this.level = level;
    }

    @Override
    public String make() {
      String captionString = caption.make();
      return empty(captionString) ? EMPTY : format("%s %s", repeat("#", level), captionString);
    }
  }

  static class MarkdownSection implements MarkdownElement {

    private final MarkdownHeader header;
    private final MarkdownElement text;

    private MarkdownSection(MarkdownHeader header, MarkdownElement text) {
      this.header = header;
      this.text = text;
    }

    @Override
    public String make() {
      String headerString = header.make();
      return empty(headerString) ? EMPTY : format("%s\n\n%s\n", headerString, text.make());
    }
  }

  static class MarkdownHeaderReference implements MarkdownElement {

    private final MarkdownElement caption;
    private final MarkdownSection section;

    MarkdownHeaderReference(MarkdownElement caption, MarkdownSection section) {
      this.caption = caption;
      this.section = section;
    }

    @Override
    public String make() {
      String captionString = caption.make();
      if (empty(captionString)) {
        return EMPTY;
      }

      String headerString = section.header.caption.make();
      if (empty(headerString)) {
        return EMPTY;
      }

      String anchor = headerString.toLowerCase().replaceAll(" ", "-");
      return format("[%s](#%s)", captionString, anchor);
    }
  }

  static class MarkdownChoice implements MarkdownElement {

    private final BooleanSupplier condition;
    private final MarkdownElement firstOption;
    private final MarkdownElement secondOption;

    public MarkdownChoice(
        BooleanSupplier condition, MarkdownElement firstOption, MarkdownElement secondOption) {

      this.condition = condition;
      this.firstOption = firstOption;
      this.secondOption = secondOption;
    }

    @Override
    public String make() {
      return condition.getAsBoolean() ? firstOption.make() : secondOption.make();
    }
  }

  static class JoinedMarkdownElements implements MarkdownElement {

    private final String delimiter;
    private final List<MarkdownElement> elements;

    JoinedMarkdownElements(String delimiter, List<MarkdownElement> elements) {
      this.delimiter = delimiter;
      this.elements = new ArrayList<>(elements);
    }

    @Override
    public String make() {
      return elements.stream().map(MarkdownElement::make).collect(joining(delimiter));
    }
  }

  static class GroupedMarkdownElements implements MarkdownElement {

    private final List<MarkdownElement> elements;

    GroupedMarkdownElements(MarkdownElement... elements) {
      this(asList(elements));
    }

    GroupedMarkdownElements(List<MarkdownElement> elements) {
      this.elements = new ArrayList<>(elements);
    }

    List<MarkdownElement> get() {
      return new ArrayList<>(elements);
    }

    @Override
    public String make() {
      return elements.stream().map(MarkdownElement::make).collect(joining(NEW_LINE));
    }
  }

  abstract static class MarkdownList implements MarkdownElement {

    private final List<MarkdownElement> elements;
    private final String prefix;

    MarkdownList(List<MarkdownElement> elements, String prefix) {
      this.elements = new ArrayList<>(elements);
      this.prefix = prefix;
    }

    @Override
    public String make() {
      StringBuilder content = new StringBuilder();
      String indent = repeat(" ", prefix.length());
      for (MarkdownElement element : elements) {
        if (element instanceof GroupedMarkdownElements) {
          GroupedMarkdownElements nestedElements = (GroupedMarkdownElements) element;
          Iterator<MarkdownElement> iterator = nestedElements.get().iterator();
          if (!iterator.hasNext()) {
            continue;
          }
          content.append(format("%s%s\n", prefix, iterator.next().make()));
          while (iterator.hasNext()) {
            String indentedContent = Arrays.stream(iterator.next().make().split(NEW_LINE))
                .map(line -> format("%s%s", indent, line))
                .collect(joining(NEW_LINE));
            content.append(format("%s\n", indentedContent));
          }
        } else {
          content.append(format("%s%s\n", prefix, element.make()));
        }
      }

      return content.toString();
    }
  }

  static class OrderedMarkdownList extends MarkdownList {

    OrderedMarkdownList(List<MarkdownElement> elements) {
      super(elements, "1.  ");
    }
  }

  static class UnorderedMarkdownList extends MarkdownList {

    UnorderedMarkdownList(List<MarkdownElement> elements) {
      super(elements, "*  ");
    }
  }

  static class Markdown {

    static MarkdownString string(String value) {
      return new MarkdownString(value);
    }

    static JoinedMarkdownBuilder join(MarkdownElement... elements) {
      return new JoinedMarkdownBuilder(elements);
    }

    static JoinedMarkdownBuilder join(List<MarkdownElement> elements) {
      return new JoinedMarkdownBuilder(elements);
    }

    static GroupedMarkdownElements group(MarkdownElement... elements) {
      return new GroupedMarkdownElements(elements);
    }

    static MarkdownRuleIdentifier rule(MarkdownString id) {
      return new MarkdownRuleIdentifier(id);
    }

    static MarkdownChoiceBuilder choose(MarkdownElement element) {
      return new MarkdownChoiceBuilder().firstOption(element);
    }

    static MarkdownHeaderBuilder header() {
      return new MarkdownHeaderBuilder();
    }

    static MarkdownSectionBuilder section() {
      return new MarkdownSectionBuilder();
    }

    static MarkdownHeaderReferenceBuilder reference() {
      return new MarkdownHeaderReferenceBuilder();
    }

    static OrderedMarkdownList orderedListOf(List<MarkdownElement> elements) {
      return new OrderedMarkdownList(elements);
    }

    static UnorderedMarkdownList unorderedListOf(List<MarkdownElement> elements) {
      return new UnorderedMarkdownList(elements);
    }
  }

  static class JoinedMarkdownBuilder {

    private final List<MarkdownElement> elements = new ArrayList<>();

    JoinedMarkdownBuilder(MarkdownElement... elements) {
      this(asList(elements));
    }

    JoinedMarkdownBuilder(List<MarkdownElement> elements) {
      this.elements.addAll(elements);
    }

    JoinedMarkdownBuilder of(List<MarkdownElement> elements) {
      this.elements.addAll(elements);
      return this;
    }

    JoinedMarkdownElements delimitedBy(String delimiter) {
      return new JoinedMarkdownElements(delimiter, elements);
    }
  }

  static class MarkdownHeaderReferenceBuilder {

    private MarkdownSection section;

    MarkdownHeaderReferenceBuilder to(MarkdownSection section) {
      this.section = section;
      return this;
    }

    MarkdownHeaderReference withCaption(MarkdownElement caption) {
      return new MarkdownHeaderReference(caption, section);
    }
  }

  static class MarkdownSectionBuilder {

    private MarkdownHeader header;

    MarkdownSectionBuilder with(MarkdownHeader header) {
      this.header = header;
      return this;
    }

    MarkdownSection thatContains(String text) {
      return new MarkdownSection(header, new MarkdownString(text));
    }

    MarkdownSection thatContains(MarkdownElement text) {
      return new MarkdownSection(header, text);
    }
  }

  static class MarkdownHeaderBuilder {

    private int level;

    MarkdownHeader withCaption(String caption) {
      return new MarkdownHeader(new MarkdownString(caption), level);
    }

    MarkdownHeader withCaption(MarkdownElement caption) {
      return new MarkdownHeader(caption, level);
    }

    MarkdownHeaderBuilder level(int n) {
      this.level = n;
      return this;
    }
  }

  static class MarkdownChoiceBuilder {

    private MarkdownElement firstOption;
    private BooleanSupplier condition;

    MarkdownChoiceBuilder firstOption(MarkdownElement firstOption) {
      this.firstOption = firstOption;
      return this;
    }

    MarkdownChoiceBuilder when(BooleanSupplier condition) {
      this.condition = condition;
      return this;
    }

    MarkdownChoice otherwise(MarkdownElement element) {
      return new MarkdownChoice(condition, firstOption, element);
    }
  }

}
