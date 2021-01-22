package com.sap.oss.phosphor.fosstars.tool.github;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import com.sap.oss.phosphor.fosstars.data.NoUserCallback;
import com.sap.oss.phosphor.fosstars.data.github.TestGitHubDataFetcherHolder;
import com.sap.oss.phosphor.fosstars.model.math.DoubleInterval;
import com.sap.oss.phosphor.fosstars.model.rating.oss.OssSecurityRating.SecurityLabel;
import com.sap.oss.phosphor.fosstars.model.subject.oss.GitHubProject;
import com.sap.oss.phosphor.fosstars.model.value.RatingValue;
import com.sap.oss.phosphor.fosstars.nvd.NVD;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.Test;
import org.kohsuke.github.GHRepository;

public class MultipleSecurityRatingsCalculatorTest extends TestGitHubDataFetcherHolder {

  @Test
  public void testCalculateFor() throws IOException {
    GHRepository repository = mock(GHRepository.class);
    when(fetcher.github().getRepository(any())).thenReturn(repository);

    NVD nvd = new NVD();

    SingleSecurityRatingCalculator singleRatingCalculator
        = new SingleSecurityRatingCalculator(fetcher, nvd);
    singleRatingCalculator.set(NoUserCallback.INSTANCE);
    singleRatingCalculator = spy(singleRatingCalculator);
    when(singleRatingCalculator.dataProviders()).thenReturn(Collections.emptyList());

    MultipleSecurityRatingsCalculator multipleRatingsCalculator
        = new MultipleSecurityRatingsCalculator(singleRatingCalculator);
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

    assertTrue(multipleRatingsCalculator.failedProjects().isEmpty());
  }

  private static void check(RatingValue ratingValue) {
    assertEquals(SecurityLabel.UNCLEAR, ratingValue.label());
    assertTrue(DoubleInterval.closed(0, 3).contains(ratingValue.scoreValue().confidence()));
    assertTrue(DoubleInterval.closed(0, 3).contains(ratingValue.scoreValue().get()));
  }
}