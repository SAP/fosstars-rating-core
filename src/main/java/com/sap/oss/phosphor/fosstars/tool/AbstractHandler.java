package com.sap.oss.phosphor.fosstars.tool;

import static java.lang.String.format;
import static java.util.Collections.emptyList;
import static java.util.Objects.requireNonNull;
import static java.util.Optional.empty;

import com.github.packageurl.PackageURL;
import com.github.packageurl.PackageURL.StandardTypes;
import com.sap.oss.phosphor.fosstars.data.DataProvider;
import com.sap.oss.phosphor.fosstars.data.DataProviderSelector;
import com.sap.oss.phosphor.fosstars.data.NoUserCallback;
import com.sap.oss.phosphor.fosstars.data.SubjectValueCache;
import com.sap.oss.phosphor.fosstars.data.UserCallback;
import com.sap.oss.phosphor.fosstars.data.github.GitHubDataFetcher;
import com.sap.oss.phosphor.fosstars.data.github.GitHubDataFetcher.CleanupStrategy;
import com.sap.oss.phosphor.fosstars.model.Rating;
import com.sap.oss.phosphor.fosstars.model.Subject;
import com.sap.oss.phosphor.fosstars.model.subject.oss.GitHubProject;
import com.sap.oss.phosphor.fosstars.nvd.NVD;
import com.sap.oss.phosphor.fosstars.tool.format.Formatter;
import com.sap.oss.phosphor.fosstars.tool.format.PrettyPrinter;
import com.sap.oss.phosphor.fosstars.tool.report.Reporter;
import com.sap.oss.phosphor.fosstars.util.Json;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * A base class for command-line handlers.
 */
public abstract class AbstractHandler implements Handler {

  /**
   * No configuration file.
   */
  static final Config NO_CONFIG = null;

  /**
   * Maps a command-line option for subjects to a processor.
   */
  final Map<String, Processor> router = new HashMap<>();

  /**
   * A logger.
   */
  final Logger logger = LogManager.getLogger(getClass());

  /**
   * An interface to NVD.
   */
  final NVD nvd = new NVD();

  /**
   * A rating that the handler calculates.
   */
  final Rating rating;

  /**
   * Parsed command-line parameters.
   */
  CommandLine commandLine;

  /**
   * A list of configs for data providers.
   */
  List<String> withDataProviderConfigs = emptyList();

  /**
   * An interface to GitHub.
   */
  GitHubDataFetcher fetcher;

  /**
   * A user callback.
   */
  UserCallback callback = NoUserCallback.INSTANCE;

  /**
   * A cache of values for subjects.
   */
  SubjectValueCache cache = new SubjectValueCache();

  /**
   * A base directory.
   */
  String baseDirectory = "./";

  /**
   * Create a handler.
   *
   * @param rating A rating that the handler calculates.
   */
  AbstractHandler(Rating rating) {
    this.rating = requireNonNull(rating, "Oops! Rating is null!");
    this.router.put("--url", this::processUrl);
    this.router.put("--gav", this::processMaven);
    this.router.put("--npm", this::processNpm);
    this.router.put("--purl", this::processPurl);
    this.router.put("--config", this::processConfig);
    this.router.put("--pom", this::processPom);
  }

  /**
   * Creates a rating calculator that calculates a rating for a single subject.
   *
   * @throws IOException If something went wrong.
   */
  SingleRatingCalculator calculator() throws IOException {
    List<DataProvider> providers = dataProviderSelector().providersFor(rating);
    SingleRatingCalculator calculator
        = new SingleRatingCalculator(rating, providers).set(cache).set(callback);

    if (commandLine.hasOption("cleanup")) {
      calculator.doAfter(subject -> {
        if (subject instanceof GitHubProject) {
          GitHubProject project = (GitHubProject) subject;
          CleanupStrategy processedRepository = (url, info, total) -> project.scm().equals(url);
          try {
            fetcher.cleanup(processedRepository);
          } catch (IOException e) {
            logger.warn("Oops! Could not clean up!", e);
          }
        }
      });
    }

    return calculator;
  }

