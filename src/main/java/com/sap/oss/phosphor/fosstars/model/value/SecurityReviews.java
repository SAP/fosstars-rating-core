package com.sap.oss.phosphor.fosstars.model.value;

import static java.util.Arrays.asList;

import com.fasterxml.jackson.annotation.JsonGetter;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

/**
 * A set of security reviews.
 */
public class SecurityReviews implements Set<SecurityReview> {

  /**
   * Returns an empty set of security reviews.
   *
   * @return An empty set of security reviews.
   */
  public static SecurityReviews noReviews() {
    return new SecurityReviews();
  }

  /**
   * Security reviews.
   */
  private final Set<SecurityReview> elements = new HashSet<>();

  /**
   * Create an empty set of security reviews.
   */
  public SecurityReviews() {

  }

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
   * Returns a set of security reviews.
   *
   * @return A set of security reviews.
   */
  @JsonGetter("elements")
  public Set<SecurityReview> elements() {
    return new HashSet<>(elements);
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o != null && !SecurityReviews.class.isAssignableFrom(o.getClass())) {
      return false;
    }
    SecurityReviews that = (SecurityReviews) o;
    return Objects.equals(elements, that.elements);
  }

  @Override
  public int hashCode() {
    return Objects.hash(elements);
  }

  @Override
  public String toString() {
    return String.format("%d security review%s", size(), size() == 1 ? "" : "s");
  }
}
