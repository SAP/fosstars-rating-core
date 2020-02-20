package com.sap.sgs.phosphor.fosstars.model.rating.example;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import com.sap.sgs.phosphor.fosstars.model.Version;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.Test;

public class SecurityRatingExampleMonteCarloTest {

  @Test
  public void run() throws Exception {
    Path path = Files.createTempFile("fosstars", "oss_security_rating_example");
    SecurityRatingExample rating = new SecurityRatingExample(Version.SECURITY_RATING_EXAMPLE_1_1);
    try {
      new SecurityRatingExampleMonteCarlo(
          rating, SecurityRatingExampleVerification.TEST_VECTORS, path.toString()).run();
      byte[] content = Files.readAllBytes(path);

      // smoke test
      assertNotNull(content);
      assertNotEquals(0, content.length);
    } finally {
      Files.delete(path);
    }
  }

}