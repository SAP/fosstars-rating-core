package com.sap.oss.phosphor.fosstars.data.github;

import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.HAS_SECURITY_TEAM;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.sap.oss.phosphor.fosstars.data.SubjectValueCache;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.subject.oss.GitHubProject;
import com.sap.oss.phosphor.fosstars.model.value.ValueHashSet;
import java.io.IOException;
import java.util.Optional;
import org.junit.jupiter.api.Test;

public class HasSecurityTeamTest extends TestGitHubDataFetcherHolder {

  private static Value<Boolean> check(HasSecurityTeam provider, GitHubProject project)
      throws IOException {
    ValueHashSet values = new ValueHashSet();
    provider.update(project, values);

    assertEquals(1, values.size());
    assertTrue(values.has(HAS_SECURITY_TEAM));

    Optional<Value<Boolean>> something = values.of(HAS_SECURITY_TEAM);
    assertNotNull(something);
    assertTrue(something.isPresent());

    return something.get();
  }

  @Test
  public void testHasTeam() throws IOException {
    GitHubProject project = new GitHubProject("apache", "poi");
    HasSecurityTeam provider = new HasSecurityTeam(fetcher);
    provider.set(new SubjectValueCache());
    Value<Boolean> value = check(provider, project);
    assertNotNull(value);
    assertTrue(value.get());
  }

  @Test
  public void testNoTeam() throws IOException {
    GitHubProject project = new GitHubProject("FasterXML", "jackson-databind");
    HasSecurityTeam provider = new HasSecurityTeam(fetcher);
    provider.set(new SubjectValueCache());
    Value<Boolean> value = check(provider, project);
    assertNotNull(value);
    assertFalse(value.get());
  }
}
