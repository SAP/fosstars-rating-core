package com.sap.oss.phosphor.fosstars.advice.oss;

import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.RUNS_CODEQL_SCANS;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_CODEQL_CHECKS;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_LGTM_CHECKS;

import com.sap.oss.phosphor.fosstars.advice.Advice;
import com.sap.oss.phosphor.fosstars.advice.oss.OssAdviceContentYamlStorage.OssAdviceContext;
import com.sap.oss.phosphor.fosstars.model.Subject;
import com.sap.oss.phosphor.fosstars.model.Value;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * An advisor for features related to CodeQL.
 */
public class CodeqlAdvisor extends AbstractOssAdvisor {

  /**
   * Create a new advisor.
   *
   * @param contextFactory A factory that provides contexts for advice.
   */
  public CodeqlAdvisor(OssAdviceContextFactory contextFactory) {
    super(OssAdviceContentYamlStorage.DEFAULT, contextFactory);
  }

  @Override
  protected List<Advice> adviseFor(
      Subject subject, List<Value<?>> usedValues, OssAdviceContext context) {

    return Stream.of(USES_LGTM_CHECKS, USES_CODEQL_CHECKS, RUNS_CODEQL_SCANS)
        .map(feature -> adviseForBooleanFeature(usedValues, feature, subject, context))
        .collect(ArrayList::new, ArrayList::addAll, ArrayList::addAll);
  }
}
