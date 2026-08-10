CREATE TABLE users
(
    id         INT          NOT NULL AUTO_INCREMENT,
    login_id   VARCHAR(255) NOT NULL,
    password   VARCHAR(255) NOT NULL,
    created_at TIMESTAMP(6) NOT NULL,
    updated_at TIMESTAMP(6) NOT NULL,

    CONSTRAINT pk_users PRIMARY KEY (id),
    CONSTRAINT uk_users_login_id UNIQUE (login_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;


CREATE TABLE api_keys
(
    id          INT          NOT NULL AUTO_INCREMENT,
    user_id     INT          NOT NULL,
    private_key VARCHAR(255) NOT NULL,
    client_key  VARCHAR(255) NOT NULL,
    status      VARCHAR(20)  NOT NULL,
    created_at  TIMESTAMP(6) NOT NULL,
    updated_at  TIMESTAMP(6) NOT NULL,

    CONSTRAINT pk_api_keys PRIMARY KEY (id),
    CONSTRAINT uk_api_keys_client_key UNIQUE (client_key),
    CONSTRAINT fk_api_keys_user
        FOREIGN KEY (user_id)
            REFERENCES users (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;


CREATE TABLE payments
(
    id           INT          NOT NULL AUTO_INCREMENT,
    user_id      INT          NOT NULL,
    order_id     VARCHAR(255) NOT NULL,
    amount       INT          NOT NULL,
    status       VARCHAR(20)  NOT NULL,
    payment_key  VARCHAR(255) NOT NULL,
    requested_at TIMESTAMP(6) NOT NULL,
    approved_at  TIMESTAMP(6) NULL,
    canceled_at  TIMESTAMP(6) NULL,
    failed_at    TIMESTAMP(6) NULL,
    created_at   TIMESTAMP(6) NOT NULL,
    updated_at   TIMESTAMP(6) NOT NULL,

    CONSTRAINT pk_payments PRIMARY KEY (id),
    CONSTRAINT uk_payments_payment_key UNIQUE (payment_key),
    CONSTRAINT fk_payments_user
        FOREIGN KEY (user_id)
            REFERENCES users (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;
