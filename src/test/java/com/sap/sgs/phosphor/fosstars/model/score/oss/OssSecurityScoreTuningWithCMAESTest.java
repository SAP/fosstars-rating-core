package com.sap.sgs.phosphor.fosstars.model.score.oss;

import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.FUZZED_IN_OSS_FUZZ;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.HAS_BUG_BOUNTY_PROGRAM;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.HAS_SECURITY_POLICY;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.HAS_SECURITY_TEAM;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.IS_APACHE;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.IS_ECLIPSE;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.LANGUAGES;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.NUMBER_OF_COMMITS_LAST_THREE_MONTHS;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.NUMBER_OF_CONTRIBUTORS_LAST_THREE_MONTHS;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.NUMBER_OF_GITHUB_STARS;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.NUMBER_OF_WATCHERS_ON_GITHUB;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.PACKAGE_MANAGERS;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.PROJECT_START_DATE;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.SCANS_FOR_VULNERABLE_DEPENDENCIES;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.SIGNS_ARTIFACTS;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.SUPPORTED_BY_COMPANY;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.USES_ADDRESS_SANITIZER;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.USES_DEPENDABOT;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.USES_FIND_SEC_BUGS;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.USES_GITHUB_FOR_DEVELOPMENT;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.USES_LGTM_CHECKS;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.USES_MEMORY_SANITIZER;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.USES_NOHTTP;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.USES_SIGNED_COMMITS;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.USES_UNDEFINED_BEHAVIOR_SANITIZER;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.VULNERABILITIES;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.WORST_LGTM_GRADE;
import static com.sap.sgs.phosphor.fosstars.model.qa.TestVectorBuilder.newTestVector;
import static com.sap.sgs.phosphor.fosstars.model.value.Language.JAVA;
import static com.sap.sgs.phosphor.fosstars.model.value.Language.OTHER;
import static com.sap.sgs.phosphor.fosstars.model.value.PackageManager.MAVEN;
import static org.junit.Assert.assertNotNull;

import com.sap.sgs.phosphor.fosstars.model.Score;
import com.sap.sgs.phosphor.fosstars.model.math.DoubleInterval;
import com.sap.sgs.phosphor.fosstars.model.qa.TestVectors;
import com.sap.sgs.phosphor.fosstars.model.value.Languages;
import com.sap.sgs.phosphor.fosstars.model.value.LgtmGrade;
import com.sap.sgs.phosphor.fosstars.model.value.PackageManagers;
import com.sap.sgs.phosphor.fosstars.model.value.UnknownValue;
import com.sap.sgs.phosphor.fosstars.model.value.Vulnerabilities;
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
          .set(UnknownValue.of(HAS_BUG_BOUNTY_PROGRAM))
          .set(UnknownValue.of(SIGNS_ARTIFACTS))
          .set(UnknownValue.of(SCANS_FOR_VULNERABLE_DEPENDENCIES))
          .set(UnknownValue.of(VULNERABILITIES))
          .set(UnknownValue.of(PROJECT_START_DATE))
          .set(UnknownValue.of(USES_SIGNED_COMMITS))
          .set(UnknownValue.of(USES_LGTM_CHECKS))
          .set(UnknownValue.of(WORST_LGTM_GRADE))
          .set(UnknownValue.of(USES_NOHTTP))
          .set(UnknownValue.of(USES_DEPENDABOT))
          .set(UnknownValue.of(USES_GITHUB_FOR_DEVELOPMENT))
          .set(UnknownValue.of(LANGUAGES))
          .set(UnknownValue.of(PACKAGE_MANAGERS))
          .set(UnknownValue.of(USES_ADDRESS_SANITIZER))
          .set(UnknownValue.of(USES_MEMORY_SANITIZER))
          .set(UnknownValue.of(USES_UNDEFINED_BEHAVIOR_SANITIZER))
          .set(UnknownValue.of(FUZZED_IN_OSS_FUZZ))
          .set(UnknownValue.of(USES_FIND_SEC_BUGS))
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
          .set(HAS_BUG_BOUNTY_PROGRAM.value(false))
          .set(SIGNS_ARTIFACTS.value(false))
          .set(SCANS_FOR_VULNERABLE_DEPENDENCIES.value(false))
          .set(VULNERABILITIES.value(NO_VULNERABILITIES))
          .set(PROJECT_START_DATE.value(FIVE_YEARS_AGO))
          .set(USES_SIGNED_COMMITS.value(false))
          .set(USES_LGTM_CHECKS.value(false))
          .set(WORST_LGTM_GRADE.unknown())
          .set(USES_NOHTTP.value(false))
          .set(USES_DEPENDABOT.value(false))
          .set(USES_GITHUB_FOR_DEVELOPMENT.value(false))
          .set(LANGUAGES.value(Languages.of(OTHER)))
          .set(PACKAGE_MANAGERS.unknown())
          .set(USES_ADDRESS_SANITIZER.value(false))
          .set(USES_MEMORY_SANITIZER.value(false))
          .set(USES_UNDEFINED_BEHAVIOR_SANITIZER.value(false))
          .set(FUZZED_IN_OSS_FUZZ.value(false))
          .set(USES_FIND_SEC_BUGS.value(false))
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
          .set(HAS_BUG_BOUNTY_PROGRAM.value(true))
          .set(SIGNS_ARTIFACTS.value(true))
          .set(SCANS_FOR_VULNERABLE_DEPENDENCIES.value(true))
          .set(VULNERABILITIES.value(NO_VULNERABILITIES))
          .set(PROJECT_START_DATE.value(FIVE_YEARS_AGO))
          .set(USES_SIGNED_COMMITS.value(true))
          .set(USES_LGTM_CHECKS.value(true))
          .set(WORST_LGTM_GRADE.value(LgtmGrade.A_PLUS))
          .set(USES_NOHTTP.value(true))
          .set(USES_DEPENDABOT.value(true))
          .set(USES_GITHUB_FOR_DEVELOPMENT.value(true))
          .set(LANGUAGES.value(Languages.of(JAVA)))
          .set(PACKAGE_MANAGERS.value(new PackageManagers(MAVEN)))
          .set(USES_ADDRESS_SANITIZER.value(true))
          .set(USES_MEMORY_SANITIZER.value(true))
          .set(USES_UNDEFINED_BEHAVIOR_SANITIZER.value(true))
          .set(FUZZED_IN_OSS_FUZZ.value(true))
          .set(USES_FIND_SEC_BUGS.value(true))
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

    new OssSecurityScoreTuningWithCMAES(score, SIMPLE_TEST_VECTORS).run();
    verification.run();
  }
}