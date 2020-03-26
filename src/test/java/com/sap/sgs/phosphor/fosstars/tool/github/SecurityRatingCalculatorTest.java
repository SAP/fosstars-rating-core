package com.sap.sgs.phosphor.fosstars.tool.github;

import static com.sap.sgs.phosphor.fosstars.tool.github.GitHubProjectFinder.EMPTY_EXCLUDE_LIST;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import com.sap.sgs.phosphor.fosstars.tool.github.GitHubProjectFinder.OrganizationConfig;
import com.sap.sgs.phosphor.fosstars.tool.github.GitHubProjectFinder.ProjectConfig;
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
      GitHubProjectFinder.Config finderConfig = mainConfig.finderConfig;
      assertNotNull(finderConfig);
      assertNotNull(finderConfig);
      assertNotNull(finderConfig.organizationConfigs);
      assertEquals(3, finderConfig.organizationConfigs.size());
      assertThat(
          finderConfig.organizationConfigs,
          hasItem(
              new OrganizationConfig("apache", Arrays.asList("incubator", "incubating"))));
      assertThat(finderConfig.organizationConfigs,
          hasItem(
              new OrganizationConfig("eclipse", Collections.singletonList("incubator"))));
      assertThat(finderConfig.organizationConfigs,
          hasItem(
              new OrganizationConfig("spring-projects", EMPTY_EXCLUDE_LIST)));
      assertNotNull(finderConfig.projectConfigs);
      assertEquals(2, finderConfig.projectConfigs.size());
      assertThat(
          finderConfig.projectConfigs,
          hasItem(new ProjectConfig("FasterXML", "jackson-databind")));
      assertThat(
          finderConfig.projectConfigs,
          hasItem(new ProjectConfig("FasterXML", "jackson-dataformat-xml")));
    }
  }

}