package com.sap.oss.phosphor.fosstars.data.artifact;

import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.RELEASED_ARTIFACT_VERSIONS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import com.sap.oss.phosphor.fosstars.data.github.ReleasesFromGitHub;
import com.sap.oss.phosphor.fosstars.data.github.TestGitHubDataFetcherHolder;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.subject.oss.GitHubProject;
import com.sap.oss.phosphor.fosstars.model.subject.oss.MavenArtifact;
import com.sap.oss.phosphor.fosstars.model.subject.oss.NpmArtifact;
import com.sap.oss.phosphor.fosstars.model.value.ArtifactVersion;
import com.sap.oss.phosphor.fosstars.model.value.ArtifactVersions;
import com.sap.oss.phosphor.fosstars.model.value.ValueHashSet;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.kohsuke.github.GHRelease;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GHTag;
import org.kohsuke.github.PagedIterable;

public class ReleaseInfoLoaderTest extends TestGitHubDataFetcherHolder {

  private static final GitHubProject PROJECT = new GitHubProject("org", "project");
  private static final MavenArtifact MAVEN_ARTIFACT =
      new MavenArtifact("group", "artifact", "1.0.0", PROJECT);
  private static final NpmArtifact NPM_ARTIFACT = new NpmArtifact("identifier", "1.1", PROJECT);
  private ReleaseInfoFromNpm releaseInfoFromNpm;
  private ReleaseInfoFromMaven releaseInfoFromMaven;
  private ReleasesFromGitHub releasesFromGitHub;

  @Before
  public void setup() throws IOException {
    final GHRepository repository = mock(GHRepository.class);

    PagedIterable<GHRelease> pagedIterable = mock(PagedIterable.class);
    when(repository.listReleases()).thenReturn(pagedIterable);

    GHRelease release = mock(GHRelease.class);
    when(release.getName()).thenReturn("2.0.0");
    when(release.getPublished_at()).thenReturn(new Date());

    List<GHRelease> releaselist = Arrays.asList(release);
    when(pagedIterable.toList()).thenReturn(releaselist);

    when(fetcher.github().getRepository(any())).thenReturn(repository);
    releasesFromGitHub = new ReleasesFromGitHub(fetcher);

    releaseInfoFromNpm = mock(ReleaseInfoFromNpm.class);
    releaseInfoFromMaven = mock(ReleaseInfoFromMaven.class);
  }

  @Test
  public void testIfArtifactIsMavenArtifact() throws IOException {
    ValueHashSet values = new ValueHashSet();
    assertEquals(0, values.size());

    doAnswer(invocation -> {
      Object[] args = invocation.getArguments();
      ((ValueHashSet) args[1]).update(RELEASED_ARTIFACT_VERSIONS
          .value(new ArtifactVersions(new ArtifactVersion("1.10.10", LocalDateTime.now()))));
      return null;
    }).when(releaseInfoFromMaven).update(MAVEN_ARTIFACT, values);

    ReleaseInfoLoader provider =
        new ReleaseInfoLoader(releasesFromGitHub, releaseInfoFromMaven, releaseInfoFromNpm);
    provider.update(MAVEN_ARTIFACT, values);

    assertEquals(1, values.size());
    assertTrue(values.has(RELEASED_ARTIFACT_VERSIONS));
    assertTrue(values.of(RELEASED_ARTIFACT_VERSIONS).isPresent());
    assertFalse(values.of(RELEASED_ARTIFACT_VERSIONS).get().isUnknown());
    assertFalse(values.of(RELEASED_ARTIFACT_VERSIONS).get().get().empty());
    assertEquals(1, values.of(RELEASED_ARTIFACT_VERSIONS).get().get().size());
    assertTrue(values.of(RELEASED_ARTIFACT_VERSIONS).get().get().get("1.10.10").isPresent());
  }

