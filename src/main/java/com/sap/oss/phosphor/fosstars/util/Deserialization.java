package com.sap.oss.phosphor.fosstars.util;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator.Builder;
import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;
import com.sap.oss.phosphor.fosstars.model.feature.BooleanFeature;
import com.sap.oss.phosphor.fosstars.model.feature.BoundedDoubleFeature;
import com.sap.oss.phosphor.fosstars.model.feature.BoundedIntegerFeature;
import com.sap.oss.phosphor.fosstars.model.feature.DateFeature;
import com.sap.oss.phosphor.fosstars.model.feature.DoubleFeature;
import com.sap.oss.phosphor.fosstars.model.feature.EnumFeature;
import com.sap.oss.phosphor.fosstars.model.feature.LgtmGradeFeature;
import com.sap.oss.phosphor.fosstars.model.feature.OwaspDependencyCheckCvssThreshold;
import com.sap.oss.phosphor.fosstars.model.feature.OwaspDependencyCheckUsageFeature;
import com.sap.oss.phosphor.fosstars.model.feature.PositiveIntegerFeature;
import com.sap.oss.phosphor.fosstars.model.feature.StringFeature;
import com.sap.oss.phosphor.fosstars.model.feature.example.NumberOfCommitsLastMonthExample;
import com.sap.oss.phosphor.fosstars.model.feature.example.NumberOfContributorsLastMonthExample;
import com.sap.oss.phosphor.fosstars.model.feature.example.SecurityReviewDoneExample;
import com.sap.oss.phosphor.fosstars.model.feature.example.StaticCodeAnalysisDoneExample;
import com.sap.oss.phosphor.fosstars.model.feature.oss.ArtifactVersionFeature;
import com.sap.oss.phosphor.fosstars.model.feature.oss.ArtifactVersionsFeature;
import com.sap.oss.phosphor.fosstars.model.feature.oss.LanguagesFeature;
import com.sap.oss.phosphor.fosstars.model.feature.oss.PackageManagersFeature;
import com.sap.oss.phosphor.fosstars.model.feature.oss.SecurityReviewsFeature;
import com.sap.oss.phosphor.fosstars.model.feature.oss.VulnerabilitiesFeature;
import com.sap.oss.phosphor.fosstars.model.math.DoubleInterval;
import com.sap.oss.phosphor.fosstars.model.qa.ScoreTestVector;
import com.sap.oss.phosphor.fosstars.model.qa.StandardTestVector;
import com.sap.oss.phosphor.fosstars.model.qa.TestScoreValue;
import com.sap.oss.phosphor.fosstars.model.rating.NotApplicableLabel;
import com.sap.oss.phosphor.fosstars.model.rating.example.SecurityRatingExample;
import com.sap.oss.phosphor.fosstars.model.rating.oss.OssArtifactSecurityRating.ArtifactSecurityLabel;
import com.sap.oss.phosphor.fosstars.model.rating.oss.OssRulesOfPlayRating;
import com.sap.oss.phosphor.fosstars.model.rating.oss.OssSecurityRating;
import com.sap.oss.phosphor.fosstars.model.rating.oss.SecurityRiskIntroducedByOss;
import com.sap.oss.phosphor.fosstars.model.score.AverageCompositeScore;
import com.sap.oss.phosphor.fosstars.model.score.WeightedCompositeScore;
import com.sap.oss.phosphor.fosstars.model.score.example.ProjectActivityScoreExample;
import com.sap.oss.phosphor.fosstars.model.score.example.SecurityScoreExample;
import com.sap.oss.phosphor.fosstars.model.score.example.SecurityTestingScoreExample;
import com.sap.oss.phosphor.fosstars.model.score.oss.ArtifactLatestReleaseAgeScore;
import com.sap.oss.phosphor.fosstars.model.score.oss.ArtifactReleaseHistoryScore;
import com.sap.oss.phosphor.fosstars.model.score.oss.ArtifactVersionUpToDateScore;
import com.sap.oss.phosphor.fosstars.model.score.oss.ArtifactVersionVulnerabilityScore;
import com.sap.oss.phosphor.fosstars.model.score.oss.BanditScore;
import com.sap.oss.phosphor.fosstars.model.score.oss.CodeqlScore;
import com.sap.oss.phosphor.fosstars.model.score.oss.CommunityCommitmentScore;
import com.sap.oss.phosphor.fosstars.model.score.oss.DependabotScore;
import com.sap.oss.phosphor.fosstars.model.score.oss.DependencyScanScore;
import com.sap.oss.phosphor.fosstars.model.score.oss.FindSecBugsScore;
import com.sap.oss.phosphor.fosstars.model.score.oss.FuzzingScore;
import com.sap.oss.phosphor.fosstars.model.score.oss.GoSecScore;
import com.sap.oss.phosphor.fosstars.model.score.oss.LgtmScore;
import com.sap.oss.phosphor.fosstars.model.score.oss.MemorySafetyTestingScore;
import com.sap.oss.phosphor.fosstars.model.score.oss.MyPyScore;
import com.sap.oss.phosphor.fosstars.model.score.oss.NoHttpToolScore;
import com.sap.oss.phosphor.fosstars.model.score.oss.OssArtifactSecurityScore;
import com.sap.oss.phosphor.fosstars.model.score.oss.OssRulesOfPlayScore;
import com.sap.oss.phosphor.fosstars.model.score.oss.OssSecurityScore;
import com.sap.oss.phosphor.fosstars.model.score.oss.OwaspDependencyScanScore;
import com.sap.oss.phosphor.fosstars.model.score.oss.ProjectActivityScore;
import com.sap.oss.phosphor.fosstars.model.score.oss.ProjectPopularityScore;
import com.sap.oss.phosphor.fosstars.model.score.oss.ProjectSecurityAwarenessScore;
import com.sap.oss.phosphor.fosstars.model.score.oss.ProjectSecurityTestingScore;
import com.sap.oss.phosphor.fosstars.model.score.oss.PylintScore;
import com.sap.oss.phosphor.fosstars.model.score.oss.SecurityReviewScore;
import com.sap.oss.phosphor.fosstars.model.score.oss.SnykDependencyScanScore;
import com.sap.oss.phosphor.fosstars.model.score.oss.StaticAnalysisScore;
import com.sap.oss.phosphor.fosstars.model.score.oss.UnpatchedVulnerabilitiesScore;
import com.sap.oss.phosphor.fosstars.model.score.oss.VulnerabilityDiscoveryAndSecurityTestingScore;
import com.sap.oss.phosphor.fosstars.model.score.oss.VulnerabilityLifetimeScore;
import com.sap.oss.phosphor.fosstars.model.score.oss.risk.AdoptedRiskLikelihoodFactor;
import com.sap.oss.phosphor.fosstars.model.score.oss.risk.CalculatedSecurityRiskIntroducedByOss;
import com.sap.oss.phosphor.fosstars.model.score.oss.risk.DataConfidentialityRiskImpactFactor;
import com.sap.oss.phosphor.fosstars.model.score.oss.risk.FunctionalityRiskLikelihoodFactor;
import com.sap.oss.phosphor.fosstars.model.score.oss.risk.HandlingUntrustedDataRiskLikelihoodFactor;
import com.sap.oss.phosphor.fosstars.model.score.oss.risk.ImpactScore;
import com.sap.oss.phosphor.fosstars.model.score.oss.risk.RiskImpactScore;
import com.sap.oss.phosphor.fosstars.model.score.oss.risk.RiskLikelihoodCoefficient;
import com.sap.oss.phosphor.fosstars.model.score.oss.risk.RiskLikelihoodFactors;
import com.sap.oss.phosphor.fosstars.model.score.oss.risk.RiskLikelihoodScore;
import com.sap.oss.phosphor.fosstars.model.score.oss.risk.UsageRiskLikelihoodFactor;
import com.sap.oss.phosphor.fosstars.model.subject.oss.GitHubOrganization;
import com.sap.oss.phosphor.fosstars.model.subject.oss.GitHubProject;
import com.sap.oss.phosphor.fosstars.model.subject.oss.MavenArtifact;
import com.sap.oss.phosphor.fosstars.model.subject.oss.NpmArtifact;
import com.sap.oss.phosphor.fosstars.model.value.ArtifactVersionValue;
import com.sap.oss.phosphor.fosstars.model.value.ArtifactVersionsValue;
import com.sap.oss.phosphor.fosstars.model.value.BooleanValue;
import com.sap.oss.phosphor.fosstars.model.value.CVSS;
import com.sap.oss.phosphor.fosstars.model.value.DateValue;
import com.sap.oss.phosphor.fosstars.model.value.DoubleValue;
import com.sap.oss.phosphor.fosstars.model.value.EnumValue;
import com.sap.oss.phosphor.fosstars.model.value.ExpiringValue;
import com.sap.oss.phosphor.fosstars.model.value.IntegerValue;
import com.sap.oss.phosphor.fosstars.model.value.LanguagesValue;
import com.sap.oss.phosphor.fosstars.model.value.LgtmGradeValue;
import com.sap.oss.phosphor.fosstars.model.value.NotApplicableValue;
import com.sap.oss.phosphor.fosstars.model.value.OwaspDependencyCheckCvssThresholdValue;
import com.sap.oss.phosphor.fosstars.model.value.OwaspDependencyCheckUsageValue;
import com.sap.oss.phosphor.fosstars.model.value.PackageManagersValue;
import com.sap.oss.phosphor.fosstars.model.value.ScoreValue;
import com.sap.oss.phosphor.fosstars.model.value.SecurityReviewsValue;
import com.sap.oss.phosphor.fosstars.model.value.StringValue;
import com.sap.oss.phosphor.fosstars.model.value.UnknownValue;
import com.sap.oss.phosphor.fosstars.model.value.ValueHashSet;
import com.sap.oss.phosphor.fosstars.model.value.VulnerabilitiesValue;
import com.sap.oss.phosphor.fosstars.model.weight.ImmutableWeight;
import com.sap.oss.phosphor.fosstars.model.weight.MutableWeight;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * The class holds common functionality for JSON and YAML serialization/deserialization.
 */
