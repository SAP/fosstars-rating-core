package com.sap.sgs.phosphor.fosstars.data.github;

import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.LANGUAGES;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.PACKAGE_MANAGERS;
import static com.sap.sgs.phosphor.fosstars.model.value.Language.CPP;
import static com.sap.sgs.phosphor.fosstars.model.value.Language.JAVA;
import static com.sap.sgs.phosphor.fosstars.model.value.Language.JAVASCRIPT;
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

import com.sap.sgs.phosphor.fosstars.model.Value;
import com.sap.sgs.phosphor.fosstars.model.ValueSet;
import com.sap.sgs.phosphor.fosstars.model.value.Languages;
import com.sap.sgs.phosphor.fosstars.model.value.PackageManager;
import com.sap.sgs.phosphor.fosstars.model.value.PackageManagers;
import com.sap.sgs.phosphor.fosstars.model.value.ValueHashSet;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.Test;
import org.kohsuke.github.GHContent;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;

public class PackageManagementTest {

  @Test
  public void update() throws IOException {
    ProgrammingLanguages programmingLanguagesProvider = mock(ProgrammingLanguages.class);
    when(programmingLanguagesProvider.update(any())).thenAnswer(invocation -> {
      ValueSet values = (ValueSet) invocation.getArguments()[0];
      values.update(LANGUAGES.value(new Languages(JAVASCRIPT, CPP)));
      return null;
    });

    GitHub github = mock(GitHub.class);
    PackageManagement provider = new PackageManagement("org", "test", github);
    provider = spy(provider);
    when(provider.languagesProvider()).thenReturn(programmingLanguagesProvider);

    final List<GHContent> contents = new ArrayList<>();

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

    GHRepository repository = mock(GHRepository.class);
    when(repository.getDirectoryContent("/")).thenReturn(contents);

    when(github.getRepository(any())).thenReturn(repository);

    ValueSet values = new ValueHashSet();
    provider.update(values);

    assertTrue(values.has(PACKAGE_MANAGERS));
    assertFalse(values.has(LANGUAGES));

    Optional<Value> something = values.of(PACKAGE_MANAGERS);
    assertTrue(something.isPresent());

    Value<PackageManagers> value = something.get();
    assertTrue(value.get().list().contains(NPM));
  }

  @Test
  public void isKnownConfigFile() {
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
  public void languages() throws IOException {
    ProgrammingLanguages programmingLanguagesProvider = mock(ProgrammingLanguages.class);
    when(programmingLanguagesProvider.update(any())).thenAnswer(invocation -> {
      ValueSet values = (ValueSet) invocation.getArguments()[0];
      values.update(LANGUAGES.value(new Languages(JAVA, OTHER)));
      return null;
    });

    GitHub github = mock(GitHub.class);
    PackageManagement provider = new PackageManagement("org", "test", github);
    provider = spy(provider);
    when(provider.languagesProvider()).thenReturn(programmingLanguagesProvider);

    Languages languages = provider.languages();
    assertNotNull(languages);
    assertEquals(2, languages.size());
    assertTrue(languages.get().contains(JAVA));
    assertTrue(languages.get().contains(OTHER));
  }

}