package com.sap.oss.phosphor.fosstars.tool.report;

import com.sap.oss.phosphor.fosstars.model.rating.oss.OssSecurityRating.SecurityLabel;
import com.sap.oss.phosphor.fosstars.model.subject.oss.GitHubProject;
import com.sap.oss.phosphor.fosstars.model.value.RatingValue;

/**
 * Statistics for the projects processed from a config file.
 */
public class ProjectStatistics extends AbstractStatistics<GitHubProject> {

  /**
   * Adds a project to the statistics.
   *
   * @param project The {@link GitHubProject}.
   */
  @Override
  public void add(GitHubProject project) {
    total++;

    if (!project.ratingValue().isPresent()) {
      unknownRatings++;
      return;
    }

    RatingValue ratingValue = project.ratingValue().get();

    if (ratingValue.label() instanceof SecurityLabel == false) {
      unknownRatings++;
      return;
    }

    SecurityLabel label = (SecurityLabel) project.ratingValue().get().label();
    switch (label) {
      case BAD:
        badRatings++;
        break;
      case MODERATE:
        moderateRatings++;
        break;
      case GOOD:
        goodRatings++;
        break;
      case UNCLEAR:
        unknownRatings++;
        break;
      default:
        throw new IllegalArgumentException(
            String.format("Hey! I don't know this label: %s", label));
    }
  }
}
