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
import com.sap.sgs.phosphor.fosstars.data.github.IsApache;
import com.sap.sgs.phosphor.fosstars.data.github.IsEclipse;
import com.sap.sgs.phosphor.fosstars.model.math.DoubleInterval;
import com.sap.sgs.phosphor.fosstars.model.rating.oss.OssSecurityRating.SecurityLabel;
import com.sap.sgs.phosphor.fosstars.model.value.RatingValue;
import com.sap.sgs.phosphor.fosstars.nvd.NVD;
import java.io.IOException;
import java.util.Arrays;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;

public class SingleSecurityRatingCalculatorTest {

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

    SingleSecurityRatingCalculator calculator
        = new SingleSecurityRatingCalculator(github, new NVD());

    calculator.token("test");
    calculator.set(NoUserCallback.INSTANCE);
    calculator = spy(calculator);

    when(calculator.dataProviders())
        .thenReturn(Arrays.asList(new IsApache(github), new IsEclipse(github)));

    GitHubProject apacheNiFi = new GitHubProject("apache", "nifi");
    assertFalse(apacheNiFi.ratingValue().isPresent());

    calculator.calculateFor(apacheNiFi);

    assertTrue(apacheNiFi.ratingValue().isPresent());
    RatingValue ratingValue = apacheNiFi.ratingValue().get();
    assertEquals(SecurityLabel.BAD, ratingValue.label());
    assertTrue(DoubleInterval.closed(0, 3).contains(ratingValue.scoreValue().get()));
  }

  @Test
  public void testCalculateForWithNoContent() throws IOException {
    GitHub github = mock(GitHub.class);
    GHRepository repository = mock(GHRepository.class);
    when(github.getRepository(any())).thenReturn(repository);
    when(repository.getDirectoryContent(any())).thenThrow(new IOException());
    checkNoRating(github);
  }

  @Test
  public void testCalculateForWithNullRepository() throws IOException {
    GitHub github = mock(GitHub.class);
    when(github.getRepository(any())).thenReturn(null);
    checkNoRating(github);
  }

  @Test
  public void testCalculateForWithExceptionWhenFetchingRepository() throws IOException {
    GitHub github = mock(GitHub.class);
    when(github.getRepository(any())).thenThrow(new IOException());
    checkNoRating(github);
  }

  private static void checkNoRating(GitHub github) throws IOException {
    SingleSecurityRatingCalculator calculator
        = new SingleSecurityRatingCalculator(github, new NVD());

    calculator.token("test");
    calculator.set(NoUserCallback.INSTANCE);
    calculator = spy(calculator);

    when(calculator.dataProviders())
        .thenReturn(Arrays.asList(new IsApache(github), new IsEclipse(github)));

    GitHubProject apacheNiFi = new GitHubProject("apache", "nifi");
    assertFalse(apacheNiFi.ratingValue().isPresent());

    calculator.calculateFor(apacheNiFi);
    assertFalse(apacheNiFi.ratingValue().isPresent());
  }
}