package com.sap.sgs.phosphor.fosstars.data.github;

import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.NUMBER_OF_GITHUB_STARS;

import com.sap.sgs.phosphor.fosstars.model.Value;
import com.sap.sgs.phosphor.fosstars.model.ValueSet;
import com.sap.sgs.phosphor.fosstars.tool.github.GitHubProject;
import java.io.IOException;
import java.util.Optional;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;

/**
 * This data provider returns a number of stars for a project.
 */
public class NumberOfStars extends AbstractGitHubDataProvider {

  /**
   * Initializes a data provider.
   *
   * @param github An interface to the GitHub API.
   */
  public NumberOfStars(GitHub github) {
    super(github);
  }

  @Override
  protected NumberOfStars doUpdate(GitHubProject project, ValueSet values) throws IOException {
    logger.info("Counting how many stars the project has ...");
    values.update(starsOf(project));
    return this;
  }

  /**
   * Looks for a number of stars for a project on GitHub.
   *
   * @param project The project.
   * @return The number of stars.
   * @throws IOException If something went wrong.
   */
  private Value<Integer> starsOf(GitHubProject project) throws IOException {
    Optional<Value> something = cache.get(project, NUMBER_OF_GITHUB_STARS);
    if (something.isPresent()) {
      return something.get();
    }

    GHRepository repository = github.getRepository(project.path());
    Value<Integer> value = NUMBER_OF_GITHUB_STARS.value(repository.getStargazersCount());
    cache.put(project, value);

    return value;
  }
}
