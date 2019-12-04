/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 50562
 Source Host           : localhost:3306
 Source Schema         : bcb-pay

 Target Server Type    : MySQL
 Target Server Version : 50562
 File Encoding         : 65001

 Date: 04/12/2019 14:12:15
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for file_index
-- ----------------------------
DROP TABLE IF EXISTS `file_index`;
CREATE TABLE `file_index`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `size` bigint(20) NULL DEFAULT NULL,
  `status` int(11) NOT NULL,
  `type` int(11) NULL DEFAULT NULL,
  `update_time` datetime NULL,
  `folder_id` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `FK7jb35kij3a5dfrajcr45hiy2c`(`folder_id`) USING BTREE
) ENGINE = MyISAM AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for folder
-- ----------------------------
DROP TABLE IF EXISTS `folder`;
CREATE TABLE `folder`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `sort` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = MyISAM AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for pay_account
-- ----------------------------
DROP TABLE IF EXISTS `pay_account`;
CREATE TABLE `pay_account`  (
  `id` bigint(20) NOT NULL,
  `balance` decimal(8, 2) NOT NULL,
  `bonus_points` bigint(20) NOT NULL,
  `create_time` datetime NULL,
  `credit_rating` int(11) NOT NULL,
  `deposit` decimal(5, 2) NOT NULL,
  `deposit_time` datetime NULL DEFAULT NULL,
  `member_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `password` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `profit_balance` decimal(8, 2) NOT NULL,
  `profit_total` decimal(8, 2) NOT NULL,
  `status` int(11) NOT NULL,
  `type` int(11) NOT NULL,
  `unit_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `update_time` datetime NULL,
  `contact` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `mobile_phone` varchar(13) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `UK_boq7o8u6g33x6j0gdusfxpo2f`(`name`) USING BTREE,
  UNIQUE INDEX `UK_toojgo29wv7ew43314s3b9iab`(`member_id`) USING BTREE,
  UNIQUE INDEX `UK_cfs1wc51hg2u03nm0wnj8gxn5`(`unit_id`) USING BTREE
) ENGINE = MyISAM CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of pay_account
-- ----------------------------
INSERT INTO `pay_account` VALUES (405755116407226368, 0.00, 0, '2019-11-25 16:05:58', 0, 0.00, NULL, NULL, NULL, 0.00, 0.00, 1, 2, '123456789', '2019-11-25 16:05:58', '老顾', '13333333333', '河南菠菜包有限公司');
INSERT INTO `pay_account` VALUES (408944079850700800, 0.00, 0, '2019-12-04 11:17:46', 0, 0.00, NULL, NULL, NULL, 0.00, 0.00, 1, 2, '12345678910', '2019-12-04 11:17:46', '老顾', '13333333333', '河南李唐电子商务有限公司');

