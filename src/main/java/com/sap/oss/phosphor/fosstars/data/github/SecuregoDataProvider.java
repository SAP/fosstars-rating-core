package com.sap.oss.phosphor.fosstars.data.github;

import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.RUNS_SECUREGO_SCANS;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_SECUREGO_SCAN_CHECKS;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_SECUREGO_WITH_RULES;
import static com.sap.oss.phosphor.fosstars.model.other.Utils.setOf;

import com.sap.oss.phosphor.fosstars.data.AbstractStaticScanToolsDataProvider;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.ValueSet;
import com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures;
import com.sap.oss.phosphor.fosstars.model.subject.oss.GitHubProject;
import com.sap.oss.phosphor.fosstars.model.value.ValueHashSet;
import com.sap.oss.phosphor.fosstars.util.Yaml;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Pattern;
import org.apache.commons.collections4.IteratorUtils;

/**
 * The data provider gathers info about how a project uses Bandit for static analysis. In
 * particular, it tries to fill out the following features:
 *
 * <ul>
 *   <li>{@link OssFeatures#USES_SECUREGO_SCAN_CHECKS}
 *   <li>{@link OssFeatures#RUNS_SECUREGO_SCANS}
 *   <li>{@link OssFeatures#USES_SECUREGO_SCAN_CHECKS}
 * </ul>
 */
public class SecuregoDataProvider extends AbstractStaticScanToolsDataProvider {

  /** A step in a GitHub action that triggers analysis with Securego. */
  private static final Pattern RUN_STEP_SECUREGO_REGEX_PATTERN =
      Pattern.compile("^.*securego/gosec.*$", Pattern.DOTALL);

  /** A step config of a GitHub action that triggers Securego scans with include rule. */
  private static final Pattern RUN_STEP_SECUREGO_WITH_INCLUDE_REGEX_PATTERN =
      Pattern.compile("^.*-include=.*$", Pattern.DOTALL);

  /** A step config of a GitHub action that triggers Securego scans with include rule. */
  private static final Pattern RUN_STEP_SECUREGO_WITH_EXCLUDE_REGEX_PATTERN =
      Pattern.compile("^.*-exclude=.*$", Pattern.DOTALL);

  /**
   * Initializes a data provider.
   *
   * @param fetcher An interface to GitHub.
   */
  public SecuregoDataProvider(GitHubDataFetcher fetcher) {
    super(fetcher, setOf(USES_SECUREGO_SCAN_CHECKS, RUNS_SECUREGO_SCANS, USES_SECUREGO_WITH_RULES));
  }

  @Override
  protected ValueSet fetchValuesFor(GitHubProject project) throws IOException {
    logger.info("Figuring out how the project uses Securego ...");

    LocalRepository repository = GitHubDataFetcher.localRepositoryFor(project);

    Value<Boolean> runsSecurego = RUNS_SECUREGO_SCANS.value(false);
    Value<Boolean> usesSecuregoScanChecks = USES_SECUREGO_SCAN_CHECKS.value(false);
    Value<Boolean> usesSecuregoWithSelectedRules = USES_SECUREGO_WITH_RULES.value(false);

    // ideally, we're looking for a GitHub action that runs Securego scan on pull requests
    // but if we just find an action that runs Securego scans with or without rules is also fine
    for (Path configPath : findGitHubActionsIn(repository)) {
      try (InputStream content = Files.newInputStream(configPath)) {
        Map<String, Object> githubAction = Yaml.readMap(content);
        if (triggersScan(githubAction)) {
          runsSecurego = RUNS_SECUREGO_SCANS.value(true);
          if (runsOnPullRequests(githubAction)) {
            usesSecuregoScanChecks = USES_SECUREGO_SCAN_CHECKS.value(true);
          }
          if (runsWithSelectedRules(githubAction)) {
            usesSecuregoWithSelectedRules = USES_SECUREGO_WITH_RULES.value(true);
            break;
          }
        }
      }
    }

    return ValueHashSet.from(runsSecurego, usesSecuregoWithSelectedRules, usesSecuregoScanChecks);
  }

  /**
   * Checks if the Github action uses Securego sacn with rules.
   *
   * @param githubAction A config of the action
   * @return True if the Github Action has config to run Securego with rules.
   */
  private boolean runsWithSelectedRules(Map<?, ?> githubAction) {
    return Optional.ofNullable(githubAction.get("jobs"))
        .filter(Map.class::isInstance)
        .map(Map.class::cast)
        .map(jobs -> jobs.values())
        .filter(Iterable.class::isInstance)
        .map(Iterable.class::cast)
        .map(SecuregoDataProvider::checkAllJobs)
        .get();
  }

