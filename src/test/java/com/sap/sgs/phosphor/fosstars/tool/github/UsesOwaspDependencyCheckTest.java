package com.sap.sgs.phosphor.fosstars.tool.github;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assume.assumeNotNull;

import com.sap.sgs.phosphor.fosstars.data.Terminal;
import com.sap.sgs.phosphor.fosstars.data.github.UsesOwaspDependencyCheck;
import com.sap.sgs.phosphor.fosstars.model.Value;
import java.io.IOException;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.kohsuke.github.GitHub;

/**
 * TODO: This test fails when GitHub API rate limit exceeded or some networking issues occur.
 *       The test needs to be re-written to use mocks.
 *       Meanwhile, the test is ignored.
 */
@Ignore
public class UsesOwaspDependencyCheckTest {

  private static GitHub github;

  @BeforeClass
  public static void initGithubConnection() {
    try {
      github = GitHub.connectAnonymously();
      github.checkApiUrlValidity();
    } catch (Exception e) {
      System.out.println("Couldn't connect to GitHub!");
      e.printStackTrace();
      github = null;
    }
  }

  @Test
  public void testApacheCxf() throws IOException {
    assumeNotNull(github);
    UsesOwaspDependencyCheck provider = new UsesOwaspDependencyCheck(
        "apache", "cxf", github, false);
    Value value = provider.get(new Terminal());
    assertNotNull(value);
    assertEquals(Boolean.TRUE, value.get());
  }

  @Test
  public void testApachePoi() throws IOException {
    assumeNotNull(github);
    UsesOwaspDependencyCheck provider = new UsesOwaspDependencyCheck(
        "apache", "poi", github, false);
    Value value = provider.get(new Terminal());
    assertNotNull(value);
    assertEquals(Boolean.FALSE, value.get());
  }

  @Test
  public void testSpringSecurityOAuth() throws IOException {
    assumeNotNull(github);
    UsesOwaspDependencyCheck provider = new UsesOwaspDependencyCheck(
        "spring-projects", "spring-security-oauth", github, false);
    Value value = provider.get(new Terminal());
    assertNotNull(value);
    assertEquals(Boolean.FALSE, value.get());
  }

}