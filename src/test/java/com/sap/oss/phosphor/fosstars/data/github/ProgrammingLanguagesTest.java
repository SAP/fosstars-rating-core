package com.sap.oss.phosphor.fosstars.data.github;

import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.LANGUAGES;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.ValueSet;
import com.sap.oss.phosphor.fosstars.model.subject.oss.GitHubProject;
import com.sap.oss.phosphor.fosstars.model.value.Language;
import com.sap.oss.phosphor.fosstars.model.value.Languages;
import com.sap.oss.phosphor.fosstars.model.value.ValueHashSet;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;
import org.kohsuke.github.GHRepository;

public class ProgrammingLanguagesTest extends TestGitHubDataFetcherHolder {

  @Test
  public void testUpdate() throws IOException {
    ProgrammingLanguages provider = new ProgrammingLanguages(fetcher);
    provider = spy(provider);

    Map<String, Long> languages = new HashMap<>();
    languages.put("Java", 10L);
    languages.put("C", 5L);
    languages.put("Brainfuck", 1000L); // https://en.wikipedia.org/wiki/Brainfuck

    GHRepository repository = mock(GHRepository.class);
    when(repository.listLanguages()).thenReturn(languages);

    when(fetcher.github().getRepository(any())).thenReturn(repository);

    GitHubProject project = new GitHubProject("org", "test");

    when(fetcher.repositoryFor(project)).thenReturn(repository);

    ValueSet values = new ValueHashSet();
    assertEquals(0, values.size());

    provider.update(project, values);
    assertEquals(1, values.size());
    assertTrue(values.has(LANGUAGES));
    assertTrue(values.of(LANGUAGES).isPresent());
    Value<Languages> value = values.of(LANGUAGES).get();
    assertEquals(3, value.get().size());
    assertTrue(value.get().get().contains(Language.C));
    assertTrue(value.get().get().contains(Language.JAVA));
    assertTrue(value.get().get().contains(Language.OTHER));
  }
}