package com.sap.sgs.phosphor.fosstars.model.score.oss;

import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.FIRST_COMMIT_DATE;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.HAS_SECURITY_POLICY;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.HAS_SECURITY_TEAM;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.IS_APACHE;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.IS_ECLIPSE;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.NUMBER_OF_COMMITS_LAST_THREE_MONTHS;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.NUMBER_OF_CONTRIBUTORS_LAST_THREE_MONTHS;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.NUMBER_OF_GITHUB_STARS;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.NUMBER_OF_WATCHERS_ON_GITHUB;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.PROJECT_START_DATE;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.SCANS_FOR_VULNERABLE_DEPENDENCIES;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.SECURITY_REVIEWS_DONE;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.SUPPORTED_BY_COMPANY;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.VULNERABILITIES;
import static com.sap.sgs.phosphor.fosstars.model.qa.RatingVerification.loadTestVectorsFromCsvResource;
import static com.sap.sgs.phosphor.fosstars.model.qa.TestVectorBuilder.newTestVector;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import com.sap.sgs.phosphor.fosstars.model.Score;
import com.sap.sgs.phosphor.fosstars.model.math.DoubleInterval;
import com.sap.sgs.phosphor.fosstars.model.qa.TestVector;
import com.sap.sgs.phosphor.fosstars.model.qa.VerificationFailedException;
import com.sap.sgs.phosphor.fosstars.model.value.SecurityReview;
import com.sap.sgs.phosphor.fosstars.model.value.SecurityReviews;
import com.sap.sgs.phosphor.fosstars.model.value.UnknownValue;
import com.sap.sgs.phosphor.fosstars.model.value.Vulnerabilities;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import org.junit.Test;

public class OssSecurityScoreTuningWithCMAESTest {

  private static final SecurityReviews NO_SECURITY_REVIEWS = new SecurityReviews();
  private static final SecurityReviews ONE_SECURITY_REVIEW;

  static {
    try {
      ONE_SECURITY_REVIEW = new SecurityReviews(
          new SecurityReview(new URL("https://site/proof"), new Date(), "Wolfgang Amadeus Mozart"));
    } catch (MalformedURLException e) {
      throw new IllegalArgumentException("Looks like something is wrong with the URL", e);
    }
  }

  private static final Vulnerabilities NO_VULNERABILITIES = new Vulnerabilities();
  private static final Date FIVE_YEARS_AGO
      = new Date(System.currentTimeMillis() - 5 * 365 * 24 * 60 * 60 * 1000L);

