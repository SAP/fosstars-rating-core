# Test suite for the command-line tool

The test suite contains a number of shell-based tests
to make sure that the command-line tool works fine.

Setup:

1.  Build the project with `mvn clean package` command.
1.  Get a token for GitHub.
    You can create your personal token [here](https://github.com/settings/tokens).

Then, you can run the tests:

```
TOKEN=xyz # put your token for GitHub
export TOKEN
bash src/test/shell/tool/github/run_tests.sh
```

Logs are going to be available in `src/test/shell/tool/github`.

You can only run one test, for example:

```
bash src/test/shell/tool/github/run_tests.sh test_project_security_with_single_project.sh
```
