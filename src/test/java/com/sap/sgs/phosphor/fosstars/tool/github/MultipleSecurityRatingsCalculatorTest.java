package com.sap.sgs.phosphor.fosstars.tool.github;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import com.sap.sgs.phosphor.fosstars.data.NoUserCallback;
import com.sap.sgs.phosphor.fosstars.data.github.GitHubDataFetcher;
import com.sap.sgs.phosphor.fosstars.model.math.DoubleInterval;
import com.sap.sgs.phosphor.fosstars.model.rating.oss.OssSecurityRating.SecurityLabel;
import com.sap.sgs.phosphor.fosstars.model.value.RatingValue;
import com.sap.sgs.phosphor.fosstars.nvd.NVD;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;

public class MultipleSecurityRatingsCalculatorTest {

  @Before
  @After
  public void cleanup() {
    GitHubDataFetcher.instance().repositoryCache().clear();
    GitHubDataFetcher.instance().commitsCache().clear();
  }

  @Test
  public void testCalculateFor() throws IOException {
    GitHub github = mock(GitHub.class);

    GHRepository repository = mock(GHRepository.class);
    when(github.getRepository(any())).thenReturn(repository);

    NVD nvd = new NVD();

    SingleSecurityRatingCalculator singleRatingCalculator
        = new SingleSecurityRatingCalculator(github, nvd);
    singleRatingCalculator = spy(singleRatingCalculator);
    when(singleRatingCalculator.dataProviders()).thenReturn(Collections.emptyList());

    MultipleSecurityRatingsCalculator multipleRatingsCalculator
        = new MultipleSecurityRatingsCalculator(github, nvd);
    multipleRatingsCalculator.token("test");
    multipleRatingsCalculator.set(NoUserCallback.INSTANCE);
    multipleRatingsCalculator = spy(multipleRatingsCalculator);
    when(multipleRatingsCalculator.singleSecurityRatingCalculator())
        .thenReturn(singleRatingCalculator);

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

    assertTrue(multipleRatingsCalculator.failedProjects().isEmpty());
  }

  private static void check(RatingValue ratingValue) {
    assertEquals(SecurityLabel.BAD, ratingValue.label());
    assertTrue(DoubleInterval.closed(0, 3).contains(ratingValue.scoreValue().get()));
  }
}