package com.sap.sgs.phosphor.fosstars.advice.oss.github;

import com.sap.sgs.phosphor.fosstars.advice.CompositeAdvisor;
import com.sap.sgs.phosphor.fosstars.advice.oss.AbstractOssAdvisor.OssAdviceContextFactory;
import com.sap.sgs.phosphor.fosstars.advice.oss.CodeqlAdvisor;
import com.sap.sgs.phosphor.fosstars.advice.oss.FindSecBugsAdvisor;
import com.sap.sgs.phosphor.fosstars.advice.oss.LgtmAdvisor;
import com.sap.sgs.phosphor.fosstars.advice.oss.OssAdviceContentYamlStorage.OssAdviceContext;
import com.sap.sgs.phosphor.fosstars.advice.oss.SecurityPolicyAdvisor;
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
  private static final OssAdviceContextFactory CONTEXT_FACTORY = subject -> new OssAdviceContext() {

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

  /**
   * Create a new advisor.
   */
  public OssSecurityGithubAdvisor() {
    super(
        new CodeqlAdvisor(CONTEXT_FACTORY),
        new LgtmAdvisor(CONTEXT_FACTORY),
        new SecurityPolicyAdvisor(CONTEXT_FACTORY),
        new FindSecBugsAdvisor(CONTEXT_FACTORY));
  }
}
