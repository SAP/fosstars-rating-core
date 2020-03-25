package com.sap.sgs.phosphor.fosstars.tool.github;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import com.sap.sgs.phosphor.fosstars.data.NoUserCallback;
import com.sap.sgs.phosphor.fosstars.model.math.DoubleInterval;
import com.sap.sgs.phosphor.fosstars.model.rating.oss.OssSecurityRating.SecurityLabel;
import com.sap.sgs.phosphor.fosstars.model.value.RatingValue;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.Test;
import org.kohsuke.github.GitHub;

public class MultipleSecurityRatingsCalculatorTest {

  @Test
  public void calculateFor() throws IOException {
    GitHub github = mock(GitHub.class);

    SingleSecurityRatingCalculator singleRatingCalculator
        = new SingleSecurityRatingCalculator(github);
    singleRatingCalculator = spy(singleRatingCalculator);
    when(singleRatingCalculator.dataProviders(anyString(), anyString()))
        .thenReturn(Collections.emptyList());

    MultipleSecurityRatingsCalculator multipleRatingsCalculator
        = new MultipleSecurityRatingsCalculator(github);
    multipleRatingsCalculator.token("test");
    multipleRatingsCalculator.set(NoUserCallback.INSTANCE);
    multipleRatingsCalculator = spy(multipleRatingsCalculator);
    when(multipleRatingsCalculator.singleSecurityRatingCalculator())
        .thenReturn(singleRatingCalculator);

    final String apache = "apache";
    final String eclipse = "eclipse";

    GitHubProject apacheNiFi = new GitHubProject(new GitHubOrganization(apache), "nifi");
    GitHubProject eclipseSteady = new GitHubProject(new GitHubOrganization(eclipse), "steady");

    assertFalse(apacheNiFi.ratingValue().isPresent());
    assertFalse(eclipseSteady.ratingValue().isPresent());

    List<GitHubProject> projects = Arrays.asList(apacheNiFi, eclipseSteady);
    multipleRatingsCalculator.calculateFor(projects);

    assertTrue(apacheNiFi.ratingValue().isPresent());
    check(apacheNiFi.ratingValue().get());

    assertTrue(eclipseSteady.ratingValue().isPresent());
    check(eclipseSteady.ratingValue().get());
  }

  private static void check(RatingValue ratingValue) {
    assertEquals(SecurityLabel.BAD, ratingValue.label());
    assertTrue(DoubleInterval.closed(0, 3).contains(ratingValue.scoreValue().get()));
  }
}