package com.sap.sgs.phosphor.fosstars.advice.oss.github;

import com.sap.sgs.phosphor.fosstars.advice.CompositeAdvisor;
import com.sap.sgs.phosphor.fosstars.advice.oss.AbstractOssScoreAdvisor.ContextFactory;
import com.sap.sgs.phosphor.fosstars.advice.oss.CodeqlScoreAdvisor;
import com.sap.sgs.phosphor.fosstars.advice.oss.LgtmScoreAdvisor;
import com.sap.sgs.phosphor.fosstars.advice.oss.OssAdviceContentYamlStorage.OssAdviceContext;
import com.sap.sgs.phosphor.fosstars.model.subject.oss.GitHubProject;
import java.util.Optional;

/**
 * This is an advisor for open-source projects hosted on GitHub.
 * The advisor is based on multiple advisors for open-source projects.
 */
public class OssSecurityGithubAdvisor extends CompositeAdvisor {

  /**
   * A context factory that provides contexts for projects on GitHub.
   */
  private static final ContextFactory CONTEXT_FACTORY = subject -> new OssAdviceContext() {

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
  };

  /**
   * Create a new advisor.
   */
  public OssSecurityGithubAdvisor() {
    super(new CodeqlScoreAdvisor(CONTEXT_FACTORY), new LgtmScoreAdvisor(CONTEXT_FACTORY));
  }
}
