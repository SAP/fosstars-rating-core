package com.sap.oss.phosphor.fosstars.data.github;

import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.RELEASED_ARTIFACT_VERSIONS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.subject.oss.GitHubProject;
import com.sap.oss.phosphor.fosstars.model.value.ArtifactVersions;
import com.sap.oss.phosphor.fosstars.model.value.ValueHashSet;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import org.junit.Test;
import org.kohsuke.github.GHCommit;
import org.kohsuke.github.GHRelease;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GHTag;
import org.kohsuke.github.PagedIterable;

public class ReleasesFromGitHubTest extends TestGitHubDataFetcherHolder {

  private static final GitHubProject PROJECT = new GitHubProject("org", "project");
  private ReleasesFromGitHub releasesFromGitHub;

  @Test
  public void testIfGitHubHasReleaseInfo() throws IOException {
    final GHRepository repository = mock(GHRepository.class);

    PagedIterable<GHRelease> pagedIterable = mock(PagedIterable.class);

    GHRelease release = mock(GHRelease.class);
    when(release.getName()).thenReturn("2.0.0");
    when(release.getPublished_at()).thenReturn(new Date());

    List<GHRelease> releaselist = Arrays.asList(release);
    when(pagedIterable.toList()).thenReturn(releaselist);
    when(repository.listReleases()).thenReturn(pagedIterable);

    when(fetcher.github().getRepository(any())).thenReturn(repository);
    releasesFromGitHub = new ReleasesFromGitHub(fetcher);

    ValueHashSet values = new ValueHashSet();
    assertEquals(0, values.size());

    releasesFromGitHub.update(PROJECT, values);

    assertEquals(1, values.size());
    assertTrue(values.has(RELEASED_ARTIFACT_VERSIONS));
    assertTrue(values.of(RELEASED_ARTIFACT_VERSIONS).isPresent());
    assertFalse(values.of(RELEASED_ARTIFACT_VERSIONS).get().isUnknown());
    assertFalse(values.of(RELEASED_ARTIFACT_VERSIONS).get().get().empty());
    assertEquals(1, values.of(RELEASED_ARTIFACT_VERSIONS).get().get().size());
    assertTrue(values.of(RELEASED_ARTIFACT_VERSIONS).get().get().get("2.0.0").isPresent());
  }

  @Test
  public void testIfGitHubHasNoReleaseInfoButTags() throws IOException {
    final GHRepository repository = mock(GHRepository.class);

    PagedIterable<GHRelease> pagedIterable = mock(PagedIterable.class);
    when(pagedIterable.toList()).thenReturn(Collections.emptyList());
    when(repository.listReleases()).thenReturn(pagedIterable);

    PagedIterable<GHTag> pagedTagIterable = mock(PagedIterable.class);
    GHTag tag = mock(GHTag.class);
    when(tag.getName()).thenReturn("2.0.1");

    GHCommit commit = mock(GHCommit.class);
    when(commit.getCommitDate()).thenReturn(new Date());
    when(tag.getCommit()).thenReturn(commit);

    List<GHTag> tagList = Arrays.asList(tag);
    when(pagedTagIterable.toList()).thenReturn(tagList);
    when(repository.listTags()).thenReturn(pagedTagIterable);

    when(fetcher.github().getRepository(any())).thenReturn(repository);
    releasesFromGitHub = new ReleasesFromGitHub(fetcher);

    ValueHashSet values = new ValueHashSet();
    assertEquals(0, values.size());

    releasesFromGitHub.update(PROJECT, values);

    assertEquals(1, values.size());
    assertTrue(values.has(RELEASED_ARTIFACT_VERSIONS));
    assertTrue(values.of(RELEASED_ARTIFACT_VERSIONS).isPresent());
    assertFalse(values.of(RELEASED_ARTIFACT_VERSIONS).get().isUnknown());
    assertFalse(values.of(RELEASED_ARTIFACT_VERSIONS).get().get().empty());
    assertEquals(1, values.of(RELEASED_ARTIFACT_VERSIONS).get().get().size());
    assertTrue(values.of(RELEASED_ARTIFACT_VERSIONS).get().get().get("2.0.1").isPresent());
  }

  @Test
  public void testIfGitHubHasNoReleaseAndTags() throws IOException {
    final GHRepository repository = mock(GHRepository.class);

    PagedIterable<GHRelease> pagedIterable = mock(PagedIterable.class);
    when(pagedIterable.toList()).thenReturn(Collections.emptyList());
    when(repository.listReleases()).thenReturn(pagedIterable);

    PagedIterable<GHTag> pagedTagIterable = mock(PagedIterable.class);
    when(pagedTagIterable.toList()).thenReturn(Collections.emptyList());
    when(repository.listTags()).thenReturn(pagedTagIterable);

    evaluateRepository(repository);
  }

  @Test
  public void testGitHubRepoWhenReleasesHaveNulls() throws IOException {
    final GHRepository repository = mock(GHRepository.class);

    PagedIterable<GHRelease> pagedIterable = mock(PagedIterable.class);
    List<GHRelease> releases = Arrays.asList(new GHRelease[] {null});
    when(pagedIterable.toList()).thenReturn(releases);
    when(repository.listReleases()).thenReturn(pagedIterable);

    PagedIterable<GHTag> pagedTagIterable = mock(PagedIterable.class);
    when(pagedTagIterable.toList()).thenReturn(Collections.emptyList());
    when(repository.listTags()).thenReturn(pagedTagIterable);

    evaluateRepository(repository);
  }
  
  private void evaluateRepository(final GHRepository repository) throws IOException {
    when(fetcher.github().getRepository(any())).thenReturn(repository);
    releasesFromGitHub = new ReleasesFromGitHub(fetcher);

    ValueHashSet values = new ValueHashSet();
    assertEquals(0, values.size());

    releasesFromGitHub.update(PROJECT, values);

    assertEquals(1, values.size());
    assertTrue(values.has(RELEASED_ARTIFACT_VERSIONS));
    assertTrue(values.of(RELEASED_ARTIFACT_VERSIONS).isPresent());

    Value<ArtifactVersions> releasedArtifactVersions = values.of(RELEASED_ARTIFACT_VERSIONS).get();
    assertFalse(releasedArtifactVersions.isUnknown());
    assertTrue(releasedArtifactVersions.get().empty());
  }
}
