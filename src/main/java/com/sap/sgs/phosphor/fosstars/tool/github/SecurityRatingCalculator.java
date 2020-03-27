package com.sap.sgs.phosphor.fosstars.tool.github;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.sap.sgs.phosphor.fosstars.data.NoUserCallback;
import com.sap.sgs.phosphor.fosstars.data.Terminal;
import com.sap.sgs.phosphor.fosstars.data.UserCallback;
import com.sap.sgs.phosphor.fosstars.model.value.RatingValue;
import com.sap.sgs.phosphor.fosstars.tool.InputString;
import com.sap.sgs.phosphor.fosstars.tool.YesNoQuestion;
import com.sap.sgs.phosphor.fosstars.tool.YesNoQuestion.Answer;
import com.sap.sgs.phosphor.fosstars.tool.format.PrettyPrinter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.kohsuke.github.GitHub;

/**
 * This is a command line tool for calculating security ratings for one
 * of multiple open-source projects.
 */
public class SecurityRatingCalculator {

  /**
   * A usage message.
   */
  private static final String USAGE =
      "java -jar fosstars-github-rating-calc.jar [options]";

  /**
   * Entry point.
   *
   * @param args Command-line parameters.
   */
  public static void main(String... args) {
    try {
      run(args);
    } catch (Exception e) {
      System.out.printf("[x] Something went wrong!%n");
      e.printStackTrace(System.out);
      System.out.printf("[+] Bye!%n");
      System.exit(1);
    }
    System.out.printf("[+] Bye!%n");
  }

  /**
   * Run the tool with a list of command-line parameters.
   *
   * @param args The command-line parameters.
   * @throws IOException If something went wrong.
   */
  static void run(String... args) throws IOException {
    Options options = new Options();
    options.addOption("h", "help", false,
        "print this message");
    options.addOption("n", "no-questions", false,
        "don't ask a user if a feature can't be automatically gathered");
    options.addOption(Option.builder("t")
        .longOpt("token")
        .hasArg()
        .desc("access token")
        .build());
    OptionGroup group = new OptionGroup();
    group.addOption(Option.builder("u")
        .required()
        .hasArg()
        .longOpt("url")
        .desc("repository URL")
        .build());
    group.addOption(Option.builder("c")
        .longOpt("config")
        .hasArg()
        .desc("path to a config")
        .build());
    options.addOptionGroup(group);

    CommandLine commandLine;
    try {
      commandLine = new DefaultParser().parse(options, args);
    } catch (ParseException e) {
      HelpFormatter formatter = new HelpFormatter();
      formatter.printHelp(USAGE, options);
      throw new IOException("Could not parse command-line parameters", e);
    }

    if (commandLine.hasOption("h")) {
      HelpFormatter formatter = new HelpFormatter();
      formatter.printHelp(USAGE, options);
      return;
    }

    if (!commandLine.hasOption("url") && !commandLine.hasOption("config")) {
      throw new IllegalArgumentException(
          "You have to give me either --url or --config option but not both!");
    }

    boolean mayTalk = !commandLine.hasOption("no-questions");
    UserCallback callback = mayTalk ? new Terminal() : NoUserCallback.INSTANCE;

    GitHub github = connectToGithub(commandLine.getOptionValue("token"), callback);

    String token = commandLine.getOptionValue("token");

    if (commandLine.hasOption("url")) {
      processUrl(commandLine.getOptionValue("url"), github, token, callback);
    }

    if (commandLine.hasOption("config")) {
      processConfig(commandLine.getOptionValue("config"), github, token, callback);
    }
  }

  /**
   * Calculate a rating for a single project.
   *
   * @param url A URL of the project repository.
   * @param github An interface for accessing the GitHub APIs.
   * @param githubToken A token for accessing the GitHub APIs.
   * @param callback An interface for interacting with a user.
   * @throws IOException If something went wrong.
   */
  private static void processUrl(String url, GitHub github, String githubToken,
      UserCallback callback) throws IOException {

    GitHubProject project = GitHubProject.parse(url);

    new SingleSecurityRatingCalculator(github)
        .set(callback)
        .token(githubToken)
        .calculateFor(project);

    RatingValue ratingValue = project.ratingValue()
        .orElseThrow(() -> new IOException("Could not calculate a rating!"));

    System.out.println("[+]");
    System.out.print(new PrettyPrinter().print(ratingValue));
  }

