package com.sap.oss.phosphor.fosstars.tool.format;

import static com.sap.oss.phosphor.fosstars.model.score.oss.OssRulesOfPlayScore.findViolatedRules;

import com.sap.oss.phosphor.fosstars.model.Confidence;
import com.sap.oss.phosphor.fosstars.model.Subject;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.value.BooleanValue;
import com.sap.oss.phosphor.fosstars.model.value.RatingValue;
import com.sap.oss.phosphor.fosstars.model.value.ScoreValue;
import java.util.List;
import java.util.Objects;
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
    List<Value<Boolean>> violatedRules = findViolatedRules(scoreValue.usedValues());

    if (violatedRules.isEmpty()) {
      return StringUtils.EMPTY;
    }

    String content = violatedRules.stream()
        .map(OssRulesOfPlayRatingMarkdownFormatter::formatRule)
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
    List<Value<Boolean>> violatedRules = findViolatedRules(scoreValue.usedValues());

    String content = scoreValue.usedValues().stream()
        .filter(rule -> !violatedRules.contains(rule))
        .filter(rule -> !rule.isUnknown())
        .map(BooleanValue.class::cast)
        .map(OssRulesOfPlayRatingMarkdownFormatter::formatRule)
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
        .map(value -> String.format("1.  %s", nameOf(value.feature())))
        .collect(Collectors.joining("\n"));

    return String.format("## Unclear rules%n%n%s%n", content);
  }

  /**
   * Prints a formatted violated rule.
   *
   * @param value The rule.
   * @return A formatted violated rule.
   */
  private static String formatRule(Value<Boolean> value) {
    return String.format("1.  %s **%s**", nameOf(value.feature()), value.get() ? "Yes" : "No");
  }
}
