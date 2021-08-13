package com.sap.oss.phosphor.fosstars.tool;

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.joining;

import com.sap.oss.phosphor.fosstars.data.NoUserCallback;
import com.sap.oss.phosphor.fosstars.data.StandardValueCache;
import com.sap.oss.phosphor.fosstars.data.SubjectValueCache;
import com.sap.oss.phosphor.fosstars.data.Terminal;
import com.sap.oss.phosphor.fosstars.data.UserCallback;
import com.sap.oss.phosphor.fosstars.data.github.GitHubDataFetcher;
import com.sap.oss.phosphor.fosstars.tool.YesNoQuestion.Answer;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;
import org.kohsuke.github.HttpConnector;
import org.kohsuke.github.extras.ImpatientHttpConnector;

/**
 * This is a command-line tool for calculating ratings.
 */
public class Application {

  /**
   * A logger.
   */
  private static final Logger LOGGER = LogManager.getLogger(Application.class);

  /**
   * A directory where the tool stores stuff.
   */
  private static final String FOSSTARS_DIRECTORY = ".fosstars";

  /**
   * A path to cache.
   */
  private static final String SUBJECT_VALUE_CACHE_FILE
      = FOSSTARS_DIRECTORY + File.separator + "github_project_value_cache.json";

  /**
   * A usage message.
   */
  private static final String USAGE =
      "java -jar fosstars-github-rating-calc.jar [options]";

  /**
   * A list of handlers for specific ratings.
   */
  private final Handler[] handlers;

  /**
   * The default handler.
   */
  private final Handler defaultHandler;

  /**
   * Entry point.
   *
   * @param args Command-line parameters.
   */
  public static void main(String... args) {
    try {
      new Application().run(args);
    } catch (Exception e) {
      LOGGER.error("Something went wrong!", e);
      LOGGER.error("Bye!");
      System.exit(1);
    }
    LOGGER.info("Bye!");
  }

  /**
   * Initialize CLI.
   *
   * @throws IOException If something went wrong.
   */
  public Application() throws IOException {
    handlers = new Handler[] {
        new OssProjectSecurityRatingHandler(),
        new OssArtifactSecurityRatingHandler(),
        new OssRulesOfPlayRatingHandler(),
        new SecurityRiskIntroducedByOssHandler()
    };
    defaultHandler = handlers[0];
  }

  /**
   * Prepare supported command-line options.
   *
   * @return Command-line options.
   */
  private Options commandLineOptions() {
    Options options = new Options();
    options.addOption("h", "help", false,
        "Print this message.");
    options.addOption("i", "interactive", false,
        "Ask a question if a feature can't be automatically gathered.");
    options.addOption(
        Option.builder("r")
            .longOpt("rating")
            .hasArg()
            .desc(format("A rating to use: %s (default is %s).",
                Arrays.stream(handlers).map(Handler::supportedRatingName).collect(joining(", ")),
                defaultHandler.supportedRatingName()))
            .build());
    options.addOption(
        Option.builder("t")
            .longOpt("token")
            .hasArg()
            .desc("An access token for the GitHub API.")
            .build());
    options.addOption(
        Option.builder("v")
            .longOpt("verbose")
            .desc("Print all the details.")
            .build());
    options.addOption(
        Option.builder()
            .longOpt("report-file")
            .hasArg()
            .argName("path")
            .desc("A path to a report.")
            .build());
    options.addOption(
        Option.builder()
            .longOpt("report-type")
            .hasArg()
            .argName("format")
            .desc("Format of the report (text or markdown).")
            .build());
    options.addOption(
        Option.builder()
            .longOpt("create-issues")
            .desc("Create GitHub issues for the respective repository in case of findings.")
            .build());
    options.addOption(
        Option.builder()
            .longOpt("raw-rating-file")
            .hasArg()
            .argName("path")
            .desc("Store a raw rating to a specified file.")
            .build());
    options.addOption(
        Option.builder()
            .longOpt("data-provider-configs")
            .hasArg()
            .desc("A list of YAML config files for data providers, format: ProviderClassName.yaml")
            .valueSeparator(',')
            .build());
    options.addOption(
        Option.builder("cl")
            .longOpt("cleanup")
            .desc("Clean up locally stored data while rating calculation.")
            .build());

    OptionGroup group = new OptionGroup();
    group.addOption(
        Option.builder("u")
            .hasArg()
            .longOpt("url")
            .desc("A URL to project's SCM")
            .build());
    group.addOption(
        Option.builder("g")
            .hasArg()
            .longOpt("gav")
            .desc("GAV coordinates of a jar artifact in the format 'G:A:V' or 'G:A' "
                + "where G is a group id, A is an artifact if, and V is an optional version.")
            .build());
    group.addOption(
        Option.builder("n")
            .hasArg()
            .longOpt("npm")
            .desc("A name of NPM package.")
            .build());
    group.addOption(
        Option.builder("p")
            .hasArg()
            .longOpt("purl")
            .desc("The PURL of a project.")
            .build());
    group.addOption(
        Option.builder("c")
            .longOpt("config")
            .hasArg()
            .desc("A path to a config file.")
            .build());
    group.addOption(
        Option.builder("pom")
            .hasArg()
            .longOpt("pom")
            .desc("A path to a Maven POM file.")
            .build());
    options.addOptionGroup(group);

    for (Handler handler : handlers) {
      handler.options().forEach(options::addOption);
      handler.optionGroups().forEach(options::addOptionGroup);
    }

    return options;
  }

