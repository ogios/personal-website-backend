/*
 Navicat Premium Data Transfer

 Source Server         : ogios.dns.army_3306
 Source Server Type    : MySQL
 Source Server Version : 80033
 Source Host           : ogios.dns.army:3306
 Source Schema         : git_backend

 Target Server Type    : MySQL
 Target Server Version : 80033
 File Encoding         : 65001

 Date: 26/07/2023 16:48:29
*/

CREATE DATABASE git_backend;
USE git_backend;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_blog
-- ----------------------------
DROP TABLE IF EXISTS `t_blog`;
CREATE TABLE `t_blog`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `title` varchar(255)  NOT NULL COMMENT '标题',
  `head_img` varchar(255)  NULL DEFAULT NULL COMMENT '封面地址',
  `content` varchar(255)  NOT NULL COMMENT '文章文件名',
  `summary` varchar(255)  NULL DEFAULT NULL,
  `is_finished` tinyint(1) UNSIGNED ZEROFILL NOT NULL DEFAULT 0 COMMENT '0-草稿 | 1-完成',
  `is_top` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否被置顶',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `owner_id` int NOT NULL COMMENT '所属人id',
  `update_user_id` int NOT NULL COMMENT '更新用户',
  `category_id` int NOT NULL COMMENT '文章分类',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `top_desc`(`is_top` DESC) USING BTREE,
  INDEX `category_id`(`category_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for t_category
-- ----------------------------
DROP TABLE IF EXISTS `t_category`;
CREATE TABLE `t_category`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '类别id',
  `name` varchar(255)  NOT NULL COMMENT '类别名字',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for t_tab
-- ----------------------------
DROP TABLE IF EXISTS `t_tab`;
CREATE TABLE `t_tab`  (
  `name` varchar(255)  NOT NULL COMMENT 'tab的名称',
  `blog_id` bigint NOT NULL COMMENT '所属文章的id',
  INDEX `blog_tabs`(`blog_id` ASC) USING BTREE,
  CONSTRAINT `blog_tabs` FOREIGN KEY (`blog_id`) REFERENCES `t_blog` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for t_user
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(255)  NOT NULL,
  `password` varchar(255)  NOT NULL,
  `is_admin` tinyint(1) UNSIGNED ZEROFILL NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `username_unique`(`username` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for t_user_blog
-- ----------------------------
DROP TABLE IF EXISTS `t_user_blog`;
CREATE TABLE `t_user_blog`  (
  `blog_id` bigint NOT NULL,
  `user_id` int NOT NULL,
  PRIMARY KEY (`blog_id`, `user_id`) USING BTREE,
  INDEX `user_foreign`(`user_id` ASC) USING BTREE,
  CONSTRAINT `blog_foreign` FOREIGN KEY (`blog_id`) REFERENCES `t_blog` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `user_foreign` FOREIGN KEY (`user_id`) REFERENCES `t_user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB  ROW_FORMAT = DYNAMIC;

SET FOREIGN_KEY_CHECKS = 1;
