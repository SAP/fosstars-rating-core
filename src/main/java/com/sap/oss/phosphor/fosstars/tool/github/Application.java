package com.sap.oss.phosphor.fosstars.tool.github;

import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.VERSION;
import static com.sap.oss.phosphor.fosstars.model.subject.oss.GitHubProject.isOnGitHub;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.packageurl.MalformedPackageURLException;
import com.github.packageurl.PackageURL;
import com.sap.oss.phosphor.fosstars.advice.Advisor;
import com.sap.oss.phosphor.fosstars.advice.oss.github.OssSecurityGithubAdvisor;
import com.sap.oss.phosphor.fosstars.data.DataProvider;
import com.sap.oss.phosphor.fosstars.data.NoUserCallback;
import com.sap.oss.phosphor.fosstars.data.Terminal;
import com.sap.oss.phosphor.fosstars.data.UserCallback;
import com.sap.oss.phosphor.fosstars.data.github.GitHubDataFetcher;
import com.sap.oss.phosphor.fosstars.model.Rating;
import com.sap.oss.phosphor.fosstars.model.RatingRepository;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.ValueSet;
import com.sap.oss.phosphor.fosstars.model.rating.oss.OssArtifactSecurityRating;
import com.sap.oss.phosphor.fosstars.model.rating.oss.OssRulesOfPlayRating;
import com.sap.oss.phosphor.fosstars.model.rating.oss.OssSecurityRating;
import com.sap.oss.phosphor.fosstars.model.subject.oss.GitHubProject;
import com.sap.oss.phosphor.fosstars.model.value.ValueHashSet;
import com.sap.oss.phosphor.fosstars.nvd.NVD;
import com.sap.oss.phosphor.fosstars.tool.InputString;
import com.sap.oss.phosphor.fosstars.tool.Reporter;
import com.sap.oss.phosphor.fosstars.tool.YesNoQuestion;
import com.sap.oss.phosphor.fosstars.tool.YesNoQuestion.Answer;
import com.sap.oss.phosphor.fosstars.tool.format.Formatter;
import com.sap.oss.phosphor.fosstars.tool.format.OssArtifactSecurityRatingMarkdownFormatter;
import com.sap.oss.phosphor.fosstars.tool.format.OssRulesOfPlayRatingMarkdownFormatter;
import com.sap.oss.phosphor.fosstars.tool.format.OssSecurityRatingMarkdownFormatter;
import com.sap.oss.phosphor.fosstars.tool.format.PrettyPrinter;
import com.sap.oss.phosphor.fosstars.tool.github.Application.ReportConfig.ReportType;
import com.sap.oss.phosphor.fosstars.util.Json;
import com.sap.oss.phosphor.fosstars.util.Yaml;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
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
 * This is a command line tool for calculating security ratings for one
 * of multiple open-source projects.
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
  private static final Advisor ADVISOR = new OssSecurityGithubAdvisor();

  /**
   * Maps an alias to a rating procedure.
   */
  private static final Map<String, Rating> RATINGS = new HashMap<>();

  static {
    Rating ossSecurityRating = RatingRepository.INSTANCE.rating(OssSecurityRating.class);
    RATINGS.put("default", ossSecurityRating);
    RATINGS.put("security", ossSecurityRating);
    RATINGS.put(ossSecurityRating.getClass().getSimpleName(), ossSecurityRating);
    RATINGS.put(ossSecurityRating.getClass().getCanonicalName(), ossSecurityRating);

    Rating ossRulesOfPlay = RatingRepository.INSTANCE.rating(OssRulesOfPlayRating.class);
    RATINGS.put("oss-rules-of-play", ossRulesOfPlay);
    RATINGS.put(ossRulesOfPlay.getClass().getSimpleName(), ossRulesOfPlay);
    RATINGS.put(ossRulesOfPlay.getClass().getCanonicalName(), ossRulesOfPlay);

    Rating ossArtifactSecurityRating =
        RatingRepository.INSTANCE.rating(OssArtifactSecurityRating.class);
    RATINGS.put("oss-artifact-security", ossArtifactSecurityRating);
    RATINGS.put(ossArtifactSecurityRating.getClass().getSimpleName(), ossArtifactSecurityRating);
    RATINGS.put(ossArtifactSecurityRating.getClass().getCanonicalName(), ossArtifactSecurityRating);
  }

  /**
   * A set of supported PURL types.
   *
   * @see <a href="https://github.com/package-url/purl-spec#known-purl-types">Known PURL types</a>
   */
  private enum SupportedPurlTypes {
    GITHUB, MAVEN
  }

  /**
   * Entry point.
   *
   * @param args Command-line parameters.
   */
  public static void main(String... args) {
    try {
      new Application(args).run();
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
   * A rating to use.
   */
  private final Rating rating;

  /**
   * An interface for accessing GitHub.
   */
  private final GitHubDataFetcher fetcher;

  /**
   * A calculator for security ratings.
   */
  private final SingleRatingCalculator calculator;

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
  Application(String... args) throws IOException {
    options = new Options();
    options.addOption("h", "help", false,
        "Print this message.");
    options.addOption("i", "interactive", false,
        "Ask a question if a feature can't be automatically gathered.");
    options.addOption(
        Option.builder("r")
            .longOpt("rating")
            .hasArg()
            .desc("A rating to use: security, oss-artifact-security, oss-rules-of-play "
                + "(default is security)")
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
            .desc("Store a report to a specified file.")
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
    group.addOption(Option.builder("p")
        .required()
        .hasArg()
        .longOpt("purl")
        .desc("The PURL of a project.")
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

    checkOptionsIn(commandLine);

    String ratingName = commandLine.getOptionValue("r", "default");
    rating = RATINGS.get(ratingName);
    if (rating == null) {
      throw new IllegalArgumentException(String.format("Could not find a rating '%s'", ratingName));
    }

    if (commandLine.hasOption("v")) {
      LoggerContext context = (LoggerContext) LogManager.getContext(false);
      context.getRootLogger().setLevel(Level.DEBUG);
      context.updateLoggers();
    }

    UserCallback callback = commandLine.hasOption("interactive")
        ? new Terminal() : NoUserCallback.INSTANCE;

    String githubToken = commandLine.getOptionValue("token", "");
    List<String> withConfigs = Arrays.asList(
        commandLine.getOptionValue("data-provider-configs", "")
            .split("\\s+,\\s+,"));

    fetcher = new GitHubDataFetcher(connectToGithub(githubToken, callback), githubToken);
    DataProviderSelector dataProviderSelector = new DataProviderSelector(fetcher, new NVD());
    dataProviderSelector.configure(withConfigs);
    List<DataProvider<GitHubProject>> providers = dataProviderSelector.providersFor(rating);

    calculator = new SingleRatingCalculator(rating, providers);
    calculator.set(VALUE_CACHE);
    calculator.set(callback);

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

    Path path = Paths.get(FOSSTARS_DIRECTORY);
    if (!Files.exists(path)) {
      Files.createDirectories(path);
    }

    try {
      if (commandLine.hasOption("url")) {
        processUrl(commandLine.getOptionValue("url"));
      }

      if (commandLine.hasOption("gav")) {
        processGav(commandLine.getOptionValue("gav"));
      }

      if (commandLine.hasOption("config")) {
        processConfig(commandLine.getOptionValue("config"));
      }

      if (commandLine.hasOption("purl")) {
        processPurl(commandLine.getOptionValue("purl"));
      }
    } finally {
      VALUE_CACHE.store(PATH_TO_VALUE_CACHE);
    }
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

    if (!commandLine.hasOption("url") && !commandLine.hasOption("config")
        && !commandLine.hasOption("gav") && !commandLine.hasOption("purl")) {

      throw new IllegalArgumentException(
          "You have to give me either --url, --gav, --purl or --config option!");
    }

    if (commandLine.hasOption("report-type") && !commandLine.hasOption("report-file")) {
      throw new IllegalArgumentException(
          "The option --report-type has to be used with --report-file");
    }

    if (commandLine.hasOption("report-type")
        && !Arrays.asList("text", "markdown").contains(commandLine.getOptionValue("report-type"))) {

      throw new IllegalArgumentException(
          String.format("Unknown report type: %s", commandLine.getOptionValue("report-type")));
    }
  }

  /**
   * Calculate a rating for a single project identified by a URL to its SCM.
   *
   * @param url A URL of the project repository.
   * @throws IOException If something went wrong.
   */
  private void processUrl(String url) throws IOException {
    processUrl(url, ValueHashSet.empty());
  }

  /**
   * Calculate a rating for a single project identified by a URL to its SCM.
   *
   * @param url A URL of the project repository.
   * @param knownValues values which are known and should not be collected by a data provider
   * @throws IOException If something went wrong.
   */
  private void processUrl(String url, ValueSet knownValues) throws IOException {
    GitHubProject project = GitHubProject.parse(url);
    calculator.calculateFor(project, knownValues);

    if (!project.ratingValue().isPresent()) {
      throw new IOException("Could not calculate a rating!");
    }

    Arrays.stream(prettyPrinter.print(project).split("\n")).forEach(LOGGER::info);
    LOGGER.info("");

    storeReportIfRequested(project, commandLine);
  }

  /**
   * Calculate a rating for a single project identified by GAV coordinates.
   *
   * @param gav The GAV coordinates.
   * @throws IOException If something went wrong.
   */
  private void processGav(String gav) throws IOException {
    MavenScmFinder finder = new MavenScmFinder();
    LOGGER.info("Start with GAV {}", gav);

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

    // maybe the parsing of the GAV should be moved to separate class
    // as it is also done in `finder.findScmFor(gav)`.
    String[] parts = gav.trim().split(":");
    String version = parts.length > 2 ? parts[2] : "";
    ValueSet values = new ValueHashSet(VERSION.value(version));

    processUrl(url, values);
  }

  /**
   * Calculate a rating for projects specified in a config.
   *
   * @param filename A path to the config.
   * @throws IOException If something went wrong.
   */
  private void processConfig(String filename) throws IOException {
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
    MultipleRatingsCalculator multipleRatingsCalculator =
        new MultipleRatingsCalculator(calculator)
            .set(loadProjectCache(projectCacheFile))
            .storeProjectCacheTo(projectCacheFile)
            .calculateFor(projects);

    LOGGER.info("Okay, we've done calculating the ratings");

    List<GitHubProject> failedProjects = multipleRatingsCalculator.failedProjects();
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
   * Calculate a rating for a single project identified by a PURL (package url).
   *
   * @param purl The PURL.
   * @throws IOException If something went wrong.
   */
  private void processPurl(String purl) throws IOException {
    try {
      LOGGER.info("Start with PURL {}", purl);
      PackageURL parsedPurl = new PackageURL(purl);

      switch (SupportedPurlTypes.valueOf(parsedPurl.getType().toUpperCase())) {
        case GITHUB:
          String projectUrl = String.format("https://github.com/%s/%s",
              parsedPurl.getNamespace(), parsedPurl.getName());
          LOGGER.info("Found github PURL and start with {}", projectUrl);
          Value<String> versionValue;
          if (parsedPurl.getVersion() == null) {
            versionValue = VERSION.unknown();
          } else {
            versionValue = VERSION.value(parsedPurl.getVersion());
          }
          processUrl(projectUrl, new ValueHashSet(versionValue));
          break;
        case MAVEN:
          String gav = String.format("%s:%s%s",
              parsedPurl.getNamespace(), parsedPurl.getName(),
              parsedPurl.getVersion() == null ? "" : ":" + parsedPurl.getVersion());
          processGav(gav);
          break;
        default:
          throw new IOException(String.format(
              "Oh no! Unexpected PURL type: '%s' is not supported! Supported types are %s",
              parsedPurl.getType(), Arrays.toString(SupportedPurlTypes.values())));
      }
    } catch (MalformedPackageURLException e) {
      throw new IOException("Oh no! Given PURL could not be parsed!", e);
    }
  }

  /**
   * Stores a rating of a project if a user asked about it.
   *
   * @param project The project.
   * @param commandLine Command-line options.
   * @throws IOException If something went wrong.
   */
  private void storeReportIfRequested(GitHubProject project, CommandLine commandLine)
      throws IOException {

    if (!project.ratingValue().isPresent()) {
      throw new IOException("Could not calculate a rating!");
    }

    if (commandLine.hasOption("report-file")) {
      String type = commandLine.getOptionValue("report-type", "text");
      String file = commandLine.getOptionValue("report-file");
      Formatter formatter = createFormatter(type);

      LOGGER.info("Storing a report to {} ({})", file, type);

      Files.write(
          Paths.get(file),
          formatter.print(project).getBytes(StandardCharsets.UTF_8));
    }

    if (commandLine.hasOption("raw-rating-file")) {
      String file = commandLine.getOptionValue("raw-rating-file");
      LOGGER.info("Storing a raw rating to {}", file);
      Files.write(Paths.get(file), Json.toBytes(project.ratingValue().get()));
    }
  }

  /**
   * Creates a formatter for a specified type.
   *
   * @param type The type.
   * @return A formatter.
   * @throws IllegalArgumentException If the type is unknown.
   */
  private Formatter createFormatter(String type) throws IOException {
    switch (type) {
      case "text":
        return PrettyPrinter.withVerboseOutput(ADVISOR);
      case "markdown":
        if (rating instanceof OssSecurityRating) {
          return new OssSecurityRatingMarkdownFormatter(ADVISOR);
        }
        if (rating instanceof OssRulesOfPlayRating) {
          return new OssRulesOfPlayRatingMarkdownFormatter();
        }
        if (rating instanceof OssArtifactSecurityRating) {
          return new OssArtifactSecurityRatingMarkdownFormatter(ADVISOR);
        }
        throw new IllegalArgumentException("No markdown formatter for the rating!");
      default:
        throw new IllegalArgumentException(String.format("Unknown report type: %s", type));
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
   * The method returns a list with only {@link Reporter#dummy()} if the config is null.
   *
   * @param config The config.
   * @return A reporter.
   * @throws IllegalArgumentException If the type is unknown.
   */
  private List<Reporter<GitHubProject>> makeReporters(Config config) throws IOException {
    Objects.requireNonNull(config, "Oh no! Config is null!");
    if (config.reportConfigs == null) {
      return Collections.singletonList(Reporter.dummy());
    }

    List<Reporter<GitHubProject>> reporters = new ArrayList<>();
    for (ReportConfig reportConfig : config.reportConfigs) {
      reporters.add(reporterFrom(reportConfig));
    }

    return reporters;
  }

  /**
   * Create a reporter from a specified report config.
   *
   * @param reportConfig The config.
   * @return A reporter.
   * @throws IOException If something went wrong.
   */
  private Reporter<GitHubProject> reporterFrom(ReportConfig reportConfig) throws IOException {
    Objects.requireNonNull(reportConfig.type, "Hey! Reporter type can't be null!");

    if (reportConfig.type == ReportType.JSON) {
      return new MergedJsonReporter(reportConfig.where);
    }

    if (reportConfig.type == ReportType.MARKDOWN && rating instanceof OssSecurityRating) {
      return new OssSecurityRatingMarkdownReporter(
          reportConfig.where, reportConfig.source, (OssSecurityRating) rating, ADVISOR);
    }

    if (reportConfig.type == ReportType.MARKDOWN && rating instanceof OssRulesOfPlayRating) {
      return new OssRulesOfPlayMarkdownReporter(reportConfig.where);
    }

    throw new IllegalArgumentException(String.format(
        "Oh no! That's an unknown type of report: %s", reportConfig.type));
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
    ObjectMapper mapper = Yaml.mapper();
    mapper.enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS);
    return mapper.readValue(is, Config.class);
  }

  /**
   * The class holds a configuration for {@link Application}.
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
