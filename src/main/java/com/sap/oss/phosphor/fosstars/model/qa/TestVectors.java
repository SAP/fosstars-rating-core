package com.sap.oss.phosphor.fosstars.model.qa;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.util.Yaml;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * A collection of test vectors.
 */
public class TestVectors implements Iterable<TestVector> {

  /**
   * No default values.
   */
  private static final Set<Value<?>> NO_DEFAULTS = Collections.emptySet();

  /**
   * A list of test vectors.
   */
  private final List<TestVector> elements;

  /**
   * A set of default values.
   */
  private final Set<Value<?>> defaults;

  /**
   * Initializes a new collection of test vectors.
   *
   * @param vectors The test vectors.
   */
  public TestVectors(TestVector... vectors) {
    this(Arrays.asList(vectors), NO_DEFAULTS);
  }

  /**
   * Initializes a collection of test vectors.
   *
   * @param vectors The test vectors.
   * @param defaults A set of default values.
   */
  public TestVectors(
      @JsonProperty("elements") List<TestVector> vectors,
      @JsonProperty("defaults") Set<Value<?>> defaults) {

    Objects.requireNonNull(vectors, "Hey! Vectors can't be null!");
    Objects.requireNonNull(defaults, "Hey! Defaults can't be null!");
    this.elements = new ArrayList<>(vectors);
    this.defaults = new HashSet<>(defaults);
  }

  /**
   * Adds a test vector to the collection.
   *
   * @param vector The test vector to be added.
   */
  public void add(TestVector vector) {
    elements.add(vector);
  }

  /**
   * Adds test vectors to the collection.
   *
   * @param vectors The test vectors to be added.
   */
  public void add(TestVectors vectors) {
    for (TestVector vector : vectors.elements) {
      add(vector);
    }
  }

  /**
   * Get a test vectors by its index.
   *
   * @param i The index.
   * @return The test vector.
   */
  public TestVector get(int i) {
    return new TestVectorWithDefaults(elements.get(i), defaults);
  }

  /**
   * Checks if the collection of test vectors is empty.
   *
   * @return True if the collection is empty, false otherwise.
   */
  @JsonIgnore
  public boolean isEmpty() {
    return elements.isEmpty();
  }

  /**
   * Get a size of the collection.
   *
   * @return A size of the collection.
   */
  @JsonIgnore
  public int size() {
    return elements.size();
  }

  /**
   * This getter is to make Jackson happy.
   */
  @JsonGetter("elements")
  private List<TestVector> elements() {
    return new ArrayList<>(elements);
  }

  /**
   * This getter is to make Jackson happy.
   */
  @JsonGetter("defaults")
  private Set<Value<?>> defaults() {
    return new HashSet<>(defaults);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o instanceof TestVectors == false) {
      return false;
    }
    TestVectors that = (TestVectors) o;
    return Objects.equals(elements, that.elements)
        && Objects.equals(defaults, that.defaults);
  }

  @Override
  public int hashCode() {
    return Objects.hash(elements, defaults);
  }

  @Override
  public Iterator<TestVector> iterator() {
    return vectorsWithDefaults().iterator();
  }

  /**
   * Wraps the original test vectors into {@link TestVectorWithDefaults}.
   */
  private List<TestVector> vectorsWithDefaults() {
    List<TestVector> list = new ArrayList<>();
    for (TestVector vector : elements) {
      list.add(new TestVectorWithDefaults(vector, defaults));
    }

    return list;
  }

  /**
   * Loads a list of test vectors from a YAML file.
   *
   * @param filename The filename.
   * @return A list of loaded test vectors.
   * @throws IOException If something went wrong (file doesn't exist, the content is wrong, etc).
   */
  public static TestVectors loadFromYaml(Path filename) throws IOException {
    Objects.requireNonNull(filename, "Filename can't be null!");
    try (InputStream is = Files.newInputStream(filename)) {
      return loadFromYaml(is);
    }
  }

  /**
   * Loads a list of test vectors from YAML.
   *
   * @param is An input stream with YAML.
   * @return A list of test vectors.
   * @throws IOException If something went wrong.
   */
  public static TestVectors loadFromYaml(InputStream is) throws IOException {
    Objects.requireNonNull(is, "Input stream can't be null!");
    return Yaml.read(is, TestVectors.class);
  }

  /**
   * Stores a list of test vectors to a YAML file.
   *
   * @param filename The filename.
   * @throws IOException If something went wrong.
   */
  public void storeToYaml(Path filename) throws IOException {
    Objects.requireNonNull(filename, "Filename can't be null!");
    Files.write(filename, Yaml.toBytes(this));
  }

}
