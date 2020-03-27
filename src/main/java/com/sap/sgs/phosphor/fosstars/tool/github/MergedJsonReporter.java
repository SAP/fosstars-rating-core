package com.sap.sgs.phosphor.fosstars.tool.github;

import com.fasterxml.jackson.databind.ObjectMapper;
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
   * Serializer and deserializer.
   */
  private static final ObjectMapper MAPPER = new ObjectMapper();

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

    System.out.printf("[+] Storing info about projects to %s%n", filename);
    allProjects.sort(Comparator.comparing(project -> project.url().toString()));
    Files.write(
        Paths.get(filename),
        MAPPER.writerWithDefaultPrettyPrinter().writeValueAsBytes(allProjects));
  }
}
