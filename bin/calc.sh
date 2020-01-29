#!/bin/bash

if [ ! -d target ]; then
    mvn clean package -DskipTests
fi
java -jar $(find target -name "*-all.jar") $@

