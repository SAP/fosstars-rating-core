package com.sap.oss.phosphor.fosstars.data.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sap.oss.phosphor.fosstars.model.subject.oss.GitHubProject;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * This class maintains information about bug bounty programs for open-source projects.
 */
public class BugBountyProgramStorage extends AbstractJsonStorage {

  /**
   * Path to a resource that contains information about bug bounty programs.
   */
  private static final String RESOURCE_PATH =
      "com/sap/oss/phosphor/fosstars/data/BugBountyPrograms.json";

  /**
   * Maps a URL that describes a bug bounty program to a list of URLs of SCM of open-source projects
   * that are included to the program.
   */
  private final Map<URL, List<URL>> bugBountyPrograms;

  /**
   * Initializes a new storage.
   *
   * @param bugBountyPrograms Info about bug bounty programs.
   */
  private BugBountyProgramStorage(
      @JsonProperty("bugBountyPrograms") Map<URL, List<URL>> bugBountyPrograms) {

    Objects.requireNonNull(bugBountyPrograms, "Oh no! Bug bounty programs are null!");
    this.bugBountyPrograms = bugBountyPrograms;
  }

  /**
   * Checks if a project has a bug bounty program.
   *
   * @param project The project to be checked.
   * @return True if the projects has a bug bounty program, false otherwise.
   */
  public boolean existsFor(GitHubProject project) {
    return existsFor(project.scm());
  }

  /**
   * Checks if a project with a specified URL of project's SCM has a bug bounty program.
   *
   * @param url The URL to project's SCM.
   * @return True if the project has a bug bounty program, false otherwise.
   */
  public boolean existsFor(URL url) {
    for (Map.Entry<URL, List<URL>> entry : bugBountyPrograms.entrySet()) {
      if (entry.getValue().contains(url)) {
        return true;
      }
    }

    return false;
  }

  /**
   * Loads a {@link BugBountyProgramStorage}.
   *
   * @return The loaded storage.
   * @throws IOException If something went wrong.
   */
  public static BugBountyProgramStorage load() throws IOException {
    return load(RESOURCE_PATH, BugBountyProgramStorage.class);
  }
}
