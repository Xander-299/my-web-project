# CHANGELOG

本文件记录 Tlias 智能学习辅助系统的用户可见变更。
格式参考 [Keep a Changelog](https://keepachangelog.com/zh-CN/1.1.0/)，
版本号遵循 [语义化版本](https://semver.org/lang/zh-CN/)。

## [未发布]

### 新增
- 根级 `README.md`：项目说明、技术栈、目录结构、快速启动、接口一览与常见问题
- `sql/init.sql`：建库建表脚本与初始化数据
- `deploy/nginx/README.md`：生产环境 nginx 部署步骤
- `docs/` 目录：汇总接口文档与 OpenAPI 规范

### 变更
- 项目结构重组：文档统一收敛到 `docs/`，nginx 配置迁至 `deploy/nginx/`，
  SQL 脚本独立到 `sql/`
- 包名由 `com.example` 改为 `com.tlias`，`Filter` / `Impl` 包名规范化为小写
- 敏感配置（数据库口令、JWT 密钥）改为环境变量注入，不再入库
- 日志与上传目录路径改为可配置，移除硬编码绝对路径

### 修复
- 登录失败不再依赖空指针异常表达，改用显式业务异常，避免任意 NPE 被
  误报为「用户名或密码错误」

### 移除
- 构建产物（`backend/target/`）、运行时日志、nginx 可执行文件不再纳入版本控制
- 移除重复的接口文档副本与教学残留类 `DemoFilter` / `DemoInterceptor`

## [0.1.0] - 2026-08-11

首个可用版本：部门、员工、班级、学员四类主数据的增删改查与条件分页查询，
数据统计报表，JWT 登录鉴权，AOP 操作日志审计，文件上传。
