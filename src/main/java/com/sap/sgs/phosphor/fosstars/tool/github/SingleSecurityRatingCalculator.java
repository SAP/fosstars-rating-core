package com.sap.sgs.phosphor.fosstars.tool.github;

import com.sap.sgs.phosphor.fosstars.data.DataProvider;
import com.sap.sgs.phosphor.fosstars.data.github.HasCompanySupport;
import com.sap.sgs.phosphor.fosstars.data.github.HasSecurityPolicy;
import com.sap.sgs.phosphor.fosstars.data.github.HasSecurityTeam;
import com.sap.sgs.phosphor.fosstars.data.github.InfoAboutVulnerabilities;
import com.sap.sgs.phosphor.fosstars.data.github.IsApache;
import com.sap.sgs.phosphor.fosstars.data.github.IsEclipse;
import com.sap.sgs.phosphor.fosstars.data.github.LgtmDataProvider;
import com.sap.sgs.phosphor.fosstars.data.github.NumberOfCommits;
import com.sap.sgs.phosphor.fosstars.data.github.NumberOfContributors;
import com.sap.sgs.phosphor.fosstars.data.github.NumberOfStars;
import com.sap.sgs.phosphor.fosstars.data.github.NumberOfWatchers;
import com.sap.sgs.phosphor.fosstars.data.github.ProjectStarted;
import com.sap.sgs.phosphor.fosstars.data.github.ScansForVulnerableDependencies;
import com.sap.sgs.phosphor.fosstars.data.github.SecurityReviewForProject;
import com.sap.sgs.phosphor.fosstars.data.github.UsesSignedCommits;
import com.sap.sgs.phosphor.fosstars.data.interactive.AskAboutSecurityTeam;
import com.sap.sgs.phosphor.fosstars.data.interactive.AskAboutUnpatchedVulnerabilities;
import com.sap.sgs.phosphor.fosstars.model.RatingRepository;
import com.sap.sgs.phosphor.fosstars.model.Value;
import com.sap.sgs.phosphor.fosstars.model.ValueSet;
import com.sap.sgs.phosphor.fosstars.model.rating.oss.OssSecurityRating;
import com.sap.sgs.phosphor.fosstars.model.value.ValueHashSet;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import org.kohsuke.github.GitHub;

/**
 * The class calculates a security rating for a single open-source project.
 */
class SingleSecurityRatingCalculator extends AbstractRatingCalculator {

  /**
   * Initializes a new calculator.
   *
   * @param github An interface to GitHub.
   */
  SingleSecurityRatingCalculator(GitHub github) {
    super(github);
  }

  @Override
  public SingleSecurityRatingCalculator calculateFor(GitHubProject project) throws IOException {
    Objects.requireNonNull(project, "Oh no! Project can't be null!");

    OssSecurityRating rating = RatingRepository.INSTANCE.rating(OssSecurityRating.class);

    logger.info("Project: {}", project.url());
    logger.info("Let's get info about the project and calculate a security rating");

    ValueSet values = ValueHashSet.unknown(rating.allFeatures());
    for (DataProvider<GitHubProject> provider : dataProviders()) {

      // skip data providers that talk to users but the callback doesn't allow that
      if (provider.interactive() && !callback.canTalk()) {
        continue;
      }

      try {
        provider.set(callback).set(cache).update(project, values);
      } catch (Exception e) {
        logger.warn("Holy Moly, {} data provider failed!",
            provider.getClass().getSimpleName());
        logger.warn("The last thing that it said was", e);
        logger.warn("But we don't give up!");
      }
    }

    logger.info("Here is what we know about the project:");
    for (Value value : values.toArray()) {
      logger.info("   {}: {}",
          value.feature(), value.isUnknown() ? "unknown" : value.get());
    }

    project.set(rating.calculate(values));

    return this;
  }

  @Override
  public SingleSecurityRatingCalculator calculateFor(List<GitHubProject> projects) {
    throw new UnsupportedOperationException("I can't handle multiple projects!");
  }

  /**
   * Initializes a list of data providers that are going to be used by the calculator.
   *
   * @return The list of data providers.
   * @throws IOException If something went wrong during the initialization.
   */
  List<DataProvider<GitHubProject>> dataProviders() throws IOException {
    return Arrays.asList(
        new NumberOfCommits(github),
        new NumberOfContributors(github),
        new NumberOfStars(github),
        new NumberOfWatchers(github),
        new ProjectStarted(github),
        new HasSecurityTeam(github),
        new HasCompanySupport(github),
        new HasSecurityPolicy(github),
        new SecurityReviewForProject(github),
        new InfoAboutVulnerabilities(github),
        new IsApache(github),
        new IsEclipse(github),
        new LgtmDataProvider(github),
        new UsesSignedCommits(github),
        new ScansForVulnerableDependencies(github),

        // currently interactive data provider have to be added to the end, see issue #133
        new AskAboutSecurityTeam<>(),
        new AskAboutUnpatchedVulnerabilities<>()
    );
  }
}
