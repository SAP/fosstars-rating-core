package com.sap.sgs.phosphor.fosstars.data.github;

import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.VULNERABILITIES;
import static com.sap.sgs.phosphor.fosstars.model.value.Vulnerability.NO_DESCRIPTION;
import static com.sap.sgs.phosphor.fosstars.model.value.Vulnerability.NO_REFERENCES;
import static com.sap.sgs.phosphor.fosstars.model.value.Vulnerability.UNKNOWN_FIXED_DATE;
import static com.sap.sgs.phosphor.fosstars.model.value.Vulnerability.UNKNOWN_INTRODUCED_DATE;
import static com.sap.sgs.phosphor.fosstars.tool.YesNoQuestion.Answer.YES;

import com.sap.sgs.phosphor.fosstars.data.json.UnpatchedVulnerabilitiesStorage;
import com.sap.sgs.phosphor.fosstars.model.Value;
import com.sap.sgs.phosphor.fosstars.model.ValueSet;
import com.sap.sgs.phosphor.fosstars.model.value.CVSS;
import com.sap.sgs.phosphor.fosstars.model.value.Vulnerabilities;
import com.sap.sgs.phosphor.fosstars.model.value.Vulnerability;
import com.sap.sgs.phosphor.fosstars.model.value.Vulnerability.Resolution;
import com.sap.sgs.phosphor.fosstars.tool.InputURL;
import com.sap.sgs.phosphor.fosstars.tool.YesNoQuestion;
import com.sap.sgs.phosphor.fosstars.tool.YesNoQuestion.Answer;
import com.sap.sgs.phosphor.fosstars.tool.github.GitHubProject;
import java.io.IOException;
import org.kohsuke.github.GitHub;

/**
 * This data provider tries to figure out if an open-source project has any unpatched
 * vulnerabilities.
 * TODO: This class doesn't talk to GitHub. Instead, it uses a local storage
 *       which contains info about known security teams.
 *       UnpatchedVulnerabilities may be converted to a data provider.
 * TODO: The data provider should use the cache.
 */
public class UnpatchedVulnerabilities extends AbstractGitHubDataProvider {

  private static final String PATH =
      "src/main/resources/com/sap/sgs/phosphor/fosstars/data/UnpatchedVulnerabilities.json";

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
  protected UnpatchedVulnerabilities doUpdate(GitHubProject project, ValueSet values)
      throws IOException {

    logger.info("Figuring out if the project has any unpatched vulnerability ...");

    Vulnerabilities unpatchedVulnerabilities = storage.get(project.url());

    if (callback.canTalk()) {
      Answer answer = new YesNoQuestion(
          callback, "Are you aware about any unpatched vulnerability in the project?").ask();

      if (answer == YES) {
        do {
          callback.say("[+] This is awesome! Or, no?..");
          callback.say("[?] Please give me a URL for the vulnerability");
          String id = new InputURL(callback).get().toString();
          Vulnerability vulnerability = new Vulnerability(
              id, NO_DESCRIPTION, CVSS.UNKNOWN, NO_REFERENCES, Resolution.UNPATCHED,
              UNKNOWN_INTRODUCED_DATE, UNKNOWN_FIXED_DATE);
          unpatchedVulnerabilities.add(vulnerability);
          storage.add(project.url().toString(), vulnerability);
          answer = new YesNoQuestion(callback, "One more?").ask();
        } while (answer == YES);

        try {
          storage.store(PATH);
        } catch (IOException e) {
          logger.warn("Could not store vulnerabilities to a file", e);
        }
      }
    }

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
