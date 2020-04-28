package com.sap.sgs.phosphor.fosstars.tool.github;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.sap.sgs.phosphor.fosstars.data.NoUserCallback;
import com.sap.sgs.phosphor.fosstars.data.Terminal;
import com.sap.sgs.phosphor.fosstars.data.UserCallback;
import com.sap.sgs.phosphor.fosstars.model.value.RatingValue;
import com.sap.sgs.phosphor.fosstars.nvd.NVD;
import com.sap.sgs.phosphor.fosstars.tool.InputString;
import com.sap.sgs.phosphor.fosstars.tool.Reporter;
import com.sap.sgs.phosphor.fosstars.tool.YesNoQuestion;
import com.sap.sgs.phosphor.fosstars.tool.YesNoQuestion.Answer;
import com.sap.sgs.phosphor.fosstars.tool.format.PrettyPrinter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;
import org.kohsuke.github.HttpConnector;
import org.kohsuke.github.extras.ImpatientHttpConnector;

/**
 * This is a command line tool for calculating security ratings for one
 * of multiple open-source projects.
 */
public class SecurityRatingCalculator {

  /**
   * A logger.
   */
  private static final Logger LOGGER = LogManager.getLogger(SecurityRatingCalculator.class);

  /**
   * A directory where the tool stores stuff.
   */
  private static final String FOSSTARS_DIRECTORY = ".fosstars";

  /**
   * A path to the cache.
   */
  private static final String PATH_TO_VALUE_CACHE
      = FOSSTARS_DIRECTORY + File.separator + "github_project_value_cache.json";

  /**
   * A shared cache.
   */
  private static final GitHubProjectValueCache VALUE_CACHE = loadValueCache();

  /**
   * A file name of the default cache of projects.
   */
  private static final String DEFAULT_PROJECT_CACHE_FILE
      = FOSSTARS_DIRECTORY + File.separator + "project_cache.json";

  /**
   * A usage message.
   */
  private static final String USAGE =
      "java -jar fosstars-github-rating-calc.jar [options]";

  /**
   * An interface to NVD.
   */
  private static final NVD nvd = new NVD();

