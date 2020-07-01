package com.sap.sgs.phosphor.fosstars.tool.format;

import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.FIRST_COMMIT_DATE;
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
import static com.sap.sgs.phosphor.fosstars.model.other.Utils.setOf;
import static com.sap.sgs.phosphor.fosstars.model.value.Language.C;
import static com.sap.sgs.phosphor.fosstars.model.value.Language.JAVA;
import static com.sap.sgs.phosphor.fosstars.model.value.PackageManager.GRADLE;
import static com.sap.sgs.phosphor.fosstars.model.value.PackageManager.MAVEN;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.sap.sgs.phosphor.fosstars.model.Feature;
import com.sap.sgs.phosphor.fosstars.model.RatingRepository;
import com.sap.sgs.phosphor.fosstars.model.Value;
import com.sap.sgs.phosphor.fosstars.model.rating.oss.OssSecurityRating;
import com.sap.sgs.phosphor.fosstars.model.value.Languages;
import com.sap.sgs.phosphor.fosstars.model.value.LgtmGrade;
import com.sap.sgs.phosphor.fosstars.model.value.PackageManagers;
import com.sap.sgs.phosphor.fosstars.model.value.RatingValue;
import com.sap.sgs.phosphor.fosstars.model.value.Vulnerabilities;
import java.util.Date;
import java.util.Set;
import org.junit.Test;

public class PrettyPrinterTest {

  @Test
  public void testPrint() {
    OssSecurityRating rating = RatingRepository.INSTANCE.rating(OssSecurityRating.class);
    Set<Value> values = setOf(
        SUPPORTED_BY_COMPANY.value(false),
        IS_APACHE.value(true),
        IS_ECLIPSE.value(false),
        NUMBER_OF_COMMITS_LAST_THREE_MONTHS.value(50),
        NUMBER_OF_CONTRIBUTORS_LAST_THREE_MONTHS.value(3),
        NUMBER_OF_GITHUB_STARS.value(10),
        NUMBER_OF_WATCHERS_ON_GITHUB.value(5),
        HAS_SECURITY_TEAM.value(false),
        HAS_SECURITY_POLICY.value(false),
        HAS_BUG_BOUNTY_PROGRAM.value(false),
        SIGNS_ARTIFACTS.value(false),
        SCANS_FOR_VULNERABLE_DEPENDENCIES.value(false),
        VULNERABILITIES.value(new Vulnerabilities()),
        PROJECT_START_DATE.value(new Date()),
        FIRST_COMMIT_DATE.value(new Date()),
        USES_SIGNED_COMMITS.value(false),
        USES_LGTM_CHECKS.value(true),
        WORST_LGTM_GRADE.value(LgtmGrade.A),
        USES_GITHUB_FOR_DEVELOPMENT.value(false),
        USES_NOHTTP.value(false),
        USES_DEPENDABOT.value(false),
        USES_ADDRESS_SANITIZER.value(false),
        USES_MEMORY_SANITIZER.value(false),
        USES_UNDEFINED_BEHAVIOR_SANITIZER.value(false),
        FUZZED_IN_OSS_FUZZ.value(false),
        LANGUAGES.value(Languages.of(C)),
        USES_FIND_SEC_BUGS.value(false),
        PACKAGE_MANAGERS.value(new PackageManagers(MAVEN)));
    
    RatingValue ratingValue = rating.calculate(values);

    PrettyPrinter printer = new PrettyPrinter();
    String text = printer.print(ratingValue);

    assertNotNull(text);
    assertFalse(text.isEmpty());
    System.out.println(text);
    for (Value value : ratingValue.scoreValue().usedValues()) {
      assertTrue(text.contains(PrettyPrinter.nameOf(value.feature())));
    }
    for (Feature feature : rating.allFeatures()) {
      assertTrue(String.format("'%s' feature should be there!", feature.name()),
          text.contains(PrettyPrinter.nameOf(feature)));
    }
    assertTrue(text.contains("Value"));
    assertTrue(text.contains("Confidence"));
    assertTrue(text.contains("Importance"));
    assertTrue(text.contains("Based on"));
    assertTrue(text.contains("Description"));
    assertTrue(text.contains("Explanation"));
  }

  @Test
  public void testConsistency() {
    OssSecurityRating rating = RatingRepository.INSTANCE.rating(OssSecurityRating.class);
    Set<Value> values = setOf(
        SUPPORTED_BY_COMPANY.value(false),
        IS_APACHE.value(true),
        IS_ECLIPSE.value(false),
        NUMBER_OF_COMMITS_LAST_THREE_MONTHS.value(50),
        NUMBER_OF_CONTRIBUTORS_LAST_THREE_MONTHS.value(3),
        NUMBER_OF_GITHUB_STARS.value(10),
        NUMBER_OF_WATCHERS_ON_GITHUB.value(5),
        HAS_SECURITY_TEAM.value(false),
        HAS_SECURITY_POLICY.value(false),
        HAS_BUG_BOUNTY_PROGRAM.value(true),
        SIGNS_ARTIFACTS.value(true),
        SCANS_FOR_VULNERABLE_DEPENDENCIES.value(false),
        VULNERABILITIES.value(new Vulnerabilities()),
        PROJECT_START_DATE.value(new Date()),
        FIRST_COMMIT_DATE.value(new Date()),
        USES_SIGNED_COMMITS.value(false),
        USES_LGTM_CHECKS.value(true),
        WORST_LGTM_GRADE.value(LgtmGrade.A),
        USES_GITHUB_FOR_DEVELOPMENT.value(false),
        USES_NOHTTP.value(true),
        USES_DEPENDABOT.value(true),
        USES_ADDRESS_SANITIZER.value(false),
        USES_MEMORY_SANITIZER.value(false),
        USES_UNDEFINED_BEHAVIOR_SANITIZER.value(false),
        FUZZED_IN_OSS_FUZZ.value(false),
        LANGUAGES.value(Languages.of(JAVA)),
        USES_FIND_SEC_BUGS.value(true),
        PACKAGE_MANAGERS.value(new PackageManagers(GRADLE)));
    RatingValue ratingValue = rating.calculate(values);

    PrettyPrinter printer = new PrettyPrinter();
    String text = printer.print(ratingValue);
    for (int i = 0; i < 100; i++) {
      assertEquals(text, printer.print(ratingValue));
    }
  }
}