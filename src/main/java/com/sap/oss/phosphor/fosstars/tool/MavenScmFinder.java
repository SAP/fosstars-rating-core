package com.sap.oss.phosphor.fosstars.tool;

import static com.sap.oss.phosphor.fosstars.maven.MavenUtils.readModel;
import static com.sap.oss.phosphor.fosstars.model.subject.oss.GitHubProject.isOnGitHub;
import static java.lang.String.format;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.apache.commons.lang3.StringUtils.strip;

import com.sap.oss.phosphor.fosstars.maven.GAV;
import com.sap.oss.phosphor.fosstars.model.subject.oss.GitHubProject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.maven.model.Model;
import org.apache.maven.model.Scm;

/**
 * The class takes GAV coordinates of an artifact and looks for a URL to its SCM.
 */
public class MavenScmFinder {

  /**
   * A template of a request for downloading files from the Maven Central repository.
   */
  private static final String MAVEN_DOWNLOAD_REQUEST_TEMPLATE
        = "https://repo1.maven.org/maven2/{PATH}";

  /**
   * A template of a path to a POM file of an artifact in the Maven Central repository.
   */
  private static final String PATH_TEMPLATE
      = "{GROUP}/{ARTIFACT}/{VERSION}/{ARTIFACT}-{VERSION}.pom";

  /**
   * Pattern to match the latest version.
   */
  private static final Pattern LATEST_VERSION_PATTERN =
      Pattern.compile("\\s*<latest>([A-Za-z0-9]+(\\.[A-Za-z0-9]+)*.*)</latest>",
          Pattern.CASE_INSENSITIVE);

  /**
   * Takes GAV coordinates of an artifact and looks for a URL to its SCM.
   *
   * @param gav The GAV coordinates.
   * @return A URL to SCM.
   * @throws IOException If something went wrong.
   */
  public Optional<String> findScmFor(GAV gav) throws IOException {
    Model pom = loadPomFor(gav);
    Scm scm = pom.getScm();
    if (scm == null) {
      return Optional.empty();
    }

    return normalizeGitHubProjectPath(scm.getUrl());
  }
 
  /**
   * The method tries to normalize and resolve a GitHub project path from the given URI syntax.
   * 
   * @param url The input URL to be parsed and converted into a GitHub URL.
   * @return A GitHub URL if parsing is successful. Otherwise an #Optional.empty().
   * @throws IOException If something goes wrong.
   */
  public static Optional<String> normalizeGitHubProjectPath(String url) throws IOException {
    final String github = "github";
    Optional<String> path = Optional.empty();

    if (url == null || !url.contains(github)) {
      return path;
    }

    if (url.startsWith("http")) {
      return Optional.ofNullable(strip(url, "/"));
    }

    try {
      path = extractProjectPath(url);
    } catch (IllegalArgumentException e) {
      throw new IOException(format("Oh no!!! The %s is not parseable", url), e);
    }

    if (path.isPresent()) {
      return Optional.ofNullable(format("https://%s.com/%s", github, strip(path.get(), "/")));
    }

    return path;
  }

  /**
   * The method identifies and extracts path from the given URL by matching known syntaxes.
   * 
   * @param url The input URL is parsed to identify the project path.
   * @return A GitHub project path if found. Otherwise an #Optional.empty().
   * @throws IllegalArgumentException If something goes wrong.
   */
  private static Optional<String> extractProjectPath(String url) throws IllegalArgumentException {
    if (url.matches("^\\w+\\@github\\.com\\:(\\/?[\\w-]+)+\\.git\\/?$")) {
      return Optional.ofNullable(url.split(":")[1]);
    }

    URI uri = URI.create(url);
    return Optional.ofNullable(uri.getPath());
  }

  /**
   * Takes GAV coordinates to an artifact and looks for a corresponding GitHub project
   * (maybe a mirror) that contains the source code.
   *
   * @param gav The GAV coordinates.
   * @return A project on GitHub.
   * @throws IOException If something went wrong.
   */
  public Optional<GitHubProject> findGithubProjectFor(GAV gav) throws IOException {
    Optional<String> scm = findScmFor(gav);
    if (scm.isPresent()) {
      String url = scm.get();
      if (isOnGitHub(url)) {
        return Optional.of(GitHubProject.parse(url));
      }
    }

    return tryToGuessGitHubProjectFor(gav);
  }

  /**
   * Takes GAV coordinates and tries to guess a possible GitHub project.
   *
   * @param gav The GAV coordinates.
   * @return A project on GitHub if it exists.
   */
  public Optional<GitHubProject> tryToGuessGitHubProjectFor(GAV gav) {
    Optional<GitHubProject> project = guessGitHubProjectFor(gav);
    if (project.isPresent() && looksLikeValid(project.get())) {
      return project;
    }

    return Optional.empty();
  }

  /**
   * Checks if a specified GitHub project is valid.
   *
   * @param project The project to be checked.
   * @return True if the project is valid, false otherwise.
   */
  private static boolean looksLikeValid(GitHubProject project) {
    try {
      String content = fetch(project.scm().toString());
      return StringUtils.isNotEmpty(content);
    } catch (IOException e) {
      return false;
    }
  }

  /**
   * Guesses a URL to a GitHub project for specified GAV coordinates.
   *
   * @param gav The GAV coordinates.
   * @return A project on GitHub.
   */
  private static Optional<GitHubProject> guessGitHubProjectFor(GAV gav) {
    Optional<GitHubProject> project = guessApacheProjectFor(gav);
    if (project.isPresent()) {
      return project;
    }

    return guessEclipseProjectFor(gav);
  }

