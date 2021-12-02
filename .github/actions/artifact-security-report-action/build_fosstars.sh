#!/bin/bash

old_directory=$(pwd)
git clone https://github.com/sourabhsparkala/fosstars-rating-core && \
    cd fosstars-rating-core && \
    git checkout $FOSSTARS_VERSION && \
    mvn package -ntp -DskipTests -Dcheckstyle.skip -Dmaven.javadoc.skip
code=$?
cd ${old_directory}
exit $code
