package com.sap.oss.phosphor.fosstars.data.github.experimental;

import static java.lang.String.format;
import static java.util.Arrays.asList;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.packageurl.MalformedPackageURLException;
import com.github.packageurl.PackageURL;
import com.sap.oss.phosphor.fosstars.data.github.CachedSingleFeatureGitHubDataProvider;
import com.sap.oss.phosphor.fosstars.data.github.GitHubDataFetcher;
import com.sap.oss.phosphor.fosstars.data.github.LocalRepository;
import com.sap.oss.phosphor.fosstars.model.Feature;
import com.sap.oss.phosphor.fosstars.model.Subject;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.feature.AbstractFeature;
import com.sap.oss.phosphor.fosstars.model.subject.oss.GitHubProject;
import com.sap.oss.phosphor.fosstars.model.value.AbstractKnownValue;
import com.sap.oss.phosphor.fosstars.util.Yaml;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
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
  private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

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
      PackageURL packageURL = new PackageURL(purl);
      return "github".equalsIgnoreCase(packageURL.getType())
          && packageURL.getNamespace() != null
          && packageURL.getName() != null
          && project.equals(new GitHubProject(packageURL.getNamespace(), packageURL.getName()));
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

/**
 * A security review.
 */
class SecurityReview {

  /**
   * What was reviewed.
   */
  private final Subject subject;

  /**
   * When the review was done.
   */
  private final Date date;

  /**
   * Create a new review.
   *
   * @param subject What was reviewed.
   * @param date Whe the review wes done.
   */
  SecurityReview(Subject subject, Date date) {
    this.subject = Objects.requireNonNull(subject, "Subject can't be null!");
    this.date = Objects.requireNonNull(date, "Date can't be null!");
  }

  /**
   * Returns what was reviewed.
   *
   * @return What was reviewed.
   */
  public Subject subject() {
    return subject;
  }

  /**
   * Returns a date when the review was done.
   *
   * @return A date of review.
   */
  public Date date() {
    return date;
  }
}

/**
 * A set of security reviews.
 */
class SecurityReviews implements Set<SecurityReview> {

  /**
   * Security reviews.
   */
  private final Set<SecurityReview> elements = new HashSet<>();

  /**
   * Create a set of security reviews.
   *
   * @param elements Reviews to be added to the new set.
   */
  public SecurityReviews(SecurityReview... elements) {
    Objects.requireNonNull(elements, "Elements can't be null!");
    this.elements.addAll(asList(elements));
  }

  /**
   * Create a set of security reviews.
   *
   * @param reviews Reviews to be added to the new set.
   */
  public SecurityReviews(SecurityReviews reviews) {
    Objects.requireNonNull(reviews, "Reviews can't be null!");
    this.elements.addAll(reviews.elements);
  }

  @Override
  public int size() {
    return elements.size();
  }

  @Override
  public boolean isEmpty() {
    return elements.isEmpty();
  }

  @Override
  public boolean contains(Object o) {
    return elements.contains(o);
  }

  @Override
  public Iterator<SecurityReview> iterator() {
    return elements.iterator();
  }

  @Override
  public Object[] toArray() {
    return elements.toArray();
  }

  @Override
  public <T> T[] toArray(T[] a) {
    return elements.toArray(a);
  }

  @Override
  public boolean add(SecurityReview review) {
    return elements.add(review);
  }

  @Override
  public boolean remove(Object element) {
    return elements.remove(element);
  }

  @Override
  public boolean containsAll(Collection<?> elements) {
    return this.elements.containsAll(elements);
  }

  @Override
  public boolean addAll(Collection<? extends SecurityReview> elements) {
    return this.elements.addAll(elements);
  }

  @Override
  public boolean retainAll(Collection<?> elements) {
    return this.elements.retainAll(elements);
  }

  @Override
  public boolean removeAll(Collection<?> elements) {
    return this.elements.removeAll(elements);
  }

  @Override
  public void clear() {
    elements.clear();
  }
}

/**
 * A features that holds security reviews.
 */
class SecurityReviewsFeature extends AbstractFeature<SecurityReviews> {

  /**
   * Initializes a feature.
   *
   * @param name The feature name.
   */
  public SecurityReviewsFeature(String name) {
    super(name);
  }

  @Override
  public Value<SecurityReviews> value(SecurityReviews reviews) {
    return new SecurityReviewsValue(this, reviews);
  }

  @Override
  public Value<SecurityReviews> parse(String string) {
    throw new UnsupportedOperationException("Unfortunately I can't parse security reviews");
  }
}

/**
 * A value that holds security reviews.
 */
class SecurityReviewsValue extends AbstractKnownValue<SecurityReviews> {

  /**
   * A set of security reviews.
   */
  private final SecurityReviews reviews;

  /**
   * Create a value with security reviews.
   *
   * @param feature A features for the value.
   * @param reviews A set of security reviews.
   */
  public SecurityReviewsValue(SecurityReviewsFeature feature, SecurityReviews reviews) {
    super(feature);
    Objects.requireNonNull(reviews, "Reviews can't be null!");
    this.reviews = new SecurityReviews(reviews);
  }

  @Override
  public SecurityReviews get() {
    return new SecurityReviews(reviews);
  }
}
