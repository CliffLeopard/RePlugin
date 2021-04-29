#!/usr/bin/env bash
./gradlew :replugin-host-gradle:publishToMavenLocal   --info
./gradlew :replugin-plugin-gradle:publishToMavenLocal  --info