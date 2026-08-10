#!/usr/bin/env bash

set -euo pipefail

IMAGE_TAG="${1:?사용법: $0 <image-tag>}"
DEPLOY_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

cd "${DEPLOY_DIR}"

sed -i "s/^COMMERCE_API_IMAGE_TAG=.*/COMMERCE_API_IMAGE_TAG=${IMAGE_TAG}/" .env

docker compose pull commerce-api

docker compose up --detach --no-build --wait --wait-timeout 180

echo "Commerce 배포 완료: ${IMAGE_TAG}"
