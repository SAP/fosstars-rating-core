package com.sap.oss.phosphor.fosstars.data.github;

import static com.sap.oss.phosphor.fosstars.TestUtils.DELTA;
import static com.sap.oss.phosphor.fosstars.TestUtils.PROJECT;
import static com.sap.oss.phosphor.fosstars.data.github.SecurityReviewsFromOpenSSF.DATE_FORMAT;
import static com.sap.oss.phosphor.fosstars.data.github.SecurityReviewsFromOpenSSF.SECURITY_REVIEWS_PROJECT;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.sap.oss.phosphor.fosstars.TestUtils;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.subject.oss.GitHubProject;
import com.sap.oss.phosphor.fosstars.model.value.SecurityReview;
import com.sap.oss.phosphor.fosstars.model.value.SecurityReviews;
import com.sap.oss.phosphor.fosstars.util.Yaml;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.ParseException;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.junit.Test;

public class SecurityReviewsFromOpenSSFTest extends TestGitHubDataFetcherHolder {

  @Test
  public void testPurlBelongsTo() {
    SecurityReviewsFromOpenSSF provider = new SecurityReviewsFromOpenSSF(fetcher);
    GitHubProject project = new GitHubProject("org", "test");
    assertTrue(provider.purlBelongsTo(project, "pkg:github/org/test"));
    assertTrue(provider.purlBelongsTo(project, "pkg:github/org/test@1.0.1"));
    assertFalse(provider.purlBelongsTo(project, "pkg:other/org/test"));
    assertFalse(provider.purlBelongsTo(project, "pkg:github/org/other"));
    assertFalse(provider.purlBelongsTo(project, "pkg:github/other/test"));
    assertFalse(provider.purlBelongsTo(project, "pkg:github/noname"));
    assertTrue(provider.purlBelongsTo(project, "pkg:pkg:github/org/test"));
    assertTrue(provider.purlBelongsTo(project, "pkg:pkg:pkg:pkg:github/org/test"));
  }

  @Test
  public void testIsReviewFor() throws JsonProcessingException {
    SecurityReviewsFromOpenSSF provider = new SecurityReviewsFromOpenSSF(fetcher);

    JsonNode metadata = Yaml.mapper().readTree(
        "---\n"
        + "Publication-State: Active\n"
        + "Access: Public\n"
        + "Reviewers:\n"
        + "- Organization: Trail of Bits\n"
        + "  Associated-With-Project: false\n"
        + "  Compensation-Source: non-project\n"
        + "- Organization: TrustInSoft\n"
        + "  Associated-With-Project: false\n"
        + "  Compensation-Source: non-project  \n"
        + "Domain: Security\n"
        + "Methodology:\n"
        + "- External-Review\n"
        + "Issues-Identified: Non-Severe\n"
        + "Package-URLs:\n"
        + "- pkg:github/madler/zlib@1.2.8\n"
        + "Review-Date: 2016-09-30\n"
        + "Scope: Implementation/Partial\n"
        + "Schema-Version: 1.0\n"
        + "SPDX-License-Identifier: CC-BY-4.0");

    GitHubProject zlib = new GitHubProject("madler", "zlib");
    assertTrue(provider.isReviewFor(zlib, metadata));

    GitHubProject other = new GitHubProject("org", "other");
    assertFalse(provider.isReviewFor(other, metadata));
  }

  @Test
  public void testReadMetadataFrom() throws IOException {
    String content = "---\n"
        + "Publication-State: Active\n"
        + "Access: Public\n"
        + "Reviewers:\n"
        + "- Organization: Trail of Bits\n"
        + "  Associated-With-Project: false\n"
        + "  Compensation-Source: non-project\n"
        + "- Organization: TrustInSoft\n"
        + "  Associated-With-Project: false\n"
        + "  Compensation-Source: non-project  \n"
        + "Domain: Security\n"
        + "Methodology:\n"
        + "- External-Review\n"
        + "Issues-Identified: Non-Severe\n"
        + "Package-URLs:\n"
        + "- pkg:github/madler/zlib@1.2.8\n"
        + "Review-Date: 2016-09-30\n"
        + "Scope: Implementation/Partial\n"
        + "Schema-Version: 1.0\n"
        + "SPDX-License-Identifier: CC-BY-4.0\n"
        + "---\n"
        + "\n"
        + "### Summary\n"
        + "\n"
        + "Here is some text.\n";

    try (BufferedReader reader = new BufferedReader(new StringReader(content))) {
      Optional<JsonNode> metadata = SecurityReviewsFromOpenSSF.readMetadataFrom(reader);
      assertTrue(metadata.isPresent());
      assertEquals("2016-09-30", metadata.get().at("/Review-Date").asText());
      assertTrue(metadata.get().at("/Package-URLs").isArray());
    }

    try (BufferedReader reader = new BufferedReader(new StringReader("Something else"))) {
      Optional<JsonNode> metadata = SecurityReviewsFromOpenSSF.readMetadataFrom(reader);
      assertFalse(metadata.isPresent());
    }

    String wrongContent = "---\n# Something else\n\nText\n"; // technically, it is valid YAML
    try (BufferedReader reader = new BufferedReader(new StringReader(wrongContent))) {
      Optional<JsonNode> metadata = SecurityReviewsFromOpenSSF.readMetadataFrom(reader);
      assertTrue(metadata.isPresent());
    }
  }