  /**
   * Calculate a rating for projects specified in a config.
   *
   * @param filename A path to the config.
   * @param github An interface for accessing the GitHub APIs.
   * @param githubToken A token for accessing the GitHub APIs.
   * @param callback An interface for interacting with a user.
   * @throws IOException If something went wrong.
   */
  private static void processConfig(String filename, GitHub github, String githubToken,
      UserCallback callback) throws IOException {

    System.out.printf("[+] Loading config from %s%n", filename);
    Config config = config(filename);

    System.out.printf("[+] Look for projects ...%n");
    List<GitHubProject> projects = new GitHubProjectFinder(github).set(config.finderConfig).run();
    System.out.printf("[+] Found %d project%s%n", projects.size(), projects.size() > 1 ? "s" : "");
    for (GitHubProject project : projects) {
      System.out.printf("[+]   %s%n", project.url());
    }

    GitHubProjectCache projectCache = GitHubProjectCache.empty();
    if (config.hasCacheFile()) {
      if (Files.exists(Paths.get(config.cacheFilename))) {
        System.out.printf("[+] Loading a project cache from %s%n", config.cacheFilename);
        projectCache = GitHubProjectCache.load(config.cacheFilename);
      }
    }

    System.out.printf("[+] Starting calculating ratings ...%n");
    new MultipleSecurityRatingsCalculator(github)
        .set(projectCache)
        .set(callback)
        .token(githubToken)
        .calculateFor(projects);

    System.out.println("[+] Okay, we've done with calculating the ratings");
    System.out.println("[+]");

    for (GitHubProject project : projects) {
      System.out.printf("[+] Let's take a look at %s%n", project.url());
      if (!project.ratingValue().isPresent()) {
        System.out.println("[+] Hmm ... looks like something went wrong "
            + "and the rating is not available ...");
        System.out.println("[+]");
        continue;
      }

      RatingValue ratingValue = project.ratingValue().get();
      System.out.print(new PrettyPrinter().print(ratingValue));
      System.out.println("[+]");
    }

    if (config.hasCacheFile()) {
      System.out.printf("[+] Storing the project cache to %s%n", config.cacheFilename);
      ObjectMapper mapper = new ObjectMapper();
      Files.write(
          Paths.get(config.cacheFilename),
          mapper.writerWithDefaultPrettyPrinter().writeValueAsBytes(projectCache));
    }
  }

  /**
   * Loads a config from a file.
   *
   * @param filename A path to the config.
   * @return A loaded config.
   * @throws IOException If something went wrong.
   */
  static Config config(String filename) throws IOException {
    return config(Files.newInputStream(Paths.get(filename)));
  }

  /**
   * Loads a config from an input stream.
   *
   * @param is The input stream.
   * @return A loaded config.
   * @throws IOException If something went wrong.
   */
  static Config config(InputStream is) throws IOException {
    YAMLFactory factory = new YAMLFactory();
    ObjectMapper mapper = new ObjectMapper(factory);
    return mapper.readValue(is, Config.class);
  }

  /**
   * The class holds a configuration for {@link SecurityRatingCalculator}.
   */
  static class Config {

    /**
     * Where a cache file is located.
     */
    final String cacheFilename;

    /**
     * A config for reporting.
     */
    final ReportConfig reportConfig;

    /**
     * A config for {@link GitHubProjectFinder}.
     */
    final GitHubProjectFinder.Config finderConfig;

    /**
     * Creates a new config.
     *
     * @param cacheFilename Where a cache file is located.
     * @param reportConfig A config for reporting.
     * @param finderConfig A configuration from {@link GitHubProjectFinder}.
     */
    Config(
        @JsonProperty("cache") String cacheFilename,
        @JsonProperty("report") ReportConfig reportConfig,
        @JsonProperty("finder") GitHubProjectFinder.Config finderConfig) {

      this.cacheFilename = cacheFilename;
      this.reportConfig = reportConfig;
      this.finderConfig = finderConfig;
    }

    /**
     * Checks the config has a filename of a cache.
     *
     * @return True if the config has a filename of a cache, false otherwise.
     */
    boolean hasCacheFile() {
      return cacheFilename != null && !cacheFilename.trim().isEmpty();
    }

  }

  /**
   * A config for reporting.
   */
  static class ReportConfig {

    /**
     * A type of a report.
     */
    final String type;

    /**
     * Where a report should be stored.
     */
    final String where;

    /**
     * Creates a new config.
     *
     * @param type A type of a report.
     * @param where Where a report should be stored.
     */
    ReportConfig(
        @JsonProperty("type") String type,
        @JsonProperty("where") String where) {

      this.type = type;
      this.where = where;
    }
  }

  /**
   * Tries to establish a connection to GitHub.
   *
   * @param token A GitHub token (may be null).
   * @return An interface for the GitHub API.
   * @throws IOException If a connection to GitHub couldn't be established.
   */
  private static GitHub connectToGithub(String token, UserCallback callback) throws IOException {
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

    List<Exception> suppressed = new ArrayList<>();
    if (token != null) {
      try {
        return GitHub.connectUsingOAuth(token);
      } catch (IOException e) {
        System.out.printf("[x] Something went wrong: %s%n", e);
        suppressed.add(e);
      }
    } else {
      System.out.printf("[!] No token provided%n");
    }

    try {
      return GitHub.connect();
    } catch (IOException e) {
      System.out.printf("[x] Could not connect to GitHub: %s%n", e);
      System.out.printf("[x] Let's try to establish an anonymous connection%n");
      suppressed.add(e);
    }

    try {
      GitHub github = GitHub.connectAnonymously();
      System.out.println("[!] We have established only an anonymous connection to GitHub ...");
      return github;
    } catch (IOException e) {
      System.out.printf("[x] Something went wrong: %s%n", e);
      suppressed.add(e);
    }

    IOException error = new IOException("Could not connect to GitHub!");
    for (Exception e : suppressed) {
      error.addSuppressed(e);
    }
    throw error;
  }

}
