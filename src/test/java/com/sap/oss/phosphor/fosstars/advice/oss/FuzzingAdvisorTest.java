package com.sap.oss.phosphor.fosstars.advice.oss;

import static com.sap.oss.phosphor.fosstars.advice.oss.AbstractOssAdvisor.OssAdviceContextFactory.WITH_EMPTY_CONTEXT;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.FUZZED_IN_OSS_FUZZ;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.LANGUAGES;
import static com.sap.oss.phosphor.fosstars.model.other.Utils.allUnknown;
import static com.sap.oss.phosphor.fosstars.model.value.Language.C;
import static com.sap.oss.phosphor.fosstars.model.value.Language.JAVA;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.sap.oss.phosphor.fosstars.model.Rating;
import com.sap.oss.phosphor.fosstars.model.RatingRepository;
import com.sap.oss.phosphor.fosstars.model.ValueSet;
import com.sap.oss.phosphor.fosstars.model.rating.oss.OssSecurityRating;
import com.sap.oss.phosphor.fosstars.model.subject.oss.GitHubProject;
import com.sap.oss.phosphor.fosstars.model.value.Languages;
import com.sap.oss.phosphor.fosstars.model.value.ValueHashSet;
import java.net.MalformedURLException;
import org.junit.Test;

public class FuzzingAdvisorTest {

  @Test
  public void testAdviseForOssFuzz() throws MalformedURLException {
    FuzzingAdvisor advisor = new FuzzingAdvisor(WITH_EMPTY_CONTEXT);
    GitHubProject project = new GitHubProject("org", "test");

    // no advice if no rating value is set
    assertTrue(advisor.adviceFor(project).isEmpty());

    Rating rating = RatingRepository.INSTANCE.rating(OssSecurityRating.class);
    ValueSet values = new ValueHashSet();

    // no advice for an unknown values
    values.update(allUnknown(rating.score().allFeatures()));
    assertTrue(advisor.adviceFor(project).isEmpty());

    // no advice if the project is fuzzed in OSS-Fuzz
    values.update(FUZZED_IN_OSS_FUZZ.value(true));
    values.update(LANGUAGES.value(Languages.of(C)));
    project.set(rating.calculate(values));
    assertTrue(advisor.adviceFor(project).isEmpty());

    // expect an advice if the project is not fuzzed in OSS-Fuzz
    values.update(FUZZED_IN_OSS_FUZZ.value(false));
    project.set(rating.calculate(values));
    assertEquals(1, advisor.adviceFor(project).size());
  }

  @Test
  public void testAdviceWhenFuzzingScoreIsNotApplicable() throws MalformedURLException {
    final FuzzingAdvisor advisor = new FuzzingAdvisor(WITH_EMPTY_CONTEXT);
    final GitHubProject project = new GitHubProject("org", "test");

    Rating rating = RatingRepository.INSTANCE.rating(OssSecurityRating.class);
    ValueSet values = new ValueHashSet();
    values.update(allUnknown(rating.score().allFeatures()));
    values.update(FUZZED_IN_OSS_FUZZ.value(false));
    values.update(LANGUAGES.value(Languages.of(JAVA)));
    project.set(rating.calculate(values));

    // fuzzing score is not applicable for projects that use only Java
    // therefore no advice are expected.
    assertTrue(advisor.adviceFor(project).isEmpty());
  }
}