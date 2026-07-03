SET FOREIGN_KEY_CHECKS = 0;

TRUNCATE TABLE order_items;
TRUNCATE TABLE orders;
TRUNCATE TABLE products;
TRUNCATE TABLE users;

SET FOREIGN_KEY_CHECKS = 1;

INSERT INTO users (id, login_id, password, created_at, updated_at)
WITH RECURSIVE numbers(n) AS (
    SELECT 1
    UNION ALL
    SELECT n + 1 FROM numbers WHERE n < 1000
)
SELECT
    n,
    CONCAT('test-user-', RIGHT(CONCAT('000', n), 3)),
    '$2b$10$HUvN9HDxKr.tClXZ7gfEI.fsGeYqU9zGYpkPG2fEvEdlNyQd6Iyi.',
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
FROM numbers;

INSERT INTO products (id, name, price, quantity, status, created_at, updated_at)
VALUES (1, 'Concurrency Test Product', 10000, 100, 'SALE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
