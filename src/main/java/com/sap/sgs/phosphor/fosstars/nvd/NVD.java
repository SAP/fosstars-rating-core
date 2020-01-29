package com.sap.sgs.phosphor.fosstars.nvd;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sap.sgs.phosphor.fosstars.nvd.data.NVDEntry;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import us.springett.nistdatamirror.NistDataMirror;

/**
 * This class offers an interface to the NVD.
 */
public class NVD {

  /**
   * The default location where the date from the NVD is stored.
   */
  private static final String DEFAULT_DOWNLOAD_DIRECTORY = ".fosstars_model";

  /**
   * The version of NVD feed to be used.
   */
  // TODO: migrate to 1.1
  private static final String NVD_FEED_VERSION = "1.0";

  /**
   * An {@link ObjectMapper} for serialization and deserialization.
   */
  private static final ObjectMapper MAPPER = new ObjectMapper();

  /**
   * A list of matchers to find vulnerabilities for an open-source project.
   */
  private static final Matcher[] MATCHERS = {
      new ExactMatcher()
  };

  /**
   * The location where the data from the NVD is stored.
   */
  private final String downloadDirectory;

  /**
   * The default constructor.
   */
  public NVD() {
    this(DEFAULT_DOWNLOAD_DIRECTORY);
  }

  /**
   * @param downloadDirectory A directory where the date from the NVD should be stored.
   */
  public NVD(String downloadDirectory) {
    this.downloadDirectory = downloadDirectory;
  }

  /**
   * Downloads data from the NVD database.
   */
  public void download() {
    new NistDataMirror(DEFAULT_DOWNLOAD_DIRECTORY).mirror(NVD_FEED_VERSION);
  }

  /**
   * @return True if downloading the data from the NVD failed, false otherwise.
   */
  public boolean downloadFailed() {
    try {
      return jsonFiles().isEmpty();
    } catch (IOException e) {
      return true;
    }
  }

  /**
   * @return Where the data from the NVD is stored.
   */
  public String location() {
    return downloadDirectory;
  }

  /**
   * @return A list of downloaded JSON files.
   * @throws IOException If something went wrong.
   */
  public List<String> jsonFiles() throws IOException {
    try (Stream<Path> walk = Files.walk(Paths.get(downloadDirectory))) {
      return walk
          .filter(Files::isRegularFile)
          .filter(path -> path.toString().contains(NVD_FEED_VERSION))
          .filter(path -> path.toString().endsWith(".json"))
          .map(Path::toString)
          .collect(Collectors.toList());
    }
  }

  /**
   * Looks for NVD entries for a specific vendor and product.
   *
   * @param vendor The vendor's name.
   * @param product The product's name.
   * @return A list of NVD entries for the specified vendor and product.
   * @throws IOException If something went wrong.
   */
  public List<NVDEntry> find(String vendor, String product) throws IOException {
    List<NVDEntry> nvdEntries = new ArrayList<>();

    JsonFactory factory = MAPPER.getFactory();
    for (String path : jsonFiles()) {
      try (JsonParser parser = factory.createParser(new FileInputStream(path))) {
        while (!parser.isClosed()) {
          if (!JsonToken.FIELD_NAME.equals(parser.nextToken())) {
            continue;
          }
          if ("CVE_Items".equals(parser.getCurrentName())) {
            break;
          }
        }

        if (!JsonToken.START_ARRAY.equals(parser.nextToken())) {
          throw new IllegalArgumentException("Hmm ... Looks like 'CVE_Items' is not an array!");
        }

        for (NVDEntry entry : MAPPER.readValue(parser, NVDEntry[].class)) {
          for (Matcher matcher : MATCHERS) {
            if (matcher.match(entry, vendor, product)) {
              nvdEntries.add(entry);
            }
          }
        }
      }
    }
    return nvdEntries;
  }
}
