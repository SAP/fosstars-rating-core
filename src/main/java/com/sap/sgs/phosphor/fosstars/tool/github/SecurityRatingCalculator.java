package com.sap.sgs.phosphor.fosstars.tool.github;

import static com.sap.sgs.phosphor.fosstars.model.subject.oss.GitHubProject.isOnGitHub;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.sap.sgs.phosphor.fosstars.data.NoUserCallback;
import com.sap.sgs.phosphor.fosstars.data.Terminal;
import com.sap.sgs.phosphor.fosstars.data.UserCallback;
import com.sap.sgs.phosphor.fosstars.data.github.GitHubDataFetcher;
import com.sap.sgs.phosphor.fosstars.model.Advisor;
import com.sap.sgs.phosphor.fosstars.model.advice.oss.CodeqlScoreAdvisor;
import com.sap.sgs.phosphor.fosstars.model.subject.oss.GitHubProject;
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
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
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
   * An advisor for calculated ratings.
   */
  private static final Advisor ADVISOR = new CodeqlScoreAdvisor();

  /**
   * Entry point.
   *
   * @param args Command-line parameters.
   */
  public static void main(String... args) {
    try {
      new SecurityRatingCalculator(args).run();
    } catch (Exception e) {
      LOGGER.error("Something went wrong!", e);
      LOGGER.error("Bye!");
      System.exit(1);
    }
    LOGGER.info("Bye!");
  }

  /**
   * A config for command-line options.
   */
  private final Options options;

  /**
   * Parsed command-line options.
   */
  private final CommandLine commandLine;

  /**
   * An interface to NVD.
   */
  private final NVD nvd = new NVD();

  /**
   * A {@link PrettyPrinter} for printing out a security rating.
   */
  private final PrettyPrinter prettyPrinter;

  /**
   * Creates a new security rating calculator.
   *
   * @param args The command-line parameters.
   * @throws IOException If something went wrong.
   */
  SecurityRatingCalculator(String... args) throws IOException, URISyntaxException {
    options = new Options();
    options.addOption("h", "help", false,
        "Print this message.");
    options.addOption("i", "interactive", false,
        "Ask a question if a feature can't be automatically gathered.");
    options.addOption(Option.builder("t")
        .longOpt("token")
        .hasArg()
        .desc("An access token for the GitHub API.")
        .build());
    options.addOption(
        Option.builder("v")
            .longOpt("verbose")
            .desc("Print all the details.")
            .build());

    OptionGroup group = new OptionGroup();
    group.addOption(Option.builder("u")
        .required()
        .hasArg()
        .longOpt("url")
        .desc("A URL to project's SCM")
        .build());
    group.addOption(Option.builder("g")
        .required()
        .hasArg()
        .longOpt("gav")
        .desc("GAV coordinates of a jar artifact in the format 'G:A:V' or 'G:A' "
            + "where G is a group id, A is an artifact if, and V is an optional version.")
        .build());
    group.addOption(Option.builder("c")
        .longOpt("config")
        .hasArg()
        .desc("A path to a config file.")
        .build());
    options.addOptionGroup(group);

    try {
      commandLine = new DefaultParser().parse(options, args);
    } catch (ParseException e) {
      HelpFormatter formatter = new HelpFormatter();
      formatter.printHelp(USAGE, options);
      throw new IOException("Could not parse command-line parameters", e);
    }

    String logConfig = commandLine.hasOption("v") ? "/log4j2-verbose.xml" : "/log4j2-standard.xml";
    LoggerContext context = (LoggerContext) LogManager.getContext(false);
    context.setConfigLocation(SecurityRatingCalculator.class.getResource(logConfig).toURI());
    context.updateLoggers();

    prettyPrinter = commandLine.hasOption("v")
        ? PrettyPrinter.withVerboseOutput(ADVISOR) : PrettyPrinter.withoutVerboseOutput();
  }

  /**
   * Run the tool with a list of command-line parameters.
   *
   * @throws IOException If something went wrong.
   */
  void run() throws IOException {
    if (commandLine.hasOption("h")) {
      HelpFormatter formatter = new HelpFormatter();
      formatter.printHelp(USAGE, options);
      return;
    }

    if (!commandLine.hasOption("url") && !commandLine.hasOption("config")
        && !commandLine.hasOption("gav")) {

      throw new IllegalArgumentException(
          "You have to give me either --url, --gav or --config option!");
    }

    UserCallback callback = commandLine.hasOption("interactive")
        ? new Terminal() : NoUserCallback.INSTANCE;

    String token = commandLine.getOptionValue("token");

    GitHubDataFetcher fetcher = new GitHubDataFetcher(connectToGithub(token, callback));

    nvd.download();
    nvd.parse();

    Path path = Paths.get(FOSSTARS_DIRECTORY);
    if (!Files.exists(path)) {
      Files.createDirectories(path);
    }

    try {
      if (commandLine.hasOption("url")) {
        processUrl(commandLine.getOptionValue("url"), fetcher, token, callback);
      }

      if (commandLine.hasOption("gav")) {
        processGav(commandLine.getOptionValue("gav"), fetcher, token, callback);
      }

      if (commandLine.hasOption("config")) {
        processConfig(commandLine.getOptionValue("config"), fetcher, token, callback);
      }
    } finally {
      VALUE_CACHE.store(PATH_TO_VALUE_CACHE);
    }
  }

  /**
   * Calculate a rating for a single project identified by a URL to its SCM.
   *
   * @param url A URL of the project repository.
   * @param fetcher An interface for accessing the GitHub.
   * @param githubToken A token for accessing the GitHub APIs.
   * @param callback An interface for interacting with a user.
   * @throws IOException If something went wrong.
   */
  private void processUrl(
      String url, GitHubDataFetcher fetcher, String githubToken, UserCallback callback)
      throws IOException {

    GitHubProject project = GitHubProject.parse(url);

    new SingleSecurityRatingCalculator(fetcher, nvd)
        .set(callback)
        .set(VALUE_CACHE)
        .token(githubToken)
        .calculateFor(project);

    if (!project.ratingValue().isPresent()) {
      throw new IOException("Could not calculate a rating!");
    }

    Arrays.stream(prettyPrinter.print(project).split("\n")).forEach(LOGGER::info);
    LOGGER.info("");
  }

  /**
   * Calculate a rating for a single project identified by GAV coordinates.
   *
   * @param gav The GAV coordinates.
   * @param fetcher An interface for accessing the GitHub.
   * @param githubToken A token for accessing the GitHub APIs.
   * @param callback An interface for interacting with a user.
   * @throws IOException If something went wrong.
   */
  private void processGav(
      String gav, GitHubDataFetcher fetcher, String githubToken, UserCallback callback)
      throws IOException {

    MavenScmFinder finder = new MavenScmFinder();

    Optional<String> scm = finder.findScmFor(gav);
    if (!scm.isPresent()) {
      throw new IOException("Oh no! Could not find a URL to SCM!");
    }

    String url = scm.get();
    LOGGER.info("SCM is {}", url);

    if (!isOnGitHub(url)) {
      LOGGER.info("But unfortunately I can work only with projects that stay on GitHub ...");
      LOGGER.info("Let me try to find a mirror on GitHub ...");

      Optional<GitHubProject> mirror = finder.tryToFindGitHubProjectFor(gav);
      if (!mirror.isPresent()) {
        throw new IOException("Oh no! I could not find a mirror on GitHub!");
      }

      url = mirror.get().scm().toString();

      LOGGER.info("Yup, that seems to be a corresponding project on GitHub:");
      LOGGER.info("  {}", url);
    }

    processUrl(url, fetcher, githubToken, callback);
  }

  /**
   * Calculate a rating for projects specified in a config.
   *
   * @param filename A path to the config.
   * @param fetcher An interface for accessing the GitHub.
   * @param githubToken A token for accessing the GitHub APIs.
   * @param callback An interface for interacting with a user.
   * @throws IOException If something went wrong.
   */
  private void processConfig(
      String filename, GitHubDataFetcher fetcher, String githubToken, UserCallback callback)
      throws IOException {

    LOGGER.info("Loading config from {}", filename);
    Config config = config(filename);

    // try to create reporters earlier to catch a possible misconfiguration
    // before calculating ratings
    final List<Reporter<GitHubProject>> reporters = makeReporters(config);

    LOGGER.info("Look for projects ...");
    List<GitHubProject> projects = new GitHubProjectFinder(fetcher.github())
        .set(config.finderConfig)
        .run();
    LOGGER.info("Found {} project{}", projects.size(), projects.size() > 1 ? "s" : "");
    for (GitHubProject project : projects) {
      LOGGER.info("  {}", project.scm());
    }

    String projectCacheFile = projectCacheFile(config);

    LOGGER.info("Starting calculating ratings ...");
    MultipleSecurityRatingsCalculator calculator =
        (MultipleSecurityRatingsCalculator) new MultipleSecurityRatingsCalculator(fetcher, nvd)
            .set(loadProjectCache(projectCacheFile))
            .storeProjectCacheTo(projectCacheFile)
            .set(VALUE_CACHE)
            .set(callback)
            .token(githubToken)
            .calculateFor(projects);

    LOGGER.info("Okay, we've done calculating the ratings");

    List<GitHubProject> failedProjects = calculator.failedProjects();
    if (!failedProjects.isEmpty()) {
      LOGGER.warn("Ratings couldn't be calculated for {} project{}",
          failedProjects.size(), failedProjects.size() == 1 ? "" : "s");
      for (GitHubProject project : projects) {
        LOGGER.info("    {}", project.scm());
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
    try (InputStream is = Files.newInputStream(Paths.get(filename))) {
      return config(is);
    }
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
        LOGGER.warn("Something went wrong: {}", e.getMessage());
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
