package com.sap.oss.phosphor.fosstars.tool.github;

import static java.lang.String.format;
import static java.util.Collections.emptyList;
import static java.util.Objects.requireNonNull;

import com.sap.oss.phosphor.fosstars.data.DataProvider;
import com.sap.oss.phosphor.fosstars.data.DataProviderSelector;
import com.sap.oss.phosphor.fosstars.data.NoUserCallback;
import com.sap.oss.phosphor.fosstars.data.SubjectValueCache;
import com.sap.oss.phosphor.fosstars.data.UserCallback;
import com.sap.oss.phosphor.fosstars.data.github.GitHubDataFetcher;
import com.sap.oss.phosphor.fosstars.model.Rating;
import com.sap.oss.phosphor.fosstars.model.Subject;
import com.sap.oss.phosphor.fosstars.model.subject.oss.GitHubProject;
import com.sap.oss.phosphor.fosstars.nvd.NVD;
import com.sap.oss.phosphor.fosstars.tool.MultipleRatingsCalculator;
import com.sap.oss.phosphor.fosstars.tool.SingleRatingCalculator;
import com.sap.oss.phosphor.fosstars.tool.SubjectCache;
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
import java.util.List;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 */
public abstract class AbstractHandler implements Handler {

  /**
   * A logger.
   */
  final Logger logger = LogManager.getLogger(getClass());

  /**
   *
   */
  final NVD nvd = new NVD();

  /**
   *
   */
  final Rating rating;

  /**
   *
   */
  CommandLine commandLine;

  /**
   *
   */
  List<String> withDataProviderConfigs = emptyList();

  /**
   *
   */
  GitHubDataFetcher fetcher;

  /**
   *
   */
  UserCallback callback = NoUserCallback.INSTANCE;

  /**
   *
   */
  SubjectValueCache cache = new SubjectValueCache();

  /**
   *
   */
  String baseDirectory = "./";

  AbstractHandler(Rating rating) {
    this.rating = requireNonNull(rating, "Oops! Rating is null!");
  }

  /**
   *
   * @throws IOException
   */
  SingleRatingCalculator calculator() throws IOException {
    List<DataProvider> providers = dataProviderSelector().providersFor(rating);
    return new SingleRatingCalculator(rating, providers).set(cache).set(callback);
  }

  /**
   *
   * @return
   * @throws IOException
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
  public void close() {

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
    return emptyList();
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
  Formatter createFormatter(String type) throws IOException {
    if ("text".equals(type)) {
      return PrettyPrinter.withoutVerboseOutput();
    }

    throw new IllegalArgumentException(format("Unsupported output type: %s", type));
  }

  /**
   *
   * @param commandLine
   * @param subjectOptions
   */
  static void requireOneOfIn(CommandLine commandLine, String... subjectOptions) {
    boolean found = Arrays.stream(subjectOptions).anyMatch(commandLine::hasOption);
    if (!found) {
      throw new IllegalArgumentException(format(
          "You have to give me one of the following options: %s",
          String.join(", ", subjectOptions)));
    }
  }
}
