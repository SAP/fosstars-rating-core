package com.sap.sgs.phosphor.fosstars.model.other;

import com.sap.sgs.phosphor.fosstars.model.Feature;
import com.sap.sgs.phosphor.fosstars.model.Value;
import com.sap.sgs.phosphor.fosstars.model.value.UnknownValue;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;

public class Utils {

  // don't allow creating instances of this class
  private Utils() {

  }

  /**
   * Converts a number of objects to a set. An exception is thrown if there is any duplicate.
   *
   * @param objects to be added to a set.
   * @param <T> A type of the objects.
   * @return A set of objects.
   * @throws IllegalArgumentException if specified objects contain duplicates.
   */
  public static <T> Set<T> setOf(T... objects) {
    Objects.requireNonNull(objects, "Give me objects but not a null, please!");
    Set<T> set = new HashSet<>();
    for (T object : objects) {
      boolean added = set.add(object);
      if (!added) {
        throw new IllegalArgumentException(String.format(
            "You're supposed to give me unique objects! Found a dup: %s", object));
      }
    }
    return set;
  }

  /**
   * Checks if a set of feature values already contains a feature.
   *
   * @param values A set of values.
   * @param feature A feature.
   * @return True if the specified set of values contains the feature, false otherwise.
   */
  public static boolean contains(Set<Value> values, Feature feature) {
    Objects.requireNonNull(values, "Give me a set of values but not a null please!");
    Objects.requireNonNull(feature, "Give me a feature but not a null please!");
    for (Value value : values) {
      if (value.feature().equals(feature)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Takes a number of features, and returns as set of unknown values for those features.
   *
   * @param features An array of features.
   * @return A set of unknown values for the specified features.
   */
  public static Set<Value> allUnknown(Feature... features) {
    return allUnknown(Arrays.asList(features));
  }

  /**
   * Takes a number of features, and returns as set of unknown values for those features.
   *
   * @param features A collection of features.
   * @return A set of unknown values for the specified features.
   */
  public static Set<Value> allUnknown(Collection<Feature> features) {
    Objects.requireNonNull(features, "Hey! Give me features but not a null!");
    Set<Value> values = new HashSet<>();
    for (Feature feature : features) {
      values.add(UnknownValue.of(feature));
    }
    return values;
  }

  /**
   * Looks for a value of a specified feature in a set of values.
   *
   * @param values The set of values.
   * @param feature The feature.
   * @param <T> A type of the data in the value.
   * @return The value if it was found, null otherwise.
   */
  public static <T> Value<T> findValue(Set<Value> values, Feature feature) {
    return findValue(values.toArray(new Value[0]), feature);
  }

  private static <T> Value<T> findValue(Value[] values, Feature feature) {
    Objects.requireNonNull(feature, "Oh no! Feature can't be null!");
    Objects.requireNonNull(values, "Oh no! Feature values can't be null!");
    for (Value value : values) {
      if (feature.equals(value.feature())) {
        return value;
      }
    }

    return null;
  }

  /**
   * Looks for a value of the specified feature in a set of values.
   *
   * @param values The set of values.
   * @param feature The feature.
   * @param errorMessage The error message for an exception
   * @param <T> Type of the value.
   * @return The value of the specified feature.
   * @throws IllegalArgumentException with the specified error message
   *                                  if no value was found for the specified feature.
   */
  public static <T> Value<T> findValue(Set<Value> values, Feature feature, String errorMessage) {
    return findValue(values.toArray(new Value[0]), feature, errorMessage);
  }

  /**
   * Looks for a value of the specified feature in an array of values.
   *
   * @param values The array of values.
   * @param feature The feature.
   * @param errorMessage The error message for an exception
   * @param <T> Type of the value.
   * @return The value of the specified feature.
   * @throws IllegalArgumentException with the specified error message
   *                                  if no value was found for the specified feature.
   */
  public static <T> Value<T> findValue(Value[] values, Feature feature, String errorMessage) {
    Value<T> value = findValue(values, feature);
    if (value == null) {
      throw new IllegalArgumentException(errorMessage);
    }
    return value;
  }

  /**
   * Parses a string to produce a date.
   *
   * @param string The string to be parsed.
   * @return An instance of {@link java.util.Date}
   */
  public static Date date(String string) {
    int[] formats = { DateFormat.SHORT, DateFormat.MEDIUM, DateFormat.LONG, DateFormat.FULL };
    for (int format : formats) {
      try {
        return DateFormat.getDateInstance(format, Locale.US).parse(string);
      } catch (ParseException e) {
        // no luck
      }
    }

    try {
      return new SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(string);
    } catch (ParseException e) {
      // no luck
    }

    throw new IllegalArgumentException(String.format(
        "Couldn't parse date '%s'", string));
  }

}