public abstract class Deserialization {

  /**
   * A type reference for deserialization to a Map.
   */
  static final TypeReference<Map<String, Object>> MAP_TYPE_REFERENCE
      = new TypeReference<Map<String, Object>>() {};

  /**
   * A list of types that are allowed for deserialization by default.
   */
  private static final List<String> DEFAULT_ALLOWED_SUB_TYPES = Collections.singletonList(
      "com.sap.oss.phosphor.fosstars");

  /**
   * A list of classes that are allowed for deserialization.
   */
  private static final List<String> ALLOWED_SUB_TYPES = new ArrayList<>(DEFAULT_ALLOWED_SUB_TYPES);

  /**
   * Allows deserialization of a specified packages or classes.
   *
   * @param patterns The packages or classes.
   */
  public static void allow(String... patterns) {
    Objects.requireNonNull(patterns, "Oh no! Patterns are null!");
    ALLOWED_SUB_TYPES.addAll(Arrays.asList(patterns));
  }

  /**
   * Creates a validator for deserialization.
   *
   * @return A validator for deserialization.
   */
  static PolymorphicTypeValidator validator() {
    Builder builder = BasicPolymorphicTypeValidator.builder();
    ALLOWED_SUB_TYPES.forEach(builder::allowIfSubType);
    return builder.build();
  }

