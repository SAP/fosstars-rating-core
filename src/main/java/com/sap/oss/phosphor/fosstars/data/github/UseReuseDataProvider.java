package com.sap.oss.phosphor.fosstars.data.github;

import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.HAS_REUSE_LICENSES;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.IS_REUSE_COMPLIANT;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.README_HAS_REUSE_INFO;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.REGISTERED_IN_REUSE;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_REUSE;
import static com.sap.oss.phosphor.fosstars.model.other.Utils.setOf;
import static com.sap.oss.phosphor.fosstars.util.Deserialization.readListFrom;
import static java.lang.String.format;

import com.fasterxml.jackson.databind.JsonNode;
import com.sap.oss.phosphor.fosstars.model.Feature;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.ValueSet;
import com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures;
import com.sap.oss.phosphor.fosstars.model.subject.oss.GitHubProject;
import com.sap.oss.phosphor.fosstars.model.value.ValueHashSet;
import com.sap.oss.phosphor.fosstars.util.Json;
import com.sap.oss.phosphor.fosstars.util.Yaml;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;

/**
 * The data provider gathers info about how a project uses REUSE tool.
 * It fills out the following features:
 * <ul>
 *   <li>{@link OssFeatures#USES_REUSE}</li>
 *   <li>{@link OssFeatures#README_HAS_REUSE_INFO}</li>
 *   <li>{@link OssFeatures#HAS_REUSE_LICENSES}</li>
 *   <li>{@link OssFeatures#REGISTERED_IN_REUSE}</li>
 *   <li>{@link OssFeatures#IS_REUSE_COMPLIANT}</li>
 * </ul>
 */
public class UseReuseDataProvider extends GitHubCachingDataProvider {

  /**
   * A path to a REUSE config.
   */
  static final String REUSE_CONFIG = ".reuse/dep5";

  /**
   * A directory where REUSE stores all licenses.
   */
  static final String REUSE_LICENCES_DIRECTORY = "LICENSES";

  /**
   * A list of repositories that are already known to be compliant.
   */
  private final List<String> repositoryExceptionUrls = new ArrayList<>();

  /**
   * Initializes a data provider.
   *
   * @param fetcher An interface to GitHub.
   * @throws IOException If something went wrong.
   */
  public UseReuseDataProvider(GitHubDataFetcher fetcher) throws IOException {
    super(fetcher);
    loadDefaultConfigIfAvailable();
  }

  @Override
  public Set<Feature<?>> supportedFeatures() {
    return setOf(
        USES_REUSE,
        README_HAS_REUSE_INFO,
        HAS_REUSE_LICENSES,
        REGISTERED_IN_REUSE,
        IS_REUSE_COMPLIANT);
  }

  @Override
  protected ValueSet fetchValuesFor(GitHubProject project) throws IOException {
    logger.info("Figuring out how the project uses REUSE ...");

    // Some repositories apply other copyright annotations and are well-known exceptions.
    // Those ones will reported as OK by this data provider.
    if (this.repositoryExceptionUrls.contains(project.toString())) {
      return ValueHashSet.from(
          USES_REUSE.value(true),
          README_HAS_REUSE_INFO.value(true),
          HAS_REUSE_LICENSES.value(true),
          REGISTERED_IN_REUSE.value(true),
          IS_REUSE_COMPLIANT.value(true));
    }

    ValueSet values = ValueHashSet.from(
        useReuse(project),
        readmeHasReuseInfo(project),
        hasReuseLicenses(project));
    values.update(reuseInfo(project));
    return values;
  }

  /**
   * Check whether a project uses REUSE or not.
   *
   * @param project The project.
   * @return A value of {@link OssFeatures#USES_REUSE} feature.
   * @throws IOException If something went wrong.
   */
  static Value<Boolean> useReuse(GitHubProject project) throws IOException {
    return USES_REUSE.value(GitHubDataFetcher.localRepositoryFor(project).hasFile(REUSE_CONFIG))
        .explainIf(false, "The project does not have a config for REUSE (%s)", REUSE_CONFIG);
  }

  /**
   * Checks whether project's README file contains info about REUSE or not.
   *
   * @param project The project.
   * @return A value of {@link OssFeatures#README_HAS_REUSE_INFO} feature.
   * @throws IOException If something went wrong.
   */
  static Value<Boolean> readmeHasReuseInfo(GitHubProject project) throws IOException {
    LocalRepository repository = GitHubDataFetcher.localRepositoryFor(project);

    Optional<String> filename = ReadmeInfo.readmeIn(repository);
    if (!filename.isPresent()) {
      return README_HAS_REUSE_INFO.value(false).explain("The project does not have a README");
    }

    Optional<String> content = repository.file(filename.get());
    if (!content.isPresent()) {
      return README_HAS_REUSE_INFO.value(false).explain("Could not read the README");
    }

    String reuseUrl = format("https://api.reuse.software/info/github.com/%s/%s",
        project.organization().name(), project.name());
    return README_HAS_REUSE_INFO.value(
        StringUtils.containsIgnoreCase(content.get(), reuseUrl)).explainIf(false,
        "The README does not seem to have a badge that points to REUSE status (%s)", reuseUrl);
  }

