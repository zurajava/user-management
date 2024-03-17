#!/usr/bin/env bash

#Build project as boot jar
./gradlew clean bootJar
echo Finish build process

#Create docker image from docker file and boot jar
docker build -t user-management:1.0 .
echo Finish docker image creation process
#
docker-compose up -d
echo Finish service run