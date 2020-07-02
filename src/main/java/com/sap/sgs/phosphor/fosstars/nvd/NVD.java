package com.sap.sgs.phosphor.fosstars.nvd;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sap.sgs.phosphor.fosstars.nvd.data.CVE;
import com.sap.sgs.phosphor.fosstars.nvd.data.CveMetaData;
import com.sap.sgs.phosphor.fosstars.nvd.data.NvdEntry;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import us.springett.nistdatamirror.NistDataMirror;

/**
 * This class offers an interface to the NVD.
 */
public class NVD {

  /**
   * A logger.
   */
  private static final Logger LOGGER = LogManager.getLogger(NVD.class);

  /**
   * The default location where the date from the NVD is stored.
   */
  private static final String DEFAULT_DOWNLOAD_DIRECTORY = ".fosstars";

  /**
   * The version of NVD feed to be used.
   */
  private static final String NVD_FEED_VERSION = "1.1";

  /**
   * An {@link ObjectMapper} for serialization and deserialization.
   */
  private static final ObjectMapper MAPPER = new ObjectMapper();

  /**
   * A factory for parsing JSON.
   */
  private static final JsonFactory JSON_FACTORY = MAPPER.getFactory();

  /**
   * The location where the data from the NVD is stored.
   */
  private final String downloadDirectory;

  /**
   * Maps a CVE identifier to its entry in NVD.
   */
  private final Map<String, NvdEntry> nvdEntries = new HashMap<>();

  /**
   * The default constructor.
   */
  public NVD() {
    this(DEFAULT_DOWNLOAD_DIRECTORY);
  }

  /**
   * Initializes a new {@link NVD} instance.
   *
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
   * Tells whether downloading date from the NVD failed or not.
   *
   * @return True if downloading the data from the NVD failed, false otherwise.
   */
  boolean downloadFailed() {
    try {
      return jsonFiles().isEmpty();
    } catch (IOException e) {
      return true;
    }
  }

  /**
   * Returns where the data from the NVD is stored.
   */
  String location() {
    return downloadDirectory;
  }

  /**
   * Returns a list of JSON files downloaded from the NVD.
   *
   * @return A list of downloaded JSON files.
   * @throws IOException If something went wrong.
   */
  public List<String> jsonFiles() throws IOException {
    String prefix = String.format("nvdcve-%s-", NVD_FEED_VERSION);
    try (Stream<Path> walk = Files.walk(Paths.get(downloadDirectory), 1)) {
      return walk
          .filter(Files::isRegularFile)
          .filter(path -> path.getFileName().toString().startsWith(prefix))
          .filter(path -> path.getFileName().toString().endsWith(".json"))
          .map(Path::toString)
          .collect(Collectors.toList());
    }
  }

  /**
   * Looks for NVD entries that match to criteria set by a number of matchers.
   *
   * @param matchers The matchers.
   * @return A list of NVD entries for the specified vendor and product.
   */
  public List<NvdEntry> search(Matcher... matchers) {
    List<NvdEntry> result = new ArrayList<>();

    for (NvdEntry entry : nvdEntries.values()) {
      for (Matcher matcher : matchers) {
        if (matcher.match(entry)) {
          result.add(entry);
        }
      }
    }

    return result;
  }

  /**
   * Find a vulnerability by CVE ID.
   *
   * @param cve The CVE ID.
   * @return The vulnerability.
   */
  public Optional<NvdEntry> get(String cve) {
    return Optional.ofNullable(nvdEntries.get(cve));
  }

  /**
   * Open a file.
   *
   * @param file The file.
   * @return Content of the file.
   * @throws IOException If something went wrong.
   */
  InputStream open(String file) throws IOException {
    return Files.newInputStream(Paths.get(file));
  }

  /**
   * Parses the downloaded data from NVD.
   *
   * @throws IOException If something went wrong.
   */
  public void parse() throws IOException {
    if (!nvdEntries.isEmpty()) {
      return;
    }

    for (String file : jsonFiles()) {
      try (JsonParser parser = JSON_FACTORY.createParser(open(file))) {
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

        for (NvdEntry entry : MAPPER.readValue(parser, NvdEntry[].class)) {

          // if one of the following null-checks becomes true,
          // that would mean the there is an entry in NVD without a CVE id
          // that's almost impossible
          // so we just are paranoids

          CVE cve = entry.getCve();
          if (cve == null) {
            LOGGER.warn("Found an entry without CVE! Skip it.");
            continue;
          }

          CveMetaData metadata = cve.getCveDataMeta();
          if (metadata == null) {
            LOGGER.warn("Found an entry without CVE metadata! Skip it.");
            continue;
          }

          String id = metadata.getId();
          if (id == null) {
            LOGGER.warn("Found an entry without CVE id! Skip it.");
            continue;
          }

          nvdEntries.put(id, entry);
        }
      }
    }
  }
}
