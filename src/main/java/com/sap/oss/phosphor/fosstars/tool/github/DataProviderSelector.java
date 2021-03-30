package com.sap.oss.phosphor.fosstars.tool.github;

import com.sap.oss.phosphor.fosstars.data.DataProvider;
import com.sap.oss.phosphor.fosstars.data.github.CodeqlDataProvider;
import com.sap.oss.phosphor.fosstars.data.github.ContributingGuidelineInfo;
import com.sap.oss.phosphor.fosstars.data.github.FuzzedInOssFuzz;
import com.sap.oss.phosphor.fosstars.data.github.GitHubDataFetcher;
import com.sap.oss.phosphor.fosstars.data.github.HasBugBountyProgram;
import com.sap.oss.phosphor.fosstars.data.github.HasCompanySupport;
import com.sap.oss.phosphor.fosstars.data.github.HasSecurityPolicy;
import com.sap.oss.phosphor.fosstars.data.github.HasSecurityTeam;
import com.sap.oss.phosphor.fosstars.data.github.InfoAboutVulnerabilities;
import com.sap.oss.phosphor.fosstars.data.github.IsApache;
import com.sap.oss.phosphor.fosstars.data.github.IsEclipse;
import com.sap.oss.phosphor.fosstars.data.github.LgtmDataProvider;
import com.sap.oss.phosphor.fosstars.data.github.LicenseInfo;
import com.sap.oss.phosphor.fosstars.data.github.NumberOfCommits;
import com.sap.oss.phosphor.fosstars.data.github.NumberOfContributors;
import com.sap.oss.phosphor.fosstars.data.github.NumberOfStars;
import com.sap.oss.phosphor.fosstars.data.github.NumberOfWatchers;
import com.sap.oss.phosphor.fosstars.data.github.OwaspSecurityLibraries;
import com.sap.oss.phosphor.fosstars.data.github.PackageManagement;
import com.sap.oss.phosphor.fosstars.data.github.ProgrammingLanguages;
import com.sap.oss.phosphor.fosstars.data.github.ReadmeInfo;
import com.sap.oss.phosphor.fosstars.data.github.ReleasesFromGitHub;
import com.sap.oss.phosphor.fosstars.data.github.SignsJarArtifacts;
import com.sap.oss.phosphor.fosstars.data.github.TeamsInfo;
import com.sap.oss.phosphor.fosstars.data.github.UseReuseDataProvider;
import com.sap.oss.phosphor.fosstars.data.github.UsesDependabot;
import com.sap.oss.phosphor.fosstars.data.github.UsesFindSecBugs;
import com.sap.oss.phosphor.fosstars.data.github.UsesGithubForDevelopment;
import com.sap.oss.phosphor.fosstars.data.github.UsesNoHttpTool;
import com.sap.oss.phosphor.fosstars.data.github.UsesOwaspDependencyCheck;
import com.sap.oss.phosphor.fosstars.data.github.UsesSanitizers;
import com.sap.oss.phosphor.fosstars.data.github.UsesSignedCommits;
import com.sap.oss.phosphor.fosstars.data.github.VulnerabilityAlertsInfo;
import com.sap.oss.phosphor.fosstars.data.interactive.AskAboutSecurityTeam;
import com.sap.oss.phosphor.fosstars.data.interactive.AskAboutUnpatchedVulnerabilities;
import com.sap.oss.phosphor.fosstars.model.Feature;
import com.sap.oss.phosphor.fosstars.model.Rating;
import com.sap.oss.phosphor.fosstars.model.subject.oss.GitHubProject;
import com.sap.oss.phosphor.fosstars.nvd.NVD;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * The class selects data providers that gather data for calculating a rating.
 */
public class DataProviderSelector {

  /**
   * A list of available data providers.
   */
  private final List<DataProvider<GitHubProject>> providers;

  /**
   * Initializes a new selector and providers.
   *
   * @param fetcher An interface to GitHub.
   * @param nvd An interface to NVD.
   * @throws IOException If something went wrong.
   */
  DataProviderSelector(GitHubDataFetcher fetcher, NVD nvd) throws IOException {
    Objects.requireNonNull(fetcher, "Oops! Fetcher can't be null!");
    Objects.requireNonNull(nvd, "Oops! NVD can't be null!");

    providers = Arrays.asList(
        new NumberOfCommits(fetcher),
        new NumberOfContributors(fetcher),
        new NumberOfStars(fetcher),
        new NumberOfWatchers(fetcher),
        new HasSecurityTeam(fetcher),
        new HasCompanySupport(fetcher),
        new HasSecurityPolicy(fetcher),
        new HasBugBountyProgram(fetcher),
        new InfoAboutVulnerabilities(fetcher, nvd),
        new IsApache(fetcher),
        new IsEclipse(fetcher),
        new CodeqlDataProvider(fetcher),
        new LgtmDataProvider(fetcher),
        new UsesSignedCommits(fetcher),
        new UsesDependabot(fetcher),
        new ProgrammingLanguages(fetcher),
        new PackageManagement(fetcher),
        new UsesNoHttpTool(fetcher),
        new UsesGithubForDevelopment(fetcher),
        new UsesOwaspDependencyCheck(fetcher),
        new UsesSanitizers(fetcher),
        new UsesFindSecBugs(fetcher),
        new FuzzedInOssFuzz(fetcher),
        new SignsJarArtifacts(fetcher),
        new OwaspSecurityLibraries(fetcher),
        new UseReuseDataProvider(fetcher),
        new ReleasesFromGitHub(fetcher),
        new LicenseInfo(fetcher),
        new ReadmeInfo(fetcher),
        new TeamsInfo(fetcher),
        new ContributingGuidelineInfo(fetcher),
        new VulnerabilityAlertsInfo(fetcher),

        // currently interactive data provider have to be added to the end, see issue #133
        new AskAboutSecurityTeam<>(),
        new AskAboutUnpatchedVulnerabilities<>()
    );
  }

  /**
   * Configure data providers.
   *
   * @param configs A list of config files.
   * @throws IOException If something went wrong.
   */
  public void configure(List<String> configs) throws IOException {
    Objects.requireNonNull(configs, "Oops! Configs can't be null!");
    for (String config : configs) {
      loadConfigFrom(Paths.get(config));
    }
  }

  /**
   * Configure a data provider.
   *
   * @param path A config file
   * @throws IOException If something went wrong.
   */
  private void loadConfigFrom(Path path) throws IOException {

  }

  /**
   * Select data providers that gather data for calculating a specified rating.
   *
   * @param rating The rating.
   * @return A list of providers.
   */
  public List<DataProvider<GitHubProject>> providersFor(Rating rating) {
    return rating.allFeatures().stream()
        .map(this::providersFor)
        .flatMap(List::stream)
        .collect(Collectors.toList());
  }

  /**
   * Select data providers that gather a specified feature.
   *
   * @param feature The feature.
   * @return A list of data providers.
   */
  List<DataProvider<GitHubProject>> providersFor(Feature<?> feature) {
    return providers.stream()
        .filter(provider -> applicable(provider, feature))
        .collect(Collectors.toList());
  }

  /**
   * Check whether a data provider gathers a feature.
   *
   * @param provider The provider.
   * @param feature The feature.
   * @return True if the data provider gathers the feature, false otherwise.
   */
  private static boolean applicable(DataProvider<?> provider, Feature<?> feature) {
    return provider.supportedFeatures().contains(feature);
  }
}
