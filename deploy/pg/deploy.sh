#!/usr/bin/env bash

set -euo pipefail

IMAGE_TAG="${1:?사용법: $0 <image-tag>}"
DEPLOY_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

cd "${DEPLOY_DIR}"

sed -i "s/^PG_API_IMAGE_TAG=.*/PG_API_IMAGE_TAG=${IMAGE_TAG}/" .env

docker compose pull pg-api

docker compose up --detach --no-build --wait --wait-timeout 180

echo "PG 배포 완료: ${IMAGE_TAG}"