  /**
   * Creates a selector for data providers.
   *
   * @return A selector for data providers.
   * @throws IOException If something went wrong.
   */
  DataProviderSelector dataProviderSelector() throws IOException {
    requireNonNull(fetcher, "Oops! Fetcher is not set!");
    DataProviderSelector selector = new DataProviderSelector(fetcher, nvd);
    selector.configure(withDataProviderConfigs);
    return selector;
  }

  @Override
  public List<Option> options() {
    return emptyList();
  }

  @Override
  public List<OptionGroup> optionGroups() {
    return emptyList();
  }

  @Override
  public Handler configureDataProviders(List<String> withDataProviderConfigs) {
    requireNonNull(withDataProviderConfigs, "Oops! Data provider configs are null!");
    this.withDataProviderConfigs = new ArrayList<>(withDataProviderConfigs);
    return this;
  }

  @Override
  public Handler baseDirectory(String path) {
    requireNonNull(path, "Oops! Path is null!");
    this.baseDirectory = path;
    return this;
  }

  @Override
  public AbstractHandler set(CommandLine commandLine) {
    requireNonNull(commandLine, "Oops! Command-line options are null!");
    this.commandLine = commandLine;
    return this;
  }

  @Override
  public Handler set(UserCallback callback) {
    requireNonNull(callback, "Oops! Callback is null!");
    this.callback = callback;
    return this;
  }

  @Override
  public Handler set(GitHubDataFetcher fetcher) {
    requireNonNull(fetcher, "Oops! Fetcher is null!");
    this.fetcher = fetcher;
    return this;
  }

  @Override
  public Handler set(SubjectValueCache cache) {
    requireNonNull(cache, "Oops! Cache is null!");
    this.cache = cache;
    return this;
  }

  @Override
  public final Handler run() throws Exception {
    List<String> options = router.keySet().stream()
        .filter(option -> commandLine.hasOption(option))
        .collect(Collectors.toList());

    if (options.isEmpty()) {
      throw new IllegalArgumentException(format(
          "You have to give me one of the following options: %s",
          String.join(", ", router.keySet())));
    }

    if (options.size() > 1) {
      throw new IllegalArgumentException(
          format("Oops! %s cannot be used together!", String.join(", ", options)));
    }

    String option = options.get(0);
    Processor processor = router.get(option);

    if (processor == null || !supportedSubjectOptions().contains(option)) {
      throw new IllegalArgumentException(format("%s is not supported!", option));
    }

    processor.run(commandLine.getOptionValue(StringUtils.strip(option, "-")));

    return this;
  }

  /**
   * Returns a set of command-line options for specifying subjects.
   *
   * @return A set of command-line options for specifying subjects.
   */
  abstract Set<String> supportedSubjectOptions();

  /**
   * Calculate a rating for a single subject identified by a URL to its SCM.
   *
   * @param url A URL of the project repository.
   * @return The processed {@link Subject}.
   * @throws IOException If something went wrong.
   */
  void processUrl(String url) throws IOException {
    process(GitHubProject.parse(url));
  }

  /**
   * Process a GitHub project.
   *
   * @param project The project.
   * @throws IOException If something went wrong.
   */
  void process(GitHubProject project) throws IOException {
    calculator().calculateFor(project);

    if (!project.ratingValue().isPresent()) {
      throw new IOException("Could not calculate a rating!");
    }

    Arrays.stream(createFormatter("text").print(project).split("\n")).forEach(logger::info);
    logger.info("");
    storeReportIfRequested(project, commandLine);
  }

