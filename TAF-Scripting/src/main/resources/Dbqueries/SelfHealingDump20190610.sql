CREATE DATABASE  IF NOT EXISTS `selfhealing` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `selfhealing`;
-- MySQL dump 10.13  Distrib 5.6.13, for Win32 (x86)
--
-- Host: localhost    Database: selfhealing
-- ------------------------------------------------------
-- Server version	5.6.17

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `applicationdetails`
--

DROP TABLE IF EXISTS `applicationdetails`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `applicationdetails` (
  `applicationID` int(11) NOT NULL AUTO_INCREMENT,
  `applicationName` varchar(45) NOT NULL,
  PRIMARY KEY (`applicationID`)
) ENGINE=InnoDB AUTO_INCREMENT=101 DEFAULT CHARSET=utf8 COMMENT='This table depicts the priority of locators for elements';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `applicationdetails`
--

LOCK TABLES `applicationdetails` WRITE;
/*!40000 ALTER TABLE `applicationdetails` DISABLE KEYS */;
INSERT INTO `applicationdetails` VALUES (100,'application1');
/*!40000 ALTER TABLE `applicationdetails` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `locatorpriority`
--

DROP TABLE IF EXISTS `locatorpriority`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `locatorpriority` (
  `applicationID` int(11) NOT NULL,
  `priority1` varchar(255) NOT NULL,
  `priority2` varchar(255) NOT NULL,
  `priority3` varchar(255) NOT NULL,
  KEY `fk_applicationID` (`applicationID`),
  CONSTRAINT `locatorpriority_ibfk_1` FOREIGN KEY (`applicationID`) REFERENCES `applicationdetails` (`applicationID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='This table contains element locators priority across applications';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `locatorpriority`
--

LOCK TABLES `locatorpriority` WRITE;
/*!40000 ALTER TABLE `locatorpriority` DISABLE KEYS */;
INSERT INTO `locatorpriority` VALUES (100,'id','name','xpath');
/*!40000 ALTER TABLE `locatorpriority` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `objectlocators`
--

DROP TABLE IF EXISTS `objectlocators`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `objectlocators` (
  `objectID` int(11) NOT NULL,
  `objname` varchar(45) NOT NULL,
  `ID` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `xpath` varchar(255) DEFAULT NULL,
  `applicationID` int(11) NOT NULL,
  PRIMARY KEY (`objectID`),
  KEY `fk_appid` (`applicationID`),
  CONSTRAINT `objectlocators_ibfk_1` FOREIGN KEY (`applicationID`) REFERENCES `applicationdetails` (`applicationID`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Table to store object hierarchy details';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `objectlocators`
--

LOCK TABLES `objectlocators` WRITE;
/*!40000 ALTER TABLE `objectlocators` DISABLE KEYS */;
INSERT INTO `objectlocators` VALUES (1,'txtlogin','null','q','//input[@name=\'q\']',100);
/*!40000 ALTER TABLE `objectlocators` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2019-06-10 19:36:52
