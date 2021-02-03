package com.sap.oss.phosphor.fosstars.tool.github;

import com.sap.oss.phosphor.fosstars.model.subject.oss.GitHubProject;
import com.sap.oss.phosphor.fosstars.util.Json;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * This reporter takes a number of projects and merge them in to a JSON file.
 */
public class MergedJsonReporter extends AbstractReporter<GitHubProject> {

  /**
   * A path to an output file.
   */
  private final String filename;

  /**
   * Initializes a new reporter.
   *
   * @param filename A path to an output file.
   */
  MergedJsonReporter(String filename) {
    Objects.requireNonNull(filename, "Oh no! Output filename is null!");
    if (filename.trim().isEmpty()) {
      throw new IllegalArgumentException("Oh no! Output filename is empty!");
    }
    this.filename = filename;
  }

  @Override
  public void runFor(List<GitHubProject> projects) throws IOException {
    List<GitHubProject> existingProjects = loadProjects(filename);
    List<GitHubProject> allProjects = merge(projects, existingProjects);

    logger.info("Storing info about projects to {}", filename);
    allProjects.sort(Comparator.comparing(project -> project.scm().toString()));
    Files.write(Paths.get(filename), Json.toBytes(allProjects));
  }
}
