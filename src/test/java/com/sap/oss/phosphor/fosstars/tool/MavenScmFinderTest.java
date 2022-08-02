package com.sap.oss.phosphor.fosstars.tool;

import static com.sap.oss.phosphor.fosstars.tool.MavenScmFinder.normalizeGitHubProjectPath;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.Test;

public class MavenScmFinderTest {

  @Test
  public void testGitHubUrlParser() throws IOException {
    List<String> inputSyntaxes = Arrays.asList(
        "git://github.com/path/to/repo.git/",
        "ssh://user@github.com/path/to/repo.git/",
        "https://github.com/path/to/repo.git/",
        "ftps://github.com/path/to/repo.git/",
        "https://github.com/path/to/repo.git",
        "git@github.com:/path/to/repo.git",
        "git@github.com:path/to/repo.git");

    for (String url : inputSyntaxes) {
      Optional<String> parsedUrl = normalizeGitHubProjectPath(url);
      assertTrue(parsedUrl.isPresent());
      assertEquals("https://github.com/path/to/repo.git", parsedUrl.get());
    }
  }

  @Test
  public void testNonGitHubUrls() throws IOException {
    List<String> inputSyntaxes =
        Arrays.asList(
            "git://xyz.com/path/to/repo.git/",
            "ssh://user@xyz.com/path/to/repo.git/");

    for (String url : inputSyntaxes) {
      Optional<String> parsedUrl = normalizeGitHubProjectPath(url);
      assertTrue(!parsedUrl.isPresent());
    }
  }

  @Test(expected = IOException.class)
  public void testNonParseableUrls() throws IOException {
    List<String> inputSyntaxes = Arrays.asList(
        "github.com/path/to/repo.git/",
        "git@github.com:.git",
        "git@github.com:/repo.git");

    for (String url : inputSyntaxes) {
      normalizeGitHubProjectPath(url);
    }
  }
  
  @Test
  public void testGitHubUrlParserWithHyphen() throws IOException {
    String inputSyntax = "git@github.com:codelibs/elasticsearch-module.git";
    Optional<String> parsedUrl = normalizeGitHubProjectPath(inputSyntax);
    assertTrue(parsedUrl.isPresent());
    assertEquals("https://github.com/codelibs/elasticsearch-module.git", parsedUrl.get());
  }
}
