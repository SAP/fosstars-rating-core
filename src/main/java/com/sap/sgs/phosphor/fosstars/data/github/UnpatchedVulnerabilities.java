package com.sap.sgs.phosphor.fosstars.data.github;

import static com.sap.sgs.phosphor.fosstars.model.value.Vulnerability.NO_DESCRIPTION;
import static com.sap.sgs.phosphor.fosstars.model.value.Vulnerability.NO_REFERENCES;
import static com.sap.sgs.phosphor.fosstars.model.value.Vulnerability.UNKNOWN_FIXED_DATE;
import static com.sap.sgs.phosphor.fosstars.model.value.Vulnerability.UNKNOWN_INTRODUCED_DATE;
import static com.sap.sgs.phosphor.fosstars.tool.YesNoQuestion.Answer.YES;

import com.sap.sgs.phosphor.fosstars.data.UserCallback;
import com.sap.sgs.phosphor.fosstars.data.json.UnpatchedVulnerabilitiesStorage;
import com.sap.sgs.phosphor.fosstars.model.Value;
import com.sap.sgs.phosphor.fosstars.model.value.CVSS;
import com.sap.sgs.phosphor.fosstars.model.value.Vulnerabilities;
import com.sap.sgs.phosphor.fosstars.model.value.Vulnerability;
import com.sap.sgs.phosphor.fosstars.model.value.Vulnerability.Resolution;
import com.sap.sgs.phosphor.fosstars.tool.InputURL;
import com.sap.sgs.phosphor.fosstars.tool.YesNoQuestion;
import com.sap.sgs.phosphor.fosstars.tool.YesNoQuestion.Answer;
import java.io.IOException;
import java.net.MalformedURLException;
import org.kohsuke.github.GitHub;

/**
 * This data provider tries to figure out if an open-source project has any unpatched
 * vulnerabilities.
 */
public class UnpatchedVulnerabilities extends AbstractGitHubDataProvider {

  private static final String PATH =
      "src/main/resources/com/sap/sgs/phosphor/fosstars/data/UnpatchedVulnerabilities.json";

  /**
   * A storage of unpatched vulnerabilities.
   */
  private final UnpatchedVulnerabilitiesStorage storage;

  /**
   * A list of vulnerabilities to be updated by this data provider.
   */
  private final Value<Vulnerabilities> vulnerabilities;

  /**
   * @param where A GitHub organization of user name.
   * @param name A name of a repository.
   * @param github An interface to the GitHub API.
   * @param mayTalk A flag which shows if the provider can communicate with a user or not.
   * @param vulnerabilities A list of vulnerabilities to be updated by this data provider.
   * @throws IOException If the info about unpatched vulnerabilities can't be loaded.
   */
  public UnpatchedVulnerabilities(String where, String name, GitHub github, boolean mayTalk,
      Value<Vulnerabilities> vulnerabilities) throws IOException {
    super(where, name, github, mayTalk);
    storage = UnpatchedVulnerabilitiesStorage.load();
    this.vulnerabilities = vulnerabilities;
  }

  @Override
  public Value<Vulnerabilities> get(UserCallback callback) throws MalformedURLException {
    System.out.println("[+] Figuring out if the project has any unpatched vulnerability ...");

    Vulnerabilities unpatchedVulnerabilities = storage.get(url);

    if (mayTalk()) {
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
          storage.add(url, vulnerability);
          answer = new YesNoQuestion(callback, "One more?").ask();
        } while (answer == YES);

        try {
          storage.store(PATH);
        } catch (IOException e) {
          System.out.printf("[!] Could not store vulnerabilities to a file: %s%n", e);
        }
      }
    }

    vulnerabilities.get().add(unpatchedVulnerabilities);

    return vulnerabilities;
  }
}
