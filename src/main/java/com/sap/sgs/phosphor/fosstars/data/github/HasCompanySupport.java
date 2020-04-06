package com.sap.sgs.phosphor.fosstars.data.github;

import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.SUPPORTED_BY_COMPANY;

import com.sap.sgs.phosphor.fosstars.data.json.CompanySupportStorage;
import com.sap.sgs.phosphor.fosstars.model.ValueSet;
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
   * Initializes a data provider.
   *
   * @param where A GitHub organization of user name.
   * @param name A name of a repository.
   * @param github An interface to the GitHub API.
   * @throws IOException If the info about open-source projects can't be loaded.
   */
  public HasCompanySupport(String where, String name, GitHub github) throws IOException {
    super(where, name, github);
    storage = CompanySupportStorage.load();
  }

  @Override
  public HasCompanySupport update(ValueSet values) {
    logger.info("Figuring out if the project is supported by a company ...");
    values.update(new BooleanValue(SUPPORTED_BY_COMPANY, storage.supported(url)));
    return this;
  }
}
