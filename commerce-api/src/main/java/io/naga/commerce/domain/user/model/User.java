package io.naga.commerce.domain.user.model;

import static lombok.AccessLevel.PROTECTED;

import io.naga.commerce.global.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "users")
@NoArgsConstructor(access = PROTECTED)
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @Column(name = "login_id", nullable = false, unique = true)
    private String loginId;

    @NotNull
    @Column(name = "password", nullable = false)
    private String password;

    @Builder
    private User(String loginId, String password) {
        this.loginId = loginId;
        this.password = password;
    }

    public static User create(String loginId, String password) {
        return User.builder()
            .loginId(loginId)
            .password(password)
            .build();
    }
}