  /**
   * Calculate ratings for a number of projects.
   *
   * @param projects The projects.
   * @param reporters A list of reporters to be applied to the calculated ratings.
   * @param projectCacheFile A path to projects cache.
   * @throws IOException If something went wrong.
   */
  void process(List<GitHubProject> projects, List<Reporter<GitHubProject>> reporters,
      String projectCacheFile) throws IOException {

    logger.info("Starting calculating ratings ...");
    MultipleRatingsCalculator multipleRatingsCalculator =
        new MultipleRatingsCalculator(calculator())
            .set(loadSubjectCache(projectCacheFile))
            .storeCacheTo(projectCacheFile)
            .calculateFor(projects);

    logger.info("Okay, we've done calculating the ratings");

    List<Subject> failedSubjects = multipleRatingsCalculator.failedSubjects();
    if (!failedSubjects.isEmpty()) {
      logger.warn("Ratings couldn't be calculated for {} project{}",
          failedSubjects.size(), failedSubjects.size() == 1 ? "" : "s");
      for (GitHubProject project : projects) {
        logger.info("    {}", project.scm());
      }
    }

    if (!reporters.isEmpty()) {
      logger.info("Now let's generate reports");
      for (Reporter<GitHubProject> reporter : reporters) {
        reporter.runFor(projects);
      }
    }
  }

  /**
   * Process a Maven artifact.
   *
   * @param coordinates GAV coordinates.
   * @throws Exception If something went wrong.
   */
  void processMaven(String coordinates) throws Exception {
    throw new UnsupportedOperationException("Oops! I don't support GAV!");
  }

  /**
   * Process an NPM package.
   *
   * @param identifier The package's identifier.
   * @throws Exception If something went wrong.
   */
  void processNpm(String identifier) throws Exception {
    throw new UnsupportedOperationException("Oops! I don't support NPM!");
  }

  /**
   * Calculate a rating for a single project identified by a PURL.
   *
   * @param packageUrl The PURL.
   * @throws IOException If something went wrong.
   */
  void processPurl(String packageUrl) throws Exception {
    PackageURL purl = new PackageURL(packageUrl);

    switch (purl.getType().toLowerCase()) {
      case StandardTypes.GITHUB:
        if (purl.getNamespace() == null) {
          throw new IllegalArgumentException("Oops! No namespace in the PURL!");
        }
        if (purl.getName() == null) {
          throw new IllegalArgumentException("Oops! No name in the PURL!");
        }
        String url = format("https://github.com/%s/%s", purl.getNamespace(), purl.getName());
        processUrl(url);
        break;
      case StandardTypes.MAVEN:
        if (purl.getNamespace() == null) {
          throw new IllegalArgumentException("Oops! No namespace in the PURL!");
        }
        if (purl.getName() == null) {
          throw new IllegalArgumentException("Oops! No name in the PURL!");
        }
        String coordinates = format("%s:%s", purl.getNamespace(), purl.getName());
        if (purl.getVersion() != null) {
          coordinates += ":" + purl.getVersion();
        }
        processMaven(coordinates);
        break;
      case StandardTypes.NPM:
        if (purl.getName() == null) {
          throw new IllegalArgumentException("Oops! No name in the PURL!");
        }
        String identifier = format("%s", purl.getName());
        if (purl.getVersion() != null) {
          identifier += "@" + purl.getVersion();
        }
        processNpm(identifier);
        break;
      default:
        throw new IOException(format("Oh no! Unsupported PURL type: '%s'", purl.getType()));
    }
  }

  /**
   * Calculate a rating for projects specified in a config.
   *
   * @param filename A path to the config.
   * @throws IOException If something went wrong.
   */
  void processConfig(String filename) throws IOException {
    logger.info("Loading config from {}", filename);
    Config config = Config.from(filename);

    // try to create reporters earlier to catch a possible misconfiguration
    // before calculating ratings
    final List<Reporter<GitHubProject>> reporters = makeReporters(config);

    logger.info("Look for projects ...");
    List<GitHubProject> projects = new GitHubProjectFinder(fetcher.github())
        .set(config.finderConfig)
        .run();
    logger.info("Found {} project{}", projects.size(), projects.size() > 1 ? "s" : "");
    for (GitHubProject project : projects) {
      logger.info("  {}", project.scm());
    }

    String projectCacheFile = projectCacheFile(config);
    process(projects, reporters, projectCacheFile);
  }

