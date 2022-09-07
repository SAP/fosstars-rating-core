package com.sap.oss.phosphor.fosstars.data.github;

import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.RUNS_CODEQL_SCANS;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_CODEQL_CHECKS;
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

/**
 * The data provider gathers info about how a project uses CodeQL for static analysis. In
 * particular, it tires to fill out the following features:
 * <ul>
 *  <li>{@link OssFeatures#RUNS_CODEQL_SCANS}</li>
 *  <li>{@link OssFeatures#USES_CODEQL_CHECKS}</li>
 * </ul>
 *
 * @see LgtmDataProvider
 */
public class CodeqlDataProvider extends AbstractStaticScanToolsDataProvider {

  /**
   * A predicate to check if any step in GitHub action that triggers analysis with CodeQL.
   */
  private static final Map<String, Predicate<String>> MATCH_CODEQL_ANALYZE_PREDICATE =
      new HashMap<>();

  static {
    MATCH_CODEQL_ANALYZE_PREDICATE.put("uses",
        uses -> uses.startsWith("github/codeql-action/analyze"));
  }

  /**
   * Initializes a data provider.
   *
   * @param fetcher An interface to GitHub.
   */
  public CodeqlDataProvider(GitHubDataFetcher fetcher) {
    super(fetcher, setOf(RUNS_CODEQL_SCANS, USES_CODEQL_CHECKS));
  }

  @Override
  protected ValueSet fetchValuesFor(GitHubProject project) throws IOException {
    logger.info("Figuring out how the project uses CodeQL ...");

    LocalRepository repository = GitHubDataFetcher.localRepositoryFor(project);

    Visitor visitor = withVisitor();
    browse(repository, MATCH_CODEQL_ANALYZE_PREDICATE, Collections.emptyMap(), visitor);

    Value<Boolean> runsCodeqlScans = RUNS_CODEQL_SCANS.value(visitor.runCheck);
    Value<Boolean> usesCodeqlChecks = USES_CODEQL_CHECKS.value(visitor.usesCheck);

    return ValueHashSet.from(runsCodeqlScans, usesCodeqlChecks);
  }
}
