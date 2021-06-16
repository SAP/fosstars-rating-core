package com.sap.oss.phosphor.fosstars.tool;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;

import com.github.packageurl.PackageURL;
import com.github.packageurl.PackageURL.StandardTypes;
import com.sap.oss.phosphor.fosstars.maven.GAV;
import com.sap.oss.phosphor.fosstars.model.RatingRepository;
import com.sap.oss.phosphor.fosstars.model.rating.oss.OssArtifactSecurityRating;
import com.sap.oss.phosphor.fosstars.model.subject.oss.Artifact;
import com.sap.oss.phosphor.fosstars.model.subject.oss.GitHubProject;
import com.sap.oss.phosphor.fosstars.model.subject.oss.MavenArtifact;
import com.sap.oss.phosphor.fosstars.model.subject.oss.NpmArtifact;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

/**
 *
 */
public class OssArtifactSecurityRatingHandler extends AbstractHandler {

  /**
   *
   */
  private static final String NO_VERSION = null;

  /**
   *
   */
  private static final GitHubProject NO_PROJECT = null;

  /**
   *
   */
  public OssArtifactSecurityRatingHandler() {
    super(RatingRepository.INSTANCE.rating(OssArtifactSecurityRating.class));
  }

  @Override
  public String supportedRatingName() {
    return "oss-artifact-security";
  }

  @Override
  public OssArtifactSecurityRatingHandler run() throws Exception {
    requireOneOfIn(commandLine, "--gav", "--npm", "--purl");

    if (commandLine.hasOption("gav")) {
      processGav(GAV.parse(commandLine.getOptionValue("gav")));
    }

    if (commandLine.hasOption("npm")) {
      processNpm(commandLine.getOptionValue("npm"));
    }

    if (commandLine.hasOption("purl")) {
      processPurl(commandLine.getOptionValue("purl"));
    }

    return this;
  }

  /**
   * Calculate a rating for an artifact identified by a PURL.
   *
   * @param packageUrl The PURL.
   * @throws IOException If something went wrong.
   */
  private void processPurl(String packageUrl) throws Exception {
    PackageURL purl = new PackageURL(packageUrl);

    switch (purl.getType().toLowerCase()) {
      case StandardTypes.MAVEN:
        processGav(new GAV(purl.getNamespace(), purl.getName(), purl.getVersion()));
        break;
      case StandardTypes.NPM:
        processNpm(purl.getName());
        break;
      default:
        throw new IOException(format("Oh no! Unsupported PURL type: '%s'", purl.getType()));
    }
  }

  /**
   * Calculate a rating for an artifact identified by GAV coordinates.
   *
   * @param gav The GAV coordinates.
   * @throws IOException If something went wrong.
   */
  private void processGav(GAV gav) throws IOException {
    Optional<GitHubProject> project = new MavenScmFinder().findGithubProjectFor(gav);
    if (!project.isPresent()) {
      logger.warn("Could not find SCM on GitHub for the artifact!");
    }

    MavenArtifact artifact = new MavenArtifact(
        gav.group(),
        gav.artifact(),
        gav.version().orElseThrow(() -> new IllegalArgumentException("No version specified!")),
        project.orElse(NO_PROJECT));

    calculator().calculateFor(artifact);

    if (!artifact.ratingValue().isPresent()) {
      throw new IOException("Could not calculate a rating!");
    }

    Arrays.stream(createFormatter("text").print(artifact).split("\n")).forEach(logger::info);
    logger.info("");
  }

  /**
   * Calculate a rating for an artifact identified by NPM name.
   *
   * @param identifier The NPM package.
   * @throws IOException If something went wrong.
   */
  private void processNpm(String identifier) throws IOException {
    requireNonNull(identifier, "Oops! NPM package identifier is null!");
    String[] parts = identifier.split("@");
    if (parts.length != 2) {
      throw new IllegalArgumentException("Oops! NPM package seems to be wrong!");
    }
    String name = parts[0];
    String version = parts[1];

    Optional<GitHubProject> project = new NpmScmFinder().findGithubProjectFor(name);
    if (!project.isPresent()) {
      logger.warn("Could not find SCM on GitHub for the artifact!");
    }

    NpmArtifact artifact = new NpmArtifact(name, version, project.orElse(NO_PROJECT));

    calculator().calculateFor(artifact);

    if (!artifact.ratingValue().isPresent()) {
      throw new IOException("Could not calculate a rating!");
    }

    Arrays.stream(createFormatter("text").print(artifact).split("\n")).forEach(logger::info);
    logger.info("");
  }

  @Override
  SingleRatingCalculator calculator() throws IOException {
    return super.calculator().set((subject, provider) -> {
      if (provider.supports(subject)) {
        return Optional.of(subject);
      }

      if (subject instanceof Artifact) {
        Artifact artifact = (Artifact) subject;
        if (artifact.project().isPresent() && provider.supports(artifact.project().get())) {
          return Optional.of(artifact.project().get());
        }
      }

      return Optional.empty();
    });
  }

}
