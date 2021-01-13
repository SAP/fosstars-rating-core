package com.sap.oss.phosphor.fosstars.model.score.oss;

import static org.junit.Assert.fail;

import com.sap.oss.phosphor.fosstars.model.Feature;
import com.sap.oss.phosphor.fosstars.model.Parameter;
import com.sap.oss.phosphor.fosstars.model.Rating;
import com.sap.oss.phosphor.fosstars.model.RatingRepository;
import com.sap.oss.phosphor.fosstars.model.Score;
import com.sap.oss.phosphor.fosstars.model.Visitor;
import com.sap.oss.phosphor.fosstars.model.qa.ScoreVerification;
import com.sap.oss.phosphor.fosstars.model.qa.TestVectors;
import com.sap.oss.phosphor.fosstars.model.qa.VerificationFailedException;
import com.sap.oss.phosphor.fosstars.model.rating.oss.OssSecurityRating;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;

public class ScoreVerificationTest {

  @Test
  public void testVerification() {
    ScoreCollector collector = new ScoreCollector();
    RatingRepository.INSTANCE.rating(OssSecurityRating.class).accept(collector);

    // add extra scores
    collector.scores.add(new VulnerabilityLifetimeScore());

    boolean failed = false;
    for (Score score : collector.scores) {
      String className = score.getClass().getSimpleName();
      String testVectorsFileName = String.format("%sTestVectors.yml", className);
      try (InputStream is = score.getClass().getResourceAsStream(testVectorsFileName)) {
        TestVectors testVectors = TestVectors.loadFromYaml(is);
        if (testVectors.isEmpty()) {
          failed = true;
          System.out.printf("No test vectors for %s%n", className);
          continue;
        }
        System.out.printf("Verify %s with %d test vectors%n", className, testVectors.size());
        new ScoreVerification(score, testVectors).run();
      } catch (VerificationFailedException e) {
        failed = true;
        System.out.printf("Verification failed for %s%n", className);
      } catch (Exception e) {
        failed = true;
        System.out.printf("Couldn't verify %s%n", className);
        e.printStackTrace(System.out);
      }
    }

    if (failed) {
      fail("Verification failed");
    }
  }

  private static class ScoreCollector implements Visitor {

    private final List<Score> scores = new ArrayList<>();

    @Override
    public void visit(Score score) {
      scores.add(score);
    }

    @Override
    public void visit(Rating rating) {
      // do nothing
    }

    @Override
    public void visit(Feature feature) {
      // do nothing
    }

    @Override
    public void visit(Parameter parameter) {
      // do nothing
    }
  }
}