  /**
   * Checks whether a project has a not-empty directory with licences or not.
   *
   * @param project The project.
   * @return A value of {@link OssFeatures#HAS_REUSE_LICENSES} feature.
   * @throws IOException If something went wrong.
   */
  static Value<Boolean> hasReuseLicenses(GitHubProject project) throws IOException {
    LocalRepository repository = GitHubDataFetcher.localRepositoryFor(project);
    Path licenseDirectory = Paths.get(REUSE_LICENCES_DIRECTORY);
    if (!repository.hasDirectory(licenseDirectory)) {
      return HAS_REUSE_LICENSES.value(false)
          .explain("The project does not have %s directory", REUSE_LICENCES_DIRECTORY);
    }

    return HAS_REUSE_LICENSES
        .value(!repository.files(licenseDirectory, UseReuseDataProvider::isFile).isEmpty())
        .explainIf(false, "The project doesn't have licenses in %s directory", licenseDirectory);
  }

  /**
   * Possible results of the REUSE tool registration check.
   */
  private enum ReuseInfo {
    UNAVAILABLE,
    UNKNOWN,
    UNREGISTERED,
    COMPLIANT,
    NON_COMPLIANT
  }

  /**
   * Check if a project is registered in REUSE and its status.
   *
   * @param project The project.
   * @return A {@link ValueSet} with {@link OssFeatures#REGISTERED_IN_REUSE}
   *         and {@link OssFeatures#IS_REUSE_COMPLIANT} features.
   */
  ValueSet reuseInfo(GitHubProject project) {
    try (CloseableHttpClient client = httpClient()) {

      ReuseInfo rawReuseInfo = retrieveReuseInfo(client, project, false);
      if (rawReuseInfo == ReuseInfo.UNREGISTERED) {
        rawReuseInfo = retrieveReuseInfo(client, project, true);
      }

      String note = "Received unknown as project's REUSE status. "
          + "You may want to open an issue for that.";
      switch (rawReuseInfo) {
        case UNAVAILABLE:
          logger.warn("Oops! Could not get REUSE status!");
          note = "Could not retrieve the project's REUSE status";
          return ValueHashSet.from(
              REGISTERED_IN_REUSE.unknown().explain(note),
              IS_REUSE_COMPLIANT.unknown().explain(note));
        case UNREGISTERED:
          note = "The project is not registered in REUSE";
          return ValueHashSet.from(
              REGISTERED_IN_REUSE.value(false).explain(note),
              IS_REUSE_COMPLIANT.value(false).explain(note));
        case COMPLIANT:
          return ValueHashSet.from(
              REGISTERED_IN_REUSE.value(true),
              IS_REUSE_COMPLIANT.value(true));
        case NON_COMPLIANT:
          note = "The project violates REUSE rules";
          return ValueHashSet.from(
              REGISTERED_IN_REUSE.value(true),
              IS_REUSE_COMPLIANT.value(false).explain(note));
        case UNKNOWN:
        default:
          logger.warn("Oops! Unknown REUSE status");
          return ValueHashSet.from(
              REGISTERED_IN_REUSE.unknown().explain(note),
              IS_REUSE_COMPLIANT.unknown().explain(note));
      }

    } catch (IOException e) {
      logger.warn("Oops! Could not retrieve REUSE status!", e);
      String note = "Could not retrieve the project's REUSE status";
      return ValueHashSet.from(
          REGISTERED_IN_REUSE.unknown().explain(note),
          IS_REUSE_COMPLIANT.unknown().explain(note));
    }
  }

