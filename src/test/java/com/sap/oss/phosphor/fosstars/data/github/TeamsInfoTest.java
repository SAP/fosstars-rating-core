package com.sap.oss.phosphor.fosstars.data.github;

import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.HAS_ADMIN_TEAM_ON_GITHUB;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.HAS_ENOUGH_ADMINS_ON_GITHUB;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.HAS_ENOUGH_TEAMS_ON_GITHUB;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.HAS_ENOUGH_TEAM_MEMBERS_ON_GITHUB;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.HAS_TEAM_WITH_PUSH_PRIVILEGES_ON_GITHUB;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.sap.oss.phosphor.fosstars.model.Feature;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.ValueSet;
import com.sap.oss.phosphor.fosstars.model.subject.oss.GitHubProject;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import org.junit.Test;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GHTeam;
import org.kohsuke.github.GHUser;

public class TeamsInfoTest extends TestGitHubDataFetcherHolder {

  @Test
  public void testSupportedFeatures() {
    TeamsInfo provider = new TeamsInfo(fetcher);
    assertTrue(provider.supportedFeatures().contains(HAS_ENOUGH_TEAMS_ON_GITHUB));
    assertTrue(provider.supportedFeatures().contains(HAS_ADMIN_TEAM_ON_GITHUB));
    assertTrue(provider.supportedFeatures().contains(HAS_ENOUGH_ADMINS_ON_GITHUB));
    assertTrue(provider.supportedFeatures().contains(HAS_TEAM_WITH_PUSH_PRIVILEGES_ON_GITHUB));
    assertTrue(provider.supportedFeatures().contains(HAS_ENOUGH_TEAM_MEMBERS_ON_GITHUB));
  }

  @Test
  public void testWithPerfectProject() throws IOException {
    Set<GHUser> admins = new HashSet<>();
    admins.add(mock(GHUser.class));
    admins.add(mock(GHUser.class));

    Set<GHUser> maintainers = new HashSet<>();
    maintainers.add(mock(GHUser.class));
    maintainers.add(mock(GHUser.class));

    GHTeam adminTeam = mock(GHTeam.class);
    when(adminTeam.getMembers()).thenReturn(admins);
    when(adminTeam.getPermission()).thenReturn("admin");

    GHTeam otherTeam = mock(GHTeam.class);
    when(otherTeam.getMembers()).thenReturn(maintainers);
    when(otherTeam.getPermission()).thenReturn("push");

    Set<GHTeam> teams = new HashSet<>();
    teams.add(adminTeam);
    teams.add(otherTeam);

    GHRepository repository = mock(GHRepository.class);
    when(repository.getTeams()).thenReturn(teams);
    GitHubProject project = new GitHubProject("test", "project");
    fetcher.addForTesting(project, repository);

    TeamsInfo provider = new TeamsInfo(fetcher);
    ValueSet values = provider.fetchValuesFor(project);
    checkValue(values, HAS_ENOUGH_TEAMS_ON_GITHUB, true);
    checkValue(values, HAS_ADMIN_TEAM_ON_GITHUB, true);
    checkValue(values, HAS_ENOUGH_ADMINS_ON_GITHUB, true);
    checkValue(values, HAS_TEAM_WITH_PUSH_PRIVILEGES_ON_GITHUB, true);
    checkValue(values, HAS_ENOUGH_TEAM_MEMBERS_ON_GITHUB, true);
  }

  @Test
  public void testWithHigherRequirements() throws IOException {
    Set<GHUser> admins = new HashSet<>();
    admins.add(mock(GHUser.class));
    admins.add(mock(GHUser.class));

    Set<GHUser> maintainers = new HashSet<>();
    maintainers.add(mock(GHUser.class));
    maintainers.add(mock(GHUser.class));

    GHTeam adminTeam = mock(GHTeam.class);
    when(adminTeam.getMembers()).thenReturn(admins);
    when(adminTeam.getPermission()).thenReturn("admin");

    GHTeam otherTeam = mock(GHTeam.class);
    when(otherTeam.getMembers()).thenReturn(maintainers);
    when(otherTeam.getPermission()).thenReturn("push");

    Set<GHTeam> teams = new HashSet<>();
    teams.add(adminTeam);
    teams.add(otherTeam);

    GHRepository repository = mock(GHRepository.class);
    when(repository.getTeams()).thenReturn(teams);
    GitHubProject project = new GitHubProject("test", "project");
    fetcher.addForTesting(project, repository);

    TeamsInfo provider = new TeamsInfo(fetcher);
    provider.minTeams(3);
    provider.minAdmins(3);
    provider.minMembers(3);

    ValueSet values = provider.fetchValuesFor(project);
    checkValue(values, HAS_ENOUGH_TEAMS_ON_GITHUB, false);
    checkValue(values, HAS_ADMIN_TEAM_ON_GITHUB, true);
    checkValue(values, HAS_ENOUGH_ADMINS_ON_GITHUB, false);
    checkValue(values, HAS_TEAM_WITH_PUSH_PRIVILEGES_ON_GITHUB, true);
    checkValue(values, HAS_ENOUGH_TEAM_MEMBERS_ON_GITHUB, false);
  }

  @Test
  public void testWithBadProject() throws IOException {
    Set<GHUser> maintainers = new HashSet<>();
    maintainers.add(mock(GHUser.class));
    maintainers.add(mock(GHUser.class));

    GHTeam otherTeam = mock(GHTeam.class);
    when(otherTeam.getMembers()).thenReturn(maintainers);
    when(otherTeam.getPermission()).thenReturn("read");

    GHRepository repository = mock(GHRepository.class);
    when(repository.getTeams()).thenReturn(Collections.singleton(otherTeam));

    GitHubProject project = new GitHubProject("test", "project");
    fetcher.addForTesting(project, repository);

    TeamsInfo provider = new TeamsInfo(fetcher);

    when(otherTeam.getPermission()).thenReturn("read");
    ValueSet values = provider.fetchValuesFor(project);
    checkValue(values, HAS_ENOUGH_TEAMS_ON_GITHUB, false);
    checkValue(values, HAS_ADMIN_TEAM_ON_GITHUB, false);
    checkValue(values, HAS_ENOUGH_ADMINS_ON_GITHUB, false);
    checkValue(values, HAS_TEAM_WITH_PUSH_PRIVILEGES_ON_GITHUB, false);
    checkValue(values, HAS_ENOUGH_TEAM_MEMBERS_ON_GITHUB, false);
  }

  private static void checkValue(ValueSet values, Feature<Boolean> feature, boolean expected) {
    Optional<Value<Boolean>> something = values.of(feature);
    assertTrue(something.isPresent());
    Value<Boolean> value = something.get();
    assertFalse(value.isUnknown());
    assertFalse(value.isNotApplicable());
    assertEquals(expected, value.get());
  }
}