package com.sap.oss.phosphor.fosstars.data.github;

import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.ALLOWED_LICENSE;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.HAS_LICENSE;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.LICENSE_HAS_DISALLOWED_CONTENT;
import static com.sap.oss.phosphor.fosstars.model.other.Utils.setOf;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

import com.fasterxml.jackson.databind.JsonNode;
import com.sap.oss.phosphor.fosstars.model.Feature;
import com.sap.oss.phosphor.fosstars.model.ValueSet;
import com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures;
import com.sap.oss.phosphor.fosstars.model.subject.oss.GitHubProject;
import com.sap.oss.phosphor.fosstars.model.value.ValueHashSet;
import com.sap.oss.phosphor.fosstars.util.Yaml;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * This data provider gathers info about project's license. It fills out the following features:
 * <ul>
 *   <li>{@link OssFeatures#HAS_LICENSE}</li>
 *   <li>{@link OssFeatures#ALLOWED_LICENSE}</li>
 *   <li>{@link OssFeatures#LICENSE_HAS_DISALLOWED_CONTENT}</li>
 * </ul>
 */
public class LicenseInfo extends GitHubCachingDataProvider {

  /**
   * A list of files that may be a license.
   */
  private static final List<String> DEFAULT_KNOWN_LICENSE_FILES
      = Arrays.asList("LICENSE", "LICENSE.txt");

  /**
   * A list of paths to licenses.
   */
  private final List<String> knownLicenseFiles = new ArrayList<>();

  /**
   * A list of allowed license headers.
   */
  private final List<String> allowedLicenseHeaders = new ArrayList<>();

  /**
   * A list of patterns that are not allowed in licenses.
   */
  private final List<String> disallowedLicenseContentPatterns = new ArrayList<>();

  /**
   * Initializes a data provider.
   *
   * @param fetcher An interface to GitHub.
   */
  public LicenseInfo(GitHubDataFetcher fetcher) {
    super(fetcher);
    this.knownLicenseFiles.addAll(DEFAULT_KNOWN_LICENSE_FILES);
  }

  /**
   * Set a list of file that may be a license.
   *
   * @param files The files.
   * @return This data provider.
   */
  public LicenseInfo knownLicenseFiles(String... files) {
    Objects.requireNonNull(files, "Oops! Files can't be null");

    if (files.length == 0) {
      throw new IllegalArgumentException("Oops! Files can't be empty!");
    }

    knownLicenseFiles.clear();
    knownLicenseFiles.addAll(Arrays.asList(files));

    return this;
  }

  /**
   * Returns a list of allowed license headers.
   *
   * @return A list of allowed license headers.
   */
  List<String> allowedLicenseHeaders() {
    return new ArrayList<>(allowedLicenseHeaders);
  }

  /**
   * Set a list of allowed license headers.
   *
   * @param headers The headers.
   * @return This data provider.
   */
  public LicenseInfo allowedLicenseHeaders(String... headers) {
    Objects.requireNonNull(headers, "Oops! Headers is null");
    allowedLicenseHeaders.clear();
    allowedLicenseHeaders.addAll(Arrays.asList(headers));
    return this;
  }

  /**
   * Set a list of patterns that are not allowed in licenses.
   *
   * @param patterns The patterns.
   * @return This data provider.
   */
  public LicenseInfo disallowedLicenseContentPatterns(String... patterns) {
    Objects.requireNonNull(patterns, "Oops! Patterns can't be null!");
    disallowedLicenseContentPatterns.clear();
    disallowedLicenseContentPatterns.addAll(Arrays.asList(patterns));
    return this;
  }

  @Override
  public Set<Feature<?>> supportedFeatures() {
    return setOf(HAS_LICENSE, ALLOWED_LICENSE, LICENSE_HAS_DISALLOWED_CONTENT);
  }

  @Override
  protected ValueSet fetchValuesFor(GitHubProject project) throws IOException {
    logger.info("Gathering info about project's license ...");

    Optional<List<String>> license = lookForLicenseIn(project);
    if (!license.isPresent()) {
      return ValueHashSet.from(HAS_LICENSE.value(false), ALLOWED_LICENSE.unknown(),
          LICENSE_HAS_DISALLOWED_CONTENT.unknown());
    }

    ValueSet values = new ValueHashSet();
    values.update(HAS_LICENSE.value(true));
    values.update(infoAboutLicense(license.get()));

    return values;
  }

  /**
   * Looks for license in a project.
   *
   * @param project The project.
   * @return Content of the license if found.
   * @throws IOException If something went wrong.
   */
  private Optional<List<String>> lookForLicenseIn(GitHubProject project) throws IOException {
    LocalRepository repository = GitHubDataFetcher.localRepositoryFor(project);

    for (String path : knownLicenseFiles) {
      Optional<List<String>> content = repository.readLinesOf(path);
      if (content.isPresent()) {
        return content;
      }
    }

    return Optional.empty();
  }

  /**
   * Extract info about license.
   *
   * @param content Content of the license.
   * @return A set of values.
   */
  ValueSet infoAboutLicense(List<String> content) {
    ValueSet values = ValueHashSet.from(
        ALLOWED_LICENSE.value(false), LICENSE_HAS_DISALLOWED_CONTENT.value(false));

    String header = headerOf(content).toLowerCase();
    values.update(ALLOWED_LICENSE.value(
        allowedLicenseHeaders.stream().map(String::toLowerCase).anyMatch(header::contains)));
    values.update(LICENSE_HAS_DISALLOWED_CONTENT.value(content.stream().anyMatch(this::isWrong)));

    return values;
  }

  /**
   * Looks for the first not-empty string in a text.
   *
   * @param content The text.
   * @return The first not-empty string.
   * @throws IllegalArgumentException If nothing found.
   */
  static String headerOf(List<String> content) {
    for (String line : content) {
      if (!line.trim().isEmpty()) {
        return line;
      }
    }

    throw new IllegalArgumentException("Oops! No header found!");
  }

  /**
   * Checks if a line contains {@link #disallowedLicenseContentPatterns disallowed patterns}.
   *
   * @param line The line to check.
   * @return True if the line contains disallowed patterns, false otherwise.
   */
  boolean isWrong(String line) {
    line = line.toLowerCase();
    return disallowedLicenseContentPatterns.stream()
        .map(String::toLowerCase).anyMatch(line::contains);
  }

  /**
   * Reads a configuration from a YAML file.
   *
   * @param path A path to the YAML file.
   * @return This data provider.
   * @throws IOException If something went wrong.
   */
  @Override
  public LicenseInfo configure(Path path) throws IOException {
    return configure(Files.newInputStream(path));
  }

  /**
   * Reads a configuration from YAML.
   *
   * @param is An input stream with YAML.
   * @return This data provider.
   * @throws IOException If something went wrong.
   */
  LicenseInfo configure(InputStream is) throws IOException {
    JsonNode config = Yaml.mapper().readTree(is);

    this.allowedLicenseHeaders.clear();
    this.allowedLicenseHeaders.addAll(allowedLicenseHeadersIn(config));

    return this;
  }

  /**
   * Read allowed license header from a config.
   *
   * @param config The config.
   * @return A list of allowed license headers if available.
   * @throws IOException If the config is not correct, or something else went wrong.
   */
  static List<String> allowedLicenseHeadersIn(JsonNode config)
      throws IOException {

    if (!config.has("allowedLicenseHeaders")) {
      return emptyList();
    }

    JsonNode node = config.get("allowedLicenseHeaders");
    if (node.isTextual()) {
      return singletonList(node.asText());
    }

    if (!node.isArray()) {
      throw new IOException("Oops! allowedLicenseHeaders is not an array and not a string!");
    }

    List<String> headers = new ArrayList<>();
    Iterator<JsonNode> iterator = node.elements();
    while (iterator.hasNext()) {
      JsonNode element = iterator.next();
      if (!element.isTextual()) {
        throw new IOException("Oops! Element of allowedLicenseHeaders is not a string!");
      }
      headers.add(element.asText());
    }

    return headers;
  }
}
