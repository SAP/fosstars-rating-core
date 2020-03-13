package com.sap.sgs.phosphor.fosstars.data.github;

import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.NUMBER_OF_COMMITS_LAST_THREE_MONTHS;

import com.sap.sgs.phosphor.fosstars.model.Value;
import com.sap.sgs.phosphor.fosstars.model.ValueSet;
import com.sap.sgs.phosphor.fosstars.model.value.IntegerValue;
import java.io.IOException;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import org.kohsuke.github.GHCommit;
import org.kohsuke.github.GHRepository;
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
   * @param where A GitHub organization of user name.
   * @param name A name of a repository.
   * @param github An interface to the GitHub API.
   */
  public NumberOfCommits(String where, String name, GitHub github) {
    super(where, name, github);
  }

  @Override
  public NumberOfCommits update(ValueSet values) throws IOException {
    Objects.requireNonNull(values, "Hey! Values can't be null!");
    System.out.println("[+] Counting how many commits have been done in the last three months ...");

    Optional<Value> something = cache().get(url, NUMBER_OF_COMMITS_LAST_THREE_MONTHS);
    if (something.isPresent()) {
      values.update(something.get());
      return this;
    }

    GHRepository repository = github.getRepository(path);
    int counter = 0;
    Date date = new Date(System.currentTimeMillis() - DELTA);
    for (GHCommit commit : repository.listCommits()) {
      if (commit.getCommitDate().after(date)) {
        counter++;
      } else {
        break;
      }
    }

    Value<Integer> numberOfCommits = new IntegerValue(NUMBER_OF_COMMITS_LAST_THREE_MONTHS, counter);
    values.update(numberOfCommits);
    cache().put(url, numberOfCommits, tomorrow());

    return this;
  }
}