  /**
   * Entry point.
   *
   * @param args Command-line parameters.
   */
  public static void main(String... args) {
    try {
      run(args);
    } catch (Exception e) {
      LOGGER.error("Something went wrong!", e);
      LOGGER.error("Bye!");
      System.exit(1);
    }
    LOGGER.info("Bye!");
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

    UserCallback callback = commandLine.hasOption("no-questions")
        ? NoUserCallback.INSTANCE : new Terminal();

    String token = commandLine.getOptionValue("token");

    GitHub github = connectToGithub(token, callback);

    nvd.download();
    nvd.parse();

    Path path = Paths.get(FOSSTARS_DIRECTORY);
    if (!Files.exists(path)) {
      Files.createDirectories(path);
    }

    try {
      if (commandLine.hasOption("url")) {
        processUrl(commandLine.getOptionValue("url"), github, token, callback);
      }

      if (commandLine.hasOption("config")) {
        processConfig(commandLine.getOptionValue("config"), github, token, callback);
      }
    } finally {
      VALUE_CACHE.store(PATH_TO_VALUE_CACHE);
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

    new SingleSecurityRatingCalculator(github, nvd)
        .set(callback)
        .set(VALUE_CACHE)
        .token(githubToken)
        .calculateFor(project);

    RatingValue ratingValue = project.ratingValue()
        .orElseThrow(() -> new IOException("Could not calculate a rating!"));

    String content = new PrettyPrinter().print(ratingValue);
    for (String line : content.split("\n")) {
      LOGGER.info(line);
    }
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

    LOGGER.info("Loading config from {}", filename);
    Config config = config(filename);

    // try to create reporters earlier to catch a possible misconfiguration
    // before calculating ratings
    final List<Reporter<GitHubProject>> reporters = makeReporters(config);

    LOGGER.info("Look for projects ...");
    List<GitHubProject> projects = new GitHubProjectFinder(github).set(config.finderConfig).run();
    LOGGER.info("Found {} project{}", projects.size(), projects.size() > 1 ? "s" : "");
    for (GitHubProject project : projects) {
      LOGGER.info("  {}", project.url());
    }

    String projectCacheFile = projectCacheFile(config);
    GitHubProjectCache projectCache = loadProjectCache(projectCacheFile);

    LOGGER.info("Starting calculating ratings ...");
    MultipleSecurityRatingsCalculator calculator
        = new MultipleSecurityRatingsCalculator(github, nvd);
    calculator.set(projectCache);
    calculator.set(VALUE_CACHE);
    calculator.set(callback);
    calculator.token(githubToken);
    calculator.calculateFor(projects);

    LOGGER.info("Storing the project cache to {}", projectCacheFile);
    projectCache.store(projectCacheFile);

    LOGGER.info("Okay, we've done calculating the ratings");

    List<GitHubProject> failedProjects = calculator.failedProjects();
    if (!failedProjects.isEmpty()) {
      LOGGER.warn("Ratings couldn't be calculated for {} project{}",
          failedProjects.size(), failedProjects.size() == 1 ? "" : "s");
      for (GitHubProject project : projects) {
        LOGGER.info("    {}", project.url());
      }
    }

    if (!reporters.isEmpty()) {
      LOGGER.info("Now let's generate reports");
      for (Reporter<GitHubProject> reporter : reporters) {
        reporter.runFor(projects);
      }
    }
  }

  /**
   * Loads a cache of projects from a file.
   * If the file doesn't exist, then the method returns an empty cache.
   *
   * @param filename A path to the file.
   * @return A loaded cache of projects.
   * @throws IOException If something went wrong.
   */
  private static GitHubProjectCache loadProjectCache(String filename) throws IOException {
    if (Files.exists(Paths.get(filename))) {
      LOGGER.info("Loading a project cache from {}", filename);
      return GitHubProjectCache.load(filename);
    }

    return GitHubProjectCache.empty();
  }

  /**
   * Returns a filename of a project cache.
   *
   * @param config A config of the tool.
   * @return A file from the config if available, the default cache file otherwise.
   */
  private static String projectCacheFile(Config config) {
    return config.hasCacheFile() ? config.cacheFilename : DEFAULT_PROJECT_CACHE_FILE;
  }

  /**
   * Create a reporter from a config.
   * The method returns {@link Reporter#DUMMY} if the config is null.
   *
   * @param config The config.
   * @return A reporter.
   * @throws IllegalArgumentException If the type is unknown.
   */
  private static List<Reporter<GitHubProject>> makeReporters(Config config) throws IOException {
    Objects.requireNonNull(config, "Oh no! Config is null!");
    if (config.reportConfigs == null) {
      return Collections.singletonList(Reporter.DUMMY);
    }

    List<Reporter<GitHubProject>> reporters = new ArrayList<>();
    for (ReportConfig reportConfig : config.reportConfigs) {
      Objects.requireNonNull(reportConfig.type, "Hey! Reporter type can't be null!");
      switch (reportConfig.type) {
        case MARKDOWN:
          reporters.add(new MarkdownReporter(reportConfig.where, reportConfig.source));
          break;
        case JSON:
          reporters.add(new MergedJsonReporter(reportConfig.where));
          break;
        default:
          throw new IllegalArgumentException(String.format(
              "Oh no! That's an unknown type of report: %s", reportConfig.type));
      }
    }

    return reporters;
  }

  /**
   * Loads a config from a file.
   *
   * @param filename A path to the config.
   * @return A loaded config.
   * @throws IOException If something went wrong.
   */
  private static Config config(String filename) throws IOException {
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
    mapper.enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS);
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
    final List<ReportConfig> reportConfigs;

    /**
     * A config for {@link GitHubProjectFinder}.
     */
    final GitHubProjectFinder.Config finderConfig;

    /**
     * Creates a new config.
     *
     * @param cacheFilename Where a cache file is located.
     * @param reportConfigs A config for reporting.
     * @param finderConfig A configuration from {@link GitHubProjectFinder}.
     */
    Config(
        @JsonProperty("cache") String cacheFilename,
        @JsonProperty("reports") List<ReportConfig> reportConfigs,
        @JsonProperty("finder") GitHubProjectFinder.Config finderConfig) {

      this.cacheFilename = cacheFilename;
      this.reportConfigs = reportConfigs;
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

    public enum ReportType {
      MARKDOWN, JSON
    }

    /**
     * A type of a report.
     */
    final ReportType type;

    /**
     * Where a report should be stored.
     */
    final String where;

    /**
     * A source of data.
     */
    final String source;

    /**
     * Creates a new config.
     *
     * @param type A type of a report.
     * @param where Where a report should be stored.
     * @param source A source of data
     */
    ReportConfig(
        @JsonProperty("type") ReportType type,
        @JsonProperty("where") String where,
        @JsonProperty("source") String source) {

      this.type = type;
      this.where = where;
      this.source = source;
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
      LOGGER.warn("You didn't provide an access token for GitHub ...");
      LOGGER.warn("But you can create it now. Do the following:");
      LOGGER.info("    1. Go to https://github.com/settings/tokens");
      LOGGER.info("    2. Click the 'Generate new token' button");
      LOGGER.info("    3. Write a short note for a token");
      LOGGER.info("    4. Select scopes");
      LOGGER.info("    5. Click the 'Generate token' button");
      LOGGER.info("    6. Copy your new token");
      LOGGER.info("    7. Paste the token here");

      Answer answer = new YesNoQuestion(callback, "Would you like to create a token now?").ask();
      switch (answer) {
        case YES:
          LOGGER.info("Paste the token here ------+");
          LOGGER.info("                               |");
          LOGGER.info("                               |");
          LOGGER.info("     +-------------------------+");
          LOGGER.info("     |");
          LOGGER.info("     |");
          LOGGER.info("     V");
          token = new InputString(callback).get();
          break;
        case NO:
          LOGGER.info("Okay ...");
          break;
        default:
          throw new IllegalArgumentException(
              String.format("Not sure what I can do with '%s'", answer));
      }
    }

    ImpatientHttpConnector connector = new ImpatientHttpConnector(HttpConnector.DEFAULT);

    List<Exception> suppressed = new ArrayList<>();
    if (token != null) {
      LOGGER.info("Okay, we have a GitHub token, let's try to use it");
      try {
        GitHub github = new GitHubBuilder()
            .withConnector(connector)
            .withOAuthToken(token)
            .build();
        return github;
      } catch (IOException e) {
        LOGGER.warn("Something went wrong: {}", e);
        suppressed.add(e);
      }
    } else {
      LOGGER.warn("No GitHub token provided");
    }

    try {
      LOGGER.info("Now, let's try to use GitHub settings from environment variables");
      return GitHubBuilder.fromEnvironment()
          .withConnector(connector)
          .build();
    } catch (IOException e) {
      LOGGER.warn("Could not connect to GitHub", e);

      suppressed.add(e);
    }

    try {
      LOGGER.info("Then, let's try to establish an anonymous connection");
      GitHub github = new GitHubBuilder().withConnector(connector).build();
      LOGGER.warn("We have established only an anonymous connection to GitHub ...");
      return github;
    } catch (IOException e) {
      LOGGER.warn("Something went wrong", e);
      suppressed.add(e);
    }

    IOException error = new IOException("Could not connect to GitHub!");
    for (Exception e : suppressed) {
      error.addSuppressed(e);
    }
    throw error;
  }

  /**
   * Initializes a value cache.
   *
   * @return The value cache.
   */
  private static GitHubProjectValueCache loadValueCache() {
    try {
      return GitHubProjectValueCache.load(PATH_TO_VALUE_CACHE);
    } catch (FileNotFoundException e) {
      LOGGER.info("The default value cache doesn't exist yet.");
    } catch (IOException e) {
      LOGGER.warn("Could not load the default value cache!", e);
    }
    return new GitHubProjectValueCache();
  }

}
