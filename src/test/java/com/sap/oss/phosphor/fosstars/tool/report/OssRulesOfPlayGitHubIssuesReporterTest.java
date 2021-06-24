package com.sap.oss.phosphor.fosstars.tool.report;

import static junit.framework.TestCase.assertEquals;

import com.sap.oss.phosphor.fosstars.advice.oss.OssRulesOfPlayAdvisor;
import com.sap.oss.phosphor.fosstars.data.github.TestGitHubDataFetcherHolder;
import com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures;
import com.sap.oss.phosphor.fosstars.tool.format.OssRulesOfPlayRatingMarkdownFormatter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

public class OssRulesOfPlayGitHubIssuesReporterTest extends TestGitHubDataFetcherHolder {

  private static final Path CONFIG_PATH
      = Paths.get("OssRulesOfPlayRatingMarkdownFormatter.config.yml");

  private static final String RULE_IDS =
      "---\n"
          + "ruleIds:\n"
          + "  rl-license_file-1: If a project has a license\n"
          + "  rl-license_file-2: If a project uses an allowed license\n"
          + "  rl-license_file-3: If a license has disallowed text\n"
          + "  rl-readme_file-1: If a project has a README file\n"
          + "documentationUrl: https://wiki.local/TestPage";

  @Test
  public void testPrintTitleAndBody() throws IOException {
    Files.write(CONFIG_PATH, RULE_IDS.getBytes());
    try {
      OssRulesOfPlayGitHubIssuesReporter reporter = new OssRulesOfPlayGitHubIssuesReporter(
          fetcher, new OssRulesOfPlayRatingMarkdownFormatter(new OssRulesOfPlayAdvisor()));

      String printedTitle = reporter.printTitle(OssFeatures.HAS_LICENSE.value(false));
      assertEquals("[rl-license_file-1] Violation against OSS Rules of Play", printedTitle);

      String stringBuffer = "A violation against the OSS Rules of Play has been detected.\n\n"
          + "Rule ID: rl-license_file-2\n"
          + "Explanation: Does it use an allowed license? **No**\n\n"
          + "Find more information at: https://wiki.local/TestPage";
      assertEquals(stringBuffer, reporter.printBody(OssFeatures.ALLOWED_LICENSE.value(false)));
    } finally {
      FileUtils.forceDeleteOnExit(CONFIG_PATH.toFile());
    }
  }

}