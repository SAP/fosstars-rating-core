package com.sap.oss.phosphor.fosstars.github;

import com.sap.oss.phosphor.fosstars.data.github.LocalRepository;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

/**
 * An implementation of {@link GitHubVisitor} that does nothing.
 */
public abstract class AbstractGitHubVisitor implements GitHubVisitor {

  @Override
  public void visitPreCommitHook(LocalRepository repository,
      Map<String, Predicate<String>> matchers, Set<Location> locations) throws IOException {
    // do nothing
  }

  @Override
  public void visitIniConfig(LocalRepository repository, Map<String, Predicate<String>> matchers,
      Set<Location> locations) throws IOException {
    // do nothing
  }

  @Override
  public void visitSourceCode(LocalRepository repository, Map<String, Predicate<String>> matchers,
      Set<Location> locations) throws IOException {
    // don nothing
  }

  @Override
  public void visitGitHubAction(LocalRepository repository, Map<String, Predicate<String>> matchers,
      Map<String, Predicate<String>> configMatchers, Set<Location> locations) throws IOException {
    // do nothing
  }
}
