package com.sap.oss.phosphor.fosstars.github;

import com.sap.oss.phosphor.fosstars.data.github.LocalRepository;
import com.sap.oss.phosphor.fosstars.model.subject.oss.GitHubProject;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

/**
 * A visitor for visiting elements in a {@link GitHubProject}.
 */
public interface GitHubVisitor {

  /**
   * Known locations of elements in a {@link GitHubProject}.
   */
  enum Location {
    PRE_COMMIT_HOOK, INI_CONFIG, TYPE_PY, GITHUB_ACTION
  }

  /**
   * Visit the pre-commit hook config file.
   *
   * @param repository The {@link LocalRepository}.
   * @param matchers map of {@link Predicate}s to parse for a specific content and match the
   *        predicate.
   * @param locations A set of locations that tells where the plugin is located.
   * @throws IOException if something went wrong.
   */
  void visitPreCommitHook(LocalRepository repository, Map<String, Predicate<String>> matchers,
      Set<Location> locations) throws IOException;

  /**
   * Visit the *.ini files and check for the config.
   *
   * @param repository The {@link LocalRepository}.
   * @param matchers map of {@link Predicate}s to parse for a specific content and match the
   *        predicate.
   * @param locations A set of locations that tells where the plugin is located.
   * @throws IOException if something went wrong.
   */
  void visitIniConfig(LocalRepository repository, Map<String, Predicate<String>> matchers,
      Set<Location> locations) throws IOException;

  /**
   * Visit the source code and check for the config.
   *
   * @param repository The {@link LocalRepository}.
   * @param matchers map of {@link Predicate}s to parse for a specific content and match the
   *        predicate.
   * @param locations A set of locations that tells where the plugin is located.
   * @throws IOException if something went wrong.
   */
  void visitSourceCode(LocalRepository repository, Map<String, Predicate<String>> matchers,
      Set<Location> locations) throws IOException;

  /**
   * Visit the GitHub action and check for the config.
   *
   * @param repository The {@link LocalRepository}.
   * @param matchers map of {@link Predicate}s to parse for a specific content and match the
   *        predicate.
   * @param configMatchers map of {@link Predicate}s to parse for a specific content and match the
   *        specific configs with the given predicate.
   * @param locations A set of locations that tells where the plugin is located.
   * @throws IOException if something went wrong.
   */
  void visitGitHubAction(LocalRepository repository, Map<String, Predicate<String>> matchers,
      Map<String, Predicate<String>> configMatchers, Set<Location> locations) throws IOException;
}
