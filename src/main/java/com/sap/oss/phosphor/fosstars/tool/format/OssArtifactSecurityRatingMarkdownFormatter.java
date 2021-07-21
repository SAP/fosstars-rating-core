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
 * for {@link com.sap.oss.phosphor.fosstars.model.rating.oss.OssArtifactSecurityRating} in Markdown.
 */
public class OssArtifactSecurityRatingMarkdownFormatter extends AbstractMarkdownFormatter {

  /**
   * A resource with a default Markdown template.
   */
  private static final String RATING_VALUE_TEMPLATE_RESOURCE
      = "OssArtifactSecurityRatingMarkdownRatingValueTemplate.md";

  /**
   * A default Markdown template for a rating value.
   */
  private static final String DEFAULT_RATING_VALUE_TEMPLATE
      = loadFrom(RATING_VALUE_TEMPLATE_RESOURCE, OssArtifactSecurityRatingMarkdownFormatter.class);

  /**
   * A Markdown template for reports.
   */
  private final String template;

  /**
   * Create a new formatter with the default report template.
   *
   * @param advisor An advisor for calculated ratings.
   */
  public OssArtifactSecurityRatingMarkdownFormatter(Advisor advisor) {
    this(advisor, DEFAULT_RATING_VALUE_TEMPLATE);
  }

  @Override
  public String print(Subject subject) {
    if (!subject.ratingValue().isPresent()) {
      return StringUtils.EMPTY;
    }

    return print(subject.ratingValue().get(), markdownAdviceFor(subject));
  }

  /**
   * Create a new formatter.
   *
   * @param advisor An advisor for calculated ratings.
   * @param template A Markdown template for reports.
   */
  public OssArtifactSecurityRatingMarkdownFormatter(Advisor advisor, String template) {
    super(advisor);
    this.template = Objects.requireNonNull(template, "Oh no! Template can't be null!");
  }

  protected String print(RatingValue ratingValue, MarkdownElement advice) {
    Objects.requireNonNull(ratingValue, "Hey! Rating can't be null!");

    ScoreValue scoreValue = ratingValue.scoreValue();
    return template
        .replaceAll("%MAX_SCORE%", formatted(Score.MAX))
        .replaceAll("%MAX_CONFIDENCE%", formatted(Confidence.MAX))
        .replace("%SCORE_VALUE%", actualValueOf(scoreValue).make())
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
