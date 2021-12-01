package com.sap.oss.phosphor.fosstars.tool.report;

import com.sap.oss.phosphor.fosstars.model.subject.oss.GitHubProject;
import com.sap.oss.phosphor.fosstars.util.Json;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Comparator;
import java.util.List;

/**
 * This reporter takes a number of projects and merge them in to a JSON file.
 */
public class MergedJsonReporter extends AbstractJsonReporter<GitHubProject> {

  /**
   * Initializes a new reporter.
   *
   * @param filename A path to an output file.
   */
  public MergedJsonReporter(String filename) {
    super(filename);
  }

  @Override
  public void runFor(List<GitHubProject> projects) throws IOException {
    List<GitHubProject> existingProjects = loadProjects(filename);
    List<GitHubProject> allProjects = merge(projects, existingProjects);
    logger.info("Storing info about projects to {}", filename);
    allProjects.sort(Comparator.comparing(project -> project.scm().toString()));
    byte[] content = Json.mapper().writerFor(LIST_OF_SUBJECTS_REF_TYPE)
        .writeValueAsBytes(allProjects);
    Files.write(filename, content);
  }
}
