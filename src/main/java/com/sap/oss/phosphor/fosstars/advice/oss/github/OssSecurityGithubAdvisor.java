package com.sap.oss.phosphor.fosstars.advice.oss.github;

import com.sap.oss.phosphor.fosstars.advice.CompositeAdvisor;
import com.sap.oss.phosphor.fosstars.advice.oss.ArtifactVersionAdvisor;
import com.sap.oss.phosphor.fosstars.advice.oss.BanditAdvisor;
import com.sap.oss.phosphor.fosstars.advice.oss.CodeqlAdvisor;
import com.sap.oss.phosphor.fosstars.advice.oss.DependabotAdvisor;
import com.sap.oss.phosphor.fosstars.advice.oss.FindSecBugsAdvisor;
import com.sap.oss.phosphor.fosstars.advice.oss.FuzzingAdvisor;
import com.sap.oss.phosphor.fosstars.advice.oss.GoSecAdvisor;
import com.sap.oss.phosphor.fosstars.advice.oss.LgtmAdvisor;
import com.sap.oss.phosphor.fosstars.advice.oss.MemorySafetyAdvisor;
import com.sap.oss.phosphor.fosstars.advice.oss.NoHttpAdvisor;
import com.sap.oss.phosphor.fosstars.advice.oss.OwaspDependencyCheckAdvisor;
import com.sap.oss.phosphor.fosstars.advice.oss.SecurityPolicyAdvisor;
import com.sap.oss.phosphor.fosstars.advice.oss.SigningAdvisor;
import com.sap.oss.phosphor.fosstars.advice.oss.SnykAdvisor;

/**
 * This is an advisor for open-source projects hosted on GitHub.
 * The advisor is based on multiple advisors for open-source projects.
 */
public class OssSecurityGithubAdvisor extends CompositeAdvisor {

  /**
   * Create a new advisor.
   */
  public OssSecurityGithubAdvisor() {
    super(
        new CodeqlAdvisor(AdviceForGitHubContextFactory.INSTANCE),
        new LgtmAdvisor(AdviceForGitHubContextFactory.INSTANCE),
        new SecurityPolicyAdvisor(AdviceForGitHubContextFactory.INSTANCE),
        new FindSecBugsAdvisor(AdviceForGitHubContextFactory.INSTANCE),
        new MemorySafetyAdvisor(AdviceForGitHubContextFactory.INSTANCE),
        new ArtifactVersionAdvisor(AdviceForGitHubContextFactory.INSTANCE),
        new FuzzingAdvisor(AdviceForGitHubContextFactory.INSTANCE),
        new DependabotAdvisor(AdviceForGitHubContextFactory.INSTANCE),
        new OwaspDependencyCheckAdvisor(AdviceForGitHubContextFactory.INSTANCE),
        new SigningAdvisor(AdviceForGitHubContextFactory.INSTANCE),
        new NoHttpAdvisor(AdviceForGitHubContextFactory.INSTANCE),
        new BanditAdvisor(AdviceForGitHubContextFactory.INSTANCE),
        new SnykAdvisor(AdviceForGitHubContextFactory.INSTANCE),
        new GoSecAdvisor(AdviceForGitHubContextFactory.INSTANCE));
  }
}