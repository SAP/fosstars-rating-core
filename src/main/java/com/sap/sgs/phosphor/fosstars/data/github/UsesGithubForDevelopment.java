package com.sap.sgs.phosphor.fosstars.data.github;

import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.USES_GITHUB_FOR_DEVELOPMENT;

import com.sap.sgs.phosphor.fosstars.model.Value;
import com.sap.sgs.phosphor.fosstars.model.ValueSet;
import com.sap.sgs.phosphor.fosstars.model.value.BooleanValue;
import com.sap.sgs.phosphor.fosstars.tool.github.GitHubProject;
import java.io.IOException;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;

/**
 * This data provider checks if a project uses GitHub as a main development platform. The check
 * would be based on mirror url is null or empty. This will ensure whether the underlying project is
 * a mirror project or not.
 */
public class UsesGithubForDevelopment extends AbstractGitHubDataProvider {

  /**
   * Initializes a data provider.
   *
   * @param github An interface to the GitHub API.
   */
  public UsesGithubForDevelopment(GitHub github) {
    super(github);
  }

  @Override
  protected UsesGithubForDevelopment doUpdate(GitHubProject project, ValueSet values)
      throws IOException {
    logger.info("Figuring out if the project uses GitHub as the main development platform ...");

    Optional<Value> something = cache.get(project, USES_GITHUB_FOR_DEVELOPMENT);
    if (something.isPresent()) {
      values.update(something.get());
      return this;
    }

    Value<Boolean> usesGithubForDevelopment = usesGithubForDevelopment(project);
    values.update(usesGithubForDevelopment);
    cache.put(project, usesGithubForDevelopment, tomorrow());

    return this;
  }

  /**
   * Checks if the project uses GitHub as a main development platform.
   *
   * @param project of type {@link GitHubProject}.
   * @return {@link BooleanValue} value of the feature.
   */
  private Value<Boolean> usesGithubForDevelopment(GitHubProject project) {
    // TODO: This is just a basic check, more constraints have to be considered in the future with
    //       more clarity. Please refer https://github.com/SAP/fosstars-rating-core/issues/122
    try {
      return USES_GITHUB_FOR_DEVELOPMENT.value(!isMirror(project));
    } catch (IOException e) {
      logger.warn("Couldn't fetch data, something went wrong!", e);
      return USES_GITHUB_FOR_DEVELOPMENT.unknown();
    }
  }

  /**
   * Checks if the input GitHub project is a mirror or an original project.
   * 
   * @param project of type {@link GitHubProject}.
   * @return true if the project is a mirror project. Otherwise, false.
   * @throws IOException may throw an exception during REST call to GitHub API.
   */
  private boolean isMirror(GitHubProject project) throws IOException {
    GHRepository repository = gitHubDataFetcher().repositoryFor(project, github);
    return StringUtils.isNotEmpty(repository.getMirrorUrl());
  }
}
