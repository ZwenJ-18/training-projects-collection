-- MySQL dump 10.13  Distrib 8.0.42, for Win64 (x86_64)
--
-- Host: localhost    Database: supermarket_points_db
-- ------------------------------------------------------
-- Server version	8.0.42

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `commodity`
--

DROP TABLE IF EXISTS `commodity`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `commodity` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '商品ID',
  `name` varchar(100) NOT NULL COMMENT '商品名称',
  `code` varchar(50) NOT NULL COMMENT '商品编码',
  `cover` varchar(255) DEFAULT NULL COMMENT '商品封面图片URL',
  `description` text COMMENT '商品描述',
  `price` decimal(10,2) NOT NULL COMMENT '商品价格',
  `stock` int NOT NULL DEFAULT '0' COMMENT '库存数量',
  `points` int NOT NULL DEFAULT '0' COMMENT '兑换所需积分',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `status` tinyint DEFAULT '1' COMMENT '状态（1-上架，0-下架）',
  PRIMARY KEY (`id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='商品表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `commodity`
--

LOCK TABLES `commodity` WRITE;
/*!40000 ALTER TABLE `commodity` DISABLE KEYS */;
INSERT INTO `commodity` VALUES (1,'小米14手机','SP001','/images/小米14手机.jpg','',4999.00,91,49990,'2025-12-04 02:55:38',1),(4,'矿泉水','SP003','/images/矿泉水.jpg','',2.00,31,20,'2025-12-06 16:06:00',1),(9,'泡面','SP004','/images/方便面.jpg','',5.00,103,50,'2025-12-06 17:59:57',1),(11,'辣条','SP007','/images/辣条.jpg','',5.00,77,50,'2025-12-06 19:07:21',1),(14,'沐浴露','SP006','/images/fbb65d8f-5cfc-47b8-9b5c-a50dc4db5b74.jpg','',35.00,49,350,'2025-12-06 19:51:57',1),(25,'大米','SP009','/images/efd5a9d0-4ee1-45f9-b21d-2955bf110ce6.jpg','',80.00,58,800,'2025-12-07 06:27:51',1),(26,'鸭子','SP0010','/images/6c511797-32e1-4150-8d4f-b0e459624163.jpg','',8000.00,50,80000,'2025-12-07 06:29:23',1),(28,'沐浴','SP088','/images/8b254d9d-3714-4bb7-b102-ad65b511b528.jpg','美女',63.00,43,10,'2025-12-18 14:04:59',0);
/*!40000 ALTER TABLE `commodity` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order_info`
--

DROP TABLE IF EXISTS `order_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `order_info` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '订单ID',
  `order_no` varchar(32) NOT NULL COMMENT '订单编号',
  `user_id` int NOT NULL COMMENT '用户ID（关联sys_user.id）',
  `commodity_id` int NOT NULL COMMENT '商品ID（关联commodity.id）',
  `amount` decimal(10,2) NOT NULL COMMENT '订单金额',
  `points` int NOT NULL COMMENT '获得/扣除积分',
  `pay_status` varchar(20) NOT NULL DEFAULT 'UNPAID' COMMENT '支付状态（PAID-已支付，UNPAID-未支付）',
  `order_status` varchar(20) NOT NULL DEFAULT 'UNPAID' COMMENT '订单状态（SUCCESS-成功，CANCEL-取消，UNPAID-未支付）',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `pay_time` datetime DEFAULT NULL COMMENT '支付时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `order_no` (`order_no`),
  KEY `commodity_id` (`commodity_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_order_no` (`order_no`),
  CONSTRAINT `order_info_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`id`) ON DELETE CASCADE,
  CONSTRAINT `order_info_ibfk_2` FOREIGN KEY (`commodity_id`) REFERENCES `commodity` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='订单表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_info`
--

