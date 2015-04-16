#!/bin/bash

mvn install -Dmaven.test.skip=true
cp target/backend-rest-1.0-SNAPSHOT-jar-with-dependencies.jar .

