package com.sap.sgs.phosphor.fosstars.tool.github;

import com.sap.sgs.phosphor.fosstars.data.NoUserCallback;
import com.sap.sgs.phosphor.fosstars.data.Terminal;
import com.sap.sgs.phosphor.fosstars.data.UserCallback;
import com.sap.sgs.phosphor.fosstars.model.value.RatingValue;
import com.sap.sgs.phosphor.fosstars.tool.InputString;
import com.sap.sgs.phosphor.fosstars.tool.YesNoQuestion;
import com.sap.sgs.phosphor.fosstars.tool.YesNoQuestion.Answer;
import com.sap.sgs.phosphor.fosstars.tool.format.PrettyPrinter;
import java.io.IOException;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.kohsuke.github.GitHub;

public class SecurityRatingCalculator {

  /**
   * A usage message.
   */
  private static final String USAGE =
      "java -jar fosstars-github-rating-calc.jar [options]";

  /**
   * Entry point.
   */
  public static void main(String... args) throws IOException {
    Options options = new Options();
    options.addOption("h", "help", false,
        "print this message");
    options.addOption("n", "no-questions", false,
        "don't ask a user if a feature can't be automatically gathered");
    options.addOption(Option.builder("u")
        .required()
        .hasArg()
        .longOpt("url")
        .desc("repository URL")
        .build());
    options.addOption(Option.builder("t")
        .longOpt("token")
        .hasArg()
        .desc("access token")
        .build());

    CommandLine commandLine;
    try {
      commandLine = new DefaultParser().parse(options, args);
    } catch (ParseException e) {
      System.out.printf("[x] Something went wrong: %s%n", e.getMessage());
      HelpFormatter formatter = new HelpFormatter();
      formatter.printHelp(USAGE, options);
      return;
    }

    if (commandLine.hasOption("h")) {
      HelpFormatter formatter = new HelpFormatter();
      formatter.printHelp(USAGE, options);
      return;
    }

    boolean mayTalk = !commandLine.hasOption("no-questions");
    UserCallback callback = mayTalk ? new Terminal() : NoUserCallback.INSTANCE;

    GitHub github = connectToGithub(commandLine.getOptionValue("token"), callback);
    if (github == null) {
      System.out.println("[x] Couldn't connect to GitHub!");
      return;
    }

    GitHubProject project = GitHubProject.parse(commandLine.getOptionValue("url"));

    new SingleSecurityRatingCalculator(github)
        .token(commandLine.getOptionValue("token"))
        .set(callback)
        .process(project);

    RatingValue ratingValue = project.ratingValue()
        .orElseThrow(() -> new IOException("Could not calculate a rating!"));

    System.out.println("[+]");
    System.out.print(new PrettyPrinter().print(ratingValue));
    System.out.printf("[+] Bye!%n");
  }

  /**
   * Tries to establish a connection to GitHub.
   *
   * @param token A GitHub token (may be null).
   * @return An interface for the GitHub API, or null if it couldn't establish a connection.
   */
  private static GitHub connectToGithub(String token, UserCallback callback) {
    if (token == null && callback.canTalk()) {
      System.out.println("[!] You didn't provide an access token for GitHub ...");
      System.out.println("[!] But you can create it now. Do the following:");
      System.out.println("    1. Go to https://github.com/settings/tokens");
      System.out.println("    2. Click the 'Generate new token' button");
      System.out.println("    3. Write a short note for a token");
      System.out.println("    4. Select scopes");
      System.out.println("    5. Click the 'Generate token' button");
      System.out.println("    6. Copy your new token");
      System.out.println("    7. Paste the token here");
      Answer answer = new YesNoQuestion(callback, "Would you like to create a token now?").ask();
      switch (answer) {
        case YES:
          System.out.println("[+] Paste the token here ------+");
          System.out.println("                               |");
          System.out.println("                               |");
          System.out.println("     +-------------------------+");
          System.out.println("     |");
          System.out.println("     |");
          System.out.println("     V");
          token = new InputString(callback).get();
          break;
        case NO:
          System.out.println("Okay ...");
          break;
        default:
          throw new IllegalArgumentException(
              String.format("Not sure what I can do with '%s'", answer));
      }
    }
    if (token != null) {
      try {
        return GitHub.connectUsingOAuth(token);
      } catch (IOException e) {
        System.out.printf("[x] Something went wrong: %s%n", e);
      }
    } else {
      System.out.printf("[!] No token provided%n");
    }
    try {
      return GitHub.connect();
    } catch (IOException e) {
      System.out.printf("[x] Something went wrong: %s%n", e);
    }
    try {
      GitHub github = GitHub.connectAnonymously();
      System.out.println("[!] We have established only an anonymous connection to GitHub ...");
      return github;
    } catch (IOException e) {
      System.out.printf("[x] Something went wrong: %s%n", e);
    }
    return null;
  }
}
