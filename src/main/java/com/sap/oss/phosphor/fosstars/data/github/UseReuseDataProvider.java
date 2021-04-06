package com.sap.oss.phosphor.fosstars.data.github;

import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.HAS_REUSE_LICENSES;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.IS_REUSE_COMPLIANT;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.README_HAS_REUSE_INFO;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.REGISTERED_IN_REUSE;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_REUSE;
import static com.sap.oss.phosphor.fosstars.model.other.Utils.setOf;
import static java.nio.file.LinkOption.NOFOLLOW_LINKS;

import com.fasterxml.jackson.databind.JsonNode;
import com.sap.oss.phosphor.fosstars.model.Feature;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.ValueSet;
import com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures;
import com.sap.oss.phosphor.fosstars.model.subject.oss.GitHubProject;
import com.sap.oss.phosphor.fosstars.model.value.ValueHashSet;
import com.sap.oss.phosphor.fosstars.util.Json;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.Set;
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
   * Initializes a data provider.
   *
   * @param fetcher An interface to GitHub.
   */
  public UseReuseDataProvider(GitHubDataFetcher fetcher) {
    super(fetcher);
  }

  @Override
  public Set<Feature<?>> supportedFeatures() {
    return setOf(USES_REUSE, README_HAS_REUSE_INFO, HAS_REUSE_LICENSES, REGISTERED_IN_REUSE,
        IS_REUSE_COMPLIANT);
  }

  @Override
  protected ValueSet fetchValuesFor(GitHubProject project) throws IOException {
    logger.info("Figuring out how the project uses REUSE ...");
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
    return USES_REUSE.value(GitHubDataFetcher.localRepositoryFor(project).hasFile(REUSE_CONFIG));
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
      return README_HAS_REUSE_INFO.value(false);
    }

    Optional<String> content = repository.file(filename.get());
    if (!content.isPresent()) {
      return README_HAS_REUSE_INFO.value(false);
    }

    String reuseUrl = String.format("https://api.reuse.software/info/github.com/%s/%s",
        project.organization().name(), project.name());
    return README_HAS_REUSE_INFO.value(content.get().contains(reuseUrl));
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
      return HAS_REUSE_LICENSES.value(false);
    }

    return HAS_REUSE_LICENSES.value(
        !repository.files(licenseDirectory, UseReuseDataProvider::isFile).isEmpty());
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
      String url = String.format("https://api.reuse.software/status/github.com/%s/%s",
          project.organization().name(), project.name());
      HttpGet request = new HttpGet(url);
      try (CloseableHttpResponse response = client.execute(request)) {
        if (response.getStatusLine().getStatusCode() != 200) {
          logger.warn("Oops! Could not fetch info from REUSE API ({})",
              response.getStatusLine().getStatusCode());
          return ValueHashSet.from(REGISTERED_IN_REUSE.unknown(), IS_REUSE_COMPLIANT.unknown());
        }

        JsonNode root = Json.mapper().readTree(response.getEntity().getContent());
        if (!root.has("status")) {
          logger.warn("Oops! Could not get REUSE status!");
          return ValueHashSet.from(REGISTERED_IN_REUSE.unknown(), IS_REUSE_COMPLIANT.unknown());
        }

        String status = root.get("status").asText();
        switch (status) {
          case "unregistered":
            return ValueHashSet.from(
                REGISTERED_IN_REUSE.value(false), IS_REUSE_COMPLIANT.value(false));
          case "compliant":
            return ValueHashSet.from(
                REGISTERED_IN_REUSE.value(true), IS_REUSE_COMPLIANT.value(true));
          case "non-compliant":
            return ValueHashSet.from(
                REGISTERED_IN_REUSE.value(true), IS_REUSE_COMPLIANT.value(false));
          default:
            logger.warn("Oops! Unknown REUSE status ({})", status);
            return ValueHashSet.from(REGISTERED_IN_REUSE.unknown(), IS_REUSE_COMPLIANT.unknown());
        }
      }
    } catch (IOException e) {
      logger.warn("Oops! Could not check whether vulnerability alerts are enabled or not", e);
      return ValueHashSet.from(REGISTERED_IN_REUSE.unknown(), IS_REUSE_COMPLIANT.unknown());
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
    return Files.isRegularFile(path, NOFOLLOW_LINKS) && !Files.isSymbolicLink(path);
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
    String string;
    Optional<Value<Boolean>> something = values.of(feature);
    System.out.printf("%s: %s%n",
        feature.name(), something.map(Value::toString).orElse("not found"));
  }
}
