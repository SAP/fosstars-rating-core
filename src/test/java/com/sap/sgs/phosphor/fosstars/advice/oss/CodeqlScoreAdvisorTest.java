package com.sap.sgs.phosphor.fosstars.advice.oss;

import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.RUNS_CODEQL_SCANS;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.USES_CODEQL_CHECKS;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.USES_LGTM_CHECKS;
import static com.sap.sgs.phosphor.fosstars.model.other.Utils.allUnknown;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.sap.sgs.phosphor.fosstars.advice.oss.AbstractOssScoreAdvisor.ContextFactory;
import com.sap.sgs.phosphor.fosstars.model.Rating;
import com.sap.sgs.phosphor.fosstars.model.RatingRepository;
import com.sap.sgs.phosphor.fosstars.model.ValueSet;
import com.sap.sgs.phosphor.fosstars.model.rating.oss.OssSecurityRating;
import com.sap.sgs.phosphor.fosstars.model.subject.oss.GitHubProject;
import com.sap.sgs.phosphor.fosstars.model.value.ValueHashSet;
import org.junit.Test;

public class CodeqlScoreAdvisorTest {

  @Test
  public void testBasics() {
    CodeqlScoreAdvisor advisor = new CodeqlScoreAdvisor(ContextFactory.WITH_EMPTY_CONTEXT);
    GitHubProject project = new GitHubProject("org", "test");

    // no advices if no rating value is set
    assertTrue(advisor.adviseFor(project).isEmpty());

    Rating rating = RatingRepository.INSTANCE.rating(OssSecurityRating.class);
    ValueSet values = new ValueHashSet();

    // no advices for an unknown values
    values.update(allUnknown(rating.score().allFeatures()));
    assertTrue(advisor.adviseFor(project).isEmpty());

    // no advices if the LGTM checks are enabled
    values.update(USES_LGTM_CHECKS.value(true));
    project.set(rating.calculate(values));
    assertTrue(advisor.adviseFor(project).isEmpty());

    // expect an advice if the LGTM checks are not enabled
    values.update(USES_LGTM_CHECKS.value(false));
    project.set(rating.calculate(values));
    assertEquals(1, advisor.adviseFor(project).size());

    // expect an advice if the  checks are not enabled
    values.update(USES_CODEQL_CHECKS.value(false));
    project.set(rating.calculate(values));
    assertEquals(2, advisor.adviseFor(project).size());

    // expect an advice if the LGTM checks are not enabled
    values.update(RUNS_CODEQL_SCANS.value(false));
    project.set(rating.calculate(values));
    assertEquals(3, advisor.adviseFor(project).size());
  }
}