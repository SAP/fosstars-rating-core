package com.sap.sgs.phosphor.fosstars.model;

import static com.sap.sgs.phosphor.fosstars.model.Version.OSS_SECURITY_RATING_1_0;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sap.sgs.phosphor.fosstars.model.other.MakeImmutable;
import com.sap.sgs.phosphor.fosstars.model.rating.example.SecurityRatingExample;
import com.sap.sgs.phosphor.fosstars.model.rating.oss.OssSecurityRating;
import com.sap.sgs.phosphor.fosstars.model.score.oss.OssSecurityScore;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This is a repository for all available ratings.
 */
public class RatingRepository {

  /**
   * An interface of a factory that can create a rating.
   */
  private interface RatingFactory {

    /**
     * Create a new rating.
     *
     * @return A new rating.
     * @throws IOException If something went wrong.
     */
    Rating create() throws IOException;
  }

  /**
   * A logger.
   */
  private static final Logger LOGGER = LogManager.getLogger(RatingRepository.class);

  /**
   * An ObjectMapper for serialization and deserialization ratings to JSON.
   */
  private static final ObjectMapper MAPPER = new ObjectMapper();

  /**
   * Singleton.
   */
  public static final RatingRepository INSTANCE = new RatingRepository();

  /**
   * A mapping from a version to a rating.
   */
  private final Map<Version, Rating> ratings = new EnumMap<>(Version.class);

  /**
   * This constructor loads all available ratings.
   */
  private RatingRepository() {
    register(() -> load(
        "com/sap/sgs/phosphor/fosstars/model/rating/example/SecurityRatingExample_1_1.json",
        SecurityRatingExample.class));

    register(() -> {
      OssSecurityScore score = load(
          "com/sap/sgs/phosphor/fosstars/model/score/oss/OssSecurityScore_1_0.json",
          OssSecurityScore.class);
      return new OssSecurityRating(score, OSS_SECURITY_RATING_1_0);
    });
  }

  /**
   * Looks for a rating with a specified version.
   *
   * @param version The version to look for.
   * @return A rating of the specified version.
   */
  public Rating rating(Version version) {
    return ratings.get(version);
  }

  /**
   * Looks for a specific type of rating with the specified version.
   *
   * @param version The version to search for.
   * @param clazz The type of rating.
   * @return A rating of the specified type and with the specified version.
   * @throws IllegalArgumentException If such a rating was not found.
   */
  public <T extends Rating> T rating(Version version, Class<T> clazz) {
    Objects.requireNonNull(version, "Version is null!");
    Objects.requireNonNull(clazz, "Clazz is null");
    Rating rating = rating(version);
    if (rating.getClass() != clazz) {
      throw new IllegalArgumentException("Classes don't match!");
    }
    return clazz.cast(rating);
  }

  /**
   * Returns the latest version of a rating specified by a class.
   *
   * @param clazz The class of the rating.
   * @return An instance of rating of the specified class.
   * @throws IllegalArgumentException If no rating of the specified class was found.
   */
  public <T extends Rating> T rating(Class<T> clazz) {
    Objects.requireNonNull(clazz, "You just gave me a null instead of class!");

    Rating currentRating = null;
    Version currentVersion = null;
    for (Map.Entry<Version, Rating> entry : ratings.entrySet()) {
      Version version = entry.getKey();
      Rating rating = entry.getValue();
      if (clazz != rating.getClass()) {
        continue;
      }
      if (currentVersion == null || version.ordinal() > currentVersion.ordinal()) {
        currentRating = rating;
        currentVersion = version;
      }
    }

    if (currentRating == null) {
      throw new IllegalArgumentException(String.format(
          "Looks like we don't have the rating %s", clazz.getCanonicalName()));
    }

    return clazz.cast(currentRating);
  }

  /**
   * Calls a rating factory to create a rating,
   * and then registers the created rating in the repository.
   *
   * @param factory The rating factory.
   */
  private void register(RatingFactory factory) {
    try {
      register(factory.create());
    } catch (IOException e) {
      LOGGER.warn("Initialization failed", e);
    }
  }

  /**
   * Registers a new rating in the repository. The method makes the rating immutable.
   *
   * @param rating The rating to be registered.
   */
  private void register(Rating rating) {
    if (rating.getClass() != rating.version().clazz) {
      throw new IllegalArgumentException("Hey! Classes should match!");
    }
    rating.accept(new MakeImmutable());
    ratings.put(rating.version(), rating);
  }

  /**
   * Stores a rating to a file.
   *
   * @param rating The rating to be stored.
   * @param path The path to a file.
   * @throws IOException If something went wrong.
   */
  public void store(Rating rating, String path) throws IOException {
    store(rating, Paths.get(path));
  }

  /**
   * Stores a rating to a file.
   *
   * @param rating The rating to be stored.
   * @param path The path to a file.
   * @throws IOException If something went wrong.
   */
  private void store(Rating rating, Path path) throws IOException {
    Files.write(path, MAPPER.writerWithDefaultPrettyPrinter().writeValueAsBytes(rating));
  }

  /**
   * Stores a score to a file.
   *
   * @param score The score to be stored.
   * @param path The path to a file.
   * @throws IOException If something went wrong.
   */
  public void store(Score score, String path) throws IOException {
    store(score, Paths.get(path));
  }

  /**
   * Stores a score to a file.
   *
   * @param score The score to be stored.
   * @param path The path to a file.
   * @throws IOException If something went wrong.
   */
  private void store(Score score, Path path) throws IOException {
    Files.write(path, MAPPER.writerWithDefaultPrettyPrinter().writeValueAsBytes(score));
  }

  /**
   * Loads a serialized object from a resource specified by a path. First, the method checks
   * if the path points to an existing file,
   * and if so, the method tries to load the object from the file.
   * If the path doesn't point to an existing file,
   * then the method tries to load the object from a resource.
   *
   * @param path The path to a stored rating.
   * @param clazz The class of the object to be loaded.
   * @param <T> The type of the object.
   * @return The loaded object.
   * @throws IOException If the object can't be loaded
   * @throws NullPointerException If the specified path is null
   */
  private static <T> T load(String path, Class<T> clazz) throws IOException {
    Objects.requireNonNull(path, "Hey! Path can't be null!");

    File file = Paths.get(path.replace('/', File.separatorChar)).toFile();
    if (file.exists()) {
      return MAPPER.readValue(file, clazz);
    }

    InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
    if (is != null) {
      try {
        return MAPPER.readValue(is, clazz);
      } finally {
        is.close();
      }
    }

    throw new IOException(String.format("Could not load a rating from %s", file));
  }
}
