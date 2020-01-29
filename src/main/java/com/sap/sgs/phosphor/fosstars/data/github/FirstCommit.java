package com.sap.sgs.phosphor.fosstars.data.github;

import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.FIRST_COMMIT_DATE;

import com.sap.sgs.phosphor.fosstars.data.UserCallback;
import com.sap.sgs.phosphor.fosstars.model.Value;
import com.sap.sgs.phosphor.fosstars.model.value.DateValue;
import com.sap.sgs.phosphor.fosstars.model.value.UnknownValue;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.kohsuke.github.GHCommit;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.PagedIterator;

/**
 * This data provider returns a date of the first commit.
 */
public class FirstCommit extends AbstractGitHubDataProvider {

  /**
   * 7 days in millis.
   */
  private static final long DELTA = 7 * 24 * 60 * 60 * 1000L;

  public FirstCommit(String where, String name, GitHub github, boolean mayTalk) {
    super(where, name, github, mayTalk);
  }

  @Override
  public Value<Date> get(UserCallback callback) throws IOException {
    System.out.println("[+] Figuring out when the first commit was done ...");

    Optional<Value> something = cache().get(url, FIRST_COMMIT_DATE);
    if (something.isPresent()) {
      return something.get();
    }

    GHRepository repository = github.getRepository(path);
    long millis = repository.getCreatedAt().getTime() + DELTA;

    PagedIterator<GHCommit> iterator = repository.queryCommits().until(millis)
        .list().withPageSize(10000).iterator();
    List<GHCommit> lastPage = iterator.nextPage();
    while (iterator.hasNext()) {
      lastPage = iterator.nextPage();
    }

    Value<Date> firstCommit;
    if (lastPage == null) {
      firstCommit = UnknownValue.of(FIRST_COMMIT_DATE);
    } else {
      firstCommit = new DateValue(
          FIRST_COMMIT_DATE,
          lastPage.get(lastPage.size() - 1).getCommitDate());
    }

    cache().put(url, firstCommit);

    return firstCommit;
  }
}
