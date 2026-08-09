SET FOREIGN_KEY_CHECKS = 0;

TRUNCATE TABLE order_items;
TRUNCATE TABLE orders;
TRUNCATE TABLE products;
TRUNCATE TABLE users;

SET FOREIGN_KEY_CHECKS = 1;

INSERT INTO products (id, name, price, quantity, status, created_at, updated_at)
VALUES (1, 'k6 concurrency test product', 10000, 100, 'SALE', NOW(6), NOW(6));

INSERT INTO users (id, login_id, password, created_at, updated_at)
SELECT
    seq,
    CONCAT('user', LPAD(seq, 3, '0')),
    '$2b$12$zSOQ5QUEsR8ZrXGiO1Kvj.ByM8NETzX66zJ6wd4qINF9Rl1L0OLGi',
    NOW(6),
    NOW(6)
FROM (
    WITH RECURSIVE user_numbers(seq) AS (
        SELECT 1
        UNION ALL
        SELECT seq + 1 FROM user_numbers WHERE seq < 300
    )
    SELECT seq FROM user_numbers
) AS mock_users;
