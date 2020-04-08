package com.sap.sgs.phosphor.fosstars.data.github;

import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.LANGUAGES;

import com.sap.sgs.phosphor.fosstars.model.Value;
import com.sap.sgs.phosphor.fosstars.model.ValueSet;
import com.sap.sgs.phosphor.fosstars.model.value.Language;
import com.sap.sgs.phosphor.fosstars.model.value.Languages;
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
   * @param where A GitHub organization of user name.
   * @param name A name of a repository.
   * @param github An interface to the GitHub API.
   */
  public ProgrammingLanguages(String where, String name, GitHub github) {
    super(where, name, github);
  }

  @Override
  public ProgrammingLanguages update(ValueSet values) throws IOException {
    Objects.requireNonNull(values, "Hey! Values can't be null!");
    logger.info("Looking for programming languages that are used in the project...");
    values.update(languages());
    return this;
  }

  /**
   * Looks for languages that are used in the project.
   *
   * @return A value with the languages.
   * @throws IOException If something went wrong.
   */
  private Value<Languages> languages() throws IOException {
    Optional<Value> something = cache().get(url, LANGUAGES);
    if (something.isPresent()) {
      return something.get();
    }

    GHRepository repository = github.getRepository(path);

    Set<Language> set = EnumSet.noneOf(Language.class);
    for (String string : repository.listLanguages().keySet()) {
      set.add(Language.parse(string));
    }
    Value<Languages> value = LANGUAGES.value(new Languages(set));

    cache().put(url, value);
    return value;
  }
}
