package com.sap.oss.phosphor.fosstars.model.subject.oss;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.sap.oss.phosphor.fosstars.util.Json;
import java.io.IOException;
import org.junit.Test;

public class GitHubOrganizationTest {

  @Test
  public void testEqualsAndHashCode() {
    GitHubOrganization firstOrg = new GitHubOrganization("first");
    GitHubOrganization theSameOrg = new GitHubOrganization("first");
    assertTrue(firstOrg.equals(theSameOrg) && theSameOrg.equals(firstOrg));
    assertEquals(firstOrg.hashCode(), theSameOrg.hashCode());

    GitHubOrganization anotherOrg = new GitHubOrganization("another");
    assertNotEquals(anotherOrg, firstOrg);
  }

  @Test
  public void testSerialization() throws IOException {
    GitHubOrganization org = new GitHubOrganization("test");
    byte[] bytes = Json.toBytes(org);
    assertNotNull(bytes);
    assertTrue(bytes.length > 0);
    GitHubOrganization clone = Json.read(bytes, GitHubOrganization.class);
    assertNotNull(clone);
    assertEquals(org, clone);
    assertEquals(org.hashCode(), clone.hashCode());
  }

}