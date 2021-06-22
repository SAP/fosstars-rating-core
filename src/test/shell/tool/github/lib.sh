#!/bin/bash

check_expected_output() {
  arr=("$@")
  for expected_string in "${arr[@]}"
  do
    echo "Check: $expected_string"
    grep "$expected_string" tmp.log > /dev/null 2>&1
    if [ $? -ne 0 ]; then
      echo "Failed: could not find in the output: $expected_string"
    fi
  done
}

clean_cache() {
  if [ -d .fosstars ]; then
    find .fosstars -name "*cache*.json" | xargs rm -rf
  fi
}

declare -a project_security_default_expected_strings=(
  'Figuring out if the project belongs to the Eclipse Software Foundation'
  'Figuring out how the project uses CodeQL'
  'Figuring out how the project uses LGTM'
  'Figuring out if the project uses OWASP security libraries'
  'Checking how the project uses Dependabot'
  'Figuring out if the project uses GitHub for development'
  'Figuring out if the project uses sanitizers'
  'Figuring out if the project uses FindSecBugs'
  'Figuring out if the project signs jar files'
  'Looking for vulnerabilities in the project'
  'Figuring out if the project has any unpatched vulnerability'
  'Looking for vulnerabilities in NVD'
  'Counting how many stars the project has'
  'Counting how many people contributed to the project in the last three months'
  'Counting how many commits have been done in the last three months'
  'Figuring out if the project belongs to the Apache Software Foundation'
  'Figuring out if the project uses signed commits'
  'Figuring out if the project has a security team'
  'Figuring out if the project uses nohttp'
  'Figuring out if the project uses OWASP Dependency Check'
  'Figuring out if the project has a bug bounty program'
  'Counting how many watchers the project has'
  'Looking for programming languages that are used in the project'
  'Figuring out if the project has a security policy'
  'Figuring out if the project is fuzzed in OSS-Fuzz'
  'Looking for package managers'
  'Looking for programming languages that are used in the project'
  'Figuring out if the project is supported by a company'
  'Here is how the rating was calculated'
  'Rating'
  'Confidence'
  'Sub-score:....Security testing'
  'Sub-score:....Static analysis'
  'Sub-score:....LGTM score'
  'Sub-score:....How a project uses CodeQL'
  'Sub-score:....FindSecBugs score'
  'Sub-score:....Dependency testing'
  'Sub-score:....Dependabot score'
  'Sub-score:....OWASP Dependency Check score'
  'Sub-score:....Fuzzing'
  'Sub-score:....Memory-safety testing'
  'Sub-score:....nohttp tool'
  'Sub-score:....Security awareness'
  'Sub-score:....Unpatched vulnerabilities'
  'Sub-score:....Vulnerability discovery and security testing'
  'Sub-score:....Security testing'
  'Sub-score:....Community commitment'
  'Sub-score:....Project activity'
  'Sub-score:....Project popularity'
  'Sub-score:....Security reviews'
)

declare -a artifact_security_default_expected_strings=(
  'Artifact version'
  'Released artifact versions'
  'Score:........Security score for an artifact of an open-source project'
  'Sub-score:....Security score for an artifact version of an open-source project'
  'Sub-score:....Known vulnerabilities for an artifact of an open-source project'
  'Sub-score:....How frequent an open source project releases new versions'
  'Sub-score:....How old the latest released artifact is'
  'Sub-score:....How up-to-date the given version is'
)

for expected_string in "${project_security_default_expected_strings[@]}"
do
  artifact_security_default_expected_strings+=("$expected_string")
done

declare -a oss_rop_default_expected_strings=(
  'Gathering info about project'"'"'s README file'
  'Fetching info about project'"'"'s teams'
  'Gathering info about vulnerability alerts'
  'Gathering info about project'"'"'s contributing guidelines'
  'Gathering info about project'"'"'s license'
  'Figuring out how the project uses REUSE'
  'Figuring out if the project has a security policy'
  'Here is what we know about the project'
  'If a license has disallowed text'
  'If a project has a LICENSES folder with licenses'
  'If a project has a README file'
  'If a project has a contributing guideline'
  'If a project has a license'
  'If a project has a team with push privileges'
  'If a project has an admin team on GitHub'
  'If a project has enough admins on GitHub'
  'If a project has enough team members on GitHub'
  'If a project has enough teams on GitHub'
  'If a project has unresolved vulnerability alerts'
  'If a project is compliant with REUSE rules'
  'If a project is registered in REUSE'
  'If a project uses REUSE tool'
  'If a project uses an allowed license'
  'If a project'"'"'s README doesn'"'"'t contain required info'
  'If a project'"'"'s contributing guideline has required text'
  'If an open-source project has a security policy'
  'If project'"'"'s README has info about REUSE'
  'If vulnerability alerts are enabled for a project on GitHub'
  'Here is how the rating was calculated'
  'Score:........Open source rules or play score'
  'Are vulnerability alerts enabled?'
  'Does README mention REUSE?'
  'Does it have LICENSES directory with licenses?'
  'Does it have a README file?'
  'Does it have a contributing guideline?'
  'Does it have a license file?'
  'Does it have a security policy?'
  'Does it have a team with push privileges on GitHub?'
  'Does it have an admin team on GitHub?'
  'Does it have enough admins on GitHub?'
  'Does it have enough teams on GitHub?'
  'Does it have unresolved vulnerability alerts?'
  'Does it use an allowed license?'
  'Does teams have enough members on GitHub?'
  'Does the contributing guideline have required text?'
  'Does the license have disallowed content?'
  'If a project'"'"'s README doesn'"'"'t contain required info'
  'Is it compliant with REUSE rules?'
  'Is it registered in REUSE?'
  'Rating'
  'Confidence'
)
