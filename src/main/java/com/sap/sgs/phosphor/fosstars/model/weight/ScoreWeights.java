package com.sap.sgs.phosphor.fosstars.model.weight;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import com.sap.sgs.phosphor.fosstars.model.Score;
import com.sap.sgs.phosphor.fosstars.model.Tunable;
import com.sap.sgs.phosphor.fosstars.model.Weight;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * A collection of weights for scores.
 */
public class ScoreWeights implements Tunable {

  /**
   * A factory for parsing YAML.
   */
  static final YAMLFactory YAML_FACTORY;

  /**
   * For serialization and deserialization in YAML.
   */
  static final ObjectMapper YAML_OBJECT_MAPPER;

  static {
    YAML_FACTORY = new YAMLFactory();
    YAML_FACTORY.disable(YAMLGenerator.Feature.USE_NATIVE_TYPE_ID);
    YAML_OBJECT_MAPPER = new ObjectMapper(YAML_FACTORY);
    YAML_OBJECT_MAPPER.findAndRegisterModules();
  }

  private static final ObjectMapper JSON_OBJECT_MAPPER = new ObjectMapper();

  /**
   * The default weight for sub-scores.
   */
  static final double DEFAULT_WEIGHT = 1.0;

  /**
   * Maps a score type to its weight.
   */
  private final Map<Class<? extends Score>, Weight> values;

  /**
   * Creates a collection of weights for a number of scores.
   *
   * @param scores The scores.
   * @return The collection of weights.
   */
  public static ScoreWeights createFor(Score... scores) {
    Objects.requireNonNull(scores, "Scores can't be null!");
    Map<Class<? extends Score>, Weight> values = new HashMap<>();
    for (Score score : scores) {
      values.put(score.getClass(), new MutableWeight(DEFAULT_WEIGHT));
    }
    return new ScoreWeights(values);
  }

  /**
   * Creates a collection of weights for a set of scores.
   *
   * @param scores The scores.
   * @return The collection of weights.
   */
  public static ScoreWeights createFor(Set<Score> scores) {
    Objects.requireNonNull(scores, "Scores can't be null!");
    return createFor(scores.toArray(new Score[0]));
  }

  /**
   * Initializes a new collection of weights.
   *
   * @param values The weights.
   */
  @JsonCreator
  public ScoreWeights(@JsonProperty("values") Map<Class<? extends Score>, Weight> values) {
    Objects.requireNonNull(values, "Weights can't be null!");
    this.values = values;
  }

  /**
   * Creates a weight for a score.
   *
   * @param score The score.
   * @return A weight for a score if it exists.
   */
  public Optional<Weight> of(Score score) {
    return of(score.getClass());
  }

  /**
   * Creates a weight for a score of specified type.
   *
   * @param clazz The type.
   * @return A weight for a score type if it exists.
   */
  public Optional<Weight> of(Class<? extends Score> clazz) {
    return Optional.ofNullable(values.get(clazz));
  }

  /**
   * Get a number of weights.
   *
   * @return A number of weights.
   */
  public int size() {
    return values.size();
  }

  /**
   * Sets a weight for a score.
   *
   * @param score The score.
   * @param weight The weight.
   */
  public void set(Score score, Weight weight) {
    values.put(score.getClass(), weight);
  }

  /**
   * Sets a weight for a score.
   *
   * @param score A type of the score.
   * @param weight The weight.
   */
  public void set(Class<? extends Score> score, Weight weight) {
    values.put(score, weight);
  }

  /**
   * Update the weights.
   *
   * @param weights The new weights.
   */
  public void update(ScoreWeights weights) {
    Objects.requireNonNull(weights, "Oh no! Weights is null!");
    for (Map.Entry<Class<? extends  Score>, Weight> entry : weights.values.entrySet()) {
      Class<? extends Score> scoreClass = entry.getKey();
      if (!this.values.containsKey(scoreClass)) {
        throw new IllegalArgumentException(
            String.format("Oh no! Could not find %s", scoreClass.getCanonicalName()));
      }
      Weight weight = entry.getValue();
      set(scoreClass, weight);
    }
  }

  /**
   * This method exists to make Jackson happy.
   */
  @JsonGetter("values")
  private Map<Class, Weight> values() {
    return new HashMap<>(values);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o instanceof ScoreWeights == false) {
      return false;
    }
    ScoreWeights weights = (ScoreWeights) o;
    return Objects.equals(values, weights.values);
  }

  @Override
  public int hashCode() {
    return Objects.hash(values);
  }

  @Override
  public List<Weight> parameters() {
    List<Weight> parameters = new ArrayList<>();
    for (Weight weight : values.values()) {
      parameters.add(weight);
    }
    return parameters;
  }

  @Override
  @JsonIgnore
  public boolean isImmutable() {
    for (Weight weight : values.values()) {
      if (!weight.isImmutable()) {
        return false;
      }
    }
    return true;
  }

  @Override
  public void makeImmutable() {
    for (Map.Entry<Class<? extends Score>, Weight> entry : values.entrySet()) {
      if (!entry.getValue().isImmutable()) {
        values.put(entry.getKey(), new ImmutableWeight(entry.getValue().value()));
      }
    }
  }

  /**
   * Loads weights from YAML.
   *
   * @param is An input stream with data.
   * @return The weights.
   * @throws IOException If something went wrong.
   */
  public static ScoreWeights loadWeightsFromYaml(InputStream is) throws IOException {
    return YAML_OBJECT_MAPPER.readValue(is, ScoreWeights.class);
  }

  /**
   * Stores the weights to a JSON file.
   *
   * @param file The file.
   * @throws IOException If something went wrong.
   */
  public void storeToJson(String file) throws IOException {
    Files.write(
        Paths.get(file),
        JSON_OBJECT_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsBytes(this));
  }
}
