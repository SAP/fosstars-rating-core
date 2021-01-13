package com.sap.oss.phosphor.fosstars.data.github;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.sap.oss.phosphor.fosstars.model.subject.oss.GitHubProject;
import java.net.URI;
import java.net.URISyntaxException;
import org.junit.Test;

public class NvdEntryMatcherTest {

  @Test
  public void testCheckUrl() throws URISyntaxException {
    assertTrue(
        NvdEntryMatcher.match(
            new URI("https://github.com/testOrg/testProject/commits/123456zcxvbnm"),
            new GitHubProject("testOrg", "testProject")));

    assertFalse(
        NvdEntryMatcher.match(
            new URI("https://github.com/testOrganization/testProject/wiki/123456zcxvbnm"),
            new GitHubProject("testOrg", "testProject")));

    assertFalse(
        NvdEntryMatcher.match(
            new URI("https://github.com/testOrg/testProject/wiki/123456zcxvbnm"),
            new GitHubProject("testOrg", "testProject")));

    assertFalse(
        NvdEntryMatcher.match(
            new URI("https://github.com/testOrg/testProject/"),
            new GitHubProject("testOrg", "testProject")));

    assertTrue(
        NvdEntryMatcher.match(
            new URI("https://github.com///////////testOrg/testProject/commits/123456zcxvbnm"),
            new GitHubProject("testOrg", "testProject")));
  }
}