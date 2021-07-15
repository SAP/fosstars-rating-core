package com.sap.oss.phosphor.fosstars.tool.format;

import com.sap.oss.phosphor.fosstars.advice.Advisor;
import com.sap.oss.phosphor.fosstars.model.Confidence;
import com.sap.oss.phosphor.fosstars.model.Score;
import com.sap.oss.phosphor.fosstars.model.Subject;
import com.sap.oss.phosphor.fosstars.model.value.RatingValue;
import com.sap.oss.phosphor.fosstars.model.value.ScoreValue;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;

/**
 * The class prints a rating value
 * for {@link com.sap.oss.phosphor.fosstars.model.rating.oss.OssSecurityRating} in Markdown.
 */
public class OssSecurityRatingMarkdownFormatter extends AbstractMarkdownFormatter {

  /**
   * A resource with a default Markdown template.
   */
  private static final String RATING_VALUE_TEMPLATE_RESOURCE
      = "OssSecurityRatingMarkdownRatingValueTemplate.md";

  /**
   * A default Markdown template for a rating value.
   */
  private static final String DEFAULT_RATING_VALUE_TEMPLATE
      = loadFrom(RATING_VALUE_TEMPLATE_RESOURCE, OssSecurityRatingMarkdownFormatter.class);

  /**
   * A Markdown template for reports.
   */
  private final String template;

  /**
   * Create a new formatter with the default report template.
   *
   * @param advisor An advisor for calculated ratings.
   */
  public OssSecurityRatingMarkdownFormatter(Advisor advisor) {
    this(advisor, DEFAULT_RATING_VALUE_TEMPLATE);
  }

  /**
   * Create a new formatter.
   *
   * @param advisor An advisor for calculated ratings.
   * @param template A Markdown template for reports.
   */
  public OssSecurityRatingMarkdownFormatter(Advisor advisor, String template) {
    super(advisor);
    this.template = Objects.requireNonNull(template, "Oh no! Template can't be null!");
  }

  @Override
  public String print(Subject subject) {
    if (!subject.ratingValue().isPresent()) {
      return StringUtils.EMPTY;
    }

    MarkdownElement advice = markdownAdviceFor(subject);
    RatingValue ratingValue = subject.ratingValue().get();
    ScoreValue scoreValue = ratingValue.scoreValue();

    return template
        .replaceAll("%MAX_SCORE%", formatted(Score.MAX))
        .replaceAll("%MAX_CONFIDENCE%", formatted(Confidence.MAX))
        .replace("%SCORE_VALUE%", formatted(scoreValue.get()))
        .replace("%RATING_LABEL%", ratingValue.label().name())
        .replace("%CONFIDENCE_LABEL%", confidenceLabelFor(ratingValue.confidence()))
        .replace("%CONFIDENCE_VALUE%", formatted(ratingValue.confidence()))
        .replace("%MAIN_SCORE_NAME%", scoreValue.score().name().toLowerCase())
        .replace("%MAIN_SCORE_VALUE_DETAILS%", highLevelDescriptionOf(scoreValue).make())
        .replace("%MAIN_SCORE_DESCRIPTION%", scoreValue.score().description())
        .replace("%MAIN_SCORE_EXPLANATION%", explanationOf(scoreValue).make())
        .replace("%SUB_SCORE_DETAILS%", descriptionOfSubScoresIn(scoreValue).make())
        .replace("%ADVICE%", advice.make())
        .replace("%INFO_ABOUT_VULNERABILITIES%", infoAboutVulnerabilitiesIn(scoreValue).make());
  }
}
