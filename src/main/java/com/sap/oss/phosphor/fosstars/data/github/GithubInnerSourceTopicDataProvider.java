package com.sap.oss.phosphor.fosstars.data.github;

import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.HAS_INNERSOURCE_TOPIC;

import com.sap.oss.phosphor.fosstars.model.Feature;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.ValueSet;
import com.sap.oss.phosphor.fosstars.model.other.Utils;
import com.sap.oss.phosphor.fosstars.model.subject.oss.GitHubProject;
import com.sap.oss.phosphor.fosstars.model.value.ValueHashSet;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;

public class GithubInnerSourceTopicDataProvider extends GitHubCachingDataProvider {

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
  public Set<Feature<?>> supportedFeatures() {
    return Utils.setOf(HAS_INNERSOURCE_TOPIC);
  }

  @Override
  protected ValueSet fetchValuesFor(GitHubProject project) throws IOException {
    Optional<String> topicOptional = fetcher.githubTopicsFor(project).stream().filter(topic -> {
      return ACCEPTED_TOPICS.contains(topic.toLowerCase(Locale.US));
    }).findFirst();

    Value<Boolean> value;
    if (topicOptional.isPresent()) {
      value = HAS_INNERSOURCE_TOPIC.value(true);
    } else {
      value = HAS_INNERSOURCE_TOPIC.value(false)
        .explain("The repository does not have an InnerSource topic.");
    }

    return ValueHashSet.from(value);
  }
}
