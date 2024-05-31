/*
 Navicat Premium Data Transfer

 Source Server         : GageRain
 Source Server Type    : MySQL
 Source Server Version : 80028
 Source Host           : localhost:3306
 Source Schema         : vue-books-project

 Target Server Type    : MySQL
 Target Server Version : 80028
 File Encoding         : 65001

 Date: 06/06/2022 18:15:54
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for borrow_book
-- ----------------------------
DROP TABLE IF EXISTS `borrow_book`;
CREATE TABLE `borrow_book`  (
  `borrow_id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `book_id` int NULL DEFAULT NULL COMMENT '图书id',
  `barcode` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT 'NULL' COMMENT '条形码的生成码，ISBN-copy_num',
  `reader_id` int NULL DEFAULT NULL COMMENT '读者id',
  `borrow_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '借书时间',
  `expect_return_time` datetime NULL DEFAULT (`borrow_time` + interval 10 day) COMMENT '预期还书时间，默认10天后',
  `return_time` datetime NULL DEFAULT NULL COMMENT '还书时间',
  `apply_status` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '0: 待审核 1： 已审核  2：拒绝',
  `borrow_status` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '0:审核中 1:在借中  2：已还   3：拒绝或异常',
  `return_status` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '1: 正常还书 2：异常还书 3：丢失书',
  `excepion_text` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '异常还书备注',
  `apply_text` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '审核拒绝备注',
  PRIMARY KEY (`borrow_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 72 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of borrow_book
-- ----------------------------
INSERT INTO `borrow_book` VALUES (72, 9, '9781593278885-10', 25, '2022-05-26 00:01:53', '2022-06-05 00:01:53', NULL, '1', '1', NULL, NULL, NULL);

-- ----------------------------
-- Table structure for sys_bookcopies
-- ----------------------------
DROP TABLE IF EXISTS `sys_bookcopies`;
CREATE TABLE `sys_bookcopies`  (
  `copy_id` int NOT NULL AUTO_INCREMENT COMMENT '图书复印本的id',
  `barcode` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '条形码的生成码，ISBN-copy_num',
  `book_id` int NOT NULL COMMENT '图书id',
  `copy_num` int NOT NULL COMMENT '单本图书复印本编号，从1开始计数',
  `borrow_status` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '借阅状态 0：未借  1：已借未还  2: 已丢失或损坏',
  PRIMARY KEY (`copy_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 126 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_bookcopies
-- ----------------------------
INSERT INTO `sys_bookcopies` VALUES (108, '9781593278885-1', 9, 1, '0');
INSERT INTO `sys_bookcopies` VALUES (109, '9781593278885-2', 9, 2, '0');
INSERT INTO `sys_bookcopies` VALUES (110, '9781593278885-3', 9, 3, '0');
INSERT INTO `sys_bookcopies` VALUES (111, '9781593278885-4', 9, 4, '0');
INSERT INTO `sys_bookcopies` VALUES (112, '9781593278885-5', 9, 5, '0');
INSERT INTO `sys_bookcopies` VALUES (113, '9781593278885-6', 9, 6, '0');
INSERT INTO `sys_bookcopies` VALUES (114, '9781593278885-7', 9, 7, '0');
INSERT INTO `sys_bookcopies` VALUES (115, '9781593278885-8', 9, 8, '0');
INSERT INTO `sys_bookcopies` VALUES (116, '9781593278885-9', 9, 9, '0');
INSERT INTO `sys_bookcopies` VALUES (117, '9781593278885-10', 9, 10, '1');
INSERT INTO `sys_bookcopies` VALUES (118, '9787111227090 -1', 10, 1, '0');
INSERT INTO `sys_bookcopies` VALUES (119, '9787111227090 -2', 10, 2, '0');
INSERT INTO `sys_bookcopies` VALUES (120, '9787111227090 -3', 10, 3, '0');
INSERT INTO `sys_bookcopies` VALUES (121, '9787111227090 -4', 10, 4, '0');
INSERT INTO `sys_bookcopies` VALUES (122, '9787111227090 -5', 10, 5, '0');
INSERT INTO `sys_bookcopies` VALUES (123, '9787121088087 -1', 11, 1, '0');
INSERT INTO `sys_bookcopies` VALUES (124, '9787121088087 -2', 11, 2, '0');
INSERT INTO `sys_bookcopies` VALUES (125, '9787121088087 -3', 11, 3, '0');
INSERT INTO `sys_bookcopies` VALUES (126, '9787121088087 -4', 11, 4, '0');

-- ----------------------------
-- Table structure for sys_books
-- ----------------------------
DROP TABLE IF EXISTS `sys_books`;
CREATE TABLE `sys_books`  (
  `book_id` int NOT NULL AUTO_INCREMENT COMMENT '图书id',
  `category_id` int NULL DEFAULT NULL COMMENT '分类id',
  `book_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '图书名称',
  `book_code` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT 'ISBN',
  `book_place_num` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '书架号',
  `book_auther` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '作者',
  `book_product` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '出版社',
  `book_price` decimal(6, 2) NULL DEFAULT 25.00 COMMENT '价格',
  `book_store` int NULL DEFAULT 1 COMMENT '库存',
  `book_floor` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '楼层号',
  PRIMARY KEY (`book_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_books
-- ----------------------------
INSERT INTO `sys_books` VALUES (9, 1, 'C++ Crash Course', '9781593278885', '1', 'Joshua Lospinoso', 'No Starch Press', 59.95, 9, '1');
INSERT INTO `sys_books` VALUES (10, 1, '计算机程序设计艺术（第1卷 英文版・第3版）', '9787111227090 ', '2', '高德纳', '机械工业出版社', 95.00, 5, '2');
INSERT INTO `sys_books` VALUES (11, 1, 'Java程序设计教程', '9787121088087 ', '2', 'Loftus William Lewis John', '电子工业出版社', 85.00, 4, '2');

-- ----------------------------
-- Table structure for sys_category
-- ----------------------------
DROP TABLE IF EXISTS `sys_category`;
CREATE TABLE `sys_category`  (
  `category_id` int NOT NULL AUTO_INCREMENT COMMENT '分类id',
  `category_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '分类名称',
  `order_num` int NULL DEFAULT NULL COMMENT '序号',
  PRIMARY KEY (`category_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_category
-- ----------------------------
INSERT INTO `sys_category` VALUES (1, 'Computer Technology', 1);
INSERT INTO `sys_category` VALUES (2, 'Digital Information', 2);
INSERT INTO `sys_category` VALUES (3, 'Chemical', 3);
INSERT INTO `sys_category` VALUES (4, 'Language Study', 4);
INSERT INTO `sys_category` VALUES (5, 'Literature', 5);
INSERT INTO `sys_category` VALUES (6, 'Management', 6);

-- ----------------------------
-- Table structure for sys_fines
-- ----------------------------
DROP TABLE IF EXISTS `sys_fines`;
CREATE TABLE `sys_fines`  (
  `fine_id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `borrow_id` int NOT NULL COMMENT '借还编号',
  `fine_type` int NULL DEFAULT 0 COMMENT '罚款类型 0:逾期未还；1:损毁或丢失',
  `fine_fee` decimal(6, 2) NULL DEFAULT NULL COMMENT '罚款费用',
  `pay_time` datetime NULL DEFAULT NULL COMMENT '支付时间',
  `fine_status` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '0' COMMENT '支付状态 0:未支付；1:已支付',
  PRIMARY KEY (`fine_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_fines
-- ----------------------------

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`  (
  `menu_id` int NOT NULL AUTO_INCREMENT COMMENT '菜单id',
  `parent_id` int NULL DEFAULT NULL COMMENT '父级菜单id',
  `title` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '菜单名称',
  `code` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '权限字段',
  `name` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '路由name',
  `path` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '路由path',
  `url` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '组件路径',
  `type` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '类型(0 目录 1菜单，2按钮)',
  `icon` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '菜单图标',
  `parent_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '上级菜单名称',
  `order_num` int NULL DEFAULT NULL COMMENT '序号',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`menu_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 53 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES (1, 0, 'system control', 'sys:menu:index', 'system', '/system', '', '0', 'el-icon-setting', 'top menu', 2, '2022-04-26 17:23:08', '2022-05-24 09:09:51');
INSERT INTO `sys_menu` VALUES (2, 1, 'user control', 'sys:userList:index', 'sysUserList', '/sysUserList', '/system/sysUserList', '1', 'el-icon-s-check', 'system control', 2, '2022-04-26 17:34:06', '2022-05-02 11:55:19');
INSERT INTO `sys_menu` VALUES (3, 1, 'role control', 'sys:sysRolelist:index', 'sysRoleList', '/sysRoleList', '/system/sysRoleList', '1', 'el-icon-s-claim', 'system control', 3, '2022-04-26 17:38:53', '2022-04-27 23:15:56');
INSERT INTO `sys_menu` VALUES (5, 1, 'menu control', 'sys:menu:index', 'sysMenuList', '/sysMenuList', '/system/sysMenuList', '1', 'tree', 'system control', 4, '2022-04-30 15:53:46', '2022-04-30 15:53:51');
INSERT INTO `sys_menu` VALUES (6, 2, 'add', '', '', '', '', '2', '', 'user control', 1, '2022-04-30 15:55:03', NULL);
INSERT INTO `sys_menu` VALUES (7, 2, 'edit', '', '', '', '', '2', '', 'user control', 2, '2022-04-30 15:55:18', NULL);
INSERT INTO `sys_menu` VALUES (8, 2, 'delete', '', '', '', '', '2', '', 'user control', 3, '2022-04-30 15:55:29', NULL);
INSERT INTO `sys_menu` VALUES (9, 3, 'add', '', '', '', '', '2', '', 'role control', 1, '2022-04-30 15:55:50', NULL);
INSERT INTO `sys_menu` VALUES (10, 3, 'edit', '', '', '', '', '2', '', 'role control', 2, '2022-04-30 15:55:58', NULL);
INSERT INTO `sys_menu` VALUES (11, 3, 'delete', '', '', '', '', '2', '', 'role control', 3, '2022-04-30 15:56:08', NULL);
INSERT INTO `sys_menu` VALUES (12, 5, 'add', '', '', '', '', '2', '', 'menu control', 1, '2022-04-30 15:56:18', NULL);
INSERT INTO `sys_menu` VALUES (13, 5, 'edit', '', '', '', '', '2', '', 'menu control', 2, '2022-04-30 15:56:26', NULL);
INSERT INTO `sys_menu` VALUES (14, 5, 'delete', '', '', '', '', '2', '', 'menu control', 3, '2022-04-30 15:56:34', NULL);
INSERT INTO `sys_menu` VALUES (15, 0, 'member control', 'sys:reader:root', 'member control', '/member', '', '0', 'el-icon-user', 'top menu', 3, '2022-04-30 15:58:50', '2022-05-24 12:15:28');
INSERT INTO `sys_menu` VALUES (16, 15, 'member  list', 'sys:reader:list', 'memberList', '/memberList', '/member/memberList', '1', 'table', 'reader control', 1, '2022-04-30 16:00:41', '2022-05-24 12:15:13');
INSERT INTO `sys_menu` VALUES (17, 16, 'add', 'sys:reader:add', '', '', '', '2', '', 'reader list', 1, '2022-04-30 16:01:24', NULL);
INSERT INTO `sys_menu` VALUES (18, 16, 'edit', 'sys:reader:edit', '', '', '', '2', '', 'reader list', 2, '2022-04-30 16:01:36', NULL);
INSERT INTO `sys_menu` VALUES (19, 16, 'delete', 'sys:reader:delete', '', '', '', '2', '', 'reader list', 3, '2022-04-30 16:01:49', NULL);
INSERT INTO `sys_menu` VALUES (20, 0, 'book control', 'sys:book:index', 'book', '/book', '', '0', 'el-icon-s-management', 'top menu', 4, '2022-04-30 16:02:39', '2022-05-24 09:18:39');
INSERT INTO `sys_menu` VALUES (21, 20, 'book category', 'sys:bookCategory:index', 'bookCategory', '/bookCategory', '/book/bookCategory', '1', 'table', 'book control', 1, '2022-04-30 16:03:45', NULL);
INSERT INTO `sys_menu` VALUES (22, 20, 'book list', 'sys:bookList:index', 'bookList', '/bookList', '/book/bookList', '1', 'tree', 'book control', 2, '2022-04-30 16:04:21', NULL);
INSERT INTO `sys_menu` VALUES (23, 21, 'add', 'sys:bookCategory:add', '', '', '', '2', '', 'book category', 1, '2022-04-30 16:05:12', NULL);
INSERT INTO `sys_menu` VALUES (24, 21, 'edit', 'sys:bookCategory:edit', '', '', '', '2', '', 'book category', 2, '2022-04-30 16:05:27', NULL);
INSERT INTO `sys_menu` VALUES (25, 21, 'delete', 'sys:bookCategory:delete', '', '', '', '2', '', 'book category', 3, '2022-04-30 16:05:40', NULL);
INSERT INTO `sys_menu` VALUES (26, 22, 'add', 'sys:bookList:add', '', '', '', '2', '', 'book list', 1, '2022-04-30 16:06:00', NULL);
INSERT INTO `sys_menu` VALUES (27, 22, 'edit', 'sys:bookList:edit', '', '', '', '2', '', 'book list', 2, '2022-04-30 16:06:17', NULL);
INSERT INTO `sys_menu` VALUES (28, 22, 'delete', 'sys:bookList:delete', '', '', '', '2', '', 'book list', 3, '2022-04-30 16:06:31', NULL);
INSERT INTO `sys_menu` VALUES (29, 0, 'BR control', 'sys:borrow:index', 'borrow', '/borrow', '', '0', 'el-icon-collection', 'top menu', 5, '2022-04-30 16:07:26', '2022-05-24 09:19:53');
INSERT INTO `sys_menu` VALUES (30, 29, 'borrow control', 'sys:bookBorrow:index', 'bookBorrow', '/bookBorrow', '/borrow/bookBorrow', '1', 'table', 'BR control', 1, '2022-04-30 16:08:37', NULL);
INSERT INTO `sys_menu` VALUES (31, 29, 'return control', 'sys:bookReturn:index', 'bookReturn', '/bookReturn', '/borrow/bookReturn', '1', 'table', 'BR control', 3, '2022-04-30 16:09:20', '2022-05-25 14:34:31');
INSERT INTO `sys_menu` VALUES (32, 29, 'look borrow', 'sys:borrowLook:index', 'borrowLook', '/borrowLook', '/borrow/borrowLook', '1', 'el-icon-s-cooperation', 'BR control', 5, '2022-04-30 16:10:01', '2022-05-25 17:07:54');
INSERT INTO `sys_menu` VALUES (33, 30, 'borrow', 'sys:bookBorrow:borrow', '', '', '', '2', '', 'borrow control', 1, '2022-04-30 16:10:17', '2022-05-02 12:52:20');
INSERT INTO `sys_menu` VALUES (36, 31, 'add', '', '', '', '', '2', '', 'return control', 1, '2022-04-30 16:10:39', NULL);
INSERT INTO `sys_menu` VALUES (37, 31, 'edit', '', '', '', '', '2', '', 'return control', 2, '2022-04-30 16:10:49', NULL);
INSERT INTO `sys_menu` VALUES (38, 31, 'delete', '', '', '', '', '2', '', 'return control', 3, '2022-04-30 16:11:03', NULL);
INSERT INTO `sys_menu` VALUES (39, 32, 'add', '', '', '', '', '2', '', 'look borrow', 1, '2022-04-30 16:11:09', NULL);
INSERT INTO `sys_menu` VALUES (40, 32, 'edit', '', '', '', '', '2', '', 'look borrow', 2, '2022-04-30 16:11:16', NULL);
INSERT INTO `sys_menu` VALUES (41, 32, 'delete', '', '', '', '', '2', '', 'look borrow', 3, '2022-04-30 16:11:26', NULL);
INSERT INTO `sys_menu` VALUES (42, 32, 'borrow-look', 'sys:borrow_look:look', '', '', '', '2', '', 'look borrow', 4, '2022-05-02 12:50:17', NULL);
INSERT INTO `sys_menu` VALUES (43, 20, 'book show', 'sys:bookList2:index', 'bookList2', '/bookList2', '/book/bookList2', '1', 'tree', 'book control', 3, '2022-05-24 08:57:12', '2022-05-24 13:39:18');
INSERT INTO `sys_menu` VALUES (44, 29, 'borrow look', 'sys:borrowLookMember:index', 'borrowLookMember', '/borrowLookMember', '/borrow/borrowLookMember', '1', 'el-icon-s-cooperation', 'BR control', 6, '2022-05-24 08:58:31', '2022-05-25 17:07:57');
INSERT INTO `sys_menu` VALUES (45, 0, 'fine', '', 'fine', '/fine', '', '0', 'el-icon-s-order', 'top menu', 6, '2022-05-24 09:01:00', '2022-05-24 09:20:14');
INSERT INTO `sys_menu` VALUES (46, 45, 'pay fine', 'sys:fine:payFine', 'payFine', '/payFine', '/fine/payFine', '1', 'el-icon-s-promotion', 'fine', 1, '2022-05-24 09:01:44', '2022-05-24 09:20:45');
INSERT INTO `sys_menu` VALUES (47, 45, 'fine pay', 'sys:fine:payFineAdmin', 'payFineAdmin', '/payFineAdmin', '/fine/payFineAdmin', '1', 'el-icon-s-promotion', 'fine', 2, '2022-05-24 09:02:24', '2022-05-24 09:20:47');
INSERT INTO `sys_menu` VALUES (48, 0, 'dashboard', '', 'dashboard', '/dashboard', '', '0', 'el-icon-view', 'top menu', 1, '2022-05-24 09:11:17', '2022-05-24 09:16:58');
INSERT INTO `sys_menu` VALUES (49, 48, 'member dashboard', '', 'readerIndex', '/readerIndex', '/dashboard/readerIndex', '1', 'el-icon-view', 'dashboard', 1, '2022-05-24 09:11:59', '2022-05-24 09:17:02');
INSERT INTO `sys_menu` VALUES (50, 48, 'librarian dashboard', '', 'index', '/index', '/dashboard/index', '1', 'el-icon-view', 'dashboard', 2, '2022-05-24 09:12:40', '2022-05-24 09:17:04');
INSERT INTO `sys_menu` VALUES (51, 29, 'borrow book', '', 'bookBorrowReader', '/bookBorrowReader', '/borrow/bookBorrowReader', '1', 'table', 'BR control', 2, '2022-05-25 14:26:23', '2022-05-25 14:34:47');
INSERT INTO `sys_menu` VALUES (52, 15, 'information', '', 'memberSelf', '/memberSelf', '/member/memberSelf', '1', 'el-icon-user', 'member control', 2, '2022-05-25 16:38:37', '2022-05-25 17:08:50');
INSERT INTO `sys_menu` VALUES (53, 29, 'return book', '', 'bookReturnReader', '/bookReturnReader', '/borrow/bookReturnReader', '1', 'table', 'BR control', 4, '2022-05-25 17:07:36', '2022-05-25 17:08:32');

-- ----------------------------
-- Table structure for sys_reader
-- ----------------------------
DROP TABLE IF EXISTS `sys_reader`;
CREATE TABLE `sys_reader`  (
  `reader_id` int NOT NULL AUTO_INCREMENT COMMENT '读者id',
  `learn_num` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '学生证号码',
  `username` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '用户名',
  `id_card` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '身份证号码',
  `sex` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '性别 0男  1女',
  `phone` varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '电话',
  `password` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '123456' COMMENT '密码',
  `type` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '类型(读者、系统用户)',
  `check_status` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '1' COMMENT '审核状态 0：未审核 1：已审核',
  `user_status` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '1' COMMENT '用户状态 0:停用  1：启用',
  `class_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '班级',
  `entry_year` int NULL DEFAULT(date_format(now(), '%Y')) COMMENT '入学年份',
  PRIMARY KEY (`reader_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 35 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_reader
-- ----------------------------
INSERT INTO `sys_reader` VALUES (24, 'Jin Song', '19030500133', '...', '0', '...', '123456', '', '1', '1', '52', 2019);
INSERT INTO `sys_reader` VALUES (25, 'Ren GuiQi', '19030500141', '...', '0', '...', '123456', '', '1', '1', '52', 2019);
INSERT INTO `sys_reader` VALUES (26, 'LYH', '19030500000', '...', '1', '...', '123456', '', '1', '1', '52', 2019);

-- ----------------------------
-- Table structure for sys_reader_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_reader_role`;
CREATE TABLE `sys_reader_role`  (
  `reader_role_id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `reader_id` int NULL DEFAULT NULL COMMENT '读者id',
  `role_id` int NULL DEFAULT NULL COMMENT '角色id',
  PRIMARY KEY (`reader_role_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 62 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_reader_role
-- ----------------------------
INSERT INTO `sys_reader_role` VALUES (1, 1, 2);
INSERT INTO `sys_reader_role` VALUES (2, 2, 2);
INSERT INTO `sys_reader_role` VALUES (3, 4, 2);
INSERT INTO `sys_reader_role` VALUES (4, 5, 2);
INSERT INTO `sys_reader_role` VALUES (5, 9, 2);
INSERT INTO `sys_reader_role` VALUES (6, 8, 2);
INSERT INTO `sys_reader_role` VALUES (7, 7, 2);
INSERT INTO `sys_reader_role` VALUES (8, 10, 2);
INSERT INTO `sys_reader_role` VALUES (10, 11, 2);
INSERT INTO `sys_reader_role` VALUES (11, 12, 2);
INSERT INTO `sys_reader_role` VALUES (12, 13, 2);
INSERT INTO `sys_reader_role` VALUES (13, 14, 2);
INSERT INTO `sys_reader_role` VALUES (14, 16, 2);
INSERT INTO `sys_reader_role` VALUES (15, 15, 2);
INSERT INTO `sys_reader_role` VALUES (19, 6, 2);
INSERT INTO `sys_reader_role` VALUES (20, 17, 2);
INSERT INTO `sys_reader_role` VALUES (21, 18, 2);
INSERT INTO `sys_reader_role` VALUES (22, 19, 2);
INSERT INTO `sys_reader_role` VALUES (23, 20, 2);
INSERT INTO `sys_reader_role` VALUES (25, 21, 2);
INSERT INTO `sys_reader_role` VALUES (26, 22, 2);
INSERT INTO `sys_reader_role` VALUES (28, 23, 2);
INSERT INTO `sys_reader_role` VALUES (30, 25, 2);
INSERT INTO `sys_reader_role` VALUES (31, 26, 2);
INSERT INTO `sys_reader_role` VALUES (32, 27, 2);
INSERT INTO `sys_reader_role` VALUES (34, 28, 2);
INSERT INTO `sys_reader_role` VALUES (35, 29, 2);
INSERT INTO `sys_reader_role` VALUES (36, 30, 2);
INSERT INTO `sys_reader_role` VALUES (37, 31, 2);
INSERT INTO `sys_reader_role` VALUES (38, 32, 2);
INSERT INTO `sys_reader_role` VALUES (40, 34, 2);
INSERT INTO `sys_reader_role` VALUES (41, NULL, 2);
INSERT INTO `sys_reader_role` VALUES (42, NULL, 2);
INSERT INTO `sys_reader_role` VALUES (43, NULL, 2);
INSERT INTO `sys_reader_role` VALUES (44, NULL, 2);
INSERT INTO `sys_reader_role` VALUES (45, NULL, 2);
INSERT INTO `sys_reader_role` VALUES (46, NULL, 2);
INSERT INTO `sys_reader_role` VALUES (48, NULL, 2);
INSERT INTO `sys_reader_role` VALUES (49, NULL, 2);
INSERT INTO `sys_reader_role` VALUES (50, NULL, 2);
INSERT INTO `sys_reader_role` VALUES (51, NULL, 2);
INSERT INTO `sys_reader_role` VALUES (52, 33, 2);
INSERT INTO `sys_reader_role` VALUES (59, 24, 2);
INSERT INTO `sys_reader_role` VALUES (60, 34, 2);
INSERT INTO `sys_reader_role` VALUES (62, 35, 2);

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `role_id` int NOT NULL AUTO_INCREMENT COMMENT '角色id',
  `role_name` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '角色名称',
  `role_type` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '角色类型 1：系统用户  2：读者',
  `remark` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`role_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES (1, 'Librarian', '1', 'Librarian', '2022-05-25 23:51:23', NULL);
INSERT INTO `sys_role` VALUES (2, 'Member', '2', 'Member', '2022-05-25 23:51:29', NULL);

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu`  (
  `role_menu_id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `role_id` int NULL DEFAULT NULL COMMENT '角色id',
  `menu_id` int NULL DEFAULT NULL COMMENT '菜单id',
  PRIMARY KEY (`role_menu_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 526 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
INSERT INTO `sys_role_menu` VALUES (279, 2, 49);
INSERT INTO `sys_role_menu` VALUES (280, 2, 52);
INSERT INTO `sys_role_menu` VALUES (281, 2, 43);
INSERT INTO `sys_role_menu` VALUES (282, 2, 51);
INSERT INTO `sys_role_menu` VALUES (283, 2, 53);
INSERT INTO `sys_role_menu` VALUES (284, 2, 44);
INSERT INTO `sys_role_menu` VALUES (285, 2, 46);
INSERT INTO `sys_role_menu` VALUES (286, 2, 48);
INSERT INTO `sys_role_menu` VALUES (287, 2, 15);
INSERT INTO `sys_role_menu` VALUES (288, 2, 20);
INSERT INTO `sys_role_menu` VALUES (289, 2, 29);
INSERT INTO `sys_role_menu` VALUES (290, 2, 45);
INSERT INTO `sys_role_menu` VALUES (472, 5, 48);
INSERT INTO `sys_role_menu` VALUES (473, 5, 49);
INSERT INTO `sys_role_menu` VALUES (474, 5, 50);
INSERT INTO `sys_role_menu` VALUES (475, 5, 1);
INSERT INTO `sys_role_menu` VALUES (476, 5, 2);
INSERT INTO `sys_role_menu` VALUES (477, 5, 6);
INSERT INTO `sys_role_menu` VALUES (478, 5, 7);
INSERT INTO `sys_role_menu` VALUES (479, 5, 8);
INSERT INTO `sys_role_menu` VALUES (480, 5, 3);
INSERT INTO `sys_role_menu` VALUES (481, 5, 9);
INSERT INTO `sys_role_menu` VALUES (482, 5, 10);
INSERT INTO `sys_role_menu` VALUES (483, 5, 11);
INSERT INTO `sys_role_menu` VALUES (484, 5, 5);
INSERT INTO `sys_role_menu` VALUES (485, 5, 12);
INSERT INTO `sys_role_menu` VALUES (486, 5, 13);
INSERT INTO `sys_role_menu` VALUES (487, 5, 14);
INSERT INTO `sys_role_menu` VALUES (488, 1, 50);
INSERT INTO `sys_role_menu` VALUES (489, 1, 3);
INSERT INTO `sys_role_menu` VALUES (490, 1, 9);
INSERT INTO `sys_role_menu` VALUES (491, 1, 10);
INSERT INTO `sys_role_menu` VALUES (492, 1, 11);
INSERT INTO `sys_role_menu` VALUES (493, 1, 5);
INSERT INTO `sys_role_menu` VALUES (494, 1, 12);
INSERT INTO `sys_role_menu` VALUES (495, 1, 13);
INSERT INTO `sys_role_menu` VALUES (496, 1, 14);
INSERT INTO `sys_role_menu` VALUES (497, 1, 16);
INSERT INTO `sys_role_menu` VALUES (498, 1, 17);
INSERT INTO `sys_role_menu` VALUES (499, 1, 18);
INSERT INTO `sys_role_menu` VALUES (500, 1, 19);
INSERT INTO `sys_role_menu` VALUES (501, 1, 21);
INSERT INTO `sys_role_menu` VALUES (502, 1, 23);
INSERT INTO `sys_role_menu` VALUES (503, 1, 24);
INSERT INTO `sys_role_menu` VALUES (504, 1, 25);
INSERT INTO `sys_role_menu` VALUES (505, 1, 22);
INSERT INTO `sys_role_menu` VALUES (506, 1, 26);
INSERT INTO `sys_role_menu` VALUES (507, 1, 27);
INSERT INTO `sys_role_menu` VALUES (508, 1, 28);
INSERT INTO `sys_role_menu` VALUES (509, 1, 30);
INSERT INTO `sys_role_menu` VALUES (510, 1, 33);
INSERT INTO `sys_role_menu` VALUES (511, 1, 31);
INSERT INTO `sys_role_menu` VALUES (512, 1, 36);
INSERT INTO `sys_role_menu` VALUES (513, 1, 37);
INSERT INTO `sys_role_menu` VALUES (514, 1, 38);
INSERT INTO `sys_role_menu` VALUES (515, 1, 32);
INSERT INTO `sys_role_menu` VALUES (516, 1, 39);
INSERT INTO `sys_role_menu` VALUES (517, 1, 40);
INSERT INTO `sys_role_menu` VALUES (518, 1, 41);
INSERT INTO `sys_role_menu` VALUES (519, 1, 42);
INSERT INTO `sys_role_menu` VALUES (520, 1, 47);
INSERT INTO `sys_role_menu` VALUES (521, 1, 48);
INSERT INTO `sys_role_menu` VALUES (522, 1, 1);
INSERT INTO `sys_role_menu` VALUES (523, 1, 15);
INSERT INTO `sys_role_menu` VALUES (524, 1, 20);
INSERT INTO `sys_role_menu` VALUES (525, 1, 29);
INSERT INTO `sys_role_menu` VALUES (526, 1, 45);

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `user_id` int NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `username` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '登录账户',
  `password` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '登录密码',
  `phone` varchar(13) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '用户电话',
  `email` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '邮箱',
  `sex` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '0:男 1：女',
  `is_admin` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '是否为超级管理员 1：是 0：否',
  `is_account_non_expired` tinyint NULL DEFAULT NULL COMMENT '帐户是否过期(1 未过期，0已过期)',
  `is_account_non_locked` tinyint NULL DEFAULT NULL COMMENT '帐户是否被锁定(1 未锁定，0已锁定)',
  `is_credentials_non_expired` tinyint NULL DEFAULT NULL COMMENT '密码是否过期(1 未过期，0已过期)',
  `is_enabled` tinyint NULL DEFAULT NULL COMMENT '帐户是否可用(1 可用，0 删除用户)',
  `nick_name` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '姓名',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1, 'ILL', '123456', '15369022716', '529458280@qq.com', '0', '1', 1, 1, 1, 1, 'JinSong', '2022-04-26 12:21:51', '2022-04-30 17:07:21');
INSERT INTO `sys_user` VALUES (2, 'username_test1', '123456', '111', 'test1@xidian.cn', '0', '0', 1, 1, 1, 1, 'Test1', '2022-04-26 12:22:29', '2022-05-01 14:32:22');
INSERT INTO `sys_user` VALUES (3, 'test2', '123456', '111', '111', '0', '0', 1, 1, 1, 1, 'Test2', '2022-04-30 17:07:50', '2022-05-01 14:32:05');

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`  (
  `user_role_id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` int NULL DEFAULT NULL COMMENT '用户id',
  `role_id` int NULL DEFAULT NULL COMMENT '角色id',
  PRIMARY KEY (`user_role_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES (4, 1, 1);
INSERT INTO `sys_user_role` VALUES (7, 3, 2);
INSERT INTO `sys_user_role` VALUES (9, 2, 2);
INSERT INTO `sys_user_role` VALUES (10, 4, 4);
INSERT INTO `sys_user_role` VALUES (11, 5, 5);

SET FOREIGN_KEY_CHECKS = 1;
