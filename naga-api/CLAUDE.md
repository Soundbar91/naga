# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

# Project Charter: naga (Algorithm Judge API Server)

## 1. 프로젝트 개요
- **목표:** 알고리즘 문제 관리, 사용자 제출 기록 저장 및 조회를 위한 안정적인 RESTful API 서버 구축.
- **범위:** 문제(Problem) CRUD, 사용자(User) 관리, 제출(Submission) 메타데이터 관리. (채점 로직 제외)
- **Base Package:** `io.soundbar91.naga`

## 2. 기술 스택 (Tech Stack)
- **Framework:** Spring Boot 3.4.2
- **Language:** Java 17 (Record, Text Blocks 활용)
- **Build Tool:** Gradle 9.3.0
- **Persistence:** Spring Data JPA, MySQL 8.0 (production), H2 (development/test)
- **Documentation:** SpringDoc OpenAPI (Swagger) 2.8.5
- **AI Tooling:** MCP context7을 사용하여 Spring Boot 3.4 최신 문서를 참조할 것.

## 3. 개발 명령어 (Development Commands)

### 빌드 및 실행
```bash
# 프로젝트 빌드
./gradlew build

# 애플리케이션 실행
./gradlew bootRun

# 빌드 결과물로 실행
java -jar build/libs/naga-0.0.1-SNAPSHOT.jar
```

### 테스트
```bash
# 전체 테스트 실행
./gradlew test

# 특정 테스트 클래스 실행
./gradlew test --tests "io.soundbar91.naga.NagaApplicationTests"

# 특정 테스트 메서드 실행
./gradlew test --tests "io.soundbar91.naga.service.ProblemServiceTest.should_create_problem"

# 테스트 리포트 확인: build/reports/tests/test/index.html
```

### 의존성 및 빌드 관리
```bash
# 의존성 확인
./gradlew dependencies

# 빌드 캐시 정리
./gradlew clean

# 프로젝트 정보 확인
./gradlew properties
```

## 4. 개발 및 설계 원칙
- **API 디자인:** RESTful 원칙을 준수하며, 명확한 자원(Resource) 경로를 사용한다.
- **DTO 전용 Record:** 모든 API 요청(Request)과 응답(Response)은 Java **Record**를 사용한다.
- **응답 포맷:** 공통 응답 객체(예: `ApiResponse<T>`)를 만들어 일관된 JSON 구조를 유지한다.
- **예외 처리:** `@RestControllerAdvice`를 통해 전역적으로 예외를 관리하고 의미 있는 에러 메시지를 반환한다.

## 5. 데이터베이스 및 엔티티
- **Entity 생성:** 엔티티는 **정적 팩토리 메서드**를 사용하여 생성한다. Builder 패턴 대신 명확한 의도를 드러내는 정적 팩토리 메서드(예: `User.create(email, password)`)를 사용한다.
- **Soft Delete:** 데이터 삭제 시 실제로 지우지 않고 `is_deleted` 플래그를 사용하는 방식을 고려한다.
- **Audit:** 모든 엔티티에는 생성/수정 시간을 추적하는 `@CreatedDate`, `@LastModifiedDate`를 포함한다.
- **Validation:** Bean Validation(@NotNull, @Size 등)을 적극 사용하여 도메인 레이어 진입 전 데이터를 검증한다.

## 6. Lombok 사용 가이드
- **Entity 클래스:** `@Getter`를 사용하여 Getter 메서드를 자동 생성한다. Setter는 필요한 경우에만 명시적으로 작성한다.
- **DTO/Record:** API 요청/응답은 **Record**를 우선 사용하며, Lombok은 사용하지 않는다.
- **주의사항:**
  - `@Data`는 Entity에 사용하지 않는다. (양방향 연관관계 시 `toString()`, `hashCode()` 이슈)
  - `@Builder`는 Entity에 사용하지 않는다. 대신 정적 팩토리 메서드를 사용한다.
  - `@NoArgsConstructor`, `@AllArgsConstructor`는 필요한 경우에만 명시적으로 사용한다.
  - JPA Entity는 `@NoArgsConstructor(access = AccessLevel.PROTECTED)`를 사용하여 무분별한 객체 생성을 방지한다.

## 7. AI 에이전트 작업 지침
- 새로운 Controller 생성 시 반드시 Swagger 어노테이션을 추가하여 문서화를 병행하라.
- `context7`을 사용하여 `jakarta.persistence` 및 `spring-boot-starter-validation`의 최신 사용법을 확인하라.
- 복잡한 쿼리가 필요한 경우 Querydsl 도입 여부를 먼저 제안하라.

## 7. Git Workflow Rules
- **Atomic Commits:** 하나의 작업(예: 엔티티 생성, API 하나 구현)이 완료될 때마다 커밋을 수행하라.
- **Commit Message Format:** [Conventional Commits](https://www.conventionalcommits.org/) 스타일을 준수하라.
    - `feat:` 새로운 기능 추가
    - `fix:` 버그 수정
    - `docs:` 문서 수정
    - `refactor:` 코드 리팩토링
    - `test:` 테스트 코드 추가
- **Language:** 커밋 메시지는 한국어로 작성하라. 

## 8. Package Structure & Architecture
- **Layered Architecture:** `controller`, `service`, `repository`, `domain`, `dto`, `exception` 패키지 구조를 유지한다.
- **Domain-Driven Design (Lite):** 핵심 비즈니스 로직은 Service가 아닌 Domain Entity 내부에서 처리하도록 유도한다.

## 9. Testing & TDD Standards
- **TDD 적용 범위:** **Service 레이어에 대해서만** TDD를 적용한다. Entity, Repository, Controller는 구현 후 필요 시 테스트를 작성한다.
- **Test-First Approach (Service Only):** Service 레이어의 새로운 기능을 구현할 때 반드시 실패하는 테스트 케이스를 먼저 작성하고 제출하라.
- **Testing Tools:** JUnit 5, AssertJ, Mockito를 기본 테스트 스택으로 사용한다.
- **Test Scope:**
    - **Unit Tests:** Service 레이어는 TDD를 적용하여 단위 테스트를 필수로 작성한다.
    - **Slice Tests:** Controller는 `@WebMvcTest`를 사용하여 API 스펙을 검증한다.
    - **Integration Tests:** 필요한 경우 `@SpringBootTest`를 사용하되, 테스트 속도를 위해 단위 테스트를 우선한다.
- **Red-Green-Refactor (Service Only):**
    1. 실패하는 테스트 작성 (Red)
    2. 테스트를 통과하는 최소한의 코드 작성 (Green)
    3. 코드 리팩토링 및 커밋 (Refactor) 단계를 엄격히 준수하라.
- **Assertion Style:** `assertThat()` 등 AssertJ의 유연한 단언문을 사용하여 가독성을 높여라.
- **TDD-Git Integration (Service Only):**
    1. 실패하는 테스트 작성 후 `test: (기능명) unit test fails (Red)` 커밋.
    2. 테스트 통과 코드 작성 후 `feat: (기능명) implementation (Green)` 커밋.
    3. 리팩토링 후 `refactor: (기능명) clean up (Refactor)` 커밋.

## 10. API 문서 및 Swagger
- **Swagger UI 접근:** 애플리케이션 실행 후 `http://localhost:8080/swagger-ui.html` 접속
- **OpenAPI JSON:** `http://localhost:8080/v3/api-docs`
- Controller 작성 시 `@Tag`, `@Operation`, `@ApiResponse` 어노테이션을 활용하여 명확한 API 문서를 작성한다.
