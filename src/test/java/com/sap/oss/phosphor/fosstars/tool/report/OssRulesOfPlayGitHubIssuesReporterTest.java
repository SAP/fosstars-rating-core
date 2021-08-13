package com.sap.oss.phosphor.fosstars.tool.report;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sap.oss.phosphor.fosstars.advice.oss.OssRulesOfPlayAdvisor;
import com.sap.oss.phosphor.fosstars.data.github.GitHubDataFetcher;
import com.sap.oss.phosphor.fosstars.data.github.TestGitHubDataFetcherHolder;
import com.sap.oss.phosphor.fosstars.model.RatingRepository;
import com.sap.oss.phosphor.fosstars.model.Score;
import com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures;
import com.sap.oss.phosphor.fosstars.model.rating.oss.OssRulesOfPlayRating;
import com.sap.oss.phosphor.fosstars.model.subject.oss.GitHubProject;
import com.sap.oss.phosphor.fosstars.model.value.BooleanValue;
import com.sap.oss.phosphor.fosstars.model.value.RatingValue;
import com.sap.oss.phosphor.fosstars.model.value.ScoreValue;
import com.sap.oss.phosphor.fosstars.tool.format.OssRulesOfPlayRatingMarkdownFormatter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.kohsuke.github.GHFileNotFoundException;
import org.kohsuke.github.GHIssue;
import org.kohsuke.github.GHRepository;

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

  @Test
  public void testIssuesDisabled() throws IOException {
    OssRulesOfPlayRating rating = RatingRepository.INSTANCE.rating(OssRulesOfPlayRating.class);
    GitHubProject project = new GitHubProject("test", "project");
    project.set(
        new RatingValue(
            new ScoreValue(rating.score()).set(Score.MIN).confidence(8.0).usedValues(
                new BooleanValue(OssFeatures.HAS_LICENSE, false)),
            OssRulesOfPlayRating.OssRulesOfPlayLabel.FAILED));

    GitHubDataFetcher ghDataFetcher = mock(GitHubDataFetcher.class);

    when(ghDataFetcher.gitHubIssuesFor(any(), any())).thenReturn(Lists.newArrayList());
    when(ghDataFetcher.createGitHubIssue(any(), any(), any())).thenThrow(
        GHFileNotFoundException.class);

    GHRepository repository = mock(GHRepository.class);
    when(repository.hasIssues()).thenReturn(false);
    when(ghDataFetcher.repositoryFor(any())).thenReturn(repository);

    OssRulesOfPlayGitHubIssuesReporter reporter = new OssRulesOfPlayGitHubIssuesReporter(
        ghDataFetcher,
        new OssRulesOfPlayRatingMarkdownFormatter(new OssRulesOfPlayAdvisor()));

    reporter.createIssuesFor(project);
    verify(repository, times(1)).hasIssues();
    verify(ghDataFetcher, times(0)).createGitHubIssue(any(), any(), any());
  }

  @Test
  public void testIssuesEnabled() throws IOException {
    OssRulesOfPlayRating rating = RatingRepository.INSTANCE.rating(OssRulesOfPlayRating.class);
    GitHubProject project = new GitHubProject("test", "project");
    project.set(
        new RatingValue(
            new ScoreValue(rating.score()).set(Score.MIN).confidence(8.0).usedValues(
                new BooleanValue(OssFeatures.HAS_LICENSE, false),
                new BooleanValue(OssFeatures.HAS_README, false)),
            OssRulesOfPlayRating.OssRulesOfPlayLabel.FAILED));

    GitHubDataFetcher ghDataFetcher = mock(GitHubDataFetcher.class);

    when(ghDataFetcher.gitHubIssuesFor(any(), any())).thenReturn(Lists.newArrayList());
    when(ghDataFetcher.createGitHubIssue(any(), any(), any())).thenReturn(new GHIssue());

    GHRepository repository = mock(GHRepository.class);
    when(repository.hasIssues()).thenReturn(true);
    when(ghDataFetcher.repositoryFor(any())).thenReturn(repository);

    OssRulesOfPlayGitHubIssuesReporter reporter = new OssRulesOfPlayGitHubIssuesReporter(
        ghDataFetcher, new OssRulesOfPlayRatingMarkdownFormatter(new OssRulesOfPlayAdvisor()));

    reporter.createIssuesFor(project);
    verify(repository, times(1)).hasIssues();
    verify(ghDataFetcher, times(1)).gitHubIssuesFor(project, "[rl-license_file-1]");
    verify(ghDataFetcher, times(1)).gitHubIssuesFor(project, "[rl-readme_file-1]");
    verify(ghDataFetcher, times(2)).createGitHubIssue(any(), any(), any());
  }

}