  /**
   * Look for a handler for a rating.
   *
   * @param rating The rating.
   * @return A handler for the rating.
   * @throws IllegalArgumentException If no handler found for the rating.
   */
  private Handler handlerFor(String rating) {
    requireNonNull(rating, "Oops! Rating is null!");

    if ("default".equals(rating)) {
      return defaultHandler;
    }

    for (Handler handler : handlers) {
      if (rating.equals(handler.supportedRatingName())) {
        return handler;
      }
    }

    throw new IllegalArgumentException(format("Oops! Unknown rating: %s", rating));
  }

  /**
   * Run the tool with a list of command-line parameters.
   *
   * @param args Command-line parameters.
   * @throws IOException If something went wrong.
   */
  void run(String... args) throws Exception {
    Options options = commandLineOptions();

    CommandLine commandLine;
    try {
      commandLine = new DefaultParser().parse(options, args);
    } catch (ParseException e) {
      HelpFormatter formatter = new HelpFormatter();
      formatter.printHelp(USAGE, options);
      throw new IllegalArgumentException("Could not parse command-line parameters", e);
    }

    if (commandLine.hasOption("h")) {
      HelpFormatter formatter = new HelpFormatter();
      formatter.printHelp(USAGE, options);
      return;
    }

    if (commandLine.hasOption("v")) {
      LoggerContext context = (LoggerContext) LogManager.getContext(false);
      context.getRootLogger().setLevel(Level.DEBUG);
      context.updateLoggers();
    }

    checkOptionsIn(commandLine);

    String rating = commandLine.getOptionValue("r", "default");
    Handler handler = handlerFor(rating);
    UserCallback callback = commandLine.hasOption("interactive")
        ? new Terminal() : NoUserCallback.INSTANCE;

    String githubToken = commandLine.getOptionValue("token", "");

    GitHubDataFetcher fetcher
        = new GitHubDataFetcher(connectToGithub(githubToken, callback), githubToken);

    List<String> withConfigs = asList(
        commandLine.getOptionValue("data-provider-configs", "").split(","));

    Path path = Paths.get(FOSSTARS_DIRECTORY);
    if (!Files.exists(path)) {
      Files.createDirectories(path);
    }

    SubjectValueCache cache = loadValueCache();
    try {
      handler.configureDataProviders(withConfigs)
          .baseDirectory(FOSSTARS_DIRECTORY)
          .configureDataProviders(withConfigs)
          .set(cache)
          .set(commandLine)
          .set(callback)
          .set(fetcher)
          .run();
    } finally {
      cache.store(SUBJECT_VALUE_CACHE_FILE);
    }
  }

  /**
   * Initializes a value cache.
   *
   * @return The value cache.
   */
  private SubjectValueCache loadValueCache() {
    try {
      return new SubjectValueCache(StandardValueCache.load(SUBJECT_VALUE_CACHE_FILE));
    } catch (FileNotFoundException e) {
      LOGGER.info("The default value cache doesn't exist yet: {}", SUBJECT_VALUE_CACHE_FILE);
    } catch (IOException e) {
      LOGGER.warn("Could not load the default value cache!", e);
    }
    return new SubjectValueCache();
  }

  /**
   * Checks command-line options and throws an exception if something is wrong.
   *
   * @param commandLine The command-line options.
   * @throws IllegalArgumentException If the options are invalid.
   */
  private static void checkOptionsIn(CommandLine commandLine) {
    if (commandLine.hasOption("h")) {
      return;
    }

    if (commandLine.hasOption("report-type") && !commandLine.hasOption("report-file")) {
      throw new IllegalArgumentException(
          "The option --report-type has to be used with --report-file");
    }

    if (commandLine.hasOption("report-type")
        && !asList("text", "markdown").contains(commandLine.getOptionValue("report-type"))) {

      throw new IllegalArgumentException(
          format("Unknown report type: %s", commandLine.getOptionValue("report-type")));
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
              format("Not sure what I can do with '%s'", answer));
      }
    }

    ImpatientHttpConnector connector = new ImpatientHttpConnector(HttpConnector.DEFAULT);

    List<Exception> suppressed = new ArrayList<>();
    if (token != null) {
      LOGGER.info("Okay, we have a GitHub token, let's try to use it");
      try {
        return new GitHubBuilder()
            .withConnector(connector)
            .withOAuthToken(token)
            .build();
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

}
