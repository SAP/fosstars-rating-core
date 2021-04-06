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
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.apache.commons.io.IOUtils;

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
   * A list of patterns for matching a valid license.
   */
  private final List<Pattern> allowedLicensePatterns = new ArrayList<>();

  /**
   * A list of patterns that are not allowed in licenses.
   */
  private final List<Pattern> disallowedLicensePatterns = new ArrayList<>();

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
  List<Pattern> allowedLicensePatterns() {
    return new ArrayList<>(allowedLicensePatterns);
  }

  /**
   * Set a list of patterns for matching an allowed licence.
   *
   * @param patterns The patterns.
   * @return This data provider.
   */
  public LicenseInfo allowedLicensePatterns(String... patterns) {
    return allowedLicensePatterns(Arrays.asList(patterns));
  }

  /**
   * Set a list of patterns for matching an allowed licence.
   *
   * @param patterns The patterns.
   * @return This data provider.
   */
  public LicenseInfo allowedLicensePatterns(List<String> patterns) {
    Objects.requireNonNull(patterns, "Oops! Patterns is null");
    allowedLicensePatterns.clear();
    allowedLicensePatterns.addAll(
        patterns.stream()
            .map(pattern -> Pattern.compile(pattern, Pattern.DOTALL)).collect(Collectors.toList()));
    return this;
  }

  /**
   * Returns a list of disallowed license headers.
   *
   * @return A list of disallowed license headers.
   */
  List<Pattern> disallowedLicensePatterns() {
    return new ArrayList<>(disallowedLicensePatterns);
  }

  /**
   * Set a list of patterns for detecting not-allowed content in a license.
   *
   * @param patterns The patterns.
   * @return This data provider.
   */
  public LicenseInfo disallowedLicensePatterns(String... patterns) {
    return disallowedLicensePatterns(Arrays.asList(patterns));
  }

  /**
   * Set a list of patterns for detecting not-allowed content in a license.
   *
   * @param patterns The patterns.
   * @return This data provider.
   */
  public LicenseInfo disallowedLicensePatterns(List<String> patterns) {
    Objects.requireNonNull(patterns, "Oops! Patterns can't be null!");
    disallowedLicensePatterns.clear();
    disallowedLicensePatterns.addAll(
        patterns.stream()
            .map(pattern -> Pattern.compile(pattern, Pattern.DOTALL)).collect(Collectors.toList()));
    return this;
  }

  @Override
  public Set<Feature<?>> supportedFeatures() {
    return setOf(HAS_LICENSE, ALLOWED_LICENSE, LICENSE_HAS_DISALLOWED_CONTENT);
  }

  @Override
  protected ValueSet fetchValuesFor(GitHubProject project) throws IOException {
    logger.info("Gathering info about project's license ...");

    Optional<String> license = lookForLicenseIn(project);
    if (!license.isPresent()) {
      return ValueHashSet.from(
          HAS_LICENSE.value(false),
          ALLOWED_LICENSE.unknown(),
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
  private Optional<String> lookForLicenseIn(GitHubProject project) throws IOException {
    LocalRepository repository = GitHubDataFetcher.localRepositoryFor(project);

    for (String path : knownLicenseFiles) {
      Optional<InputStream> content = repository.read(path);
      if (content.isPresent()) {
        return Optional.of(IOUtils.toString(content.get()));
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
  ValueSet infoAboutLicense(String content) {
    ValueSet values = ValueHashSet.from(
        ALLOWED_LICENSE.value(false), LICENSE_HAS_DISALLOWED_CONTENT.value(false));

    values.update(ALLOWED_LICENSE.value(
        allowedLicensePatterns.stream()
            .allMatch(pattern -> pattern.matcher(content).find())));
    values.update(LICENSE_HAS_DISALLOWED_CONTENT.value(
        disallowedLicensePatterns.stream()
            .anyMatch(pattern -> pattern.matcher(content).find())));

    return values;
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
    allowedLicensePatterns(readListFrom(config, "allowedLicensePatterns"));
    disallowedLicensePatterns(readListFrom(config, "disallowedLicensePatterns"));
    return this;
  }

  /**
   * Reads a list from a config.
   *
   * @param config The config.
   * @param property A field that has the list.
   * @return A list of elements.
   * @throws IOException If something went wrong.
   */
  private static List<String> readListFrom(JsonNode config, String property) throws IOException {
    if (!config.has(property)) {
      return emptyList();
    }

    JsonNode node = config.get(property);
    if (node.isTextual()) {
      return singletonList(node.asText());
    }

    if (!node.isArray()) {
      throw new IOException(
          String.format("Oops! '%s' is not an array and not a string!", property));
    }

    List<String> list = new ArrayList<>();
    Iterator<JsonNode> iterator = node.elements();
    while (iterator.hasNext()) {
      JsonNode element = iterator.next();
      if (!element.isTextual()) {
        throw new IOException(String.format("Oops! Element of '%s' is not a string!", property));
      }
      list.add(element.asText());
    }

    return list;
  }
}
