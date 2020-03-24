package com.sap.sgs.phosphor.fosstars.tool.github;

import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.SCANS_FOR_VULNERABLE_DEPENDENCIES;

import com.sap.sgs.phosphor.fosstars.data.CompositeDataProvider;
import com.sap.sgs.phosphor.fosstars.data.DataProvider;
import com.sap.sgs.phosphor.fosstars.data.IsApache;
import com.sap.sgs.phosphor.fosstars.data.IsEclipse;
import com.sap.sgs.phosphor.fosstars.data.NoUserCallback;
import com.sap.sgs.phosphor.fosstars.data.UserCallback;
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
import org.kohsuke.github.GitHub;

public class SingleSecurityRatingCalculator {

  private final GitHub github;
  private String token;
  private UserCallback callback = NoUserCallback.INSTANCE;

  public SingleSecurityRatingCalculator(GitHub github) {
    this.github = github;
  }

  public SingleSecurityRatingCalculator token(String token) {
    this.token = token;
    return this;
  }

  public SingleSecurityRatingCalculator set(UserCallback callback) {
    this.callback = callback;
    return this;
  }

  void process(GitHubProject gitHubProject) throws IOException {
    String where = gitHubProject.organization().name();
    String name = gitHubProject.name();

    Value<Vulnerabilities> vulnerabilities = new VulnerabilitiesValue();

    DataProvider[] providers = {
        new NumberOfCommits(where, name, github),
        new NumberOfContributors(where, name, github),
        new NumberOfStars(where, name, github),
        new NumberOfWatchers(where, name, github),
        new ProjectStarted(where, name, github),
        new HasSecurityTeam(where, name, github),
        new HasCompanySupport(where, name, github),
        new HasSecurityPolicy(where, name, github),
        new SecurityReviewForProject(where, name, github),
        new UnpatchedVulnerabilities(where, name, github, vulnerabilities),
        new VulnerabilitiesFromNvd(where, name, github, vulnerabilities),
        new IsApache(where),
        new IsEclipse(where),
        new CompositeDataProvider(
            new UsesOwaspDependencyCheck(where, name, github),
            new UsesSnykDependencyCheck(where, name, github)
        ).stopWhenFilledOut(SCANS_FOR_VULNERABLE_DEPENDENCIES),
        new LgtmDataProvider(where, name),
        new UsesSignedCommits(where, name, github, token)
    };

    OssSecurityRating rating = RatingRepository.INSTANCE.rating(OssSecurityRating.class);

    System.out.printf("[+] Project: %s%n", gitHubProject.url());
    System.out.printf("[+] Let's get info about the project and calculate a security rating%n");
    ValueSet values = ValueHashSet.unknown(rating.allFeatures());
    for (DataProvider provider : providers) {
      provider.set(callback).update(values);
    }

    System.out.println("[+] Here is what we know about the project:");
    for (Value value : values.toArray()) {
      System.out.printf("[+]    %s: %s%n",
          value.feature(), value.isUnknown() ? "unknown" : value.get());
    }

    // store the default value cache
    ValueCache.shared().store();

    gitHubProject.set(rating.calculate(values));
  }

}
