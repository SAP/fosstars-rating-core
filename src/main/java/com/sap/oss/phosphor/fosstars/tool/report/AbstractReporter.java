package com.sap.oss.phosphor.fosstars.tool.report;

import com.fasterxml.jackson.core.type.TypeReference;
import com.sap.oss.phosphor.fosstars.model.subject.oss.GitHubProject;
import com.sap.oss.phosphor.fosstars.model.subject.oss.OpenSourceProject;
import com.sap.oss.phosphor.fosstars.util.Json;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * A base class for reporters.
 *
 * @param <T> A type of projects.
 */
abstract class AbstractReporter<T extends OpenSourceProject> implements Reporter<T> {

  /**
   * A type reference of a list of {@link GitHubProject}s for deserialization.
   */
  static final TypeReference<List<GitHubProject>> LIST_OF_GITHUB_PROJECTS_TYPE
      = new TypeReference<List<GitHubProject>>() {};

  /**
   * A logger.
   */
  final Logger logger = LogManager.getLogger(getClass());

  /**
   * Merges two lists of projects. The merge is based on URLs.
   * If there are two projects with the same URLs,
   * then the method uses the project from the first list.
   *
   * @param projects The first list of projects.
   * @param extraProjects The second list of projects.
   * @return A list of projects which contains all projects from the original lists.
   */
  static List<GitHubProject> merge(
      List<GitHubProject> projects, List<GitHubProject> extraProjects) {

    Map<URL, GitHubProject> map = new HashMap<>();
    for (GitHubProject extraProject : extraProjects) {
      map.put(extraProject.scm(), extraProject);
    }
    for (GitHubProject project : projects) {
      map.put(project.scm(), project);
    }

    List<GitHubProject> allProjects = new ArrayList<>();
    for (Map.Entry<URL, GitHubProject> entry : map.entrySet()) {
      allProjects.add(entry.getValue());
    }

    return allProjects;
  }

  /**
   * Loads projects from a JSON file.
   * If the file doesn't exist, then the method returns an empty list.
   *
   * @param extraSourceFileName A path to the file.
   * @return A list of loaded extra projects.
   * @throws IOException If the projects couldn't be loaded.
   */
  List<GitHubProject> loadProjects(String extraSourceFileName) throws IOException {
    if (extraSourceFileName == null) {
      return Collections.emptyList();
    }
    return loadProjects(Paths.get(extraSourceFileName));
  }

  /**
   * Loads projects from a JSON file.
   * If the file doesn't exist, then the method returns an empty list.
   *
   * @param extraSourceFileName A path to the file.
   * @return A list of loaded extra projects.
   * @throws IOException If the projects couldn't be loaded.
   */
  List<GitHubProject> loadProjects(Path extraSourceFileName) throws IOException {
    if (extraSourceFileName == null) {
      return Collections.emptyList();
    }
    if (!Files.exists(extraSourceFileName)) {
      logger.warn("Oh no! I could not load extra projects from {}", extraSourceFileName);
      return Collections.emptyList();
    }
    try (InputStream is = Files.newInputStream(extraSourceFileName)) {
      return Json.mapper().readValue(is, LIST_OF_GITHUB_PROJECTS_TYPE);
    }
  }

  /**
   * Loads a resource.
   *
   * @param name A name of the resource.
   * @return The content of the resource.
   */
  static String loadFrom(String name, Class<?> clazz) {
    try (InputStream is = clazz.getResourceAsStream(name)) {
      return IOUtils.toString(is, StandardCharsets.UTF_8);
    } catch (IOException e) {
      throw new UncheckedIOException("Holy moly! Could not load template!", e);
    }
  }

}
