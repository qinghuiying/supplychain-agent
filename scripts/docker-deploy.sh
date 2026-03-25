#!/usr/bin/env bash

set -euo pipefail

APP_NAME="${APP_NAME:-supplychain-agent}"
IMAGE_REGISTRY="${IMAGE_REGISTRY:-}"
IMAGE_TAG="${IMAGE_TAG:-$(date +%Y%m%d%H%M%S)}"
NAMESPACE="${NAMESPACE:-default}"
K8S_FILE="${K8S_FILE:-scripts/k8s-deploy.yaml}"

if [[ -z "${IMAGE_REGISTRY}" ]]; then
  echo "ERROR: IMAGE_REGISTRY is required, e.g. registry.cn-hangzhou.aliyuncs.com/your-namespace"
  exit 1
fi

IMAGE="${IMAGE_REGISTRY}/${APP_NAME}:${IMAGE_TAG}"

echo "[1/4] Build docker image"
APP_NAME="${APP_NAME}" IMAGE_REGISTRY="${IMAGE_REGISTRY}" IMAGE_TAG="${IMAGE_TAG}" ./scripts/docker-build.sh

echo "[2/4] Push docker image: ${IMAGE}"
docker push "${IMAGE}"

TMP_FILE="$(mktemp)"
trap 'rm -f "${TMP_FILE}"' EXIT
sed "s|IMAGE_PLACEHOLDER|${IMAGE}|g" "${K8S_FILE}" > "${TMP_FILE}"

echo "[3/4] Apply k8s yaml to namespace: ${NAMESPACE}"
kubectl apply -n "${NAMESPACE}" -f "${TMP_FILE}"

echo "[4/4] Rollout status"
kubectl rollout status deployment/"${APP_NAME}" -n "${NAMESPACE}" --timeout=180s

echo "Deploy complete: ${IMAGE}"
