package com.sap.oss.phosphor.fosstars.data.github;

import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.LANGUAGES;

import com.sap.oss.phosphor.fosstars.model.Feature;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.subject.oss.GitHubProject;
import com.sap.oss.phosphor.fosstars.model.value.Language;
import com.sap.oss.phosphor.fosstars.model.value.Languages;
import java.io.IOException;
import java.util.EnumSet;
import java.util.Set;
import org.kohsuke.github.GHRepository;

/**
 * This data provider returns a number of languages that are used
 * in an open-source project.
 */
public class ProgrammingLanguages extends CachedSingleFeatureGitHubDataProvider<Languages> {

  /**
   * Initializes a data provider.
   *
   * @param fetcher An interface to GitHub.
   */
  public ProgrammingLanguages(GitHubDataFetcher fetcher) {
    super(fetcher);
  }

  @Override
  protected Feature<Languages> supportedFeature() {
    return LANGUAGES;
  }

  @Override
  protected Value<Languages> fetchValueFor(GitHubProject project) throws IOException {
    logger.info("Looking for programming languages that are used in the project...");
    return languagesOf(project);
  }

  /**
   * Looks for languages that are used in a project on GitHub.
   *
   * @param project The project.
   * @return A value with the languages.
   * @throws IOException If something went wrong.
   */
  private Value<Languages> languagesOf(GitHubProject project) throws IOException {
    GHRepository repository = fetcher.repositoryFor(project);

    Set<Language> set = EnumSet.noneOf(Language.class);
    for (String string : repository.listLanguages().keySet()) {
      set.add(Language.parse(string));
    }

    return LANGUAGES.value(new Languages(set));
  }
}
