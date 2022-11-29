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

 Date: 29/11/2022 10:16:31
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
-- Records of answer
-- ----------------------------
BEGIN;
COMMIT;

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of exam
-- ----------------------------
BEGIN;
COMMIT;

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
-- Records of exam_ reply
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for per_exam
-- ----------------------------
DROP TABLE IF EXISTS `per_exam`;
CREATE TABLE `per_exam` (
  `id` int unsigned NOT NULL AUTO_INCREMENT COMMENT '权限id',
  `readable` tinyint(1) NOT NULL DEFAULT '1' COMMENT '可查看',
  `exam_id` bigint(16) unsigned zerofill NOT NULL COMMENT '对应的考试id',
  `checkable` tinyint(1) NOT NULL DEFAULT '0' COMMENT '可批改',
  `writealbe` tinyint(1) NOT NULL DEFAULT '0' COMMENT '可修改试卷',
  `user` int(7) unsigned zerofill NOT NULL COMMENT '权限所有人',
  PRIMARY KEY (`id`),
  KEY `exam_id` (`exam_id`),
  KEY `user` (`user`),
  CONSTRAINT `per_exam_ibfk_1` FOREIGN KEY (`exam_id`) REFERENCES `exam` (`exam_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `per_exam_ibfk_2` FOREIGN KEY (`user`) REFERENCES `user_sec` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of per_exam
-- ----------------------------
BEGIN;
COMMIT;

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
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of ques_resource
-- ----------------------------
BEGIN;
INSERT INTO `ques_resource` (`id`, `resource`, `note`, `altertime`, `resource_type`) VALUES (0000000001, 'c0a775077a8822480a2cf4b8fc6f557e.png', '性格截图', '2022-11-22 07:21:45', 2000);
COMMIT;

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
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of question
-- ----------------------------
BEGIN;
INSERT INTO `question` (`que_id`, `que_type`, `que_creator`, `que_alter_time`, `que_file`, `publicable`, `use_time`, `answerd`, `answerd_right`, `title`, `answer_data`) VALUES (0000000003, 0, 0000001, '2022-11-25 10:36:01', 0x7468697320697320612073656C656374, 0, 0, 3, 2, 'Select', '[{\"text\":\"NO\",\"right\":\"0\"},{\"text\":\"YES\",\"right\":\"1\"}]');
INSERT INTO `question` (`que_id`, `que_type`, `que_creator`, `que_alter_time`, `que_file`, `publicable`, `use_time`, `answerd`, `answerd_right`, `title`, `answer_data`) VALUES (0000000004, 1, 0000001, '2022-11-25 10:25:04', 0x7468697320697320612046696C6C426C616E6B, 1, 0, 0, 0, 'FillBlank', '[{\"text\":\"YES\",\"equal\":\"1\"}]');
INSERT INTO `question` (`que_id`, `que_type`, `que_creator`, `que_alter_time`, `que_file`, `publicable`, `use_time`, `answerd`, `answerd_right`, `title`, `answer_data`) VALUES (0000000005, 2, 0000001, '2022-11-22 08:41:59', 0x74686973206973206120416E73776572, 1, 0, 0, 0, 'AnswerQuestion', '[\"Hello world\"]');
INSERT INTO `question` (`que_id`, `que_type`, `que_creator`, `que_alter_time`, `que_file`, `publicable`, `use_time`, `answerd`, `answerd_right`, `title`, `answer_data`) VALUES (0000000006, 0, 0000002, '2022-11-25 09:44:10', 0xE4BDBFE794A82A2AE6B890E8BF91E680A72A2AE69DA5E8A1A8E7A4BAE7AE97E6B395E5A48DE69D82E5BAA6E79A84E58E9FE59BA0E698AFEFBC8820EFBC89E38082215B746869732069732061207069635D28687474703A2F2F6C6F63616C686F73743A383038302F66696C652F7265732F696D6167653F69643D3129, 1, 0, 0, 0, '计算机Test', '[{\"text\":\"A. 可以精确表示算法的复杂度\",\"right\":\"0\"},{\"text\":\"B. 算法的复杂度无法使用时间单位来表示\",\"right\":\"0\"},{\"text\":\"C. 研究者更关心算法的增长趋势\",\"right\":\"1\"},{\"text\":\"D. 我们只研究小规模问题\",\"right\":\"0\"}]');
COMMIT;

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
-- Records of resource_link
-- ----------------------------
BEGIN;
COMMIT;

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
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of tag
-- ----------------------------
BEGIN;
INSERT INTO `tag` (`tag_id`, `tag_name`, `tag_used`) VALUES (00003, 'Test', 3);
INSERT INTO `tag` (`tag_id`, `tag_name`, `tag_used`) VALUES (00008, 'Hello', 3);
INSERT INTO `tag` (`tag_id`, `tag_name`, `tag_used`) VALUES (00011, 'okkk', 0);
COMMIT;

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
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of tag_link
-- ----------------------------
BEGIN;
INSERT INTO `tag_link` (`id`, `tag_id`, `ques_id`) VALUES (4, 00008, 0000000003);
INSERT INTO `tag_link` (`id`, `tag_id`, `ques_id`) VALUES (5, 00003, 0000000004);
INSERT INTO `tag_link` (`id`, `tag_id`, `ques_id`) VALUES (6, 00003, 0000000005);
INSERT INTO `tag_link` (`id`, `tag_id`, `ques_id`) VALUES (7, 00008, 0000000005);
INSERT INTO `tag_link` (`id`, `tag_id`, `ques_id`) VALUES (8, 00003, 0000000006);
COMMIT;

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
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of user_info
-- ----------------------------
BEGIN;
INSERT INTO `user_info` (`id`, `address`, `phone`, `email`, `note`, `level`) VALUES (0000001, '', NULL, 'tongjames@live.com', '', 0);
INSERT INTO `user_info` (`id`, `address`, `phone`, `email`, `note`, `level`) VALUES (0000002, 'fdfs', 'fsdf', 'fsdf', 'sdfs', 0);
COMMIT;

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
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of user_sec
-- ----------------------------
BEGIN;
INSERT INTO `user_sec` (`id`, `name`, `password`, `sec_ques`, `sec_ans`) VALUES (0000001, 'abstergo', '09aaaf8a54f3f5547043b3a5b56f1dd0', 'i am a good man?', 'Yes');
INSERT INTO `user_sec` (`id`, `name`, `password`, `sec_ques`, `sec_ans`) VALUES (0000002, 'test', 'okkkkk', NULL, NULL);
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
