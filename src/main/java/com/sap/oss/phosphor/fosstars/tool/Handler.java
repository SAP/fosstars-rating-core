package com.sap.oss.phosphor.fosstars.tool;

import com.sap.oss.phosphor.fosstars.data.SubjectValueCache;
import com.sap.oss.phosphor.fosstars.data.UserCallback;
import com.sap.oss.phosphor.fosstars.data.github.GitHubDataFetcher;
import java.util.List;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;

public interface Handler extends AutoCloseable {

  String supportedRatingName();

  List<Option> options();

  List<OptionGroup> optionGroups();

  Handler configureDataProviders(List<String> withConfigs);

  Handler baseDirectory(String path);

  Handler set(SubjectValueCache cache);

  Handler set(CommandLine commandLine);

  Handler set(UserCallback callback);

  Handler set(GitHubDataFetcher fetcher);

  Handler run() throws Exception;
}
