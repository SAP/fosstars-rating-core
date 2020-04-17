package com.sap.sgs.phosphor.fosstars.data.github;

import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.NUMBER_OF_COMMITS_LAST_THREE_MONTHS;

import com.sap.sgs.phosphor.fosstars.model.Value;
import com.sap.sgs.phosphor.fosstars.model.ValueSet;
import com.sap.sgs.phosphor.fosstars.model.value.IntegerValue;
import com.sap.sgs.phosphor.fosstars.tool.github.GitHubProject;
import java.io.IOException;
import java.util.Date;
import java.util.Optional;
import org.kohsuke.github.GitHub;

/**
 * This data provider returns a number of commits last 3 months.
 */
public class NumberOfCommits extends AbstractGitHubDataProvider {

  /**
   * 3 months in millis.
   */
  private static final long DELTA = 90 * 24 * 60 * 60 * 1000L;

  /**
   * Initializes a data provider.
   *
   * @param github An interface to the GitHub API.
   */
  public NumberOfCommits(GitHub github) {
    super(github);
  }

  @Override
  protected NumberOfCommits doUpdate(GitHubProject project, ValueSet values) throws IOException {
    logger.info("Counting how many commits have been done in the last three months ...");

    Optional<Value> something = cache.get(project, NUMBER_OF_COMMITS_LAST_THREE_MONTHS);
    if (something.isPresent()) {
      values.update(something.get());
      return this;
    }

    Date date = new Date(System.currentTimeMillis() - DELTA);

    Value<Integer> numberOfCommits = new IntegerValue(NUMBER_OF_COMMITS_LAST_THREE_MONTHS,
        gitHubDataFetcher().commitsAfter(date, project, github).size());
    values.update(numberOfCommits);
    cache.put(project, numberOfCommits, tomorrow());

    return this;
  }
}