  /**
   * Retrieves the REUSE tool registration information for a given project. Callers can
   * specify if the project URL should include a trailing slash. Reason: The REUSE tool
   * registration differentiates between the registration URLs
   * 'https://github.com/org/repo' and
   * 'https://github.com/org/repo/', although it's the same project.
   * This might lead to erroneous check results if the registration URL differs from the URL
   * that is used when the REUSE API is called.
   * As a consequence, the information retrieval can be executed with both URL variants.
   *
   * @param client The HTTP client the REUSE information retrieval should be executed with
   * @param project The project the REUSE information retrieval should be executed for
   * @param useTrailingSlash If the REUSE information retrieval should use a trailing URL slash
   * @return A {@link ReuseInfo} with the retrieval results
   * @throws IOException If something went wrong.
   */
  private ReuseInfo retrieveReuseInfo(CloseableHttpClient client, GitHubProject project,
      boolean useTrailingSlash) throws IOException {

    String url = format("https://api.reuse.software/status/github.com/%s/%s%s",
        project.organization().name(), project.name(), useTrailingSlash ? "/" : "");
    HttpGet request = new HttpGet(url);

    try (CloseableHttpResponse response = client.execute(request)) {
      if (response.getStatusLine().getStatusCode() != 200) {
        logger.warn("Oops! Could not fetch info from REUSE API ({})",
            response.getStatusLine().getStatusCode());
        return ReuseInfo.UNAVAILABLE;
      }

      JsonNode root = Json.mapper().readTree(response.getEntity().getContent());
      if (!root.has("status")) {
        return ReuseInfo.UNAVAILABLE;
      }

      String status = root.get("status").asText();
      switch (status) {
        case "unregistered":
          return ReuseInfo.UNREGISTERED;
        case "compliant":
          return ReuseInfo.COMPLIANT;
        case "non-compliant":
          return ReuseInfo.NON_COMPLIANT;
        default:
          return ReuseInfo.UNKNOWN;
      }
    }
  }

  /**
   * Creates an HTTP client.
   *
   * @return A new HTTP client.
   */
  CloseableHttpClient httpClient() {
    return HttpClients.createDefault();
  }

  /**
   * Checks if a path is a regular file.
   *
   * @param path The path.
   * @return True if the path is a regular file, false otherwise.
   */
  private static boolean isFile(Path path) {
    return Files.isRegularFile(path);
  }

  /**
   * Command-line interface for testing.
   *
   * @param args Command-line options.
   * @throws Exception If something went wrong.
   */
  public static void main(String... args) throws Exception {
    String token = args[0];
    String url = args[1];
    GitHub github = new GitHubBuilder().withOAuthToken(token).build();
    GitHubDataFetcher fetcher = new GitHubDataFetcher(github, token);
    UseReuseDataProvider provider = new UseReuseDataProvider(fetcher);
    provider.configure(IOUtils.toInputStream(
        "---\n"
            + "repositoryExceptions:\n"
            + "  - https://github.com/SAP/SapMachine\n"
            + "  - https://github.com/SAP/async-profiler\n"
            + "  - https://github.com/SAP/jmc\n",
        "UTF-8"));
    GitHubProject project = GitHubProject.parse(url);
    ValueSet values = provider.fetchValuesFor(project);
    print(values, USES_REUSE);
    print(values, README_HAS_REUSE_INFO);
    print(values, HAS_REUSE_LICENSES);
    print(values, REGISTERED_IN_REUSE);
    print(values, IS_REUSE_COMPLIANT);
  }

  /**
   * Looks for a feature in a set of values and prints it out.
   *
   * @param values The values.
   * @param feature The feature.
   */
  private static void print(ValueSet values, Feature<Boolean> feature) {
    Optional<Value<Boolean>> something = values.of(feature);
    System.out.printf("%s: %s%n",
        feature.name(), something.map(Value::toString).orElse("not found"));
  }

  @Override
  public UseReuseDataProvider configure(Path configurationPath) throws IOException {
    try (InputStream is = Files.newInputStream(configurationPath)) {
      return configure(is);
    }
  }

  /**
   * Reads a configuration from YAML.
   *
   * @param is An input stream with YAML.
   * @return This data provider.
   * @throws IOException If something went wrong.
   */
  UseReuseDataProvider configure(InputStream is) throws IOException {
    JsonNode config = Yaml.mapper().readTree(is);
    repositoryExceptions(readListFrom(config, "repositoryExceptions"));
    return this;
  }

  /**
   * Set a list of repositories that are known to be compliant.
   *
   * @param repositoryExceptions The repository URLs.
   * @return This data provider.
   */
  public UseReuseDataProvider repositoryExceptions(String... repositoryExceptions) {
    return repositoryExceptions(Arrays.asList(repositoryExceptions));
  }

  /**
   * Set a list of repositories. that are known to be compliant.
   *
   * @param repositoryExceptions The repository URLs.
   * @return This data provider.
   */
  public UseReuseDataProvider repositoryExceptions(List<String> repositoryExceptions) {
    Objects.requireNonNull(repositoryExceptions, "Oops! Repository URL list is null");
    repositoryExceptionUrls.clear();
    repositoryExceptionUrls.addAll(repositoryExceptions);
    return this;
  }

  /**
   * Returns a list of repositories that are known to be compliant.
   *
   * @return A list of repository URLs.
   */
  List<String> repositoryExceptions() {
    return new ArrayList<>(repositoryExceptionUrls);
  }

}
