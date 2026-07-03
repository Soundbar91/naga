INSERT INTO users (id, login_id, password, created_at, updated_at)
WITH RECURSIVE numbers(n) AS (
    SELECT 1
    UNION ALL
    SELECT n + 1 FROM numbers WHERE n < 300
)
SELECT
    n,
    CONCAT('test-user-', RIGHT(CONCAT('000', n), 3)),
    '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
FROM numbers
ON DUPLICATE KEY UPDATE
    login_id = CONCAT('test-user-', RIGHT(CONCAT('000', id), 3)),
    password = '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
    updated_at = CURRENT_TIMESTAMP;

INSERT INTO products (id, name, price, quantity, status, created_at, updated_at)
VALUES (1, 'Concurrency Test Product', 10000, 100, 'SALE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON DUPLICATE KEY UPDATE
    name = 'Concurrency Test Product',
    price = 10000,
    quantity = 100,
    status = 'SALE',
    updated_at = CURRENT_TIMESTAMP;
