package com.sap.oss.phosphor.fosstars.data.github;

import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.HAS_BUG_BOUNTY_PROGRAM;
import static org.junit.Assert.assertEquals;

import com.sap.oss.phosphor.fosstars.model.subject.oss.GitHubProject;
import java.io.IOException;
import org.junit.Test;

public class HasBugBountyProgramTest extends TestGitHubDataFetcherHolder {

  @Test
  public void supportedFeature() throws IOException {
    HasBugBountyProgram provider = new HasBugBountyProgram(fetcher);
    assertEquals(provider.supportedFeature(), HAS_BUG_BOUNTY_PROGRAM);
  }

  @Test
  public void fetchValueFor() throws IOException {
    HasBugBountyProgram provider = new HasBugBountyProgram(fetcher);

    GitHubProject curl = new GitHubProject("curl", "curl");
    assertEquals(HAS_BUG_BOUNTY_PROGRAM.value(true), provider.fetchValueFor(curl));

    GitHubProject other = new GitHubProject("other", "test");
    assertEquals(HAS_BUG_BOUNTY_PROGRAM.value(false), provider.fetchValueFor(other));
  }
}