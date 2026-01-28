# JPA Auditing

## 개념

**JPA Auditing**은 엔티티의 생성 및 수정 이력을 자동으로 추적하는 Spring Data JPA의 기능입니다.

다음과 같은 정보를 자동으로 관리할 수 있습니다:
- **언제** 생성되었는지 (`@CreatedDate`)
- **언제** 수정되었는지 (`@LastModifiedDate`)
- **누가** 생성했는지 (`@CreatedBy`) - 선택사항
- **누가** 수정했는지 (`@LastModifiedBy`) - 선택사항

## 사용 이유

### 1. 자동화
개발자가 매번 수동으로 생성/수정 시간을 설정할 필요가 없습니다.

**Auditing 없이:**
```java
public void createProblem(Problem problem) {
    problem.setCreatedAt(LocalDateTime.now());  // 매번 수동 설정 필요
    problem.setUpdatedAt(LocalDateTime.now());
    repository.save(problem);
}

public void updateProblem(Problem problem) {
    problem.setUpdatedAt(LocalDateTime.now());  // 깜빡하면 누락
    repository.save(problem);
}
```

**Auditing 사용:**
```java
public void createProblem(Problem problem) {
    repository.save(problem);  // createdAt, updatedAt 자동 설정
}

public void updateProblem(Problem problem) {
    repository.save(problem);  // updatedAt 자동 갱신
}
```

### 2. 일관성
모든 엔티티에서 동일한 방식으로 이력을 추적하여 일관성을 보장합니다.

### 3. 실수 방지
개발자가 시간을 설정하지 않는 실수를 방지하고, 비즈니스 로직에 집중할 수 있습니다.

## 사용 방법

### 1. Auditing 활성화

`@EnableJpaAuditing` 어노테이션을 사용하여 Auditing 기능을 활성화합니다.

```java
@Configuration
@EnableJpaAuditing
public class JpaAuditingConfiguration {
}
```

### 2. BaseEntity 작성

공통 Auditing 필드를 관리하는 추상 클래스를 작성합니다.

```java
@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    public boolean isDeleted() {
        return deletedAt != null;
    }

    public void softDelete() {
        this.deletedAt = LocalDateTime.now();
    }

    public void restore() {
        this.deletedAt = null;
    }
}
```

### 3. 엔티티에 적용

```java
@Entity
@Table(name = "problems")
public class Problem extends BaseEntity {
    private String title;
    private String description;
    // ...
}
```

### 4. Auditing 어노테이션

| 어노테이션 | 설정 시점 | 갱신 여부 | Column 설정 권장사항 |
|-----------|----------|----------|-------------------|
| `@CreatedDate` | 최초 저장 시 | 갱신 안 됨 | `updatable = false` |
| `@LastModifiedDate` | 최초 저장 + 수정 시 | 매번 갱신 | - |
| `@CreatedBy` | 최초 저장 시 | 갱신 안 됨 | `updatable = false` |
| `@LastModifiedBy` | 최초 저장 + 수정 시 | 매번 갱신 | - |

### 5. 생성자/수정자 추적 (선택사항)

사용자 인증 기능이 있는 경우, 누가 작업했는지도 추적할 수 있습니다.

```java
@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {
    // ... 기존 필드들

    @CreatedBy
    @Column(name = "created_by", updatable = false)
    private String createdBy;

    @LastModifiedBy
    @Column(name = "modified_by")
    private String modifiedBy;
}
```

`AuditorAware` 인터페이스를 구현하여 현재 사용자 정보를 제공합니다:

```java
@Configuration
@EnableJpaAuditing
public class JpaAuditingConfiguration {

    @Bean
    public AuditorAware<String> auditorProvider() {
        return () -> {
            // Spring Security 사용 시:
            Authentication auth = SecurityContextHolder
                .getContext()
                .getAuthentication();

            if (auth == null || !auth.isAuthenticated()) {
                return Optional.empty();
            }

            return Optional.of(auth.getName());
        };
    }
}
```

## 주의사항

### 1. EntityListeners 등록 필수

`@EntityListeners(AuditingEntityListener.class)` 어노테이션을 반드시 추가해야 Auditing이 동작합니다.

```java
@EntityListeners(AuditingEntityListener.class)  // 필수!
public abstract class BaseEntity { ... }
```

### 2. @Column 설정 권장

명시적인 설정으로 의도를 명확히 합니다:

```java
@CreatedDate
@Column(name = "created_at", nullable = false, updatable = false)
private LocalDateTime createdAt;

@LastModifiedDate
@Column(name = "updated_at", nullable = false)
private LocalDateTime updatedAt;
```

### 3. 벌크 연산에서는 동작하지 않음

JPQL이나 Criteria API를 사용한 벌크 연산에서는 Auditing이 동작하지 않습니다.

```java
// ❌ Auditing 동작하지 않음
@Query("UPDATE Problem p SET p.title = :title WHERE p.id = :id")
void bulkUpdate(@Param("id") Long id, @Param("title") String title);

// ✅ Auditing 동작함
Problem problem = problemRepository.findById(id).get();
problem.setTitle(title);
problemRepository.save(problem);
```

### 4. @EnableJpaAuditing 위치

`@EnableJpaAuditing`은 반드시 `@Configuration` 클래스에 선언해야 합니다.

### 5. 테스트 환경 고려

테스트 환경에서도 Auditing 기능을 사용하려면 테스트 설정에서도 `@EnableJpaAuditing`을 활성화해야 합니다.

```java
@DataJpaTest
@Import(JpaAuditingConfiguration.class)  // 테스트에서 Auditing 활성화
class ProblemRepositoryTest {
    // ...
}
```

## 동작 원리

```
1. 엔티티 저장/수정 요청
         ↓
2. JPA가 영속성 컨텍스트에 변경 감지
         ↓
3. AuditingEntityListener가 이벤트 감지
         ↓
4. @PrePersist (저장 전) 또는 @PreUpdate (수정 전) 실행
         ↓
5. @CreatedDate/@LastModifiedDate 필드에 현재 시간 자동 주입
         ↓
6. 실제 DB 저장
```

## 실제 동작 예시

```java
// 1. Problem 엔티티 생성
Problem problem = new Problem("알고리즘 문제");
problemRepository.save(problem);

// DB 저장 결과:
// - id: 1
// - created_at: 2026-01-28 14:30:00 (자동 설정)
// - updated_at: 2026-01-28 14:30:00 (자동 설정)
// - deleted_at: null

Thread.sleep(5000);  // 5초 대기

// 2. Problem 수정
problem.setTitle("수정된 제목");
problemRepository.save(problem);

// DB 업데이트 결과:
// - id: 1
// - created_at: 2026-01-28 14:30:00 (변경 안 됨)
// - updated_at: 2026-01-28 14:30:05 (자동 갱신 ✅)
// - deleted_at: null
```