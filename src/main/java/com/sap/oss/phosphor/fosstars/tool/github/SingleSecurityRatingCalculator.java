package com.sap.oss.phosphor.fosstars.tool.github;

import com.sap.oss.phosphor.fosstars.data.DataProvider;
import com.sap.oss.phosphor.fosstars.data.NoUserCallback;
import com.sap.oss.phosphor.fosstars.data.NoValueCache;
import com.sap.oss.phosphor.fosstars.data.UserCallback;
import com.sap.oss.phosphor.fosstars.data.ValueCache;
import com.sap.oss.phosphor.fosstars.data.github.CodeqlDataProvider;
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
import com.sap.oss.phosphor.fosstars.data.github.NumberOfCommits;
import com.sap.oss.phosphor.fosstars.data.github.NumberOfContributors;
import com.sap.oss.phosphor.fosstars.data.github.NumberOfStars;
import com.sap.oss.phosphor.fosstars.data.github.NumberOfWatchers;
import com.sap.oss.phosphor.fosstars.data.github.OwaspSecurityLibraries;
import com.sap.oss.phosphor.fosstars.data.github.PackageManagement;
import com.sap.oss.phosphor.fosstars.data.github.ProgrammingLanguages;
import com.sap.oss.phosphor.fosstars.data.github.SignsJarArtifacts;
import com.sap.oss.phosphor.fosstars.data.github.UsesDependabot;
import com.sap.oss.phosphor.fosstars.data.github.UsesFindSecBugs;
import com.sap.oss.phosphor.fosstars.data.github.UsesGithubForDevelopment;
import com.sap.oss.phosphor.fosstars.data.github.UsesNoHttpTool;
import com.sap.oss.phosphor.fosstars.data.github.UsesOwaspDependencyCheck;
import com.sap.oss.phosphor.fosstars.data.github.UsesSanitizers;
import com.sap.oss.phosphor.fosstars.data.github.UsesSignedCommits;
import com.sap.oss.phosphor.fosstars.data.interactive.AskAboutSecurityTeam;
import com.sap.oss.phosphor.fosstars.data.interactive.AskAboutUnpatchedVulnerabilities;
import com.sap.oss.phosphor.fosstars.model.RatingRepository;
import com.sap.oss.phosphor.fosstars.model.ValueSet;
import com.sap.oss.phosphor.fosstars.model.rating.oss.OssSecurityRating;
import com.sap.oss.phosphor.fosstars.model.subject.oss.GitHubProject;
import com.sap.oss.phosphor.fosstars.model.value.ValueHashSet;
import com.sap.oss.phosphor.fosstars.nvd.NVD;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The class calculates a security rating for a single open-source project.
 */
class SingleSecurityRatingCalculator {

  /**
   * A logger.
   */
  private static final Logger LOGGER = LogManager.getLogger(SingleSecurityRatingCalculator.class);

  /**
   * An interface to GitHub.
   */
  private final GitHubDataFetcher fetcher;

  /**
   * An interface to NVD.
   */
  private final NVD nvd;

  /**
   * Open source security rating.
   */
  private final OssSecurityRating rating
      = RatingRepository.INSTANCE.rating(OssSecurityRating.class);

  /**
   * A cache of feature values for GitHub projects.
   */
  private ValueCache<GitHubProject> cache = NoValueCache.create();

  /**
   * An interface for interacting with a user.
   */
  private UserCallback callback = NoUserCallback.INSTANCE;

  /**
   * Initializes a new calculator.
   *
   * @param fetcher An interface to GitHub.
   * @param nvd An interface to NVD.
   */
  SingleSecurityRatingCalculator(GitHubDataFetcher fetcher, NVD nvd) {
    Objects.requireNonNull(fetcher, "Oh no! An interface to GitHub can't be null!");
    Objects.requireNonNull(nvd, "Oh no! An interface to NVD can't be null!");
    this.fetcher = fetcher;
    this.nvd = nvd;
  }

  /**
   * Get the open source security rating.
   *
   * @return The rating.
   */
  OssSecurityRating rating() {
    return rating;
  }

  /**
   * Sets an interface for interacting with a user.
   *
   * @param callback The interface for interacting with a user.
   * @return The same calculator.
   */
  SingleSecurityRatingCalculator set(UserCallback callback) {
    this.callback = callback;
    return this;
  }

  /**
   * Set a cache for the calculator.
   *
   * @param cache The cache.
   * @return The same calculator.
   */
  SingleSecurityRatingCalculator set(ValueCache<GitHubProject> cache) {
    this.cache = Objects.requireNonNull(cache, "Oh no! Cache can't be null!");
    return this;
  }

  public SingleSecurityRatingCalculator calculateFor(GitHubProject project) throws IOException {
    Objects.requireNonNull(project, "Oh no! Project can't be null!");

    LOGGER.info("Let's gather info and calculate a security rating for:");
    LOGGER.info("  {}", project.scm());

    try {
      fetcher.repositoryFor(project);
    } catch (IOException e) {
      LOGGER.error("Looks like something is wrong with the project!", e);
      LOGGER.warn("Let's skip the project ...");
      return this;
    }

    ValueSet values = ValueHashSet.unknown(rating.allFeatures());
    for (DataProvider<GitHubProject> provider : dataProviders()) {

      // skip data providers that talk to users but the callback doesn't allow that
      if (provider.interactive() && !callback.canTalk()) {
        continue;
      }

      try {
        provider.set(callback).set(cache).update(project, values);
      } catch (Exception e) {
        LOGGER.warn("Holy Moly, {} data provider failed!",
            provider.getClass().getSimpleName());
        LOGGER.warn("The last thing that it said was", e);
        LOGGER.warn("But we don't give up!");
      }
    }

    LOGGER.info("Here is what we know about the project:");
    values.toSet().stream()
        .sorted(Comparator.comparing(value -> value.feature().name()))
        .forEach(value -> LOGGER.info("   {}: {}", value.feature(), value));

    project.set(rating.calculate(values));

    return this;
  }

  /**
   * Initializes a list of data providers that are going to be used by the calculator.
   *
   * @return The list of data providers.
   * @throws IOException If something went wrong during the initialization.
   */
  List<DataProvider<GitHubProject>> dataProviders() throws IOException {
    return Arrays.asList(
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

        // currently interactive data provider have to be added to the end, see issue #133
        new AskAboutSecurityTeam<>(),
        new AskAboutUnpatchedVulnerabilities<>()
    );
  }
}
