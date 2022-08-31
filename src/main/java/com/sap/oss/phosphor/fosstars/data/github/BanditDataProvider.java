package com.sap.oss.phosphor.fosstars.data.github;

import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.RUNS_BANDIT_SCANS;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_BANDIT_SCAN_CHECKS;
import static com.sap.oss.phosphor.fosstars.model.other.Utils.setOf;

import com.sap.oss.phosphor.fosstars.data.AbstractStaticScanToolsDataProvider;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.ValueSet;
import com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures;
import com.sap.oss.phosphor.fosstars.model.subject.oss.GitHubProject;
import com.sap.oss.phosphor.fosstars.model.value.ValueHashSet;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;
import java.util.regex.Pattern;

/**
 * The data provider gathers info about how a project uses Bandit for static analysis. In
 * particular, it tries to fill out the following features:
 * <ul>
 *  <li>{@link OssFeatures#RUNS_BANDIT_SCANS}</li>
 *  <li>{@link OssFeatures#USES_BANDIT_SCAN_CHECKS}</li>
 * </ul>
 */
public class BanditDataProvider extends AbstractStaticScanToolsDataProvider {

  /**
   * A Predicate to check the any step in a GitHub action that triggers analysis with Bandit.
   */
  private static final Map<String, Predicate<String>> MATCH_BANDIT_PREDICATE = new HashMap<>();

  static {
    {
      MATCH_BANDIT_PREDICATE.put("uses",
          step -> Pattern.compile(".*bandit.*$", Pattern.DOTALL).matcher(step).matches());
      MATCH_BANDIT_PREDICATE.put("run",
          step -> Pattern.compile("^.*bandit .*$", Pattern.DOTALL).matcher(step).matches());
    }
  }

  /**
   * Initializes a data provider.
   *
   * @param fetcher An interface to GitHub.
   */
  public BanditDataProvider(GitHubDataFetcher fetcher) {
    super(fetcher, setOf(RUNS_BANDIT_SCANS, USES_BANDIT_SCAN_CHECKS));
  }

  @Override
  protected ValueSet fetchValuesFor(GitHubProject project) throws IOException {
    logger.info("Figuring out how the project uses Bandit ...");

    LocalRepository repository = GitHubDataFetcher.localRepositoryFor(project);

    // ideally, we're looking for a GitHub action that runs Bandit scan on pull requests
    // but if we just find an action that runs Bandit scans, that's also fine
    Visitor visitor = withVisitor();
    browse(repository, MATCH_BANDIT_PREDICATE, Collections.emptyMap(), visitor);

    Value<Boolean> runsBandit = RUNS_BANDIT_SCANS.value(visitor.runCheck);
    Value<Boolean> usesBanditScanChecks = USES_BANDIT_SCAN_CHECKS.value(visitor.usesCheck);

    return ValueHashSet.from(runsBandit, usesBanditScanChecks);
  }
}
