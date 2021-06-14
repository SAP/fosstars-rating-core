package com.sap.oss.phosphor.fosstars.tool.report;

import static com.sap.oss.phosphor.fosstars.tool.report.AbstractReporter.LIST_OF_GITHUB_PROJECTS_TYPE;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.sap.oss.phosphor.fosstars.model.subject.oss.GitHubProject;
import com.sap.oss.phosphor.fosstars.util.Json;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

public class MergedJsonReporterTest {

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