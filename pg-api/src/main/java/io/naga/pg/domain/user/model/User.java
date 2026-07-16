package io.naga.pg.domain.user.model;

import static lombok.AccessLevel.PROTECTED;

import io.naga.pg.global.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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

    @Column(name = "login_id", length = 255, unique = true)
    private String loginId;

    @Column(name = "password", length = 255)
    private String password;

    @Builder
    private User(String loginId, String password) {
        this.loginId = loginId;
        this.password = password;
    }
}
