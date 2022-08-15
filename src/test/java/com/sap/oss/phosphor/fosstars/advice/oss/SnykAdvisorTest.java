package com.sap.oss.phosphor.fosstars.advice.oss;

import static com.sap.oss.phosphor.fosstars.advice.oss.AbstractOssAdvisor.OssAdviceContextFactory.WITH_EMPTY_CONTEXT;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.LANGUAGES;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.PACKAGE_MANAGERS;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_GITHUB_FOR_DEVELOPMENT;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_SNYK;
import static com.sap.oss.phosphor.fosstars.model.other.Utils.allUnknown;
import static com.sap.oss.phosphor.fosstars.model.value.Language.C;
import static com.sap.oss.phosphor.fosstars.model.value.Language.GO;
import static com.sap.oss.phosphor.fosstars.model.value.PackageManager.GOMODULES;
import static com.sap.oss.phosphor.fosstars.model.value.PackageManager.OTHER;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.sap.oss.phosphor.fosstars.model.Rating;
import com.sap.oss.phosphor.fosstars.model.RatingRepository;
import com.sap.oss.phosphor.fosstars.model.ValueSet;
import com.sap.oss.phosphor.fosstars.model.rating.oss.OssSecurityRating;
import com.sap.oss.phosphor.fosstars.model.subject.oss.GitHubProject;
import com.sap.oss.phosphor.fosstars.model.value.Languages;
import com.sap.oss.phosphor.fosstars.model.value.PackageManagers;
import com.sap.oss.phosphor.fosstars.model.value.ValueHashSet;
import java.net.MalformedURLException;
import org.junit.Test;

public class SnykAdvisorTest {

  @Test
  public void testAdviseForSnyk() throws MalformedURLException {
    final SnykAdvisor advisor = new SnykAdvisor(WITH_EMPTY_CONTEXT);
    GitHubProject project = new GitHubProject("org", "test");

    // no advice if no rating value is set
    assertTrue(advisor.adviceFor(project).isEmpty());

    Rating rating = RatingRepository.INSTANCE.rating(OssSecurityRating.class);
    ValueSet values = new ValueHashSet();

    // no advice for an unknown values
    values.update(allUnknown(rating.score().allFeatures()));
    values.update(LANGUAGES.value(Languages.of(GO)));
    values.update(PACKAGE_MANAGERS.value(PackageManagers.from(GOMODULES)));
    values.update(USES_GITHUB_FOR_DEVELOPMENT.value(true));
    assertTrue(advisor.adviceFor(project).isEmpty());

    values.update(USES_SNYK.value(true));
    project.set(rating.calculate(values));
    assertTrue(advisor.adviceFor(project).isEmpty());

    values.update(USES_SNYK.value(false));
    project.set(rating.calculate(values));
    assertEquals(1, advisor.adviceFor(project).size());
    assertEquals("You can create Snyk account and configure your project.",
        advisor.adviceFor(project).get(0).content().text());
  }

  @Test
  public void testAdviceWhenSnykScoreIsNotApplicable() throws MalformedURLException {
    final SnykAdvisor advisor = new SnykAdvisor(WITH_EMPTY_CONTEXT);
    final GitHubProject project = new GitHubProject("org", "test");

    Rating rating = RatingRepository.INSTANCE.rating(OssSecurityRating.class);
    ValueSet values = new ValueHashSet();
    values.update(allUnknown(rating.score().allFeatures()));
    values.update(USES_SNYK.value(false));
    values.update(USES_GITHUB_FOR_DEVELOPMENT.value(true));
    values.update(LANGUAGES.value(Languages.of(C)));
    values.update(PACKAGE_MANAGERS.value(PackageManagers.from(OTHER)));
    project.set(rating.calculate(values));
    assertTrue(advisor.adviceFor(project).isEmpty());
  }
}
