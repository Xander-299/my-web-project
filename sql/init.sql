-- =====================================================================
-- Tlias 智能学习辅助系统 —— 数据库初始化脚本
--
-- 用法：
--   mysql -u root -p < sql/init.sql
--
-- 表结构依据 backend/src/main/resources/com/tlias/mapper/*.xml 与
-- backend/src/main/java/com/tlias/pojo/*.java 反推，与后端 Mapper 保持一致。
-- =====================================================================

CREATE DATABASE IF NOT EXISTS `tlias`
    DEFAULT CHARACTER SET utf8mb4
    DEFAULT COLLATE utf8mb4_general_ci;

USE `tlias`;

-- ---------------------------------------------------------------------
-- 部门表
-- ---------------------------------------------------------------------
DROP TABLE IF EXISTS `dept`;
CREATE TABLE `dept` (
    `id`          INT          NOT NULL AUTO_INCREMENT COMMENT '主键',
    `name`        VARCHAR(50)  NOT NULL COMMENT '部门名称',
    `create_time` DATETIME     DEFAULT NULL COMMENT '创建时间',
    `update_time` DATETIME     DEFAULT NULL COMMENT '修改时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='部门';

-- ---------------------------------------------------------------------
-- 员工表
-- ---------------------------------------------------------------------
DROP TABLE IF EXISTS `emp`;
CREATE TABLE `emp` (
    `id`          INT          NOT NULL AUTO_INCREMENT COMMENT '主键',
    `username`    VARCHAR(20)  NOT NULL COMMENT '用户名',
    `password`    VARCHAR(64)  DEFAULT NULL COMMENT '密码',
    `name`        VARCHAR(20)  NOT NULL COMMENT '姓名',
    `gender`      TINYINT      DEFAULT NULL COMMENT '性别 1:男 2:女',
    `phone`       VARCHAR(20)  DEFAULT NULL COMMENT '手机号',
    `job`         TINYINT      DEFAULT NULL COMMENT '职位 1:班主任 2:讲师 3:学工主管 4:教研主管 5:咨询师',
    `salary`      INT          DEFAULT NULL COMMENT '薪资',
    `image`       VARCHAR(255) DEFAULT NULL COMMENT '头像地址',
    `entry_date`  DATE         DEFAULT NULL COMMENT '入职日期',
    `dept_id`     INT          DEFAULT NULL COMMENT '所属部门id',
    `create_time` DATETIME     DEFAULT NULL COMMENT '创建时间',
    `update_time` DATETIME     DEFAULT NULL COMMENT '修改时间',
    PRIMARY KEY (`id`),
    KEY `idx_emp_dept_id` (`dept_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='员工';

-- ---------------------------------------------------------------------
-- 员工工作经历表
-- 注意：begin / end 为 MySQL 保留字，必须使用反引号
-- ---------------------------------------------------------------------
DROP TABLE IF EXISTS `emp_expr`;
CREATE TABLE `emp_expr` (
    `id`      INT         NOT NULL AUTO_INCREMENT COMMENT '主键',
    `emp_id`  INT         NOT NULL COMMENT '员工id',
    `begin`   DATE        DEFAULT NULL COMMENT '开始时间',
    `end`     DATE        DEFAULT NULL COMMENT '结束时间',
    `company` VARCHAR(50) DEFAULT NULL COMMENT '所在公司',
    `job`     VARCHAR(50) DEFAULT NULL COMMENT '职位',
    PRIMARY KEY (`id`),
    KEY `idx_emp_expr_emp_id` (`emp_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='员工工作经历';

-- ---------------------------------------------------------------------
-- 班级表
-- ---------------------------------------------------------------------
DROP TABLE IF EXISTS `clazz`;
CREATE TABLE `clazz` (
    `id`          INT          NOT NULL AUTO_INCREMENT COMMENT '主键',
    `name`        VARCHAR(50)  NOT NULL COMMENT '班级名称',
    `room`        VARCHAR(50)  DEFAULT NULL COMMENT '班级教室',
    `begin_date`  DATE         DEFAULT NULL COMMENT '开课时间',
    `end_date`    DATE         DEFAULT NULL COMMENT '结课时间',
    `master_id`   INT          DEFAULT NULL COMMENT '班主任（员工id）',
    `subject`     TINYINT      DEFAULT NULL COMMENT '学科 1:java 2:前端 3:大数据 4:Python 5:Go 6:嵌入式',
    `create_time` DATETIME     DEFAULT NULL COMMENT '创建时间',
    `update_time` DATETIME     DEFAULT NULL COMMENT '修改时间',
    PRIMARY KEY (`id`),
    KEY `idx_clazz_master_id` (`master_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='班级';

-- ---------------------------------------------------------------------
-- 学员表
-- ---------------------------------------------------------------------
DROP TABLE IF EXISTS `student`;
CREATE TABLE `student` (
    `id`              INT          NOT NULL AUTO_INCREMENT COMMENT '主键',
    `name`            VARCHAR(20)  NOT NULL COMMENT '姓名',
    `no`              VARCHAR(20)  DEFAULT NULL COMMENT '学号',
    `gender`          TINYINT      DEFAULT NULL COMMENT '性别 1:男 2:女',
    `phone`           VARCHAR(20)  DEFAULT NULL COMMENT '手机号',
    `id_card`         VARCHAR(20)  DEFAULT NULL COMMENT '身份证号',
    `is_college`      TINYINT      DEFAULT NULL COMMENT '是否院校学生 1:是 0:否',
    `address`         VARCHAR(100) DEFAULT NULL COMMENT '联系地址',
    `degree`          TINYINT      DEFAULT NULL COMMENT '学历 1:初中 2:高中 3:大专 4:本科 5:硕士 6:博士',
    `graduation_date` DATE         DEFAULT NULL COMMENT '毕业时间',
    `clazz_id`        INT          DEFAULT NULL COMMENT '所属班级id',
    `violation_count` INT          DEFAULT 0 COMMENT '违纪次数',
    `violation_score` INT          DEFAULT 0 COMMENT '违纪扣分',
    `create_time`     DATETIME     DEFAULT NULL COMMENT '创建时间',
    `update_time`     DATETIME     DEFAULT NULL COMMENT '修改时间',
    PRIMARY KEY (`id`),
    KEY `idx_student_clazz_id` (`clazz_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='学员';

-- ---------------------------------------------------------------------
-- 操作日志表
-- ---------------------------------------------------------------------
DROP TABLE IF EXISTS `operate_log`;
CREATE TABLE `operate_log` (
    `id`             BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
    `operate_emp_id` INT          DEFAULT NULL COMMENT '操作人id',
    `operate_time`   DATETIME     DEFAULT NULL COMMENT '操作时间',
    `class_name`     VARCHAR(255) DEFAULT NULL COMMENT '操作类名',
    `method_name`    VARCHAR(255) DEFAULT NULL COMMENT '操作方法名',
    `method_params`  TEXT         DEFAULT NULL COMMENT '方法请求参数',
    `return_value`   TEXT         DEFAULT NULL COMMENT '返回值',
    `cost_time`      BIGINT       DEFAULT NULL COMMENT '执行耗时（ms）',
    PRIMARY KEY (`id`),
    KEY `idx_operate_log_emp_id` (`operate_emp_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='操作日志';


-- =====================================================================
-- 初始化数据
-- =====================================================================

-- 部门
INSERT INTO `dept` (`id`, `name`, `create_time`, `update_time`)
VALUES (1, '学工部', NOW(), NOW()),
       (2, '教研部', NOW(), NOW()),
       (3, '就业指导部', NOW(), NOW()),
       (4, '人事部', NOW(), NOW());

-- 员工（所有账号初始密码均为 123456）
INSERT INTO `emp` (`id`, `username`, `password`, `name`, `gender`, `phone`, `job`, `salary`, `image`,
                   `entry_date`, `dept_id`, `create_time`, `update_time`)
VALUES (1, 'admin', '123456', '超级管理员', 1, '13800000000', 4, 20000,
        'https://web-65.oss-cn-beijing.aliyuncs.com/default-avatar.jpg', '2020-01-01', 4, NOW(), NOW()),
       (2, 'songjiang', '123456', '宋江', 1, '13800000001', 4, 15000,
        'https://web-65.oss-cn-beijing.aliyuncs.com/default-avatar.jpg', '2019-03-01', 4, NOW(), NOW()),
       (3, 'linpingzhi', '123456', '林平之', 1, '13800000002', 2, 12000,
        'https://web-65.oss-cn-beijing.aliyuncs.com/default-avatar.jpg', '2021-07-01', 2, NOW(), NOW()),
       (4, 'zhangwuji', '123456', '张无忌', 1, '13800000003', 1, 10000,
        'https://web-65.oss-cn-beijing.aliyuncs.com/default-avatar.jpg', '2022-02-01', 1, NOW(), NOW()),
       (5, 'xiaozhao', '123456', '小昭', 2, '13800000004', 3, 9000,
        'https://web-65.oss-cn-beijing.aliyuncs.com/default-avatar.jpg', '2022-09-01', 1, NOW(), NOW()),
       (6, 'jinyong', '123456', '金庸', 1, '13800000005', 5, 11000,
        'https://web-65.oss-cn-beijing.aliyuncs.com/default-avatar.jpg', '2020-05-01', 3, NOW(), NOW());

-- 员工工作经历
INSERT INTO `emp_expr` (`emp_id`, `begin`, `end`, `company`, `job`)
VALUES (3, '2012-07-01', '2019-03-03', '百度科技股份有限公司', 'Java开发'),
       (3, '2019-03-15', '2023-03-01', '阿里巴巴科技股份有限公司', '架构师'),
       (4, '2015-07-01', '2019-03-03', '中软国际股份有限公司', 'Java开发');

-- 班级
INSERT INTO `clazz` (`id`, `name`, `room`, `begin_date`, `end_date`, `master_id`, `subject`,
                      `create_time`, `update_time`)
VALUES (1, '黄埔班一期', '101', '2023-01-01', '2023-12-31', 4, 1, NOW(), NOW()),
       (2, 'JavaEE就业166期', '105', '2023-07-20', '2024-02-20', 3, 1, NOW(), NOW()),
       (3, '黄埔四期', '209', '2023-08-01', '2024-02-15', 2, 1, NOW(), NOW()),
       (4, '前端就业100期', '210', '2023-09-01', '2024-03-01', 2, 2, NOW(), NOW());

-- 学员
INSERT INTO `student` (`id`, `name`, `no`, `gender`, `phone`, `id_card`, `is_college`, `address`,
                       `degree`, `graduation_date`, `clazz_id`, `violation_count`, `violation_score`,
                       `create_time`, `update_time`)
VALUES (1, '张三', '2023001001', 1, '13309230911', '110090110090110090', 0, '回龙观东大街110号',
        4, '2020-07-01', 1, 0, 0, NOW(), NOW()),
       (2, '李四', '2023001002', 1, '13309230912', '110090110090110091', 0, '回龙观东大街110号',
        4, '2020-07-01', 1, 1, 2, NOW(), NOW()),
       (3, 'Lily', '2023001003', 2, '13309230913', '110090110090110092', 0, '回龙观东大街110号',
        4, '2020-07-01', 1, 2, 5, NOW(), NOW()),
       (4, 'Jerry', '2023001004', 1, '15309232323', '110090110090110093', 0, '回龙观东大街110号',
        4, '2020-07-01', 2, 0, 0, NOW(), NOW()),
       (5, '王五', '2023001005', 1, '13509230915', '110090110090110094', 1, '昌平回龙观',
        3, '2021-07-01', 2, 0, 0, NOW(), NOW()),
       (6, '赵六', '2023001006', 2, '13609230916', '110090110090110095', 1, '昌平回龙观',
        5, '2019-07-01', 2, 0, 0, NOW(), NOW());
