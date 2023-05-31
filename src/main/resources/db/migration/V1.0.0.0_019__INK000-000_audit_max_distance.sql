--
-- Table structure for table `order_distance_audit`
--
DROP TABLE IF EXISTS `order_distance_audit`;
CREATE TABLE `order_distance_audit` (
    `id` BIGINT(20) NOT NULL,
    `user` VARCHAR(40) NULL,
    `user_latitude` DECIMAL(10,8) NULL,
    `user_longitude` DECIMAL(10,8) NULL,
    `distance` VARCHAR(45) NULL,
    `ecommerceId` BIGINT(20) NOT NULL,
    `tracking_code` varchar(20) NULL,
    `delivery_latitude` DECIMAL(10,8) NULL,
    `delivery_longitude` DECIMAL(10,8) NULL,
    `create_date` DATETIME NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;;
