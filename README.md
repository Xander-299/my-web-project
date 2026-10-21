<div align="center">

# Tlias 智能学习辅助系统

**前后端分离的教务 / 人事管理系统**

部门 · 员工 · 班级 · 学员 四类主数据 · JWT 鉴权 · AOP 日志 · ECharts 报表

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.3-6DB33F?logo=springboot&logoColor=white)](#-技术选型)
[![Java](https://img.shields.io/badge/Java-25-ED8B00?logo=openjdk&logoColor=white)](#-技术选型)
[![Vue](https://img.shields.io/badge/Vue-3.2-42B883?logo=vuedotjs&logoColor=white)](#-技术选型)
[![MySQL](https://img.shields.io/badge/MySQL-8-4479A1?logo=mysql&logoColor=white)](#-技术选型)

[**🚀 快速启动**](#-快速启动) ·
[**📖 接口文档**](docs/api接口文档.md) ·
[**🔐 登录鉴权**](#-登录鉴权一次请求要过几道关) ·
[**📚 文档索引**](#-文档索引)

</div>

---

## 为什么值得看一眼

这是一套**完整**的前后端分离管理系统（56 个 Java 文件 + 22 个前端文件、31 个接口），而不是一个只有 CRUD 的教学 demo。它回答了三个具体问题：

1. **一条请求要过几道关才算安全？** 登录签发 JWT → 过滤器 → 拦截器 → ThreadLocal 传递操作人 → AOP 切面取用，一环都不缺；令牌失效在前端响应拦截器统一兜住跳登录。
2. **横切能力怎么不靠复制粘贴？** `@Log` 一个注解，环绕通知自动记录操作人 / 类名 / 方法名 / 入参 / 返回值 / 耗时并落库，业务代码零侵入。
3. **密钥怎么保证不会误提交？** 敏感项全部走环境变量或已忽略的本地配置；仓库内置 pre-commit 守卫，暂存内容出现疑似密钥直接拒绝提交。

---

## 🚀 快速启动

| 依赖 | 版本 | 备注 |
|---|---|---|
| JDK | 25+ | Spring Boot 4 要求 |
| Maven | 3.9+ | 需能拉取依赖 |
| MySQL | 8.0+ | 字符集 utf8mb4 |
| Node.js | 18+ | Vite 3 要求 |
| npm | 9+ | 随 Node 安装 |
| nginx | 1.22+ | 仅生产部署需要 |

**① 初始化数据库**

```bash
mysql -u root -p < sql/init.sql
```

脚本创建 `tlias` 库、6 张表并写入演示数据。库已存在时请先手工清理。

**② 配置数据库连接**（二选一）

```bash
# 方式 A — 环境变量（推荐）
export DB_USERNAME=root
export DB_PASSWORD='<你的 MySQL 密码>'
export JWT_SECRET='<至少 32 字节的随机密钥>'
```

```bash
# 方式 B — 本地配置文件（已被 .gitignore 忽略）
cp backend/src/main/resources/application-local.example.yaml \
   backend/src/main/resources/application-local.yaml
```

> 可选环境变量：`DB_HOST` `DB_PORT` `DB_NAME` `DB_USERNAME` `DB_PASSWORD`
> `JWT_SECRET` `JWT_EXPIRATION` `UPLOAD_DIR` `LOG_PATH`

**③ 启动后端**

```bash
cd backend
mvn spring-boot:run
```

服务启动于 <http://localhost:8080>。需要激活本地配置时加
`-Dspring-boot.run.profiles=local`。

**④ 启动前端**

```bash
cd frontend
npm install
npm run dev
```

访问 <http://localhost:5173>。开发服务器已把 `/api` 代理到 `http://localhost:8080`，
无需额外跨域配置。

**⑤ 登录**

初始化脚本创建了 6 个员工账号，**密码统一为 `123456`**：

| 用户名 | 姓名 | 职位 | 部门 |
|---|---|---|---|
| `admin` | 超级管理员 | 教研主管 | 人事部 |
| `songjiang` | 宋江 | 教研主管 | 人事部 |
| `linpingzhi` | 林平之 | 讲师 | 教研部 |
| `zhangwuji` | 张无忌 | 班主任 | 学工部 |
| `xiaozhao` | 小昭 | 学工主管 | 学工部 |
| `jinyong` | 金庸 | 咨询师 | 就业指导部 |

<details>
<summary><b>启动不起来？展开 8 条常见问题</b></summary>

**`Access denied for user 'root'@'localhost'`**
数据库密码未配置。设置 `DB_PASSWORD`，或配置 `application-local.yaml`。

**前端请求全部 404 / 502**
后端未启动，或端口不是 8080。检查 `frontend/vite.config.js` 的代理目标。

**`Communications link failure`**
MySQL 未启动，或 `DB_HOST` / `DB_PORT` 不符。默认 `localhost:3306`。

**登录后立刻跳回登录页**
JWT 密钥不匹配 —— `application.yaml` 的 `jwt.secret` 与 `application-local.yaml`
不一致，签发与校验用了不同密钥。两处保持一致即可。

**控制台打印大量 SQL 日志**
`mybatis.configuration.log-impl` 打开了 `StdOutImpl`。生产环境请注释掉。

**日志文件写到了奇怪的位置**
`logback.xml` 的日志目录由 `LOG_PATH` 决定，默认 `./logs`（相对当前工作目录）。

**`mvn` 拉不到依赖 / 构建卡住**
检查 Maven 镜像仓库，可在 `~/.m2/settings.xml` 配阿里云镜像。

**提交被 pre-commit 拒绝**
暂存内容混入了疑似密钥或构建产物。用环境变量替代硬编码；确认无误时可用
`git commit --no-verify` 跳过（不推荐）。

</details>

---

## 🧭 功能地图

<details open>
<summary><b>业务主数据（四类）</b></summary>

| 模块 | 说明 |
|---|---|
| **部门管理** | 列表查询、新增、修改、删除、按 ID 查询 |
| **员工管理** | 条件分页查询（姓名 / 性别 / 入职日期区间）、增删改查、批量删除、工作经历子表联动 |
| **班级管理** | 条件分页查询（班级名 / 结课日期区间）、增删改查、状态自动推导（未开班 / 已开班 / 已结课） |
| **学员管理** | 条件分页查询（姓名 / 学历 / 班级）、增删改查、批量删除、违纪扣分处理 |

</details>

<details open>
<summary><b>横切能力</b></summary>

| 模块 | 说明 |
|---|---|
| **登录认证** | 用户名 + 密码登录签发 JWT；拦截器统一校验，失效自动跳登录页 |
| **数据统计** | 员工性别 / 员工职位 / 学员学历 / 班级人数 四组分布（ECharts 渲染） |
| **操作日志** | `@Log` 注解 + AOP 切面自动记录操作人、类名、方法名、入参、返回值、耗时，支持分页查询 |
| **文件上传** | 统一 UUID 重命名落盘，目录可配置，自动建目录并返回文件名 |

</details>

<details>
<summary><b>REST 接口一览（31 个）</b></summary>

统一响应格式 `Result`：`code` 为 `1` 表示成功、`0` 表示失败；
分页接口的 `data` 为 `{ "total": 100, "rows": [...] }`。

| 模块 | 方法 | 路径 | 说明 |
|---|---|---|---|
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

完整参数与响应示例见 [`docs/api接口文档.md`](docs/api接口文档.md)，
或把 [`docs/Web开发-AI.openapi.json`](docs/Web开发-AI.openapi.json) 导入 Apifox / Postman。

</details>

---

## 🔐 登录鉴权：一次请求要过几道关

```
登录 ──> 校验账号密码 ──> 签发 JWT（HS256）
                              │
请求 ──> TokenFilter / TokenInterceptor ──> 校验请求头 Token
                              │  ├─ 无令牌 / 校验失败 ──> 401 ──> 前端跳 /login
                              │  └─ 校验通过 ──> CurrentHolder（ThreadLocal）存操作人 ID
                              │                         │
                              └────────────> 执行业务 ←──┘
                                                │
                              AOP 切面读 CurrentHolder ──> 落操作日志
```

每一关都有明确归属，不是「随手加个 if」：

| 关卡 | 位置 | 作用 |
|---|---|---|
| 签发 | `JwtUtil` + `EmpServiceImpl#login` | HS256 签名，密钥与有效期由配置注入 |
| 校验 | `interceptor/TokenInterceptor` | 读请求头 `Token`，失败直接 401 |
| 传递 | `utils/CurrentHolder` | ThreadLocal 存操作人 ID，供切面使用 |
| 兜底 | `frontend/src/utils/request.js` | 响应拦截器捕获 401 → 提示并跳登录 |

> **内存泄漏防护**：`CurrentHolder` 在 `postHandle` 里调用 `remove()` —— 线程池复用线程，
> 不清就会把上一个请求的操作人带给下一个请求。

---

## 🏗 项目结构

```
my-web-project/
├── README.md                   # 项目说明（本文档）
├── CHANGELOG.md                # 变更记录
├── .gitignore                  # 统一忽略构建产物、日志、密钥
├── .githooks/                  # 提交前守卫（凭据扫描 / 构建产物拦截）
│   ├── guard-credentials.sh    # 守卫逻辑
│   └── install-hooks.sh        # 安装脚本，生成 .git/hooks/pre-commit
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
        │   ├── clazz.js · dept.js · emp.js · empStat.js
        │   ├── log.js · login.js · stu.js · stuAndClazzStat.js
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

**分层约定**

```
controller（HTTP 边界）→ service（业务接口）→ service.impl（实现 + 事务）→ mapper（SQL）
```

- **事务边界在 service 实现层** —— `@Transactional(rollbackFor = Exception.class)`，
  `rollbackFor` 用于覆盖受检异常。
- **SQL 只在 Mapper XML 里** —— 接口与语句一一对应，不在 Java 里拼字符串。
- **异常统一收口** —— `BusinessException` 直出提示，`DuplicateKeyException` 翻译成
  「xxx 已经存在」，其余只对外说「出错了，请联系管理员」并记完整堆栈。
- **前端只负责呈现与校验** —— 业务规则不写在前端；`api/` 按业务域拆分，一个模块一个文件。

---

## 🔧 技术选型

**后端**

| 分类 | 选型 | 版本 |
|---|---|---|
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

**前端**

| 分类 | 选型 | 版本 |
|---|---|---|
| 框架 | Vue | 3.2.38 |
| 路由 | Vue Router | 4.1.5 |
| 构建 | Vite | 3.0.9 |
| UI | Element Plus | 2.4.4 |
| 图表 | ECharts | 6.0.0 |
| HTTP | Axios | 1.7.2 |
| 代码规范 | ESLint + Prettier | 8.22 / 2.7.1 |

---

## 🧪 构建与部署

```bash
# 后端打包（产物 target/tlias-*.jar）
cd backend
mvn clean package -DskipTests

# 前端打包（产物 dist/）
cd frontend
npm ci
npm run build
```

### 生产部署（可选）

参照 [`deploy/nginx/README.md`](deploy/nginx/README.md)：
把 `frontend/dist/` 拷进 `deploy/nginx/html/` → 启动 nginx。
`conf/nginx.conf` 已定制为 **前端 90 端口**、`/api` 反代到 **8080**。

---

## 📊 项目规模

| 维度 | 数据 |
|---|---|
| 后端 | Java **56 个文件 / 2,208 行** · Controller **8 个** · Mapper XML **8 个** |
| 前端 | Vue **11 个文件 / 2,282 行** · JS（src）**11 个文件 / 231 行** |
| 数据库 | **6 张表** · 种子数据含 6 个员工账号 |
| 接口 | **31 个** REST 端点（含 4 个统计接口） |
| 提交 | **11 次提交**（尚未打版本 tag） |

> 数字取自仓库本身（文件行数统计、`git rev-list --count HEAD`），可复算。

---

## 🔒 安全边界

- **敏感配置零入库**：`application.yaml` 里只有占位符，真值走环境变量或
  `application-local.yaml`（已被 `.gitignore` 忽略）。
- **JWT 密钥长度受控**：HS256 要求 ≥32 字节，配置项与本地文件必须一致，否则会出现
  「登录成功却被立刻踢回登录页」。
- **令牌失效统一兜底**：拦截器返回 401，前端响应拦截器集中处理跳转，不在每个页面写一遍。
- **上传文件名不可信**：统一 UUID 重命名后落盘，目录可配置，避免路径穿越与重名覆盖。

---

## ✅ 质量门禁

提交前自动跑（`.githooks/guard-credentials.sh`，由 `install-hooks.sh` 装进
`.git/hooks/pre-commit`）：

```
凭据扫描 → 构建产物拦截 → 通过才允许提交
```

- 命中 `password` / `secret` / `api_key` / `access-key-id` 等模式且值**非占位形式**时拒绝提交；
- 占位符写法（`${VAR}`、`$VAR`、`<placeholder>`、`your_password_here`、`changeme`、
  `xxxx...`）一律放行，不影响开源配置正常提交；
- 拦截 `target/`、`dist/`、`logs/`、`.env` 等不应入库的文件。

```bash
sh .githooks/install-hooks.sh      # 克隆后执行一次
sh .githooks/guard-credentials.sh  # 也可单独手动检查暂存内容
```

**提交规范**：`<type>: <简短描述>`，`type` 取 `feat` `fix` `refactor` `test` `docs`
`chore` `perf` `revert`；**一次提交只做一件事**（格式化与行为分开、重构与新功能分开）。

**分支与发布**：`main` 始终可构建可部署，改动在短期特性分支（`feature/` `fix/`
`refactor/` `chore/` `docs/`）完成后 `--no-ff` 合入；版本号由 git tag 决定，
发布时同步更新 `CHANGELOG.md`。

---

## 📚 文档索引

| 文档 | 内容 |
|---|---|
| [`docs/git-workflow.md`](docs/git-workflow.md) | Git 工作流规范：分支、提交、发布 |
| [`docs/api接口文档.md`](docs/api接口文档.md) | 全部接口的参数与响应示例 |
| [`docs/Web开发-AI.openapi.json`](docs/Web开发-AI.openapi.json) | OpenAPI 3.0 规范，可导入 Apifox / Postman |
| [`deploy/nginx/README.md`](deploy/nginx/README.md) | 生产环境 nginx 部署步骤 |
| [`sql/init.sql`](sql/init.sql) | 建表语句与种子数据 |
| [`CHANGELOG.md`](CHANGELOG.md) | 变更记录 |

---

## 📄 License

仅供学习与个人使用（All rights reserved）。转载或商用请先联系作者。
