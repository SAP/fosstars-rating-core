package com.sap.oss.phosphor.fosstars.tool.format;

import com.sap.oss.phosphor.fosstars.advice.Advisor;
import com.sap.oss.phosphor.fosstars.model.Confidence;
import com.sap.oss.phosphor.fosstars.model.Score;
import com.sap.oss.phosphor.fosstars.model.value.RatingValue;
import com.sap.oss.phosphor.fosstars.model.value.ScoreValue;
import java.util.Objects;

/**
 * The class prints a rating value
 * for {@link com.sap.oss.phosphor.fosstars.model.rating.oss.OssArtifactSecurityRating} in Markdown.
 */
public class OssArtifactSecurityRatingMarkdownFormatter extends AbstractMarkdownFormatter {

  /**
   * A resource with a Markdown template.
   */
  private static final String RATING_VALUE_TEMPLATE_RESOURCE
      = "OssArtifactSecurityRatingMarkdownRatingValueTemplate.md";

  /**
   * A Markdown template for a rating value.
   */
  private static final String RATING_VALUE_TEMPLATE
      = loadFrom(RATING_VALUE_TEMPLATE_RESOURCE, OssArtifactSecurityRatingMarkdownFormatter.class);

  /**
   * Create a new formatter.
   *
   * @param advisor An advisor for calculated ratings.
   */
  public OssArtifactSecurityRatingMarkdownFormatter(Advisor advisor) {
    super(advisor);
  }

  protected String print(RatingValue ratingValue, String advice) {
    Objects.requireNonNull(ratingValue, "Hey! Rating can't be null!");

    ScoreValue scoreValue = ratingValue.scoreValue();
    return RATING_VALUE_TEMPLATE
        .replaceAll("%MAX_SCORE%", formatted(Score.MAX))
        .replaceAll("%MAX_CONFIDENCE%", formatted(Confidence.MAX))
        .replace("%SCORE_VALUE%", actualValueOf(scoreValue))
        .replace("%RATING_LABEL%", ratingValue.label().name())
        .replace("%CONFIDENCE_LABEL%", confidenceLabelFor(ratingValue.confidence()))
        .replace("%CONFIDENCE_VALUE%", formatted(ratingValue.confidence()))
        .replace("%MAIN_SCORE_NAME%", scoreValue.score().name().toLowerCase())
        .replace("%MAIN_SCORE_VALUE_DETAILS%", highLevelDescriptionOf(scoreValue))
        .replace("%MAIN_SCORE_DESCRIPTION%", scoreValue.score().description())
        .replace("%MAIN_SCORE_EXPLANATION%", explanationOf(scoreValue))
        .replace("%SUB_SCORE_DETAILS%", descriptionOfSubScoresIn(scoreValue))
        .replace("%ADVICE%", advice)
        .replace("%INFO_ABOUT_VULNERABILITIES%", infoAboutVulnerabilitiesIn(scoreValue));
  }
}
