package com.sap.oss.phosphor.fosstars.model.value;

import static com.sap.oss.phosphor.fosstars.model.other.Utils.setOf;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

/**
 * A collection of vulnerabilities.
 */
public class Vulnerabilities implements Iterable<Vulnerability> {

  /**
   * A set of vulnerabilities.
   */
  private final Set<Vulnerability> entries;

  /**
   * Initializes an instance of {@link Vulnerabilities}.
   *
   * @param entries A number of vulnerabilities.
   */
  public Vulnerabilities(Vulnerability... entries) {
    this(entries != null ? setOf(entries) : new HashSet<>());
  }

  /**
   * Initializes an instance of {@link Vulnerabilities}.
   *
   * @param entries A set of vulnerabilities.
   */
  @JsonCreator
  public Vulnerabilities(@JsonProperty("entries") Set<Vulnerability> entries) {
    Objects.requireNonNull(entries, "You are not supposed to give me a null!");
    this.entries = entries;
  }

  /**
   * Converts to a set.
   *
   * @return A set of vulnerabilities.
   */
  @JsonGetter("entries")
  public Set<Vulnerability> entries() {
    return Collections.unmodifiableSet(entries);
  }

  /**
   * Tells whether the collection is empty or not.
   *
   * @return True if the collection is empty, false otherwise.
   */
  @JsonIgnore
  public boolean isEmpty() {
    return entries.isEmpty();
  }

  /**
   * Adds a vulnerability.
   *
   * @param vulnerability The vulnerability to be added.
   */
  public void add(Vulnerability vulnerability) {
    entries.add(vulnerability);
  }

  /**
   * Adds vulnerabilities.
   *
   * @param vulnerabilities The vulnerabilities to be added.
   */
  public void add(Vulnerabilities vulnerabilities) {
    this.entries.addAll(vulnerabilities.entries);
  }

  /**
   * Number of vulnerabilities.
   *
   * @return number of vulnerabilities
   */
  public int size() {
    return this.entries.size();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o instanceof Vulnerabilities == false) {
      return false;
    }
    Vulnerabilities that = (Vulnerabilities) o;
    return Objects.equals(entries, that.entries);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(entries);
  }

  @Override
  public String toString() {
    int n = entries.size();
    return String.format("%d %s", n, n == 1 ? "vulnerability" : "vulnerabilities");
  }

  @Override
  public Iterator<Vulnerability> iterator() {
    return entries.iterator();
  }
}
