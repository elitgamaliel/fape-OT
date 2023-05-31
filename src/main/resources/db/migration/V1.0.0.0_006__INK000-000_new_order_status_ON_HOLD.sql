
DELETE FROM order_tracker_status
WHERE code IN ('ON_STORE', 'PICKING', 'PREPARED', 'SCHEDULED', 'TRANSFERRED');

INSERT INTO order_tracker_status (code, description)
VALUES ('ON_HOLD', 'Orden en espera');