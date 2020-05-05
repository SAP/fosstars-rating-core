package com.sap.sgs.phosphor.fosstars.data.github;

import java.time.Instant;
import java.util.Date;
import java.util.Objects;
import org.eclipse.jgit.lib.PersonIdent;
import org.eclipse.jgit.revwalk.RevCommit;

/**
 * A wrapper for a commit obtained with JGit.
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
}
