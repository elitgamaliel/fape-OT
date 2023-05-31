--
-- Table structure for table `motorized_type`
--
DROP TABLE IF EXISTS `motorized_type`;
CREATE TABLE `motorized_type` (
  `code` varchar(20) NOT NULL,
  `description` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
--
-- Dumping data for table `motorized_type`
--
LOCK TABLES `motorized_type` WRITE;
INSERT INTO `motorized_type` VALUES ('DELIVERY_CENTER','Motorizado de DC'),('DRUGSTORE','Motorizado de botica');
UNLOCK TABLES;

--
-- Table structure for table `status`
--
DROP TABLE IF EXISTS `status`;
CREATE TABLE `status` (
  `type` varchar(10) NOT NULL,
  `code` varchar(50) NOT NULL,
  `description` varchar(200) NOT NULL,
  PRIMARY KEY (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
--
-- Dumping data for table `status`
--
LOCK TABLES `status` WRITE;
INSERT INTO `status` VALUES ('TRACKING','ACCIDENT','Motorizado está con percances'),('TRACKING','ARRIVED','Motorizado llegó con la orden'),('TRACKING','ASSIGNED','Orden asignada al motorizado'),('USER','DELETED','Usuario que a sido borrado de forma logica'),('TRACKING','DELIVERED','Motorizado entregó la orden'),('USER','DISABLED','Usuario desactivado'),('USER','ENABLED','Usuario activo'),('TRACKING','IN_BREAK','Motorizado está descansando'),('TRACKING','OFFLINE','Motorizado está desconectado'),('TRACKING','ONLINE','Motorizado está conectado'),('TRACKING','ON_ROUTE','Motorizado en camino'),('TRACKING','PACKING','Motorizado con ordenes asignadas'),('TRACKING','RETURNING','Motorizado está retornando'),('TRACKING','WAITING','Motorizado está en el centro de distribución');
UNLOCK TABLES;

--
-- Table structure for table `transport_type`
--
DROP TABLE IF EXISTS `transport_type`;
CREATE TABLE `transport_type` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) DEFAULT NULL,
  `description` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
--
-- Dumping data for table `transport_type`
--
LOCK TABLES `transport_type` WRITE;
INSERT INTO `transport_type` VALUES (1,'moto',NULL),(2,'carro',NULL);
UNLOCK TABLES;

--
-- Table structure for table `transport`
--
DROP TABLE IF EXISTS `transport`;
CREATE TABLE `transport` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `license_plate` varchar(20) DEFAULT NULL,
  `color` varchar(50) DEFAULT NULL,
  `description` varchar(500) DEFAULT NULL,
  `transport_type_id` bigint(20) DEFAULT NULL,
  `created_by` varchar(100) DEFAULT NULL,
  `date_created` datetime DEFAULT NULL,
  `last_updated_by` varchar(100) DEFAULT NULL,
  `date_last_updated` datetime DEFAULT NULL,
  `version` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `transportTypeId` (`transport_type_id`),
  CONSTRAINT `transport_ibfk_1` FOREIGN KEY (`transport_type_id`) REFERENCES `transport_type` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

--
-- Table structure for table `user`
--
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` varchar(40)  NOT NULL,
  `employee_code` varchar(20) DEFAULT NULL,
  `user_status` varchar(50) DEFAULT NULL,
  `dni` varchar(15) DEFAULT NULL,
  `phone` varchar(30) DEFAULT NULL,
  `email` varchar(200) NOT NULL,
  `first_name` varchar(200) DEFAULT NULL,
  `last_name` varchar(200) DEFAULT NULL,
  `alias` varchar(200) DEFAULT NULL,
  `url_photo` varchar(300) DEFAULT NULL,
  `type` varchar(20) DEFAULT NULL,
  `drugstore_group_id` int(11) DEFAULT NULL,
  `drugstore_id` bigint(20) DEFAULT NULL,
  `transport_id` bigint(20) DEFAULT NULL,
  `tracking_status` varchar(50) DEFAULT NULL,
  `end_break_status_date` datetime DEFAULT NULL,
  `created_by` varchar(100) DEFAULT NULL,
  `date_created` datetime DEFAULT NULL,
  `last_updated_by` varchar(100) DEFAULT NULL,
  `date_last_updated` datetime DEFAULT NULL,
  `version` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `transportId` (`transport_id`),
  KEY `trackeruser_ibfk_2` (`user_status`),
  KEY `trackingStatus` (`tracking_status`),
  KEY `type` (`type`),
  CONSTRAINT `motorized_type_user_fk` FOREIGN KEY (`type`) REFERENCES `motorized_type` (`code`),
  CONSTRAINT `user_ibfk_1` FOREIGN KEY (`transport_id`) REFERENCES `transport` (`id`),
  CONSTRAINT `user_ibfk_2` FOREIGN KEY (`user_status`) REFERENCES `status` (`code`),
  CONSTRAINT `user_ibfk_3` FOREIGN KEY (`tracking_status`) REFERENCES `status` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Table structure for table `application_parameter`
--
DROP TABLE IF EXISTS `application_parameter`;
CREATE TABLE `application_parameter` (
  `code` varchar(50) NOT NULL,
  `description` varchar(100) DEFAULT NULL,
  `value` varchar(500) DEFAULT NULL,
  `version` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `application_parameter` WRITE;
INSERT INTO `application_parameter` VALUES ('ADMINISTRATOR_REPORTING_DAYS','Dias anteriores para el reporte de ordenes del administrador','30',0),('ALERT_DELIVERY_TIME','Tiempo en minutos para alertar finalización de promesa','60',0),('ARRIVED_STATUS_VALIDATED','Flag para activar la validación de la distancia para el estado ARRIVED (Y/N)','Y',2),('BREAK_TIME','Tiempo de descanso en minutos de motorizado','60',2),('CANCEL_ALARM_TIME','Tiempo(en minutos) de vigencia de la alarma de cancelada','5',0),('COORDINATOR_REPORTING_DAYS','Dias anteriores para el reporte de ordenes del coordinador','30',0),('CUSTOMER_DELAY_TIME','Tiempo en minutos que el motorizado demora en entregar el pedido','7',0),('DELIVERY_DISTANCE','Distancia en metros a la cual se notifica al cliente que el paquete llegó','50',2),('DRUGSTORE_CALL_CENTER','Boticas cuyas órdenes en Call Center serás procesadas por InkaTracker','633',0),('DRUGSTORE_RADIUS','Radio de cercanía al Drugstore','100',2),('ENABLE_GROUPING_ALGORITHM','Habilitar algoritmo de agrupamiento','N',1),('ETA_COLOR_GREEN','Etiqueta color verde','#00DD61',0),('ETA_COLOR_ORANGE','Etiqueta color naranja','#F6A623',0),('ETA_COLOR_RED','Etiqueta color roja','#E74C3C',0),('ETA_CONDITION_GREEN','Condición de evaluación optima','{PROMESA - ETA} > 5',0),('ETA_CONDITION_ORANGE','Condición de evaluación media','{PROMESA - ETA} <= 5',0),('ETA_CONDITION_RED','Condición de evaluación mala','{PROMESA - ETA} < 0',0),('LOG_FIRE_AVAILABLE_ACTIVITY','Codigo de actividad permitidos','20,65',0),('LOG_FIRE_AVAILABLE_STATUS','Codigo de estados permitidos','99',0),('LOG_FIRE_ENVIRONMENT','Entorno de ejecución de log-fire','inka',0),('LOG_FIRE_INTEGRATION_ACTIVE','Integeración con LogFire','N',0),('LOG_FIRE_INTEGRATION_LOG','Configura si se muestra el log de integración con logfire','Y',0),('LOG_FIRE_NORMAL_ORDER_PRIORITY','Prioridad para ordenes normales','10',0),('LOG_FIRE_NORMAL_ORDER_TYPE','Tipo de orden normal','N',0),('LOG_FIRE_ORDER_ORIGIN','Origen del pedido','E-commerce',0),('LOG_FIRE_ORDER_PACKING_ACTIVITY_CODE','Codigo para notificaión para el finalizado del packing','65',0),('LOG_FIRE_ORDER_REQUEST','URL para crear orden de pickeo en logfire','https://c2.logfireapps.com/inka/wms/api/init_stage_interface/',0),('LOG_FIRE_ORDER_STATUS_ACTIVITY_CODE','Codigo para notificaión de cambio de estado','20',0),('LOG_FIRE_ORDER_TYPE','Tipo de pedido','P1D',0),('LOG_FIRE_OUTFLOW','Ubicación de salida para despachos de logfire','SHP111',0),('LOG_FIRE_SALE_CONFIRMED','URL para confirmar el despacho de una orden','https://c2.logfireapps.com/inka/wms/api/ship_oblpn/',0),('LOG_FIRE_SALE_REJECTED','URL para rechazar el despacho de una orden','https://c2.logfireapps.com/inka/wms/api/init_stage_interface/',0),('LOG_FIRE_SCHEDULED_ORDER_PRIORITY','Prioridad para ordenes programadas','20',0),('LOG_FIRE_SCHEDULED_ORDER_TYPE','Tipo de orden programada','P',0),('LOG_FIRE_SYNC_SETTING','Indica si se sincronizará los parámetros','Y',0),('LOG_FIRE_VERSION','Version del API de Log Fire','8.0.0',0),('MARK_ARRIVE_VALIDATED','Habilitar botón de llegada ','Y',1),('MAX_INKAVENTA_ATTEMPT_QUANTITY','Maxima cantidad de reintentos para el envio de ordenes al inkaventa','5',0),('MAX_MINUTES_IN_GROUP_BUFFER','The max number of minutes before to release an Order from group buffer','1',2),('MAX_ORDERS_BY_GROUP','The max number of Orders tha t could be in a group','5',0),('MAX_ORDERS_BY_REGROUP','Maximo numero de elementos pro grupo en reagrupados','5',0),('MAX_RETRY_HOOK_CONSUMER','Cantidad máxima de reenvíos de request fallidos a los consumidores del Webhook?','3',0),('MAX_TIME_DISPATCHED_STATUS','Tiempo máximo de espera (en minutos) para el estado DISPATCHED','10',0),('MAX_TIME_PICKING_STATUS','Tiempo máximo de espera (en minutos) para el estado PICKING','5',0),('MAX_TIME_PREPARED_STATUS','Tiempo máximo de espera (en minutos) para el estado PREPARED','15',0),('METERS_GROUP_RADIUS','The number of meters to allow an Order be part of a Group','3000',3),('MOTORIZED_CANNOT_BREAK','Mensaje si el motorizado no puede descansar','El motorizado no puede entrar en descanso',0),('ORDERS_WITH_ERROR','Lista de Orders Id with some errors in InkaTracker interpretation','Optional[2013494245],',30),('ORDER_DISTANCE_TO_MARK_ARRIVE','Distancia para el motorizado para poder marcar llego','20000',6),('ORDER_EXECUTE_FETCH','Evaluar las ordenes del Insink','Y',0),('ORDER_LAST_EVALUATED_DATE','La fecha de la última Orden procesada desde el InkaVenta (a través del InsInk)','2017-08-25 16:46:48',10993),('ORDER_LAST_EVALUATED_DATE_OFFSET','Margen de tiempo hacie atras para buscar los ids de las ultimas ordenes','1200000',0),('ORDER_LAST_EVALUATED_ID','El ID de la última Orden procesada desde el InkaVenta (a través del InsInk)','2013509386',11025),('ORDER_MAX_DELIVERY_TIME','Tiempo de entrega máximo (en minutos)','90',0),('ORDER_SEARCH_MAX_OLD_DAYS','Máximo numeros de días anteriores para la búsqueda','8',0),('PERFORMANCE_ASSIGNED_LAST_MINUTES','Últimos \"n\" minutos para evaluar órdenes que pasaron de PREPARED a ASSIGNED','60',0),('PERFORMANCE_DISPATCHED_LAST_MINUTES','Últimos \"n\" minutos para evaluar órdenes que pasaron de ASSIGNED a ON_ROUTE','60',0),('PERFORMANCE_META_ASIGNADA','Valor meta del estado asignado','10',0),('PERFORMANCE_META_DESPACHO','Valor meta del estado despachado','5',0),('PERFORMANCE_META_ORDENES','Valor meta del estado ordenes','6',0),('PERFORMANCE_ORDERS_LAST_MINUTES','Últimos \"n\" minutos para evaluar órdenes que pasaron de ON_STORE a PREPARED','60',0),('PERFORMANCE_TOTAL_LAST_MINUTES','Últimos \"n\" minutos para evaluar órdenes que pasaron de ON_STORE a ON_ROUTE','70',1),('PERFORMANCE_UMBRAL_DAY','Valor umbral del dia','50',0),('PERFORMANCE_UMBRAL_HOUR','Valor umbral de la hora ','50',0),('PICKER_SCAN_COMMAND','Indica si el pickeador puede escanear las comandas','Y',0),('PICKER_SCAN_TRAY','Indica si el pickeador puede escanear las bandejas','Y',0),('PROFIT_REPORT_DAYS','Número de días anteriores a considerar en el reporte','15',0),('PROFIT_REPORT_TITLE','Título del reporte','ESTA QUINCENA',0),('PROGRAMMED_UNLOCK','Tiempo en minutos para el desbloqueo de ordenes programadas','60',1),('RETRY_HOOK_CONSUMER_REQUEST','Se deben reintentar los request fallidos de los consumidores del Webhook?','Y',0),('ROUTE_BUFFER','Buffer de tiempo en minutos para dar inicio a en camino automatizado, para deshabilitar usar 0.','0',10),('START_TIME_PERIOD_IN_MOTORIZED','Hora de inicio de periodo para el motorizado (formato 24 horas)','01:00',0),('STATUS_ALERT_TIME','Tiempo en minutos previo a la finalización de la hora de descanso','10',2),('TRACKING_DISTANCE','Distancia cada cuanto el motorizado actualiza su ubicación en metros','20',1),('TRACKING_TIME','Tiempo en segundos cada cuanto el motorizado actualiza su ubicación','5',0),('TRAVEL_SPEED','Velocidad (en min/km) de reparto del motorizado','4',2),('VERIFY_CELLPHONE_BY_SMS','Requiere validar sms','Y',0),('WARNING_PERCENTAGE_ETA','Porcentaje sobre el ETA proyectado para enviar alerta','50',0);
UNLOCK TABLES;

--
-- Table structure for table `order_tracker_status`
--
DROP TABLE IF EXISTS `order_tracker_status`;
CREATE TABLE `order_tracker_status` (
  `code` varchar(50) NOT NULL,
  `description` varchar(200) DEFAULT NULL,
  `order_by_number` int(11) DEFAULT NULL,
  PRIMARY KEY (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
--
-- Dumping data for table `order_tracker_status`
--
LOCK TABLES `order_tracker_status` WRITE;
INSERT INTO `order_tracker_status` VALUES ('ARRIVED','Orden llego',NULL),('ASSIGNED','Orden asignada al motorizado',1),('CANCELLED','Orden cancelada',NULL),('CREATED','Orden creada',1),('DELIVERED','Orden entregada',1),('ON_ROUTE','Orden en camino',10),('ON_STORE','Orden en tienda',3),('PACKING','Orden en empaquetado',4),('PICKING','Orden lista para pickear',5),('PREPARED','Orden preparada',6),('REJECTED','Orden rechazada',7),('SCHEDULED','Orden programada',8),('TRANSFERRED','Orden transferida',9),('UNASSIGNED','Orden desasignada',11);
UNLOCK TABLES;

--
-- Table structure for table `client`
--
DROP TABLE IF EXISTS `client`;
CREATE TABLE `client` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `dni` varchar(15) DEFAULT NULL,
  `phone` varchar(30) DEFAULT NULL,
  `email` varchar(200) DEFAULT NULL,
  `first_name` varchar(200) NOT NULL,
  `last_name` varchar(200) DEFAULT NULL,
  `inka_club_client` char(1) DEFAULT NULL,
  `birth_date` datetime DEFAULT NULL,
  `is_anonymous` tinyint(1) DEFAULT NULL,
  `created_by` varchar(100) DEFAULT NULL,
  `date_created` datetime DEFAULT CURRENT_TIMESTAMP,
  `last_updated_by` varchar(100) DEFAULT NULL,
  `date_last_updated` datetime DEFAULT NULL,
  `version` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8;

--
-- Table structure for table `order_tracker`
--
DROP TABLE IF EXISTS `order_tracker`;
CREATE TABLE `order_tracker` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `source` varchar(16) DEFAULT NULL,
  `group_name` varchar(20) DEFAULT NULL,
  `group_order` int(11) unsigned DEFAULT NULL,
  `delivery_manager_id` bigint(20) DEFAULT NULL,
  `ecommerce_purchase_id` bigint(20) DEFAULT NULL,
  `external_purchase_id` bigint(20) DEFAULT NULL,
  `bridge_purchase_id` bigint(20) DEFAULT NULL,
  `order_status_code` varchar(50) NOT NULL,
  `total_cost` decimal(10,2) DEFAULT NULL,
  `delivery_cost` decimal(10,2) DEFAULT NULL,
  `client_id` bigint(20) NOT NULL,
  `with_transference` varchar(1) DEFAULT NULL,
  `in_group_process` char(1) DEFAULT NULL,
  `order_status_date` datetime NOT NULL,
  `scheduled_start_date` datetime NOT NULL,
  `scheduled_end_date` datetime NOT NULL,
  `motorized_id` varchar(40) DEFAULT NULL,
  `order_note` varchar(200) DEFAULT NULL,
  `kit` varchar(10) DEFAULT NULL,
  `kit_status` varchar(10) DEFAULT NULL,
  `sent_to_log_fire` char(1) DEFAULT NULL,
  `log_fire_attempt` int(11) DEFAULT NULL,
  `log_fire_message` varchar(250) DEFAULT NULL,
  `dispatch_in_log_fire` char(1) DEFAULT NULL,
  `message_to_dispatch` varchar(100) DEFAULT NULL,
  `cancel_in_log_fire` char(1) DEFAULT NULL,
  `liquidated` char(1) DEFAULT NULL,
  `pay_back_envelope` varchar(20) DEFAULT NULL,
  `envelope_status` varchar(6) DEFAULT NULL,
  `version` int(11) NOT NULL DEFAULT '0',
  `created_by` varchar(100) DEFAULT NULL,
  `date_created` datetime DEFAULT CURRENT_TIMESTAMP,
  `last_updated_by` varchar(100) DEFAULT NULL,
  `date_last_updated` datetime DEFAULT NULL,
  `order_tracking_code` varchar(20) DEFAULT NULL,
  `drugstore_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `order_tracker_ibfk_1` (`client_id`),
  KEY `order_tracker_ibfk_2` (`order_status_code`),
  KEY `order_tracker_ibfk_3` (`motorized_id`),
  CONSTRAINT `order_tracker_ibfk_1` FOREIGN KEY (`client_id`) REFERENCES `client` (`id`) ON DELETE NO ACTION ON UPDATE CASCADE,
  CONSTRAINT `order_tracker_ibfk_2` FOREIGN KEY (`order_status_code`) REFERENCES `order_tracker_status` (`code`) ON DELETE NO ACTION ON UPDATE CASCADE,
  CONSTRAINT `order_tracker_ibfk_3` FOREIGN KEY (`motorized_id`) REFERENCES `user` (`id`) ON DELETE NO ACTION ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=119 DEFAULT CHARSET=utf8;

--
-- Table structure for table `order_tracker_item`
--
DROP TABLE IF EXISTS `order_tracker_item`;
CREATE TABLE `order_tracker_item` (
  `order_tracker_id` bigint(20) NOT NULL,
  `product_code` varchar(16) NOT NULL,
  `product_sku` varchar(16) DEFAULT NULL,
  `product_sap_code` varchar(10) DEFAULT NULL,
  `name` varchar(200) DEFAULT NULL,
  `short_description` varchar(1000) DEFAULT NULL,
  `brand` varchar(50) DEFAULT NULL,
  `quantity` int(11) DEFAULT NULL,
  `unit_price` decimal(10,2) DEFAULT NULL,
  `total_price` decimal(10,2) DEFAULT NULL,
  `fractionated` char(1) DEFAULT NULL,
  KEY `order_tracker_id` (`order_tracker_id`),
  CONSTRAINT `order_tracker_item_ibfk_1` FOREIGN KEY (`order_tracker_id`) REFERENCES `order_tracker` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Table structure for table `service_type`
-- ex MyIsam
DROP TABLE IF EXISTS `service_type`;
CREATE TABLE `service_type` (
  `code` varchar(32) NOT NULL,
  `description` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
--
-- Dumping data for table `service_type`
--
LOCK TABLES `service_type` WRITE;
INSERT INTO `service_type` VALUES ('INKATRACKER_CALL','Inkatracker de call center'),('INKATRACKER_LITE_RAD','Inkatracker lite con RAD'),('INKATRACKER_LITE_RET','Inkatracker lite con RET'),('INKATRACKER_RAD','Inkatracker con RAD'),('NDS','Servicio no definido'),('TEMPORARY_RAD','Temporary con RAD');
UNLOCK TABLES;

--
-- Table structure for table `order_tracker_detail`
--
DROP TABLE IF EXISTS `order_tracker_detail`;
CREATE TABLE `order_tracker_detail` (
  `order_tracker_id` bigint(20) NOT NULL,
  `delivery` tinyint(1) DEFAULT NULL,
  `service_type_code` varchar(32) DEFAULT NULL,
  `service_type_description` varchar(128) DEFAULT NULL,
  `company_code` varchar(16) DEFAULT NULL,
  `company_description` varchar(128) DEFAULT NULL,
  `center_code` varchar(16) DEFAULT NULL,
  `center_description` varchar(128) DEFAULT NULL,
  `start_hour_zone` time DEFAULT NULL,
  `end_hour_zone` time DEFAULT NULL,
  `delivery_lead_time` int(11) DEFAULT NULL,
  `days_to_pickup` int(11) DEFAULT NULL,
  `start_hour_pickup` time DEFAULT NULL,
  `end_hour_pickup` time DEFAULT NULL,
  `attempt` int(11) DEFAULT NULL,
  `reprogrammed` tinyint(1) DEFAULT NULL,
  KEY `order_tracker_id` (`order_tracker_id`),
  KEY `order_tracker_detail_service_type_fk` (`service_type_code`),
  CONSTRAINT `order_tracker_detail_fk1` FOREIGN KEY (`order_tracker_id`) REFERENCES `order_tracker` (`id`) ON DELETE NO ACTION ON UPDATE CASCADE,
  CONSTRAINT `order_tracker_detail_fk2` FOREIGN KEY (`service_type_code`) REFERENCES `service_type` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Table structure for table `payment_method`
--
DROP TABLE IF EXISTS `payment_method`;
CREATE TABLE `payment_method` (
  `order_tracker_id` bigint(20) NOT NULL,
  `payment_type` varchar(15) NOT NULL,
  `payment_description` varchar(150) NOT NULL,
  `card_provider` varchar(50) DEFAULT NULL,
  `card_name` varchar(100) DEFAULT NULL,
  `card_number` char(4) DEFAULT NULL,
  `paid_amount` decimal(10,2) DEFAULT NULL,
  `change_amount` decimal(10,2) DEFAULT NULL,
  `payment_note` text,
  KEY `order_tracker_id` (`order_tracker_id`),
  CONSTRAINT `payment_method_ibfk_1` FOREIGN KEY (`order_tracker_id`) REFERENCES `order_tracker` (`id`) ON DELETE NO ACTION ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Table structure for table `receipt_type`
--
DROP TABLE IF EXISTS `receipt_type`;
CREATE TABLE `receipt_type` (
  `order_tracker_id` bigint(20) NOT NULL,
  `name` varchar(15) NOT NULL,
  `dni` varchar(8) NOT NULL,
  `ruc` varchar(11) DEFAULT NULL,
  `company_name` varchar(100) DEFAULT NULL,
  `company_address` varchar(100) DEFAULT NULL,
  `receipt_note` varchar(100) DEFAULT NULL,
  KEY `order_tracker_id` (`order_tracker_id`),
  CONSTRAINT `receipt_type_ibfk_1` FOREIGN KEY (`order_tracker_id`) REFERENCES `order_tracker` (`id`) ON DELETE NO ACTION ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Table structure for table `address_tracker`
--
DROP TABLE IF EXISTS `address_tracker`;
CREATE TABLE `address_tracker` (
  `order_tracker_id` bigint(20) NOT NULL,
  `district` varchar(100) DEFAULT NULL,
  `street` varchar(100) NOT NULL,
  `number` varchar(100) DEFAULT NULL,
  `province` varchar(100) DEFAULT NULL,
  `apartment` varchar(100) DEFAULT NULL,
  `department` varchar(100) DEFAULT NULL,
  `country` varchar(50) DEFAULT NULL,
  `latitude` decimal(10,8) DEFAULT NULL,
  `longitude` decimal(11,8) DEFAULT NULL,
  `notes` text,
  `old_address` text,
  `ubigeo_code` varchar(16) DEFAULT NULL,
  `created_by` varchar(100) DEFAULT NULL,
  `date_created` datetime DEFAULT CURRENT_TIMESTAMP,
  `last_updated_by` varchar(100) DEFAULT NULL,
  `date_last_updated` datetime DEFAULT NULL,
  `version` int(11) NOT NULL DEFAULT '0',
  KEY `order_tracker_id` (`order_tracker_id`),
  CONSTRAINT `address_tracker_ibfk_1` FOREIGN KEY (`order_tracker_id`) REFERENCES `order_tracker` (`id`) ON DELETE NO ACTION ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Table structure for table `order_tracker_history`
--
DROP TABLE IF EXISTS `order_tracker_history`;
CREATE TABLE `order_tracker_history` (
  `order_tracker_id` bigint(20) NOT NULL,
  `order_tracker_status_code` varchar(50) DEFAULT NULL,
  `latitude_history` decimal(10,8) DEFAULT NULL,
  `longitude_history` decimal(11,8) DEFAULT NULL,
  `time_from_ui` datetime DEFAULT NULL,
  `updated_by` varchar(30) DEFAULT NULL,
  `order_note` text,
  `custom_note` text,
  `pos_status` varchar(6) DEFAULT NULL,
  `pay_back_envelope` varchar(20) DEFAULT NULL,
  KEY `order_tracker_id` (`order_tracker_id`),
  KEY `order_tracker_status_code` (`order_tracker_status_code`),
  CONSTRAINT `order_tracker_history_ibfk_2` FOREIGN KEY (`order_tracker_status_code`) REFERENCES `order_tracker_status` (`code`),
  CONSTRAINT `order_tracker_history_ibfk_1` FOREIGN KEY (`order_tracker_id`) REFERENCES `order_tracker` (`id`) ON DELETE NO ACTION ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Table structure for table `tracker_reason`
--
DROP TABLE IF EXISTS `tracker_reason`;
CREATE TABLE `tracker_reason` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type` varchar(10) DEFAULT NULL,
  `code` varchar(5) DEFAULT NULL,
  `reason` varchar(100) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8;
--
-- Dumping data for table `tracker_reason`
--

LOCK TABLES `tracker_reason` WRITE;
INSERT INTO `tracker_reason` VALUES (1,'REJECTED','R01','Cliente no está en la dirección de entrega'),(2,'REJECTED','R02','Error del cliente al solicitar productos'),(3,'REJECTED','R03','Cliente no desea el pedido'),(4,'REJECTED','R04','La dirección no coincide con el punto de entrega'),(5,'REJECTED','R05','Pedido no llego en el horario ofrecido'),(6,'REJECTED','R06','No es posible ubicar la dirección de entrega'),(7,'REJECTED','R07','Pedido duplicado'),(8,'REJECTED','R08','Cliente no vive en la dirección de entrega'),(9,'REJECTED','R09','Zona sin cobertura'),(10,'REJECTED','R10','Zona peligrosa'),(11,'REJECTED','R11','Cambio de forma de pago'),(12,'REJECTED','R12','Cliente no tiene receta médica'),(13,'REJECTED','R13','Error de inkafarma al elegir los productos'),(14,'REJECTED','R14','Productos dañados'),(15,'REJECTED','R15','Cambio de comprobante de pago'),(16,'REJECTED','R16','Cliente rechazo por cambio de presentacion');
UNLOCK TABLES;

--
-- Table structure for table `tutorial`
--
DROP TABLE IF EXISTS `tutorial`;
CREATE TABLE `tutorial` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type` varchar(20) NOT NULL,
  `format` varchar(5) NOT NULL,
  `name` varchar(50) NOT NULL,
  `description` varchar(100) DEFAULT NULL,
  `url` varchar(150) NOT NULL,
  `duration` int(11) DEFAULT NULL,
  `is_active` varchar(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

--
-- Table structure for table `alert`
--
DROP TABLE IF EXISTS `alert`;
CREATE TABLE `alert` (
  `id` char(3) NOT NULL,
  `message` varchar(100) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Table structure for table `alert_scope`
--
DROP TABLE IF EXISTS `alert_scope`;
CREATE TABLE `alert_scope` (
  `alert_id` char(3) DEFAULT NULL,
  `tag` varchar(20) NOT NULL,
  `scope` varchar(100) NOT NULL,
  UNIQUE KEY `alertId` (`alert_id`,`tag`),
  CONSTRAINT `alert_scope_ibfk_1` FOREIGN KEY (`alert_id`) REFERENCES `alert` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Table structure for table `delivery_travel`
--
DROP TABLE IF EXISTS `delivery_travel`;
CREATE TABLE `delivery_travel` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `group_name` varchar(30) DEFAULT NULL,
  `motorized_id` varchar(40) DEFAULT NULL,
  `date_created` datetime DEFAULT NULL,
  `travel_status` varchar(20) DEFAULT NULL,
  `projected_eta` int(11) DEFAULT NULL,
  `start_date` datetime DEFAULT NULL,
  `end_date` datetime DEFAULT NULL,
  `pick_up_center_name` varchar(30) DEFAULT NULL,
  `pick_up_center_latitude` decimal(10,8) DEFAULT NULL,
  `pick_up_center_longitude` decimal(11,8) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=161 DEFAULT CHARSET=utf8;

--
-- Table structure for table `delivery_travel_detail`
--
DROP TABLE IF EXISTS `delivery_travel_detail`;
CREATE TABLE `delivery_travel_detail` (
  `delivery_travel_id` bigint(20) DEFAULT NULL,
  `order_group` int(11) DEFAULT NULL,
  `order_id` bigint(20) DEFAULT NULL,
  `projected_eta` int(11) DEFAULT NULL,
  `start_date` datetime DEFAULT NULL,
  `end_date` datetime DEFAULT NULL,
  `final_status` varchar(40) DEFAULT NULL,
  `wait_time` int(11) DEFAULT NULL,
  `pos_code` varchar(4) DEFAULT NULL,
  UNIQUE KEY `deliveryTravelId` (`delivery_travel_id`,`order_id`),
  KEY `orderId` (`order_id`),
  CONSTRAINT `delivery_travel_detail_ibfk_1` FOREIGN KEY (`delivery_travel_id`) REFERENCES `delivery_travel` (`id`),
  CONSTRAINT `delivery_travel_detail_ibfk_2` FOREIGN KEY (`order_id`) REFERENCES `order_tracker` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Table structure for table `device`
--
DROP TABLE IF EXISTS `device`;
CREATE TABLE `device` (
  `imei` varchar(15) NOT NULL,
  `phone_number` varchar(15) DEFAULT NULL,
  `phone_mark` varchar(30) DEFAULT NULL,
  `phone_model` varchar(30) DEFAULT NULL,
  `last_user_id` varchar(40) DEFAULT NULL,
  PRIMARY KEY (`imei`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


--
-- Table structure for table `device_history`
--
DROP TABLE IF EXISTS `device_history`;
CREATE TABLE `device_history` (
  `imei` varchar(15) NOT NULL,
  `motorized_id` varchar(40) DEFAULT NULL,
  `assigned_time` datetime DEFAULT NULL,
  KEY `imei` (`imei`),
  KEY `motorizedId` (`motorized_id`),
  CONSTRAINT `device_history_ibfk_1` FOREIGN KEY (`imei`) REFERENCES `device` (`imei`) ON UPDATE CASCADE,
  CONSTRAINT `device_history_ibfk_2` FOREIGN KEY (`motorized_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Table structure for table `drugstore_group`
--
DROP TABLE IF EXISTS `drugstore_group`;
CREATE TABLE `drugstore_group` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `group_name` varchar(50) DEFAULT NULL,
  `group_status` char(1) DEFAULT NULL,
  `parent_company_code` varchar(10) DEFAULT NULL,
  `facility_code` varchar(5) DEFAULT NULL,
  `company_code` varchar(5) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

--
-- Table structure for table `drugstore_mapping`
--
DROP TABLE IF EXISTS `drugstore_mapping`;
CREATE TABLE `drugstore_mapping` (
  `group_id` int(11) DEFAULT NULL,
  `drugstore_id` bigint(20) DEFAULT NULL,
  KEY `groupId` (`group_id`),
  CONSTRAINT `drugstore_mapping_ibfk_1` FOREIGN KEY (`group_id`) REFERENCES `drugstore_group` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Table structure for table `order_alert`
--
DROP TABLE IF EXISTS `order_alert`;
CREATE TABLE `order_alert` (
  `order_id` bigint(20) DEFAULT NULL,
  `alert_id` char(3) DEFAULT NULL,
  `finalized` char(1) NOT NULL,
  KEY `orderId` (`order_id`),
  KEY `alertId` (`alert_id`),
  CONSTRAINT `order_alert_ibfk_1` FOREIGN KEY (`order_id`) REFERENCES `order_tracker` (`id`),
  CONSTRAINT `order_alert_ibfk_2` FOREIGN KEY (`alert_id`) REFERENCES `alert` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Table structure for table `role`
--
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role` (
  `code` varchar(30) NOT NULL,
  `description` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`code`),
  UNIQUE KEY `index_trackerole_ids` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
--
-- Dumping data for table `role`
--
LOCK TABLES `role` WRITE;
INSERT INTO `role` VALUES ('ROLE_ADMINISTRATOR','Rol para administradores'),('ROLE_COORDINATOR','Rol para coordinadores'),('ROLE_CUSTOMER_SUPPORT','Rol para atención al cliente'),('ROLE_DISPATCHER','Rol para despachadores'),('ROLE_DISPATCHER_LIQUIDATOR','Rol de despacho y liquidación'),('ROLE_LIQUIDATOR','Rol para liquidar ordenes'),('ROLE_MOTORIZED','Rol para motorizados'),('ROLE_PICKER','Rol para el pickeador'),('ROLE_SUPPLIER','Rol para el supplier');
UNLOCK TABLES;

--
-- Table structure for table `shelf`
--
DROP TABLE IF EXISTS `shelf`;
CREATE TABLE `shelf` (
  `order_tracker_id` bigint(20) NOT NULL,
  `pack_code` varchar(20) DEFAULT NULL,
  `lock_code` varchar(20) DEFAULT NULL,
  `product_code` varchar(10) DEFAULT NULL,
  `quantity` int(11) DEFAULT NULL,
  KEY `order_tracker_id` (`order_tracker_id`),
  CONSTRAINT `shelf_ibfk_1` FOREIGN KEY (`order_tracker_id`) REFERENCES `order_tracker` (`id`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Table structure for table `shift`
--
DROP TABLE IF EXISTS `shift`;
CREATE TABLE `shift` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `code` varchar(50) NOT NULL,
  `start_hour` time DEFAULT NULL,
  `end_hour` time DEFAULT NULL,
  `start_break_hour` time DEFAULT NULL,
  `end_break_hour` time DEFAULT NULL,
  `is_enabled` varchar(1) DEFAULT NULL,
  `amount_motorized` bigint(20) DEFAULT NULL,
  `created_by` varchar(30) DEFAULT NULL,
  `date_created` datetime DEFAULT NULL,
  `last_updated_by` varchar(30) DEFAULT NULL,
  `date_last_updated` datetime DEFAULT NULL,
  `version` int(10) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
INSERT INTO `shift` VALUES (1,'24 Horas','00:00:00','23:59:00','12:50:00','13:00:00','Y',30,'gTwUZHB5YtXLk1ZJjxvaQphSi062','2019-06-04 17:07:22','gTwUZHB5YtXLk1ZJjxvaQphSi062','2019-06-04 17:07:22',0);
--
-- Table structure for table `user_alert`
--
DROP TABLE IF EXISTS `user_alert`;
CREATE TABLE `user_alert` (
  `user_id` varchar(40) DEFAULT NULL,
  `alert_id` char(3) DEFAULT NULL,
  `finalized` char(1) NOT NULL,
  KEY `userId` (`user_id`),
  KEY `alertId` (`alert_id`),
  CONSTRAINT `user_alert_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `user_alert_ibfk_2` FOREIGN KEY (`alert_id`) REFERENCES `alert` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Table structure for table `user_role`
--
DROP TABLE IF EXISTS `user_role`;
CREATE TABLE `user_role` (
  `user_id` varchar(40) DEFAULT NULL,
  `role_code` varchar(30) DEFAULT NULL,
  UNIQUE KEY `userrole` (`user_id`,`role_code`),
  KEY `roleCode` (`role_code`),
  CONSTRAINT `user_role_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `user_role_ibfk_2` FOREIGN KEY (`role_code`) REFERENCES `role` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Table structure for table `user_shift`
--
DROP TABLE IF EXISTS `user_shift`;
CREATE TABLE `user_shift` (
  `user_id` varchar(40) NOT NULL,
  `shift_id` bigint(20) NOT NULL,
  `is_active` varchar(1) DEFAULT NULL,
  PRIMARY KEY (`user_id`,`shift_id`),
  KEY `userShift_shift_fk` (`shift_id`),
  CONSTRAINT `userShift_shift_fk` FOREIGN KEY (`shift_id`) REFERENCES `shift` (`id`),
  CONSTRAINT `userShift_user_fk` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Table structure for table `user_status_history`
--
DROP TABLE IF EXISTS `user_status_history`;
CREATE TABLE `user_status_history` (
  `user_id` varchar(40) DEFAULT NULL,
  `status_code` varchar(50) DEFAULT NULL,
  `order_external_id` bigint(20) DEFAULT NULL,
  `latitude` decimal(10,8) DEFAULT NULL,
  `longitude` decimal(10,8) DEFAULT NULL,
  `time_from_ui` datetime DEFAULT NULL,
  KEY `userId` (`user_id`),
  KEY `statusCode` (`status_code`),
  CONSTRAINT `user_status_history_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `user_status_history_ibfk_2` FOREIGN KEY (`status_code`) REFERENCES `status` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- -------------------------------------------------------------------------------------------------------------------
-- -------------------------------------------------------------------------------------------------------------------

--
-- Table structure for table `cancel_reason`
-- ex MyIsam
DROP TABLE IF EXISTS `cancel_reason`;
CREATE TABLE `cancel_reason` (
  `code` varchar(255) NOT NULL,
  `reason` varchar(255) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `company_code` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`code`),
  KEY `FK7s9a32svo7iox6e0ftxdiso9f` (`company_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Table structure for table `company`
-- ex MyIsam
DROP TABLE IF EXISTS `company`;
CREATE TABLE `company` (
  `code` varchar(255) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Table structure for table `local`
-- ex MyIsam
DROP TABLE IF EXISTS `local`;
CREATE TABLE `local` (
  `code` varchar(255) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `company_code` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`code`),
  KEY `FK37g0nujxk7jachg016u91j0b` (`company_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Table structure for table `profit`
-- ex MyIsam
DROP TABLE IF EXISTS `profit`;
CREATE TABLE `profit` (
  `date` date NOT NULL,
  `total` int(11) DEFAULT NULL,
  PRIMARY KEY (`date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Table structure for table `motorized_service`
--
DROP TABLE IF EXISTS `motorized_service`;
CREATE TABLE `motorized_service` (
  `motorized_type_code` varchar(20) NOT NULL,
  `service_type_code` varchar(32) NOT NULL,
  PRIMARY KEY (`motorized_type_code`,`service_type_code`),
  KEY `service_type_fk` (`service_type_code`),
  CONSTRAINT `motorized_type_fk` FOREIGN KEY (`motorized_type_code`) REFERENCES `motorized_type` (`code`),
  CONSTRAINT `service_type_fk` FOREIGN KEY (`service_type_code`) REFERENCES `service_type` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
--
-- Dumping data for table `motorized_service`
--
LOCK TABLES `motorized_service` WRITE;
INSERT INTO `motorized_service` VALUES ('DRUGSTORE','INKATRACKER_LITE_RAD'),('DELIVERY_CENTER','INKATRACKER_RAD');
UNLOCK TABLES;

--
-- Table structure for table `oauth_access_token`
--
DROP TABLE IF EXISTS `oauth_access_token`;
CREATE TABLE `oauth_access_token` (
  `authentication_id` varchar(255) NOT NULL,
  `token_id` varchar(255) NOT NULL,
  `token` blob NOT NULL,
  `user_name` varchar(255) NOT NULL,
  `client_id` varchar(255) NOT NULL,
  `authentication` blob NOT NULL,
  `refresh_token` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`authentication_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Table structure for table `oauth_client_details`
--
DROP TABLE IF EXISTS `oauth_client_details`;
CREATE TABLE `oauth_client_details` (
  `client_id` varchar(256) NOT NULL,
  `resource_ids` varchar(256) DEFAULT NULL,
  `client_secret` varchar(256) DEFAULT NULL,
  `scope` varchar(256) DEFAULT NULL,
  `authorized_grant_types` varchar(256) DEFAULT NULL,
  `web_server_redirect_uri` varchar(256) DEFAULT NULL,
  `authorities` varchar(256) DEFAULT NULL,
  `access_token_validity` int(11) DEFAULT NULL,
  `refresh_token_validity` int(11) DEFAULT NULL,
  `additional_information` varchar(4096) DEFAULT NULL,
  `autoapprove` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`client_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--
-- Dumping data for table `oauth_client_details`
--
LOCK TABLES `oauth_client_details` WRITE;
INSERT INTO `oauth_client_details` VALUES ('AndroidApp',NULL,'$2a$10$SK3CdCpS2ui543vb4dMS4ev1F5NanWS.wxzlSFRa/huWBZkKIWwe6','read,write','firebase',NULL,NULL,5184000,NULL,NULL,'true');
UNLOCK TABLES;

--
-- Table structure for table `oauth_refresh_token`
--
DROP TABLE IF EXISTS `oauth_refresh_token`;
CREATE TABLE `oauth_refresh_token` (
  `token_id` varchar(255) NOT NULL,
  `token` blob NOT NULL,
  `authentication` blob NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Table structure for table `token_black_list`
--
DROP TABLE IF EXISTS `token_black_list`;
CREATE TABLE `token_black_list` (
  `token` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

