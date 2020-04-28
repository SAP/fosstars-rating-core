package com.sap.sgs.phosphor.fosstars.model.score.oss;

import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.HAS_SECURITY_POLICY;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.HAS_SECURITY_TEAM;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.IS_APACHE;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.IS_ECLIPSE;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.NUMBER_OF_COMMITS_LAST_THREE_MONTHS;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.NUMBER_OF_CONTRIBUTORS_LAST_THREE_MONTHS;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.NUMBER_OF_GITHUB_STARS;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.NUMBER_OF_WATCHERS_ON_GITHUB;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.PACKAGE_MANAGERS;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.PROJECT_START_DATE;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.SCANS_FOR_VULNERABLE_DEPENDENCIES;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.SUPPORTED_BY_COMPANY;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.USES_LGTM;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.USES_NOHTTP;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.USES_VERIFIED_SIGNED_COMMITS;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.VULNERABILITIES;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.WORST_LGTM_GRADE;
import static com.sap.sgs.phosphor.fosstars.model.qa.TestVectorBuilder.newTestVector;
import static com.sap.sgs.phosphor.fosstars.model.value.PackageManager.MAVEN;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import com.sap.sgs.phosphor.fosstars.model.Score;
import com.sap.sgs.phosphor.fosstars.model.math.DoubleInterval;
import com.sap.sgs.phosphor.fosstars.model.qa.TestVectors;
import com.sap.sgs.phosphor.fosstars.model.value.LgtmGrade;
import com.sap.sgs.phosphor.fosstars.model.value.PackageManagers;
import com.sap.sgs.phosphor.fosstars.model.value.UnknownValue;
import com.sap.sgs.phosphor.fosstars.model.value.Vulnerabilities;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;
import org.junit.Test;

public class OssSecurityScoreTuningWithCMAESTest {

  private static final Vulnerabilities NO_VULNERABILITIES = new Vulnerabilities();
  private static final Date FIVE_YEARS_AGO
      = new Date(System.currentTimeMillis() - 5 * 365 * 24 * 60 * 60 * 1000L);

  private static final TestVectors SIMPLE_TEST_VECTORS = new TestVectors(

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
          .set(UnknownValue.of(VULNERABILITIES))
          .set(UnknownValue.of(PROJECT_START_DATE))
          .set(UnknownValue.of(USES_VERIFIED_SIGNED_COMMITS))
          .set(UnknownValue.of(USES_LGTM))
          .set(UnknownValue.of(WORST_LGTM_GRADE))
          .set(UnknownValue.of(USES_NOHTTP))
          .set(UnknownValue.of(PACKAGE_MANAGERS))
          .expectedScore(DoubleInterval.closed(Score.MIN, 0.1))
          .alias("one")
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
          .set(VULNERABILITIES.value(NO_VULNERABILITIES))
          .set(PROJECT_START_DATE.value(FIVE_YEARS_AGO))
          .set(USES_VERIFIED_SIGNED_COMMITS.value(false))
          .set(USES_LGTM.value(false))
          .set(WORST_LGTM_GRADE.unknown())
          .set(USES_NOHTTP.value(false))
          .set(PACKAGE_MANAGERS.unknown())
          .expectedScore(DoubleInterval.closed(1.0, 4.0))
          .alias("two")
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
          .set(VULNERABILITIES.value(NO_VULNERABILITIES))
          .set(PROJECT_START_DATE.value(FIVE_YEARS_AGO))
          .set(USES_VERIFIED_SIGNED_COMMITS.value(true))
          .set(USES_LGTM.value(true))
          .set(WORST_LGTM_GRADE.value(LgtmGrade.A_PLUS))
          .set(USES_NOHTTP.value(true))
          .set(PACKAGE_MANAGERS.value(new PackageManagers(MAVEN)))
          .expectedScore(DoubleInterval.init().from(9.0).to(Score.MAX).make())
          .alias("three")
          .make()
  );

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

}