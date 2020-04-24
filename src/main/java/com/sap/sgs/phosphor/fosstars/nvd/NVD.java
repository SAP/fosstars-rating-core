package com.sap.sgs.phosphor.fosstars.nvd;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sap.sgs.phosphor.fosstars.nvd.data.CVE;
import com.sap.sgs.phosphor.fosstars.nvd.data.CVEDataMeta;
import com.sap.sgs.phosphor.fosstars.nvd.data.NvdEntry;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
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
  // TODO: migrate to 1.1
  private static final String NVD_FEED_VERSION = "1.0";

  /**
   * An {@link ObjectMapper} for serialization and deserialization.
   */
  private static final ObjectMapper MAPPER = new ObjectMapper();

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
   * Returns true if downloading the data from the NVD failed, false otherwise.
   */
  boolean downloadFailed() {
    try {
      return contents().isEmpty();
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
   * Returns content of the downloaded database.
   *
   * @return A list of input streams that contain contend of NVD.
   * @throws IOException If something went wrong.
   */
  public List<InputStream> contents() throws IOException {
    Path downloadDirectoryPath = Paths.get(downloadDirectory);
    if (!Files.exists(downloadDirectoryPath)) {
      return Collections.emptyList();
    }

    try (Stream<Path> walk = Files.walk(downloadDirectoryPath)) {
      return walk
          .filter(Files::isRegularFile)
          .filter(path -> path.toString().contains(NVD_FEED_VERSION))
          .filter(path -> path.toString().endsWith(".json"))
          .map(NVD::open)
          .collect(Collectors.toList());
    }
  }

  /**
   * Creates an input stream for a file.
   *
   * @param file The file.
   * @return The input stream.
   */
  private static InputStream open(Path file) {
    try {
      return Files.newInputStream(file);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
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
   * Parses the downloaded data from NVD.
   *
   * @throws IOException If something went wrong.
   */
  public void parse() throws IOException {
    if (!nvdEntries.isEmpty()) {
      return;
    }

    JsonFactory factory = MAPPER.getFactory();
    for (InputStream content : contents()) {
      try (JsonParser parser = factory.createParser(content)) {
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

          CVEDataMeta metadata = cve.getCveDataMeta();
          if (metadata == null) {
            LOGGER.warn("Found an entry without CVE metadata! Skip it.");
            continue;
          }

          String id = metadata.getID();
          if (id == null) {
            LOGGER.warn("Found an entry without CVE id! Skip it.");
            continue;
          }

          nvdEntries.put(id, entry);
        }
      } finally {
        content.close();
      }
    }
  }
}
