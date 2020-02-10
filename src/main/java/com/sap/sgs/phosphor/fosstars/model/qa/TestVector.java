package com.sap.sgs.phosphor.fosstars.model.qa;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sap.sgs.phosphor.fosstars.model.Interval;
import com.sap.sgs.phosphor.fosstars.model.Label;
import com.sap.sgs.phosphor.fosstars.model.Value;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * A test vector for a rating which contains feature values, an interval for an expected score
 * and an expected label. The class is immutable.
 */
public class TestVector {

  private static final Label NO_LABEL = null;

  /**
   * An ObjectMapper for serialization and deserialization.
   */
  private static final ObjectMapper MAPPER = new ObjectMapper();

  /**
   * A set of feature values.
   */
  private final Set<Value> values;

  /**
   * An interval for an expected score.
   */
  private final Interval expectedScore;

  /**
   * An expected label.
   */
  private final Label expectedLabel;

  /**
   * A alias of the test vector.
   */
  private final String alias;

  /**
   * Initializes a new {@link TestVector}.
   *
   * @param values A set of feature values.
   * @param expectedScore An interval for an expected score.
   * @param expectedLabel An expected label (can be null).
   * @param alias A alias of the test vector.
   */
  public TestVector(
      @JsonProperty("values") Set<Value> values,
      @JsonProperty("expectedScore") Interval expectedScore,
      @JsonProperty("expectedLabel") Label expectedLabel,
      @JsonProperty("alias") String alias) {

    Objects.requireNonNull(values, "Hey! Values can't be null!");
    Objects.requireNonNull(expectedScore, "Hey! Expected score can't be null!");
    Objects.requireNonNull(alias, "Hey! alias can't be null!");

    if (values.isEmpty()) {
      throw new IllegalArgumentException("Hey! Values can't be empty");
    }

    this.values = values;
    this.expectedScore = expectedScore;
    this.expectedLabel = expectedLabel;
    this.alias = alias;
  }

  /**
   * Returns the feature values.
   */
  @JsonGetter("values")
  public final Set<Value> values() {
    return Collections.unmodifiableSet(values);
  }

  /**
   * Returns the expected score.
   */
  @JsonGetter("expectedScore")
  public final Interval expectedScore() {
    return expectedScore;
  }

  /**
   * Checks if a score belongs to the expected interval.
   *
   * @param score The score to be checked.
   * @return True if the score belongs to the expected interval, false otherwise.
   */
  public boolean containsExpected(double score) {
    return expectedScore.contains(score);
  }

  /**
   * Checks if the test vector has a label.
   *
   * @return True if the test vector has a label, false otherwise.
   */
  public boolean hasLabel() {
    return expectedLabel != NO_LABEL;
  }

  /**
   * Returns the expected label.
   */
  @JsonGetter("expectedLabel")
  public final Label expectedLabel() {
    return expectedLabel;
  }

  /**
   * Returns the alias.
   */
  @JsonGetter("alias")
  public final String alias() {
    return alias;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o instanceof TestVector == false) {
      return false;
    }
    TestVector vector = (TestVector) o;
    return Objects.equals(values, vector.values)
        && Objects.equals(expectedScore, vector.expectedScore)
        && Objects.equals(expectedLabel, vector.expectedLabel)
        && Objects.equals(alias, vector.alias);
  }

  @Override
  public int hashCode() {
    return Objects.hash(values, expectedScore, expectedLabel, alias);
  }

  static void storeTestVectorsToJson(String filename, List<TestVector> vectors)
      throws IOException {

    Files.write(
        Paths.get(filename),
        MAPPER.writerWithDefaultPrettyPrinter().writeValueAsBytes(vectors));
  }

  static List<TestVector> loadTestVectorsFromJson(String filename)
      throws IOException {

    return loadTestVectorsFromJson(Files.newInputStream(Paths.get(filename)));
  }

  static List<TestVector> loadTestVectorsFromJson(InputStream is) throws IOException {
    return MAPPER.readValue(
        is,
        MAPPER.getTypeFactory().constructCollectionType(List.class, TestVector.class));
  }

}
