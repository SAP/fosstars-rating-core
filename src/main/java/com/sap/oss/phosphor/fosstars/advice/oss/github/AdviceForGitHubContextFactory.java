package com.sap.oss.phosphor.fosstars.advice.oss.github;

import com.sap.oss.phosphor.fosstars.advice.oss.AbstractOssAdvisor.OssAdviceContextFactory;
import com.sap.oss.phosphor.fosstars.advice.oss.OssAdviceContentYamlStorage.OssAdviceContext;
import com.sap.oss.phosphor.fosstars.model.Subject;
import com.sap.oss.phosphor.fosstars.model.subject.oss.GitHubProject;
import java.util.Optional;

/**
 * A context factory that provides contexts for projects on GitHub.
 */
public class AdviceForGitHubContextFactory implements OssAdviceContextFactory {

  /**
   * Singleton.
   */
  public static final AdviceForGitHubContextFactory INSTANCE = new AdviceForGitHubContextFactory();

  /**
   * Private constructor.
   */
  private AdviceForGitHubContextFactory() {

  }

  @Override
  public OssAdviceContext contextFor(Subject subject) {
    return new OssAdviceContext() {

      @Override
      public Optional<String> lgtmProjectLink() {
        if (subject instanceof GitHubProject) {
          GitHubProject project = (GitHubProject) subject;
          return Optional.of(
              String.format("https://lgtm.com/projects/g/%s/%s",
                  project.organization().name(), project.name()));
        }

        return Optional.empty();
      }

      @Override
      public Optional<String> suggestSecurityPolicyLink() {
        if (subject instanceof GitHubProject) {
          GitHubProject project = (GitHubProject) subject;
          return Optional.of(
              String.format("https://github.com/%s/%s/security/policy",
                  project.organization().name(), project.name()));
        }

        return Optional.empty();
      }
    };
  }
}