  /**
   * Reads a list from a node.
   *
   * @param root The node.
   * @param property A field that has the list.
   * @return A list of elements.
   * @throws IOException If something went wrong.
   */
  public static List<String> readListFrom(JsonNode root, String property) throws IOException {
    if (!root.has(property)) {
      return emptyList();
    }

    JsonNode node = root.get(property);
    if (node.isTextual()) {
      return singletonList(node.asText());
    }

    if (!node.isArray()) {
      throw new IOException(
          String.format("Oops! '%s' is not an array and not a string!", property));
    }

    List<String> list = new ArrayList<>();
    Iterator<JsonNode> iterator = node.elements();
    while (iterator.hasNext()) {
      JsonNode element = iterator.next();
      if (!element.isTextual()) {
        throw new IOException(String.format("Oops! Element of '%s' is not a string!", property));
      }
      list.add(element.asText());
    }

    return list;
  }

  /**
   * Registers known sub-types in an {@link ObjectMapper}.
   *
   * @param mapper The {@link ObjectMapper}.
   * @return The same {@link ObjectMapper};
   */
  static ObjectMapper registerSubTypesIn(ObjectMapper mapper) {

    // features
    mapper.registerSubtypes(
        PositiveIntegerFeature.class,
        DoubleFeature.class,
        BooleanFeature.class,
        BoundedIntegerFeature.class,
        BoundedDoubleFeature.class,
        DateFeature.class,
        EnumFeature.class,
        VulnerabilitiesFeature.class,
        SecurityReviewDoneExample.class,
        StaticCodeAnalysisDoneExample.class,
        NumberOfCommitsLastMonthExample.class,
        NumberOfContributorsLastMonthExample.class,
        LgtmGradeFeature.class,
        LanguagesFeature.class,
        PackageManagersFeature.class,
        StringFeature.class,
        ArtifactVersionFeature.class,
        ArtifactVersionsFeature.class,
        OwaspDependencyCheckUsageFeature.class,
        OwaspDependencyCheckCvssThreshold.class,
        SecurityReviewsFeature.class
    );

    // values
    mapper.registerSubtypes(
        IntegerValue.class,
        DoubleValue.class,
        BooleanValue.class,
        DateValue.class,
        ScoreValue.class,
        ExpiringValue.class,
        VulnerabilitiesValue.class,
        UnknownValue.class,
        NotApplicableValue.class,
        EnumValue.class,
        LgtmGradeValue.class,
        LanguagesValue.class,
        PackageManagersValue.class,
        StringValue.class,
        ArtifactVersionValue.class,
        ArtifactVersionsValue.class,
        OwaspDependencyCheckUsageValue.class,
        OwaspDependencyCheckCvssThresholdValue.class,
        SecurityReviewsValue.class,
        TestScoreValue.class
    );

    // labels
    mapper.registerSubtypes(
        ArtifactSecurityLabel.class,
        OssSecurityRating.SecurityLabel.class,
        SecurityRatingExample.SecurityLabelExample.class,
        OssRulesOfPlayRating.OssRulesOfPlayLabel.class,
        NotApplicableLabel.class,
        SecurityRiskIntroducedByOss.OssSecurityRiskLabel.class
    );

    // scores
    mapper.registerSubtypes(
        SecurityScoreExample.class,
        ProjectActivityScoreExample.class,
        SecurityTestingScoreExample.class,
        WeightedCompositeScore.class,
        AverageCompositeScore.class,
        ProjectActivityScore.class,
        ProjectPopularityScore.class,
        CommunityCommitmentScore.class,
        ProjectSecurityAwarenessScore.class,
        ProjectSecurityTestingScore.class,
        UnpatchedVulnerabilitiesScore.class,
        VulnerabilityLifetimeScore.class,
        OssSecurityScore.class,
        DependencyScanScore.class,
        LgtmScore.class,
        CodeqlScore.class,
        NoHttpToolScore.class,
        MemorySafetyTestingScore.class,
        FindSecBugsScore.class,
        FuzzingScore.class,
        StaticAnalysisScore.class,
        DependabotScore.class,
        SnykDependencyScanScore.class,
        OwaspDependencyScanScore.class,
        VulnerabilityDiscoveryAndSecurityTestingScore.class,
        SecurityReviewScore.class,
        OssArtifactSecurityScore.class,
        OssRulesOfPlayScore.class,
        ArtifactLatestReleaseAgeScore.class,
        ArtifactVersionUpToDateScore.class,
        ArtifactVersionVulnerabilityScore.class,
        ArtifactReleaseHistoryScore.class,
        UsageRiskLikelihoodFactor.class,
        FunctionalityRiskLikelihoodFactor.class,
        HandlingUntrustedDataRiskLikelihoodFactor.class,
        AdoptedRiskLikelihoodFactor.class,
        RiskLikelihoodCoefficient.class,
        RiskLikelihoodFactors.class,
        RiskLikelihoodScore.class,
        DataConfidentialityRiskImpactFactor.class,
        ImpactScore.class,
        RiskImpactScore.class,
        RiskImpactScore.ConfidentialityRiskImpactFactor.class,
        RiskImpactScore.IntegrityRiskImpactFactor.class,
        RiskImpactScore.AvailabilityRiskImpactFactor.class,
        CalculatedSecurityRiskIntroducedByOss.class,
        BanditScore.class,
        GoSecScore.class,
        PylintScore.class,
        MyPyScore.class
    );

    // ratings
    mapper.registerSubtypes(
        SecurityRatingExample.class,
        OssSecurityRating.class,
        OssRulesOfPlayScore.class,
        SecurityRiskIntroducedByOss.class
    );

    // weights
    mapper.registerSubtypes(MutableWeight.class, ImmutableWeight.class);

    // test vectors
    mapper.registerSubtypes(StandardTestVector.class, ScoreTestVector.class);

    // artifacts
    mapper.registerSubtypes(MavenArtifact.class, NpmArtifact.class);

    // other
    mapper.registerSubtypes(
        GitHubProject.class, GitHubOrganization.class, DoubleInterval.class, ValueHashSet.class,
        CVSS.V2.class, CVSS.V3.class);

    return mapper;
  }
}
