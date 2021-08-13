package com.sap.oss.phosphor.fosstars.tool.format;

import static com.sap.oss.phosphor.fosstars.tool.format.Markdown.DOUBLE_NEW_LINE;
import static com.sap.oss.phosphor.fosstars.tool.format.Markdown.NEW_LINE;
import static java.lang.String.format;
import static java.util.stream.Collectors.toList;

import com.sap.oss.phosphor.fosstars.advice.Advice;
import com.sap.oss.phosphor.fosstars.advice.Advisor;
import com.sap.oss.phosphor.fosstars.advice.Link;
import com.sap.oss.phosphor.fosstars.model.Score;
import com.sap.oss.phosphor.fosstars.model.Subject;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.value.ScoreValue;
import com.sap.oss.phosphor.fosstars.model.value.Vulnerabilities;
import com.sap.oss.phosphor.fosstars.model.value.Vulnerability;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;

/**
 * A base class for Markdown formatters.
 */
public abstract class AbstractMarkdownFormatter extends CommonFormatter {

  /**
   * Create a new formatter.
   *
   * @param advisor An advisor for calculated ratings.
   */
  protected AbstractMarkdownFormatter(Advisor advisor) {
    super(advisor);
  }

  /**
   * Returns a header for the advice section.
   *
   * @return A header for the advice section.
   */
  protected MarkdownHeader markdownAdviceHeader() {
    return Markdown.header().withCaption("## How to improve the rating");
  }

  /**
   * Print out advice for a subject.
   *
   * @param subject The subject.
   * @return Advice to be displayed.
   */
  protected MarkdownElement markdownAdviceFor(Subject subject) {
    List<Advice> adviceList;
    try {
      adviceList = advisor.adviceFor(subject);
    } catch (IOException e) {
      throw new UncheckedIOException("Oops! Could not print advice!", e);
    }

    if (adviceList.isEmpty()) {
      return MarkdownString.EMPTY;
    }

    List<MarkdownElement> elements = new ArrayList<>();
    for (Advice advice : adviceList) {
      MarkdownString text = Markdown.string(advice.content().text());
      OrderedMarkdownList links = Markdown.orderedListOf(linksIn(advice));
      MarkdownString moreInfo = Markdown.string("More info:");
      MarkdownElement adviceContent = Markdown.group(text, moreInfo, links);
      elements.add(adviceContent);
    }

    JoinedMarkdownElements advice = Markdown.join(elements).delimitedBy(DOUBLE_NEW_LINE);

    return Markdown.section().with(markdownAdviceHeader()).thatContains(advice);
  }

  /**
   * Convert links from advice to Markdown elements.
   *
   * @param advice The advice.
   * @return A list of Markdown elements with links from the advice.
   */
  List<MarkdownElement> linksIn(Advice advice) {
    return advice.content().links().stream().map(this::formatted).collect(toList());
  }

  /**
   * Format a link.
   *
   * @param link The link.
   * @return A formatted link.
   */
  MarkdownLink formatted(Link link) {
    return Markdown.link().to(link.url).withCaption(link.name);
  }

  /**
   * Build a string that shows an actual value of a score value. The method takes care about
   * unknown and not-applicable score values.
   *
   * @param scoreValue The score value.
   * @return A string that represents the score value.
   */
  public static MarkdownString actualValueOf(ScoreValue scoreValue) {
    if (scoreValue.isNotApplicable()) {
      return Markdown.string("N/A");
    }

    if (scoreValue.isUnknown()) {
      return Markdown.string("unknown");
    }

    return Markdown.string(formatted(scoreValue.get()));
  }

  @Override
  public String actualValueOf(Value<?> value) {
    if (!value.isUnknown() && value.get() instanceof Vulnerabilities) {
      Vulnerabilities vulnerabilities = (Vulnerabilities) value.get();
      if (vulnerabilities.isEmpty()) {
        return "Not found";
      }

      MarkdownHeader knownVulnerabilities = Markdown.header().withCaption("Known vulnerabilities");
      MarkdownHeaderReference reference
          = Markdown.reference().to(knownVulnerabilities).withCaption("details below");
      MarkdownString infoAboutVulnerabilities = Markdown.string(vulnerabilities.toString());
      return Markdown.join(infoAboutVulnerabilities, reference).delimitedBy(", ").make();
    }

    return super.actualValueOf(value);
  }

