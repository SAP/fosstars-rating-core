package com.sap.oss.phosphor.fosstars.data.github;

import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.VULNERABILITIES_IN_PROJECT;

import com.sap.oss.phosphor.fosstars.data.DataProvider;
import com.sap.oss.phosphor.fosstars.model.Feature;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.ValueSet;
import com.sap.oss.phosphor.fosstars.model.subject.oss.GitHubProject;
import com.sap.oss.phosphor.fosstars.model.value.ValueHashSet;
import com.sap.oss.phosphor.fosstars.model.value.Vulnerabilities;
import com.sap.oss.phosphor.fosstars.nvd.NVD;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * This data provider tries to fill out the
 * {@link com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures#VULNERABILITIES_IN_PROJECT}
 * feature. It is based on the following data providers:
 * <ul>
 *   <li>{@link UnpatchedVulnerabilities}</li>
 *   <li>{@link VulnerabilitiesFromNvd}</li>
 * </ul>
 * The data provider cache a value for the
 * {@link com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures#VULNERABILITIES_IN_PROJECT}
 * feature.
 */
public class InfoAboutVulnerabilities
    extends CachedSingleFeatureGitHubDataProvider<Vulnerabilities> {

  /**
   * A list of underlying data providers.
   */
  private final List<DataProvider> providers;

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
        new UnpatchedVulnerabilities(fetcher),
        new VulnerabilitiesFromNvd(fetcher, nvd));
  }

  @Override
  protected Feature<Vulnerabilities> supportedFeature() {
    return VULNERABILITIES_IN_PROJECT;
  }

  @Override
  protected Value<Vulnerabilities> fetchValueFor(GitHubProject project) throws IOException {
    logger.info("Looking for vulnerabilities in the project ...");

    Vulnerabilities allVulnerabilities = new Vulnerabilities();
    for (DataProvider provider : providers) {
      ValueSet subset = new ValueHashSet();
      provider.set(callback).set(cache).update(project, subset);

      for (Value<?> value : subset) {
        if (value.isUnknown()) {
          continue;
        }

        if (value.get() instanceof Vulnerabilities) {
          allVulnerabilities.add((Vulnerabilities) value.get());
        }
      }
    }

    return VULNERABILITIES_IN_PROJECT.value(allVulnerabilities);
  }
}
