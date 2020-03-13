package com.sap.sgs.phosphor.fosstars.model.score.oss;

import static com.sap.sgs.phosphor.fosstars.TestUtils.assertScore;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.SCANS_FOR_VULNERABLE_DEPENDENCIES;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.SECURITY_REVIEWS_DONE;
import static com.sap.sgs.phosphor.fosstars.model.other.Utils.setOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.sap.sgs.phosphor.fosstars.model.Score;
import com.sap.sgs.phosphor.fosstars.model.value.ScoreValue;
import com.sap.sgs.phosphor.fosstars.model.value.SecurityReview;
import com.sap.sgs.phosphor.fosstars.model.value.SecurityReviews;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import org.junit.Test;

public class ProjectSecurityTestingScoreTest {

  private static ProjectSecurityTestingScore PROJECT_SECURITY_TESTING
      = new ProjectSecurityTestingScore();

  @Test(expected = IllegalArgumentException.class)
  public void noInfoAboutDependencyScans() {
    PROJECT_SECURITY_TESTING.calculate(SECURITY_REVIEWS_DONE.unknown());
  }

  @Test(expected = IllegalArgumentException.class)
  public void noInfoAboutSecurityReviews() {
    PROJECT_SECURITY_TESTING.calculate(SCANS_FOR_VULNERABLE_DEPENDENCIES.unknown());
  }

  @Test
  public void calculate() throws MalformedURLException {
    SecurityReview review = new SecurityReview(
        new URL("http://site/proof"), new Date(), "Johann Wolfgang von Goethe");
    SecurityReviews oneReview = new SecurityReviews(review);

    assertScore(Score.MIN,
        PROJECT_SECURITY_TESTING, setOf(
            SECURITY_REVIEWS_DONE.unknown(),
            SCANS_FOR_VULNERABLE_DEPENDENCIES.unknown()));

    assertScore(Score.MIN,
        PROJECT_SECURITY_TESTING, setOf(
            SECURITY_REVIEWS_DONE.value(SecurityReviews.NO_REVIEWS),
            SCANS_FOR_VULNERABLE_DEPENDENCIES.value(false)));

    assertScore(7.0,
        PROJECT_SECURITY_TESTING, setOf(
            SECURITY_REVIEWS_DONE.value(oneReview),
            SCANS_FOR_VULNERABLE_DEPENDENCIES.value(false)));

    assertScore(3.0,
        PROJECT_SECURITY_TESTING, setOf(
            SECURITY_REVIEWS_DONE.value(SecurityReviews.NO_REVIEWS),
            SCANS_FOR_VULNERABLE_DEPENDENCIES.value(true)));

    assertScore(Score.MAX,
        PROJECT_SECURITY_TESTING, setOf(
            SECURITY_REVIEWS_DONE.value(oneReview),
            SCANS_FOR_VULNERABLE_DEPENDENCIES.value(true)));
  }

  @Test
  public void explanation() throws MalformedURLException {
    SecurityReview review = new SecurityReview(
        new URL("http://site/proof"), new Date(), "Johann Wolfgang von Goethe");
    SecurityReviews oneReview = new SecurityReviews(review);

    ScoreValue value = PROJECT_SECURITY_TESTING.calculate(
        SECURITY_REVIEWS_DONE.value(oneReview),
        SCANS_FOR_VULNERABLE_DEPENDENCIES.value(true));

    assertTrue(value.score().description().isEmpty());
    assertEquals(2, value.explanation().size());
  }

}