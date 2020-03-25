package com.sap.sgs.phosphor.fosstars.tool.github;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import com.sap.sgs.phosphor.fosstars.data.IsApache;
import com.sap.sgs.phosphor.fosstars.data.IsEclipse;
import com.sap.sgs.phosphor.fosstars.data.NoUserCallback;
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

    final String organization = "apache";
    final String project = "nifi";

    SingleSecurityRatingCalculator calculator = new SingleSecurityRatingCalculator(github);
    calculator.token("test");
    calculator.set(NoUserCallback.INSTANCE);
    calculator = spy(calculator);

    when(calculator.dataProviders(anyString(), anyString()))
        .thenReturn(Arrays.asList(new IsApache(organization), new IsEclipse(organization)));

    GitHubProject apacheNiFi = new GitHubProject(new GitHubOrganization(organization), project);
    assertFalse(apacheNiFi.ratingValue().isPresent());

    calculator.calculateFor(apacheNiFi);

    assertTrue(apacheNiFi.ratingValue().isPresent());
    RatingValue ratingValue = apacheNiFi.ratingValue().get();
    assertEquals(SecurityLabel.BAD, ratingValue.label());
    assertTrue(DoubleInterval.closed(0, 3).contains(ratingValue.scoreValue().get()));
  }
}