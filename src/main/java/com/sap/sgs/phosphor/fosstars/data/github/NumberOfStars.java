package com.sap.sgs.phosphor.fosstars.data.github;

import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.NUMBER_OF_GITHUB_STARS;

import com.sap.sgs.phosphor.fosstars.model.ValueSet;
import com.sap.sgs.phosphor.fosstars.model.value.IntegerValue;
import java.io.IOException;
import java.util.Objects;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;

/**
 * This data provider returns a number of stars for a project.
 */
public class NumberOfStars extends AbstractGitHubDataProvider {

  /**
   * Initializes a data provider.
   *
   * @param where A GitHub organization of user name.
   * @param name A name of a repository.
   * @param github An interface to the GitHub API.
   */
  public NumberOfStars(String where, String name, GitHub github) {
    super(where, name, github);
  }

  @Override
  public NumberOfStars update(ValueSet values) throws IOException {
    Objects.requireNonNull(values, "Hey! Values can't be null!");
    logger.info("Counting how many stars the project has ...");

    GHRepository repository = github.getRepository(path);
    int stars = repository.getStargazersCount();
    values.update(new IntegerValue(NUMBER_OF_GITHUB_STARS, stars));
    return this;
  }
}
