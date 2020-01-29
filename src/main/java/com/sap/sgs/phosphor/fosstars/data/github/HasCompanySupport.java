package com.sap.sgs.phosphor.fosstars.data.github;

import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.SUPPORTED_BY_COMPANY;

import com.sap.sgs.phosphor.fosstars.data.UserCallback;
import com.sap.sgs.phosphor.fosstars.data.json.CompanySupportStorage;
import com.sap.sgs.phosphor.fosstars.model.Value;
import com.sap.sgs.phosphor.fosstars.model.value.BooleanValue;
import java.io.IOException;
import org.kohsuke.github.GitHub;

/**
 * This data provider check if an open-source project is supported by a company.
 */
public class HasCompanySupport extends AbstractGitHubDataProvider {

  /**
   * Where the info about open-source projects is stored.
   */
  private final CompanySupportStorage storage;

  /**
   * @param where A GitHub organization of user name.
   * @param name A name of a repository.
   * @param github An interface to the GitHub API.
   * @param mayTalk A flag which shows if the provider can communicate with a user or not.
   * @throws IOException If the info about open-source projects can't be loaded.
   */
  public HasCompanySupport(String where, String name, GitHub github, boolean mayTalk) throws IOException {
    super(where, name, github, mayTalk);
    storage = CompanySupportStorage.load();
  }

  @Override
  public Value<Boolean> get(UserCallback callback) {
    System.out.println("[+] Figuring out if the project is supported by a company ...");
    return new BooleanValue(SUPPORTED_BY_COMPANY, storage.supported(url));
  }
}
