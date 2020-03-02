package com.sap.sgs.phosphor.fosstars.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sap.sgs.phosphor.fosstars.model.rating.example.SecurityRatingExample;
import com.sap.sgs.phosphor.fosstars.model.rating.oss.OssSecurityRating;
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
   * A logger.
   */
  private static final Logger LOGGER = LogManager.getLogger(RatingRepository.class);

  /**
   * An ObjectMapper for serialization and deserialization ratings to JSON.
   */
  private static final ObjectMapper MAPPER = new ObjectMapper();

  static {
    MAPPER.enableDefaultTyping();
  }

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
    add(Version.SECURITY_RATING_EXAMPLE_1_1, SecurityRatingExample.class);
    add(Version.OSS_SECURITY_RATING_1_0, OssSecurityRating.class);
  }

  /**
   * Searches for a rating with the specified version. The method has to return an immutable rating.
   *
   * @param version The version to search for.
   * @return A rating of the specified version.
   */
  public Rating get(Version version) {
    return ratings.get(version);
  }

  /**
   * Searches for a specific type of rating with the specified version. The method has to return
   * an immutable rating.
   *
   * @param version The version to search for.
   * @param clazz The type of rating.
   * @return A rating of the specified type and with the specified version.
   * @throws IllegalArgumentException If such a rating was not found.
   */
  public <T extends Rating> T get(Version version, Class<T> clazz) {
    Objects.requireNonNull(version, "Version is null!");
    Objects.requireNonNull(clazz, "Clazz is null");
    Rating rating = get(version);
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
  public <T extends Rating> T get(Class<T> clazz) {
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

  private void add(Version version, Class<? extends Rating> clazz) {
    try {
      Rating rating = load(version, clazz);
      ratings.put(version, rating);
    } catch (IOException e) {
      LOGGER.error("Initialization failed:", e);
    }
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
   * Loads a rating of a particular type and version.
   *
   * @param version The version of a rating to be loaded.
   * @param clazz The class of a rating to be loaded.
   * @param <T> The type of a {@link Rating}.
   * @return A rating of the specified type and version.
   * @throws IOException If a rating couldn't be loaded.
   * @throws IllegalArgumentException If the rating with the specified version was found, but
   *                                  its type doesn't match with the specified class.
   */
  private static <T extends Rating> T load(Version version, Class<T> clazz) throws IOException {
    Rating rating = load(version);
    if (rating.getClass() != clazz) {
      throw new IllegalArgumentException("Classes don't match!");
    }
    return clazz.cast(rating);
  }

  /**
   * Loads a rating of a particular {@link Version}. First, the method checks
   * if the {@link Version#path} points to an existing file,
   * and if so, the method tries to load a rating from the file.
   * If the {@link Version#path} doesn't point to an existing file, then the method tries to load
   * the {@link Version#path} as a resource.
   *
   * @param version The version
   * @return The loaded rating
   * @throws IOException If a rating can't be loaded
   * @throws NullPointerException If the specified version is null
   */
  static Rating load(Version version) throws IOException {
    Objects.requireNonNull(version, "Hey! Version can't be null!");
    File file = Paths.get(version.path.replace('/', File.separatorChar)).toFile();
    if (file.exists()) {
      return MAPPER.readValue(file, Rating.class);
    } else {
      InputStream is = Thread.currentThread().getContextClassLoader()
          .getResourceAsStream(version.path);
      if (is != null) {
        try {
          return MAPPER.readValue(is, Rating.class);
        } finally {
          is.close();
        }
      }
    }
    throw new IOException(String.format("Could not load a rating for %s from %s", version, file));
  }
}
