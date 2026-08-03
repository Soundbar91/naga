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

commerce-api의 `SERVER_PORT`는 `8080`, pg-api는 `8081`로 설정해야 현재 Prometheus 수집 대상과 일치합니다. 컨테이너 내부 DB 주소에는 각 Compose의 MySQL 서비스 이름을 사용합니다.

```text
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
