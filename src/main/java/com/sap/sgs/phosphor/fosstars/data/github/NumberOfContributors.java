package com.sap.sgs.phosphor.fosstars.data.github;

import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.NUMBER_OF_CONTRIBUTORS_LAST_THREE_MONTHS;

import com.sap.sgs.phosphor.fosstars.model.Value;
import com.sap.sgs.phosphor.fosstars.model.ValueSet;
import com.sap.sgs.phosphor.fosstars.model.value.IntegerValue;
import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import org.kohsuke.github.GHCommit;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GHUser;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitUser;

/**
 * This data provider counts how many people contributes to a project last 3 months. A contributor
 * is an author of a commit or a person who committed the commit. The provider iterates over all
 * commits which have been made for lat 3 months, and collect unique login names of contributors.
 */
public class NumberOfContributors extends AbstractGitHubDataProvider {

  /**
   * This constant means that no user found.
   */
  private static final String UNKNOWN_USER = "";

  /**
   * 90 days in millis.
   */
  private static final long DELTA = 90 * 24 * 60 * 60 * 1000L;

  /**
   * Initializes a data provider.
   *
   * @param where A GitHub organization of user name.
   * @param name A name of a repository.
   * @param github An interface to the GitHub API.
   */
  public NumberOfContributors(String where, String name, GitHub github) {
    super(where, name, github);
  }

  @Override
  public NumberOfContributors update(ValueSet values) throws IOException {
    Objects.requireNonNull(values, "Hey! Values can't be null!");
    System.out.println(
        "[+] Counting how many people contributed to the project in the last three months ...");

    Optional<Value> something = cache().get(url, NUMBER_OF_CONTRIBUTORS_LAST_THREE_MONTHS);
    if (something.isPresent()) {
      values.update(something.get());
      return this;
    }

    GHRepository repository = github.getRepository(path);
    Date date = new Date(System.currentTimeMillis() - DELTA);
    Set<String> contributors = new HashSet<>();
    for (GHCommit commit : repository.listCommits().asList()) {
      if (commit.getCommitDate().after(date)) {
        contributors.add(authorOf(commit));
        contributors.add(committerOf(commit));
      } else {
        break;
      }
    }
    contributors.remove(UNKNOWN_USER);

    Value<Integer> numberOfContributors = new IntegerValue(
        NUMBER_OF_CONTRIBUTORS_LAST_THREE_MONTHS, contributors.size());
    values.update(numberOfContributors);
    cache().put(url, numberOfContributors, tomorrow());

    return this;
  }

  /**
   * Figures out who authored a commit.
   *
   * @param commit The commit.
   * @return A name of the author, or {@link #UNKNOWN_USER} if no user name found.
   * @throws IOException If something went wrong.
   */
  static String authorOf(GHCommit commit) throws IOException {

    // first, try to figure out which user on GitHub authored the commit
    GHUser githubAuthor = commit.getAuthor();
    if (githubAuthor != null) {
      return githubAuthor.getLogin();
    }

    // it may happen that no GitHub user is available for the commit
    // for example, because the repository was just imported to GitHub
    // so that authors of commits are not mapped to GitHub users
    // then, try to get a name of the author for the git history
    GitUser gitCommitAuthor = commit.getCommitShortInfo().getAuthor();
    if (gitCommitAuthor != null) {
      return gitCommitAuthor.getName();
    }

    return UNKNOWN_USER;
  }

  /**
   * Figures out who committed a commit.
   *
   * @param commit The commit.
   * @return A name of the committer, or {@link #UNKNOWN_USER} if no user name found.
   * @throws IOException If something went wrong.
   */
  static String committerOf(GHCommit commit) throws IOException {

    // first, try to figure out which user on GitHub committed the changes
    GHUser githubCommitter = commit.getCommitter();
    if (githubCommitter != null) {
      return githubCommitter.getLogin();
    }

    // it may happen that no GitHub user is available for the commit
    // for example, because the repository was just imported to GitHub
    // so that authors of commits are not mapped to GitHub users
    // then, try to get a name of the committer for the git history
    GitUser gitCommitter = commit.getCommitShortInfo().getCommitter();
    if (gitCommitter != null) {
      return gitCommitter.getName();
    }

    return UNKNOWN_USER;
  }

}
