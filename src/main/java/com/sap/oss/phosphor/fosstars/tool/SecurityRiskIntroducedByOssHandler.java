package com.sap.oss.phosphor.fosstars.tool;

import static com.sap.oss.phosphor.fosstars.model.other.Utils.setOf;

import com.sap.oss.phosphor.fosstars.model.RatingRepository;
import com.sap.oss.phosphor.fosstars.model.Subject;
import com.sap.oss.phosphor.fosstars.model.rating.oss.SecurityRiskIntroducedByOss;
import com.sap.oss.phosphor.fosstars.model.subject.oss.GitHubProject;
import com.sap.oss.phosphor.fosstars.tool.report.Reporter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * This handler calculates {@link SecurityRiskIntroducedByOss}.
 */
public class SecurityRiskIntroducedByOssHandler extends AbstractHandler<GitHubProject> {

  /**
   * Initializes a handler.
   */
  public SecurityRiskIntroducedByOssHandler() {
    super(RatingRepository.INSTANCE.rating(SecurityRiskIntroducedByOss.class));
  }

  @Override
  public String supportedRatingName() {
    return "security-risk-from-oss";
  }

  @Override
  Set<String> supportedSubjectOptions() {
    return setOf("--url");
  }

  @Override
  void processUrl(String url) throws IOException {
    process(GitHubProject.parse(url));
  }

  @Override
  void process(GitHubProject project) throws IOException {
    calculator().calculateFor(project);

    if (!project.ratingValue().isPresent()) {
      throw new IOException("Could not calculate a rating!");
    }

    Arrays.stream(createFormatter("text").print(project).split("\n")).forEach(logger::info);
    logger.info("");
    storeReportIfRequested(project, commandLine);
  }

  @Override
  void process(List<GitHubProject> projects, List<Reporter<GitHubProject>> reporters,
      String projectCacheFile) throws IOException {

    logger.info("Starting calculating ratings ...");
    MultipleRatingsCalculator multipleRatingsCalculator =
        new MultipleRatingsCalculator(calculator())
            .set(loadSubjectCache(projectCacheFile))
            .storeCacheTo(projectCacheFile)
            .calculateFor(projects);

    logger.info("Okay, we've done calculating the ratings");

    List<Subject> failedSubjects = multipleRatingsCalculator.failedSubjects();
    if (!failedSubjects.isEmpty()) {
      logger.warn("Ratings couldn't be calculated for {} project{}",
          failedSubjects.size(), failedSubjects.size() == 1 ? "" : "s");
      for (GitHubProject project : projects) {
        logger.info("    {}", project.scm());
      }
    }

    if (!reporters.isEmpty()) {
      logger.info("Now let's generate reports");
      for (Reporter<GitHubProject> reporter : reporters) {
        reporter.runFor(projects);
      }
    }
  }

  @Override
  void processConfig(String filename) {
    throw new UnsupportedOperationException("Oops! I don't support these configs!");
  }
}
