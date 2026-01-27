# Project Charter: naga (Algorithm Judge API Server)

## 1. 프로젝트 개요
- **목표:** 알고리즘 문제 관리, 사용자 제출 기록 저장 및 조회를 위한 안정적인 RESTful API 서버 구축.
- **범위:** 문제(Problem) CRUD, 사용자(User) 관리, 제출(Submission) 메타데이터 관리. (채점 로직 제외)

## 2. 기술 스택 (Tech Stack)
- **Framework:** Spring Boot 3.4.2
- **Language:** Java 17 (Record, Text Blocks 활용)
- **Persistence:** Spring Data JPA, MySQL 8.0
- **Documentation:** SpringDoc OpenAPI (Swagger)
- **AI Tooling:** MCP context7을 사용하여 Spring Boot 3.4 최신 문서를 참조할 것.

## 3. 개발 및 설계 원칙
- **API 디자인:** RESTful 원칙을 준수하며, 명확한 자원(Resource) 경로를 사용한다.
- **DTO 전용 Record:** 모든 API 요청(Request)과 응답(Response)은 Java **Record**를 사용한다.
- **응답 포맷:** 공통 응답 객체(예: `ApiResponse<T>`)를 만들어 일관된 JSON 구조를 유지한다.
- **예외 처리:** `@RestControllerAdvice`를 통해 전역적으로 예외를 관리하고 의미 있는 에러 메시지를 반환한다.

## 4. 데이터베이스 및 엔티티
- **Soft Delete:** 데이터 삭제 시 실제로 지우지 않고 `is_deleted` 플래그를 사용하는 방식을 고려한다.
- **Audit:** 모든 엔티티에는 생성/수정 시간을 추적하는 `@CreatedDate`, `@LastModifiedDate`를 포함한다.
- **Validation:** Bean Validation(@NotNull, @Size 등)을 적극 사용하여 도메인 레이어 진입 전 데이터를 검증한다.

## 5. AI 에이전트 작업 지침
- 새로운 Controller 생성 시 반드시 Swagger 어노테이션을 추가하여 문서화를 병행하라.
- `context7`을 사용하여 `jakarta.persistence` 및 `spring-boot-starter-validation`의 최신 사용법을 확인하라.
- 복잡한 쿼리가 필요한 경우 Querydsl 도입 여부를 먼저 제안하라.

## 6. Git Workflow Rules
- **Atomic Commits:** 하나의 작업(예: 엔티티 생성, API 하나 구현)이 완료될 때마다 커밋을 수행하라.
- **Commit Message Format:** [Conventional Commits](https://www.conventionalcommits.org/) 스타일을 준수하라.
    - `feat:` 새로운 기능 추가
    - `fix:` 버그 수정
    - `docs:` 문서 수정
    - `refactor:` 코드 리팩토링
    - `test:` 테스트 코드 추가
- **Language:** 커밋 메시지는 한국어로 작성하라. 

## 7. Package Structure & Architecture
- **Layered Architecture:** `controller`, `service`, `repository`, `domain`, `dto`, `exception` 패키지 구조를 유지한다.
- **Domain-Driven Design (Lite):** 핵심 비즈니스 로직은 Service가 아닌 Domain Entity 내부에서 처리하도록 유도한다.

## 8. Testing & TDD Standards
- **Test-First Approach:** 새로운 기능을 구현할 때 반드시 실패하는 테스트 케이스를 먼저 작성하고 제출하라.
- **Testing Tools:** JUnit 5, AssertJ, Mockito를 기본 테스트 스택으로 사용한다.
- **Test Scope:** 
    - **Unit Tests:** 비즈니스 로직(Service, Domain)은 단위 테스트를 필수로 작성한다.
    - **Slice Tests:** Controller는 `@WebMvcTest`를 사용하여 API 스펙을 검증한다.
    - **Integration Tests:** 필요한 경우 `@SpringBootTest`를 사용하되, 테스트 속도를 위해 단위 테스트를 우선한다.
- **Red-Green-Refactor:** 
    1. 실패하는 테스트 작성 (Red)
    2. 테스트를 통과하는 최소한의 코드 작성 (Green)
    3. 코드 리팩토링 및 커밋 (Refactor) 단계를 엄격히 준수하라.
- **Assertion Style:** `assertThat()` 등 AssertJ의 유연한 단언문을 사용하여 가독성을 높여라.
- **TDD-Git Integration:** 
    1. 실패하는 테스트 작성 후 `test: (기능명) unit test fails (Red)` 커밋.
    2. 테스트 통과 코드 작성 후 `feat: (기능명) implementation (Green)` 커밋.
    3. 리팩토링 후 `refactor: (기능명) clean up (Refactor)` 커밋.