  @Test
  public void testIfArtifactIsNpmArtifact() throws IOException {
    ValueHashSet values = new ValueHashSet();
    assertEquals(0, values.size());

    doAnswer(invocation -> {
      Object[] args = invocation.getArguments();
      ((ValueHashSet) args[1]).update(RELEASED_ARTIFACT_VERSIONS
          .value(new ArtifactVersions(new ArtifactVersion("0.7.1", LocalDateTime.now()))));
      return null;
    }).when(releaseInfoFromNpm).update(NPM_ARTIFACT, values);

    ReleaseInfoLoader provider =
        new ReleaseInfoLoader(releasesFromGitHub, releaseInfoFromMaven, releaseInfoFromNpm);
    provider.update(NPM_ARTIFACT, values);

    assertEquals(1, values.size());
    assertTrue(values.has(RELEASED_ARTIFACT_VERSIONS));
    assertTrue(values.of(RELEASED_ARTIFACT_VERSIONS).isPresent());
    assertFalse(values.of(RELEASED_ARTIFACT_VERSIONS).get().isUnknown());
    assertFalse(values.of(RELEASED_ARTIFACT_VERSIONS).get().get().empty());
    assertEquals(1, values.of(RELEASED_ARTIFACT_VERSIONS).get().get().size());
    assertTrue(values.of(RELEASED_ARTIFACT_VERSIONS).get().get().get("0.7.1").isPresent());
  }

  @Test
  public void testIfMavenArtifactHasNoReleaseInfoOnGitHub() throws IOException {
    final GHRepository repository = mock(GHRepository.class);

    PagedIterable<GHRelease> pagedIterable = mock(PagedIterable.class);
    when(pagedIterable.toList()).thenReturn(Collections.emptyList());
    when(repository.listReleases()).thenReturn(pagedIterable);

    PagedIterable<GHTag> pagedTagIterable = mock(PagedIterable.class);
    when(pagedTagIterable.toList()).thenReturn(Collections.emptyList());
    when(repository.listTags()).thenReturn(pagedTagIterable);

    when(fetcher.github().getRepository(any())).thenReturn(repository);
    releasesFromGitHub = new ReleasesFromGitHub(fetcher);

    ValueHashSet values = new ValueHashSet();
    assertEquals(0, values.size());
    doReturn(releaseInfoFromMaven).when(releaseInfoFromMaven).update(MAVEN_ARTIFACT, values);

    ReleaseInfoLoader provider =
        new ReleaseInfoLoader(releasesFromGitHub, releaseInfoFromMaven, releaseInfoFromNpm);
    provider.update(MAVEN_ARTIFACT, values);

    assertEquals(1, values.size());
    assertTrue(values.has(RELEASED_ARTIFACT_VERSIONS));
    assertTrue(values.of(RELEASED_ARTIFACT_VERSIONS).isPresent());

    Value<ArtifactVersions> releasedArtifactVersions = values.of(RELEASED_ARTIFACT_VERSIONS).get();
    assertFalse(releasedArtifactVersions.isUnknown());
    assertTrue(releasedArtifactVersions.get().empty());
  }

  @Test
  public void testIfMavenArtifactHasReleaseInfoOnGitHub() throws IOException {
    releaseInfoFromMaven = new ReleaseInfoFromMaven();
    releaseInfoFromMaven = spy(releaseInfoFromMaven);

    ValueHashSet values = new ValueHashSet();
    assertEquals(0, values.size());

    doReturn(releaseInfoFromMaven).when(releaseInfoFromMaven).update(MAVEN_ARTIFACT, values);

    ReleaseInfoLoader provider =
        new ReleaseInfoLoader(releasesFromGitHub, releaseInfoFromMaven, releaseInfoFromNpm);
    provider.update(MAVEN_ARTIFACT, values);

    assertEquals(1, values.size());
    assertTrue(values.has(RELEASED_ARTIFACT_VERSIONS));
    assertTrue(values.of(RELEASED_ARTIFACT_VERSIONS).isPresent());
    assertFalse(values.of(RELEASED_ARTIFACT_VERSIONS).get().isUnknown());
    assertFalse(values.of(RELEASED_ARTIFACT_VERSIONS).get().get().empty());
    assertEquals(1, values.of(RELEASED_ARTIFACT_VERSIONS).get().get().size());
    assertTrue(values.of(RELEASED_ARTIFACT_VERSIONS).get().get().get("2.0.0").isPresent());
  }