  /**
   * Calculate ratings for dependencies in a POm file.
   *
   * @param filename A path to the POM file.
   * @throws IOException If something went wrong.
   */
  void processPom(String filename) throws Exception {
    throw new UnsupportedOperationException("Oops! I don't support Maven POM!");
  }

  /**
   * Loads a cache of projects from a file.
   * If the file doesn't exist, then the method returns an empty cache.
   *
   * @param filename A path to the file.
   * @return A loaded cache of projects.
   * @throws IOException If something went wrong.
   */
  SubjectCache loadSubjectCache(String filename) throws IOException {
    if (Files.exists(Paths.get(filename))) {
      logger.info("Loading a project cache from {}", filename);
      return SubjectCache.load(filename);
    }

    return SubjectCache.empty();
  }

  /**
   * Returns a filename of a project cache.
   *
   * @param config A config of the tool.
   * @return A file from the config if available, the default cache file otherwise.
   */
  private String projectCacheFile(Config config) {
    return config.hasCacheFile()
        ? config.cacheFilename
        : baseDirectory + File.separator + "project_cache.json";
  }

  /**
   * Create a reporter from a config.
   * The method returns a list with only {@link Reporter#dummy()} if the config is null.
   *
   * @param config The config.
   * @return A reporter.
   * @throws IllegalArgumentException If the type is unknown.
   */
  List<Reporter<GitHubProject>> makeReporters(Config config) throws IOException {
    if (config == NO_CONFIG || config.reportConfigs == null) {
      return emptyList();
    }

    List<Reporter<GitHubProject>> reporters = new ArrayList<>();
    for (ReportConfig reportConfig : config.reportConfigs) {
      reporterFrom(reportConfig).ifPresent(reporters::add);
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
  Optional<Reporter<GitHubProject>> reporterFrom(ReportConfig reportConfig) throws IOException {
    return empty();
  }

  /**
   * Stores a rating of a subject if a user asked about it.
   *
   * @param subject The subject.
   * @param commandLine Command-line options.
   * @throws IOException If something went wrong.
   */
  void storeReportIfRequested(Subject subject, CommandLine commandLine) throws IOException {
    if (!subject.ratingValue().isPresent()) {
      throw new IllegalArgumentException("Oops! No rating assigned for the subject!");
    }

    if (commandLine.hasOption("report-file")) {
      String type = commandLine.getOptionValue("report-type", "text");
      String file = commandLine.getOptionValue("report-file");
      Formatter formatter = createFormatter(type);

      logger.info("Storing a report to {} ({})", file, type);

      Files.write(
          Paths.get(file),
          formatter.print(subject).getBytes(StandardCharsets.UTF_8));
    }

    if (commandLine.hasOption("raw-rating-file")) {
      String file = commandLine.getOptionValue("raw-rating-file");
      logger.info("Storing a raw rating to {}", file);
      Files.write(Paths.get(file), Json.toBytes(subject.ratingValue().get()));
    }
  }

  /**
   * Creates a formatter for a specified type.
   *
   * @param type The type.
   * @return A formatter.
   * @throws IllegalArgumentException If the type is unknown.
   */
  Formatter createFormatter(String type) {
    if ("text".equals(type)) {
      return PrettyPrinter.withoutVerboseOutput();
    }

    throw new IllegalArgumentException(format("Unsupported output type: %s", type));
  }

  /**
   * A processor that runs rating calculation for a subject identifier by a string.
   */
  private interface Processor {

    /**
     * Run the processor for a subject.
     *
     * @param string The subject's identifier.
     * @throws Exception If the processor failed.
     */
    void run(String string) throws Exception;
  }
}
