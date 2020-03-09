package com.sap.sgs.phosphor.fosstars.model.score.oss;

import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.FIRST_COMMIT_DATE;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.HAS_SECURITY_POLICY;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.HAS_SECURITY_TEAM;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.IS_APACHE;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.IS_ECLIPSE;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.NUMBER_OF_COMMITS_LAST_THREE_MONTHS;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.NUMBER_OF_CONTRIBUTORS_LAST_THREE_MONTHS;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.NUMBER_OF_GITHUB_STARS;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.NUMBER_OF_WATCHERS_ON_GITHUB;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.PROJECT_START_DATE;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.SCANS_FOR_VULNERABLE_DEPENDENCIES;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.SECURITY_REVIEWS_DONE;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.SUPPORTED_BY_COMPANY;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.VULNERABILITIES;
import static com.sap.sgs.phosphor.fosstars.model.other.Utils.setOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sap.sgs.phosphor.fosstars.model.Confidence;
import com.sap.sgs.phosphor.fosstars.model.Score;
import com.sap.sgs.phosphor.fosstars.model.Value;
import com.sap.sgs.phosphor.fosstars.model.other.Utils;
import com.sap.sgs.phosphor.fosstars.model.value.ScoreValue;
import com.sap.sgs.phosphor.fosstars.model.value.SecurityReviews;
import com.sap.sgs.phosphor.fosstars.model.value.Vulnerabilities;
import java.io.IOException;
import java.util.Date;
import java.util.Set;
import org.junit.Test;

public class OssSecurityScoreTest {

  private static final double DELTA = 0.01;

  @Test
  public void serializeAndDeserialize() throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    OssSecurityScore score = new OssSecurityScore();
    byte[] bytes = mapper.writeValueAsBytes(score);
    assertNotNull(bytes);
    assertTrue(bytes.length > 0);
    OssSecurityScore clone = mapper.readValue(bytes, OssSecurityScore.class);
    assertEquals(score, clone);
  }

  @Test
  public void calculateForAllUnknown() {
    Score score = new OssSecurityScore();
    ScoreValue scoreValue = score.calculate(Utils.allUnknown(score.allFeatures()));
    assertEquals(Score.MIN, scoreValue.get(), 0.01);
    assertEquals(Confidence.MIN, scoreValue.confidence(), DELTA);
    checkUsedValues(scoreValue);
  }

  @Test
  public void calculate() {
    Score score = new OssSecurityScore();
    Set<Value> values = setOf(
        SUPPORTED_BY_COMPANY.value(false),
        IS_APACHE.value(true),
        IS_ECLIPSE.value(false),
        NUMBER_OF_COMMITS_LAST_THREE_MONTHS.value(50),
        NUMBER_OF_CONTRIBUTORS_LAST_THREE_MONTHS.value(3),
        NUMBER_OF_GITHUB_STARS.value(10),
        NUMBER_OF_WATCHERS_ON_GITHUB.value(5),
        HAS_SECURITY_TEAM.value(false),
        HAS_SECURITY_POLICY.value(false),
        SECURITY_REVIEWS_DONE.value(SecurityReviews.NO_REVIEWS),
        SCANS_FOR_VULNERABLE_DEPENDENCIES.value(false),
        VULNERABILITIES.value(new Vulnerabilities()),
        PROJECT_START_DATE.value(new Date()),
        FIRST_COMMIT_DATE.value(new Date()));
    ScoreValue scoreValue = score.calculate(values);
    assertTrue(Score.INTERVAL.contains(scoreValue.get()));
    assertEquals(Confidence.MAX, scoreValue.confidence(), DELTA);
    checkUsedValues(scoreValue);
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