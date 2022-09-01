package com.sap.oss.phosphor.fosstars.data.github;

import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.RUNS_GOSEC_SCANS;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_GOSEC_SCAN_CHECKS;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_GOSEC_WITH_RULES;
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
 * The data provider gathers info about how a project uses GoSec for static analysis. In
 * particular, it tries to fill out the following features:
 *
 * <ul>
 *   <li>{@link OssFeatures#USES_GOSEC_SCAN_CHECKS}
 *   <li>{@link OssFeatures#RUNS_GOSEC_SCANS}
 *   <li>{@link OssFeatures#USES_GOSEC_SCAN_CHECKS}
 * </ul>
 */
public class GoSecDataProvider extends AbstractStaticScanToolsDataProvider {

  /** 
    * A step in a GitHub action that triggers analysis with GoSec.
    */
  private static final Pattern RUN_STEP_GOSEC_REGEX_PATTERN =
      Pattern.compile("^.*securego/gosec.*$", Pattern.DOTALL);

  /** A step config of a GitHub action that triggers GoSec scans with include rule. */
  private static final Pattern RUN_STEP_GOSEC_WITH_INCLUDE_REGEX_PATTERN =
      Pattern.compile("^.*-include=.*$", Pattern.DOTALL);

  /** A step config of a GitHub action that triggers GoSec scans with include rule. */
  private static final Pattern RUN_STEP_GOSEC_WITH_EXCLUDE_REGEX_PATTERN =
      Pattern.compile("^.*-exclude=.*$", Pattern.DOTALL);

  /**
   * Initializes a data provider.
   *
   * @param fetcher An interface to GitHub.
   */
  public GoSecDataProvider(GitHubDataFetcher fetcher) {
    super(fetcher, setOf(USES_GOSEC_SCAN_CHECKS, RUNS_GOSEC_SCANS, USES_GOSEC_WITH_RULES));
  }

  @Override
  protected ValueSet fetchValuesFor(GitHubProject project) throws IOException {
    logger.info("Figuring out how the project uses GoSec ...");

    LocalRepository repository = GitHubDataFetcher.localRepositoryFor(project);

    Value<Boolean> runsGoSec = RUNS_GOSEC_SCANS.value(false);
    Value<Boolean> usesGoSecScanChecks = USES_GOSEC_SCAN_CHECKS.value(false);
    Value<Boolean> usesGoSecWithSelectedRules = USES_GOSEC_WITH_RULES.value(false);

    // ideally, we're looking for a GitHub action that runs GoSec scan on pull requests
    // but if we just find an action that runs GoSec scans with or without rules is also fine
    for (Path configPath : findGitHubActionsIn(repository)) {
      try (InputStream content = Files.newInputStream(configPath)) {
        Map<String, Object> githubAction = Yaml.readMap(content);
        if (triggersScan(githubAction)) {
          runsGoSec = RUNS_GOSEC_SCANS.value(true);
          if (runsOnPullRequests(githubAction)) {
            usesGoSecScanChecks = USES_GOSEC_SCAN_CHECKS.value(true);
          }
          if (runsWithSelectedRules(githubAction)) {
            usesGoSecWithSelectedRules = USES_GOSEC_WITH_RULES.value(true);
            break;
          }
        }
      }
    }

    return ValueHashSet.from(runsGoSec, usesGoSecWithSelectedRules, usesGoSecScanChecks);
  }

  /**
   * Checks if the Github action uses GoSec sacn with rules.
   *
   * @param githubAction A config of the action
   * @return True if the Github Action has config to run GoSec with rules.
   */
  private boolean runsWithSelectedRules(Map<?, ?> githubAction) {
    return Optional.ofNullable(githubAction.get("jobs"))
        .filter(Map.class::isInstance)
        .map(Map.class::cast)
        .map(jobs -> jobs.values())
        .filter(Iterable.class::isInstance)
        .map(Iterable.class::cast)
        .map(GoSecDataProvider::checkAllJobs)
        .orElse(false);
  }

  /**
   * Checks if any job triggers a GoSec scan with rules.
   *
   * @param jobs The collection of jobs from GitHub action.
   * @return True if a step triggers a GoSec scan with rules, false otherwise.
   */
  private static boolean checkAllJobs(Iterable<?> jobs) {
    return IteratorUtils.toList(jobs.iterator()).stream()
        .filter(Map.class::isInstance)
        .map(Map.class::cast)
        .map(job -> job.get("steps"))
        .filter(Iterable.class::isInstance)
        .map(Iterable.class::cast)
        .anyMatch(GoSecDataProvider::checkAllSteps);
  }

  /**
   * Checks if a collection of steps from a GitHub action contains a step that triggers a GoSec
   * scan with rules.
   *
   * @param steps The steps to be checked.
   * @return True if the steps contain a step that triggers a GoSec scan with rules, false
   *     otherwise.
   */
  private static boolean checkAllSteps(Iterable<?> steps) {
    AtomicBoolean usesWithRules = new AtomicBoolean(false);
    IteratorUtils.toList(steps.iterator()).stream()
        .filter(Map.class::isInstance)
        .map(Map.class::cast)
        .forEach(
            step -> {
              if (goSecConfigExists(step.get("uses"))) {
                usesWithRules.set(hasArgsWithRules(step.get("with")));
              } else if (goSecConfigExists(step.get("run"))) {
                usesWithRules.set(runsGoSecWithRules(step.get("run")));
              }
            });
    return usesWithRules.get();
  }

  /**
   * Checks if a step contains config to run GoSec.
   *
   * @param step The step to be checked.
   * @return True if the step contains config to run GoSec scan, false otherwise.
   */
  private static boolean goSecConfigExists(Object step) {
    return Optional.ofNullable(step)
        .filter(String.class::isInstance)
        .map(String.class::cast)
        .map(run -> RUN_STEP_GOSEC_REGEX_PATTERN.matcher(run).matches())
        .orElse(false);
  }

  /**
   * Checks if a step contains config to run GoSec scans with rules.
   *
   * @param step The step to be checked.
   * @return True if the step contains config to run GoSec scan with rules, false otherwise.
   */
  private static boolean runsGoSecWithRules(Object step) {
    return Optional.ofNullable(step)
        .filter(String.class::isInstance)
        .map(String.class::cast)
        .map(run -> containsAnyRulesPattern(run))
        .orElse(false);
  }

  /**
   * Checks if a step contains argument config to run GoSec scans with rules.
   *
   * @param step The step to be checked.
   * @return True if the step contains arguments to run GoSec scan with rules, false otherwise.
   */
  private static boolean hasArgsWithRules(Object step) {
    return Optional.ofNullable(step)
        .filter(Map.class::isInstance)
        .map(Map.class::cast)
        .map(with -> with.get("args"))
        .filter(String.class::isInstance)
        .map(String.class::cast)
        .map(args -> containsAnyRulesPattern(args))
        .orElse(false);
  }

  /**
   * Checks if a step contains any pattern to run GoSec with rules.
   *
   * @param stepInfo The string info of a step
   * @return True if the step matches any pattern to run GoSec scan with rules, false otherwise.
   */
  private static boolean containsAnyRulesPattern(String stepInfo) {
    return RUN_STEP_GOSEC_WITH_INCLUDE_REGEX_PATTERN.matcher(stepInfo).matches()
        || RUN_STEP_GOSEC_WITH_EXCLUDE_REGEX_PATTERN.matcher(stepInfo).matches();
  }

  @Override
  public boolean triggersScan(Map<?, ?> githubAction) {
    return Optional.ofNullable(githubAction.get("jobs"))
        .filter(Map.class::isInstance)
        .map(Map.class::cast)
        .map(jobs -> jobs.values())
        .filter(Iterable.class::isInstance)
        .map(Iterable.class::cast)
        .map(GoSecDataProvider::scanJobs)
        .orElse(false);
  }

  /**
   * Checks if any step in a collection of jobs triggers a GoSec scan.
   *
   * @param jobs The collection of jobs from GitHub action.
   * @return True if a step triggers a GoSec scan, false otherwise.
   */
  private static boolean scanJobs(Iterable<?> jobs) {
    return IteratorUtils.toList(jobs.iterator()).stream()
        .filter(Map.class::isInstance)
        .map(Map.class::cast)
        .map(job -> job.get("steps"))
        .filter(Iterable.class::isInstance)
        .map(Iterable.class::cast)
        .anyMatch(GoSecDataProvider::hasGoSecStep);
  }

  /**
   * Checks if a collection of steps from a GitHub action contains a step that triggers a GoSec
   * scan.
   *
   * @param steps The steps to be checked.
   * @return True if the steps contain a step that triggers a GoSec scan, false otherwise.
   */
  private static boolean hasGoSecStep(Iterable<?> steps) {
    return hasGoSecStep(steps, "uses") || hasGoSecStep(steps, "run");
  }

  /**
   * Checks if a collection of steps from a GitHub action contains a step that triggers a GoSec
   * scan.
   *
   * @param steps The steps to be checked.
   * @return True if the steps contain a step that triggers a GoSec scan, false otherwise.
   */
  private static boolean hasGoSecStep(Iterable<?> steps, String stepKeyToCheck) {
    return IteratorUtils.toList(steps.iterator()).stream()
        .filter(Map.class::isInstance)
        .map(Map.class::cast)
        .map(step -> step.get(stepKeyToCheck))
        .filter(String.class::isInstance)
        .map(String.class::cast)
        .anyMatch(run -> RUN_STEP_GOSEC_REGEX_PATTERN.matcher(run).matches());
  }
}
