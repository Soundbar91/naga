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

## 실행

서비스는 각 배포 디렉터리에서 독립적으로 빌드하고 실행합니다.

```shell
cd deploy/commerce-api
docker compose build commerce-api
docker compose up -d
```

```shell
cd deploy/pg-api
docker compose build pg-api
docker compose up -d
```

WAS만 새 이미지로 갱신할 때는 다른 서비스의 의존성을 다시 생성하지 않습니다.

```shell
docker compose up -d --no-deps --build commerce-api
docker compose up -d --no-deps --build pg-api
```

## GitHub Actions 배포

`main` 브랜치에 push하면 `.github/workflows/deploy.yml`이 다음 순서로 배포합니다.

1. commerce-api와 pg-api 이미지를 GHCR에 push
2. 서버의 Compose 파일 갱신
3. 커밋 SHA 태그 이미지로 컨테이너 교체
4. Actuator health 확인

GitHub의 `production` Environment에 다음 Secret을 등록합니다.

```text
SSH_HOST
SSH_PORT
SSH_USER
SSH_PRIVATE_KEY
SSH_KNOWN_HOSTS
```

`SSH_KNOWN_HOSTS`는 서버의 SSH host key를 검증한 후 등록합니다. 배포 사용자는 비대화형 환경에서 `sudo -n docker compose`와 Compose 파일 설치를 실행할 수 있어야 하며, 서버에 `curl`이 설치되어 있어야 합니다.

서버의 환경변수 파일은 배포 중 덮어쓰지 않습니다.

```text
/opt/naga/commerce/deploy/.env
/opt/naga/pg/deploy/.env
```

GHCR 패키지는 최초 push 후 `Public`으로 변경해야 서버에서 인증 없이 pull할 수 있습니다. 최초 배포가 패키지 공개 전에 실행되어 실패하면 두 패키지를 공개한 뒤 워크플로를 재실행합니다.
