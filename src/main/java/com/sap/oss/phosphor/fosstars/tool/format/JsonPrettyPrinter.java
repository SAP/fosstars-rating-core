package com.sap.oss.phosphor.fosstars.tool.format;

import static java.util.Collections.emptyList;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sap.oss.phosphor.fosstars.advice.Advice;
import com.sap.oss.phosphor.fosstars.advice.AdviceContent;
import com.sap.oss.phosphor.fosstars.advice.Advisor;
import com.sap.oss.phosphor.fosstars.model.Subject;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.value.RatingValue;
import com.sap.oss.phosphor.fosstars.model.value.ScoreValue;
import com.sap.oss.phosphor.fosstars.tool.format.model.Advices;
import com.sap.oss.phosphor.fosstars.tool.format.model.Feature;
import com.sap.oss.phosphor.fosstars.tool.format.model.Rating;
import com.sap.oss.phosphor.fosstars.tool.format.model.Score;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The class prints a pretty rating value in JSON.
 */
public class JsonPrettyPrinter extends CommonFormatter {

  /**
   * Object Mapper for Json.
   */
  private static final ObjectMapper mapper = new ObjectMapper();

  /**
   * A logger.
   */
  private static final Logger LOGGER
      = LogManager.getLogger(JsonPrettyPrinter.class);

  /**
   * A formatter for doubles.
   */
  private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.#");

  static {
    DECIMAL_FORMAT.setMinimumFractionDigits(1);
    DECIMAL_FORMAT.setMaximumFractionDigits(2);
  }

  /**
   * Initializes a pretty printer.
   *
   * @param advisor to be added to the printed output.
   */
  public JsonPrettyPrinter(Advisor advisor) {
    super(advisor);
  }

  @Override
  public String print(Subject subject) {
    if (!subject.ratingValue().isPresent()) {
      return StringUtils.EMPTY;
    }
    RatingValue ratingValue = subject.ratingValue().get();
    Rating rating = from(ratingValue, subject);
    rating.advices(adviceFor(subject));
    StringBuilder output = new StringBuilder();
    try {
      output.append(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(rating));
    } catch (JsonProcessingException e) {
      throw new UncheckedIOException(
          "Oops! Could not parse the rating value object to Json string!", e);
    }

    return output.toString();
  }

  /**
   * Extract advices for a subject.
   *
   * @param subject The subject.
   * @return Advices collected form a subject.
   */
  private List<Advices> adviceFor(Subject subject) {
    try {
      return advisor.adviceFor(subject).stream().map(JsonPrettyPrinter::from)
          .collect(Collectors.toList());
    } catch (IOException e) {
      LOGGER.warn("Oops! Could not collect advices!", e);
      return emptyList();
    }
  }

  /**
   * Map Advice to serializable class.
   *
   * @param advice The Advice.
   * @return The serializable class.
   */
  private static Advices from(Advice advice) {
    AdviceContent content = advice.content();
    return new Advices(content.text(), content.feature().name(), content.links());
  }

  /**
   * Format a rating value.
   *
   * @param ratingValue The rating value.
   * @param subject     The subject.
   * @return A formatted rating value.
   */
  private static Rating from(RatingValue ratingValue, Subject subject) {
    ScoreValue scoreValue = ratingValue.scoreValue();
    Rating rating = new Rating()
        .purl(subject.purl())
        .label(ratingValue.label().name());
    Score score = from(scoreValue);
    rating.score(score);
    return rating;
  }

  /**
   * Extract Score from Score Value.
   *
   * @param scoreValue The score value.
   * @return the serializable Score.
   */
  private static Score from(ScoreValue scoreValue) {
    Score score = new Score()
        .name(scoreValue.score().name())
        .value(tellMeActualValueOf(scoreValue))
        .confidence(printValue(scoreValue.confidence()))
        .weight(printValue(scoreValue.weight()));
    from(scoreValue, score);
    return score;
  }

  /**
   * Map feature value to serializable class.
   *
   * @param featureValue The feature value.
   * @return The serializable class from feature value.
   */
  private static Feature from(Value<?> featureValue) {
    return new Feature()
        .name(featureValue.feature().name())
        .value(tellMeActualValueOf(featureValue));
  }

  /**
   * Extract Sub scores from the score value.
   *
   * @param scoreValue The score value to be printed.
   * @param score      Tells if the score is a top-level score in the rating.
   */
  private static void from(ScoreValue scoreValue, Score score) {
    for (Value<?> usedValue : scoreValue.usedValues()) {
      if (usedValue instanceof ScoreValue) {
        score.subScore(from((ScoreValue) usedValue));
      } else {
        score.feature(from(usedValue));
      }
    }
  }

  /**
   * Prints an actual value of a score value. The method takes care about unknown and not-applicable
   * score values.
   *
   * @param scoreValue The score value.
   * @return A string that represents the score value.
   */
  public static String tellMeActualValueOf(ScoreValue scoreValue) {
    if (scoreValue.isNotApplicable()) {
      return "N/A";
    }

    if (scoreValue.isUnknown()) {
      return "unknown";
    }

    return printValue(scoreValue.get());
  }

  /**
   * Prints an actual value. The method takes care about unknown and not-applicable values.
   *
   * @param value The value.
   * @return A string that represents the score value.
   */
  public static String tellMeActualValueOf(Value<?> value) {
    if (value.isNotApplicable()) {
      return "N/A";
    }

    if (value.isUnknown()) {
      return "unknown";
    }

    return String.format("%s", value.get());
  }

  /**
   * Prints out a number with its max value.
   *
   * @param value The number.
   * @return A formatted string with the number and max value.
   */
  public static String printValue(double value) {
    return String.format("%-4s",
        DECIMAL_FORMAT.format(value));
  }
}