/*
 Navicat Premium Data Transfer

 Source Server         : docker
 Source Server Type    : MySQL
 Source Server Version : 80031 (8.0.31)
 Source Host           : localhost:3310
 Source Schema         : exam_db

 Target Server Type    : MySQL
 Target Server Version : 80031 (8.0.31)
 File Encoding         : 65001

 Date: 20/01/2023 10:51:26
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for answer
-- ----------------------------
DROP TABLE IF EXISTS `answer`;
CREATE TABLE `answer` (
  `id` int(10) unsigned zerofill NOT NULL AUTO_INCREMENT COMMENT '标准答案id',
  `ques_id` int(10) unsigned zerofill NOT NULL COMMENT '对应题目id',
  `answer_content` blob NOT NULL COMMENT '标准答案内容',
  `answer_analysis` mediumblob COMMENT '解题分析',
  PRIMARY KEY (`id`),
  KEY `ques_id` (`ques_id`),
  CONSTRAINT `answer_ibfk_1` FOREIGN KEY (`ques_id`) REFERENCES `question` (`que_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for exam
-- ----------------------------
DROP TABLE IF EXISTS `exam`;
CREATE TABLE `exam` (
  `exam_id` bigint(16) unsigned zerofill NOT NULL AUTO_INCREMENT COMMENT '考试id',
  `exam_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '考试名称',
  `exam_begin` datetime NOT NULL COMMENT '考试开始时间',
  `exam_len` int NOT NULL DEFAULT '90' COMMENT '考试长度（单位：分钟）',
  `exam_file` mediumblob NOT NULL COMMENT '考试文件(sqlite db)',
  `exam_note` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '其他考试文件',
  `exam_status` tinyint NOT NULL DEFAULT '0' COMMENT '考试状态',
  PRIMARY KEY (`exam_id`),
  UNIQUE KEY `exam_name` (`exam_name`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for exam_ reply
-- ----------------------------
DROP TABLE IF EXISTS `exam_ reply`;
CREATE TABLE `exam_ reply` (
  `id` int(10) unsigned zerofill NOT NULL AUTO_INCREMENT COMMENT '答题id',
  `exam_id` bigint(16) unsigned zerofill NOT NULL COMMENT '答题对应的试卷',
  `exam_number` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '考号',
  `person_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '身份证号',
  `reply_file` varchar(128) NOT NULL COMMENT '答题卷文件',
  `status` int NOT NULL DEFAULT '0' COMMENT '批阅状态',
  `check_file` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '批阅数据文件',
  PRIMARY KEY (`id`),
  UNIQUE KEY `reply_file` (`reply_file`),
  KEY `exam_id` (`exam_id`),
  KEY `exam_number` (`exam_number`),
  CONSTRAINT `exam_ reply_ibfk_1` FOREIGN KEY (`exam_id`) REFERENCES `exam` (`exam_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for per_exam
-- ----------------------------
DROP TABLE IF EXISTS `per_exam`;
CREATE TABLE `per_exam` (
  `id` int unsigned NOT NULL AUTO_INCREMENT COMMENT '权限id',
  `readable` tinyint(1) NOT NULL DEFAULT '1' COMMENT '可查看',
  `exam_id` bigint(16) unsigned zerofill NOT NULL COMMENT '对应的考试id',
  `checkable` tinyint(1) NOT NULL DEFAULT '0' COMMENT '可批改',
  `writeable` tinyint(1) NOT NULL DEFAULT '0' COMMENT '可修改试卷',
  `user` int(7) unsigned zerofill NOT NULL COMMENT '权限所有人',
  PRIMARY KEY (`id`),
  KEY `exam_id` (`exam_id`),
  KEY `user` (`user`),
  CONSTRAINT `per_exam_ibfk_1` FOREIGN KEY (`exam_id`) REFERENCES `exam` (`exam_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `per_exam_ibfk_2` FOREIGN KEY (`user`) REFERENCES `user_sec` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for ques_resource
-- ----------------------------
DROP TABLE IF EXISTS `ques_resource`;
CREATE TABLE `ques_resource` (
  `id` int(10) unsigned zerofill NOT NULL AUTO_INCREMENT,
  `resource` varchar(96) NOT NULL,
  `note` text,
  `altertime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `resource_type` smallint(3) unsigned zerofill NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `resource` (`resource`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for question
-- ----------------------------
DROP TABLE IF EXISTS `question`;
CREATE TABLE `question` (
  `que_id` int(10) unsigned zerofill NOT NULL AUTO_INCREMENT COMMENT '题目id',
  `que_type` smallint NOT NULL COMMENT '题目类型',
  `que_creator` int(7) unsigned zerofill NOT NULL COMMENT '题目创建者',
  `que_alter_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '题目修改时间',
  `que_file` mediumblob NOT NULL COMMENT '题目细节文件',
  `publicable` tinyint unsigned NOT NULL DEFAULT '1' COMMENT '是否公开',
  `use_time` int NOT NULL DEFAULT '0' COMMENT '题目使用次数',
  `answerd` int NOT NULL DEFAULT '0' COMMENT '题目回答次数',
  `answerd_right` float NOT NULL DEFAULT '0' COMMENT '题目回答正确情况，一次最多为1',
  `title` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '题目标题',
  `answer_data` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '答案文件',
  PRIMARY KEY (`que_id`),
  KEY `que_creator` (`que_creator`),
  CONSTRAINT `question_ibfk_1` FOREIGN KEY (`que_creator`) REFERENCES `user_sec` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for resource_link
-- ----------------------------
DROP TABLE IF EXISTS `resource_link`;
CREATE TABLE `resource_link` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `ques_id` int(10) unsigned zerofill NOT NULL,
  `resource_id` int(10) unsigned zerofill NOT NULL,
  PRIMARY KEY (`id`),
  KEY `ques_id` (`ques_id`),
  KEY `resource_id` (`resource_id`),
  CONSTRAINT `resource_link_ibfk_1` FOREIGN KEY (`ques_id`) REFERENCES `question` (`que_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `resource_link_ibfk_2` FOREIGN KEY (`resource_id`) REFERENCES `ques_resource` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for tag
-- ----------------------------
DROP TABLE IF EXISTS `tag`;
CREATE TABLE `tag` (
  `tag_id` smallint(5) unsigned zerofill NOT NULL AUTO_INCREMENT COMMENT '题目标签id',
  `tag_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '标签名',
  `tag_used` int unsigned NOT NULL DEFAULT '0' COMMENT '标签使用次数',
  PRIMARY KEY (`tag_id`),
  UNIQUE KEY `tag_name` (`tag_name`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for tag_link
-- ----------------------------
DROP TABLE IF EXISTS `tag_link`;
CREATE TABLE `tag_link` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `tag_id` smallint(5) unsigned zerofill NOT NULL,
  `ques_id` int(10) unsigned zerofill NOT NULL,
  PRIMARY KEY (`id`),
  KEY `ques_id` (`ques_id`),
  KEY `tag_id` (`tag_id`),
  CONSTRAINT `tag_link_ibfk_1` FOREIGN KEY (`ques_id`) REFERENCES `question` (`que_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `tag_link_ibfk_2` FOREIGN KEY (`tag_id`) REFERENCES `tag` (`tag_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for user_info
-- ----------------------------
DROP TABLE IF EXISTS `user_info`;
CREATE TABLE `user_info` (
  `id` int(7) unsigned zerofill NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `address` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '用户住址',
  `phone` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '用户手机号',
  `email` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '用户email',
  `note` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '用户个性签名',
  `level` tinyint NOT NULL DEFAULT '0' COMMENT '用户权限等级',
  PRIMARY KEY (`id`),
  CONSTRAINT `user_info_ibfk_1` FOREIGN KEY (`id`) REFERENCES `user_sec` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for user_sec
-- ----------------------------
DROP TABLE IF EXISTS `user_sec`;
CREATE TABLE `user_sec` (
  `id` int(7) unsigned zerofill NOT NULL AUTO_INCREMENT COMMENT '用户安全信息id',
  `name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户名',
  `password` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户密码',
  `sec_ques` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '安全问题',
  `sec_ans` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '安全问题答案',
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

SET FOREIGN_KEY_CHECKS = 1;
