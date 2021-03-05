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
   * @param contextFactory A factory that provides contexts for advices.
   */
  public ArtifactVersionAdvisor(OssAdviceContextFactory contextFactory) {
    super(OssAdviceContentYamlStorage.DEFAULT, contextFactory);
  }

  @Override
  protected List<Advice> advicesFor(
      Subject subject, List<Value<?>> usedValues, OssAdviceContext context) {

    Optional<Value<String>> versionValue = findValue(usedValues, ARTIFACT_VERSION);
    Optional<Value<ArtifactVersions>> releasedVersionsValue = findValue(usedValues,
        RELEASED_ARTIFACT_VERSIONS);

    if (isAllVersionInformationAvailable(versionValue, releasedVersionsValue)) {
      ArtifactVersion latestVersion = getLatestVersion(releasedVersionsValue.get());
      String usedVersion = versionValue.get().get();

      if (!latestVersion.getVersion().equals(usedVersion)) {
        List<AdviceContent> advice =
            adviceStorage.advicesFor(versionValue.get().feature(),
                getAdviceContext(usedVersion, latestVersion.getVersion()));

        return advice.stream()
            .map(content -> new SimpleAdvice(subject, versionValue.get(), content))
            .map(Advice.class::cast)
            .collect(Collectors.toList());
      }
    }
    return Collections.emptyList();
  }

  private boolean isAllVersionInformationAvailable(
      Optional<Value<String>> versionValue,
      Optional<Value<ArtifactVersions>> releasedVersionsValue) {

    if (versionValue.isPresent() && releasedVersionsValue.isPresent()) {
      if (versionValue.get().isUnknown() || versionValue.get().get().isEmpty()) {
        return false;
      }
      if (releasedVersionsValue.get().isUnknown() || releasedVersionsValue.get().get().empty()) {
        return false;
      }
      return true;
    }
    return false;
  }

  private ArtifactVersion getLatestVersion(Value<ArtifactVersions> releasedVersionsValue) {
    Collection<ArtifactVersion> releasedVersions =
        releasedVersionsValue.get().getSortByReleaseDate();
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
