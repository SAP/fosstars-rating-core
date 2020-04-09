package com.sap.sgs.phosphor.fosstars.data.github;

import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.VULNERABILITIES;

import com.sap.sgs.phosphor.fosstars.data.json.UnpatchedVulnerabilitiesStorage;
import com.sap.sgs.phosphor.fosstars.model.Value;
import com.sap.sgs.phosphor.fosstars.model.ValueSet;
import com.sap.sgs.phosphor.fosstars.model.value.Vulnerabilities;
import com.sap.sgs.phosphor.fosstars.tool.github.GitHubProject;
import java.io.IOException;
import org.kohsuke.github.GitHub;

/**
 * This data provider tries to figure out if an open-source project has any unpatched
 * vulnerabilities.
 * TODO: The data provider should use the cache.
 */
public class UnpatchedVulnerabilities extends AbstractGitHubDataProvider {

  /**
   * A storage of unpatched vulnerabilities.
   */
  private final UnpatchedVulnerabilitiesStorage storage;

  /**
   * Initializes a data provider.
   *
   * @param github An interface to the GitHub API.
   * @throws IOException If the info about unpatched vulnerabilities can't be loaded.
   */
  public UnpatchedVulnerabilities(GitHub github) throws IOException {
    super(github);
    storage = UnpatchedVulnerabilitiesStorage.load();
  }

  @Override
  protected UnpatchedVulnerabilities doUpdate(GitHubProject project, ValueSet values) {
    logger.info("Figuring out if the project has any unpatched vulnerability ...");

    Vulnerabilities unpatchedVulnerabilities = storage.get(project.url());
    Value<Vulnerabilities> vulnerabilities = knownVulnerabilities(values);
    vulnerabilities.get().add(unpatchedVulnerabilities);
    values.update(vulnerabilities);

    return this;
  }

  /**
   * Searches for
   * {@link com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures#VULNERABILITIES}
   * feature a set of values.
   *
   * @param values The set of value.
   * @return An existing value for the feature, or an empty value otherwise.
   */
  private static Value<Vulnerabilities> knownVulnerabilities(ValueSet values) {
    return values.of(VULNERABILITIES).orElseGet(() -> VULNERABILITIES.value(new Vulnerabilities()));
  }
}
