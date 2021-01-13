package com.sap.oss.phosphor.fosstars.data.github;

import java.util.Date;
import java.util.List;

/**
 * An interface of a commit.
 */
public interface Commit {

  /**
   * Get a date of the commit.
   *
   * @return A date when the commit was done.
   */
  Date date();

  /**
   * Get a name of the committer.
   *
   * @return A name of the committer.
   */
  String committerName();

  /**
   * Get a name of the author.
   *
   * @return A name of the author.
   */
  String authorName();

  /**
   * Tells whether the commit is signed or not.
   *
   * @return True if the commit is signed, false otherwise.
   */
  boolean isSigned();

  /**
   * Returns the commit message.
   *
   * @return The commit message.
   */
  List<String> message();
}
