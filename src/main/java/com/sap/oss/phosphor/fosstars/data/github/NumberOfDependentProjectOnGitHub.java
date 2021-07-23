package com.sap.oss.phosphor.fosstars.data.github;

import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.NUMBER_OF_DEPENDENT_PROJECTS_ON_GITHUB;
import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.EMPTY;

import com.sap.oss.phosphor.fosstars.model.Feature;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures;
import com.sap.oss.phosphor.fosstars.model.subject.oss.GitHubProject;
import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;

/**
 * This data provider looks for a number of projects that use a specified project on GitHub.
 * It fills out the {@link OssFeatures#NUMBER_OF_DEPENDENT_PROJECTS_ON_GITHUB} feature.
 */
public class NumberOfDependentProjectOnGitHub
    extends CachedSingleFeatureGitHubDataProvider<Integer> {

  /**
   * Initializes a data provider.
   *
   * @param fetcher An interface to GitHub.
   * @throws IOException If something went wrong.
   */
  public NumberOfDependentProjectOnGitHub(GitHubDataFetcher fetcher) throws IOException {
    super(fetcher);
  }

  @Override
  protected Feature<Integer> supportedFeature() {
    return NUMBER_OF_DEPENDENT_PROJECTS_ON_GITHUB;
  }

  @Override
  protected Value<Integer> fetchValueFor(GitHubProject project) throws IOException {
    logger.info("Figuring out how many projects on GitHub uses this project ...");

    Element page = loadFrontPageOf(project);
    for (Element link : page.getElementsByTag("a")) {
      if (!link.hasAttr("href") || !link.attr("href").contains("/network/dependents")) {
        continue;
      }

      for (Element span : link.getElementsByTag("span")) {
        if (span.hasAttr("title")) {
          try {
            return NUMBER_OF_DEPENDENT_PROJECTS_ON_GITHUB.value(numberFrom(span.attr("title")));
          } catch (NumberFormatException e) {
            // no luck
          }
        }

        try {
          return NUMBER_OF_DEPENDENT_PROJECTS_ON_GITHUB.value(numberFrom(span.text()));
        } catch (NumberFormatException e) {
          // no luck
        }
      }
    }

    return NUMBER_OF_DEPENDENT_PROJECTS_ON_GITHUB.unknown();
  }

  /**
   * Load a front page of a project on GitHub.
   *
   * @param project The project.
   * @return A front page of the project.
   * @throws IOException If the front page could not be loaded.
   */
  Element loadFrontPageOf(GitHubProject project) throws IOException {
    String url = format("https://github.com/%s/%s", project.organization().name(), project.name());
    return Jsoup.connect(url).get();
  }

  /**
   * Try to extract an integer from a string.
   *
   * @param s The string.
   * @return An integer.
   * @throws NumberFormatException If the string doesn't have an integer.
   */
  static int numberFrom(String s) throws NumberFormatException {
    return Integer.parseInt(s.replaceAll("[,+\\s+]", EMPTY));
  }

  /**
   * This is for testing and demo purposes.
   *
   * @param args Command-line options (option 1: API token, option 2: project URL).
   * @throws Exception If something went wrong.
   */
  public static void main(String... args) throws Exception {
    String token = args.length > 0 ? args[0] : "";
    String url = args.length > 1 ? args[1] : "https://github.com/FasterXML/jackson-databind";
    GitHubProject project = GitHubProject.parse(url);
    GitHub github = new GitHubBuilder().withOAuthToken(token).build();
    NumberOfDependentProjectOnGitHub provider
        = new NumberOfDependentProjectOnGitHub(new GitHubDataFetcher(github, token));
    System.out.println(provider.fetchValueFor(project));
  }
}