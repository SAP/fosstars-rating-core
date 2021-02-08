package com.sap.oss.phosphor.fosstars.model.score.oss;

import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.LANGUAGES;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_FIND_SEC_BUGS;
import static com.sap.oss.phosphor.fosstars.model.value.Language.JAVA;
import static java.lang.Boolean.FALSE;

import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.score.FeatureBasedScore;
import com.sap.oss.phosphor.fosstars.model.value.Languages;
import com.sap.oss.phosphor.fosstars.model.value.ScoreValue;

/**
 * <p>The score shows if an open-source project uses FindSecBugs to catch security issues.</p>
 *
 * <p>The score is based on the following features.</p>
 * <ul>
 *   <li>{@link com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures#LANGUAGES}</li>
 *   <li>{@link com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures#USES_FIND_SEC_BUGS}</li>
 * </ul>
 */
public class FindSecBugsScore extends FeatureBasedScore {

  /**
   * Initializes a new score.
   */
  FindSecBugsScore() {
    super("How a project uses FindSecBugs", LANGUAGES, USES_FIND_SEC_BUGS);
  }

  @Override
  public ScoreValue calculate(Value<?>... values) {
    Value<Languages> languages = find(LANGUAGES, values);
    Value<Boolean> usesFindSecBugs = find(USES_FIND_SEC_BUGS, values);

    ScoreValue scoreValue = scoreValue(MIN, languages, usesFindSecBugs);

    if (allUnknown(languages, usesFindSecBugs)) {
      return scoreValue.makeUnknown();
    }

    boolean applicable = languages.isUnknown() || languages.get().containsAnyOf(JAVA);
    if (!applicable) {
      return scoreValue.makeNotApplicable();
    }

    return usesFindSecBugs.orElse(FALSE) ? scoreValue.set(MAX) : scoreValue.set(MIN);
  }
}
