package com.sap.oss.phosphor.fosstars.data.github;

import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.HAS_ADMIN_TEAM_ON_GITHUB;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.HAS_ENOUGH_ADMINS_ON_GITHUB;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.HAS_ENOUGH_TEAMS_ON_GITHUB;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.HAS_ENOUGH_TEAM_MEMBERS_ON_GITHUB;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.HAS_TEAM_WITH_PUSH_PRIVILEGES_ON_GITHUB;
import static com.sap.oss.phosphor.fosstars.model.other.Utils.setOf;

import com.sap.oss.phosphor.fosstars.model.Feature;
import com.sap.oss.phosphor.fosstars.model.ValueSet;
import com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures;
import com.sap.oss.phosphor.fosstars.model.subject.oss.GitHubProject;
import com.sap.oss.phosphor.fosstars.model.value.ValueHashSet;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GHTeam;

/**
 * This data provider gathers info about project's teams on GitHub.
 * It fills out the following features:
 * <ul>
 *   <li>{@link OssFeatures#HAS_ENOUGH_TEAMS_ON_GITHUB}</li>
 *   <li>{@link OssFeatures#HAS_ADMIN_TEAM_ON_GITHUB}</li>
 *   <li>{@link OssFeatures#HAS_ENOUGH_ADMINS_ON_GITHUB}</li>
 *   <li>{@link OssFeatures#HAS_TEAM_WITH_PUSH_PRIVILEGES_ON_GITHUB}</li>
 *   <li>{@link OssFeatures#HAS_ENOUGH_TEAM_MEMBERS_ON_GITHUB}</li>
 * </ul>
 */
public class TeamsInfo extends GitHubCachingDataProvider {

  /**
   * A list of permissions that allows pushing to a repository on GitHub.
   */
  private static final List<String> PUSH_PRIVILEGES = Arrays.asList("admin", "push");

  /**
   * A minimal number of teams to fulfill {@link OssFeatures#HAS_ENOUGH_TEAMS_ON_GITHUB}.
   */
  private int minTeams = 2;

  /**
   * A minimal number of admins to fulfill {@link OssFeatures#HAS_ADMIN_TEAM_ON_GITHUB}.
   */
  private int minAdmins = 2;

  /**
   * A minimal number of members in a team
   * to fulfill {@link OssFeatures#HAS_ENOUGH_TEAM_MEMBERS_ON_GITHUB}.
   */
  private int minMembers = 2;

  /**
   * Initializes a data provider.
   *
   * @param fetcher An interface to GitHub.
   */
  public TeamsInfo(GitHubDataFetcher fetcher) {
    super(fetcher);
  }

  /**
   * Set a minimal number of teams to fulfill {@link OssFeatures#HAS_ENOUGH_TEAMS_ON_GITHUB}.
   *
   * @param n The number.
   * @return This data provider.
   * @throws IllegalArgumentException If the number of teams is wrong.
   */
  public TeamsInfo minTeams(int n) {
    if (n < 0) {
      throw new IllegalArgumentException("Oops! The number of teams can't be negative!");
    }
    this.minTeams = n;
    return this;
  }

  /**
   * Set a minimal number of admins to fulfill {@link OssFeatures#HAS_ADMIN_TEAM_ON_GITHUB}.
   *
   * @param n The number.
   * @return This data provider.
   * @throws IllegalArgumentException If the number of admins is wrong.
   */
  public TeamsInfo minAdmins(int n) {
    if (n < 0) {
      throw new IllegalArgumentException("Oops! The number of admins can't be negative!");
    }
    this.minAdmins = n;
    return this;
  }

  /**
   * Set a minimal number of members in a team
   * to fulfill {@link OssFeatures#HAS_ENOUGH_TEAM_MEMBERS_ON_GITHUB}.
   *
   * @param n The number.
   * @return This data provider.
   * @throws IllegalArgumentException If the number of members is wrong.
   */
  public TeamsInfo minMembers(int n) {
    if (n < 0) {
      throw new IllegalArgumentException("Oops! The number of members can't be negative!");
    }
    this.minMembers = n;
    return this;
  }

  @Override
  public Set<Feature<?>> supportedFeatures() {
    return setOf(
        HAS_ENOUGH_TEAMS_ON_GITHUB,
        HAS_ADMIN_TEAM_ON_GITHUB,
        HAS_ENOUGH_ADMINS_ON_GITHUB,
        HAS_TEAM_WITH_PUSH_PRIVILEGES_ON_GITHUB,
        HAS_ENOUGH_TEAM_MEMBERS_ON_GITHUB);
  }

  @Override
  protected ValueSet fetchValuesFor(GitHubProject project) throws IOException {
    logger.info("Fetching info about project's teams ...");

    GHRepository repository = fetcher.repositoryFor(project);
    ValueSet values = new ValueHashSet();

    values.update(HAS_ENOUGH_TEAMS_ON_GITHUB.value(repository.getTeams().size() >= minTeams)
        .explainIf(false, "The project should have at least %d teams", minTeams));
    values.update(HAS_ADMIN_TEAM_ON_GITHUB.value(false)
        .explain("The project does not have an admin team"));
    values.update(HAS_ENOUGH_ADMINS_ON_GITHUB.value(false)
        .explain("The project should have at least %n admins", minAdmins));
    values.update(HAS_TEAM_WITH_PUSH_PRIVILEGES_ON_GITHUB.value(false)
        .explain("The project does not have a team with push privileges"));
    values.update(HAS_ENOUGH_TEAM_MEMBERS_ON_GITHUB.value(false)
        .explain("The project should have at least %d team members", minMembers));

    for (GHTeam team : repository.getTeams()) {
      if ("admin".equals(team.getPermission())) {
        values.update(HAS_ADMIN_TEAM_ON_GITHUB.value(true));
        if (team.getMembers().size() >= minAdmins) {
          values.update(HAS_ENOUGH_ADMINS_ON_GITHUB.value(true));
        }
      }
      if (PUSH_PRIVILEGES.contains(team.getPermission())) {
        values.update(HAS_TEAM_WITH_PUSH_PRIVILEGES_ON_GITHUB.value(true));
        if (team.getMembers().size() >= minMembers) {
          values.update(HAS_ENOUGH_TEAM_MEMBERS_ON_GITHUB.value(true));
        }
      }
    }

    return values;
  }

}
