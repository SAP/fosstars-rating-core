package com.sap.oss.phosphor.fosstars.model.value;

import java.util.Objects;

/**
 * The enum contains programming languages.
 */
public enum Language {

  C, CPP,

  // JVM languages
  JAVA, SCALA,

  // .NET languages
  C_SHARP, F_SHARP, VISUALBASIC,

  PHP, RUBY, PYTHON, JAVASCRIPT, TYPESCRIPT, GO,

  OTHER;

  /**
   * Convert a string to a programming language.
   * If the language is unknown, then the method returns {@link #OTHER}.
   *
   * @param string The string.
   * @return A language.
   */
  public static Language parse(String string) {
    Objects.requireNonNull(string, "String can't be null!");

    string = string.trim();
    if (string.isEmpty()) {
      throw new IllegalArgumentException("String can't be empty!");
    }

    for (Language language : values()) {
      if (string.equalsIgnoreCase(language.name())) {
        return language;
      }
    }
    switch (string.toUpperCase()) {
      case "C++":
        return CPP;
      case "C#":
        return C_SHARP;
      case "F#":
        return F_SHARP;
      case "GO":
      case "GOLANG":
        return GO;
      default:
        return OTHER;
    }
  }
}
