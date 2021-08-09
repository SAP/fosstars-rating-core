package com.sap.oss.phosphor.fosstars.tool;

import static com.sap.oss.phosphor.fosstars.TestUtils.DELTA;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.sap.oss.phosphor.fosstars.data.NoUserCallback;
import com.sap.oss.phosphor.fosstars.data.github.TestGitHubDataFetcherHolder;
import com.sap.oss.phosphor.fosstars.model.Confidence;
import com.sap.oss.phosphor.fosstars.model.RatingRepository;
import com.sap.oss.phosphor.fosstars.model.rating.oss.OssSecurityRating;
import com.sap.oss.phosphor.fosstars.model.rating.oss.OssSecurityRating.SecurityLabel;
import com.sap.oss.phosphor.fosstars.model.subject.oss.GitHubProject;
import com.sap.oss.phosphor.fosstars.model.value.RatingValue;
import java.util.Collections;
import org.junit.Test;

public class SingleRatingCalculatorTest extends TestGitHubDataFetcherHolder {

  @Test
  public void testCalculateFor() {
    GitHubProject project = new GitHubProject("test", "project");

    SingleRatingCalculator calculator
        = new SingleRatingCalculator(
            RatingRepository.INSTANCE.rating(OssSecurityRating.class),
            Collections.emptyList());
    calculator.set(NoUserCallback.INSTANCE);

    assertFalse(project.ratingValue().isPresent());

    calculator.calculateFor(project);

    assertTrue(project.ratingValue().isPresent());
    RatingValue ratingValue = project.ratingValue().get();
    assertEquals(SecurityLabel.UNCLEAR, ratingValue.label());
    assertEquals(
        Confidence.MIN,
        Double.compare(Confidence.MIN, ratingValue.scoreValue().confidence()),
        DELTA);
    assertTrue(ratingValue.scoreValue().isUnknown());
  }
}