LOCK TABLES `order_info` WRITE;
/*!40000 ALTER TABLE `order_info` DISABLE KEYS */;
/*!40000 ALTER TABLE `order_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `point_log`
--

DROP TABLE IF EXISTS `point_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `point_log` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '日志ID',
  `user_id` int NOT NULL COMMENT '用户ID',
  `username` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户名',
  `commodity_id` int NOT NULL COMMENT '商品ID',
  `commodity_name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '商品名称',
  `point_change` int NOT NULL COMMENT '积分变动（正数=增加，负数=减少）',
  `type` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '变动类型：CONSUME（消费返积分）、EXCHANGE（积分兑换）',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='积分变动日志表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `point_log`
--

LOCK TABLES `point_log` WRITE;
/*!40000 ALTER TABLE `point_log` DISABLE KEYS */;
INSERT INTO `point_log` VALUES (1,2,'user1',4,'矿泉水',20,'CONSUME','2025-12-08 03:40:42'),(2,2,'user1',4,'矿泉水',-20,'EXCHANGE','2025-12-08 03:40:51'),(3,11,'user3',4,'矿泉水',20,'CONSUME','2025-12-08 03:54:28'),(4,11,'user3',9,'泡面',50,'CONSUME','2025-12-08 03:54:36'),(5,11,'user3',4,'矿泉水',20,'CONSUME','2025-12-08 03:55:39'),(6,2,'user1',4,'矿泉水',20,'CONSUME','2025-12-08 10:04:48'),(7,2,'user1',4,'矿泉水',-20,'EXCHANGE','2025-12-08 10:05:11'),(8,2,'user1',9,'泡面',-50,'EXCHANGE','2025-12-08 10:05:15'),(9,11,'user3',25,'大米',800,'CONSUME','2025-12-08 10:05:35'),(10,11,'user3',25,'大米',-800,'EXCHANGE','2025-12-08 10:05:38'),(11,2,'user1',9,'泡面',50,'CONSUME','2025-12-08 10:09:03'),(12,2,'user1',4,'矿泉水',-20,'EXCHANGE','2025-12-08 10:09:09'),(13,2,'user1',4,'矿泉水',20,'CONSUME','2025-12-08 10:35:45');
/*!40000 ALTER TABLE `point_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `points_record`
--

DROP TABLE IF EXISTS `points_record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `points_record` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '记录ID',
  `user_id` int NOT NULL COMMENT '用户ID（关联sys_user.id）',
  `commodity_id` int NOT NULL COMMENT '商品ID（关联commodity.id）',
  `points` int NOT NULL COMMENT '积分变动（正数-增加，负数-扣除）',
  `type` varchar(20) NOT NULL COMMENT '变动类型（BUY-购买获取，EXCHANGE-兑换扣除）',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '记录时间',
  PRIMARY KEY (`id`),
  KEY `commodity_id` (`commodity_id`),
  KEY `idx_user_id` (`user_id`),
  CONSTRAINT `points_record_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`id`) ON DELETE CASCADE,
  CONSTRAINT `points_record_ibfk_2` FOREIGN KEY (`commodity_id`) REFERENCES `commodity` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='积分记录表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `points_record`
--

LOCK TABLES `points_record` WRITE;
/*!40000 ALTER TABLE `points_record` DISABLE KEYS */;
/*!40000 ALTER TABLE `points_record` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_points_log`
--

DROP TABLE IF EXISTS `sys_points_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_points_log` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '日志ID',
  `user_id` int NOT NULL COMMENT '用户ID',
  `username` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户名',
  `points` int NOT NULL COMMENT '变动积分（正数增加，负数扣除）',
  `type` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '变动类型：消费/兑换/人工调整',
  `remark` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '备注（如：消费订单号、兑换商品名）',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `commodity_id` bigint DEFAULT NULL COMMENT '商品ID',
  `commodity_name` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '商品名称',
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `sys_points_log_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=127 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='积分变动日志表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_points_log`
--

LOCK TABLES `sys_points_log` WRITE;
/*!40000 ALTER TABLE `sys_points_log` DISABLE KEYS */;
INSERT INTO `sys_points_log` VALUES (1,2,'user1',20,'消费','买了瓶水','2025-12-04 17:48:03',NULL,NULL),(2,2,'user1',50,'消费','买水果','2025-12-04 17:58:18',NULL,NULL),(11,2,'user1',50,'消费','买了盐','2025-12-06 16:18:05',NULL,NULL),(12,2,'user1',-100,'兑换','换大米','2025-12-06 16:18:37',NULL,NULL),(13,2,'user1',-4,'兑换','兑换泡面（商品：泡面）','2025-12-06 18:54:14',NULL,NULL),(15,2,'user1',-20,'兑换','兑换泡面（商品：泡面×5，扣减积分：20）','2025-12-06 19:05:55',NULL,NULL),(24,2,'user1',20,'CONSUME','商品：矿泉水','2025-12-08 03:40:42',4,'矿泉水'),(25,2,'user1',-20,'EXCHANGE','商品：矿泉水','2025-12-08 03:40:51',4,'矿泉水'),(29,2,'user1',20,'CONSUME','商品：矿泉水','2025-12-08 10:04:48',4,'矿泉水'),(30,2,'user1',-20,'EXCHANGE','商品：矿泉水','2025-12-08 10:05:11',4,'矿泉水'),(31,2,'user1',-50,'EXCHANGE','商品：泡面','2025-12-08 10:05:15',9,'泡面'),(34,2,'user1',50,'CONSUME','商品：泡面','2025-12-08 10:09:03',9,'泡面'),(35,2,'user1',-20,'EXCHANGE','商品：矿泉水','2025-12-08 10:09:09',4,'矿泉水'),(39,2,'user1',-50,'兑换','她要老坛（商品：泡面×1，扣减积分：50）','2025-12-08 10:36:37',NULL,NULL),(40,2,'user1',50,'消费','消费商品：辣条×1，自动加积分：50','2025-12-08 11:05:26',11,'辣条'),(41,2,'user1',-20,'EXCHANGE','商品：矿泉水','2025-12-08 11:05:47',4,'矿泉水'),(44,15,'user2',50,'消费','买了臭脚泡面（消费商品：泡面×1，自动加积分：50）','2025-12-08 11:18:38',9,'泡面'),(45,15,'user2',800000,'消费','点了个老八（消费商品：鸭子×1，自动加积分：800000）','2025-12-08 11:19:53',26,'鸭子'),(46,15,'user2',-49990,'兑换','商品：小米14手机 | 扣减积分：49990','2025-12-08 11:20:19',1,'小米14手机'),(47,15,'user2',-49990,'兑换','商品：小米14手机 | 扣减积分：49990','2025-12-08 11:20:21',1,'小米14手机'),(48,15,'user2',-49990,'兑换','商品：小米14手机 | 扣减积分：49990','2025-12-08 11:20:22',1,'小米14手机'),(49,15,'user2',-49990,'兑换','商品：小米14手机 | 扣减积分：49990','2025-12-08 11:20:22',1,'小米14手机'),(50,15,'user2',49990,'消费','商品：小米14手机 | 新增积分：49990','2025-12-08 11:27:07',1,'小米14手机'),(51,15,'user2',-20,'兑换','商品：矿泉水 | 扣减积分：20','2025-12-08 11:27:11',4,'矿泉水'),(52,15,'user2',49990,'消费','商品：小米14手机 | 新增积分：49990','2025-12-08 11:27:27',1,'小米14手机'),(53,15,'user2',3,'消费','买了一包亲嘴烧（消费商品：辣条×1，单价：5.00元，自动加积分：3（价格÷2））','2025-12-08 11:28:18',11,'辣条'),(54,15,'user2',2500,'消费','商品：小米14手机 | 新增积分：2500','2025-12-08 12:06:33',1,'小米14手机'),(55,15,'user2',-50,'兑换','商品：泡面 | 扣减积分：50','2025-12-08 12:06:40',9,'泡面'),(56,15,'user2',2500,'消费','商品：小米14手机 | 新增积分：2500','2025-12-08 12:06:46',1,'小米14手机'),(57,15,'user2',2500,'消费','商品：小米14手机 | 新增积分：2500','2025-12-08 12:06:48',1,'小米14手机'),(58,15,'user2',2500,'消费','商品：小米14手机 | 新增积分：2500','2025-12-08 12:06:50',1,'小米14手机'),(59,15,'user2',40000,'消费','商品：鸭子 | 新增积分：40000','2025-12-08 12:06:57',26,'鸭子'),(60,15,'user2',40000,'消费','商品：鸭子 | 新增积分：40000','2025-12-08 12:06:59',26,'鸭子'),(61,15,'user2',40000,'消费','商品：鸭子 | 新增积分：40000','2025-12-08 12:07:04',26,'鸭子'),(62,15,'user2',-800000,'兑换','商品：鸭子 | 扣减积分：800000','2025-12-08 12:07:05',26,'鸭子'),(63,15,'user2',-800,'兑换','商品：大米 | 扣减积分：800','2025-12-08 12:07:13',25,'大米'),(64,15,'user2',-800,'兑换','商品：大米 | 扣减积分：800','2025-12-08 12:07:14',25,'大米'),(67,15,'user2',40,'消费','商品：大米 | 新增积分：40','2025-12-08 12:28:37',25,'大米'),(68,15,'user2',-800,'兑换','商品：大米 | 扣减积分：800','2025-12-08 12:28:45',25,'大米'),(69,15,'user2',-800,'兑换','商品：大米 | 扣减积分：800','2025-12-08 12:28:48',25,'大米'),(70,15,'user2',-800,'兑换','商品：大米 | 扣减积分：800','2025-12-08 12:28:53',25,'大米'),(71,15,'user2',-350,'兑换','商品：沐浴露 | 扣减积分：350','2025-12-08 12:28:55',14,'沐浴露'),(72,15,'user2',-299,'兑换','商品：瑞士卷 | 扣减积分：299','2025-12-08 12:33:50',27,'瑞士卷'),(73,15,'user2',-299,'兑换','商品：瑞士卷 | 扣减积分：299','2025-12-08 12:33:54',27,'瑞士卷'),(74,15,'user2',-299,'兑换','商品：瑞士卷 | 扣减积分：299','2025-12-08 12:33:56',27,'瑞士卷'),(75,15,'user2',-299,'兑换','商品：瑞士卷 | 扣减积分：299','2025-12-08 12:33:58',27,'瑞士卷'),(76,15,'user2',-299,'兑换','商品：瑞士卷 | 扣减积分：299','2025-12-08 12:34:02',27,'瑞士卷'),(77,15,'user2',40000,'消费','商品：鸭子 | 新增积分：40000','2025-12-08 12:36:01',26,'鸭子'),(78,15,'user2',-20,'兑换','商品：矿泉水 | 扣减积分：20','2025-12-08 12:40:01',4,'矿泉水'),(79,15,'user2',1,'消费','商品：矿泉水 | 新增积分：1','2025-12-08 12:40:05',4,'矿泉水'),(80,15,'user2',-299,'兑换','商品：瑞士卷 | 扣减积分：299','2025-12-08 12:40:30',27,'瑞士卷'),(81,15,'user2',-20,'兑换','商品：矿泉水 | 扣减积分：20','2025-12-08 12:40:44',4,'矿泉水'),(82,15,'user2',-49990,'兑换','商品：小米14手机 | 扣减积分：49990','2025-12-08 12:43:21',1,'小米14手机'),(83,15,'user2',-20,'兑换','商品：矿泉水 | 扣减积分：20','2025-12-08 12:43:28',4,'矿泉水'),(84,15,'user2',-20,'兑换','商品：矿泉水 | 扣减积分：20','2025-12-08 12:43:41',4,'矿泉水'),(85,15,'user2',-20,'兑换','商品：矿泉水 | 扣减积分：20','2025-12-08 12:46:13',4,'矿泉水'),(86,15,'user2',-20,'兑换','商品：矿泉水 | 扣减积分：20','2025-12-08 12:46:18',4,'矿泉水'),(87,15,'user2',1,'消费','商品：矿泉水 | 新增积分：1','2025-12-08 12:46:37',4,'矿泉水'),(88,15,'user2',-20,'兑换','商品：矿泉水 | 扣减积分：20','2025-12-08 12:46:39',4,'矿泉水'),(89,15,'user2',-20,'兑换','商品：矿泉水 | 扣减积分：20','2025-12-08 12:46:49',4,'矿泉水'),(90,15,'user2',-20,'兑换','商品：矿泉水 | 扣减积分：20','2025-12-08 13:32:44',4,'矿泉水'),(91,15,'user2',-20,'兑换','商品：矿泉水 | 扣减积分：20','2025-12-08 13:32:47',4,'矿泉水'),(92,15,'user2',-20,'兑换','商品：矿泉水 | 扣减积分：20','2025-12-08 13:32:51',4,'矿泉水'),(93,15,'user2',-20,'兑换','商品：矿泉水 | 扣减积分：20','2025-12-08 13:46:45',4,'矿泉水'),(94,15,'user2',-20,'兑换','商品：矿泉水 | 扣减积分：20','2025-12-08 13:46:48',4,'矿泉水'),(95,15,'user2',-20,'兑换','商品：矿泉水 | 扣减积分：20','2025-12-08 13:46:49',4,'矿泉水'),(96,15,'user2',-20,'兑换','商品：矿泉水 | 扣减积分：20','2025-12-08 13:46:49',4,'矿泉水'),(97,15,'user2',-20,'兑换','商品：矿泉水 | 扣减积分：20','2025-12-08 13:46:49',4,'矿泉水'),(98,15,'user2',-20,'兑换','商品：矿泉水 | 扣减积分：20','2025-12-08 13:46:49',4,'矿泉水'),(99,15,'user2',-20,'兑换','商品：矿泉水 | 扣减积分：20','2025-12-08 13:46:50',4,'矿泉水'),(100,15,'user2',-20,'兑换','商品：矿泉水 | 扣减积分：20','2025-12-08 13:46:50',4,'矿泉水'),(101,15,'user2',-20,'兑换','商品：矿泉水 | 扣减积分：20','2025-12-08 13:46:50',4,'矿泉水'),(102,15,'user2',-20,'兑换','商品：矿泉水 | 扣减积分：20','2025-12-08 13:46:50',4,'矿泉水'),(103,15,'user2',-20,'兑换','商品：矿泉水 | 扣减积分：20','2025-12-08 13:46:50',4,'矿泉水'),(104,15,'user2',-20,'兑换','商品：矿泉水 | 扣减积分：20','2025-12-08 13:46:51',4,'矿泉水'),(105,15,'user2',-20,'兑换','商品：矿泉水 | 扣减积分：20','2025-12-08 13:46:51',4,'矿泉水'),(106,15,'user2',-20,'兑换','商品：矿泉水 | 扣减积分：20','2025-12-08 13:49:50',4,'矿泉水'),(107,15,'user2',-299,'兑换','商品：瑞士卷 | 扣减积分：299','2025-12-08 14:38:38',27,'瑞士卷'),(108,15,'user2',-20,'兑换','商品：矿泉水 | 扣减积分：20','2025-12-08 14:38:44',4,'矿泉水'),(109,15,'user2',-20,'兑换','商品：矿泉水 | 扣减积分：20','2025-12-08 14:44:32',4,'矿泉水'),(110,15,'user2',-20,'兑换','商品：矿泉水 | 扣减积分：20','2025-12-08 14:44:42',4,'矿泉水'),(111,15,'user2',1,'消费','商品：矿泉水 | 新增积分：1','2025-12-08 14:44:45',4,'矿泉水'),(112,15,'user2',1,'消费','商品：矿泉水 | 新增积分：1','2025-12-08 14:44:47',4,'矿泉水'),(113,15,'user2',40000,'消费','商品：鸭子 | 新增积分：40000','2025-12-08 14:45:53',26,'鸭子'),(114,15,'user2',-50,'兑换','商品：泡面 | 扣减积分：50','2025-12-17 10:35:28',9,'泡面'),(115,2,'user1',1,'消费','商品：矿泉水 | 新增积分：1','2025-12-17 18:19:22',4,'矿泉水'),(116,2,'user1',-20,'兑换','商品：矿泉水 | 扣减积分：20','2025-12-17 18:24:51',4,'矿泉水'),(117,2,'user1',1,'消费','消费商品：矿泉水×1（单价：2.00元），自动加积分：1（价格÷2）','2025-12-17 19:08:56',4,'矿泉水'),(118,15,'user2',-49990,'兑换','（商品：小米14手机×1，扣减积分：49990）','2025-12-17 19:09:28',1,'小米14手机'),(119,15,'user2',1,'消费','渴了（消费商品：矿泉水×1，单价：2.00元，自动加积分：1（价格÷2））','2025-12-18 13:59:25',4,'矿泉水'),(120,15,'user2',-50,'兑换','饿了（商品：泡面×1，扣减积分：50）','2025-12-18 13:59:57',9,'泡面'),(121,15,'user2',1,'消费','渴了（消费商品：矿泉水×1，单价：2.00元，自动加积分：1（价格÷2））','2025-12-18 14:03:29',4,'矿泉水'),(122,15,'user2',-50,'兑换','饿了（商品：辣条×1，扣减积分：50）','2025-12-18 14:03:52',11,'辣条'),(123,15,'user2',2500,'消费','商品：小米14手机 | 新增积分：2500','2025-12-18 14:06:07',1,'小米14手机'),(124,15,'user2',-20,'兑换','商品：矿泉水 | 扣减积分：20','2025-12-18 14:06:13',4,'矿泉水'),(125,15,'user2',3,'消费','商品：泡面 | 新增积分：3','2025-12-18 14:06:17',9,'泡面'),(126,2,'user1',18,'消费','商品：沐浴露 | 新增积分：18','2025-12-18 14:06:35',14,'沐浴露');
/*!40000 ALTER TABLE `sys_points_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_user`
--

DROP TABLE IF EXISTS `sys_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_user` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `password` varchar(100) NOT NULL COMMENT '密码（BCrypt加密）',
  `phone` varchar(20) DEFAULT NULL COMMENT '手机号',
  `real_name` varchar(50) DEFAULT NULL COMMENT '真实姓名',
  `points` int DEFAULT '0' COMMENT '总积分',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `status` tinyint DEFAULT '1' COMMENT '状态（1-正常，0-禁用）',
  `role` varchar(20) NOT NULL DEFAULT 'user' COMMENT '用户角色',
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`),
  KEY `idx_username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='系统用户表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_user`
--

LOCK TABLES `sys_user` WRITE;
/*!40000 ALTER TABLE `sys_user` DISABLE KEYS */;
INSERT INTO `sys_user` VALUES (1,'admin','$2a$10$Njh4iixuLIDG2ysQ6WLXYeQ.F9uJ/uRsoaSkhzcFKyidNhWi0cpY6','13800138000','系统管理员',1000,'2025-12-06 03:31:01','2025-12-17 20:17:18',1,'admin'),(2,'user1','$2a$10$fQ26NTxg7IGqYuOcLzhlfOaEgiSvMLD.UNtkpYPmFbhTc1Xdtj2wK','13900139000','张三',74,'2025-12-04 02:55:16','2025-12-18 14:06:35',1,'user'),(15,'user2','$2a$10$lI.hxp6OI5W6LG4Cvud/x.CumnR3nYrffJ44RvZOKjSM0gROQGygG','13900139000','张四',5458,'2025-12-08 11:18:00','2025-12-18 14:06:16',1,'user');
/*!40000 ALTER TABLE `sys_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_info`
--

DROP TABLE IF EXISTS `user_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_info` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `password` varchar(100) NOT NULL COMMENT '加密密码',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_info`
--

LOCK TABLES `user_info` WRITE;
/*!40000 ALTER TABLE `user_info` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_info` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-12-18 16:00:44
