package com.sap.sgs.phosphor.fosstars.tool.github;

import static org.junit.Assume.assumeTrue;

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
public class SecurityRatingCalculatorTest {

  private static boolean githubAvailable = false;

  @BeforeClass
  public static void initGithubConnection() {
    try {
      GitHub github = GitHub.connectAnonymously();
      github.checkApiUrlValidity();
      githubAvailable = true;
    } catch (Exception e) {
      System.out.println("Couldn't connect to GitHub!");
      e.printStackTrace();
    }
  }

  @Test
  public void smoke() throws IOException {
    SecurityRatingCalculator.main();
  }

  @Test
  public void help() throws Exception {
    SecurityRatingCalculator.main("-help");
    SecurityRatingCalculator.main("-h");
  }

  @Test
  public void apacheCommonsHttpClient() throws IOException {
    assumeTrue(githubAvailable);
    SecurityRatingCalculator.main(
        "--no-questions",
        "--url", "https://github.com/apache/httpcomponents-client");
  }

  @Test
  public void badToken() throws IOException {
    assumeTrue(githubAvailable);
    SecurityRatingCalculator.main(
        "--no-questions",
        "--url", "https://github.com/apache/httpcomponents-client",
        "--token", "xxx");
  }
}