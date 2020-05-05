package com.sap.sgs.phosphor.fosstars.data.github;

import java.util.Date;

/**
 * An interface of a commit.
 */
public interface Commit {

  /**
   * Returns a date when the commit was done.
   */
  Date date();

  /**
   * Returns a name of the committer.
   */
  String committerName();

  /**
   * Returns a name of the author.
   */
  String authorName();

  /**
   * Returns true if the commit is signed, false otherwise.
   */
  boolean isSigned();
}
