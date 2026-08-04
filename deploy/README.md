# 컨테이너 배포

## 공용 네트워크

API와 모니터링 스택이 통신할 외부 네트워크를 최초 한 번 생성합니다.

```shell
docker network inspect naga-network >/dev/null 2>&1 || docker network create naga-network
```

## 환경변수

각 서비스의 배포 디렉터리에서 예제 파일을 복사하고 모든 값을 입력합니다.

```shell
cp deploy/commerce-api/.env.example deploy/commerce-api/.env
cp deploy/pg-api/.env.example deploy/pg-api/.env
```

commerce-api의 `SERVER_PORT`는 `8080`, pg-api는 `8081`로 설정해야 현재 Prometheus 수집 대상과 일치합니다. 호스트에 공개할 포트는 `SERVER_HOST_PORT`로 별도 설정합니다. 컨테이너 내부 DB 주소에는 각 Compose의 MySQL 서비스 이름을 사용합니다.

```text
SERVER_HOST_PORT=18080 # commerce-api
SERVER_PORT=8080
SERVER_HOST_PORT=18081 # pg-api
SERVER_PORT=8081
DB_URL=jdbc:mysql://mysql:3306/<database>
PG_API_BASE_URL=http://pg-api:8081
```

## 서버 준비

Compose 파일과 환경변수 파일은 서버에서 관리합니다. `.env`에는 애플리케이션 실행 환경변수를 입력하며 `IMAGE_NAME`과 `IMAGE_TAG`는 서버의 배포 스크립트가 실행 시 설정합니다.

`/opt/naga/deploy.sh`는 이 프로젝트에 포함하지 않고 서버에서 별도로 관리합니다. Git 커밋 SHA와 GHCR 이미지 네임스페이스를 인자로 받아 이미지 pull, 컨테이너 교체, Actuator health 확인을 수행해야 합니다.

```shell
sudo /opt/naga/deploy.sh \
  0123456789abcdef0123456789abcdef01234567 \
  ghcr.io/<github-owner>
```

## GitHub Actions 배포

`main` 브랜치에 push하면 `.github/workflows/deploy.yml`이 다음 순서로 배포합니다.

1. commerce-api와 pg-api 이미지를 GHCR에 push
2. SSH로 서버에 접속
3. `/opt/naga/deploy.sh`에 커밋 SHA 태그와 이미지 네임스페이스 전달
4. 서버에서 이미지 pull, 컨테이너 교체, Actuator health 확인

GitHub의 `production` Environment에 다음 Secret을 등록합니다.

```text
SSH_HOST
SSH_PORT
SSH_USER
SSH_PRIVATE_KEY
SSH_HOST_FINGERPRINT
MAIN_DEPLOY_SCRIPT_PATH
```

`SSH_HOST_FINGERPRINT`에는 서버 SSH host key의 SHA256 fingerprint를 검증한 후 등록하고, `MAIN_DEPLOY_SCRIPT_PATH`에는 서버에서 관리하는 배포 스크립트의 절대 경로를 등록합니다. 배포 사용자는 해당 스크립트만 비대화형으로 root 실행할 수 있도록 sudoers를 설정합니다. 스크립트와 Compose 파일은 root만 수정할 수 있어야 하며 서버에는 Docker Compose와 `curl`이 설치되어 있어야 합니다.

```sudoers
<deploy-user> ALL=(root) NOPASSWD: <main-deploy-script-path> *
```

서버의 환경변수 파일은 배포 중 덮어쓰지 않습니다.

```text
/opt/naga/commerce/deploy/.env
/opt/naga/pg/deploy/.env
```

GHCR 패키지는 최초 push 후 `Public`으로 변경해야 서버에서 인증 없이 pull할 수 있습니다. 비공개 패키지를 유지하려면 root 계정의 Docker 설정에 `read:packages` 권한으로 GHCR 로그인을 먼저 구성합니다. 최초 배포가 패키지 공개 또는 서버 인증 전에 실행되어 실패하면 설정을 마친 뒤 워크플로를 재실행합니다.