  @Test
  public void testIfMavenArtifactHasNoGitHubProject() throws IOException {
    releaseInfoFromMaven = new ReleaseInfoFromMaven();
    releaseInfoFromMaven = spy(releaseInfoFromMaven);
    MavenArtifact artifact = new MavenArtifact("group", "artifact", "1.0.0", null);

    ValueHashSet values = new ValueHashSet();
    assertEquals(0, values.size());

    doReturn(releaseInfoFromMaven).when(releaseInfoFromMaven).update(artifact, values);

    ReleaseInfoLoader provider =
        new ReleaseInfoLoader(releasesFromGitHub, releaseInfoFromMaven, releaseInfoFromNpm);
    provider.update(artifact, values);

    assertEquals(1, values.size());
    assertTrue(values.has(RELEASED_ARTIFACT_VERSIONS));
    assertTrue(values.of(RELEASED_ARTIFACT_VERSIONS).isPresent());
    assertTrue(values.of(RELEASED_ARTIFACT_VERSIONS).get().isUnknown());
  }

  @Test(expected = IOException.class)
  public void testIfValuesHasFeature() throws IOException {
    ValueHashSet values = new ValueHashSet();
    ArtifactVersions artifactVersions =
        new ArtifactVersions(new ArtifactVersion("3.0.0", LocalDateTime.now()));
    values.update(RELEASED_ARTIFACT_VERSIONS.value(artifactVersions));

    assertEquals(1, values.size());
    assertTrue(values.has(RELEASED_ARTIFACT_VERSIONS));
    assertTrue(values.of(RELEASED_ARTIFACT_VERSIONS).isPresent());
    assertFalse(values.of(RELEASED_ARTIFACT_VERSIONS).get().isUnknown());
    assertFalse(values.of(RELEASED_ARTIFACT_VERSIONS).get().get().empty());
    assertEquals(1, values.of(RELEASED_ARTIFACT_VERSIONS).get().get().size());
    assertTrue(values.of(RELEASED_ARTIFACT_VERSIONS).get().get().get("3.0.0").isPresent());

    ReleaseInfoLoader provider =
        new ReleaseInfoLoader(releasesFromGitHub, releaseInfoFromMaven, releaseInfoFromNpm);
    provider.update(NPM_ARTIFACT, values);
  }

  @Test
  public void testIfValueHasUnknownFeature() throws IOException {
    ValueHashSet values = new ValueHashSet();
    values.update(RELEASED_ARTIFACT_VERSIONS.unknown());

    assertEquals(1, values.size());
    assertTrue(values.has(RELEASED_ARTIFACT_VERSIONS));
    assertTrue(values.of(RELEASED_ARTIFACT_VERSIONS).isPresent());
    assertTrue(values.of(RELEASED_ARTIFACT_VERSIONS).get().isUnknown());

    doReturn(releaseInfoFromMaven).when(releaseInfoFromMaven).update(MAVEN_ARTIFACT, values);

    ReleaseInfoLoader provider =
        new ReleaseInfoLoader(releasesFromGitHub, releaseInfoFromMaven, releaseInfoFromNpm);
    provider.update(NPM_ARTIFACT, values);

    assertEquals(1, values.size());
    assertTrue(values.has(RELEASED_ARTIFACT_VERSIONS));
    assertTrue(values.of(RELEASED_ARTIFACT_VERSIONS).isPresent());
    assertFalse(values.of(RELEASED_ARTIFACT_VERSIONS).get().isUnknown());
    assertFalse(values.of(RELEASED_ARTIFACT_VERSIONS).get().get().empty());
    assertEquals(1, values.of(RELEASED_ARTIFACT_VERSIONS).get().get().size());
    assertTrue(values.of(RELEASED_ARTIFACT_VERSIONS).get().get().get("2.0.0").isPresent());
  }
}