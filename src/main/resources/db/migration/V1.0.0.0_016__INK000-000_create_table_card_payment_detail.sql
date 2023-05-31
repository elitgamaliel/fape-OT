--
-- Table structure for table `cardPaymentDetail`
--
DROP TABLE IF EXISTS `card_payment_detail`;
CREATE TABLE `card_payment_detail` (
      `id` int(11) NOT NULL AUTO_INCREMENT,
      `order_id` varchar(250) DEFAULT NULL,
      `cod_local` varchar(250) DEFAULT NULL,
      `transaction_result` varchar(250) DEFAULT NULL,
      `card_bin` varchar(250) DEFAULT NULL,
      `card_brand` varchar(250) DEFAULT NULL,
      `currency` varchar(250) DEFAULT NULL,
      `amount` decimal(10,2)  DEFAULT NULL,
      `installments` int(11) DEFAULT NULL,
      `transaction_id` varchar(250) DEFAULT NULL,
      `authorization_code` varchar(250) DEFAULT NULL,
      `purcher_number` varchar(250) DEFAULT NULL,
      `transaction_date` varchar(250) DEFAULT NULL,
      `transaction_time` varchar(250) DEFAULT NULL,
      `createdBy` varchar(250) DEFAULT NULL,
      `dateCreated` varchar(250) DEFAULT NULL,
      `lastUpdateBy` varchar(250) DEFAULT NULL,
      `dateLastUpdated` varchar(250) DEFAULT NULL,
       PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;
