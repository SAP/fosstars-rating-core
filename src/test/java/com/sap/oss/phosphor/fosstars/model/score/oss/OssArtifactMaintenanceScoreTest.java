package com.sap.oss.phosphor.fosstars.model.score.oss;

import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.ARTIFACT_VERSION;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.IS_APACHE;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.IS_ECLIPSE;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.NUMBER_OF_COMMITS_LAST_THREE_MONTHS;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.NUMBER_OF_CONTRIBUTORS_LAST_THREE_MONTHS;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.NUMBER_OF_GITHUB_STARS;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.NUMBER_OF_WATCHERS_ON_GITHUB;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.PACKAGE_MANAGERS;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.RELEASED_ARTIFACT_VERSIONS;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.SUPPORTED_BY_COMPANY;
import static com.sap.oss.phosphor.fosstars.model.other.Utils.setOf;
import static com.sap.oss.phosphor.fosstars.model.value.PackageManager.MAVEN;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sap.oss.phosphor.fosstars.advice.Advisor;
import com.sap.oss.phosphor.fosstars.model.Confidence;
import com.sap.oss.phosphor.fosstars.model.Score;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.other.Utils;
import com.sap.oss.phosphor.fosstars.model.rating.oss.OssArtifactSecurityRating;
import com.sap.oss.phosphor.fosstars.model.rating.oss.OssArtifactSecurityRating.Thresholds;
import com.sap.oss.phosphor.fosstars.model.value.ArtifactVersion;
import com.sap.oss.phosphor.fosstars.model.value.ArtifactVersions;
import com.sap.oss.phosphor.fosstars.model.value.PackageManagers;
import com.sap.oss.phosphor.fosstars.model.value.RatingValue;
import com.sap.oss.phosphor.fosstars.model.value.ScoreValue;
import com.sap.oss.phosphor.fosstars.tool.format.PrettyPrinter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Set;
import org.junit.Ignore;
import org.junit.Test;

public class OssArtifactMaintenanceScoreTest {

  private static final double DELTA = 0.01;

