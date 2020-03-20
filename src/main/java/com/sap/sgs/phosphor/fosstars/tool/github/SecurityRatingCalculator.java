package com.sap.sgs.phosphor.fosstars.tool.github;

import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.SCANS_FOR_VULNERABLE_DEPENDENCIES;

import com.sap.sgs.phosphor.fosstars.data.CompositeDataProvider;
import com.sap.sgs.phosphor.fosstars.data.DataProvider;
import com.sap.sgs.phosphor.fosstars.data.IsApache;
import com.sap.sgs.phosphor.fosstars.data.IsEclipse;
import com.sap.sgs.phosphor.fosstars.data.NoUserCallback;
import com.sap.sgs.phosphor.fosstars.data.Terminal;
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
import com.sap.sgs.phosphor.fosstars.model.value.RatingValue;
import com.sap.sgs.phosphor.fosstars.model.value.ValueHashSet;
import com.sap.sgs.phosphor.fosstars.model.value.Vulnerabilities;
import com.sap.sgs.phosphor.fosstars.model.value.VulnerabilitiesValue;
import com.sap.sgs.phosphor.fosstars.tool.InputString;
import com.sap.sgs.phosphor.fosstars.tool.PrettyPrinter;
import com.sap.sgs.phosphor.fosstars.tool.YesNoQuestion;
import com.sap.sgs.phosphor.fosstars.tool.YesNoQuestion.Answer;
import java.io.IOException;
import java.net.URL;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.kohsuke.github.GitHub;

public class SecurityRatingCalculator {

  private static final String USAGE =
      "java -jar fosstars-github-rating-calc.jar [options]";

  /**
   * Entry point.
   */
  public static void main(String... args) throws IOException {
    Options options = new Options();
    options.addOption("h", "help", false,
        "print this message");
    options.addOption("n", "no-questions", false,
        "don't ask a user if a feature can't be automatically gathered");
    options.addOption(Option.builder("u")
        .required()
        .hasArg()
        .longOpt("url")
        .desc("repository URL")
        .build());
    options.addOption(Option.builder("t")
        .longOpt("token")
        .hasArg()
        .desc("access token")
        .build());

    CommandLine commandLine;
    try {
      commandLine = new DefaultParser().parse(options, args);
    } catch (ParseException e) {
      System.out.printf("[x] Something went wrong: %s%n", e.getMessage());
      HelpFormatter formatter = new HelpFormatter();
      formatter.printHelp(USAGE, options);
      return;
    }

    if (commandLine.hasOption("h")) {
      HelpFormatter formatter = new HelpFormatter();
      formatter.printHelp(USAGE, options);
      return;
    }

    boolean mayTalk = !commandLine.hasOption("no-questions");
    UserCallback callback = mayTalk ? new Terminal() : NoUserCallback.INSTANCE;

    GitHub github = connectToGithub(commandLine.getOptionValue("token"), callback);
    if (github == null) {
      System.out.println("[x] Couldn't connect to GitHub!");
      return;
    }

    URL url = new URL(commandLine.getOptionValue("url"));
    String[] parts = url.getPath().split("/");
    if (parts.length != 3) {
      System.out.println("[x] The URL doesn't seem to be correct!");
      return;
    }

    String where = parts[1];
    String name = parts[2];

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
        new UsesSignedCommits(where, name, github, commandLine.getOptionValue("token"))
    };

    OssSecurityRating rating = RatingRepository.INSTANCE.rating(OssSecurityRating.class);

    System.out.printf("[+] Project: %s%n", url);
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

    System.out.println("[+]");
    RatingValue ratingValue = rating.calculate(values);
    System.out.print(new PrettyPrinter().print(ratingValue));
    System.out.printf("[+] Bye!%n");

    // store the default value cache
    ValueCache.shared().store();
  }

  /**
   * Tries to establish a connection to GitHub.
   *
   * @param token A GitHub token (may be null).
   * @return An interface for the GitHub API, or null if it couldn't establish a connection.
   */
  private static GitHub connectToGithub(String token, UserCallback callback) {
    if (token == null && callback.canTalk()) {
      System.out.println("[!] You didn't provide an access token for GitHub ...");
      System.out.println("[!] But you can create it now. Do the following:");
      System.out.println("    1. Go to https://github.com/settings/tokens");
      System.out.println("    2. Click the 'Generate new token' button");
      System.out.println("    3. Write a short note for a token");
      System.out.println("    4. Select scopes");
      System.out.println("    5. Click the 'Generate token' button");
      System.out.println("    6. Copy your new token");
      System.out.println("    7. Paste the token here");
      Answer answer = new YesNoQuestion(callback, "Would you like to create a token now?").ask();
      switch (answer) {
        case YES:
          System.out.println("[+] Paste the token here ------+");
          System.out.println("                               |");
          System.out.println("                               |");
          System.out.println("     +-------------------------+");
          System.out.println("     |");
          System.out.println("     |");
          System.out.println("     V");
          token = new InputString(callback).get();
          break;
        case NO:
          System.out.println("Okay ...");
          break;
        default:
          throw new IllegalArgumentException(
              String.format("Not sure what I can do with '%s'", answer));
      }
    }
    if (token != null) {
      try {
        return GitHub.connectUsingOAuth(token);
      } catch (IOException e) {
        System.out.printf("[x] Something went wrong: %s%n", e);
      }
    } else {
      System.out.printf("[!] No token provided%n");
    }
    try {
      return GitHub.connect();
    } catch (IOException e) {
      System.out.printf("[x] Something went wrong: %s%n", e);
    }
    try {
      GitHub github = GitHub.connectAnonymously();
      System.out.println("[!] We have established only an anonymous connection to GitHub ...");
      return github;
    } catch (IOException e) {
      System.out.printf("[x] Something went wrong: %s%n", e);
    }
    return null;
  }
}
