package com.sap.oss.phosphor.fosstars.data.interactive;

import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.VULNERABILITIES_IN_PROJECT;
import static com.sap.oss.phosphor.fosstars.model.value.Vulnerability.Builder.newVulnerability;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.sap.oss.phosphor.fosstars.data.NoValueCache;
import com.sap.oss.phosphor.fosstars.data.UserCallback;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.ValueSet;
import com.sap.oss.phosphor.fosstars.model.subject.oss.GitHubProject;
import com.sap.oss.phosphor.fosstars.model.value.ValueHashSet;
import com.sap.oss.phosphor.fosstars.model.value.Vulnerabilities;
import org.junit.Test;

public class AskAboutUnpatchedVulnerabilitiesTest {

  @Test
  public void twoVulnerabilities() {
    final String firstIssueId = "https://github.com/org/test/issues/1";
    final String secondIssueId = "https://github.com/org/test/issues/2";

    testProvider(
        new Vulnerabilities(
            newVulnerability(firstIssueId).make(),
            newVulnerability(secondIssueId).make()),
        new AskAboutUnpatchedVulnerabilities(),
        new TestUserCallback("yes", firstIssueId, "yes", secondIssueId, "no"));
  }

  @Test
  public void noVulnerabilities() {
    testProvider(
        new Vulnerabilities(),
        new AskAboutUnpatchedVulnerabilities(),
        new TestUserCallback("no"));
  }

  private static void testProvider(Vulnerabilities expectedVulnerabilities,
      AskAboutUnpatchedVulnerabilities provider, UserCallback callback) {

    ValueSet values = new ValueHashSet();
    provider.set(NoValueCache.create());
    provider.set(callback);
    GitHubProject project = new GitHubProject("org", "test");
    provider.ask(project, values);
    assertEquals(1, values.size());
    assertTrue(values.has(VULNERABILITIES_IN_PROJECT));
    assertTrue(values.of(VULNERABILITIES_IN_PROJECT).isPresent());
    Value<Vulnerabilities> value = values.of(VULNERABILITIES_IN_PROJECT).get();
    assertEquals(expectedVulnerabilities, value.get());
  }
}