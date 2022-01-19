package com.sap.oss.phosphor.fosstars.nvd;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.sap.oss.phosphor.fosstars.nvd.data.CVE;
import com.sap.oss.phosphor.fosstars.nvd.data.CveMetaData;
import com.sap.oss.phosphor.fosstars.nvd.data.NvdEntry;
import com.sap.oss.phosphor.fosstars.util.Json;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import us.springett.nistdatamirror.NistDataMirror;

/**
 * <p>This class offers an interface to the NVD.</p>
 * <p>The class is not thread-safe.</p>
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
   * A file where the class stores a timestamp when the data was downloaded.
   */
  private static final String TIMESTAMP_FILENAME = "nvd_last_updated_timestamp";

  /**
   * How often data from NVD should be downloaded.
   */
  private static final Duration UPDATE_INTERVAL = Duration.ofDays(1);

  /**
   * The version of NVD feed to be used.
   */
  private static final String NVD_FEED_VERSION = "1.1";

  /**
   * The location where the data from the NVD is stored.
   */
  private final String downloadDirectory;

  /**
   * A list of JSON files downloaded from the NVD.
   */
  private final List<String> jsonFiles = new ArrayList<>();

  /**
   * A list of pre-loaded NVD entries.
   */
  private final List<NvdEntry> nvdEntries = new ArrayList<>();

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
    Objects.requireNonNull(downloadDirectory, "Oh no! Download directory is null!");
    this.downloadDirectory = downloadDirectory;
  }

  /**
   * Downloads data from the NVD database.
   */
  public void download() {
    new NistDataMirror(downloadDirectory).mirror(NVD_FEED_VERSION);
    updateTimestamp();
    jsonFiles.clear();
  }

  /**
   * Checks if data from NVD should be downloaded.
   *
   * @return True if data from NVD should be downloaded, false otherwise.
   */
  private boolean shouldDownload() {
    Path path = timestampFile();
    if (Files.isRegularFile(path)) {
      try {
        long timestamp = Long.parseLong(new String(Files.readAllBytes(path)));
        Duration lastUpdated = Duration.ofMillis(System.currentTimeMillis() - timestamp);
        return lastUpdated.compareTo(UPDATE_INTERVAL) > 0;
      } catch (IOException | NumberFormatException e) {
        LOGGER.warn("Oops! Could not read a timestamp from '{}'", path);
        return true;
      }
    }

    return true;
  }

  /**
   * Store a timestamp when NVD was downloaded.
   */
  private void updateTimestamp() {
    Path path = timestampFile();
    try {
      Files.write(path, String.valueOf(System.currentTimeMillis()).getBytes());
    } catch (IOException e) {
      LOGGER.warn("Oops! Could not write a timestamp to '{}'", path);
    }
  }

  /**
   * Returns a path to the file that stores a timestamp when NVD was downloaded.
   *
   * @return A path to the file that stores a timestamp when NVD was downloaded.
   */
  private Path timestampFile() {
    return Paths.get(downloadDirectory).resolve(TIMESTAMP_FILENAME);
  }

  /**
   * Download and parses NVD if necessary.
   */
  private void updateIfNecessary() {
    if (shouldDownload()) {
      download();
    }
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
   * Returns a list of JSON files downloaded from the NVD.
   *
   * @return A list of downloaded JSON files.
   * @throws IOException If something went wrong.
   */
  public List<String> jsonFiles() throws IOException {
    if (jsonFiles.isEmpty()) {
      String prefix = String.format("nvdcve-%s-", NVD_FEED_VERSION);
      try (Stream<Path> walk = Files.walk(Paths.get(downloadDirectory), 1)) {
        jsonFiles.addAll(
            walk.filter(Files::isRegularFile)
                .filter(path -> path.getFileName().toString().startsWith(prefix))
                .filter(path -> path.getFileName().toString().endsWith(".json"))
                .map(Path::toString)
                .collect(Collectors.toList()));
      }
    }

    return new ArrayList<>(jsonFiles);
  }

  /**
   * Pre-load data from NVD.
   *
   * @throws IOException If data could not be loaded or parsed.
   */
  public void preload() throws IOException {
    LOGGER.info("Load data from NVD ...");
    parse(nvdEntries::add);
    LOGGER.info("Loaded {} CVE entries from NVD", nvdEntries.size());
  }

  /**
   * Looks for NVD entries that match to criteria set by a number of matchers.
   *
   * @param matchers The matchers.
   * @return A list of NVD entries for the specified vendor and product.
   * @throws IOException If something went wrong.
   */
  public List<NvdEntry> search(Matcher... matchers) throws IOException {
    updateIfNecessary();

    List<NvdEntry> result = new ArrayList<>();
    Consumer<NvdEntry> collector = nvdEntry -> {
      for (Matcher matcher : matchers) {
        if (matcher.match(nvdEntry)) {
          result.add(nvdEntry);
        }
      }
    };

    if (!nvdEntries.isEmpty()) {
      nvdEntries.forEach(collector);
    } else {
      parse(collector);
    }

    return result;
  }

  /**
   * Looks for an NVD entry for the given cveId.
   *
   * @param matcher The {@link Matcher}.
   * @return An {@link NvdEntry}.
   * @throws IOException If something went wrong.
   */
  public Optional<NvdEntry> searchNvd(Matcher matcher) throws IOException {
    updateIfNecessary();
    List<NvdEntry> nvdEntries = search(matcher);
    if (nvdEntries.size() == 1) {
      return Optional.ofNullable(nvdEntries.get(0));
    }
    return Optional.empty();
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
   * Parses the downloaded data from NVD and feed entries to a consumer.
   *
   * @param consumer The consumer.
   * @throws IOException If something went wrong.
   */
  private void parse(Consumer<NvdEntry> consumer) throws IOException {
    updateIfNecessary();

    for (String file : jsonFiles()) {
      try (JsonParser parser = Json.mapper().getFactory().createParser(open(file))) {
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

        for (NvdEntry entry : Json.mapper().readValue(parser, NvdEntry[].class)) {

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

          consumer.accept(entry);
        }
      }
    }
  }

}
