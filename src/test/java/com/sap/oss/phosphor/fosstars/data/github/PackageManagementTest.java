package com.sap.oss.phosphor.fosstars.data.github;

import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.LANGUAGES;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.PACKAGE_MANAGERS;
import static com.sap.oss.phosphor.fosstars.model.value.Language.JAVA;
import static com.sap.oss.phosphor.fosstars.model.value.Language.OTHER;
import static com.sap.oss.phosphor.fosstars.model.value.PackageManager.MAVEN;
import static com.sap.oss.phosphor.fosstars.model.value.PackageManager.NPM;
import static com.sap.oss.phosphor.fosstars.model.value.PackageManager.YARN;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import com.sap.oss.phosphor.fosstars.data.SubjectValueCache;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.ValueSet;
import com.sap.oss.phosphor.fosstars.model.subject.oss.GitHubProject;
import com.sap.oss.phosphor.fosstars.model.value.Languages;
import com.sap.oss.phosphor.fosstars.model.value.PackageManager;
import com.sap.oss.phosphor.fosstars.model.value.PackageManagers;
import com.sap.oss.phosphor.fosstars.model.value.ValueHashSet;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.kohsuke.github.GHRepository;

public class PackageManagementTest extends TestGitHubDataFetcherHolder {

  @Test
  public void testUpdate() throws IOException {
    final SubjectValueCache cache = new SubjectValueCache();
    final GitHubProject project = new GitHubProject("org", "test");

    Map<String, Long> languagesMap = new HashMap<>();
    languagesMap.put("Javascript", 42L);
    languagesMap.put("Java", 100L);
    languagesMap.put("C++", 1001L);

    final GHRepository repository = mock(GHRepository.class);
    when(repository.listLanguages()).thenReturn(languagesMap);
    when(fetcher.github().getRepository(any())).thenReturn(repository);

    ProgrammingLanguages programmingLanguagesProvider = new ProgrammingLanguages(fetcher);
    programmingLanguagesProvider.set(cache);
    programmingLanguagesProvider = spy(programmingLanguagesProvider);

    Path baseDir = Files.createTempDirectory(PackageManagementTest.class.getName());
    try {
      Path pomXml = baseDir.resolve("pom.xml");
      Files.write(pomXml, StringUtils.repeat("x", 1000).getBytes());
      Path submodule = baseDir.resolve("submodule");
      Files.createDirectory(submodule);
      Path packageJson = submodule.resolve("package.json");
      Files.write(packageJson, StringUtils.repeat("x", 500).getBytes());

      LocalRepository localRepository = mock(LocalRepository.class);
      when(localRepository.files(any()))
          .thenReturn(Arrays.asList(baseDir, pomXml, submodule, packageJson));
      TestGitHubDataFetcher.addForTesting(project, localRepository);

      PackageManagement provider = new PackageManagement(fetcher);
      provider.set(cache);
      provider = spy(provider);
      when(provider.languagesProvider()).thenReturn(programmingLanguagesProvider);

      ValueSet values = new ValueHashSet();
      provider.update(project, values);

      assertTrue(values.has(PACKAGE_MANAGERS));
      assertFalse(values.has(LANGUAGES));

      Optional<Value<PackageManagers>> something = values.of(PACKAGE_MANAGERS);
      assertTrue(something.isPresent());

      Value<PackageManagers> value = something.get();
      assertEquals(3, value.get().list().size());
      assertTrue(value.get().list().contains(MAVEN));
      assertTrue(value.get().list().contains(NPM));
      assertTrue(value.get().list().contains(YARN));
    } finally {
      FileUtils.deleteDirectory(baseDir.toFile());
    }
  }

  @Test
  public void testIsKnownConfigFile() throws IOException {
    Path baseDir = Files.createTempDirectory(PackageManagementTest.class.getName());
    try {
      assertFalse(PackageManagement.isKnownConfigFile(baseDir, NPM));

      Path path = baseDir.resolve("Makefile");
      Files.write(path, StringUtils.repeat("x", 1000).getBytes());
      assertFalse(PackageManagement.isKnownConfigFile(path, PackageManager.OTHER));

      path = baseDir.resolve("unknown.config");
      Files.write(path, StringUtils.repeat("x", 1000).getBytes());
      assertFalse(PackageManagement.isKnownConfigFile(path, MAVEN));

      path = baseDir.resolve(".pom.xml");
      Files.write(path, StringUtils.repeat("x", 1000).getBytes());
      assertFalse(PackageManagement.isKnownConfigFile(path, MAVEN));

      path = baseDir.resolve("pom.xml");
      Files.write(path, StringUtils.repeat("x", 10).getBytes());
      assertFalse(PackageManagement.isKnownConfigFile(path, MAVEN));

      path = baseDir.resolve("pom.xml");
      Files.write(path, StringUtils.repeat("x", 1000).getBytes());
      assertTrue(PackageManagement.isKnownConfigFile(path, MAVEN));
    } finally {
      FileUtils.deleteDirectory(baseDir.toFile());
    }
  }

  @Test
  public void testLanguages() throws IOException {
    final SubjectValueCache cache = new SubjectValueCache();

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