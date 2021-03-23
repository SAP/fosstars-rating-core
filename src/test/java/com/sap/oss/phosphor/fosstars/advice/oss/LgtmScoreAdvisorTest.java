package com.sap.oss.phosphor.fosstars.advice.oss;

import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.WORST_LGTM_GRADE;
import static com.sap.oss.phosphor.fosstars.model.other.Utils.allUnknown;
import static com.sap.oss.phosphor.fosstars.model.value.LgtmGrade.A_PLUS;
import static com.sap.oss.phosphor.fosstars.model.value.LgtmGrade.B;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.sap.oss.phosphor.fosstars.advice.Advice;
import com.sap.oss.phosphor.fosstars.advice.oss.OssAdviceContentYamlStorage.OssAdviceContext;
import com.sap.oss.phosphor.fosstars.model.Rating;
import com.sap.oss.phosphor.fosstars.model.RatingRepository;
import com.sap.oss.phosphor.fosstars.model.ValueSet;
import com.sap.oss.phosphor.fosstars.model.rating.oss.OssSecurityRating;
import com.sap.oss.phosphor.fosstars.model.subject.oss.GitHubProject;
import com.sap.oss.phosphor.fosstars.model.value.ValueHashSet;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Optional;
import org.junit.Test;

public class LgtmScoreAdvisorTest {

  @Test
  public void testAdviseForLgtmGrade() throws MalformedURLException {
    LgtmAdvisor advisor = new LgtmAdvisor(subject -> new OssAdviceContext() {

      @Override
      public Optional<String> lgtmProjectLink() {
        return Optional.of("https://lgtm.com");
      }

      @Override
      public Optional<String> suggestSecurityPolicyLink() {
        return Optional.of("https://github.com");
      }
    });
    GitHubProject project = new GitHubProject("org", "test");

    // no advice if no rating value is set
    assertTrue(advisor.adviceFor(project).isEmpty());

    Rating rating = RatingRepository.INSTANCE.rating(OssSecurityRating.class);
    ValueSet values = new ValueHashSet();

    // no advice for an unknown values
    values.update(allUnknown(rating.score().allFeatures()));
    assertTrue(advisor.adviceFor(project).isEmpty());

    // no advice if the LGTM grade is the best
    values.update(WORST_LGTM_GRADE.value(A_PLUS));
    project.set(rating.calculate(values));
    assertTrue(advisor.adviceFor(project).isEmpty());

    // expect an advice if the LGTM grade is not the best
    values.update(WORST_LGTM_GRADE.value(B));
    project.set(rating.calculate(values));
    List<Advice> adviceList = advisor.adviceFor(project);
    assertEquals(1, adviceList.size());
    Advice advice = adviceList.get(0);
    assertFalse(advice.content().text().isEmpty());
    assertFalse(advice.content().links().isEmpty());
  }
}