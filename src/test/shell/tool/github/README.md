# Test suite for the demo tool

The test suite contains a number of shell-based tests
to make sure that the demo tool works fine.

Pre-requisites:

1.  Build the project with `maven clean package` command.
1.  Get a token for GitHub.
    You can create your personal token [here](https://github.com/settings/tokens).

Then, you can run the tests:

```
TOKEN=xyz # put your token for GitHub
export TOKEN
bash src/test/shell/tool/github/run_tests.sh
```

Logs are going to be available in `src/test/shell/tool/github`.

When running for the first time, the tests take longer to execute.
Next time, the tests will use the cached data which speed them up.
