# Tlias 智能学习辅助系统

> 一套前后端分离的教务 / 人事管理系统。后端 Spring Boot 4 + MyBatis，前端 Vue 3 + Element Plus + ECharts。

系统覆盖 **部门、员工、班级、学员** 四类主数据的增删改查与条件分页查询，
在此之上提供 **数据统计报表**（ECharts 可视化）、**JWT 登录鉴权**、
**AOP 操作日志审计** 三项横切能力。

---

## 目录

- [功能特性](#功能特性)
- [技术栈](#技术栈)
- [项目结构](#项目结构)
- [环境要求](#环境要求)
- [快速启动](#快速启动)
- [默认账号](#默认账号)
- [接口一览](#接口一览)
- [关键机制](#关键机制)
- [Git 工作流](#git-工作流)
- [常见问题](#常见问题)
- [相关文档](#相关文档)

---

## 功能特性

| 模块 | 说明 |
| --- | --- |
| 登录认证 | 用户名 + 密码登录，签发 JWT；拦截器统一校验令牌，失效自动跳转登录页 |
| 部门管理 | 部门列表查询、新增、修改、删除、按 ID 查询 |
| 员工管理 | 条件分页查询（姓名 / 性别 / 入职日期区间）、增删改查、批量删除、工作经历子表联动 |
| 班级管理 | 条件分页查询（班级名 / 结课日期区间）、增删改查、状态自动推导（未开班 / 已开班 / 已结课） |
| 学员管理 | 条件分页查询（姓名 / 学历 / 班级）、增删改查、批量删除、违纪扣分处理 |
| 数据统计 | 员工性别分布、员工职位分布、学员学历分布、班级人数分布（ECharts 渲染） |
| 操作日志 | `@Log` 注解 + AOP 切面自动记录操作人、类名、方法名、入参、返回值、耗时，并支持分页查询 |
| 文件上传 | 统一 UUID 重命名落盘，目录可配置，自动创建目录并返回文件名 |

---

## 技术栈

### 后端

| 分类 | 选型 | 版本 |
| --- | --- | --- |
| 框架 | Spring Boot | 4.0.3 |
| 语言 | Java | 25 |
| ORM | MyBatis Spring Boot Starter | 4.0.1 |
| 数据库 | MySQL | 8.x |
| 分页 | PageHelper | 1.4.7 |
| 认证 | JJWT | 0.11.5 |
| 切面 | Spring AOP + AspectJ | 5.3.30 / 1.9.21 |
| JSON | Jackson | 2.17.0 |
| 对象存储 | Aliyun OSS SDK | 3.16.1 |
| 日志 | Logback | 随 Boot |
| 构建 | Maven | 3.9+ |

### 前端

| 分类 | 选型 | 版本 |
| --- | --- | --- |
| 框架 | Vue | 3.2.38 |
| 路由 | Vue Router | 4.1.5 |
| 构建 | Vite | 3.0.9 |
| UI | Element Plus | 2.4.4 |
| 图表 | ECharts | 6.0.0 |
| HTTP | Axios | 1.7.2 |
| 代码规范 | ESLint + Prettier | 8.22 / 2.7.1 |

---

## 项目结构

```
my-web-project/
├── README.md                   # 项目说明（本文档）
├── CHANGELOG.md                # 变更记录
├── .gitignore                  # 统一忽略构建产物、日志、密钥
├── .githooks/pre-commit        # 提交前自动检查：密钥扫描 / 构建产物拦截
├── .github/                    # PR 与 Issue 模板
├── sql/
│   └── init.sql                # 建库建表 + 初始化数据
├── docs/
│   ├── git-workflow.md         # Git 工作流规范
│   ├── api接口文档.md           # 手写版接口文档
│   ├── api接口文档.pdf          # PDF 版接口文档
│   └── Web开发-AI.openapi.json  # OpenAPI 3.0 规范文件
├── deploy/
│   └── nginx/                  # 生产环境 nginx 配置
│       ├── conf/nginx.conf     # 已定制：90 端口 + /api 反向代理
│       ├── html/               # 前端构建产物投放目录（git 忽略）
│       └── README.md           # 部署步骤说明
├── backend/                    # 后端服务
│   ├── pom.xml
│   └── src/main/
│       ├── java/com/tlias/
│       │   ├── TliasApplication.java      # 启动类
│       │   ├── anno/                       # @Log 操作日志注解
│       │   ├── aop/                        # 操作日志切面
│       │   ├── config/                     # 拦截器注册
│       │   ├── controller/                 # 8 个控制器
│       │   ├── exception/                  # 全局异常处理 + 业务异常
│       │   ├── filter/                     # Token 过滤器
│       │   ├── interceptor/                # Token 拦截器
│       │   ├── mapper/                     # MyBatis Mapper 接口
│       │   ├── pojo/                       # 实体 / 查询参数 / 响应包装
│       │   ├── service/                    # 业务接口
│       │   │   └── impl/                   # 业务实现
│       │   └── utils/                      # JwtUtil、CurrentHolder
│       └── resources/
│           ├── application.yaml            # 主配置（敏感项全部走占位符）
│           ├── application-local.example.yaml  # 本地配置模板
│           ├── logback.xml                 # 日志配置
│           ├── com/tlias/mapper/*.xml       # MyBatis SQL 映射
│           └── static/upload.html          # 上传接口测试页
└── frontend/                   # 前端工程
    ├── package.json
    ├── vite.config.js          # 开发代理 /api → localhost:8080
    └── src/
        ├── api/                # 按业务域拆分的接口封装
        │   ├── clazz.js
        │   ├── dept.js
        │   ├── emp.js
        │   ├── empStat.js
        │   ├── log.js
        │   ├── login.js
        │   ├── stu.js
        │   └── stuAndClazzStat.js
        ├── assets/             # 全局样式与静态资源
        ├── router/index.js     # 路由与菜单映射
        ├── utils/request.js    # Axios 实例 + 令牌注入 + 401 拦截
        └── views/              # 页面组件
            ├── layout/         # 主框架（顶栏 / 侧边菜单 / 退出登录）
            ├── login/          # 登录页
            ├── index/          # 工作台
            ├── dept/           # 部门管理
            ├── emp/            # 员工管理
            ├── clazz/          # 班级管理
            ├── stu/            # 学员管理
            ├── log/            # 操作日志
            └── report/
                ├── emp/        # 员工统计报表
                └── stu/        # 学员统计报表
```

---

## 环境要求

| 依赖 | 版本 | 备注 |
| --- | --- | --- |
| JDK | 25+ | Spring Boot 4 要求 |
| Maven | 3.9+ | 需能拉取依赖 |
| MySQL | 8.0+ | 字符集 utf8mb4 |
| Node.js | 18+ | Vite 3 要求 |
| npm | 9+ | 随 Node 安装 |
| nginx | 1.22+ | 仅生产部署需要 |

---

## 快速启动

### 1. 初始化数据库

```bash
mysql -u root -p < sql/init.sql
```

脚本会创建 `tlias` 库、6 张表，并写入演示数据。若库已存在请先手工清理。

### 2. 配置数据库连接

后端默认从环境变量读取敏感配置，二选一：

**方式 A — 环境变量（推荐）**

```bash
export DB_USERNAME=root
export DB_PASSWORD=你的MySQL密码
export JWT_SECRET=至少32字节的密钥
```

**方式 B — 本地配置文件**

```bash
cp backend/src/main/resources/application-local.example.yaml \
   backend/src/main/resources/application-local.yaml
# 编辑其中的 password / secret
```

`application-local.yaml` 已被 `.gitignore` 忽略，不会误提交。

> 可选环境变量一览：`DB_HOST` `DB_PORT` `DB_NAME` `DB_USERNAME` `DB_PASSWORD`
> `JWT_SECRET` `JWT_EXPIRATION` `UPLOAD_DIR` `LOG_PATH`

### 3. 启动后端

```bash
cd backend
mvn spring-boot:run
```

服务启动于 <http://localhost:8080>，可用 `mvn spring-boot:run -Dspring-boot.run.profiles=local`
激活本地配置。

### 4. 启动前端

```bash
cd frontend
npm install
npm run dev
```

访问 <http://localhost:5173>。开发服务器已将 `/api` 代理到 `http://localhost:8080`，
无需额外跨域配置。

### 5. 生产部署（可选）

参照 [`deploy/nginx/README.md`](deploy/nginx/README.md)：构建前端 → 拷贝到
`deploy/nginx/html/` → 启动 nginx（前端 90 端口，API 反代至 8080）。

---

## 默认账号

初始化脚本创建了 6 个员工账号，**密码统一为 `123456`**：

| 用户名 | 姓名 | 职位 | 部门 |
| --- | --- | --- | --- |
| `admin` | 超级管理员 | 教研主管 | 人事部 |
| `songjiang` | 宋江 | 教研主管 | 人事部 |
| `linpingzhi` | 林平之 | 讲师 | 教研部 |
| `zhangwuji` | 张无忌 | 班主任 | 学工部 |
| `xiaozhao` | 小昭 | 学工主管 | 学工部 |
| `jinyong` | 金庸 | 咨询师 | 就业指导部 |

---

## 接口一览

统一响应格式（`Result`）：

```json
{ "code": 1, "msg": "success", "data": {} }
```

`code` 为 `1` 表示成功、`0` 表示失败。分页接口的 `data` 结构为 `{ "total": 100, "rows": [...] }`。

| 模块 | 方法 | 路径 | 说明 |
| --- | --- | --- | --- |
| 登录 | POST | `/login` | 登录，返回用户信息与 JWT |
| 部门 | GET | `/depts` | 查询全部部门 |
| 部门 | GET | `/depts/{id}` | 按 ID 查询 |
| 部门 | POST | `/depts` | 新增 |
| 部门 | PUT | `/depts` | 修改 |
| 部门 | DELETE | `/depts?id={id}` | 删除 |
| 员工 | GET | `/emps` | 条件分页查询 |
| 员工 | GET | `/emps/list` | 查询全部员工 |
| 员工 | GET | `/emps/{id}` | 按 ID 查询（含工作经历） |
| 员工 | POST | `/emps` | 新增 |
| 员工 | PUT | `/emps` | 修改 |
| 员工 | DELETE | `/emps?ids=1,2` | 批量删除 |
| 班级 | GET | `/clazzs` | 条件分页查询 |
| 班级 | GET | `/clazzs/list` | 查询全部班级 |
| 班级 | GET | `/clazzs/{id}` | 按 ID 查询 |
| 班级 | POST | `/clazzs` | 新增 |
| 班级 | PUT | `/clazzs` | 修改 |
| 班级 | DELETE | `/clazzs/{id}` | 删除 |
| 学员 | GET | `/students` | 条件分页查询 |
| 学员 | GET | `/students/{id}` | 按 ID 查询 |
| 学员 | POST | `/students` | 新增 |
| 学员 | PUT | `/students` | 修改 |
| 学员 | DELETE | `/students/{ids}` | 批量删除 |
| 学员 | PUT | `/students/violation/{id}/{score}` | 违纪扣分 |
| 统计 | GET | `/report/empGenderData` | 员工性别分布 |
| 统计 | GET | `/report/empJobData` | 员工职位分布 |
| 统计 | GET | `/report/studentDegreeData` | 学员学历分布 |
| 统计 | GET | `/report/studentCountData` | 班级人数分布 |
| 日志 | GET | `/log/page` | 操作日志分页查询 |
| 上传 | POST | `/upload` | 文件上传（multipart） |

完整参数与响应示例见 [`docs/api接口文档.md`](docs/api接口文档.md)。

---

## 关键机制

### JWT 登录鉴权

```
登录 ──> 服务端校验账号密码 ──> 签发 JWT（HS256）
                                    │
请求 ──> TokenInterceptor ──> 校验请求头 Token 头
                                    │  ├─ 无令牌 / 校验失败 ──> 401
                                    │  └─ 校验通过 ──> 存入 ThreadLocal（当前操作人 ID）──> 执行业务
```

- 令牌存放于请求头 `Token`，前端由 `src/utils/request.js` 的请求拦截器统一注入。
- 响应拦截器捕获 `401` 后弹出提示并跳转 `/login`。
- 当前操作人 ID 通过 `CurrentHolder`（ThreadLocal）传递，供操作日志切面使用；
  在 `postHandle` 中调用 `remove()` 防止线程复用导致的内存泄漏。
- 密钥与有效期由 `jwt.secret` / `jwt.expiration` 配置注入，生产环境用
  `JWT_SECRET` 环境变量覆盖。

### 操作日志 AOP

在 Controller 方法上标注 `@Log` 即可，切面 `OperateLogAspect` 环绕通知自动记录：

| 字段 | 来源 |
| --- | --- |
| `operateEmpId` | `CurrentHolder.getCurrentId()` |
| `operateTime` | `LocalDateTime.now()` |
| `className` / `methodName` | 切点反射获取 |
| `methodParams` / `returnValue` | Jackson 序列化 |
| `costTime` | 环绕通知首尾时间差 |

### 员工与工作经历的事务一致性

`EmpServiceImpl` 对新增、修改、删除三个方法标注
`@Transactional(rollbackFor = Exception.class)`，保证 `emp` 主表与 `emp_expr`
子表的写操作要么全部成功、要么全部回滚（`rollbackFor` 用于覆盖受检异常）。

### 班级状态自动推导

`ClazzsServiceImpl#queryPageResult` 依据当前日期与 `beginDate` / `endDate`
的比较结果，为每个班级推导 `status`：`已结课` / `已开班` / `未开班`。

### 统一异常处理

| 异常 | 处理方式 |
| --- | --- |
| `BusinessException` | 直接返回异常携带的提示信息（如「用户名或密码错误」） |
| `DuplicateKeyException` | 解析数据库报错，翻译为「xxx已经存在」 |
| 其他异常 | 记录完整堆栈，对外只返回「出错了，请联系管理员」 |

---

## Git 工作流

完整规范见 [`docs/git-workflow.md`](docs/git-workflow.md)，要点如下。

### 分支模型

主干开发变体：`main` 始终保持可构建、可部署，改动在短期特性分支完成后合入。

```bash
git checkout main && git pull
git checkout -b feature/student-violation   # feature/ fix/ refactor/ chore/ docs/
# ... 开发、验证 ...
git checkout main && git merge --no-ff feature/student-violation
git branch -d feature/student-violation
```

分支目标生命周期 1-3 天，合入后立即删除。

### 提交规范

```
<type>: <简短描述>

<可选正文，说明「为什么」而不是「做了什么」>
```

类型：`feat` `fix` `refactor` `test` `docs` `chore` `perf` `revert`

**一次提交只做一件事。** 格式化改动与行为改动分开，重构与新功能分开。

```
✅  feat: add student violation endpoint
    fix: handle empty file in upload controller
    refactor: extract pagination params into QueryParam objects

❌  feat: add violation feature and fix upload and refactor pagination
```

### 提交前自动检查

仓库内置 pre-commit hook，已通过 `core.hooksPath` 启用：

- 扫描新增行中的明文口令 / 密钥（`password` `secret` `api_key` 等）
- 拦截构建产物、日志、`.env` 等不应入库的文件

若 hook 未生效，手动启用：

```bash
git config core.hooksPath .githooks
```

### 版本与发布

版本号遵循语义化版本，由 git tag 决定，不手工在多处编辑：

```bash
git tag -a v0.1.0 -m "Release 0.1.0"
git push origin v0.1.0
```

每次发布同步更新 `CHANGELOG.md`，按用户影响分组记录。

---

## 常见问题

**Q：启动报 `Access denied for user 'root'@'localhost'`**

数据库密码未配置。设置 `DB_PASSWORD` 环境变量，或配置 `application-local.yaml`。

**Q：前端请求全部 404 / 502**

后端未启动，或后端端口不是 8080。检查 `frontend/vite.config.js` 中的代理目标地址。

**Q：启动报 `Communications link failure`**

MySQL 未启动，或 `DB_HOST` / `DB_PORT` 与实际不符。默认 `localhost:3306`。

**Q：登录后立刻跳回登录页**

JWT 密钥不匹配——`application.yaml` 中的 `jwt.secret` 与 `application-local.yaml`
中的配置不一致，导致签发与校验使用不同密钥。两处保持一致即可。

**Q：控制台打印大量 SQL 日志**

`application.yaml` 中 `mybatis.configuration.log-impl` 打开了
`StdOutImpl`（SQL 打印）。生产环境请将其注释掉。

**Q：日志文件写到了奇怪的位置**

`logback.xml` 的日志目录由 `LOG_PATH` 决定，默认 `./logs`（相对当前工作目录）。
可通过环境变量覆盖为绝对路径。

**Q：`mvn` 拉不到依赖 / 构建卡住**

检查 Maven 是否配置了镜像仓库。可在 `~/.m2/settings.xml` 中配置阿里云镜像。

**Q：提交被 pre-commit 拒绝**

说明暂存内容中混入了疑似密钥或构建产物。确认后用环境变量替代硬编码，
或在确认无误时用 `git commit --no-verify` 跳过（不推荐）。

---

## 相关文档

| 文档 | 说明 |
| --- | --- |
| [docs/git-workflow.md](docs/git-workflow.md) | Git 工作流规范：分支、提交、发布 |
| [docs/api接口文档.md](docs/api接口文档.md) | 全部接口的参数与响应示例 |
| [docs/Web开发-AI.openapi.json](docs/Web开发-AI.openapi.json) | OpenAPI 3.0 规范，可导入 Apifox / Postman |
| [deploy/nginx/README.md](deploy/nginx/README.md) | 生产环境 nginx 部署步骤 |
| [sql/init.sql](sql/init.sql) | 建表语句与种子数据 |
| [CHANGELOG.md](CHANGELOG.md) | 变更记录 |
