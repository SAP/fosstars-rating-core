package com.sap.sgs.phosphor.fosstars.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sap.sgs.phosphor.fosstars.model.other.MakeImmutable;
import com.sap.sgs.phosphor.fosstars.model.rating.example.SecurityRatingExample;
import com.sap.sgs.phosphor.fosstars.model.rating.oss.OssSecurityRating;
import com.sap.sgs.phosphor.fosstars.model.rating.oss.OssSecurityRating.Thresholds;
import com.sap.sgs.phosphor.fosstars.model.score.oss.OssSecurityScore;
import com.sap.sgs.phosphor.fosstars.model.score.oss.ProjectSecurityTestingScore;
import com.sap.sgs.phosphor.fosstars.model.weight.ScoreWeights;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
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
  private final Map<Class, Rating> ratings = new HashMap<>();

  /**
   * This constructor loads all available ratings.
   */
  private RatingRepository() {
    register(() -> load(
        "com/sap/sgs/phosphor/fosstars/model/rating/example/SecurityRatingExample.json",
        SecurityRatingExample.class));

    register(() -> {
      OssSecurityScore ossSecurityScore = new OssSecurityScore();
      ossSecurityScore.weights().update(
          loadScoreWeights("com/sap/sgs/phosphor/fosstars/model/score/oss/"
              + "OssSecurityScoreWeights.json"));

      Optional<ProjectSecurityTestingScore> projectSecurityTestingScore =
          ossSecurityScore.subScore(ProjectSecurityTestingScore.class);
      if (!projectSecurityTestingScore.isPresent()) {
        throw new IllegalStateException(
            "Oh no! Could not find the project security testing score!");
      }

      projectSecurityTestingScore.get().weights().update(
          loadScoreWeights("com/sap/sgs/phosphor/fosstars/model/score/oss/"
              + "ProjectSecurityTestingScoreWeights.json"));

      Thresholds thresholds = load(
          "com/sap/sgs/phosphor/fosstars/model/rating/oss/OssSecurityRatingThresholds.json",
          Thresholds.class);

      return new OssSecurityRating(ossSecurityScore, thresholds);
    });
  }

  /**
   * Returns the latest version of a rating specified by a class.
   *
   * @param clazz The class of the rating.
   * @param <T> The type of the rating.
   * @return An instance of rating of the specified class.
   * @throws IllegalArgumentException If no rating of the specified class was found.
   */
  public <T extends Rating> T rating(Class<T> clazz) {
    Objects.requireNonNull(clazz, "You just gave me a null instead of class!");

    Rating rating = ratings.get(clazz);
    if (rating == null) {
      throw new IllegalArgumentException(
          String.format("Oh no! Could not find %s", clazz.getCanonicalName()));
    }

    return clazz.cast(rating);
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
  public void register(Rating rating) {
    Objects.requireNonNull(rating, "Oh no! The rating is null");
    rating.accept(new MakeImmutable());
    ratings.put(rating.getClass(), rating);
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

    throw new IOException(String.format("Could not load %s from %s", clazz.getSimpleName(), file));
  }

  /**
   * Load score weights from a file.
   *
   * @param path A path to the file.
   * @return The loaded weights.
   * @throws IOException If something went wrong.
   */
  private static ScoreWeights loadScoreWeights(String path) throws IOException {
    return load(path, ScoreWeights.class);
  }
}
