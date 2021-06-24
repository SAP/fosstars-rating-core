package com.sap.oss.phosphor.fosstars.tool;

import static com.sap.oss.phosphor.fosstars.tool.GitHubProjectFinder.EMPTY_EXCLUDE_LIST;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import com.sap.oss.phosphor.fosstars.tool.GitHubProjectFinder.OrganizationConfig;
import com.sap.oss.phosphor.fosstars.tool.GitHubProjectFinder.ProjectConfig;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import org.junit.Test;

public class ApplicationTest {

  @Test(expected = IllegalArgumentException.class)
  public void testNoParameters() throws Exception {
    new Application().run();
  }

  @Test
  public void testHelp() throws Exception {
    new Application().run("-help");
    new Application().run("-h");
  }

  @Test
  public void testLoadConfig() throws IOException {
    final String filename = "ValidSecurityRatingCalculatorConfig.yml";
    try (InputStream is = getClass().getResourceAsStream(filename)) {
      Config mainConfig = Config.from(is);

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
              new OrganizationConfig("apache", Arrays.asList("incubator", "incubating"), 0)));
      assertThat(
          mainConfig.finderConfig.organizationConfigs,
          hasItem(
              new OrganizationConfig("eclipse", Collections.singletonList("incubator"), 0)));
      assertThat(
          mainConfig.finderConfig.organizationConfigs,
          hasItem(
              new OrganizationConfig("spring-projects", EMPTY_EXCLUDE_LIST, 0)));
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