package com.sap.sgs.phosphor.fosstars.data.github;

import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.LANGUAGES;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.PACKAGE_MANAGERS;
import static com.sap.sgs.phosphor.fosstars.model.value.Language.JAVA;
import static com.sap.sgs.phosphor.fosstars.model.value.Language.OTHER;
import static com.sap.sgs.phosphor.fosstars.model.value.PackageManager.NPM;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import com.sap.sgs.phosphor.fosstars.TestGitHubDataFetcherHolder;
import com.sap.sgs.phosphor.fosstars.model.Value;
import com.sap.sgs.phosphor.fosstars.model.ValueSet;
import com.sap.sgs.phosphor.fosstars.model.value.Languages;
import com.sap.sgs.phosphor.fosstars.model.value.PackageManager;
import com.sap.sgs.phosphor.fosstars.model.value.PackageManagers;
import com.sap.sgs.phosphor.fosstars.model.value.ValueHashSet;
import com.sap.sgs.phosphor.fosstars.tool.github.GitHubProject;
import com.sap.sgs.phosphor.fosstars.tool.github.GitHubProjectValueCache;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.Test;
import org.kohsuke.github.GHContent;
import org.kohsuke.github.GHRepository;

public class PackageManagementTest extends TestGitHubDataFetcherHolder {

  @Test
  public void testUpdate() throws IOException {
    final GitHubProjectValueCache cache = new GitHubProjectValueCache();
    final GitHubProject project = new GitHubProject("org", "test");

    Map<String, Long> languagesMap = new HashMap<>();
    languagesMap.put("Javascript", 42L);
    languagesMap.put("C++", 1001L);

    final GHRepository repository = mock(GHRepository.class);
    when(repository.listLanguages()).thenReturn(languagesMap);
    when(fetcher.github().getRepository(any())).thenReturn(repository);

    ProgrammingLanguages programmingLanguagesProvider = new ProgrammingLanguages(fetcher);
    programmingLanguagesProvider.set(cache);
    programmingLanguagesProvider = spy(programmingLanguagesProvider);
    when(programmingLanguagesProvider.gitHubDataFetcher()).thenReturn(fetcher);

    final List<GHContent> contents = new ArrayList<>();
    when(repository.getDirectoryContent("/")).thenReturn(contents);

    GHContent src = mock(GHContent.class);
    when(src.getName()).thenReturn("src");
    when(src.isFile()).thenReturn(false);
    when(src.isDirectory()).thenReturn(true);
    contents.add(src);

    GHContent packageJson = mock(GHContent.class);
    when(packageJson.getName()).thenReturn("package.json");
    when(packageJson.isFile()).thenReturn(true);
    when(packageJson.isDirectory()).thenReturn(false);
    when(packageJson.getSize()).thenReturn(1500L);
    contents.add(packageJson);

    PackageManagement provider = new PackageManagement(fetcher);
    provider.set(cache);
    provider = spy(provider);
    when(provider.languagesProvider()).thenReturn(programmingLanguagesProvider);
    when(provider.gitHubDataFetcher()).thenReturn(fetcher);

    ValueSet values = new ValueHashSet();
    provider.update(project, values);

    assertTrue(values.has(PACKAGE_MANAGERS));
    assertFalse(values.has(LANGUAGES));

    Optional<Value> something = values.of(PACKAGE_MANAGERS);
    assertTrue(something.isPresent());

    Value<PackageManagers> value = something.get();
    assertTrue(value.get().list().contains(NPM));
  }

  @Test
  public void testIsKnownConfigFile() {
    GHContent content = mock(GHContent.class);
    assertFalse(PackageManagement.isKnownConfigFile(content, PackageManager.OTHER));

    when(content.getSize()).thenReturn(1000L);
    when(content.getName()).thenReturn("unknown.config");
    assertFalse(PackageManagement.isKnownConfigFile(content, PackageManager.MAVEN));

    when(content.getName()).thenReturn(".pom.xml");
    assertFalse(PackageManagement.isKnownConfigFile(content, PackageManager.MAVEN));

    when(content.getSize()).thenReturn(1L);
    when(content.getName()).thenReturn("pom.xml");
    assertFalse(PackageManagement.isKnownConfigFile(content, PackageManager.MAVEN));

    when(content.getSize()).thenReturn(1000L);
    when(content.getName()).thenReturn("pom.xml");
    assertTrue(PackageManagement.isKnownConfigFile(content, PackageManager.MAVEN));
  }

  @Test
  public void testLanguages() throws IOException {
    final GitHubProjectValueCache cache = new GitHubProjectValueCache();

    Map<String, Long> languagesMap = new HashMap<>();
    languagesMap.put("Java", 42L);
    languagesMap.put("Super language", 1001L);

    final GHRepository repository = mock(GHRepository.class);
    when(repository.listLanguages()).thenReturn(languagesMap);

    final GitHubDataFetcher fetcher = mock(GitHubDataFetcher.class);
    when(fetcher.repositoryFor(any())).thenReturn(repository);

    ProgrammingLanguages programmingLanguagesProvider = new ProgrammingLanguages(fetcher);
    programmingLanguagesProvider.set(cache);
    programmingLanguagesProvider = spy(programmingLanguagesProvider);
    when(programmingLanguagesProvider.gitHubDataFetcher()).thenReturn(fetcher);

    PackageManagement provider = new PackageManagement(fetcher);
    provider.set(cache);
    provider = spy(provider);
    when(provider.languagesProvider()).thenReturn(programmingLanguagesProvider);

    GitHubProject project = new GitHubProject("org", "test");

    Languages languages = provider.languages(project);
    assertNotNull(languages);
    assertEquals(2, languages.size());
    assertTrue(languages.get().contains(JAVA));
    assertTrue(languages.get().contains(OTHER));
  }

}