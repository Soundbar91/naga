package io.naga.pg.domain.apikey.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import io.naga.pg.domain.apikey.model.ApiKey;
import io.naga.pg.domain.apikey.model.ApiKeyStatus;

public interface ApiKeyRepository extends JpaRepository<ApiKey, Integer> {

    boolean existsByUserIdAndStatus(Integer userId, ApiKeyStatus status);

    Optional<ApiKey> findByIdAndUserId(Integer id, Integer userId);

    List<ApiKey> findAllByUserIdOrderByCreatedAtDescIdDesc(Integer userId);

    List<ApiKey> findAllByUserIdAndStatusOrderByCreatedAtDescIdDesc(Integer userId, ApiKeyStatus status);
}
