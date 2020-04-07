package com.sap.sgs.phosphor.fosstars.data.github;

import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.SCANS_FOR_VULNERABLE_DEPENDENCIES;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeNotNull;

import com.sap.sgs.phosphor.fosstars.data.Terminal;
import com.sap.sgs.phosphor.fosstars.model.Value;
import com.sap.sgs.phosphor.fosstars.model.value.ValueHashSet;
import com.sap.sgs.phosphor.fosstars.tool.github.GitHubProject;
import java.io.IOException;
import java.util.Optional;
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
    GitHubProject project = new GitHubProject("apache", "cxf");
    UsesOwaspDependencyCheck provider = new UsesOwaspDependencyCheck(github);
    ValueHashSet values = new ValueHashSet();
    provider.set(new Terminal()).update(project, values);
    assertTrue(values.has(SCANS_FOR_VULNERABLE_DEPENDENCIES));
    Optional<Value> something = values.of(SCANS_FOR_VULNERABLE_DEPENDENCIES);
    assertNotNull(something);
    assertTrue(something.isPresent());
    Value<Boolean> value = something.get();
    assertNotNull(value);
    assertEquals(Boolean.TRUE, value.get());
  }

  @Test
  public void testApachePoi() throws IOException {
    assumeNotNull(github);
    GitHubProject project = new GitHubProject("apache", "poi");
    UsesOwaspDependencyCheck provider = new UsesOwaspDependencyCheck(github);
    ValueHashSet values = new ValueHashSet();
    provider.set(new Terminal()).update(project, values);
    assertTrue(values.has(SCANS_FOR_VULNERABLE_DEPENDENCIES));
    Optional<Value> something = values.of(SCANS_FOR_VULNERABLE_DEPENDENCIES);
    assertNotNull(something);
    assertTrue(something.isPresent());
    Value<Boolean> value = something.get();
    assertNotNull(value);
    assertEquals(Boolean.FALSE, value.get());
  }

  @Test
  public void testSpringSecurityOAuth() throws IOException {
    assumeNotNull(github);
    GitHubProject project = new GitHubProject("spring-projects", "spring-security-oauth");
    UsesOwaspDependencyCheck provider = new UsesOwaspDependencyCheck(github);
    ValueHashSet values = new ValueHashSet();
    provider.set(new Terminal()).update(project, values);
    assertTrue(values.has(SCANS_FOR_VULNERABLE_DEPENDENCIES));
    Optional<Value> something = values.of(SCANS_FOR_VULNERABLE_DEPENDENCIES);
    assertNotNull(something);
    assertTrue(something.isPresent());
    Value<Boolean> value = something.get();
    assertNotNull(value);
    assertEquals(Boolean.FALSE, value.get());
  }

}