  /**
   * Guesses a URL to a project under Apache organization on GitHub for specified GAV coordinates.
   *
   * @param gav The GAV coordinates.
   * @return A project on GitHub.
   */
  private static Optional<GitHubProject> guessApacheProjectFor(GAV gav) {
    if (!gav.group().startsWith("org.apache")) {
      return Optional.empty();
    }

    return Optional.of(new GitHubProject("apache", gav.artifact()));
  }

  /**
   * Guesses a URL to a project under Eclipse organization on GitHub for specified GAV coordinates.
   *
   * @param gav The GAV coordinates.
   * @return A project on GitHub.
   */
  private static Optional<GitHubProject> guessEclipseProjectFor(GAV gav) {
    if (!gav.group().startsWith("org.eclipse")) {
      return Optional.empty();
    }

    return Optional.of(new GitHubProject("eclipse", gav.artifact()));
  }

  /**
   * Search for the latest version of an artifact in the Maven Central repository.
   *
   * @param gav GAV coordinates of the artifact.
   * @return The latest version of the artifact.
   * @throws IOException If something went wrong.
   */
  private static String latestVersionOf(GAV gav) throws IOException {
    Optional<String> latest = latestVersionOf(gav.group(), gav.artifact());
    if (!latest.isPresent()) {
      throw new IOException("Oh no! The latest element did not have the version!");
    }
    return latest.get();
  }

  /**
   * Gathers latest release version from group:artifact coordinate.
   *
   * @param group    A maven artifact group Id.
   * @param artifact A maven artifact Id.
   * @return An optional {@link String} containing the latest release version.
   * @throws IOException If something goes wrong.
   */
  private static Optional<String> latestVersionOf(String group, String artifact)
      throws IOException {
    String url = String.format(
        "https://repo1.maven.org/maven2/%s/%s/maven-metadata.xml",
        group.replaceAll("\\.", "/"), artifact);
    return fetchLatestVersionOf(url);
  }

  /**
   * Loads a POM file for an artifact.
   *
   * @param gav GAV coordinates of the artifact.
   * @return A Maven model of the POM file.
   * @throws IOException If something wrong.
   */
  private static Model loadPomFor(GAV gav) throws IOException {
    String path = PATH_TEMPLATE
        .replace("{GROUP}", gav.group().replace(".", "/"))
        .replace("{ARTIFACT}", gav.artifact())
        .replace("{VERSION}", gav.version().orElse(latestVersionOf(gav)));
    String urlString = MAVEN_DOWNLOAD_REQUEST_TEMPLATE.replace("{PATH}", path);
    String content = fetch(urlString);

    return readModel(IOUtils.toInputStream(content, UTF_8));
  }

  /**
   * Fetches info from the URL.
   *
   * @param url from which the information needs to be collected.
   * @return The info from the URL.
   * @throws IOException If something went wrong.
   */
  private static String fetch(String url) throws IOException {
    try (CloseableHttpClient client = httpClient()) {
      HttpGet httpGetRequest = new HttpGet(url);
      httpGetRequest.addHeader(HttpHeaders.ACCEPT, ContentType.APPLICATION_JSON.getMimeType());
      try (CloseableHttpResponse httpResponse = client.execute(httpGetRequest)) {
        return IOUtils.toString(httpResponse.getEntity().getContent(), UTF_8);
      }
    }
  }

  /**
   * Creates an HTTP client.
   *
   * @return An HTTP client.
   */
  private static CloseableHttpClient httpClient() {
    // Set the Connection timeout to 120 seconds
    int timeout = 120;
    RequestConfig config = RequestConfig.custom().setConnectTimeout(timeout * 1000)
        .setConnectionRequestTimeout(timeout * 1000).setSocketTimeout(timeout * 1000).build();
    return HttpClientBuilder.create()
        .setDefaultRequestConfig(config).build();
  }

  /**
   * Fetches latest version info from the URL.
   *
   * @param url from which the information needs to be collected.
   * @return An optional {@link String} containing the latest release version.
   * @throws IOException If something went wrong.
   */
  private static Optional<String> fetchLatestVersionOf(String url) throws IOException {
    try (CloseableHttpClient client = httpClient()) {
      HttpGet httpGetRequest = new HttpGet(url);
      httpGetRequest.addHeader(HttpHeaders.ACCEPT, ContentType.TEXT_HTML.getMimeType());
      try (CloseableHttpResponse httpResponse = client.execute(httpGetRequest);
          BufferedReader reader = new BufferedReader(
              new InputStreamReader(httpResponse.getEntity().getContent(), UTF_8))) {
        String line = null;
        while ((line = reader.readLine()) != null) {
          Matcher matcher = LATEST_VERSION_PATTERN.matcher(line);
          if (matcher.matches()) {
            return Optional.ofNullable(matcher.group(1));
          }
        }
      }
      return Optional.empty();
    }
  }

  /**
   * Entry point for demo and testing purposes.
   *
   * @param args Command line options.
   * @throws IOException If something went wrong.
   */
  public static void main(String... args) throws IOException {
    String coordinates = args.length > 0 ? args[0] : "org.apache.rocketmq:rocketmq-all:4.9.0";
    Optional<GitHubProject> project
        = new MavenScmFinder().findGithubProjectFor(GAV.parse(coordinates));
    System.out.println(
        project.isPresent() ? String.format("GitHub URL = %s", project.get()) : "No SCM found!");
  }
}