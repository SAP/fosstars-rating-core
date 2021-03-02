package com.sap.oss.phosphor.fosstars.tool.format;

import static com.sap.oss.phosphor.fosstars.model.score.oss.OssRulesOfPlayScore.findViolatedRules;

import com.sap.oss.phosphor.fosstars.model.Confidence;
import com.sap.oss.phosphor.fosstars.model.Subject;
import com.sap.oss.phosphor.fosstars.model.Value;
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
  private static final String RESOURCE = "OssSecurityRatingMarkdownRatingValueTemplate.md";

  /**
   * A Markdown template for a rating value.
   */
  private static final String TEMPLATE
      = loadFrom(RESOURCE, OssRulesOfPlayRatingMarkdownFormatter.class);

  @Override
  public String print(Subject subject) {
    if (!subject.ratingValue().isPresent()) {
      return StringUtils.EMPTY;
    }

    return print(subject.ratingValue().get());
  }

  /**
   * Print a rating value and advices.
   *
   * @param ratingValue The rating value.
   * @return A string to be displayed.
   */
  @Override
  public String print(RatingValue ratingValue) {
    Objects.requireNonNull(ratingValue, "Hey! Rating can't be null!");

    ScoreValue scoreValue = ratingValue.scoreValue();
    return TEMPLATE
        .replaceAll("%MAX_CONFIDENCE%", formatted(Confidence.MAX))
        .replace("%STATUS%", ratingValue.label().name())
        .replace("%CONFIDENCE_LABEL%", confidenceLabelFor(ratingValue.confidence()))
        .replace("%CONFIDENCE_VALUE%", formatted(ratingValue.confidence()))
        .replace("%VIOLATED_RULES%", violatedRules(scoreValue));
  }

  /**
   * Prints violated rules.
   *
   * @param scoreValue A score value.
   * @return A formatted list of violated rules.
   */
  private String violatedRules(ScoreValue scoreValue) {
    List<Value<Boolean>> violatedRules = findViolatedRules(scoreValue.usedValues());

    if (violatedRules.isEmpty()) {
      return "No violated rules.";
    }

    return violatedRules.stream()
        .map(OssRulesOfPlayRatingMarkdownFormatter::formatViolatedRule)
        .collect(Collectors.joining("\n"));
  }

  /**
   * Prints a formatted violated rule.
   *
   * @param value The rule.
   * @return A formatted violated rule.
   */
  private static String formatViolatedRule(Value<Boolean> value) {
    return String.format("1.  %s", nameOf(value.feature()));
  }
}
