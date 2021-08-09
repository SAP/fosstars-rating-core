package com.sap.oss.phosphor.fosstars.model.score.oss;

import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.OWASP_DEPENDENCY_CHECK_FAIL_CVSS_THRESHOLD;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.OWASP_DEPENDENCY_CHECK_USAGE;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.PACKAGE_MANAGERS;
import static com.sap.oss.phosphor.fosstars.model.value.PackageManager.MAVEN;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.sap.oss.phosphor.fosstars.model.Score;
import com.sap.oss.phosphor.fosstars.model.value.OwaspDependencyCheckUsage;
import com.sap.oss.phosphor.fosstars.model.value.PackageManagers;
import com.sap.oss.phosphor.fosstars.model.value.ScoreValue;
import org.junit.Test;

public class OwaspDependencyScanScoreTest {

  private static final OwaspDependencyScanScore SCORE = new OwaspDependencyScanScore();

  @Test
  public void testWithUnknown() {
    ScoreValue value = SCORE.calculate(
        OWASP_DEPENDENCY_CHECK_USAGE.unknown(),
        OWASP_DEPENDENCY_CHECK_FAIL_CVSS_THRESHOLD.unknown(),
        PACKAGE_MANAGERS.unknown());
    assertNotNull(value);
    assertTrue(value.isUnknown());
  }

  @Test
  public void testCalculate() {
    ScoreValue value = SCORE.calculate(
        OWASP_DEPENDENCY_CHECK_USAGE.value(OwaspDependencyCheckUsage.OPTIONAL),
        OWASP_DEPENDENCY_CHECK_FAIL_CVSS_THRESHOLD.value(7.2),
        PACKAGE_MANAGERS.value(PackageManagers.from(MAVEN)));
    assertNotNull(value);
    assertFalse(value.isUnknown());
    assertTrue(Score.INTERVAL.contains(value.get()));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testWithNoUsage() {
    SCORE.calculate(OWASP_DEPENDENCY_CHECK_FAIL_CVSS_THRESHOLD.unknown());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testWithNoThreshold() {
    SCORE.calculate(OWASP_DEPENDENCY_CHECK_USAGE.unknown());
  }
}