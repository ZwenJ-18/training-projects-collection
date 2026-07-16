-- MySQL dump 10.13  Distrib 8.0.42, for Win64 (x86_64)
--
-- Host: localhost    Database: scdb
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
-- Table structure for table `course`
--

DROP TABLE IF EXISTS `course`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `course` (
  `cno` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `cname` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `cpno` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `ccredit` smallint NOT NULL,
  `ctime` int DEFAULT NULL COMMENT '学时',
  `cdept` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '开课院系',
  `teacher` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '授课教师',
  PRIMARY KEY (`cno`),
  UNIQUE KEY `uk_cname` (`cname`),
  KEY `course_ibfk_1` (`cpno`),
  CONSTRAINT `course_ibfk_1` FOREIGN KEY (`cpno`) REFERENCES `course` (`cno`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `course_chk_1` CHECK ((`ccredit` > 0))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `course`
--

LOCK TABLES `course` WRITE;
/*!40000 ALTER TABLE `course` DISABLE KEYS */;
INSERT INTO `course` VALUES ('C0004','学术英语','C0004',2,30,'外国语学院','张文静'),('C0008','JAVA','C003',3,20,'计算机科学与技术学院','梁燕龙'),('C001','Python程序设计',NULL,4,64,'001','梁燕龙'),('C002','数据库原理','C001',5,30,'计算机科学与技术学院','宋强'),('C003','编程语言基础','C001',3,30,'计算机科学与技术学院','梁燕龙'),('C0088','高中数学','C0004',1,36,'数学系','张明'),('C009','Python进阶','C001',4,64,'电子信息系','梁燕龙');
/*!40000 ALTER TABLE `course` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `department`
--

DROP TABLE IF EXISTS `department`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `department` (
  `dno` char(3) COLLATE utf8mb4_general_ci NOT NULL,
  `dname` varchar(30) COLLATE utf8mb4_general_ci NOT NULL,
  `dmanager` char(8) COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`dno`),
  UNIQUE KEY `uk_dname` (`dname`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `department`
--

LOCK TABLES `department` WRITE;
/*!40000 ALTER TABLE `department` DISABLE KEYS */;
INSERT INTO `department` VALUES ('001','计算机科学与技术学院','T2024002'),('002','电子信息系','T2024002'),('003','数学系','T2024003'),('011','外国语学院','T2024001');
/*!40000 ALTER TABLE `department` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `major`
--

DROP TABLE IF EXISTS `major`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `major` (
  `id` int NOT NULL AUTO_INCREMENT,
  `major_name` varchar(30) COLLATE utf8mb4_general_ci NOT NULL,
  `dept_name` varchar(30) COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `major`
--

LOCK TABLES `major` WRITE;
/*!40000 ALTER TABLE `major` DISABLE KEYS */;
INSERT INTO `major` VALUES (1,'物联网大工程','计算机科学与技术学院'),(3,'电子信息工程','电子信息系'),(4,'数据科学与大数据技术','计算机科学与技术学院'),(5,'英语师范','外国语学院'),(6,'数学师范','数学系'),(8,'中国画','美术学院');
/*!40000 ALTER TABLE `major` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sct`
--

DROP TABLE IF EXISTS `sct`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sct` (
  `sno` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `cno` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `semester` varchar(20) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `score` int DEFAULT '0',
  PRIMARY KEY (`sno`,`cno`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sct`
--

LOCK TABLES `sct` WRITE;
/*!40000 ALTER TABLE `sct` DISABLE KEYS */;
INSERT INTO `sct` VALUES ('202324115103','C0004','2025-2',69),('202324115118','C003','2024-1',100),('202324115122','C003','2024-1',60),('202324115122','C0088','2024-2025-1',56),('202324115131','C0004','2024-2025-1',NULL),('202324115131','C001','2024-2025-1',53),('202324115131','C003','2024-2025-1',NULL);
/*!40000 ALTER TABLE `sct` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `student`
--

DROP TABLE IF EXISTS `student`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `student` (
  `sno` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `sname` varchar(20) COLLATE utf8mb4_general_ci NOT NULL,
  `ssex` varchar(2) COLLATE utf8mb4_general_ci NOT NULL,
  `sage` int NOT NULL,
  `class_name` varchar(20) COLLATE utf8mb4_general_ci NOT NULL,
  `major` varchar(20) COLLATE utf8mb4_general_ci NOT NULL,
  `sdept` varchar(30) COLLATE utf8mb4_general_ci NOT NULL,
  `dno` varchar(3) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '001',
  `phone` varchar(20) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '联系电话',
  PRIMARY KEY (`sno`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `student`
--

LOCK TABLES `student` WRITE;
/*!40000 ALTER TABLE `student` DISABLE KEYS */;
INSERT INTO `student` VALUES ('202324115101','李涵','男',24,'23数本1班','数学师范','电子信息系','002',NULL),('202324115103','明在选','男',21,'23物联网工程1班','英语师范','电子信息系','002',NULL),('202324115105','裴真率','女',24,'23英师1班','软件工程','外国语学院','011',NULL),('202324115112','李莉','女',18,'23数本1班','物联网大工程','计算机科学与技术学院','001',NULL),('202324115118','张文静','男',20,'23物联网工程1班','物联网工程','计算机科学与技术学院','001',NULL),('202324115122','余小鱼','女',22,'23英师2班','英语师范','外国语学院','011',NULL),('202324115131','叶蕴柔','女',21,'23物联网工程1班','物联网工程','计算机科学与技术学院','001',NULL);
/*!40000 ALTER TABLE `student` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_user`
--

DROP TABLE IF EXISTS `sys_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(20) COLLATE utf8mb4_general_ci NOT NULL,
  `password` varchar(100) COLLATE utf8mb4_general_ci NOT NULL,
  `role` varchar(10) COLLATE utf8mb4_general_ci NOT NULL,
  `real_name` varchar(20) COLLATE utf8mb4_general_ci NOT NULL,
  `phone` varchar(20) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '学生联系电话',
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_user`
--

LOCK TABLES `sys_user` WRITE;
/*!40000 ALTER TABLE `sys_user` DISABLE KEYS */;
INSERT INTO `sys_user` VALUES (17,'202324115131','040326','student','叶蕴柔','18898376318'),(18,'202324115118','ZwJ82843695','student','张文静','13794376428'),(19,'admin','123456','admin','系统管理员',NULL),(20,'202324115101','leehan','student','李涵','19965432902'),(21,'202324115106','wunaga','student','吴娜','19965432902'),(22,'202324115103','jeahyun','student','明在选',NULL),(23,'202324115105','shuaier','student','裴真率',NULL),(24,'202324115122','1234567','student','余小鱼',''),(31,'T2024006','dfd4c158aac23a56654fc72b01673aca','teacher','梁燕龙',NULL),(32,'T2024018','56f2b6be71aed8381d7d459d82d3517c','teacher','张文静',NULL);
/*!40000 ALTER TABLE `sys_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `teacher`
--

DROP TABLE IF EXISTS `teacher`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `teacher` (
  `tno` char(8) NOT NULL,
  `tname` varchar(8) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `tsex` char(1) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `tage` int NOT NULL,
  `teb` varchar(10) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `tpt` varchar(10) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `cno1` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `cno2` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `cno3` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`tno`),
  KEY `teacher_ibfk_1` (`cno1`),
  KEY `teacher_ibfk_2` (`cno2`),
  KEY `teacher_ibfk_3` (`cno3`),
  CONSTRAINT `teacher_ibfk_1` FOREIGN KEY (`cno1`) REFERENCES `course` (`cno`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `teacher_ibfk_2` FOREIGN KEY (`cno2`) REFERENCES `course` (`cno`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `teacher_ibfk_3` FOREIGN KEY (`cno3`) REFERENCES `course` (`cno`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `teacher_chk_1` CHECK ((`tsex` in (_utf8mb3'男',_utf8mb3'女'))),
  CONSTRAINT `teacher_chk_2` CHECK ((`tage` between 24 and 60)),
  CONSTRAINT `teacher_chk_4` CHECK ((`tpt` in (_utf8mb3'助教',_utf8mb3'讲师',_utf8mb3'副教授',_utf8mb3'教授')))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `teacher`
--

LOCK TABLES `teacher` WRITE;
/*!40000 ALTER TABLE `teacher` DISABLE KEYS */;
INSERT INTO `teacher` VALUES ('T2024001','张明','男',35,'博士','副教授',NULL,NULL,NULL),('T2024003','王强','男',30,'学士','讲师',NULL,NULL,NULL),('T2024004','宋强','男',45,'硕士','副教授',NULL,NULL,NULL),('T2024006','梁燕龙','女',42,'硕士','教授','C001','C002',NULL),('T2024018','张文静','女',32,'硕士','教授','C0088',NULL,NULL);
/*!40000 ALTER TABLE `teacher` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-12-18 15:11:59
