package com.sap.sgs.phosphor.fosstars.tool.github;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import com.sap.sgs.phosphor.fosstars.data.NoUserCallback;
import com.sap.sgs.phosphor.fosstars.data.github.IsApache;
import com.sap.sgs.phosphor.fosstars.data.github.IsEclipse;
import com.sap.sgs.phosphor.fosstars.model.math.DoubleInterval;
import com.sap.sgs.phosphor.fosstars.model.rating.oss.OssSecurityRating.SecurityLabel;
import com.sap.sgs.phosphor.fosstars.model.value.RatingValue;
import java.io.IOException;
import java.util.Arrays;
import org.junit.Test;
import org.kohsuke.github.GitHub;

public class SingleSecurityRatingCalculatorTest {

  @Test
  public void calculateFor() throws IOException {
    GitHub github = mock(GitHub.class);

    SingleSecurityRatingCalculator calculator = new SingleSecurityRatingCalculator(github);
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
}