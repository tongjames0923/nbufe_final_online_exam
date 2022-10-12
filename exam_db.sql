# ************************************************************
# Sequel Ace SQL dump
# 版本号： 20035
#
# https://sequel-ace.com/
# https://github.com/Sequel-Ace/Sequel-Ace
#
# 主机: localhost (MySQL 8.0.30)
# 数据库: exam_db
# 生成时间: 2022-10-12 03:57:05 +0000
# ************************************************************


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
SET NAMES utf8mb4;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE='NO_AUTO_VALUE_ON_ZERO', SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


# 转储表 answer
# ------------------------------------------------------------

CREATE TABLE `answer` (
  `id` int(10) unsigned zerofill NOT NULL AUTO_INCREMENT,
  `ques_id` int(10) unsigned zerofill NOT NULL,
  `answer_content` blob NOT NULL,
  `answer_analysis` mediumblob,
  PRIMARY KEY (`id`),
  KEY `ques_id` (`ques_id`),
  CONSTRAINT `answer_ibfk_1` FOREIGN KEY (`ques_id`) REFERENCES `question` (`que_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;



# 转储表 exam
# ------------------------------------------------------------

CREATE TABLE `exam` (
  `exam_id` bigint(16) unsigned zerofill NOT NULL AUTO_INCREMENT COMMENT '考试id',
  `exam_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '考试名称',
  `exam_begin` datetime NOT NULL COMMENT '考试开始时间',
  `exam_len` int NOT NULL DEFAULT '90' COMMENT '考试长度（单位：分钟）',
  `exam_file` varchar(128) NOT NULL COMMENT '试题文件(sqlite db)',
  `exam_note` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '其他考试文件',
  `exam_status` tinyint NOT NULL DEFAULT '0' COMMENT '考试状态',
  PRIMARY KEY (`exam_id`),
  UNIQUE KEY `exam_name` (`exam_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;



# 转储表 exam_ reply
# ------------------------------------------------------------

CREATE TABLE `exam_ reply` (
  `id` int(10) unsigned zerofill NOT NULL AUTO_INCREMENT,
  `exam_id` bigint(16) unsigned zerofill NOT NULL,
  `exam_number` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `person_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `reply_file` mediumblob NOT NULL,
  `status` int NOT NULL DEFAULT '0',
  `check_file` varchar(128) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `exam_id` (`exam_id`),
  KEY `exam_number` (`exam_number`),
  CONSTRAINT `exam_ reply_ibfk_1` FOREIGN KEY (`exam_id`) REFERENCES `exam` (`exam_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;



# 转储表 per_exam
# ------------------------------------------------------------

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
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;



# 转储表 ques_resource
# ------------------------------------------------------------

CREATE TABLE `ques_resource` (
  `id` int(10) unsigned zerofill NOT NULL AUTO_INCREMENT,
  `resource` varchar(96) NOT NULL,
  `note` text,
  `altertime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `resource_type` tinyint(3) unsigned zerofill NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `resource` (`resource`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;



# 转储表 question
# ------------------------------------------------------------

CREATE TABLE `question` (
  `que_id` int(10) unsigned zerofill NOT NULL AUTO_INCREMENT COMMENT '题目id',
  `que_type` smallint NOT NULL COMMENT '题目类型',
  `que_creator` int(7) unsigned zerofill NOT NULL COMMENT '题目创建者',
  `que_alter_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '题目修改时间',
  `que_file` mediumblob NOT NULL COMMENT '题目细节文件',
  `publicable` tinyint unsigned NOT NULL DEFAULT '1' COMMENT '是否公开',
  `tag` smallint(5) unsigned zerofill DEFAULT NULL COMMENT '题目标签',
  `use_time` int NOT NULL DEFAULT '0' COMMENT '题目使用次数',
  `answerd` int NOT NULL DEFAULT '0' COMMENT '题目回答次数',
  `answerd_right` float NOT NULL DEFAULT '0' COMMENT '题目回答正确情况，一次最多为1',
  PRIMARY KEY (`que_id`),
  KEY `que_creator` (`que_creator`),
  KEY `tag` (`tag`),
  CONSTRAINT `question_ibfk_1` FOREIGN KEY (`que_creator`) REFERENCES `user_sec` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `question_ibfk_2` FOREIGN KEY (`tag`) REFERENCES `tag` (`tag_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;



# 转储表 resource_link
# ------------------------------------------------------------

CREATE TABLE `resource_link` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `ques_id` int(10) unsigned zerofill NOT NULL,
  `resource_id` int(10) unsigned zerofill NOT NULL,
  PRIMARY KEY (`id`),
  KEY `ques_id` (`ques_id`),
  KEY `resource_id` (`resource_id`),
  CONSTRAINT `resource_link_ibfk_1` FOREIGN KEY (`ques_id`) REFERENCES `question` (`que_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `resource_link_ibfk_2` FOREIGN KEY (`resource_id`) REFERENCES `ques_resource` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;



# 转储表 tag
# ------------------------------------------------------------

CREATE TABLE `tag` (
  `tag_id` smallint(5) unsigned zerofill NOT NULL AUTO_INCREMENT COMMENT '题目标签id',
  `tag_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '标签名',
  `tag_used` int NOT NULL DEFAULT '0' COMMENT '标签使用次数',
  PRIMARY KEY (`tag_id`),
  UNIQUE KEY `tag_name` (`tag_name`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;



# 转储表 user_info
# ------------------------------------------------------------

CREATE TABLE `user_info` (
  `id` int(7) unsigned zerofill NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `address` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '用户住址',
  `phone` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '用户手机号',
  `email` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '用户email',
  `note` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '用户个性签名',
  PRIMARY KEY (`id`),
  CONSTRAINT `user_info_ibfk_1` FOREIGN KEY (`id`) REFERENCES `user_sec` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;



# 转储表 user_sec
# ------------------------------------------------------------

CREATE TABLE `user_sec` (
  `id` int(7) unsigned zerofill NOT NULL AUTO_INCREMENT COMMENT '用户安全信息id',
  `name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户名',
  `password` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户密码',
  `sec_ques` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '安全问题',
  `sec_ans` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '安全问题答案',
  `level` tinyint NOT NULL DEFAULT '0' COMMENT '用户权限等级',
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;




/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
