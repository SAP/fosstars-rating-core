package com.sap.sgs.phosphor.fosstars.tool.github;

import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.SCANS_FOR_VULNERABLE_DEPENDENCIES;

import com.sap.sgs.phosphor.fosstars.data.CompositeDataProvider;
import com.sap.sgs.phosphor.fosstars.data.DataProvider;
import com.sap.sgs.phosphor.fosstars.data.IsApache;
import com.sap.sgs.phosphor.fosstars.data.IsEclipse;
import com.sap.sgs.phosphor.fosstars.data.ValueCache;
import com.sap.sgs.phosphor.fosstars.data.github.HasCompanySupport;
import com.sap.sgs.phosphor.fosstars.data.github.HasSecurityPolicy;
import com.sap.sgs.phosphor.fosstars.data.github.HasSecurityTeam;
import com.sap.sgs.phosphor.fosstars.data.github.NumberOfCommits;
import com.sap.sgs.phosphor.fosstars.data.github.NumberOfContributors;
import com.sap.sgs.phosphor.fosstars.data.github.NumberOfStars;
import com.sap.sgs.phosphor.fosstars.data.github.NumberOfWatchers;
import com.sap.sgs.phosphor.fosstars.data.github.ProjectStarted;
import com.sap.sgs.phosphor.fosstars.data.github.SecurityReviewForProject;
import com.sap.sgs.phosphor.fosstars.data.github.UnpatchedVulnerabilities;
import com.sap.sgs.phosphor.fosstars.data.github.UsesOwaspDependencyCheck;
import com.sap.sgs.phosphor.fosstars.data.github.UsesSignedCommits;
import com.sap.sgs.phosphor.fosstars.data.github.UsesSnykDependencyCheck;
import com.sap.sgs.phosphor.fosstars.data.github.VulnerabilitiesFromNvd;
import com.sap.sgs.phosphor.fosstars.data.lgtm.LgtmDataProvider;
import com.sap.sgs.phosphor.fosstars.model.RatingRepository;
import com.sap.sgs.phosphor.fosstars.model.Value;
import com.sap.sgs.phosphor.fosstars.model.ValueSet;
import com.sap.sgs.phosphor.fosstars.model.rating.oss.OssSecurityRating;
import com.sap.sgs.phosphor.fosstars.model.value.ValueHashSet;
import com.sap.sgs.phosphor.fosstars.model.value.Vulnerabilities;
import com.sap.sgs.phosphor.fosstars.model.value.VulnerabilitiesValue;
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
  SingleSecurityRatingCalculator calculateFor(GitHubProject project) throws IOException {
    Objects.requireNonNull(project, "Oh no! Project can't be null!");
    String where = project.organization().name();
    String name = project.name();

    OssSecurityRating rating = RatingRepository.INSTANCE.rating(OssSecurityRating.class);

    System.out.printf("[+] Project: %s%n", project.url());
    System.out.printf("[+] Let's get info about the project and calculate a security rating%n");
    ValueSet values = ValueHashSet.unknown(rating.allFeatures());
    for (DataProvider provider : dataProviders(where, name)) {
      try {
        provider.set(callback).update(values);
      } catch (Exception e) {
        System.out.printf("[!] Holy Moly, %s data provider failed!%n",
            provider.getClass().getSimpleName());
        System.out.printf("[!] The last thing that it said was: %s%n", e.getMessage());
        System.out.printf("[!] But we don't give up!%n");
      }
    }

    System.out.println("[+] Here is what we know about the project:");
    for (Value value : values.toArray()) {
      System.out.printf("[+]    %s: %s%n",
          value.feature(), value.isUnknown() ? "unknown" : value.get());
    }

    // store the default value cache
    ValueCache.shared().store();

    project.set(rating.calculate(values));

    return this;
  }

  @Override
  AbstractRatingCalculator calculateFor(List<GitHubProject> projects) throws IOException {
    throw new UnsupportedOperationException("I can't handle multiple projects!");
  }

  /**
   * Returns a list of data providers which the calculator supports.
   * The data providers are initialized to gather information about
   * a specified project.
   *
   * @param organization Organization's name.
   * @param project Project's name.
   * @return A list of data providers.
   * @throws IOException If something went wrong.
   */
  List<DataProvider> dataProviders(String organization, String project) throws IOException {
    Value<Vulnerabilities> vulnerabilities = new VulnerabilitiesValue();
    return Arrays.asList(
        new NumberOfCommits(organization, project, github),
        new NumberOfContributors(organization, project, github),
        new NumberOfStars(organization, project, github),
        new NumberOfWatchers(organization, project, github),
        new ProjectStarted(organization, project, github),
        new HasSecurityTeam(organization, project, github),
        new HasCompanySupport(organization, project, github),
        new HasSecurityPolicy(organization, project, github),
        new SecurityReviewForProject(organization, project, github),
        new UnpatchedVulnerabilities(organization, project, github, vulnerabilities),
        new VulnerabilitiesFromNvd(organization, project, github, vulnerabilities),
        new IsApache(organization),
        new IsEclipse(organization),
        new CompositeDataProvider(
            new UsesOwaspDependencyCheck(organization, project, github),
            new UsesSnykDependencyCheck(organization, project, github)
        ).stopWhenFilledOut(SCANS_FOR_VULNERABLE_DEPENDENCIES),
        new LgtmDataProvider(organization, project),
        new UsesSignedCommits(organization, project, github, token)
    );
  }

}
