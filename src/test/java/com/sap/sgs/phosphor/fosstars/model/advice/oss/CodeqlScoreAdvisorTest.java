package com.sap.sgs.phosphor.fosstars.model.advice.oss;

import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.USES_LGTM_CHECKS;
import static com.sap.sgs.phosphor.fosstars.model.other.Utils.allUnknown;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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
    CodeqlScoreAdvisor advisor = new CodeqlScoreAdvisor();
    GitHubProject project = new GitHubProject("org", "test");

    // no advices if no rating value is set
    assertTrue(advisor.adviseFor(project).isEmpty());

    Rating rating = RatingRepository.INSTANCE.rating(OssSecurityRating.class);
    ValueSet values = new ValueHashSet();

    // no advices for an unknown value
    values.update(allUnknown(rating.score().allFeatures()));
    assertTrue(advisor.adviseFor(project).isEmpty());

    // no advices if the LGTM checks are enabled
    values.update(USES_LGTM_CHECKS.value(true));
    project.set(rating.calculate(values));
    assertTrue(advisor.adviseFor(project).isEmpty());

    // expect an advice if the LGTM checks are not enabled
    values.update(USES_LGTM_CHECKS.value(false));
    project.set(rating.calculate(values));
    assertFalse(advisor.adviseFor(project).isEmpty());
  }
}