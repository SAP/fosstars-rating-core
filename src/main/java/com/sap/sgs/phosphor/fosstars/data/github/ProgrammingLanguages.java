package com.sap.sgs.phosphor.fosstars.data.github;

import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.LANGUAGES;

import com.sap.sgs.phosphor.fosstars.model.Value;
import com.sap.sgs.phosphor.fosstars.model.ValueSet;
import com.sap.sgs.phosphor.fosstars.model.value.Language;
import com.sap.sgs.phosphor.fosstars.model.value.Languages;
import com.sap.sgs.phosphor.fosstars.tool.github.GitHubProject;
import java.io.IOException;
import java.util.EnumSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;

/**
 * This data provider returns a number of languages that are used
 * in an open-source project.
 */
public class ProgrammingLanguages extends AbstractGitHubDataProvider {

  /**
   * Initializes a data provider.
   *
   * @param github An interface to the GitHub API.
   */
  public ProgrammingLanguages(GitHub github) {
    super(github);
  }

  @Override
  public ProgrammingLanguages doUpdate(GitHubProject project, ValueSet values) throws IOException {
    Objects.requireNonNull(values, "Hey! Values can't be null!");
    logger.info("Looking for programming languages that are used in the project...");
    values.update(languagesOf(project));
    return this;
  }

  /**
   * Looks for languages that are used in a project on GitHub.
   *
   * @param project The project.
   * @return A value with the languages.
   * @throws IOException If something went wrong.
   */
  private Value<Languages> languagesOf(GitHubProject project) throws IOException {
    Optional<Value> something = cache.get(project, LANGUAGES);
    if (something.isPresent()) {
      return something.get();
    }

    GHRepository repository = gitHubDataFetcher().repositoryFor(project, github);

    Set<Language> set = EnumSet.noneOf(Language.class);
    for (String string : repository.listLanguages().keySet()) {
      set.add(Language.parse(string));
    }
    Value<Languages> value = LANGUAGES.value(new Languages(set));

    cache.put(project, value);
    return value;
  }
}
