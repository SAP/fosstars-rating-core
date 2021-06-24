package com.sap.oss.phosphor.fosstars.tool;

import com.sap.oss.phosphor.fosstars.data.SubjectValueCache;
import com.sap.oss.phosphor.fosstars.data.UserCallback;
import com.sap.oss.phosphor.fosstars.data.github.GitHubDataFetcher;
import java.util.List;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;

/**
 * A command-line handler that calculates a specific rating.
 */
public interface Handler {

  /**
   * Returns a supported rating name that the command-line tool is aware of.
   * In other words, the rating name can be passed via --rating command-line option.
   *
   * @return A supported rating name.
   */
  String supportedRatingName();

  /**
   * Returns a list of additional command-line options.
   *
   * @return A list of additional command-line options.
   */
  List<Option> options();

  /**
   * Returns a list of additional command-line option groups.
   *
   * @return A list of additional command-line option groups.
   */
  List<OptionGroup> optionGroups();

  /**
   * Set configs for data providers.
   *
   * @param withConfigs A list of configs.
   * @return This handler.
   */
  Handler configureDataProviders(List<String> withConfigs);

  /**
   * Set a path to base directory.
   *
   * @param path A path to base directory.
   * @return This handler.
   */
  Handler baseDirectory(String path);

  /**
   * Set a cache of values for subjects.
   *
   * @param cache The cache.
   * @return This handler.
   */
  Handler set(SubjectValueCache cache);

  /**
   * Set parsed command-line parameters.
   *
   * @param commandLine The command-line parameters.
   * @return This handler.
   */
  Handler set(CommandLine commandLine);

  /**
   * Set a user callback.
   *
   * @param callback The callback
   * @return This handler.
   */
  Handler set(UserCallback callback);

  /**
   * Set an interface to GitHub.
   *
   * @param fetcher The interface to GitHub.
   * @return This handler.
   */
  Handler set(GitHubDataFetcher fetcher);

  /**
   * Run the handler.
   *
   * @return This handler.
   * @throws Exception If something went wrong.
   */
  Handler run() throws Exception;
}
