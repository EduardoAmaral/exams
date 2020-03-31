# Exams

[![](https://github.com/EduardoAmaral/exams/workflows/build/badge.svg)](https://github.com/EduardoAmaral/exams) [![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=EduardoAmaral_exams&metric=alert_status)](https://sonarcloud.io/dashboard?id=EduardoAmaral_exams) [![Coverage](https://sonarcloud.io/api/project_badges/measure?project=EduardoAmaral_exams&metric=coverage)](https://sonarcloud.io/dashboard?id=EduardoAmaral_exams) [![BCH compliance](https://bettercodehub.com/edge/badge/EduardoAmaral/exams?branch=master)](https://bettercodehub.com/)

Welcome to Exams!

## Initial Setup
* Install the jdk 11 or higher
* Install the Docker Desktop
* Create the environment variables in `~/.bashrc` or `~/.zshrc`
* From the root folder, run `./gradlew build`

## Running
* From the root folder, run `docker-compose up -d` to create all docker containers necessary
* After that, run `./gradlew bootRun`

## Debugging
* In the Intellij IDEA, create a `Remote Configuration` with the default values, port `5005`
* With the application running, start the remote task
