package com.sap.oss.phosphor.fosstars.model.subject.oss;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.sap.oss.phosphor.fosstars.model.Subject;
import java.util.Optional;

/**
 * An artifact that is produced by an open source project. For example, it may be a jar file that
 * can be downloaded from a Maven repository.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
public interface Artifact extends Subject {

  /**
   * Returns the GitHub Project of the artifact.
   *
   * @return A {@link GitHubProject}.
   */
  Optional<GitHubProject> project();
}