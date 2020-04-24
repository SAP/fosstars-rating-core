package com.sap.sgs.phosphor.fosstars.tool.github;

import static com.sap.sgs.phosphor.fosstars.tool.github.GitHubProjectFinder.EMPTY_EXCLUDE_LIST;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import com.sap.sgs.phosphor.fosstars.tool.github.GitHubProjectFinder.OrganizationConfig;
import com.sap.sgs.phosphor.fosstars.tool.github.GitHubProjectFinder.ProjectConfig;
import com.sap.sgs.phosphor.fosstars.tool.github.SecurityRatingCalculator.ReportConfig;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import org.junit.Test;

public class SecurityRatingCalculatorTest {

  @Test(expected = IllegalArgumentException.class)
  public void noParameters() throws IOException {
    SecurityRatingCalculator.run();
  }

  @Test
  public void help() throws IOException {
    SecurityRatingCalculator.run("-help");
    SecurityRatingCalculator.run("-h");
  }

  @Test
  public void loadConfig() throws IOException {
    final String filename = "ValidSecurityRatingCalculatorConfig.yml";
    try (InputStream is = getClass().getResourceAsStream(filename)) {
      SecurityRatingCalculator.Config mainConfig = SecurityRatingCalculator.config(is);

      assertEquals(".fosstars/project_rating_cache.json", mainConfig.cacheFilename);

      assertNotNull(mainConfig.reportConfigs);
      assertEquals(2, mainConfig.reportConfigs.size());

      for (ReportConfig reportConfig : mainConfig.reportConfigs) {
        assertNotNull(reportConfig.type);
        switch (reportConfig.type) {
          case MARKDOWN:
            assertEquals(".fosstars/report", reportConfig.where);
            assertEquals(".fosstars/report/github_projects.json", reportConfig.source);
            break;
          case JSON:
            assertEquals(".fosstars/report/github_projects.json", reportConfig.where);
            break;
          default:
            fail("Unexpected report type: " + reportConfig.type);
        }
      }

      assertNotNull(mainConfig.finderConfig);
      assertNotNull(mainConfig.finderConfig.organizationConfigs);
      assertEquals(3, mainConfig.finderConfig.organizationConfigs.size());
      assertThat(
          mainConfig.finderConfig.organizationConfigs,
          hasItem(
              new OrganizationConfig("apache", Arrays.asList("incubator", "incubating"))));
      assertThat(
          mainConfig.finderConfig.organizationConfigs,
          hasItem(
              new OrganizationConfig("eclipse", Collections.singletonList("incubator"))));
      assertThat(
          mainConfig.finderConfig.organizationConfigs,
          hasItem(
              new OrganizationConfig("spring-projects", EMPTY_EXCLUDE_LIST)));
      assertNotNull(mainConfig.finderConfig.projectConfigs);
      assertEquals(2, mainConfig.finderConfig.projectConfigs.size());
      assertThat(
          mainConfig.finderConfig.projectConfigs,
          hasItem(
              new ProjectConfig("FasterXML", "jackson-databind")));
      assertThat(
          mainConfig.finderConfig.projectConfigs,
          hasItem(
              new ProjectConfig("FasterXML", "jackson-dataformat-xml")));
    }
  }

}