package com.sap.oss.phosphor.fosstars.data.github;

import static com.sap.oss.phosphor.fosstars.data.github.TestGitHubDataFetcherHolder.TestGitHubDataFetcher.addForTesting;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_ADDRESS_SANITIZER;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_MEMORY_SANITIZER;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_UNDEFINED_BEHAVIOR_SANITIZER;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.sap.oss.phosphor.fosstars.data.SubjectValueCache;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.ValueSet;
import com.sap.oss.phosphor.fosstars.model.subject.oss.GitHubProject;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.Test;

public class UsesSanitizersTest extends TestGitHubDataFetcherHolder {

  @Test
  public void testFetchValuesForWithAllSanitizers() throws IOException {
    String content = String.join("\n", new String[] {
        "first line",
        "--debug -fsanitize=memory --another-option",
        "--option -fsanitize=undefined,address --another-option --debug",
        "another line",
    });

    testProvider(content,
        USES_ADDRESS_SANITIZER.value(true),
        USES_MEMORY_SANITIZER.value(true),
        USES_UNDEFINED_BEHAVIOR_SANITIZER.value(true));
  }

  @Test
  public void testFetchValuesForWithSomeSanitizers() throws IOException {
    testProvider("\"-fsanitize=address\";",
        USES_ADDRESS_SANITIZER.value(true),
        USES_MEMORY_SANITIZER.value(false),
        USES_UNDEFINED_BEHAVIOR_SANITIZER.value(false));
    testProvider("-fsanitize= memory",
        USES_ADDRESS_SANITIZER.value(false),
        USES_MEMORY_SANITIZER.value(true),
        USES_UNDEFINED_BEHAVIOR_SANITIZER.value(false));
    testProvider("-fsanitize=undefined ",
        USES_ADDRESS_SANITIZER.value(false),
        USES_MEMORY_SANITIZER.value(false),
        USES_UNDEFINED_BEHAVIOR_SANITIZER.value(true));
    testProvider("--test  -fsanitize=address,    memory  --other=a,b --test",
        USES_ADDRESS_SANITIZER.value(true),
        USES_MEMORY_SANITIZER.value(true),
        USES_UNDEFINED_BEHAVIOR_SANITIZER.value(false));
    testProvider("--test  -fsanitize=  address, undefined ,   memory  --other=a,b --test",
        USES_ADDRESS_SANITIZER.value(true),
        USES_MEMORY_SANITIZER.value(true),
        USES_UNDEFINED_BEHAVIOR_SANITIZER.value(true));
    testProvider("--test  -fsanitize=address --memory",
        USES_ADDRESS_SANITIZER.value(true),
        USES_MEMORY_SANITIZER.value(false),
        USES_UNDEFINED_BEHAVIOR_SANITIZER.value(false));
    testProvider(" -fsanitize=aaa --test",
        USES_ADDRESS_SANITIZER.value(false),
        USES_MEMORY_SANITIZER.value(false),
        USES_UNDEFINED_BEHAVIOR_SANITIZER.value(false));

    String content = String.join("\n", new String[] {
        "first line",
        "-fsanitize=undefined,address",
        "another line",
    });

    testProvider(content,
        USES_ADDRESS_SANITIZER.value(true),
        USES_MEMORY_SANITIZER.value(false),
        USES_UNDEFINED_BEHAVIOR_SANITIZER.value(true));

    content = String.join("\n", new String[] {
        "unless ($disabled{asan} || defined $detected_sanitizers{asan}) {",
        "    push @{$config{cflags}}, \"-fsanitize=address\";",
        "}"
    });

    testProvider(content,
        USES_ADDRESS_SANITIZER.value(true),
        USES_MEMORY_SANITIZER.value(false),
        USES_UNDEFINED_BEHAVIOR_SANITIZER.value(false));
  }

  @Test
  public void testFetchValuesForWithoutSanitizers() throws IOException {
    String content = String.join("\n", new String[] {
        "first line",
        "another line",
    });

    testProvider(content,
        USES_ADDRESS_SANITIZER.value(false),
        USES_MEMORY_SANITIZER.value(false),
        USES_UNDEFINED_BEHAVIOR_SANITIZER.value(false));
  }

  private void testProvider(String content, Value... expectedValues) throws IOException {
    UsesSanitizers provider = new UsesSanitizers(fetcher);
    provider.set(new SubjectValueCache());

    final LocalRepository repository = mock(LocalRepository.class);
    when(repository.files(any()))
        .thenReturn(Collections.singletonList(Paths.get("CMakeLists.txt")));

    when(repository.file(any(Path.class))).thenReturn(Optional.of(content));

    GitHubProject project = new GitHubProject("org", "test");
    addForTesting(project, repository);

    ValueSet values = provider.fetchValuesFor(project);
    assertEquals(3, values.size());

    for (Value expectedValue : expectedValues) {
      Optional<Value> something = values.of(expectedValue.feature());
      assertTrue(something.isPresent());
      Value actualValue = something.get();
      assertEquals(expectedValue, actualValue);
    }
  }

  @Test
  public void testMaybeBuildConfig() {
    assertTrue(UsesSanitizers.maybeBuildConfig(Paths.get("configure.ac")));
    assertTrue(UsesSanitizers.maybeBuildConfig(Paths.get("Configure")));
    assertFalse(UsesSanitizers.maybeBuildConfig(Paths.get("README.md")));
  }

  @Test
  public void testLookForSanitizers() throws IOException {
    String content = String.join("\n", new String[] {
        "first line",
        "-fsanitize=address",
        "another line",
        "--debug -fsanitize=memory --another-option",
        "--option -fsanitize=undefined,address --another-option --debug"
    });
    List<String> options = UsesSanitizers.lookForSanitizers(content);
    assertEquals(Arrays.asList("address", "memory", "undefined", "address"), options);

    content = String.join("\n", new String[] {
        "--test -fsanitize=  address , memory --undefined",
    });
    options = UsesSanitizers.lookForSanitizers(content);
    assertEquals(Arrays.asList("address", "memory"), options);
  }

  @Test
  public void testParseOptions() {
    assertTrue(UsesSanitizers.parseOptions("something else").isEmpty());
    assertEquals(
        Arrays.asList("address"),
        UsesSanitizers.parseOptions("-fsanitize=address"));
    assertEquals(
        Arrays.asList("memory"),
        UsesSanitizers.parseOptions("-fsanitize=memory"));
    assertEquals(
        Arrays.asList("address", "memory", "test"),
        UsesSanitizers.parseOptions("-fsanitize=address,memory,test"));
    assertEquals(
        Arrays.asList("address", "memory", "test"),
        UsesSanitizers.parseOptions("-fsanitize=  address  ,  memory  ,    test "));
    assertEquals(
        Collections.emptyList(),
        UsesSanitizers.parseOptions("-fsanitize="));
    assertEquals(
        Collections.emptyList(),
        UsesSanitizers.parseOptions("-fsomething"));
  }
}