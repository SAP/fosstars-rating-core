package com.sap.oss.phosphor.fosstars.model.score.oss;

import static org.junit.jupiter.api.Assertions.fail;

import com.sap.oss.phosphor.fosstars.ScoreCollector;
import com.sap.oss.phosphor.fosstars.model.RatingRepository;
import com.sap.oss.phosphor.fosstars.model.Score;
import com.sap.oss.phosphor.fosstars.model.qa.ScoreVerification;
import com.sap.oss.phosphor.fosstars.model.qa.TestVectors;
import com.sap.oss.phosphor.fosstars.model.qa.VerificationFailedException;
import com.sap.oss.phosphor.fosstars.model.rating.oss.OssSecurityRating;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;

public class ScoreVerificationTest {

  public static final String TEST_VECTORS_FILE_NAME_TEMPLATE_DEFAULT = "%sTestVectors.yml";

  public static final String TEST_VECTORS_FILE_NAME_TEMPLATE_TIME_DEPENDENT =
      "%sTestVectorsTimeDependent.yml";

  @Test
  public void testVerification() {

    //Scores which are based on dates or somewhat else time dependent and will fail in the future
    List<String> failingScores =
        List.of("VulnerabilityDiscoveryAndSecurityTestingScore", "SecurityReviewScore");

    ScoreCollector collector = new ScoreCollector();
    RatingRepository.INSTANCE.rating(OssSecurityRating.class).accept(collector);

    // add extra scores
    collector.scores().add(new VulnerabilityLifetimeScore());

    for (Score score : collector.scores()) {
      if (!failingScores.contains(score.getClass().getSimpleName())) {
        scoreVerification(score);
      }
    }
  }

  @Test
  public void testVulnerabilityDiscoveryAndSecurityTestingScoreTimeIndependent() {
    scoreVerification(
        new VulnerabilityDiscoveryAndSecurityTestingScore(new ProjectSecurityTestingScore()));
  }

  @Test
  public void testVulnerabilityDiscoveryAndSecurityTestingScoreTimeDependent() throws IOException {
    scoreVerificationWithTimeDependentValuesSubstituted(
        new VulnerabilityDiscoveryAndSecurityTestingScore(new ProjectSecurityTestingScore()));
  }

  @Test
  public void testSecurityReviewScoreTimeIndependent() {
    scoreVerification(new SecurityReviewScore());
  }

  @Test
  public void testSecurityReviewScoreTimeDependent() throws IOException {
    scoreVerificationWithTimeDependentValuesSubstituted(new SecurityReviewScore());
  }

  protected void scoreVerificationWithTimeDependentValuesSubstituted(Score score)
      throws IOException {
    String testVectorsFileName = String.format(TEST_VECTORS_FILE_NAME_TEMPLATE_TIME_DEPENDENT,
        score.getClass().getSimpleName());
    URL url = score.getClass().getResource(testVectorsFileName);
    File testYamlFile = FileUtils.getFile(url.getFile());
    String yamlString = FileUtils.readFileToString(testYamlFile, StandardCharsets.UTF_8);
    String output = substituteYearsAndDaysPlaceholder(yamlString);
    // Write the output to a temporary file
    Path tempFile = Files.createTempFile("", ".yml");
    Files.writeString(tempFile, output);
    System.out.println(tempFile + " created for time-dependent test.");
    scoreVerification(score, tempFile.toFile());
    Files.delete(tempFile);
    System.out.println(tempFile + " deleted for time-dependent test.");
  }

  protected void scoreVerification(Score score) {
    String testVectorsFileName =
        String.format(TEST_VECTORS_FILE_NAME_TEMPLATE_DEFAULT, score.getClass().getSimpleName());
    URL url = score.getClass().getResource(testVectorsFileName);
    File testYamlFile = FileUtils.getFile(url.getFile());
    scoreVerification(score, testYamlFile);
  }

  protected void scoreVerification(Score score, File file) {
    try (InputStream is = new FileInputStream(file)) {
      TestVectors testVectors = TestVectors.loadFromYaml(is);
      if (testVectors.isEmpty()) {
        System.out.printf("No test vectors for %s%n", score.getClass().getSimpleName());
        fail("Verification failed");
      }
      System.out.printf("Verify %s with %d test vectors%n", score.getClass().getSimpleName(),
          testVectors.size());
      new ScoreVerification(score, testVectors).run();
    } catch (VerificationFailedException e) {
      System.out.printf("Verification failed for %s%n", score.getClass().getSimpleName());
      fail("Verification failed");
    } catch (Exception e) {
      System.out.printf("Couldn't verify %s%n", score.getClass().getSimpleName());
      e.printStackTrace(System.out);
      fail("Verification failed");
    }
  }

  protected String substituteYearsAndDaysPlaceholder(String input) {
    String output = substitutePlaceholder(input, TimeUnit.YEARS);
    output = substitutePlaceholder(output, TimeUnit.DAYS);
    return output;
  }

  protected String substitutePlaceholder(String input, TimeUnit timeUnit) {
    Pattern pattern = Pattern.compile(timeUnit.getRegex());
    Matcher matcher = pattern.matcher(input);

    LocalDate currentDate = LocalDate.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    StringBuilder buf = new StringBuilder();
    while (matcher.find()) {
      LocalDate newDate = currentDate;
      int timeUnitToSubtract = Integer.parseInt(matcher.group(1));
      switch (timeUnit) {
        case DAYS:
          newDate = currentDate.minusDays(timeUnitToSubtract);
          break;
        case YEARS:
          newDate = currentDate.minusYears(timeUnitToSubtract);
          break;
        default:
          break;
      }
      String replacement = newDate.format(formatter);
      matcher.appendReplacement(buf, replacement);
    }
    matcher.appendTail(buf);
    return buf.toString();
  }

  public enum TimeUnit {
    DAYS("\\$CURDATE\\{MINUS_DAYS\\((\\d+)\\)\\}"),
    YEARS("\\$CURDATE\\{MINUS_YEARS\\((\\d+)\\)\\}");

    private final String regex;

    TimeUnit(String regex) {
      this.regex = regex;
    }

    public String getRegex() {
      return regex;
    }
  }
}
