package com.sap.oss.phosphor.fosstars.advice.oss;

import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.LANGUAGES;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.RUNS_GOSEC_SCANS;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_GOSEC_SCAN_CHECKS;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_GOSEC_WITH_RULES;
import static com.sap.oss.phosphor.fosstars.model.other.Utils.allUnknown;
import static com.sap.oss.phosphor.fosstars.model.value.Language.GO;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.sap.oss.phosphor.fosstars.advice.oss.AbstractOssAdvisor.OssAdviceContextFactory;
import com.sap.oss.phosphor.fosstars.model.Rating;
import com.sap.oss.phosphor.fosstars.model.RatingRepository;
import com.sap.oss.phosphor.fosstars.model.ValueSet;
import com.sap.oss.phosphor.fosstars.model.rating.oss.OssSecurityRating;
import com.sap.oss.phosphor.fosstars.model.subject.oss.GitHubProject;
import com.sap.oss.phosphor.fosstars.model.value.Languages;
import com.sap.oss.phosphor.fosstars.model.value.ValueHashSet;
import java.net.MalformedURLException;
import org.junit.Test;

public class GoSecAdvisorTest {

  @Test
  public void testAdviseForGoSec() throws MalformedURLException {
    GoSecAdvisor advisor = new GoSecAdvisor(OssAdviceContextFactory.WITH_EMPTY_CONTEXT);
    GitHubProject project = new GitHubProject("org", "test");

    // no advice if no rating value is set
    assertTrue(advisor.adviceFor(project).isEmpty());

    Rating rating = RatingRepository.INSTANCE.rating(OssSecurityRating.class);
    ValueSet values = new ValueHashSet();

    // no advice for an unknown values
    values.update(allUnknown(rating.score().allFeatures()));
    assertTrue(advisor.adviceFor(project).isEmpty());

    values.update(LANGUAGES.value(Languages.of(GO)));
    // expect an advice if the checks are not enabled
    values.update(RUNS_GOSEC_SCANS.value(true));
    values.update(USES_GOSEC_SCAN_CHECKS.value(false));
    values.update(USES_GOSEC_WITH_RULES.value(false));
    project.set(rating.calculate(values));
    assertEquals(1, advisor.adviceFor(project).size());

    // expect an advice if all the checks and scans are not enabled
    values.update(RUNS_GOSEC_SCANS.value(false));
    values.update(USES_GOSEC_SCAN_CHECKS.value(false));
    values.update(USES_GOSEC_WITH_RULES.value(false));
    project.set(rating.calculate(values));
    assertEquals(1, advisor.adviceFor(project).size());

    // expect an advice if the scans are enabled with rules
    values.update(RUNS_GOSEC_SCANS.value(true));
    values.update(USES_GOSEC_SCAN_CHECKS.value(true));
    values.update(USES_GOSEC_WITH_RULES.value(true));
    project.set(rating.calculate(values));
    assertEquals(1, advisor.adviceFor(project).size());

  }
}