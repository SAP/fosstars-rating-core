package com.sap.sgs.phosphor.fosstars.advice.oss;

import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.WORST_LGTM_GRADE;
import static com.sap.sgs.phosphor.fosstars.model.other.Utils.allUnknown;
import static com.sap.sgs.phosphor.fosstars.model.value.LgtmGrade.A_PLUS;
import static com.sap.sgs.phosphor.fosstars.model.value.LgtmGrade.B;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.sap.sgs.phosphor.fosstars.advice.Advice;
import com.sap.sgs.phosphor.fosstars.advice.oss.AbstractOssScoreAdvisor.ContextFactory;
import com.sap.sgs.phosphor.fosstars.model.Rating;
import com.sap.sgs.phosphor.fosstars.model.RatingRepository;
import com.sap.sgs.phosphor.fosstars.model.ValueSet;
import com.sap.sgs.phosphor.fosstars.model.rating.oss.OssSecurityRating;
import com.sap.sgs.phosphor.fosstars.model.subject.oss.GitHubProject;
import com.sap.sgs.phosphor.fosstars.model.value.ValueHashSet;
import java.util.List;
import org.junit.Test;

public class LgtmScoreAdvisorTest {

  @Test
  public void testAdvicesForLgtmGrade() {
    LgtmScoreAdvisor advisor = new LgtmScoreAdvisor(ContextFactory.WITH_EMPTY_CONTEXT);
    GitHubProject project = new GitHubProject("org", "test");

    // no advices if no rating value is set
    assertTrue(advisor.adviseFor(project).isEmpty());

    Rating rating = RatingRepository.INSTANCE.rating(OssSecurityRating.class);
    ValueSet values = new ValueHashSet();

    // no advices for an unknown values
    values.update(allUnknown(rating.score().allFeatures()));
    assertTrue(advisor.adviseFor(project).isEmpty());

    // no advices if the LGTM grade is the best
    values.update(WORST_LGTM_GRADE.value(A_PLUS));
    project.set(rating.calculate(values));
    assertTrue(advisor.adviseFor(project).isEmpty());

    // expect an advice if the LGTM grade is not the best
    values.update(WORST_LGTM_GRADE.value(B));
    project.set(rating.calculate(values));
    List<Advice> advices = advisor.adviseFor(project);
    assertEquals(1, advices.size());
    Advice advice = advices.get(0);
    assertFalse(advice.content().text().isEmpty());
    assertTrue(advice.content().links().isEmpty());
  }
}