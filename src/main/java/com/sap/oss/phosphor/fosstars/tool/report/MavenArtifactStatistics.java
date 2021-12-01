package com.sap.oss.phosphor.fosstars.tool.report;

import com.sap.oss.phosphor.fosstars.model.rating.oss.OssArtifactSecurityRating.ArtifactSecurityLabel;
import com.sap.oss.phosphor.fosstars.model.subject.oss.MavenArtifact;
import com.sap.oss.phosphor.fosstars.model.value.RatingValue;

/**
 * Statistics for the artifacts processed from a config file.
 */
public class MavenArtifactStatistics extends AbstractStatistics<MavenArtifact> {

  /**
   * Adds a mavenArtifact to the statistics.
   *
   * @param mavenArtifact The {@link MavenArtifact}.
   */
  @Override
  public void add(MavenArtifact mavenArtifact) {
    total++;

    if (!mavenArtifact.ratingValue().isPresent()) {
      unknownRatings++;
      return;
    }

    RatingValue ratingValue = mavenArtifact.ratingValue().get();

    if (ratingValue.label() instanceof ArtifactSecurityLabel == false) {
      unknownRatings++;
      return;
    }

    ArtifactSecurityLabel label = (ArtifactSecurityLabel) mavenArtifact.ratingValue().get().label();
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