  @Test
  public void testReadReviewDateFrom() throws JsonProcessingException {
    SecurityReviewsFromOpenSSF provider = new SecurityReviewsFromOpenSSF(fetcher);

    String content = "---\n"
        + "Package-URLs:\n"
        + "- pkg:github/madler/zlib@1.2.8\n"
        + "Review-Date: 2016-09-30\n";

    Optional<Date> date = provider.readReviewDateFrom(Yaml.mapper().readTree(content));
    assertTrue(date.isPresent());
    assertEquals("2016-09-30", DATE_FORMAT.format(date.get()));

    String wrongContent = "---\n"
        + "Package-URLs:\n"
        + "- pkg:github/madler/zlib@1.2.8\n"
        + "Review-Date: wrong\n";

    date = provider.readReviewDateFrom(Yaml.mapper().readTree(wrongContent));
    assertFalse(date.isPresent());

    wrongContent = "---\n"
        + "Package-URLs:\n"
        + "- pkg:github/madler/zlib@1.2.8\n";

    date = provider.readReviewDateFrom(Yaml.mapper().readTree(wrongContent));
    assertFalse(date.isPresent());
  }

  @Test
  public void testFetchValueFor() throws IOException, GitAPIException, ParseException {
    Path securityReviewDirectory
        = Files.createTempDirectory(SecurityReviewsFromOpenSSFTest.class.getName());
    Path directory = Files.createTempDirectory(getClass().getName());
    try (Repository repository = FileRepositoryBuilder.create(directory.resolve(".git").toFile());
         Git git = new Git(repository)) {

      repository.create();

      TestUtils.commit(
          new HashMap<String, String>() {
            {
              put("App.java", "public class App {}");
              put("One.java", "public class One {}");
              put("Two.java", "public class Two {}");
            }
            }, "First commit: init", git);

      TestUtils.commit(
          new HashMap<String, String>() {
            {
              put("App.java", "public class App { /* something new */ }");
            }
            }, "Second commit: updated App", git);

      TestUtils.commit(
          new HashMap<String, String>() {
            {
              put("Other.java", "public class Other {}");
              put("One.java", "public class One { /* something new */ }");
            }
            }, "Third commit: added Other, updated One", git);

      LocalRepository localRepository = new LocalRepository(
          new LocalRepositoryInfo(directory, new Date(), PROJECT.scm()),
          repository);
      localRepository = spy(localRepository);
      TestGitHubDataFetcher.addForTesting(PROJECT, localRepository);

      LocalRepository securityReviewLocalRepository = mock(LocalRepository.class);
      TestGitHubDataFetcher.addForTesting(SECURITY_REVIEWS_PROJECT, securityReviewLocalRepository);

      Path reviewFile
          = securityReviewDirectory.resolve("reviews").resolve("github").resolve("test.md");
      Files.createDirectories(reviewFile.getParent());

      final SecurityReviewsFromOpenSSF provider = new SecurityReviewsFromOpenSSF(fetcher);

      String content = "---\n"
          + "Publication-State: Active\n"
          + "Access: Public\n"
          + "Reviewers:\n"
          + "- Organization: Mordor\n"
          + "  Associated-With-Project: false\n"
          + "  Compensation-Source: non-project\n"
          + "Domain: Security\n"
          + "Methodology:\n"
          + "- External-Review\n"
          + "Issues-Identified: Non-Severe\n"
          + "Package-URLs:\n"
          + "- pkg:github/org/test@1.2.3\n"
          + "Review-Date: 2020-12-25\n"
          + "Scope: Implementation/Partial\n"
          + "Schema-Version: 1.0\n"
          + "SPDX-License-Identifier: CC-BY-4.0\n"
          + "---\n"
          + "\n"
          + "### Summary\n"
          + "\n"
          + "Some text here.\n";
      Files.write(reviewFile, content.getBytes());
      when(securityReviewLocalRepository.files(any(), any()))
          .thenReturn(Collections.singletonList(reviewFile));

      Date reviewDate = DATE_FORMAT.parse("2020-12-25");
      List<GitCommit> commits = localRepository.commits();

      when(localRepository.firstCommitAfter(reviewDate))
          .thenReturn(Optional.of(commits.get(1)));
      Value<SecurityReviews> value = provider.fetchValueFor(PROJECT);
      assertFalse(value.isUnknown());
      assertFalse(value.isNotApplicable());
      SecurityReviews reviews = value.get();
      assertEquals(1, reviews.size());
      SecurityReview review = reviews.iterator().next();
      assertEquals("2020-12-25", DATE_FORMAT.format(review.date()));
      assertEquals(0.5,
          review.projectChanged()
              .orElseThrow(() -> new Error("Could not figure out how much of code has changes!")),
          DELTA);

      when(securityReviewLocalRepository.files(any(), any())).thenReturn(Collections.emptyList());
      value = provider.fetchValueFor(PROJECT);
      assertFalse(value.isUnknown());
      assertFalse(value.isNotApplicable());
      assertTrue(value.get().isEmpty());
    } finally {
      FileUtils.forceDeleteOnExit(securityReviewDirectory.toFile());
      FileUtils.forceDeleteOnExit(directory.toFile());
    }
  }
}