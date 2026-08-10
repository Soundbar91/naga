#!/usr/bin/env bash

set -euo pipefail

DEPLOY_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

cd "${DEPLOY_DIR}"

docker compose pull
docker compose up --detach --wait --wait-timeout 180

echo "모니터링 배포 완료"
