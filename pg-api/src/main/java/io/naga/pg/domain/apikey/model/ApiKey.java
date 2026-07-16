package io.naga.pg.domain.apikey.model;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

import io.naga.pg.domain.user.model.User;
import io.naga.pg.global.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "api_keys")
@NoArgsConstructor(access = PROTECTED)
public class ApiKey extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "public_key", length = 255, unique = true)
    private String publicKey;

    @Column(name = "private_key", length = 255)
    private String privateKey;

    @Enumerated(STRING)
    @Column(name = "status")
    private ApiKeyStatus status;

    @Builder
    private ApiKey(User user, String publicKey, String privateKey, ApiKeyStatus status) {
        this.user = user;
        this.publicKey = publicKey;
        this.privateKey = privateKey;
        this.status = status;
    }

    public static ApiKey create(User user, String publicKey, String privateKey) {
        return ApiKey.builder()
            .user(user)
            .publicKey(publicKey)
            .privateKey(privateKey)
            .status(ApiKeyStatus.ACTIVE)
            .build();
    }

    public void deactivate() {
        this.status = ApiKeyStatus.INACTIVE;
    }

    public boolean isInactive() {
        return status == ApiKeyStatus.INACTIVE;
    }
}
