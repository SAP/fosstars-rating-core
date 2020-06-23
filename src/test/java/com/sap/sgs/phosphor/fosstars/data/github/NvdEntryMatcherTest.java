package com.sap.sgs.phosphor.fosstars.data.github;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.sap.sgs.phosphor.fosstars.nvd.data.ReferenceLink;
import com.sap.sgs.phosphor.fosstars.tool.github.GitHubProject;
import java.net.URI;
import java.net.URISyntaxException;
import org.junit.Test;

public class NvdEntryMatcherTest {

  @Test
  public void testCheckUrl() throws URISyntaxException {
    // If the URL is valid.
    String referenceUrl = "https://github.com/testOrg/testProject/commits/123456zcxvbnm";
    boolean isValid =
        NvdEntryMatcher.match(url(referenceUrl), new GitHubProject("testOrg", "testProject"));
    assertTrue(isValid);

    // If the input URL does not relate to the project .
    referenceUrl = "https://github.com/testOrganization/testProject/wiki/123456zcxvbnm";
    isValid = NvdEntryMatcher.match(url(referenceUrl), new GitHubProject("testOrg", "testProject"));
    assertFalse(isValid);

    // If the URL has a STOP_WORD.
    referenceUrl = "https://github.com/testOrg/testProject/wiki/123456zcxvbnm";
    isValid = NvdEntryMatcher.match(url(referenceUrl), new GitHubProject("testOrg", "testProject"));
    assertFalse(isValid);

    // If path split length is < 2.
    referenceUrl = "https://github.com/testOrg/testProject/";
    isValid = NvdEntryMatcher.match(url(referenceUrl), new GitHubProject("testOrg", "testProject"));
    assertFalse(isValid);

    // If path is leading with `/`.
    referenceUrl = "https://github.com///////////testOrg/testProject/commits/123456zcxvbnm";
    isValid = NvdEntryMatcher.match(url(referenceUrl), new GitHubProject("testOrg", "testProject"));
    assertTrue(isValid);
  }

  /**
   * Return {@link URI} of {@link ReferenceLink#url}.
   * 
   * @param url input URL.
   * @return type {@link URI}.
   * @throws URISyntaxException if something goes wrong.
   */
  public URI url(String url) throws URISyntaxException {
    return new URI(url);
  }
}