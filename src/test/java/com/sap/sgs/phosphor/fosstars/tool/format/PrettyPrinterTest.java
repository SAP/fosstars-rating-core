package com.sap.sgs.phosphor.fosstars.tool.format;

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
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.SUPPORTED_BY_COMPANY;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.USES_LGTM;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.USES_VERIFIED_SIGNED_COMMITS;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.VULNERABILITIES;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.WORST_LGTM_GRADE;
import static com.sap.sgs.phosphor.fosstars.model.other.Utils.setOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.sap.sgs.phosphor.fosstars.model.Feature;
import com.sap.sgs.phosphor.fosstars.model.RatingRepository;
import com.sap.sgs.phosphor.fosstars.model.Value;
import com.sap.sgs.phosphor.fosstars.model.rating.oss.OssSecurityRating;
import com.sap.sgs.phosphor.fosstars.model.value.LgtmGrade;
import com.sap.sgs.phosphor.fosstars.model.value.RatingValue;
import com.sap.sgs.phosphor.fosstars.model.value.Vulnerabilities;
import java.util.Date;
import java.util.Set;
import org.junit.Test;

public class PrettyPrinterTest {

  @Test
  public void print() {
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
        SCANS_FOR_VULNERABLE_DEPENDENCIES.value(false),
        VULNERABILITIES.value(new Vulnerabilities()),
        PROJECT_START_DATE.value(new Date()),
        FIRST_COMMIT_DATE.value(new Date()),
        USES_VERIFIED_SIGNED_COMMITS.value(false),
        USES_LGTM.value(true),
        WORST_LGTM_GRADE.value(LgtmGrade.A));
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
      assertTrue(text.contains(PrettyPrinter.nameOf(feature)));
    }
    assertTrue(text.contains("Value"));
    assertTrue(text.contains("Confidence"));
    assertTrue(text.contains("Importance"));
    assertTrue(text.contains("Based on"));
    assertTrue(text.contains("Description"));
    assertTrue(text.contains("Explanation"));
  }

  @Test
  public void consistency() {
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
        SCANS_FOR_VULNERABLE_DEPENDENCIES.value(false),
        VULNERABILITIES.value(new Vulnerabilities()),
        PROJECT_START_DATE.value(new Date()),
        FIRST_COMMIT_DATE.value(new Date()),
        USES_VERIFIED_SIGNED_COMMITS.value(false),
        USES_LGTM.value(true),
        WORST_LGTM_GRADE.value(LgtmGrade.A));
    RatingValue ratingValue = rating.calculate(values);

    PrettyPrinter printer = new PrettyPrinter();
    String text = printer.print(ratingValue);
    for (int i = 0; i < 100; i++) {
      assertEquals(text, printer.print(ratingValue));
    }
  }
}