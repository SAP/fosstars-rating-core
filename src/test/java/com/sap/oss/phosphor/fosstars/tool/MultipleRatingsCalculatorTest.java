package com.sap.oss.phosphor.fosstars.tool;

import static com.sap.oss.phosphor.fosstars.TestUtils.DELTA;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.spy;

import com.sap.oss.phosphor.fosstars.data.NoUserCallback;
import com.sap.oss.phosphor.fosstars.data.github.TestGitHubDataFetcherHolder;
import com.sap.oss.phosphor.fosstars.model.Confidence;
import com.sap.oss.phosphor.fosstars.model.Rating;
import com.sap.oss.phosphor.fosstars.model.RatingRepository;
import com.sap.oss.phosphor.fosstars.model.rating.oss.OssSecurityRating;
import com.sap.oss.phosphor.fosstars.model.rating.oss.OssSecurityRating.SecurityLabel;
import com.sap.oss.phosphor.fosstars.model.subject.oss.GitHubProject;
import com.sap.oss.phosphor.fosstars.model.value.RatingValue;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.Test;

public class MultipleRatingsCalculatorTest extends TestGitHubDataFetcherHolder {

  @Test
  public void testCalculateFor() {
    Rating rating = RatingRepository.INSTANCE.rating(OssSecurityRating.class);

    SingleRatingCalculator singleRatingCalculator
        = new SingleRatingCalculator(rating, Collections.emptyList());
    singleRatingCalculator.set(NoUserCallback.INSTANCE);

    MultipleRatingsCalculator multipleRatingsCalculator
        = new MultipleRatingsCalculator(singleRatingCalculator);
    multipleRatingsCalculator = spy(multipleRatingsCalculator);

    GitHubProject apacheNiFi = new GitHubProject("apache", "nifi");
    GitHubProject eclipseSteady = new GitHubProject("eclipse", "steady");

    assertFalse(apacheNiFi.ratingValue().isPresent());
    assertFalse(eclipseSteady.ratingValue().isPresent());

    List<GitHubProject> projects = Arrays.asList(apacheNiFi, eclipseSteady);
    multipleRatingsCalculator.calculateFor(projects);

    assertTrue(apacheNiFi.ratingValue().isPresent());
    check(apacheNiFi.ratingValue().get());

    assertTrue(eclipseSteady.ratingValue().isPresent());
    check(eclipseSteady.ratingValue().get());

    assertTrue(multipleRatingsCalculator.failedSubjects().isEmpty());
  }

  private static void check(RatingValue ratingValue) {
    assertEquals(SecurityLabel.UNCLEAR, ratingValue.label());
    assertEquals(
        Confidence.MIN,
        Double.compare(Confidence.MIN, ratingValue.scoreValue().confidence()),
        DELTA);
    assertTrue(ratingValue.scoreValue().isUnknown());
  }
}