  private static final List<TestVector> SIMPLE_TEST_VECTORS
      = Collections.unmodifiableList(Arrays.asList(

      // all values are unknown
      newTestVector()
          .set(UnknownValue.of(NUMBER_OF_COMMITS_LAST_THREE_MONTHS))
          .set(UnknownValue.of(NUMBER_OF_CONTRIBUTORS_LAST_THREE_MONTHS))
          .set(UnknownValue.of(NUMBER_OF_GITHUB_STARS))
          .set(UnknownValue.of(NUMBER_OF_WATCHERS_ON_GITHUB))
          .set(UnknownValue.of(SUPPORTED_BY_COMPANY))
          .set(UnknownValue.of(IS_APACHE))
          .set(UnknownValue.of(IS_ECLIPSE))
          .set(UnknownValue.of(HAS_SECURITY_POLICY))
          .set(UnknownValue.of(HAS_SECURITY_TEAM))
          .set(UnknownValue.of(SCANS_FOR_VULNERABLE_DEPENDENCIES))
          .set(UnknownValue.of(SECURITY_REVIEWS_DONE))
          .set(UnknownValue.of(VULNERABILITIES))
          .set(UnknownValue.of(PROJECT_START_DATE))
          .set(UnknownValue.of(FIRST_COMMIT_DATE))
          .expectedScore(DoubleInterval.closed(Score.MIN, 0.1))
          .make(),

      // very bad project
      newTestVector()
          .set(NUMBER_OF_COMMITS_LAST_THREE_MONTHS.value(5))
          .set(NUMBER_OF_CONTRIBUTORS_LAST_THREE_MONTHS.value(1))
          .set(NUMBER_OF_GITHUB_STARS.value(2))
          .set(NUMBER_OF_WATCHERS_ON_GITHUB.value(0))
          .set(SUPPORTED_BY_COMPANY.value(false))
          .set(IS_APACHE.value(false))
          .set(IS_ECLIPSE.value(false))
          .set(HAS_SECURITY_POLICY.value(false))
          .set(HAS_SECURITY_TEAM.value(false))
          .set(SCANS_FOR_VULNERABLE_DEPENDENCIES.value(false))
          .set(SECURITY_REVIEWS_DONE.value(NO_SECURITY_REVIEWS))
          .set(VULNERABILITIES.value(NO_VULNERABILITIES))
          .set(PROJECT_START_DATE.value(FIVE_YEARS_AGO))
          .set(FIRST_COMMIT_DATE.value(FIVE_YEARS_AGO))
          .expectedScore(DoubleInterval.closed(Score.MIN, 1.0))
          .make(),

      // very good project
      newTestVector()
          .set(NUMBER_OF_COMMITS_LAST_THREE_MONTHS.value(1000))
          .set(NUMBER_OF_CONTRIBUTORS_LAST_THREE_MONTHS.value(50))
          .set(NUMBER_OF_GITHUB_STARS.value(15000))
          .set(NUMBER_OF_WATCHERS_ON_GITHUB.value(5000))
          .set(SUPPORTED_BY_COMPANY.value(true))
          .set(IS_APACHE.value(true))
          .set(IS_ECLIPSE.value(false))
          .set(HAS_SECURITY_POLICY.value(true))
          .set(HAS_SECURITY_TEAM.value(true))
          .set(SCANS_FOR_VULNERABLE_DEPENDENCIES.value(true))
          .set(SECURITY_REVIEWS_DONE.value(ONE_SECURITY_REVIEW))
          .set(VULNERABILITIES.value(NO_VULNERABILITIES))
          .set(PROJECT_START_DATE.value(FIVE_YEARS_AGO))
          .set(FIRST_COMMIT_DATE.value(FIVE_YEARS_AGO))
          .expectedScore(DoubleInterval.init().from(9.0).to(Score.MAX).make())
          .make()
  ));

  @Test
  public void simpleTestVectors() throws Exception {
    OssSecurityScore score = new OssSecurityScore();

    OssSecurityScore.Verification verification
        = new OssSecurityScore.Verification(score, SIMPLE_TEST_VECTORS);
    assertNotNull(verification);

    Path path = Files.createTempFile("fosstars", "oss_security_score");
    try {
      new OssSecurityScoreTuningWithCMAES(score, SIMPLE_TEST_VECTORS, path.toString()).run();
      byte[] content = Files.readAllBytes(path);

      // smoke test
      assertNotNull(content);
      assertNotEquals(0, content.length);
    } finally {
      Files.delete(path);
    }

    verification.run();
  }

  @Test
  public void loadTestVectorsFromCSV() throws IOException, VerificationFailedException {
    OssSecurityScore score = new OssSecurityScore();

    String filename = "com/sap/sgs/phosphor/fosstars/model/score/oss/SimpleTestVectors.csv";
    try (InputStream is = getClass().getClassLoader().getResourceAsStream(filename)) {
      List<TestVector> vectors = loadTestVectorsFromCsvResource(score.allFeatures(), is);
      OssSecurityScore.Verification verification
          = new OssSecurityScore.Verification(score, vectors);
      assertNotNull(verification);

      Path path = Files.createTempFile("fosstars", "oss_security_score");
      try {
        new OssSecurityScoreTuningWithCMAES(score, verification.vectors(), path.toString()).run();
        byte[] content = Files.readAllBytes(path);

        // smoke test
        assertNotNull(content);
        assertNotEquals(0, content.length);
      } finally {
        Files.delete(path);
      }

      verification.run();
    }
  }

}