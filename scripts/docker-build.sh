#!/usr/bin/env bash

set -euo pipefail

APP_NAME="${APP_NAME:-supplychain-agent}"
APP_VERSION="${APP_VERSION:-0.0.1-SNAPSHOT}"
IMAGE_REGISTRY="${IMAGE_REGISTRY:-}"
IMAGE_TAG="${IMAGE_TAG:-$(date +%Y%m%d%H%M%S)}"
SKIP_TESTS="${SKIP_TESTS:-true}"

if [[ -n "${IMAGE_REGISTRY}" ]]; then
  IMAGE="${IMAGE_REGISTRY}/${APP_NAME}:${IMAGE_TAG}"
else
  IMAGE="${APP_NAME}:${IMAGE_TAG}"
fi

echo "[1/3] Build jar ..."
if [[ "${SKIP_TESTS}" == "true" ]]; then
  ./mvnw clean package -DskipTests
else
  ./mvnw clean package
fi

echo "[2/3] Build docker image: ${IMAGE}"
docker build --build-arg JAR_FILE="target/${APP_NAME}-${APP_VERSION}.jar" -t "${IMAGE}" .

echo "[3/3] Done."
echo "IMAGE=${IMAGE}"
