package com.sap.oss.phosphor.fosstars;

import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.ARTIFACT_VERSION;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.FUZZED_IN_OSS_FUZZ;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.HAS_BUG_BOUNTY_PROGRAM;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.HAS_SECURITY_POLICY;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.HAS_SECURITY_TEAM;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.IS_APACHE;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.IS_ECLIPSE;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.LANGUAGES;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.NUMBER_OF_COMMITS_LAST_THREE_MONTHS;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.NUMBER_OF_CONTRIBUTORS_LAST_THREE_MONTHS;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.NUMBER_OF_GITHUB_STARS;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.NUMBER_OF_WATCHERS_ON_GITHUB;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.OWASP_DEPENDENCY_CHECK_FAIL_CVSS_THRESHOLD;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.OWASP_DEPENDENCY_CHECK_USAGE;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.PACKAGE_MANAGERS;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.PROJECT_START_DATE;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.RELEASED_ARTIFACT_VERSIONS;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.RUNS_CODEQL_SCANS;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.SIGNS_ARTIFACTS;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.SUPPORTED_BY_COMPANY;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_ADDRESS_SANITIZER;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_CODEQL_CHECKS;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_DEPENDABOT;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_FIND_SEC_BUGS;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_GITHUB_FOR_DEVELOPMENT;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_LGTM_CHECKS;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_MEMORY_SANITIZER;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_NOHTTP;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_OWASP_ESAPI;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_OWASP_JAVA_ENCODER;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_OWASP_JAVA_HTML_SANITIZER;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_SIGNED_COMMITS;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_UNDEFINED_BEHAVIOR_SANITIZER;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.VULNERABILITIES;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.WORST_LGTM_GRADE;
import static com.sap.oss.phosphor.fosstars.model.other.Utils.setOf;
import static com.sap.oss.phosphor.fosstars.model.value.Language.JAVA;
import static com.sap.oss.phosphor.fosstars.model.value.OwaspDependencyCheckUsage.MANDATORY;
import static com.sap.oss.phosphor.fosstars.model.value.PackageManager.MAVEN;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.sap.oss.phosphor.fosstars.model.Confidence;
import com.sap.oss.phosphor.fosstars.model.Interval;
import com.sap.oss.phosphor.fosstars.model.Score;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.value.CVSS;
import com.sap.oss.phosphor.fosstars.model.value.CVSS.Version;
import com.sap.oss.phosphor.fosstars.model.value.Languages;
import com.sap.oss.phosphor.fosstars.model.value.LgtmGrade;
import com.sap.oss.phosphor.fosstars.model.value.PackageManagers;
import com.sap.oss.phosphor.fosstars.model.value.ScoreValue;
import com.sap.oss.phosphor.fosstars.model.value.VersionRange;
import com.sap.oss.phosphor.fosstars.model.value.Vulnerabilities;
import com.sap.oss.phosphor.fosstars.model.value.Vulnerability;
import com.sap.oss.phosphor.fosstars.model.value.Vulnerability.Resolution;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class TestUtils {

  private static final double DELTA = 0.01;

  /**
   * The method checks if a score calculates an expected score value for a specified set of values.
   * First, the method asks the score to calculate a score value for the passed values.
   * Next, it checks if the score value equals to the expected one.
   * Then, it checks if the score value contains all the passed values.
   *
   * @param expectedScoreValue The expected score value.
   * @param score The score.
   * @param values The values.
   */
  public static void assertScore(double expectedScoreValue, Score score, Set<Value<?>> values) {
    ScoreValue scoreValue = score.calculate(values);
    assertEquals(expectedScoreValue, scoreValue.get(), DELTA);
    assertEquals(values.size(), scoreValue.usedValues().size());
    assertTrue(values.containsAll(scoreValue.usedValues()));
  }

  /**
   * The method checks if a score calculates an expected score value for a specified set of values.
   * First, the method asks the score to calculate a score value for the passed values.
   * Next, it checks if the score value belong to the expected range.
   * Then, it checks if the score value contains all the passed values.
   *
   * @param expectedInterval The expected range for the score value.
   * @param score The score.
   * @param values The values.
   */
  public static void assertScore(Interval expectedInterval, Score score, Set<Value<?>> values) {
    ScoreValue scoreValue = score.calculate(values);
    assertTrue(expectedInterval.contains(scoreValue.get()));
    assertTrue(Confidence.INTERVAL.contains(scoreValue.confidence()));
    assertEquals(values.size(), scoreValue.usedValues().size());
    assertTrue(values.containsAll(scoreValue.usedValues()));
  }


  /**
   * Create the default values and overwrite defaults with given values.
   *
   * @param values to be added to the default values set.
   * @return A set of values
   */
  public static Set<Value<?>> getDefaultValues(Value<?>... values) {
    Set<Value<?>> defaultValues = getDefaultValues();
    Map<String, Value<?>> name2Value = new HashMap<>();
    for (Value<?> v : values) {
      name2Value.put(v.feature().name(), v);
    }

    Set<Value<?>> resultValues = defaultValues.stream()
        .filter(v -> !name2Value.containsKey(v.feature().name()))
        .collect(Collectors.toSet());
    resultValues.addAll(name2Value.values());

    return resultValues;
  }

  /**
   * Create the default values.
   *
   * @return A set of values
   */
  public static Set<Value<?>> getDefaultValues() {
    return setOf(
        RELEASED_ARTIFACT_VERSIONS.unknown(),
        ARTIFACT_VERSION.unknown(),
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
        SIGNS_ARTIFACTS.value(true),
        VULNERABILITIES.value(new Vulnerabilities()),
        PROJECT_START_DATE.value(new Date()),
        USES_SIGNED_COMMITS.value(false),
        RUNS_CODEQL_SCANS.value(false),
        USES_CODEQL_CHECKS.value(false),
        USES_LGTM_CHECKS.value(true),
        WORST_LGTM_GRADE.value(LgtmGrade.B),
        USES_NOHTTP.value(true),
        USES_DEPENDABOT.value(true),
        USES_GITHUB_FOR_DEVELOPMENT.value(true),
        LANGUAGES.value(Languages.of(JAVA)),
        USES_ADDRESS_SANITIZER.value(false),
        USES_MEMORY_SANITIZER.value(false),
        USES_UNDEFINED_BEHAVIOR_SANITIZER.value(false),
        USES_OWASP_ESAPI.value(false),
        USES_OWASP_JAVA_ENCODER.value(false),
        USES_OWASP_JAVA_HTML_SANITIZER.value(false),
        FUZZED_IN_OSS_FUZZ.value(false),
        USES_FIND_SEC_BUGS.value(true),
        OWASP_DEPENDENCY_CHECK_USAGE.value(MANDATORY),
        OWASP_DEPENDENCY_CHECK_FAIL_CVSS_THRESHOLD.value(7.0),
        PACKAGE_MANAGERS.value(PackageManagers.from(MAVEN)));
  }

  /**
   * Create a vulnerability with given parameters.
   * All other parameters have some default test values (as they are not required for all tests)
   *
   * @param cvssValue the cvss value
   * @param startVersion the start version of the vulnerability
   * @param endVersion the end version of the vulnerability
   * @return the created vulnerability
   */
  public static Vulnerability createBasicVulnerability(
      double cvssValue, String startVersion, String endVersion) {

    String id = UUID.randomUUID().toString();
    CVSS cvss = new CVSS(Version.V3, cvssValue);
    Date introduced = createDate("21-03-21");
    Date fixed = createDate("21-03-22");
    Date published = createDate("21-03-23");
    List<VersionRange> versions = new ArrayList<>();
    versions.add(new VersionRange(startVersion, endVersion));

    return Vulnerability.Builder.newVulnerability(id)
        .set(cvss).published(published).introduced(introduced).fixed(fixed)
        .set(Resolution.PATCHED).setVersionRanges(versions)
        .make();
  }

  /**
   * Parse date in format 'yyyy-MM-dd'.
   * @param date date in format 'yyyy-MM-dd'.
   * @return according date
   */
  private static Date createDate(String date) {
    try {
      return new SimpleDateFormat("yyyy-MM-dd").parse(date);
    } catch (ParseException e) {
      throw new RuntimeException(e);
    }
  }
}
