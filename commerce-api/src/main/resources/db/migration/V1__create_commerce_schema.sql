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


CREATE TABLE products
(
    id         INT          NOT NULL AUTO_INCREMENT,
    name       VARCHAR(255) NOT NULL,
    price      INT          NOT NULL,
    quantity   INT          NOT NULL,
    status     VARCHAR(255) NOT NULL,
    created_at TIMESTAMP(6) NOT NULL,
    updated_at TIMESTAMP(6) NOT NULL,

    CONSTRAINT pk_products PRIMARY KEY (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;


CREATE TABLE orders
(
    id          INT          NOT NULL AUTO_INCREMENT,
    user_id     INT          NOT NULL,
    order_key   VARCHAR(255) NOT NULL,
    total_price INT          NOT NULL,
    status      VARCHAR(255) NOT NULL,
    created_at  TIMESTAMP(6) NOT NULL,
    updated_at  TIMESTAMP(6) NOT NULL,

    CONSTRAINT pk_orders PRIMARY KEY (id),
    CONSTRAINT uk_orders_order_key UNIQUE (order_key),
    CONSTRAINT fk_orders_user
        FOREIGN KEY (user_id)
            REFERENCES users (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;


CREATE TABLE order_items
(
    id         INT          NOT NULL AUTO_INCREMENT,
    order_id   INT          NOT NULL,
    product_id INT          NOT NULL,
    price      INT          NOT NULL,
    quantity   INT          NOT NULL,
    created_at TIMESTAMP(6) NOT NULL,
    updated_at TIMESTAMP(6) NOT NULL,

    CONSTRAINT pk_order_items PRIMARY KEY (id),
    CONSTRAINT fk_order_items_order
        FOREIGN KEY (order_id)
            REFERENCES orders (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT,
    CONSTRAINT fk_order_items_product
        FOREIGN KEY (product_id)
            REFERENCES products (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;


CREATE TABLE payments
(
    id           INT          NOT NULL AUTO_INCREMENT,
    order_id     INT          NOT NULL,
    payment_key  VARCHAR(255) NOT NULL,
    amount       INT          NOT NULL,
    status       VARCHAR(255) NOT NULL,
    approved_at  TIMESTAMP(6) NOT NULL,
    created_at   TIMESTAMP(6) NOT NULL,
    updated_at   TIMESTAMP(6) NOT NULL,

    CONSTRAINT pk_payments PRIMARY KEY (id),
    CONSTRAINT uk_payments_order_id UNIQUE (order_id),
    CONSTRAINT uk_payments_payment_key UNIQUE (payment_key),
    CONSTRAINT fk_payments_order
        FOREIGN KEY (order_id)
            REFERENCES orders (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;
