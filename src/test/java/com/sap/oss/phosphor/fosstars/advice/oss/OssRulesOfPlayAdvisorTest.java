package com.sap.oss.phosphor.fosstars.advice.oss;

import static com.sap.oss.phosphor.fosstars.advice.oss.AbstractOssAdvisor.OssAdviceContextFactory.WITH_EMPTY_CONTEXT;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.HAS_LICENSE;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.IS_REUSE_COMPLIANT;
import static com.sap.oss.phosphor.fosstars.model.other.Utils.allUnknown;
import static com.sap.oss.phosphor.fosstars.model.score.oss.OssRulesOfPlayScoreTest.allRulesPassed;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.sap.oss.phosphor.fosstars.model.RatingRepository;
import com.sap.oss.phosphor.fosstars.model.ValueSet;
import com.sap.oss.phosphor.fosstars.model.rating.oss.OssRulesOfPlayRating;
import com.sap.oss.phosphor.fosstars.model.subject.oss.GitHubProject;
import com.sap.oss.phosphor.fosstars.model.value.ValueHashSet;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

public class OssRulesOfPlayAdvisorTest {

  @Test
  public void testAdvice() throws IOException {
    Path path = Files.createTempFile(OssRulesOfPlayAdvisorTest.class.getName(), "test");
    String content =
          "---\n"
        + "If a project is compliant with REUSE rules:\n"
        + "  - advice: Text\n"
        + "    links:\n"
        + "      - name: Test link\n"
        + "        url: https://test.com/path\n"
        + "If a project has a license:\n"
        + "  - advice: Add a license";
    Files.write(path, content.getBytes());
    try {
      OssRulesOfPlayAdvisor advisor
          = new OssRulesOfPlayAdvisor(path.toString(), WITH_EMPTY_CONTEXT);
      GitHubProject project = new GitHubProject("org", "test");

      // no advice if no rating value is set
      assertTrue(advisor.adviceFor(project).isEmpty());

      OssRulesOfPlayRating rating = RatingRepository.INSTANCE.rating(OssRulesOfPlayRating.class);
      ValueSet values = new ValueHashSet();

      // no advice for unknown values
      values.update(allUnknown(rating.score().allFeatures()));
      assertTrue(advisor.adviceFor(project).isEmpty());

      // no advice if all checks are passed
      values.update(allRulesPassed());
      assertTrue(advisor.adviceFor(project).isEmpty());

      values.update(HAS_LICENSE.value(false));
      project.set(rating.calculate(values));
      assertEquals(1, advisor.adviceFor(project).size());

      values.update(IS_REUSE_COMPLIANT.value(false));
      project.set(rating.calculate(values));
      assertEquals(2, advisor.adviceFor(project).size());
    } finally {
      FileUtils.forceDeleteOnExit(path.toFile());
    }
  }
}