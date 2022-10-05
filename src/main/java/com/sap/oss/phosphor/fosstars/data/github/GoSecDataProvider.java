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
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;
import java.util.regex.Pattern;

/**
 * The data provider gathers info about how a project uses GoSec for static analysis. In
 * particular, it tries to fill out the following features:
 *
 * <ul>
 *   <li>{@link OssFeatures#USES_GOSEC_SCAN_CHECKS}
 *   <li>{@link OssFeatures#RUNS_GOSEC_SCANS}
 *   <li>{@link OssFeatures#USES_GOSEC_WITH_RULES}
 * </ul>
 */
public class GoSecDataProvider extends AbstractStaticScanToolsDataProvider {
  
  /**
   * A Predicate to check step in a GitHub action that triggers analysis with GoSec.
   */
  private static final Map<String, Predicate<String>> MATCH_GOSEC_PREDICATE = new HashMap<>();

  static {
    {
      MATCH_GOSEC_PREDICATE.put("uses",
          step -> Pattern.compile(".*securego/gosec.*$", Pattern.DOTALL).matcher(step).matches());
      MATCH_GOSEC_PREDICATE.put("run",
          step -> Pattern.compile("^.*gosec .*$", Pattern.DOTALL).matcher(step).matches());
    }
  }
  
  /**
   * A Predicate to check step in a GitHub action that triggers analysis with GoSec with specific
   * configs.
   */
  private static final Map<String, Predicate<String>> MATCH_GOSEC_STEP_CONFIG_PREDICATE =
      new HashMap<>();

  static {
    {
      MATCH_GOSEC_STEP_CONFIG_PREDICATE.put("with",
          step -> Pattern.compile("(^.*-include=.*$)|(^.*-exclude=.*$)", Pattern.DOTALL)
              .matcher(step).matches());
      MATCH_GOSEC_STEP_CONFIG_PREDICATE.put("run",
          step -> Pattern.compile("(^.*-include=.*$)|(^.*-exclude=.*$)", Pattern.DOTALL)
              .matcher(step).matches());
    }
  }

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

    // ideally, we're looking for a GitHub action that runs GoSec scan on pull requests
    // but if we just find an action that runs GoSec scans with or without rules is also fine
    Visitor visitor = withVisitor();
    browse(repository, MATCH_GOSEC_PREDICATE, MATCH_GOSEC_STEP_CONFIG_PREDICATE, visitor);

    Value<Boolean> runsGoSec = RUNS_GOSEC_SCANS.value(visitor.runCheck);
    Value<Boolean> usesGoSecScanChecks = USES_GOSEC_SCAN_CHECKS.value(visitor.usesCheck);
    Value<Boolean> usesGoSecWithSelectedRules = USES_GOSEC_WITH_RULES.value(visitor.hasRules);

    return ValueHashSet.from(runsGoSec, usesGoSecWithSelectedRules, usesGoSecScanChecks);
  }
}