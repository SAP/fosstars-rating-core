package com.sap.sgs.phosphor.fosstars.tool.github;

import static com.sap.sgs.phosphor.fosstars.model.rating.oss.OssSecurityRating.SecurityLabel.BAD;
import static com.sap.sgs.phosphor.fosstars.model.rating.oss.OssSecurityRating.SecurityLabel.GOOD;
import static com.sap.sgs.phosphor.fosstars.model.rating.oss.OssSecurityRating.SecurityLabel.MODERATE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.sap.sgs.phosphor.fosstars.model.Rating;
import com.sap.sgs.phosphor.fosstars.model.RatingRepository;
import com.sap.sgs.phosphor.fosstars.model.Score;
import com.sap.sgs.phosphor.fosstars.model.rating.oss.OssSecurityRating;
import com.sap.sgs.phosphor.fosstars.model.value.RatingValue;
import com.sap.sgs.phosphor.fosstars.model.value.ScoreValue;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
import org.junit.Test;

public class MarkdownReporterTest {

  @Test
  public void testReport() throws IOException {
    Path outputDirectory = Files.createTempDirectory(MarkdownReporterTest.class.getName());
    try {
      Rating rating = RatingRepository.INSTANCE.rating(OssSecurityRating.class);

      GitHubProject goodProject = new GitHubProject("org", "good");
      goodProject.set(new RatingValue(new ScoreValue(rating.score()).set(Score.MAX), GOOD));

      GitHubProject moderateProject = new GitHubProject("org", "moderate");
      moderateProject.set(new RatingValue(new ScoreValue(rating.score()).set(5.0), MODERATE));

      GitHubProject badProject = new GitHubProject("org", "bad");
      badProject.set(new RatingValue(new ScoreValue(rating.score()).set(Score.MIN), BAD));

      List<GitHubProject> projects = Arrays.asList(
          goodProject, moderateProject, badProject
      );

      MarkdownReporter reporter = new MarkdownReporter(outputDirectory.toString(), null);
      reporter.runFor(projects);

      Path reportFileName = outputDirectory.resolve(MarkdownReporter.REPORT_FILENAME);
      assertTrue(Files.exists(reportFileName));

      String report = new String(Files.readAllBytes(reportFileName));

      System.out.println(report);

      assertFalse(report.isEmpty());
      assertTrue(report.contains("Total"));
      assertTrue(report.contains("GOOD"));
      assertTrue(report.contains("BAD"));
      assertTrue(report.contains("MODERATE"));
      assertTrue(report.contains("org/good"));
      assertTrue(report.contains("org/bad"));
      assertTrue(report.contains("org/moderate"));
      assertTrue(report.contains("10.0"));
      assertTrue(report.contains("0.00"));
      assertTrue(report.contains("5.00"));
      assertEquals(1, linesWith("100%", report));
      assertEquals(3, linesWith("33.3%", report));
      assertEquals(1, linesWith("0.0%", report));
    } finally {
      cleanup(outputDirectory);
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

  private static void cleanup(Path directory) throws IOException {

    // first, remove files
    try (Stream<Path> walk = Files.walk(directory)) {
      walk.filter(Files::isRegularFile)
          .forEach(path -> {
            try {
              Files.delete(path);
            } catch (IOException e) {
              // ignore
            }
          });
    }

    // then, remove directories
    try (Stream<Path> walk = Files.walk(directory)) {
      walk.filter(Files::isDirectory)
          .forEach(path -> {
            try {
              Files.delete(path);
            } catch (IOException e) {
              // ignore
            }
          });
    }

    // finally, remove the output directory
    Files.delete(directory);
  }
}