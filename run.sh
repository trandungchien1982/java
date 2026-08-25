#!/usr/bin/env bash
set -euo pipefail

JAVA_VERSION=$(java -version 2>&1 | head -n 1)
echo "Using: $JAVA_VERSION"

if ! command -v gradle >/dev/null 2>&1; then
  echo "ERROR: Gradle 8.x is required but 'gradle' was not found in PATH." >&2
  exit 1
fi

gradle clean build

java \
  -javaagent:custom-agent/build/libs/custom-agent-1.0.0.jar \
  -jar demo-app/build/libs/demo-app-1.0.0.jar
