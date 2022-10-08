/*
Navicat MySQL Data Transfer

Source Server         : localhost_3306
Source Server Version : 80030
Source Host           : localhost:3306
Source Database       : exam_db

Target Server Type    : MYSQL
Target Server Version : 80030
File Encoding         : 65001

Date: 2022-10-08 10:31:42
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `exam`
-- ----------------------------
DROP TABLE IF EXISTS `exam`;
CREATE TABLE `exam` (
  `exam_id` bigint(16) unsigned zerofill NOT NULL AUTO_INCREMENT COMMENT '考试id',
  `exam_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '考试名称',
  `exam_begin` datetime NOT NULL COMMENT '考试开始时间',
  `exam_len` int NOT NULL DEFAULT '90' COMMENT '考试长度（单位：分钟）',
  `exam_file` mediumblob NOT NULL COMMENT '试题文件(sqlite db)',
  `exam_note` blob COMMENT '其他考试文件',
  `exam_status` tinyint DEFAULT NULL,
  `exam_result` blob,
  PRIMARY KEY (`exam_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of exam
-- ----------------------------

-- ----------------------------
-- Table structure for `question`
-- ----------------------------
DROP TABLE IF EXISTS `question`;
CREATE TABLE `question` (
  `que_id` int(10) unsigned zerofill NOT NULL,
  `que_type` smallint NOT NULL,
  `que_creator` int(7) unsigned zerofill NOT NULL,
  `que_alter_time` datetime NOT NULL,
  `que_file` mediumblob NOT NULL,
  `que_ans` mediumblob NOT NULL,
  `tag` smallint(5) unsigned zerofill DEFAULT NULL,
  `use_time` int NOT NULL DEFAULT '0',
  `answerd` int NOT NULL DEFAULT '0',
  `answerd_right` int NOT NULL DEFAULT '0',
  PRIMARY KEY (`que_id`),
  KEY `que_creator` (`que_creator`),
  CONSTRAINT `question_ibfk_1` FOREIGN KEY (`que_creator`) REFERENCES `user_sec` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of question
-- ----------------------------

-- ----------------------------
-- Table structure for `tag`
-- ----------------------------
DROP TABLE IF EXISTS `tag`;
CREATE TABLE `tag` (
  `tag_id` smallint(5) unsigned zerofill NOT NULL AUTO_INCREMENT,
  `tag_name` varchar(128) NOT NULL,
  `tag_used` int NOT NULL DEFAULT '0',
  PRIMARY KEY (`tag_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of tag
-- ----------------------------

-- ----------------------------
-- Table structure for `user_info`
-- ----------------------------
DROP TABLE IF EXISTS `user_info`;
CREATE TABLE `user_info` (
  `id` int(7) unsigned zerofill NOT NULL AUTO_INCREMENT,
  `address` varchar(128) DEFAULT NULL,
  `phone` varchar(128) DEFAULT NULL,
  `email` varchar(128) DEFAULT NULL,
  `note` varchar(256) DEFAULT NULL,
  `level` int NOT NULL DEFAULT '0',
  `score` int NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  CONSTRAINT `user_info_ibfk_1` FOREIGN KEY (`id`) REFERENCES `user_sec` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of user_info
-- ----------------------------

-- ----------------------------
-- Table structure for `user_sec`
-- ----------------------------
DROP TABLE IF EXISTS `user_sec`;
CREATE TABLE `user_sec` (
  `id` int(7) unsigned zerofill NOT NULL AUTO_INCREMENT,
  `name` varchar(32) NOT NULL,
  `password` varchar(32) NOT NULL,
  `sec_ques` varchar(64) DEFAULT NULL,
  `sec_ans` varchar(64) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of user_sec
-- ----------------------------
INSERT INTO `user_sec` VALUES ('0000001', 'jack', 'tongyi0923', null, null);
