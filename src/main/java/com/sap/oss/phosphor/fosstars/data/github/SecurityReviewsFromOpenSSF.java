package com.sap.oss.phosphor.fosstars.data.github;

import static java.lang.String.format;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.packageurl.MalformedPackageURLException;
import com.github.packageurl.PackageURL;
import com.sap.oss.phosphor.fosstars.model.Feature;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.feature.oss.SecurityReviewsFeature;
import com.sap.oss.phosphor.fosstars.model.subject.oss.GitHubProject;
import com.sap.oss.phosphor.fosstars.model.value.SecurityReview;
import com.sap.oss.phosphor.fosstars.model.value.SecurityReviews;
import com.sap.oss.phosphor.fosstars.model.value.SecurityReviewsValue;
import com.sap.oss.phosphor.fosstars.util.Yaml;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;

/**
 * This data provider gather information about security reviews collected by OpenSSF.
 *
 * @see <a href="https://github.com/ossf/security-reviews">Security Reviews</a>
 */
public class SecurityReviewsFromOpenSSF
    extends CachedSingleFeatureGitHubDataProvider<SecurityReviews> {

  /**
   * A feature that holds security reviews.
   */
  public static final SecurityReviewsFeature FEATURE
      = new SecurityReviewsFeature("Security reviews from OpenSSF");

  /**
   * A repository that stores security review.
   */
  static final GitHubProject SECURITY_REVIEWS_PROJECT
      = new GitHubProject("ossf", "security-reviews");

  /**
   * A parser for dates.
   */
  static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

  /**
   * Initializes a data provider.
   *
   * @param fetcher An interface to GitHub.
   */
  public SecurityReviewsFromOpenSSF(GitHubDataFetcher fetcher) {
    super(fetcher);
  }

  @Override
  protected Feature<SecurityReviews> supportedFeature() {
    return FEATURE;
  }

  @Override
  protected Value<SecurityReviews> fetchValueFor(GitHubProject project) throws IOException {
    Objects.requireNonNull(project, "Project can't be null!");

    LocalRepository reviewsRepository
        = GitHubDataFetcher.localRepositoryFor(SECURITY_REVIEWS_PROJECT);

    List<Path> reviewFiles = reviewsRepository.files(
        Paths.get("reviews"), SecurityReviewsFromOpenSSF::isReview);

    SecurityReviews reviews = new SecurityReviews();
    for (Path file : reviewFiles) {
      Optional<JsonNode> metadata = readMetadataFrom(file);
      if (!metadata.isPresent()) {
        logger.warn("Oops! Could not load metadata from {}", file);
        continue;
      }

      if (isReviewFor(project, metadata.get())) {
        Optional<Date> reviewDate = readReviewDateFrom(metadata.get());
        if (!reviewDate.isPresent()) {
          continue;
        }
        reviews.add(new SecurityReview(project, reviewDate.get()));
      }
    }

    return new SecurityReviewsValue(FEATURE, reviews);
  }

  /**
   * Reads a review date from metadata.
   *
   * @param metadata The metadata.
   * @return A review date if available.
   */
  Optional<Date> readReviewDateFrom(JsonNode metadata) {
    JsonNode node = metadata.at("/Review-Date");
    if (!node.isTextual()) {
      logger.warn("Oops! Review date is not a string!");
      return Optional.empty();
    }

    try {
      return Optional.of(DATE_FORMAT.parse(node.asText()));
    } catch (ParseException e) {
      logger.warn(() -> String.format("Oops! Could not parse date: %s", node.asText()), e);
      return Optional.empty();
    }
  }

  /**
   * Checks if a review was done for a project.
   *
   * @param project The project.
   * @param metadata Metadata of the review.
   * @return True if the review was done for the project, false otherwise.
   */
  boolean isReviewFor(GitHubProject project, JsonNode metadata) {
    JsonNode node = metadata.at("/Package-URLs");
    if (!node.isArray()) {
      logger.warn("Oops! Package-URLs is not an array!");
      return false;
    }

    return StreamSupport.stream(node.spliterator(), false)
        .filter(JsonNode::isTextual)
        .map(JsonNode::asText)
        .anyMatch(purl -> purlBelongsTo(project, purl));
  }

  /**
   * Checks if a PURL belongs to a project.
   *
   * @param project The project.
   * @param purl The PURL.
   * @return True if the PURL belongs to the project, false otherwise.
   */
  boolean purlBelongsTo(GitHubProject project, String purl) {
    try {
      PackageURL packageUrl = new PackageURL(purl);
      return "github".equalsIgnoreCase(packageUrl.getType())
          && packageUrl.getNamespace() != null
          && packageUrl.getName() != null
          && project.equals(new GitHubProject(packageUrl.getNamespace(), packageUrl.getName()));
    } catch (MalformedPackageURLException e) {
      logger.warn(() -> format("Oops! Could not parse package URL: %s", purl), e);
    }

    return false;
  }

  /**
   * Reads review metadata from a file.
   *
   * @param file The file.
   * @return Metadata if available.
   * @throws IOException If something went wrong.
   */
  private static Optional<JsonNode> readMetadataFrom(Path file) throws IOException {
    try (BufferedReader reader = Files.newBufferedReader(file)) {
      return readMetadataFrom(reader);
    }
  }

  /**
   * Reads review metadata from a reader.
   *
   * @param reader The reader.
   * @return Metadata if available.
   * @throws IOException If something wrong.
   */
  static Optional<JsonNode> readMetadataFrom(BufferedReader reader) throws IOException {
    String line = reader.readLine();
    if (!"---".equals(line)) {
      return Optional.empty();
    }

    StringBuilder metadata = new StringBuilder();
    do {
      metadata.append(line).append("\n");
      line = reader.readLine();
    } while (line != null && !"---".equals(line));

    return Optional.of(Yaml.mapper().readTree(metadata.toString()));
  }

  /**
   * Checks if a file looks like a security review.
   *
   * @param path A path to the file.
   * @return True if the file looks like a security review, false otherwise.
   */
  private static boolean isReview(Path path) {
    return Files.isRegularFile(path) && path.getFileName().toString().endsWith(".md");
  }
}

