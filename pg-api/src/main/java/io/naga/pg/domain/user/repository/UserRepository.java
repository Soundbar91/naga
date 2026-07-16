package io.naga.pg.domain.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import io.naga.pg.domain.user.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {

    boolean existsByLoginId(String loginId);

    Optional<User> findByLoginId(String loginId);
}
