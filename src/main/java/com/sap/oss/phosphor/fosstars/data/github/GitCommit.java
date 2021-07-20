package com.sap.oss.phosphor.fosstars.data.github;

import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.eclipse.jgit.lib.PersonIdent;
import org.eclipse.jgit.revwalk.RevCommit;

/**
 * <p>A wrapper for a commit provided by JGit.</p>
 * <p>The docs for RevCommit class say that its methods are fairly expensive.
 * That's why the GitCommit class calls them only when required.</p>
 *
 * @see <a href="https://download.eclipse.org/jgit/site/5.7.0.202003110725-r/apidocs/index.html">RevCommit</a>
 */
public class GitCommit implements Commit {

  /**
   * An instance of {@link RevCommit} from JGit.
   */
  private final RevCommit revCommit;

  /**
   * Author's identity.
   */
  private PersonIdent authorIdentity;

  /**
   * Committer's identity.
   */
  private PersonIdent committerIdentity;

  /**
   * A signature if available.
   */
  private byte[] signature;

  /**
   * Initializes a new commit.
   *
   * @param revCommit An instance of {@link RevCommit} from JGit.
   */
  GitCommit(RevCommit revCommit) {
    this.revCommit = Objects.requireNonNull(revCommit, "On no! Rev commit is null!");
  }

  @Override
  public Date date() {
    return Date.from(Instant.ofEpochSecond(revCommit.getCommitTime()));
  }

  @Override
  public String committerName() {
    if (committerIdentity == null) {
      committerIdentity = revCommit.getCommitterIdent();
    }
    return committerIdentity.getName();
  }

  @Override
  public String authorName() {
    if (authorIdentity == null) {
      authorIdentity = revCommit.getCommitterIdent();
    }
    return authorIdentity.getName();
  }

  @Override
  public boolean isSigned() {
    if (signature == null) {
      signature = revCommit.getRawGpgSignature();
    }
    return signature != null && signature.length > 0;
  }

  @Override
  public List<String> message() {
    return Arrays.stream(revCommit.getFullMessage().split("\\r?\\n"))
        .map(String::trim).filter(s -> !s.isEmpty()).collect(Collectors.toList());
  }

  /**
   * Get an underlying {@link RevCommit}.
   *
   * @return An underlying {@link RevCommit}.
   */
  RevCommit raw() {
    return revCommit;
  }
}
