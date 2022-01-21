package com.sap.oss.phosphor.fosstars.advice.oss;

import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.RUNS_CODEQL_SCANS;

import com.sap.oss.phosphor.fosstars.advice.Advice;
import com.sap.oss.phosphor.fosstars.advice.oss.OssAdviceContentYamlStorage.OssAdviceContext;
import com.sap.oss.phosphor.fosstars.model.Subject;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.score.oss.DependabotScore;
import com.sap.oss.phosphor.fosstars.model.value.ScoreValue;
import java.net.MalformedURLException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * An advisor for features related to Dependabot.
 */

public class CodeScanAdvisor extends AbstractOssAdvisor {

  /**
   * Create a new advisor.
   *
   * @param contextFactory A factory that provides contexts for advice.
   */
  public CodeScanAdvisor(OssAdviceContextFactory contextFactory) {
    super(OssAdviceContentYamlStorage.DEFAULT, contextFactory);
  }

  @Override
  protected List<Advice> adviceFor(
      Subject subject, List<Value<?>> usedValues, OssAdviceContext context)
      throws MalformedURLException {

    Optional<ScoreValue> dependabotScore = findSubScoreValue(subject, DependabotScore.class);

    if (!dependabotScore.isPresent() || dependabotScore.get().isNotApplicable()) {
      return Collections.emptyList();
    }

    return adviceForBooleanFeature(usedValues, RUNS_CODEQL_SCANS, subject, context);
  }
}
