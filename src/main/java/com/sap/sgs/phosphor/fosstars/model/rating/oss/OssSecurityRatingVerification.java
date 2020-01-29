package com.sap.sgs.phosphor.fosstars.model.rating.oss;

import com.sap.sgs.phosphor.fosstars.model.Feature;
import com.sap.sgs.phosphor.fosstars.model.Label;
import com.sap.sgs.phosphor.fosstars.model.Value;
import com.sap.sgs.phosphor.fosstars.model.math.DoubleInterval;
import com.sap.sgs.phosphor.fosstars.model.qa.AbstractVerification;
import com.sap.sgs.phosphor.fosstars.model.qa.TestVector;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

/**
 * This class implements a verification procedure for {@link OssSecurityRating}.
 * The class loads test vectors, and provides methods to verify a {@link OssSecurityRating}
 * against those test vectors.
 */
class OssSecurityRatingVerification extends AbstractVerification {

  private static final String DEFAULT_TEST_VECTORS_CSV = "OssSecurityRatingTestVectors.csv";

  /**
   * @param rating A rating to be verified.
   * @param vectors A list of test vectors.
   */
  private OssSecurityRatingVerification(OssSecurityRating rating, List<TestVector> vectors) {
    super(rating, vectors);
  }

  /**
   * Creates an instance of {@link OssSecurityRatingVerification} for a specified rating.
   * The method loads test vectors from a default resource.
   *
   * @param rating The rating to be verified.
   * @return An instance of {@link OssSecurityRatingVerification}.
   */
  static OssSecurityRatingVerification createFor(OssSecurityRating rating) throws IOException {
    try (InputStream is = OssSecurityRatingVerification.class.getResourceAsStream(DEFAULT_TEST_VECTORS_CSV)) {
      return loadTestVectorsFromCSVResource(rating, is);
    }
  }

  /**
   * Creates an instance of {@link OssSecurityRatingVerification}
   * for a specified rating and list of test vectors.
   *
   * @param rating The rating to be verified.
   * @param vectors The test vectors.
   * @return An instance of {@link OssSecurityRatingVerification}.
   */
  static OssSecurityRatingVerification createFor(OssSecurityRating rating, List<TestVector> vectors) {
    return new OssSecurityRatingVerification(rating, vectors);
  }

  static OssSecurityRatingVerification loadTestVectorsFromCSVResource(
      OssSecurityRating rating, InputStream is) throws IOException  {

    List<TestVector> vectors = new ArrayList<>();
    try (Reader reader = new InputStreamReader(is)) {
      Feature[] features = rating.allFeatures();

      Iterable<CSVRecord> records = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(reader);
      for (CSVRecord record : records) {
        DoubleInterval expectedScore = DoubleInterval.closed(
            Double.valueOf(record.get("score_from")),
            Double.valueOf(record.get("score_to")));
        String label = record.get("label");
        Label expectedLabel = null;
        if (!"None".equals(label) && !label.isEmpty()) {
          expectedLabel = OssSecurityRating.SecurityLabel.valueOf(label);
        }
        String alias = record.get("alias");

        Set<Value> values = new HashSet<>();
        for (Feature feature : features) {
          String raw = findValue(feature, record);

          if ("unknown".equals(raw)) {
            values.add(feature.unknown());
            continue;
          }

          values.add(feature.parse(raw));
        }

        vectors.add(new TestVector(values, expectedScore, expectedLabel, alias));
      }
    }
    return createFor(rating, vectors);
  }

  /**
   * Looks for a feature value in a CSV record.
   *
   * @param feature The feature.
   * @param record The record.
   * @return The feature value if found.
   * @throws IOException If no value found.
   */
  private static String findValue(Feature feature, CSVRecord record) throws IOException {
    for (Map.Entry<String, String> entry : record.toMap().entrySet()) {
      if (entry.getKey().equals(feature.name())) {
        return entry.getValue().trim();
      }
    }
    throw new IOException(String.format("Couldn't find the feature '%s' in test vector '%s'",
        feature.name(), record.get("alias")));
  }

}