  /**
   * Checks if any step in a collection of jobs triggers a Securego scan with rules.
   *
   * @param jobs The collection of jobs from GitHub action.
   * @return True if a step triggers a Securego scan with rules, false otherwise.
   */
  private static boolean checkAllJobs(Iterable<?> jobs) {
    return IteratorUtils.toList(jobs.iterator()).stream()
        .filter(Map.class::isInstance)
        .map(Map.class::cast)
        .map(job -> job.get("steps"))
        .filter(Iterable.class::isInstance)
        .map(Iterable.class::cast)
        .map(SecuregoDataProvider::checkAllSteps)
        .findAny()
        .get();
  }

  /**
   * Checks if a collection of steps from a GitHub action contains a step that triggers a Securego
   * scan with rules.
   *
   * @param steps The steps to be checked.
   * @return True if the steps contain a step that triggers a Securego scan with rules, false
   *     otherwise.
   */
  private static boolean checkAllSteps(Iterable<?> steps) {
    AtomicBoolean usesWithRules = new AtomicBoolean(false);
    IteratorUtils.toList(steps.iterator()).stream()
        .filter(Map.class::isInstance)
        .map(Map.class::cast)
        .forEach(
            step -> {
              if (usesSecurego(step.get("uses"))) {
                usesWithRules.set(hasArgsWithRules(step.get("with")));
              }
            });
    return usesWithRules.get();
  }

  /**
   * Checks if a step contains config to run Securego.
   *
   * @param step The step to be checked.
   * @return True if the step contains config to run Securego scan, false otherwise.
   */
  private static boolean usesSecurego(Object step) {
    return Optional.ofNullable(step)
        .filter(String.class::isInstance)
        .map(String.class::cast)
        .filter(run -> RUN_STEP_SECUREGO_REGEX_PATTERN.matcher(run).matches())
        .isPresent();
  }

  /**
   * Checks if a step contains argument config to run Securego scans with rules.
   *
   * @param step The step to be checked.
   * @return True if the step contains arguments to run Securego scan with rules, false otherwise.
   */
  private static boolean hasArgsWithRules(Object step) {
    return Optional.ofNullable(step)
        .filter(Map.class::isInstance)
        .map(Map.class::cast)
        .map(with -> with.get("args"))
        .filter(String.class::isInstance)
        .map(String.class::cast)
        .filter(
            args ->
                RUN_STEP_SECUREGO_WITH_INCLUDE_REGEX_PATTERN.matcher(args).matches()
                    || RUN_STEP_SECUREGO_WITH_EXCLUDE_REGEX_PATTERN.matcher(args).matches())
        .isPresent();
  }

  @Override
  public boolean triggersScan(Map<?, ?> githubAction) {
    return Optional.ofNullable(githubAction.get("jobs"))
        .filter(Map.class::isInstance)
        .map(Map.class::cast)
        .map(jobs -> jobs.values())
        .filter(Iterable.class::isInstance)
        .map(Iterable.class::cast)
        .map(SecuregoDataProvider::scanJobs)
        .orElse(false);
  }

  /**
   * Checks if any step in a collection of jobs triggers a Securego scan.
   *
   * @param jobs The collection of jobs from GitHub action.
   * @return True if a step triggers a Securego scan, false otherwise.
   */
  private static boolean scanJobs(Iterable<?> jobs) {
    return IteratorUtils.toList(jobs.iterator()).stream()
        .filter(Map.class::isInstance)
        .map(Map.class::cast)
        .map(job -> job.get("steps"))
        .filter(Iterable.class::isInstance)
        .map(Iterable.class::cast)
        .filter(SecuregoDataProvider::hasSecuregoRunStep)
        .findAny()
        .isPresent();
  }

  /**
   * Checks if a collection of steps from a GitHub action contains a step that triggers a Securego
   * scan.
   *
   * @param steps The steps to be checked.
   * @return True if the steps contain a step that triggers a Securego scan, false otherwise.
   */
  private static boolean hasSecuregoRunStep(Iterable<?> steps) {
    return IteratorUtils.toList(steps.iterator()).stream()
        .filter(Map.class::isInstance)
        .map(Map.class::cast)
        .map(step -> step.get("uses"))
        .filter(String.class::isInstance)
        .map(String.class::cast)
        .filter(run -> RUN_STEP_SECUREGO_REGEX_PATTERN.matcher(run).matches())
        .findAny()
        .isPresent();
  }
}
