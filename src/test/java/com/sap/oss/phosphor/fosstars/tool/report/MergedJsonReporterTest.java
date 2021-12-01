package com.sap.oss.phosphor.fosstars.tool.report;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.core.type.TypeReference;
import com.sap.oss.phosphor.fosstars.model.subject.oss.GitHubProject;
import com.sap.oss.phosphor.fosstars.util.Json;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

public class MergedJsonReporterTest {

  /**
   * A type reference of a list of {@link GitHubProject}s for deserialization.
   */
  static final TypeReference<List<GitHubProject>> LIST_OF_GITHUB_PROJECTS_TYPE
      = new TypeReference<List<GitHubProject>>() {};

  @Test
  public void testLoadAndSave() throws IOException {
    Path path = Files.createTempFile(MergedJsonReporterTest.class.getName(), "test");
    try {
      Files.write(path, "[]".getBytes());

      List<GitHubProject> projects = asList(
          new GitHubProject("org", "test"),
          new GitHubProject("org", "project"));

      MergedJsonReporter reporter = new MergedJsonReporter(path.toString());
      reporter.runFor(projects);

      List<GitHubProject> clone
          = Json.mapper().readValue(path.toFile(), LIST_OF_GITHUB_PROJECTS_TYPE);

      assertEquals(projects.size(), clone.size());
      for (GitHubProject project : projects) {
        assertTrue(clone.contains(project));
      }
    } finally {
      FileUtils.forceDeleteOnExit(path.toFile());
    }
  }
}