package com.sap.oss.phosphor.fosstars.tool.report;

import com.sap.oss.phosphor.fosstars.model.subject.oss.MavenArtifact;
import com.sap.oss.phosphor.fosstars.util.Json;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Comparator;
import java.util.List;

/**
 * This reporter takes a number of artifacts and merge them in to a JSON file.
 */
public class MergedArtifactJsonReporter extends AbstractJsonReporter<MavenArtifact> {

  /**
   * Initializes a new reporter.
   *
   * @param filename A path to an output file.
   */
  public MergedArtifactJsonReporter(String filename) {
    super(filename);
  }

  @Override
  public void runFor(List<MavenArtifact> artifacts) throws IOException {
    List<MavenArtifact> existingArtifacts = loadProjects(filename);
    List<MavenArtifact> allArtifacts = mergeArtifacts(artifacts, existingArtifacts);
    logger.info("Storing info about artifacts to {}", filename);
    allArtifacts.sort(Comparator.comparing(artifact -> artifact.gav()));
    byte[] content = Json.mapper().writerFor(LIST_OF_SUBJECTS_REF_TYPE)
        .writeValueAsBytes(allArtifacts);
    Files.write(filename, content);
  }
}
