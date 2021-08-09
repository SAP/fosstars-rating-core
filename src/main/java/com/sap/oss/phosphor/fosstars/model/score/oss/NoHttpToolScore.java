package com.sap.oss.phosphor.fosstars.model.score.oss;

import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.PACKAGE_MANAGERS;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_NOHTTP;
import static com.sap.oss.phosphor.fosstars.model.other.Utils.findValue;
import static com.sap.oss.phosphor.fosstars.model.value.PackageManager.GRADLE;
import static com.sap.oss.phosphor.fosstars.model.value.PackageManager.MAVEN;

import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.score.FeatureBasedScore;
import com.sap.oss.phosphor.fosstars.model.value.PackageManagers;
import com.sap.oss.phosphor.fosstars.model.value.ScoreValue;

/**
 * <p>The score shows if an open-source project uses nohttp tool
 * to catch usage of insecure HTTP protocol.</p>
 *
 * <p>The score is based on the following features.</p>
 * <ul>
 *   <li>{@link com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures#USES_NOHTTP}</li>
 *   <li>{@link com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures#PACKAGE_MANAGERS}</li>
 * </ul>
 *
 * @see <a href="https://github.com/spring-io/nohttp">nohttp</a>
 */
public class NoHttpToolScore extends FeatureBasedScore {

  /**
   * Initializes a new score.
   */
  NoHttpToolScore() {
    super("If a project uses nohttp tool", USES_NOHTTP, PACKAGE_MANAGERS);
  }

  @Override
  public ScoreValue calculate(Value<?>... values) {
    Value<Boolean> usesNoHttp = findValue(values, USES_NOHTTP,
        "Hey! You have to tell me if the project uses nohttp tool!");
    Value<PackageManagers> packageManagers = findValue(values, PACKAGE_MANAGERS,
        "Hey! You have to tell me which package managers the project uses!");

    ScoreValue scoreValue = scoreValue(MIN, usesNoHttp, packageManagers);

    if (allUnknown(scoreValue.usedValues())) {
      return scoreValue.makeUnknown();
    }

    // if nohttp is used, then return the max score value, and the min value otherwise
    if (!usesNoHttp.isUnknown()) {
      return usesNoHttp.get() ? scoreValue.set(MAX) : scoreValue;
    }

    // now we don't know if nohttp is used or not
    // if we don't know anything about package managers, then return the min value
    if (packageManagers.isUnknown()) {
      return scoreValue;
    }

    // the can be integrated with Maven or Gradle
    // return N/A if other package managers are used
    if (!packageManagers.get().containsAny(MAVEN, GRADLE)) {
      return scoreValue.makeNotApplicable();
    }

    // otherwise, return the min value
    return scoreValue;
  }
}
