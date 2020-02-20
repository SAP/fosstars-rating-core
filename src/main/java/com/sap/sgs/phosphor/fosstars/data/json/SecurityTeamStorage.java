package com.sap.sgs.phosphor.fosstars.data.json;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;

/**
 * This class maintains information about security teams for open-source projects.
 */
public class SecurityTeamStorage extends AbstractJsonStorage {

  /**
   * Path to a resource which contains the information security teams.
   */
  private static final String RESOURCE_PATH =
      "com/sap/sgs/phosphor/fosstars/data/SecurityTeams.json";

  /**
   * Maps a project's code repository URL to info about security team.
   */
  private final Map<String, Info> securityTeams;

  /**
   * Initializes a storage.
   *
   * @param securityTeams Information about security teams for open-source projects.
   */
  public SecurityTeamStorage(
      @JsonProperty("securityTeams") Map<String, Info> securityTeams) {

    Objects.requireNonNull(securityTeams, "Security teams can't be null");
    this.securityTeams = securityTeams;
  }

  /**
   * Checks if a project has a security team.
   *
   * @param url The project's code repository URL.
   * @return True if the project has a security team, false otherwise.
   */
  public boolean supported(String url) {
    for (Map.Entry<String, Info> entry : securityTeams.entrySet()) {
      Info info = entry.getValue();
      for (URL definedUrl : info.urls) {
        if (url.startsWith(definedUrl.toString())) {
          return true;
        }
      }
    }

    return false;
  }

  /*
   * This getter is here to make Jackson happy.
   */
  @JsonGetter("securityTeams")
  private Map<String, Info> securityTeams() {
    return Collections.unmodifiableMap(securityTeams);
  }

  /**
   * Loads info about security teams from the default location.
   *
   * @return An instance of {@link SecurityTeamStorage}.
   * @throws IOException If something went wrong.
   */
  public static SecurityTeamStorage load() throws IOException {
    return load(RESOURCE_PATH);
  }

  /**
   * Loads info about security teams from a specified file.
   *
   * @return An instance of {@link SecurityTeamStorage}.
   * @throws IOException If something went wrong.
   */
  public static SecurityTeamStorage load(String path) throws IOException {
    File file = new File(path);
    SecurityTeamStorage storage;
    if (file.exists()) {
      storage = MAPPER.readValue(file, SecurityTeamStorage.class);
    } else {
      storage = loadFromResource(path);
    }
    if (storage == null) {
      throw new IOException(String.format(
          "Could not load info security teams from %s", path));
    }
    return storage;
  }

  /**
   * Tries to load info about security teams from a specified file.
   *
   * @param path A path to the resource.
   * @return An instance of {@link SecurityTeamStorage}.
   * @throws IOException If something went wrong.
   */
  private static SecurityTeamStorage loadFromResource(String path) throws IOException {
    InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
    if (is != null) {
      try {
        return MAPPER.readValue(is, SecurityTeamStorage.class);
      } finally {
        is.close();
      }
    }
    throw new IOException(String.format("Resource '%s' not found!", path));
  }

  /**
   * This class contains info about a security team and open-source projects which the team covers.
   */
  private static class Info {

    /**
     * Describes how the security team can be contacted.
     */
    private final String contact;

    /**
     * A link to the team's page.
     */
    private final URL link;

    /**
     * A number of URLs to open-source projects covered by the security team.
     */
    private final URL[] urls;

    /**
     * Creates an instance.
     *
     * @param contact Describes how the security team can be contacted.
     * @param link A link to the team's page.
     * @param urls A number of URLs to open-source projects covered by the security team.
     */
    public Info(
        @JsonProperty("contact") String contact,
        @JsonProperty("link") URL link,
        @JsonProperty("urls") URL[] urls) {

      this.contact = contact;
      this.link = link;
      this.urls = urls;
    }
  }
}
