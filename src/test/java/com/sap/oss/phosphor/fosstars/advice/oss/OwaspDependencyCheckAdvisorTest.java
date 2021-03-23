package com.sap.oss.phosphor.fosstars.advice.oss;

import static com.sap.oss.phosphor.fosstars.advice.oss.AbstractOssAdvisor.OssAdviceContextFactory.WITH_EMPTY_CONTEXT;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.OWASP_DEPENDENCY_CHECK_FAIL_CVSS_THRESHOLD;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.OWASP_DEPENDENCY_CHECK_USAGE;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.PACKAGE_MANAGERS;
import static com.sap.oss.phosphor.fosstars.model.other.Utils.allUnknown;
import static com.sap.oss.phosphor.fosstars.model.value.OwaspDependencyCheckUsage.MANDATORY;
import static com.sap.oss.phosphor.fosstars.model.value.OwaspDependencyCheckUsage.NOT_USED;
import static com.sap.oss.phosphor.fosstars.model.value.PackageManager.MAVEN;
import static com.sap.oss.phosphor.fosstars.model.value.PackageManager.OTHER;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.sap.oss.phosphor.fosstars.model.Rating;
import com.sap.oss.phosphor.fosstars.model.RatingRepository;
import com.sap.oss.phosphor.fosstars.model.ValueSet;
import com.sap.oss.phosphor.fosstars.model.rating.oss.OssSecurityRating;
import com.sap.oss.phosphor.fosstars.model.subject.oss.GitHubProject;
import com.sap.oss.phosphor.fosstars.model.value.PackageManagers;
import com.sap.oss.phosphor.fosstars.model.value.ValueHashSet;
import java.net.MalformedURLException;
import org.junit.Test;

public class OwaspDependencyCheckAdvisorTest {

  @Test
  public void testAdviseForOwaspDependencyScan() throws MalformedURLException {
    OwaspDependencyCheckAdvisor advisor = new OwaspDependencyCheckAdvisor(WITH_EMPTY_CONTEXT);
    GitHubProject project = new GitHubProject("org", "test");

    // no advice if no rating value is set
    assertTrue(advisor.adviceFor(project).isEmpty());

    Rating rating = RatingRepository.INSTANCE.rating(OssSecurityRating.class);
    ValueSet values = new ValueHashSet();

    // no advice for an unknown values
    values.update(allUnknown(rating.score().allFeatures()));
    values.update(OWASP_DEPENDENCY_CHECK_USAGE.value(MANDATORY));
    values.update(OWASP_DEPENDENCY_CHECK_FAIL_CVSS_THRESHOLD.value(7.0));
    values.update(PACKAGE_MANAGERS.value(PackageManagers.from(MAVEN)));
    assertTrue(advisor.adviceFor(project).isEmpty());

    values.update(OWASP_DEPENDENCY_CHECK_FAIL_CVSS_THRESHOLD.notSpecifiedValue());
    project.set(rating.calculate(values));
    assertEquals(1, advisor.adviceFor(project).size());

    values.update(OWASP_DEPENDENCY_CHECK_USAGE.value(NOT_USED));
    project.set(rating.calculate(values));
    assertEquals(2, advisor.adviceFor(project).size());
  }

  @Test
  public void testAdviceWhenOwaspDependencyScanScoreIsNotApplicable() throws MalformedURLException {
    final OwaspDependencyCheckAdvisor advisor = new OwaspDependencyCheckAdvisor(WITH_EMPTY_CONTEXT);
    final GitHubProject project = new GitHubProject("org", "test");

    Rating rating = RatingRepository.INSTANCE.rating(OssSecurityRating.class);
    ValueSet values = new ValueHashSet();
    values.update(allUnknown(rating.score().allFeatures()));
    values.update(OWASP_DEPENDENCY_CHECK_USAGE.value(NOT_USED));
    values.update(OWASP_DEPENDENCY_CHECK_FAIL_CVSS_THRESHOLD.notSpecifiedValue());
    values.update(PACKAGE_MANAGERS.value(PackageManagers.from(OTHER)));
    project.set(rating.calculate(values));
    assertTrue(advisor.adviceFor(project).isEmpty());
  }
}