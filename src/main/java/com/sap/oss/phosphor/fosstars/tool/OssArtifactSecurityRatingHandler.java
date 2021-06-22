package com.sap.oss.phosphor.fosstars.tool;

import static com.sap.oss.phosphor.fosstars.model.other.Utils.setOf;
import static java.util.Objects.requireNonNull;

import com.sap.oss.phosphor.fosstars.maven.GAV;
import com.sap.oss.phosphor.fosstars.model.RatingRepository;
import com.sap.oss.phosphor.fosstars.model.rating.oss.OssArtifactSecurityRating;
import com.sap.oss.phosphor.fosstars.model.subject.oss.Artifact;
import com.sap.oss.phosphor.fosstars.model.subject.oss.GitHubProject;
import com.sap.oss.phosphor.fosstars.model.subject.oss.MavenArtifact;
import com.sap.oss.phosphor.fosstars.model.subject.oss.NpmArtifact;
import com.sap.oss.phosphor.fosstars.tool.report.Reporter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;
import java.util.Set;

/**
 * This handler calculates
 * {@link com.sap.oss.phosphor.fosstars.model.rating.oss.OssArtifactSecurityRating}.
 */
public class OssArtifactSecurityRatingHandler extends AbstractHandler {

  /**
   * No project on GitHub.
   */
  private static final GitHubProject NO_PROJECT = null;

  /**
   * Initialize a handler.
   */
  public OssArtifactSecurityRatingHandler() {
    super(RatingRepository.INSTANCE.rating(OssArtifactSecurityRating.class));
  }

  @Override
  public String supportedRatingName() {
    return "oss-artifact-security";
  }

  @Override
  Set<String> supportedSubjectOptions() {
    return setOf("--gav", "--npm", "--purl");
  }

  @Override
  void processMaven(String coordinates) throws IOException {
    processGav(GAV.parse(coordinates));
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
  @Override
  void processNpm(String identifier) throws IOException {
    requireNonNull(identifier, "Oops! NPM package identifier is null!");
    String[] parts = identifier.split("@");
    if (parts.length != 2) {
      throw new IllegalArgumentException(
          "Oops! NPM package seems to be wrong! It has to have a name and a version!");
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
  void processUrl(String url) {
    throw new UnsupportedOperationException("Oops! I don't support URL!");
  }

  @Override
  void processConfig(String filename) {
    throw new UnsupportedOperationException("Oops! I don't support configs!");
  }

  @Override
  Optional<Reporter<GitHubProject>> reporterFrom(ReportConfig reportConfig) {
    return Optional.empty();
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
