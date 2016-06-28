#!/bin/bash
#mvn clean install
rm -rf ./release
mkdir release
mkdir release/logs
cp target/backend-rest-1.0-SNAPSHOT-jar-with-dependencies.jar release/
cp -r frontend release/
cp -r lib release/
cp -r bin release/
cp -r test_inputs release/
cp -r resources release/
cp orientdb.win.config release/
cp orientdb.lin.config release/
cp jetty.* release/
cp keystore release/
cp pom.xml release/
cp runner.* release/