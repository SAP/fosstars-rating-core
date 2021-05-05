package com.sap.oss.phosphor.fosstars.advice.oss;

import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.ARTIFACT_VERSION;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.RELEASED_ARTIFACT_VERSIONS;
import static com.sap.oss.phosphor.fosstars.model.other.Utils.findValue;

import com.sap.oss.phosphor.fosstars.advice.Advice;
import com.sap.oss.phosphor.fosstars.advice.AdviceContent;
import com.sap.oss.phosphor.fosstars.advice.AdviceContext;
import com.sap.oss.phosphor.fosstars.advice.SimpleAdvice;
import com.sap.oss.phosphor.fosstars.advice.oss.OssAdviceContentYamlStorage.OssAdviceContext;
import com.sap.oss.phosphor.fosstars.model.Subject;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.value.ArtifactVersion;
import com.sap.oss.phosphor.fosstars.model.value.ArtifactVersions;
import java.net.MalformedURLException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * An advisor for used version in comparison to released versions.
 */
public class ArtifactVersionAdvisor extends AbstractOssAdvisor {

  /**
   * Create a new advisor.
   *
   * @param contextFactory A factory that provides contexts for advice.
   */
  public ArtifactVersionAdvisor(OssAdviceContextFactory contextFactory) {
    super(OssAdviceContentYamlStorage.DEFAULT, contextFactory);
  }

  @Override
  protected List<Advice> adviceFor(
      Subject subject, List<Value<?>> usedValues, OssAdviceContext context)
      throws MalformedURLException {

    Optional<Value<ArtifactVersion>> versionValue = findValue(usedValues, ARTIFACT_VERSION);
    Optional<Value<ArtifactVersions>> releasedVersionsValue = findValue(usedValues,
        RELEASED_ARTIFACT_VERSIONS);

    if (isAllVersionInformationAvailable(versionValue, releasedVersionsValue)) {
      // isAllVersionInformationAvailable() checks that both values are present and known
      ArtifactVersion latestArtifact = getLatestVersion(releasedVersionsValue.get());
      String usedVersion = versionValue.get().get().version();

      if (!latestArtifact.version().equals(usedVersion)) {
        List<AdviceContent> advice =
            adviceStorage.adviceFor(versionValue.get().feature(),
                getAdviceContext(usedVersion, latestArtifact.version()));

        return advice.stream()
            .map(content -> new SimpleAdvice(subject, versionValue.get(), content))
            .map(Advice.class::cast)
            .collect(Collectors.toList());
      }
    }
    return Collections.emptyList();
  }

  private boolean isAllVersionInformationAvailable(
      Optional<Value<ArtifactVersion>> versionValue,
      Optional<Value<ArtifactVersions>> releasedVersionsValue) {

    if (!versionValue.isPresent() || !releasedVersionsValue.isPresent()) {
      return false;
    }

    Value<ArtifactVersion> version = versionValue.get();
    Value<ArtifactVersions> releasedVersions = releasedVersionsValue.get();
    return !version.isUnknown() && !releasedVersions.isUnknown()
        && !version.get().version().isEmpty() && !releasedVersions.get().empty();
  }

  private ArtifactVersion getLatestVersion(Value<ArtifactVersions> releasedVersionsValue) {
    Collection<ArtifactVersion> releasedVersions =
        releasedVersionsValue.get().sortByReleaseDate();
    if (releasedVersions.isEmpty()) {
      return ArtifactVersion.EMPTY;
    }
    return releasedVersions.iterator().next();
  }

  private AdviceContext getAdviceContext(String version, String latestVersion) {
    return variable -> {
      if ("VERSION".equals(variable)) {
        return Optional.ofNullable(version);
      }
      if ("LATEST_VERSION".equals(variable)) {
        return Optional.ofNullable(latestVersion);
      }
      return Optional.empty();
    };
  }
}
