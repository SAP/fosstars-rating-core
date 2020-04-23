package com.sap.sgs.phosphor.fosstars.model.qa;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import com.sap.sgs.phosphor.fosstars.model.Feature;
import com.sap.sgs.phosphor.fosstars.model.Label;
import com.sap.sgs.phosphor.fosstars.model.Value;
import com.sap.sgs.phosphor.fosstars.model.math.DoubleInterval;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

/**
 * This is a base class for verification procedures for both ratings and scores.
 */
public abstract class AbstractVerification {

  /**
   * A factory for parsing YAML.
   */
  private static final YAMLFactory YAML_FACTORY;

  /**
   * For serialization and deserialization in YAML.
   */
  private static final ObjectMapper YAML_OBJECT_MAPPER;

  /**
   * For serialization and deserialization of test vectors.
   */
  private static final TypeReference<List<TestVector>> TEST_VECTOR_LIST_TYPE_REFERENCE
      = new TypeReference<List<TestVector>>() {};

  static {
    YAML_FACTORY = new YAMLFactory();
    YAML_FACTORY.disable(YAMLGenerator.Feature.USE_NATIVE_TYPE_ID);
    YAML_OBJECT_MAPPER = new ObjectMapper(YAML_FACTORY);
    YAML_OBJECT_MAPPER.findAndRegisterModules();
  }

  /**
   * An interface of a parser which can convert a string to a label.
   */
  public interface LabelParser {
    Label parse(String string);
  }

  /**
   * No label in a test vector.
   */
  private static final Label NO_LABEL = null;

  /**
   * This label parser just returns no label.
   * It's used for parsing test vectors for scores.
   */
  private static final LabelParser DUMMY_LABEL_PARSER = string -> NO_LABEL;

  /**
   * A list of test vectors.
   */
  protected final List<TestVector> vectors;

  /**
   * Initializes a {@link AbstractVerification} with a list of test vectors.
   *
   * @param vectors A list of test vectors.
   */
  AbstractVerification(List<TestVector> vectors) {
    this.vectors = check(vectors);
  }

  /**
   * Returns the test vectors.
   */
  public List<TestVector> vectors() {
    return new ArrayList<>(vectors);
  }

  /**
   * First, the method loads test vectors from an input stream.
   * The method expects the test vectors to be in CSV format.
   * The method expects only specified features.
   * The method ignores labels in the test vectors.
   *
   * @param features A list of expected features.
   * @param is An input stream with test vectors in CSV format.
   * @return A list of loaded test vectors.
   * @throws IOException If something went wrong.
   */
  public static <T extends Feature> List<TestVector> loadTestVectorsFromCsvResource(
      Collection<T> features, InputStream is) throws IOException {

    return loadTestVectorsFromCsvResource(features, is, DUMMY_LABEL_PARSER);
  }

  /**
   * First, the method loads test vectors from an input stream.
   * The method expects the test vectors to be in CSV format.
   * The method expects only specified features.
   *
   * @param features A list of expected features.
   * @param is An input stream with test vectors in CSV format.
   * @param labelParser Converts a string to a label.
   * @return A list of loaded test vectors.
   * @throws IOException If something went wrong.
   */
  public static <T extends Feature> List<TestVector> loadTestVectorsFromCsvResource(
      Collection<T> features, InputStream is, LabelParser labelParser) throws IOException {

    Objects.requireNonNull(is, "Input stream can't be null!");
    Objects.requireNonNull(labelParser, "Label parser can't be null!");
    Objects.requireNonNull(features, "Features can't be null!");

    if (features.isEmpty()) {
      throw new IllegalArgumentException("Features can't be empty!");
    }

    List<TestVector> vectors = new ArrayList<>();
    try (Reader reader = new InputStreamReader(is)) {
      Iterable<CSVRecord> records = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(reader);
      for (CSVRecord record : records) {
        DoubleInterval expectedScore = DoubleInterval.closed(
            Double.valueOf(record.get("score_from")),
            Double.valueOf(record.get("score_to")));

        String label = record.get("label");
        Label expectedLabel = null;
        if (!"none".equalsIgnoreCase(label) && !label.isEmpty()) {
          expectedLabel = labelParser.parse(label);
        }

        String alias = record.get("alias");

        Set<Value> values = new HashSet<>();
        for (Feature feature : features) {
          String raw = findValue(feature, record);

          if ("unknown".equalsIgnoreCase(raw)) {
            values.add(feature.unknown());
            continue;
          }

          values.add(feature.parse(raw));
        }

        vectors.add(new TestVector(values, expectedScore, expectedLabel, alias));
      }
    }
    return vectors;
  }

  /**
   * Loads a list of test vectors from a YAML file.
   *
   * @param filename The filename.
   * @return A list of loaded test vectors.
   * @throws IOException If something went wrong (file doesn't exist, the content is wrong, etc).
   */
  public static List<TestVector> loadTestVectorsFromYaml(Path filename) throws IOException {
    Objects.requireNonNull(filename, "Filename can't be null!");
    return loadTestVectorsFromYaml(Files.newInputStream(filename));
  }

  /**
   * Loads a list of test vectors from YAML.
   *
   * @param is An input stream with YAML.
   * @return A list of test vectors.
   * @throws IOException If something went wrong.
   */
  public static List<TestVector> loadTestVectorsFromYaml(InputStream is) throws IOException {
    Objects.requireNonNull(is, "Input stream can't be null!");
    return YAML_OBJECT_MAPPER.readValue(is, TEST_VECTOR_LIST_TYPE_REFERENCE);
  }

  /**
   * Stores a list of test vectors to a YAML file.
   *
   * @param vectors The test vectors.
   * @param filename The filename.
   * @throws IOException If something went wrong.
   */
  public static void storeTestVectorsToYaml(List<TestVector> vectors, Path filename)
      throws IOException {

    Objects.requireNonNull(vectors, "Test vectors can't be null!");
    Objects.requireNonNull(filename, "Filename can't be null!");
    Files.write(filename, YAML_OBJECT_MAPPER.writeValueAsBytes(vectors));
  }

  /**
   * Looks for a feature value in a CSV record.
   *
   * @param feature The feature.
   * @param record The record.
   * @return The feature value if found.
   * @throws IOException If no value found.
   */
  private static String findValue(Feature feature, CSVRecord record) throws IOException {
    for (Map.Entry<String, String> entry : record.toMap().entrySet()) {
      if (entry.getKey().equals(feature.name())) {
        return entry.getValue().trim();
      }
    }
    throw new IOException(String.format("Couldn't find the feature '%s' in test vector '%s'",
        feature.name(), record.get("alias")));
  }

  /**
   * Checks if a list of test vectors is not empty and doesn't contain duplicate entries.
   *
   * @param vectors A list of test vectors to check.
   * @return The same list of test vectors if the check passed.
   */
  private static List<TestVector> check(List<TestVector> vectors) {
    Objects.requireNonNull(vectors, "Test vectors can't be null!");
    if (vectors.isEmpty()) {
      throw new IllegalArgumentException(
          "Hey! You are not supposed to give me an empty list of test vectors!");
    }
    Set<TestVector> set = new HashSet<>(vectors);
    if (set.size() != vectors.size()) {
      throw new IllegalArgumentException("Hey! Looks like you gave me duplicate test vectors!");
    }
    return vectors;
  }

}
