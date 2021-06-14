package com.sap.oss.phosphor.fosstars.tool.report;

import static com.sap.oss.phosphor.fosstars.model.rating.oss.OssSecurityRating.SecurityLabel.BAD;
import static com.sap.oss.phosphor.fosstars.model.rating.oss.OssSecurityRating.SecurityLabel.GOOD;
import static com.sap.oss.phosphor.fosstars.model.rating.oss.OssSecurityRating.SecurityLabel.MODERATE;
import static com.sap.oss.phosphor.fosstars.model.rating.oss.OssSecurityRating.SecurityLabel.UNCLEAR;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.sap.oss.phosphor.fosstars.advice.oss.github.OssSecurityGithubAdvisor;
import com.sap.oss.phosphor.fosstars.model.Label;
import com.sap.oss.phosphor.fosstars.model.RatingRepository;
import com.sap.oss.phosphor.fosstars.model.Score;
import com.sap.oss.phosphor.fosstars.model.rating.oss.OssSecurityRating;
import com.sap.oss.phosphor.fosstars.model.subject.oss.GitHubProject;
import com.sap.oss.phosphor.fosstars.model.value.RatingValue;
import com.sap.oss.phosphor.fosstars.model.value.ScoreValue;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

public class OssSecurityRatingMarkdownReporterTest {

  @Test
  public void testReport() throws IOException {
    Path outputDirectory = Files.createTempDirectory(
        OssSecurityRatingMarkdownReporterTest.class.getName());
    try {
      OssSecurityRating rating = RatingRepository.INSTANCE.rating(OssSecurityRating.class);

      GitHubProject goodProject = new GitHubProject("org", "good");
      goodProject.set(
          new RatingValue(
              new ScoreValue(rating.score()).set(Score.MAX).confidence(10.0),
              GOOD));

      GitHubProject moderateProject = new GitHubProject("org", "moderate");
      moderateProject.set(
          new RatingValue(
              new ScoreValue(rating.score()).set(5.0).confidence(9.0),
              MODERATE));

      GitHubProject badProject = new GitHubProject("org", "bad");
      badProject.set(
          new RatingValue(
              new ScoreValue(rating.score()).set(Score.MIN).confidence(8.0),
              BAD));

      GitHubProject projectWithLowConfidence = new GitHubProject("org", "unclear");
      projectWithLowConfidence.set(
          new RatingValue(
              new ScoreValue(rating.score()).set(Score.MIN).confidence(1.0),
              UNCLEAR));

      List<GitHubProject> projects = Arrays.asList(
          goodProject, moderateProject, badProject, projectWithLowConfidence
      );

      OssSecurityRatingMarkdownReporter reporter = new OssSecurityRatingMarkdownReporter(
          outputDirectory.toString(), null, rating, new OssSecurityGithubAdvisor());
      reporter.runFor(projects);

      Path reportFileName = outputDirectory.resolve(
          OssSecurityRatingMarkdownReporter.REPORT_FILENAME);
      assertTrue(Files.exists(reportFileName));

      String report = new String(Files.readAllBytes(reportFileName));
      System.out.println(report);

      assertFalse(report.isEmpty());
      assertTrue(report.contains("Total"));
      for (Label label : OssSecurityRating.SecurityLabel.values()) {
        assertTrue(report.contains(label.name()));
      }
      assertTrue(report.contains(OssSecurityRatingMarkdownReporter.UNKNOWN));
      assertTrue(report.contains("org/good"));
      assertTrue(report.contains("org/bad"));
      assertTrue(report.contains("org/moderate"));
      assertTrue(report.contains("10.0"));
      assertTrue(report.contains("0.0"));
      assertTrue(report.contains("5.0"));
      assertEquals(1, linesWith("100%", report));
      assertEquals(4, linesWith("25.0%", report));
      assertEquals(1, linesWith("0.0%", report));
    } finally {
      FileUtils.forceDeleteOnExit(outputDirectory.toFile());
    }
  }

  @Test
  public void testInsert() {
    assertEquals(
        "aaa<br>bbb<br>ccc",
        OssSecurityRatingMarkdownReporter.insert("<br>", 3, "aaabbbccc"));
    assertEquals(
        "aaa<br>bbb<br>ccc<br>dd",
        OssSecurityRatingMarkdownReporter.insert("<br>", 3, "aaabbbcccdd"));
    assertEquals(
        "aaa",
        OssSecurityRatingMarkdownReporter.insert("<br>", 3, "aaa"));
  }

  @Test
  public void testCreatingReportDirectory() throws IOException {
    Path baseDirectory = Files.createTempDirectory(
        OssSecurityRatingMarkdownReporterTest.class.getName());
    Path outputDirectory = baseDirectory.resolve("one").resolve("two");
    if (Files.exists(outputDirectory)) {
      fail("Report directory already exists. Please fix the test or its environment");
    }
    try {
      OssSecurityRatingMarkdownReporter reporter = new OssSecurityRatingMarkdownReporter(
          outputDirectory.toString(),
          null,
          RatingRepository.INSTANCE.rating(OssSecurityRating.class),
          new OssSecurityGithubAdvisor());
      reporter.runFor(Collections.emptyList());
    } finally {
      FileUtils.forceDeleteOnExit(baseDirectory.toFile());
    }
  }

  private static int linesWith(String string, String content) throws IOException {
    BufferedReader reader = new BufferedReader(new StringReader(content));

    String line;
    int n = 0;
    while ((line = reader.readLine()) != null) {
      if (line.contains(string)) {
        n++;
      }
    }

    return n;
  }
}