package com.sap.oss.phosphor.fosstars;

import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.ARTIFACT_VERSION;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.FUZZED_IN_OSS_FUZZ;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.HAS_BUG_BOUNTY_PROGRAM;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.HAS_EXECUTABLE_BINARIES;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.HAS_SECURITY_POLICY;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.HAS_SECURITY_TEAM;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.IS_APACHE;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.IS_ECLIPSE;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.LANGUAGES;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.NUMBER_OF_COMMITS_LAST_THREE_MONTHS;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.NUMBER_OF_CONTRIBUTORS_LAST_THREE_MONTHS;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.NUMBER_OF_DEPENDENT_PROJECTS_ON_GITHUB;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.NUMBER_OF_GITHUB_STARS;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.NUMBER_OF_WATCHERS_ON_GITHUB;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.OWASP_DEPENDENCY_CHECK_FAIL_CVSS_THRESHOLD;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.OWASP_DEPENDENCY_CHECK_USAGE;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.PACKAGE_MANAGERS;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.PROJECT_START_DATE;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.RELEASED_ARTIFACT_VERSIONS;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.RUNS_BANDIT_SCANS;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.RUNS_CODEQL_SCANS;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.RUNS_GOSEC_SCANS;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.RUNS_MYPY_SCANS;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.RUNS_PYLINT_SCANS;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.SECURITY_REVIEWS;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.SIGNS_ARTIFACTS;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.SUPPORTED_BY_COMPANY;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_ADDRESS_SANITIZER;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_BANDIT_SCAN_CHECKS;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_CODEQL_CHECKS;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_DEPENDABOT;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_FIND_SEC_BUGS;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_GITHUB_FOR_DEVELOPMENT;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_GOSEC_SCAN_CHECKS;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_GOSEC_WITH_RULES;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_LGTM_CHECKS;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_MEMORY_SANITIZER;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_MYPY_SCAN_CHECKS;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_NOHTTP;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_OWASP_ESAPI;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_OWASP_JAVA_ENCODER;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_OWASP_JAVA_HTML_SANITIZER;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_PYLINT_SCAN_CHECKS;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_SIGNED_COMMITS;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_SNYK;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_UNDEFINED_BEHAVIOR_SANITIZER;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.VULNERABILITIES_IN_ARTIFACT;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.VULNERABILITIES_IN_PROJECT;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.WORST_LGTM_GRADE;
import static com.sap.oss.phosphor.fosstars.model.other.Utils.setOf;
import static com.sap.oss.phosphor.fosstars.model.value.Language.JAVA;
import static com.sap.oss.phosphor.fosstars.model.value.OwaspDependencyCheckUsage.MANDATORY;
import static com.sap.oss.phosphor.fosstars.model.value.PackageManager.MAVEN;
import static com.sap.oss.phosphor.fosstars.model.value.SecurityReviews.noReviews;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.sap.oss.phosphor.fosstars.model.Confidence;
import com.sap.oss.phosphor.fosstars.model.Interval;
import com.sap.oss.phosphor.fosstars.model.Rating;
import com.sap.oss.phosphor.fosstars.model.Score;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.subject.oss.GitHubProject;
import com.sap.oss.phosphor.fosstars.model.value.ArtifactVersion;
import com.sap.oss.phosphor.fosstars.model.value.ArtifactVersions;
import com.sap.oss.phosphor.fosstars.model.value.CVSS;
import com.sap.oss.phosphor.fosstars.model.value.CVSS.V3.Impact;
import com.sap.oss.phosphor.fosstars.model.value.Languages;
import com.sap.oss.phosphor.fosstars.model.value.LgtmGrade;
import com.sap.oss.phosphor.fosstars.model.value.PackageManagers;
import com.sap.oss.phosphor.fosstars.model.value.ScoreValue;
import com.sap.oss.phosphor.fosstars.model.value.SecurityReview;
import com.sap.oss.phosphor.fosstars.model.value.SecurityReviews;
import com.sap.oss.phosphor.fosstars.model.value.VersionRange;
import com.sap.oss.phosphor.fosstars.model.value.Vulnerabilities;
import com.sap.oss.phosphor.fosstars.model.value.Vulnerability;
import com.sap.oss.phosphor.fosstars.model.value.Vulnerability.Resolution;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import org.eclipse.jgit.api.CommitCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

public class TestUtils {

  /**
   * The precision when comparing numbers.
   */
  public static final double DELTA = 0.01;

