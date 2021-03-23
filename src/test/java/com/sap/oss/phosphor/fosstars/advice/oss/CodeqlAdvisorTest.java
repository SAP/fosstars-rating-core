package com.sap.oss.phosphor.fosstars.advice.oss;

import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.RUNS_CODEQL_SCANS;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_CODEQL_CHECKS;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_LGTM_CHECKS;
import static com.sap.oss.phosphor.fosstars.model.other.Utils.allUnknown;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.sap.oss.phosphor.fosstars.advice.oss.AbstractOssAdvisor.OssAdviceContextFactory;
import com.sap.oss.phosphor.fosstars.model.Rating;
import com.sap.oss.phosphor.fosstars.model.RatingRepository;
import com.sap.oss.phosphor.fosstars.model.ValueSet;
import com.sap.oss.phosphor.fosstars.model.rating.oss.OssSecurityRating;
import com.sap.oss.phosphor.fosstars.model.subject.oss.GitHubProject;
import com.sap.oss.phosphor.fosstars.model.value.ValueHashSet;
import java.net.MalformedURLException;
import org.junit.Test;

public class CodeqlAdvisorTest {

  @Test
  public void testAdviseForCodeQl() throws MalformedURLException {
    CodeqlAdvisor advisor = new CodeqlAdvisor(OssAdviceContextFactory.WITH_EMPTY_CONTEXT);
    GitHubProject project = new GitHubProject("org", "test");

    // no advice if no rating value is set
    assertTrue(advisor.adviceFor(project).isEmpty());

    Rating rating = RatingRepository.INSTANCE.rating(OssSecurityRating.class);
    ValueSet values = new ValueHashSet();

    // no advice for an unknown values
    values.update(allUnknown(rating.score().allFeatures()));
    assertTrue(advisor.adviceFor(project).isEmpty());

    // no advice if the LGTM checks are enabled
    values.update(USES_LGTM_CHECKS.value(true));
    project.set(rating.calculate(values));
    assertTrue(advisor.adviceFor(project).isEmpty());

    // expect an advice if the LGTM checks are not enabled
    values.update(USES_LGTM_CHECKS.value(false));
    project.set(rating.calculate(values));
    assertEquals(1, advisor.adviceFor(project).size());

    // expect an advice if the  checks are not enabled
    values.update(USES_CODEQL_CHECKS.value(false));
    project.set(rating.calculate(values));
    assertEquals(2, advisor.adviceFor(project).size());

    // expect an advice if the LGTM checks are not enabled
    values.update(RUNS_CODEQL_SCANS.value(false));
    project.set(rating.calculate(values));
    assertEquals(3, advisor.adviceFor(project).size());
  }
}