  @Test
  @Ignore
  public void serializeAndDeserialize() throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    OssArtifactMaintenanceScore score = new OssArtifactMaintenanceScore();
    byte[] bytes = mapper.writeValueAsBytes(score);
    assertNotNull(bytes);
    assertTrue(bytes.length > 0);
    OssArtifactMaintenanceScore clone = mapper.readValue(bytes, OssArtifactMaintenanceScore.class);
    assertEquals(score, clone);
  }

  @Test
  @Ignore
  public void calculateForAllUnknown() {
    Score score = new OssArtifactMaintenanceScore();
    ScoreValue scoreValue = score.calculate(Utils.allUnknown(score.allFeatures()));
    assertEquals(Score.MIN, scoreValue.get(), 0.01);
    assertEquals(Confidence.MIN, scoreValue.confidence(), DELTA);
    checkUsedValues(scoreValue);
  }

  @Test
  public void calculate() {
    OssArtifactMaintenanceScore score = new OssArtifactMaintenanceScore();
    Set<Value> values = setOf(
        RELEASED_ARTIFACT_VERSIONS.value(testArtifactVersions(false)),
        ARTIFACT_VERSION.value("1.2.0"),
        SUPPORTED_BY_COMPANY.value(false),
        IS_APACHE.value(true),
        IS_ECLIPSE.value(false),
        NUMBER_OF_COMMITS_LAST_THREE_MONTHS.value(50),
        NUMBER_OF_CONTRIBUTORS_LAST_THREE_MONTHS.value(3),
        NUMBER_OF_GITHUB_STARS.value(10),
        NUMBER_OF_WATCHERS_ON_GITHUB.value(5),
        PACKAGE_MANAGERS.value(PackageManagers.from(MAVEN)));

    OssArtifactSecurityRating rating = new OssArtifactSecurityRating(score, Thresholds.DEFAULT);

    // FIXME (mibo): only for test reasons
    System.out.println("#########\n\ncalculate:");
    RatingValue ratingValue = rating.calculate(values);
    //    System.out.println(PrettyPrinter.withoutVerboseOutput().print(ratingValue));
    System.out.println(PrettyPrinter.withVerboseOutput(Advisor.DUMMY).print(ratingValue));
  }


  @Test
  public void calculateWithOldVersion() {
    OssArtifactMaintenanceScore score = new OssArtifactMaintenanceScore();
    Set<Value> values = setOf(
        RELEASED_ARTIFACT_VERSIONS.value(testArtifactVersions(false)),
        ARTIFACT_VERSION.value("1.0.0"),
        SUPPORTED_BY_COMPANY.value(false),
        IS_APACHE.value(true),
        IS_ECLIPSE.value(false),
        NUMBER_OF_COMMITS_LAST_THREE_MONTHS.value(50),
        NUMBER_OF_CONTRIBUTORS_LAST_THREE_MONTHS.value(3),
        NUMBER_OF_GITHUB_STARS.value(10),
        NUMBER_OF_WATCHERS_ON_GITHUB.value(5),
        PACKAGE_MANAGERS.value(PackageManagers.from(MAVEN)));

    OssArtifactSecurityRating rating = new OssArtifactSecurityRating(score, Thresholds.DEFAULT);

    // FIXME (mibo): only for test reasons
    System.out.println("#########\n\ncalculate:");
    RatingValue ratingValue = rating.calculate(values);
    //    System.out.println(PrettyPrinter.withoutVerboseOutput().print(ratingValue));
    System.out.println(PrettyPrinter.withVerboseOutput(Advisor.DUMMY).print(ratingValue));
  }


  @Test
  public void calculateWith20() {
    OssArtifactMaintenanceScore score = new OssArtifactMaintenanceScore();
    Set<Value> values = setOf(
        RELEASED_ARTIFACT_VERSIONS.value(testArtifactVersions(true)),
        ARTIFACT_VERSION.value("1.2.0"),
        SUPPORTED_BY_COMPANY.value(false),
        IS_APACHE.value(true),
        IS_ECLIPSE.value(false),
        NUMBER_OF_COMMITS_LAST_THREE_MONTHS.value(50),
        NUMBER_OF_CONTRIBUTORS_LAST_THREE_MONTHS.value(3),
        NUMBER_OF_GITHUB_STARS.value(10),
        NUMBER_OF_WATCHERS_ON_GITHUB.value(5));

    OssArtifactSecurityRating rating = new OssArtifactSecurityRating(score, Thresholds.DEFAULT);

    // FIXME (mibo): only for test reasons
    System.out.println("#########\n\ncalculateWith20:");
    RatingValue ratingValue = rating.calculate(values);
    //    System.out.println(PrettyPrinter.withoutVerboseOutput().print(ratingValue));
    System.out.println(PrettyPrinter.withVerboseOutput(Advisor.DUMMY).print(ratingValue));
  }


  @Test
  public void calculateWith20Used() {
    OssArtifactMaintenanceScore score = new OssArtifactMaintenanceScore();
    Set<Value> values = setOf(
        RELEASED_ARTIFACT_VERSIONS.value(testArtifactVersions(true)),
        ARTIFACT_VERSION.value("2.0.0"),
        SUPPORTED_BY_COMPANY.value(false),
        IS_APACHE.value(true),
        IS_ECLIPSE.value(false),
        NUMBER_OF_COMMITS_LAST_THREE_MONTHS.value(50),
        NUMBER_OF_CONTRIBUTORS_LAST_THREE_MONTHS.value(3),
        NUMBER_OF_GITHUB_STARS.value(10),
        NUMBER_OF_WATCHERS_ON_GITHUB.value(5));

    OssArtifactSecurityRating rating = new OssArtifactSecurityRating(score, Thresholds.DEFAULT);

    // FIXME (mibo): only for test reasons
    System.out.println("#########\n\ncalculateWith20Used:");
    RatingValue ratingValue = rating.calculate(values);
    //    System.out.println(PrettyPrinter.withoutVerboseOutput().print(ratingValue));
    System.out.println(PrettyPrinter.withVerboseOutput(Advisor.DUMMY).print(ratingValue));
  }

  private static ArtifactVersions testArtifactVersions(boolean with2xx) {
    ArtifactVersion version100 =
        new ArtifactVersion("1.0.0", LocalDate.now().minusMonths(14));
    ArtifactVersion version101 =
        new ArtifactVersion("1.0.1", LocalDate.now().minusMonths(13));
    ArtifactVersion version110 =
        new ArtifactVersion("1.1.0", LocalDate.now().minusMonths(6));
    ArtifactVersion version120 = new ArtifactVersion("1.2.0", LocalDate.now().minusDays(72));
    if (with2xx) {
      ArtifactVersion version200 = new ArtifactVersion("2.0.0", LocalDate.now().minusDays(7));
      return ArtifactVersions.of(version100, version101, version110, version120, version200);
    }
    return ArtifactVersions.of(version100, version101, version110, version120);
  }

  private static void checkUsedValues(ScoreValue scoreValue) {
    assertEquals(scoreValue.score().subScores().size(), scoreValue.usedValues().size());
    for (Value value : scoreValue.usedValues()) {
      boolean found = false;
      for (Score subScore : scoreValue.score().subScores()) {
        if (value.feature().getClass() == subScore.getClass()) {
          found = true;
        }
      }
      if (!found) {
        fail("Unexpected value: " + value.feature().getClass());
      }
    }
  }

}
