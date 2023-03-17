package com.sap.oss.phosphor.fosstars.data.github;

import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.HAS_INNERSOURCE_TOPIC;

import com.sap.oss.phosphor.fosstars.model.Feature;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.subject.oss.GitHubProject;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;

public class GithubInnerSourceTopicDataProvider extends CachedSingleFeatureGitHubDataProvider<Boolean> {

  private static final Set<String> ACCEPTED_TOPICS;

  static {
    Set<String> acceptedTopics = new HashSet<>();
    acceptedTopics.add("inner-source");
    acceptedTopics.add("innersource");
    ACCEPTED_TOPICS = Collections.unmodifiableSet(acceptedTopics);
  }

  public GithubInnerSourceTopicDataProvider(GitHubDataFetcher fetcher) {
    super(fetcher);
  }

  @Override
  public Feature<Boolean> supportedFeature() {
    return HAS_INNERSOURCE_TOPIC;
  }

  @Override
  protected Value<Boolean> fetchValueFor(GitHubProject project) throws IOException {
    Optional<String> topicOptional = fetcher.githubTopicsFor(project).stream().filter(topic -> {
      return ACCEPTED_TOPICS.contains(topic.toLowerCase(Locale.US));
    }).findFirst();

    if (topicOptional.isPresent()) {
      return HAS_INNERSOURCE_TOPIC.value(true);
    }
    return HAS_INNERSOURCE_TOPIC.value(false)
        .explain("The repository does not have an InnerSource topic.");
  }
}
