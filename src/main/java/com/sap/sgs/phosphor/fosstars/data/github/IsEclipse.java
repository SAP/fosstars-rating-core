package com.sap.sgs.phosphor.fosstars.data.github;

import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.IS_ECLIPSE;

import com.sap.sgs.phosphor.fosstars.model.ValueSet;
import com.sap.sgs.phosphor.fosstars.tool.github.GitHubProject;
import org.kohsuke.github.GitHub;

/**
 * The data provider tries to figure out if an open-source project belongs to the Eclipse Software
 * Foundation.
 */
public class IsEclipse extends AbstractGitHubDataProvider {

  /**
   * Initializes a data provider.
   *
   * @param github An interface to the GitHub API.
   */
  public IsEclipse(GitHub github) {
    super(github);
  }

  @Override
  protected IsEclipse doUpdate(GitHubProject project, ValueSet values) {
    logger.info("Figuring out if the project belongs to the Eclipse Software Foundation ...");
    values.update(IS_ECLIPSE.value("eclipse".equalsIgnoreCase(project.organization().name())));
    return this;
  }
}