-- ----------------------------
-- Table structure for pay_account_log
-- ----------------------------
DROP TABLE IF EXISTS `pay_account_log`;
CREATE TABLE `pay_account_log`  (
  `id` bigint(20) NOT NULL,
  `balance_before` decimal(20, 8) NOT NULL,
  `balance_after` decimal(20, 8) NOT NULL,
  `unit_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `pay_order_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `create_time` datetime NULL,
  `details` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `is_read` int(11) NOT NULL,
  `pay_channel` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `pay_channel_no` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `profit_type` int(11) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = MyISAM CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of pay_account_log
-- ----------------------------
INSERT INTO `pay_account_log` VALUES (406055730987139072, 0.00000000, 0.01000000, '123456789', '', '2019-11-26 12:00:30', '测试收款', 0, 'wxpay', '4200000448201911263716644418', 0);
INSERT INTO `pay_account_log` VALUES (406114779787165696, 0.01000000, 0.02000000, '123456789', '201911261540298713-1867378635', '2019-11-26 15:55:08', '测试收款', 0, 'wxpay', '4200000436201911269955968024', 0);
INSERT INTO `pay_account_log` VALUES (406114934020112384, 0.02000000, 0.03000000, '123456789', '20191126155585940-1867378635', '2019-11-26 15:55:45', '测试收款', 0, 'wxpay', '4200000442201911261643602748', 0);
INSERT INTO `pay_account_log` VALUES (406139652286709760, 0.03000000, 0.04000000, '123456789', '2019112617331281031867378635', '2019-11-26 17:33:58', '测试收款', 0, 'wxpay', '4200000423201911268573008667', 0);
INSERT INTO `pay_account_log` VALUES (406152622278443008, 0.04000000, 0.05000000, '123456789', '2019112617331281031867378635', '2019-11-26 18:25:31', '测试收款', 0, 'wxpay', '4200000423201911268573008667', 0);
INSERT INTO `pay_account_log` VALUES (406502297674186752, 0.05000000, 0.06000000, '123456789', '2019112717261637271867378635', '2019-11-27 17:35:00', '测试收款', 0, 'wxpay', '4200000434201911276476814411', 0);
INSERT INTO `pay_account_log` VALUES (406516772456366080, 0.05000000, 0.06000000, '123456789', '2019112718192124561867378635', '2019-11-27 18:32:31', '测试收款', 0, 'wxpay', '4200000446201911270851749211', 0);
INSERT INTO `pay_account_log` VALUES (406518616670863360, 0.06000000, 0.07000000, '123456789', '2019112718392013201867378635', '2019-11-27 18:39:50', '测试收款', 0, 'wxpay', '4200000435201911279540935041', 0);
INSERT INTO `pay_account_log` VALUES (406519219178438656, 0.07000000, 0.08000000, '123456789', '201911271841491401867378635', '2019-11-27 18:42:14', '测试收款', 0, 'wxpay', '4200000445201911271420746207', 0);
INSERT INTO `pay_account_log` VALUES (406519504122675200, 0.08000000, 0.07000000, '123456789', '20191127184321109301867378635', '2019-11-27 18:43:22', '测试退款', 0, 'wxpay', '50300702522019112713446186562', 0);
INSERT INTO `pay_account_log` VALUES (406773838517370880, 0.07000000, 0.08000000, '123456789', '201911281133112801867378635', '2019-11-28 11:34:00', '测试收款', 0, 'wxpay', '4200000425201911283941785875', 0);
INSERT INTO `pay_account_log` VALUES (406774317733380096, 0.08000000, 0.07000000, '123456789', '20191128113553134861867378635', '2019-11-28 11:35:54', '测试退款', 0, 'wxpay', '50300302512019112813429427470', 0);
INSERT INTO `pay_account_log` VALUES (407197296090939392, 0.07000000, 0.08000000, '123456789', '201911291535815111867378635', '2019-11-29 15:36:40', '测试收款', 0, 'wxpay', '4200000451201911296106271730', 0);
INSERT INTO `pay_account_log` VALUES (407201361466228736, 0.08000000, 0.07000000, '123456789', '20191129155221122391867378635', '2019-11-29 15:52:50', '测试退款', 0, 'wxpay', '50300202292019112913447869153', 0);

-- ----------------------------
-- Table structure for pay_channel_account
-- ----------------------------
DROP TABLE IF EXISTS `pay_channel_account`;
CREATE TABLE `pay_channel_account`  (
  `id` bigint(20) NOT NULL,
  `balance` decimal(8, 2) NOT NULL,
  `freeze_balance` decimal(8, 2) NOT NULL,
  `pay_channel` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `pay_channel_account` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `pay_channel_account_name` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `pay_channel_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `status` int(11) NOT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  `update_time` datetime NULL,
  `pay_account_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `FKc2d96l9m3w79fvng9o66tki2h`(`pay_account_id`) USING BTREE
) ENGINE = MyISAM CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of pay_channel_account
-- ----------------------------
INSERT INTO `pay_channel_account` VALUES (1, 0.07, 0.00, 'wxpay', '1525006091', NULL, NULL, 1, '2019-11-25 16:10:32', '2019-12-04 11:32:20', 405755116407226368);

-- ----------------------------
-- Table structure for pay_channel_type
-- ----------------------------
DROP TABLE IF EXISTS `pay_channel_type`;
CREATE TABLE `pay_channel_type`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `data` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `name` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `sort` int(11) NOT NULL,
  `status` int(11) NOT NULL,
  `type` int(11) NOT NULL,
  `update_time` datetime NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = MyISAM AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for pay_order
-- ----------------------------
DROP TABLE IF EXISTS `pay_order`;
CREATE TABLE `pay_order`  (
  `id` bigint(20) NOT NULL,
  `unit_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  `amount` decimal(8, 2) NOT NULL,
  `status` int(11) NOT NULL,
  `subject` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `order_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `pay_order_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `pay_channel` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `pay_type` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `app_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_time` datetime NULL,
  `details` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `is_profit_sharing` int(11) NOT NULL,
  `member_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `payee_channel_account` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `pay_notify` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `pay_notify_status` int(11) NULL DEFAULT NULL,
  `pay_notify_times` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `UK_j20xae4xg2877aa5g31aogf00`(`pay_order_no`) USING BTREE
) ENGINE = MyISAM CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of pay_order
-- ----------------------------
INSERT INTO `pay_order` VALUES (405726593835597824, '123456', '2019-11-25 14:12:38', 0.01, 0, '测试收款', '123456', '2019112514123873041450575459', 'wxpay', 'JSAPI', NULL, '2019-11-25 14:12:38', NULL, 0, NULL, '1525006091', NULL, 0, 0);
INSERT INTO `pay_order` VALUES (405756527899574272, '123456789', '2019-11-25 16:11:34', 0.01, 0, '测试收款', '1234567', '201911251611349575-1867378635', 'alipay', 'JSAPI', NULL, '2019-11-25 16:11:34', NULL, 0, NULL, '1525006091', NULL, 0, 0);
INSERT INTO `pay_order` VALUES (405768323440050176, '123456789', '2019-11-25 16:58:27', 0.01, 0, '测试收款', '12345678', '201911251658271192-1867378635', 'wxpay', 'JSAPI', NULL, '2019-11-25 16:58:27', NULL, 0, NULL, '1525006091', NULL, 0, 0);
INSERT INTO `pay_order` VALUES (405769128238907392, '123456789', '2019-11-25 17:01:38', 0.01, 0, '测试收款', '1234567891', '20191125171389393-1867378635', 'wxpay', 'NATIVE', NULL, '2019-11-25 17:01:38', NULL, 0, NULL, '1525006091', NULL, 0, 0);
INSERT INTO `pay_order` VALUES (405769337622757376, '123456789', '2019-11-25 17:02:28', 0.01, 0, '测试收款', '1234567892', '20191125172282370-1867378635', 'wxpay', 'NATIVE', NULL, '2019-11-25 17:02:28', NULL, 0, NULL, '1525006091', NULL, 0, 0);
INSERT INTO `pay_order` VALUES (405770489902923776, '123456789', '2019-11-25 17:07:03', 0.01, 0, '测试收款', '1234567893', '2019112517734026-1867378635', 'wxpay', 'NATIVE', NULL, '2019-11-25 17:07:03', NULL, 0, NULL, '1525006091', NULL, 0, 0);
INSERT INTO `pay_order` VALUES (405771260526592000, '123456789', '2019-11-25 17:10:07', 0.01, 0, '测试收款', '1234567894', '20191125171075777-1867378635', 'wxpay', 'NATIVE', NULL, '2019-11-25 17:10:07', NULL, 0, NULL, '1525006091', NULL, 0, 0);
INSERT INTO `pay_order` VALUES (405772776104787968, '123456789', '2019-11-25 17:16:08', 0.01, 0, '测试收款', '1234567895', '20191125171689038-1867378635', 'wxpay', 'NATIVE', NULL, '2019-11-25 17:16:08', NULL, 0, NULL, '1525006091', 'http://127.0.0.1/pay/notify', 0, 0);
INSERT INTO `pay_order` VALUES (405775934826741760, '123456789', '2019-11-25 17:28:41', 0.01, 0, '测试收款', '1234567896', '201911251728418054-1867378635', 'wxpay', 'NATIVE', NULL, '2019-11-25 17:28:41', NULL, 0, NULL, '1525006091', 'http://127.0.0.1/pay/notify', 0, 0);
INSERT INTO `pay_order` VALUES (405776525074366464, '123456789', '2019-11-25 17:31:02', 0.01, 0, '测试收款', '1234567897', '20191125173129455-1867378635', 'wxpay', 'JSAPI', NULL, '2019-11-25 17:31:02', NULL, 0, '123456', '1525006091', 'http://127.0.0.1/pay/notify', 0, 0);
INSERT INTO `pay_order` VALUES (405780637371334656, '123456789', '2019-11-25 17:47:22', 0.01, 0, '测试收款', '1234567898', '201911251747225624-1867378635', 'wxpay', 'JSAPI', NULL, '2019-11-25 17:47:22', NULL, 0, '123456', '1525006091', 'http://127.0.0.1/pay/notify', 0, 0);
INSERT INTO `pay_order` VALUES (405780915596296192, '123456789', '2019-11-25 17:48:29', 0.01, 0, '测试收款', '1234567899', '201911251748293728-1867378635', 'wxpay', 'JSAPI', NULL, '2019-11-25 17:48:29', NULL, 0, '123456', '1525006091', 'http://127.0.0.1/pay/notify', 0, 0);
INSERT INTO `pay_order` VALUES (405782305336983552, '123456789', '2019-11-25 17:54:00', 0.01, 0, '测试收款', '1234567890', '20191125175403730-1867378635', 'wxpay', 'JSAPI', NULL, '2019-11-25 17:54:00', NULL, 0, '123456', '1525006091', 'http://127.0.0.1/pay/notify', 0, 0);
INSERT INTO `pay_order` VALUES (405783186472173568, '123456789', '2019-11-25 17:57:30', 0.01, 0, '测试收款', '1234567880', '201911251757303373-1867378635', 'wxpay', 'JSAPI', NULL, '2019-11-25 17:57:30', NULL, 0, '123456', '1525006091', 'http://127.0.0.1/pay/notify', 0, 0);
INSERT INTO `pay_order` VALUES (405783478039216128, '123456789', '2019-11-25 17:58:40', 0.01, 0, '测试收款', '1234567881', '201911251758407525-1867378635', 'wxpay', 'JSAPI', NULL, '2019-11-25 17:58:40', NULL, 0, 'olFJwwNOyw5v5OpMq-2Ex959r5is', '1525006091', 'http://127.0.0.1/pay/notify', 0, 0);
INSERT INTO `pay_order` VALUES (405783850187227136, '123456789', '2019-11-25 18:00:08', 0.01, 0, '测试收款', '1234567882', '2019112518086021-1867378635', 'wxpay', 'JSAPI', NULL, '2019-11-25 18:00:08', NULL, 0, 'olFJwwNOyw5v5OpMq-2Ex959r5is', '1525006091', 'http://127.0.0.1/pay/notify', 0, 0);
INSERT INTO `pay_order` VALUES (405784188684337152, '123456789', '2019-11-25 18:01:29', 0.01, 0, '测试收款', '1234567883', '20191125181292149-1867378635', 'wxpay', 'JSAPI', NULL, '2019-11-25 18:01:29', NULL, 0, 'olFJwwNOyw5v5OpMq-2Ex959r5is', '1525006091', 'http://127.0.0.1/pay/notify', 0, 0);
INSERT INTO `pay_order` VALUES (405785440528891904, '123456789', '2019-11-25 18:06:28', 0.01, 0, '测试收款', '1234567884', '20191125186286621-1867378635', 'wxpay', 'JSAPI', NULL, '2019-11-25 18:06:28', NULL, 0, 'olFJwwNOyw5v5OpMq-2Ex959r5is', '1525006091', 'http://127.0.0.1/pay/notify', 0, 0);
INSERT INTO `pay_order` VALUES (406031698157895680, '123456789', '2019-11-26 10:26:20', 0.01, 1, '测试收款', '1234567885', '20191126102503529-1867378635', 'wxpay', 'NATIVE', NULL, '2019-11-26 10:25:00', NULL, 0, NULL, '1525006091', 'http://127.0.0.1/pay/notify', 0, 0);
INSERT INTO `pay_order` VALUES (406038801140416512, '123456789', '2019-11-26 10:54:50', 0.01, 1, '测试收款', '1234567886', '201911261053147410-1867378635', 'wxpay', 'NATIVE', NULL, '2019-11-26 10:53:14', NULL, 0, 'olFJwwNOyw5v5OpMq-2Ex959r5is', '1525006091', 'http://127.0.0.1/pay/notify', 0, 0);
INSERT INTO `pay_order` VALUES (406039198592663552, '123456789', '2019-11-26 10:59:21', 0.01, 1, '测试收款', '1234567887', '201911261054481221-1867378635', 'wxpay', 'NATIVE', NULL, '2019-11-26 10:54:48', NULL, 0, 'olFJwwNOyw5v5OpMq-2Ex959r5is', '1525006091', 'http://127.0.0.1/pay/notify', 0, 0);
INSERT INTO `pay_order` VALUES (406040045158400000, '123456789', '2019-11-26 10:58:51', 0.01, 1, '测试收款', '1234567888', '201911261058105589-1867378635', 'wxpay', 'NATIVE', NULL, '2019-11-26 10:58:10', NULL, 0, 'olFJwwNOyw5v5OpMq-2Ex959r5is', '1525006091', 'http://127.0.0.1/pay/notify', 0, 0);
INSERT INTO `pay_order` VALUES (406055524308615168, '123456789', '2019-11-26 12:00:30', 0.01, 1, '测试收款', '1234567889', '201911261159411226-1867378635', 'wxpay', 'NATIVE', NULL, '2019-11-26 11:59:41', NULL, 0, 'olFJwwNOyw5v5OpMq-2Ex959r5is', '1525006091', 'http://127.0.0.1/pay/notify', 0, 0);
INSERT INTO `pay_order` VALUES (406111092608073728, '123456789', '2019-11-26 15:55:08', 0.01, 1, '测试收款', '1234567870', '201911261540298713-1867378635', 'wxpay', 'NATIVE', NULL, '2019-11-26 15:40:29', NULL, 0, 'olFJwwNOyw5v5OpMq-2Ex959r5is', '1525006091', 'http://127.0.0.1/pay/notify', 0, 0);
INSERT INTO `pay_order` VALUES (406114778193330176, '123456789', '2019-11-26 15:55:45', 0.01, 1, '测试收款', '1234567871', '1234567871', 'wxpay', 'NATIVE', NULL, '2019-11-26 15:55:08', NULL, 0, 'olFJwwNOyw5v5OpMq-2Ex959r5is', '1525006091', 'http://127.0.0.1/pay/notify', 0, 0);
INSERT INTO `pay_order` VALUES (406116927727992832, '123456789', '2019-11-26 16:03:40', 0.01, 0, '测试收款', '1234567872', '201911261634024641867378635', 'wxpay', 'NATIVE', NULL, '2019-11-26 16:03:40', NULL, 0, NULL, '1525006091', 'http://127.0.0.1/pay/notify', 0, 0);
INSERT INTO `pay_order` VALUES (406117231580151808, '123456789', '2019-11-26 16:04:53', 0.01, 0, '测试收款', '1234567873', '201911261645394251867378635', 'wxpay', 'JSAPI', NULL, '2019-11-26 16:04:53', NULL, 0, 'olFJwwNOyw5v5OpMq-2Ex959r5is', '1525006091', 'http://127.0.0.1/pay/notify', 0, 0);
INSERT INTO `pay_order` VALUES (406139459831070720, '123456789', '2019-11-26 18:25:31', 0.01, 1, '测试收款', '1234567874', '2019112617331281031867378635', 'wxpay', 'NATIVE', NULL, '2019-11-26 17:33:12', NULL, 0, 'olFJwwNOyw5v5OpMq-2Ex959r5is', '1525006091', 'http://127.0.0.1/pay/notify', 0, 0);
INSERT INTO `pay_order` VALUES (406140127824314368, '123456789', '2019-11-26 17:35:52', 0.01, 0, '测试收款', '1234567875', '2019112617355246141867378635', 'wxpay', 'NATIVE', NULL, '2019-11-26 17:35:52', NULL, 0, NULL, '1525006091', 'http://127.0.0.1/pay/notify', 0, 0);
INSERT INTO `pay_order` VALUES (406162284428132352, '123456789', '2019-11-26 19:03:54', 0.01, 0, '测试收款', '1234567876', '201911261935499371867378635', 'wxpay', 'NATIVE', NULL, '2019-11-26 19:03:54', NULL, 0, NULL, '1525006091', 'http://127.0.0.1/pay/notify', 0, 0);
INSERT INTO `pay_order` VALUES (406500102841040896, '123456789', '2019-11-27 17:35:00', 0.01, 1, '测试收款', '1234567860', '2019112717261637271867378635', 'wxpay', 'NATIVE', NULL, '2019-11-27 17:26:16', NULL, 0, 'olFJwwNOyw5v5OpMq-2Ex959r5is', '1525006091', 'http://127.0.0.1/pay/notify', 0, 0);
INSERT INTO `pay_order` VALUES (406513459908313088, '123456789', '2019-11-27 18:32:31', 0.01, 1, '测试收款', '1234567861', '2019112718192124561867378635', 'wxpay', 'NATIVE', NULL, '2019-11-27 18:19:21', NULL, 0, 'olFJwwNOyw5v5OpMq-2Ex959r5is', '1525006091', 'http://127.0.0.1/pay/notify', 0, 0);
INSERT INTO `pay_order` VALUES (406518488849448960, '123456789', '2019-11-27 18:39:50', 0.01, 1, '测试收款', '1234567862', '2019112718392013201867378635', 'wxpay', 'NATIVE', NULL, '2019-11-27 18:39:20', NULL, 0, 'olFJwwNOyw5v5OpMq-2Ex959r5is', '1525006091', 'http://127.0.0.1/pay/notify', 0, 0);
INSERT INTO `pay_order` VALUES (406519112647311360, '123456789', '2019-11-27 18:42:14', 0.01, 1, '测试收款', '1234567863', '201911271841491401867378635', 'wxpay', 'NATIVE', NULL, '2019-11-27 18:41:49', NULL, 0, 'olFJwwNOyw5v5OpMq-2Ex959r5is', '1525006091', 'http://127.0.0.1/pay/notify', 0, 0);
INSERT INTO `pay_order` VALUES (406773590613032960, '123456789', '2019-11-28 11:34:00', 0.01, 1, '测试收款', '1234567864', '201911281133112801867378635', 'wxpay', 'NATIVE', NULL, '2019-11-28 11:33:01', NULL, 0, 'olFJwwNOyw5v5OpMq-2Ex959r5is', '1525006091', 'http://127.0.0.1/pay/notify', 0, 0);
INSERT INTO `pay_order` VALUES (406781750274949120, '123456789', '2019-11-28 12:05:26', 0.01, 0, '测试收款', '1234567865', '201911281252625341867378635', 'wxpay', 'NATIVE', NULL, '2019-11-28 12:05:26', NULL, 0, NULL, '1525006091', 'http://127.0.0.1/pay/notify', 0, 0);
INSERT INTO `pay_order` VALUES (407196907987795968, '123456789', '2019-11-29 15:36:40', 0.01, 1, '测试收款', '1234567866', '201911291535815111867378635', 'wxpay', 'NATIVE', NULL, '2019-11-29 15:35:08', NULL, 0, 'olFJwwNOyw5v5OpMq-2Ex959r5is', '1525006091', 'http://127.0.0.1/pay/notify', 0, 0);
INSERT INTO `pay_order` VALUES (408928377261522944, '123456789', '2019-12-04 10:15:22', 0.01, 0, '测试收款', '1234567867', '201912410152252851867378635', 'wxpay', 'NATIVE', NULL, '2019-12-04 10:15:22', NULL, 0, NULL, '1525006091', 'http://127.0.0.1/pay/notify', 0, 0);
INSERT INTO `pay_order` VALUES (408928896566689792, '123456789', '2019-12-04 10:17:26', 0.01, 0, '测试收款', '1234567868', '201912410172655111867378635', 'wxpay', 'JSAPI', NULL, '2019-12-04 10:17:26', NULL, 0, 'olFJwwNOyw5v5OpMq-2Ex959r5is', '1525006091', 'http://127.0.0.1/pay/notify', 0, 0);
INSERT INTO `pay_order` VALUES (408931051365203968, '123456789', '2019-12-04 10:26:00', 0.01, 0, '测试收款', '1234567869', '20191241026068741867378635', 'wxpay', 'MICROPAY', NULL, '2019-12-04 10:26:00', NULL, 0, 'olFJwwNOyw5v5OpMq-2Ex959r5is', '1525006091', 'http://127.0.0.1/pay/notify', 0, 0);
INSERT INTO `pay_order` VALUES (408932896737329152, '123456789', '2019-12-04 10:33:20', 0.01, 0, '测试收款', '12345678690', '201912410332040091867378635', 'wxpay', 'NATIVE', NULL, '2019-12-04 10:33:20', NULL, 0, 'olFJwwNOyw5v5OpMq-2Ex959r5is', '1525006091', 'http://127.0.0.1/pay/notify', 0, 0);
INSERT INTO `pay_order` VALUES (408986732202033152, '123456789', '2019-12-04 14:07:15', 0.01, 0, '测试收款', '12345678691', '20191241471553941867378635', 'wxpay', 'NATIVE', NULL, '2019-12-04 14:07:15', NULL, 0, 'olFJwwNOyw5v5OpMq-2Ex959r5is', '1525006091', 'http://127.0.0.1/pay/notify', 0, 0);
INSERT INTO `pay_order` VALUES (408987118283522048, '123456789', '2019-12-04 14:08:47', 0.01, 0, '测试收款', '12345678692', '20191241484754891867378635', 'wxpay', 'JSAPI', NULL, '2019-12-04 14:08:47', NULL, 0, 'olFJwwNOyw5v5OpMq-2Ex959r5is', '1525006091', 'http://127.0.0.1/pay/notify', 0, 0);

-- ----------------------------
-- Table structure for pay_order_log
-- ----------------------------
DROP TABLE IF EXISTS `pay_order_log`;
CREATE TABLE `pay_order_log`  (
  `id` bigint(20) NOT NULL,
  `unit_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `pay_channel` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `pay_order_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `order_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `amount` decimal(8, 2) NOT NULL,
  `status` int(11) NOT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  `pay_channel_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `payee_channel_account` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `payer_channel_account` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = MyISAM CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of pay_order_log
-- ----------------------------
INSERT INTO `pay_order_log` VALUES (406040214759276544, '123456789', 'wxpay', '201911261058105589-1867378635', '1234567888', 0.01, 1, '2019-11-26 10:58:51', '4200000445201911264526260807', '1525006091', 'olFJwwNOyw5v5OpMq-2Ex959r5is');
INSERT INTO `pay_order_log` VALUES (406040342551330816, '123456789', 'wxpay', '201911261159411226-1867378635', '1234567887', 0.01, 1, '2019-11-26 10:59:21', '4200000428201911263590796817', '1525006091', 'olFJwwNOyw5v5OpMq-2Ex959r5is');
INSERT INTO `pay_order_log` VALUES (406055730899058688, '123456789', 'wxpay', '201911261159411226-1867378635', '1234567889', 0.01, 1, '2019-11-26 12:00:30', '4200000448201911263716644418', '1525006091', 'olFJwwNOyw5v5OpMq-2Ex959r5is');
INSERT INTO `pay_order_log` VALUES (406112259991928832, '123456789', 'wxpay', '201911261540298713-1867378635', '1234567870', 0.01, 1, '2019-11-26 15:45:07', '4200000436201911269955968024', '1525006091', 'olFJwwNOyw5v5OpMq-2Ex959r5is');
INSERT INTO `pay_order_log` VALUES (406114779661336576, '123456789', 'wxpay', '201911261540298713-1867378635', '1234567870', 0.01, 1, '2019-11-26 15:55:08', '4200000436201911269955968024', '1525006091', 'olFJwwNOyw5v5OpMq-2Ex959r5is');
INSERT INTO `pay_order_log` VALUES (406114933973975040, '123456789', 'wxpay', '20191126155585940-1867378635', '1234567871', 0.01, 1, '2019-11-26 15:55:45', '4200000442201911261643602748', '1525006091', 'olFJwwNOyw5v5OpMq-2Ex959r5is');
INSERT INTO `pay_order_log` VALUES (406139652215406592, '123456789', 'wxpay', '2019112617331281031867378635', '1234567874', 0.01, 1, '2019-11-26 17:33:58', '4200000423201911268573008667', '1525006091', 'olFJwwNOyw5v5OpMq-2Ex959r5is');
INSERT INTO `pay_order_log` VALUES (406152622140030976, '123456789', 'wxpay', '2019112617331281031867378635', '1234567874', 0.01, 1, '2019-11-26 18:25:31', '4200000423201911268573008667', '1525006091', 'olFJwwNOyw5v5OpMq-2Ex959r5is');
INSERT INTO `pay_order_log` VALUES (406502297493831680, '123456789', 'wxpay', '2019112717261637271867378635', '1234567860', 0.01, 1, '2019-11-27 17:35:00', '4200000434201911276476814411', '1525006091', 'olFJwwNOyw5v5OpMq-2Ex959r5is');
INSERT INTO `pay_order_log` VALUES (406516772301176832, '123456789', 'wxpay', '2019112718192124561867378635', '1234567861', 0.01, 1, '2019-11-27 18:32:31', '4200000446201911270851749211', '1525006091', 'olFJwwNOyw5v5OpMq-2Ex959r5is');
INSERT INTO `pay_order_log` VALUES (406518616570200064, '123456789', 'wxpay', '2019112718392013201867378635', '1234567862', 0.01, 1, '2019-11-27 18:39:50', '4200000435201911279540935041', '1525006091', 'olFJwwNOyw5v5OpMq-2Ex959r5is');
INSERT INTO `pay_order_log` VALUES (406519219111329792, '123456789', 'wxpay', '201911271841491401867378635', '1234567863', 0.01, 1, '2019-11-27 18:42:14', '4200000445201911271420746207', '1525006091', 'olFJwwNOyw5v5OpMq-2Ex959r5is');
INSERT INTO `pay_order_log` VALUES (406773838181826560, '123456789', 'wxpay', '201911281133112801867378635', '1234567864', 0.01, 1, '2019-11-28 11:34:00', '4200000425201911283941785875', '1525006091', 'olFJwwNOyw5v5OpMq-2Ex959r5is');
INSERT INTO `pay_order_log` VALUES (407197295977693184, '123456789', 'wxpay', '201911291535815111867378635', '1234567866', 0.01, 1, '2019-11-29 15:36:40', '4200000451201911296106271730', '1525006091', 'olFJwwNOyw5v5OpMq-2Ex959r5is');

-- ----------------------------
-- Table structure for refund_order
-- ----------------------------
DROP TABLE IF EXISTS `refund_order`;
CREATE TABLE `refund_order`  (
  `id` bigint(20) NOT NULL,
  `amount` decimal(8, 2) NOT NULL,
  `create_time` datetime NULL,
  `member_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `order_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `pay_channel` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `pay_order_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `refund_order_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `status` int(11) NOT NULL,
  `subject` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `unit_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `update_time` datetime NULL,
  `payee_channel_account` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `UK_gkb7gurx8n1muows8rdrnlusa`(`pay_order_no`) USING BTREE,
  UNIQUE INDEX `UK_rit6ov5qr921uejxnxpqvx0jw`(`refund_order_no`) USING BTREE
) ENGINE = MyISAM CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of refund_order
-- ----------------------------
INSERT INTO `refund_order` VALUES (406490106912833536, 0.01, '2019-11-27 16:46:33', '123456789', '1234567874', 'wxpay', '20191127164633171831867378635', '21234567890', 0, '测试退款', '123456789', '2019-11-27 16:46:33', '1525006091');
INSERT INTO `refund_order` VALUES (406497293517193216, 0.01, '2019-11-27 17:15:07', '123456789', '1234567871', 'wxpay', '2019112717157196801867378635', '21234567892', 0, '测试退款', '123456789', '2019-11-27 17:15:07', '1525006091');
INSERT INTO `refund_order` VALUES (406498179165454336, 0.01, '2019-11-27 17:18:38', '123456789', '1234567871', 'wxpay', '20191127171838117841867378635', '21234567893', 0, '测试退款', '123456789', '2019-11-27 17:18:38', '1525006091');
INSERT INTO `refund_order` VALUES (406499313804378112, 0.01, '2019-11-27 17:23:08', '123456789', '1234567871', 'wxpay', '2019112717238132261867378635', '21234567894', 0, '测试退款', '123456789', '2019-11-27 17:23:08', '1525006091');
INSERT INTO `refund_order` VALUES (406502561160364032, 0.01, '2019-11-27 17:36:03', '123456789', '1234567860', 'wxpay', '2019112717363163181867378635', '21234567895', 1, '测试退款', '123456789', '2019-11-27 17:36:59', '1525006091');
INSERT INTO `refund_order` VALUES (406516972256231424, 0.01, '2019-11-27 18:33:18', '123456789', '1234567861', 'wxpay', '20191127183318164711867378635', '21234567896', 1, '测试退款', '123456789', '2019-11-28 14:54:07', '1525006091');
INSERT INTO `refund_order` VALUES (406518021520097280, 0.01, '2019-11-27 18:37:29', '123456789', '1234567861', 'wxpay', '20191127183729168091867378635', '21234567897', 0, '测试退款', '123456789', '2019-11-27 18:37:29', '1525006091');
INSERT INTO `refund_order` VALUES (406518867020480512, 0.01, '2019-11-27 18:40:50', '123456789', '1234567862', 'wxpay', '20191127184050180001867378635', '21234567898', 1, '测试退款', '123456789', '2019-11-28 14:24:20', '1525006091');
INSERT INTO `refund_order` VALUES (406519497785081856, 0.01, '2019-11-27 18:43:21', '123456789', '1234567863', 'wxpay', '20191127184321109301867378635', '21234567899', 1, '测试退款', '123456789', '2019-11-27 18:43:22', '1525006091');
INSERT INTO `refund_order` VALUES (406774313149005824, 0.01, '2019-11-28 11:35:53', '123456789', '1234567864', 'wxpay', '20191128113553134861867378635', '21234567880', 1, '测试退款', '123456789', '2019-11-28 11:35:54', '1525006091');
INSERT INTO `refund_order` VALUES (407197470133583872, 0.01, '2019-11-29 15:37:22', '123456789', '1234567866', 'wxpay', '20191129153722107301867378635', '21234567881', 0, '测试退款', '123456789', '2019-11-29 15:37:22', '1525006091');
INSERT INTO `refund_order` VALUES (407200315213873152, 0.01, '2019-11-29 15:48:40', '123456789', '1234567866', 'wxpay', '20191129154840195791867378635', '21234567882', 0, '测试退款', '123456789', '2019-11-29 15:48:40', '1525006091');
INSERT INTO `refund_order` VALUES (407201242985529344, 0.01, '2019-11-29 15:52:21', '123456789', '1234567866', 'wxpay', '20191129155221122391867378635', '21234567883', 1, '测试退款', '123456789', '2019-11-29 15:52:49', '1525006091');
INSERT INTO `refund_order` VALUES (408946018785165312, 0.01, '2019-12-04 11:25:28', '123456789', '1234567864', 'wxpay', '2019124112528159111867378635', '212345678811', 0, '测试退款', '123456789', '2019-12-04 11:25:28', '1525006091');
INSERT INTO `refund_order` VALUES (408947268780032000, 0.01, '2019-12-04 11:30:26', '123456789', '1234567864', 'wxpay', '2019124113026191871867378635', '212345678812', 0, '测试退款', '123456789', '2019-12-04 11:30:26', '1525006091');
INSERT INTO `refund_order` VALUES (408947455837601792, 0.01, '2019-12-04 11:31:11', '123456789', '1234567864', 'wxpay', '2019124113111135191867378635', '212345678813', 0, '测试退款', '123456789', '2019-12-04 11:31:11', '1525006091');
INSERT INTO `refund_order` VALUES (408947734788177920, 0.01, '2019-12-04 11:32:17', '123456789', '1234567864', 'wxpay', '2019124113217182721867378635', '212345678814', 0, '测试退款', '123456789', '2019-12-04 11:32:17', '1525006091');

-- ----------------------------
-- Table structure for refund_order_log
-- ----------------------------
DROP TABLE IF EXISTS `refund_order_log`;
CREATE TABLE `refund_order_log`  (
  `id` bigint(20) NOT NULL,
  `amount` decimal(8, 2) NOT NULL,
  `create_time` datetime NULL,
  `order_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `pay_channel` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `pay_channel_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `pay_order_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `refund_order_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `status` int(11) NOT NULL,
  `unit_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = MyISAM CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of refund_order_log
-- ----------------------------
INSERT INTO `refund_order_log` VALUES (406519504072343552, 0.01, '2019-11-27 18:43:22', '1234567863', 'wxpay', '50300702522019112713446186562', '20191127184321109301867378635', '21234567899', 1, '123456789');
INSERT INTO `refund_order_log` VALUES (406774317670465536, 0.01, '2019-11-28 11:35:54', '1234567864', 'wxpay', '50300302512019112813429427470', '20191128113553134861867378635', '21234567880', 1, '123456789');
INSERT INTO `refund_order_log` VALUES (406816702647500800, 0.01, '2019-11-28 14:24:20', '1234567862', 'wxpay', '4200000435201911279540935041', '20191127184050180001867378635', '21234567898', 1, '123456789');
INSERT INTO `refund_order_log` VALUES (406824200431468544, 0.01, '2019-11-28 14:54:07', '1234567861', 'wxpay', '4200000446201911270851749211', '20191127183318164711867378635', '21234567896', 1, '123456789');
INSERT INTO `refund_order_log` VALUES (407201361352982528, 0.01, '2019-11-29 15:52:49', '1234567866', 'wxpay', '50300202292019112913447869153', '20191129155221122391867378635', '21234567883', 1, '123456789');

-- ----------------------------
-- Table structure for wx_micro_account
-- ----------------------------
DROP TABLE IF EXISTS `wx_micro_account`;
CREATE TABLE `wx_micro_account`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `account_bank` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `account_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `account_number` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `bank_address_code` varchar(6) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `bank_name` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `business_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `contact` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `contact_email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `contact_phone` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `create_time` datetime NULL,
  `id_card_copy` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `id_card_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `id_card_national` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `id_card_number` varchar(18) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `id_card_valid_time` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `indoor_pic` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `merchant_short_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `product_desc` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `rate` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `service_phone` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `status` int(11) NOT NULL,
  `store_address_code` varchar(6) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `store_entrance_pic` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `store_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `store_street` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `update_time` datetime NULL,
  `applyment_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `unit_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `UK_ir0db3io4bmafbejdxlucr0wo`(`unit_id`) USING BTREE
) ENGINE = MyISAM CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wx_micro_account
-- ----------------------------
INSERT INTO `wx_micro_account` VALUES ('407597987867394048', '交通银行', '王志刚', '6222600620015541041', '410102', NULL, '20191130188539121867378635', '王志刚', '375214167@qq.com', '13703957387', '2019-11-30 18:08:53', '_qdPgxYeHjr20zZTSLO0ytytIW4WzQGYSjL9keglclA2AQyG_RoQhc87zbvCHC6B_zgcfMm7mi2zlOPEErgzFfy8z', '王志刚', 'W56fsKod1rgFm3W7Xin0HIWYTvWcHWNN2l62P8myGhDeN7dwQ3Xzen1K3EdCEuzRIOHUpEky61GQ8lZQ3JjxDrZ_k23PUCJc9pQathUIR8I', '320106496903060011', '[\"1970-01-01\",\"长期\"]', 'W56fsKod1rgFm3W7Xin0HIWYTvWcHWNN2l62P8myGhDeN7dwQ3Xzen1K3EdCEuzRIOHUpEky61GQ8lZQ3JjxDrZ_k23PUCJc9pQathUIR8I', '天天小吃', '餐饮', '0.6%', '13703957387', 0, '110000', 'W56fsKod1rgFm3W7Xin0HIWYTvWcHWNN2l62P8myGhDeN7dwQ3Xzen1K3EdCEuzRIOHUpEky61GQ8lZQ3JjxDrZ_k23PUCJc9pQathUIR8I', '郑州天天小吃', '郑州市工人路100号', '2019-11-30 18:08:53', NULL, '123456789');

-- ----------------------------
-- Table structure for wx_micro_account_log
-- ----------------------------
DROP TABLE IF EXISTS `wx_micro_account_log`;
CREATE TABLE `wx_micro_account_log`  (
  `id` bigint(20) NOT NULL,
  `applyment_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `applyment_state` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `applyment_state_desc` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `audit_detail` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `business_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `sign_url` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `sub_mch_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `unit_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = MyISAM CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wx_micro_account_log
-- ----------------------------
INSERT INTO `wx_micro_account_log` VALUES (407599647394103296, '2000002133034132', 'REJECTED', '已驳回', '{\"audit_detail\":[{\"param_name\":\"id_card_copy\",\"reject_reason\":\"身份证正面识别失败，请重新上传\"}]}', '20191130188539121867378635', NULL, NULL, '123456789');

-- ----------------------------
-- Table structure for wx_profit_receiver
-- ----------------------------
DROP TABLE IF EXISTS `wx_profit_receiver`;
CREATE TABLE `wx_profit_receiver`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `account` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `create_time` datetime NULL,
  `name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `relation_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `status` int(11) NOT NULL,
  `type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `update_time` datetime NULL,
  `pay_account_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `FKqvpdpsr81pvm06851bcecum5y`(`pay_account_id`) USING BTREE
) ENGINE = MyISAM CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for wx_red_pack
-- ----------------------------
DROP TABLE IF EXISTS `wx_red_pack`;
CREATE TABLE `wx_red_pack`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `act_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `amt_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `client_ip` varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `gzh_app_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `mch_billno` varchar(28) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `mch_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `re_open_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `remark` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `scene_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `send_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `sub_app_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `sub_mch_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `total_amount` int(11) NOT NULL,
  `total_num` int(11) NOT NULL,
  `wishing` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = MyISAM CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