  /**
   * Prints info about vulnerabilities that was used for calculating a score value.
   *
   * @param scoreValue The score value.
   * @return Formatted info about vulnerabilities.
   */
  MarkdownElement infoAboutVulnerabilitiesIn(ScoreValue scoreValue) {
    Set<Vulnerability> uniqueVulnerabilities = new TreeSet<>((first, second) -> {
      if (first.id().equals(second.id())) {
        return 0;
      }

      if (first.published().isPresent() && second.published().isPresent()) {
        return first.published().get().compareTo(second.published().get());
      }

      return first.id().compareTo(second.id());
    });

    for (Value<?> value : scoreValue.usedFeatureValues()) {
      if (value.isUnknown()) {
        continue;
      }

      if (value.get() instanceof Vulnerabilities) {
        Vulnerabilities vulnerabilities = (Vulnerabilities) value.get();
        for (Vulnerability vulnerability : vulnerabilities) {
          uniqueVulnerabilities.add(vulnerability);
        }
      }
    }

    if (uniqueVulnerabilities.isEmpty()) {
      return Markdown.string("No vulnerabilities found");
    }

    List<MarkdownElement> elements = new ArrayList<>();
    for (Vulnerability vulnerability : uniqueVulnerabilities) {
      if (vulnerability.description().isPresent()) {
        MarkdownElement description = Markdown.template("%s: %s",
            linkFor(vulnerability),
            Markdown.string(vulnerability.description().get()));
        elements.add(description);
      } else {
        elements.add(linkFor(vulnerability));
      }
    }

    return Markdown.orderedListOf(elements);
  }

  /**
   * Prints a link for a vulnerability if possible.
   *
   * @param vulnerability The vulnerability.
   * @return A link for the vulnerability if available, or its identifier otherwise.
   */
  private static MarkdownElement linkFor(Vulnerability vulnerability) {
    if (vulnerability.id().startsWith("CVE-")) {
      String url = format("https://nvd.nist.gov/vuln/detail/%s", vulnerability.id());
      return Markdown.link().to(url).withCaption(vulnerability.id());
    }

    return Markdown.string(vulnerability.id());
  }

  /**
   * Builds a string with explanations for a score value if they are available.
   *
   * @param scoreValue The score value.
   * @return A formatted string.
   */
  static MarkdownElement explanationOf(ScoreValue scoreValue) {
    List<MarkdownElement> explanations
        = scoreValue.explanation().stream().map(Markdown::string).collect(toList());
    return Markdown.join(explanations).delimitedBy(NEW_LINE);
  }

  /**
   * Looks for sub-score values that were used in a specified score value.
   *
   * @param scoreValue The score value.
   * @return A list of sub-score values.
   */
  private static List<ScoreValue> usedSubScoreValuesIn(ScoreValue scoreValue) {
    return scoreValue.usedValues().stream()
        .filter(value -> value instanceof ScoreValue)
        .map(ScoreValue.class::cast)
        .sorted(Collections.reverseOrder(Comparator.comparingDouble(ScoreValue::weight)))
        .collect(Collectors.toList());
  }

  /**
   * Builds a list of short descriptions of sub-score values for a specified score value.
   *
   * @param scoreValue The score value.
   * @return A formatted text.
   */
  MarkdownElement highLevelDescriptionOf(ScoreValue scoreValue) {
    List<MarkdownElement> elements = new ArrayList<>();
    usedSubScoreValuesIn(scoreValue).forEach(subScoreValue -> {
      elements.add(Markdown.group(
          shortDescriptionOf(subScoreValue),
          highLevelDescriptionOf(subScoreValue)
      ));
    });

    return Markdown.orderedListOf(elements);
  }

