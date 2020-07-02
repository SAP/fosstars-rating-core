package com.sap.sgs.phosphor.fosstars.data.github;

import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.VULNERABILITIES;

import com.sap.sgs.phosphor.fosstars.data.DataProvider;
import com.sap.sgs.phosphor.fosstars.model.Feature;
import com.sap.sgs.phosphor.fosstars.model.Value;
import com.sap.sgs.phosphor.fosstars.model.ValueSet;
import com.sap.sgs.phosphor.fosstars.model.value.ValueHashSet;
import com.sap.sgs.phosphor.fosstars.model.value.Vulnerabilities;
import com.sap.sgs.phosphor.fosstars.nvd.NVD;
import com.sap.sgs.phosphor.fosstars.tool.github.GitHubProject;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * This data provider tries to fill out the
 * {@link com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures#VULNERABILITIES} feature.
 * It is based on the following data providers:
 * <ul>
 *   <li>{@link UnpatchedVulnerabilities}</li>
 *   <li>{@link VulnerabilitiesFromNvd}</li>
 * </ul>
 * The data provider cache a value for
 * the {@link com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures#VULNERABILITIES} feature.
 */
public class InfoAboutVulnerabilities extends CachedSingleFeatureGitHubDataProvider {

  /**
   * A list of underlying data providers.
   */
  private List<DataProvider<GitHubProject>> providers;

  /**
   * Initializes a data provider.
   *
   * @param fetcher An interface to GitHub.
   * @param nvd An interface to the NVD.
   * @throws IOException If something went wrong.
   */
  public InfoAboutVulnerabilities(GitHubDataFetcher fetcher, NVD nvd) throws IOException {
    super(fetcher);
    Objects.requireNonNull(nvd, "NVD can't be null!");
    providers = Arrays.asList(
        new UnpatchedVulnerabilities(fetcher, nvd),
        new VulnerabilitiesFromNvd(fetcher, nvd));
  }

  @Override
  protected Feature supportedFeature() {
    return VULNERABILITIES;
  }

  @Override
  protected Value fetchValueFor(GitHubProject project) throws IOException {
    logger.info("Looking for vulnerabilities in the project ...");

    Vulnerabilities allVulnerabilities = new Vulnerabilities();
    for (DataProvider<GitHubProject> provider : providers) {
      ValueSet subset = new ValueHashSet();
      provider.set(callback).set(cache).update(project, subset);

      for (Value value : subset.toArray()) {
        if (value.isUnknown()) {
          continue;
        }

        if (value.get() instanceof Vulnerabilities) {
          allVulnerabilities.add((Vulnerabilities) value.get());
        }
      }
    }

    return VULNERABILITIES.value(allVulnerabilities);
  }
}
