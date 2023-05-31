ALTER TABLE `order_distance_audit`
ADD COLUMN `user_gps` TINYINT NULL DEFAULT 0 AFTER `user_longitude`;