  /**
   * Builds a short description of a score value.
   *
   * @param scoreValue The score value.
   * @return A formatted text.
   */
  private MarkdownElement shortDescriptionOf(ScoreValue scoreValue) {
    MarkdownHeader header = Markdown.header().withCaption(nameOf(scoreValue.score()));
    MarkdownHeaderReference reference = Markdown.reference().to(header).withHeaderName();
    return Markdown.template("%s: %s (weight is %s)",
        Markdown.bold(reference),
        Markdown.bold(actualValueOf(scoreValue)),
        Markdown.string(formatted(scoreValue.weight()))
    );
  }

  /**
   * Build details about sub-scores in a specified score value.
   * Each sub-score is printed out in a separate section.
   * The method implements BFS, therefore it prints out the direct sub-scores first.
   *
   * @param scoreValue The score value to be printed out.
   * @return A formatted text.
   */
  MarkdownElement descriptionOfSubScoresIn(ScoreValue scoreValue) {
    List<MarkdownElement> elements = new ArrayList<>();

    Set<Score> processedScores = new HashSet<>();
    Queue<ScoreValue> queue = new LinkedList<>(usedSubScoreValuesIn(scoreValue));
    while (!queue.isEmpty()) {
      ScoreValue subScoreValue = queue.poll();
      if (processedScores.contains(subScoreValue.score())) {
        continue;
      }
      processedScores.add(subScoreValue.score());

      queue.addAll(usedSubScoreValuesIn(subScoreValue));

      elements.add(detailsOf(subScoreValue));
    }

    return Markdown.join(elements).delimitedBy(DOUBLE_NEW_LINE);
  }

  /**
   * Build details of a score value in a separate section.
   * The method prints out sub-scores and feature that were are used in the score value.
   *
   * @param scoreValue The score value to be printed.
   * @return A formatted score value.
   */
  MarkdownElement detailsOf(ScoreValue scoreValue) {
    List<MarkdownElement> elements = new ArrayList<>();

    elements.add(Markdown.header().level(3).withCaption(nameOf(scoreValue.score())));

    elements.add(Markdown.template("Score: %s, confidence is %s (%s), weight is %s (%s)",
        Markdown.bold(actualValueOf(scoreValue)),
        Markdown.string(formatted(scoreValue.confidence())),
        Markdown.string(confidenceLabelFor(scoreValue.confidence()).toLowerCase()),
        Markdown.string(formatted(scoreValue.weight())),
        Markdown.string(weightLabel(scoreValue.weight()).toLowerCase())));

    elements.add(Markdown.string(scoreValue.score().description()));

    elements.add(explanationOf(scoreValue));

    List<ScoreValue> subScoreValues = new ArrayList<>();
    List<Value<?>> featureValues = new ArrayList<>();
    for (Value<?> usedValue : scoreValue.usedValues()) {
      if (usedValue instanceof ScoreValue) {
        subScoreValues.add((ScoreValue) usedValue);
      } else {
        featureValues.add(usedValue);
      }
    }

    if (!subScoreValues.isEmpty()) {
      subScoreValues.sort(Collections.reverseOrder(Comparator.comparingDouble(ScoreValue::weight)));

      elements.add(Markdown.string(
          "This sub-score is based on the following sub-score%s:%n%n",
          subScoreValues.size() == 1 ? StringUtils.EMPTY : "s"));

      elements.add(highLevelDescriptionOf(scoreValue));
    }

    if (!featureValues.isEmpty()) {
      elements.add(Markdown.string("This sub-score is based on %d feature%s:%n%n",
          featureValues.size(), featureValues.size() == 1 ? StringUtils.EMPTY : "s"));

      Map<String, String> nameToValue = new TreeMap<>(String::compareTo);
      for (Value<?> usedValue : featureValues) {
        String name = nameOf(usedValue.feature());

        if (!name.endsWith("?")) {
          name += ":";
        }

        nameToValue.put(name, actualValueOf(usedValue));
      }

      List<MarkdownElement> items = new ArrayList<>();
      for (Map.Entry<String, String> entry : nameToValue.entrySet()) {
        items.add(Markdown.template("%s %s",
            Markdown.bold(entry.getKey()), Markdown.string(entry.getValue())));
      }
      elements.add(Markdown.orderedListOf(items));
    }

    return Markdown.join(elements).delimitedBy(DOUBLE_NEW_LINE);
  }
}