  /**
   * A test GitHub project.
   */
  public static final GitHubProject PROJECT = new GitHubProject("org", "test");

  public static final Consumer<Value<Boolean>> NO_EXPLANATION
      = value -> assertTrue(value.explanation().isEmpty());

  public static final Consumer<Value<Boolean>> HAS_EXPLANATION
      = value -> assertFalse(value.explanation().isEmpty());

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
        NUMBER_OF_DEPENDENT_PROJECTS_ON_GITHUB.value(10),
        HAS_SECURITY_TEAM.value(false),
        HAS_SECURITY_POLICY.value(false),
        HAS_BUG_BOUNTY_PROGRAM.value(false),
        SIGNS_ARTIFACTS.value(true),
        VULNERABILITIES_IN_PROJECT.value(new Vulnerabilities()),
        VULNERABILITIES_IN_ARTIFACT.value(new Vulnerabilities()),
        PROJECT_START_DATE.value(new Date()),
        USES_SIGNED_COMMITS.value(false),
        RUNS_CODEQL_SCANS.value(false),
        USES_CODEQL_CHECKS.value(false),
        RUNS_BANDIT_SCANS.value(false),
        USES_BANDIT_SCAN_CHECKS.value(false),
        RUNS_GOSEC_SCANS.value(false),
        USES_GOSEC_WITH_RULES.value(false),
        USES_GOSEC_SCAN_CHECKS.value(false),
        RUNS_PYLINT_SCANS.value(false),
        USES_PYLINT_SCAN_CHECKS.value(false),
        RUNS_MYPY_SCANS.value(false),
        USES_MYPY_SCAN_CHECKS.value(false),
        USES_LGTM_CHECKS.value(true),
        WORST_LGTM_GRADE.value(LgtmGrade.B),
        USES_NOHTTP.value(true),
        USES_DEPENDABOT.value(true),
        USES_SNYK.value(false),
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
        PACKAGE_MANAGERS.value(PackageManagers.from(MAVEN)),
        SECURITY_REVIEWS.value(noReviews()),
        HAS_EXECUTABLE_BINARIES.value(false));
  }

  /**
   * Create the default values.
   *
   * @return A set of values
   */
  public static Set<Value<?>> getBestValues() {
    return setOf(
        RELEASED_ARTIFACT_VERSIONS.value(
            ArtifactVersions.of(
                new ArtifactVersion("1.0.0", LocalDateTime.now().minusDays(30)),
                new ArtifactVersion("1.5.0", LocalDateTime.now().minusDays(15)),
                new ArtifactVersion("2.0.0", LocalDateTime.now())
            )),
        ARTIFACT_VERSION.value(new ArtifactVersion("2.0.0", LocalDateTime.now())),
        SUPPORTED_BY_COMPANY.value(true),
        IS_APACHE.value(true),
        IS_ECLIPSE.value(false),
        NUMBER_OF_COMMITS_LAST_THREE_MONTHS.value(500),
        NUMBER_OF_CONTRIBUTORS_LAST_THREE_MONTHS.value(30),
        NUMBER_OF_GITHUB_STARS.value(100000),
        NUMBER_OF_WATCHERS_ON_GITHUB.value(50),
        NUMBER_OF_DEPENDENT_PROJECTS_ON_GITHUB.value(50000),
        HAS_SECURITY_TEAM.value(true),
        HAS_SECURITY_POLICY.value(true),
        HAS_BUG_BOUNTY_PROGRAM.value(true),
        SIGNS_ARTIFACTS.value(true),
        VULNERABILITIES_IN_PROJECT.value(new Vulnerabilities(
            Vulnerability.Builder.newVulnerability("ID-01")
                .set(new CVSS.V3(3.0, Impact.HIGH, Impact.LOW, Impact.NONE))
                .set(Resolution.PATCHED)
                .fixed(new Date())
                .introduced(new Date())
                .published(new Date())
                .versionRanges(Collections.singletonList(
                    new VersionRange("1.0.0", "1.1.0")))
                .make()
        )),
        VULNERABILITIES_IN_ARTIFACT.value(new Vulnerabilities(
            Vulnerability.Builder.newVulnerability("ID-02")
                .set(new CVSS.V3(3.0, Impact.HIGH, Impact.LOW, Impact.NONE))
                .set(Resolution.PATCHED)
                .fixed(new Date())
                .introduced(new Date())
                .published(new Date())
                .versionRanges(Collections.singletonList(
                    new VersionRange("1.1.0", "1.2.0")))
                .make()
        )),
        PROJECT_START_DATE.value(new Date()),
        USES_SIGNED_COMMITS.value(true),
        RUNS_CODEQL_SCANS.value(true),
        USES_CODEQL_CHECKS.value(true),
        RUNS_BANDIT_SCANS.value(true),
        USES_BANDIT_SCAN_CHECKS.value(true),
        RUNS_GOSEC_SCANS.value(true),
        USES_GOSEC_WITH_RULES.value(true),
        USES_GOSEC_SCAN_CHECKS.value(true),
        RUNS_PYLINT_SCANS.value(true),
        USES_PYLINT_SCAN_CHECKS.value(true),
        RUNS_MYPY_SCANS.value(true),
        USES_MYPY_SCAN_CHECKS.value(true),
        USES_LGTM_CHECKS.value(true),
        WORST_LGTM_GRADE.value(LgtmGrade.A_PLUS),
        USES_NOHTTP.value(true),
        USES_DEPENDABOT.value(true),
        USES_SNYK.value(true),
        USES_GITHUB_FOR_DEVELOPMENT.value(true),
        LANGUAGES.value(Languages.of(JAVA)),
        USES_ADDRESS_SANITIZER.value(true),
        USES_MEMORY_SANITIZER.value(true),
        USES_UNDEFINED_BEHAVIOR_SANITIZER.value(true),
        USES_OWASP_ESAPI.value(true),
        USES_OWASP_JAVA_ENCODER.value(true),
        USES_OWASP_JAVA_HTML_SANITIZER.value(true),
        FUZZED_IN_OSS_FUZZ.value(true),
        USES_FIND_SEC_BUGS.value(true),
        OWASP_DEPENDENCY_CHECK_USAGE.value(MANDATORY),
        OWASP_DEPENDENCY_CHECK_FAIL_CVSS_THRESHOLD.value(4.0),
        PACKAGE_MANAGERS.value(PackageManagers.from(MAVEN)),
        SECURITY_REVIEWS.value(new SecurityReviews(new SecurityReview(new Date(), 0.0))),
        HAS_EXECUTABLE_BINARIES.value(true));
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
    CVSS cvss = new CVSS.V3(cvssValue, Impact.HIGH, Impact.LOW, Impact.NONE);
    Date introduced = createDate("21-03-21");
    Date fixed = createDate("21-03-22");
    Date published = createDate("21-03-23");
    List<VersionRange> versions = new ArrayList<>();
    versions.add(new VersionRange(startVersion, endVersion));

    return Vulnerability.Builder.newVulnerability(id)
        .set(cvss).published(published).introduced(introduced).fixed(fixed)
        .set(Resolution.PATCHED).versionRanges(versions)
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

  /**
   * Commits a set of files to a Git repository.
   *
   * @param content The stuff to be committed. It is a map from file name to its content.
   * @param message A commit message.
   * @param git An interface to the Git repository.
   * @throws IOException If something went wrong.
   * @throws GitAPIException If git failed.
   */
  public static void commit(Map<String, String> content, String message, Git git)
      throws IOException, GitAPIException {

    for (Map.Entry<String, String> entry : content.entrySet()) {
      String filename = entry.getKey();
      String text = entry.getValue();

      Files.write(
          Paths.get(git.getRepository().getDirectory().getParent()).resolve(filename),
          text.getBytes());

      git.add().addFilepattern(filename).call();
    }

    CommitCommand commit = git.commit();
    commit.setCredentialsProvider(
        new UsernamePasswordCredentialsProvider("mr.pink", "don't tell anyone"));
    commit.setMessage(message)
        .setSign(false)
        .setAuthor("Mr. Pink", "mr.pink@test.com")
        .call();
  }

  /**
   * Looks for a score in a rating.
   *
   * @param scoreClazz The score's class.
   * @param rating The rating.
   * @param <T> A type of the score.
   * @return The score if found.
   */
  public static <T extends Score> Optional<T> find(Class<T> scoreClazz, Rating rating) {
    ScoreCollector collector = new ScoreCollector();
    rating.accept(collector);
    for (Score score : collector.scores()) {
      if (score.getClass().equals(scoreClazz)) {
        return Optional.of(scoreClazz.cast(score));
      }
    }
    return Optional.empty();
  }
}
