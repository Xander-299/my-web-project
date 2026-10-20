# Git 工作流规范

本项目的 Git 使用约定。目标是让每一笔提交都能独立回滚、便于代码审查，
让 `git log` 本身成为项目变更的有效文档。

---

## 分支模型

采用 **主干开发（Trunk-Based）** 变体：`main` 始终保持可构建、可部署，
所有改动在短期特性分支上完成后合入。

```
main ──●──●──●──●──●──●──●──●──●──  (始终可部署)
        ╲      ╱ ╲    ╱
         ●──●─╱    ●──╱    ← 短生命周期特性分支（1-3 天内合入）
```

### 分支命名

| 前缀 | 用途 | 示例 |
| --- | --- | --- |
| `feature/` | 新功能 | `feature/student-violation` |
| `fix/` | 缺陷修复 | `fix/token-401-loop` |
| `refactor/` | 不改变外部行为的重构 | `refactor/extract-query-params` |
| `chore/` | 工具链、依赖、配置 | `chore/bump-vite` |
| `docs/` | 纯文档变更 | `docs/api-reference` |
| `release/` | 发布准备（仅在需要稳定版本时创建） | `release/v0.2.0` |

### 分支纪律

- 分支从 `main` 切出，完成后**尽快合入**（目标 1-3 天）。长期存活的分支
  会持续累积合并风险。
- 合入后**立即删除**特性分支。
- 不使用 `--force-push` 推送到 `main` 或任何共享分支。
- `main` 上的每个提交都应能通过 `mvn clean package` 与 `npm run build`。

---

## 提交规范

### 消息格式

```
<type>: <简短描述>

<可选正文，说明「为什么」而不是「做了什么」>
```

标题行不超过 72 字符，使用祈使语气（"add" 而非 "added"）。

### 类型约定

| type | 用于 |
| --- | --- |
| `feat` | 新功能 |
| `fix` | 缺陷修复 |
| `refactor` | 重构，既不修 bug 也不加功能 |
| `test` | 新增或修改测试 |
| `docs` | 仅文档变更 |
| `chore` | 工具链、依赖、配置、构建脚本 |
| `perf` | 性能优化 |
| `revert` | 回滚此前的提交 |

### 原子性

**一次提交只做一件事。**

```
✅  feat: add student violation endpoint
    fix: handle empty file in upload controller
    refactor: extract pagination params into QueryParam objects
    docs: document violation endpoint in api reference

❌  feat: add violation feature and fix upload and refactor pagination
```

把「格式化改动」和「行为改动」分开提交；把「重构」和「新功能」分开提交。
这样每一笔提交都能被独立理解和回滚。

### 提交前检查

```bash
git diff --staged                              # 1. 确认改动内容
git diff --staged | grep -iE "password|secret|token|api_key"   # 2. 确认无密钥泄漏
cd backend && mvn clean package -DskipTests   # 3. 后端构建通过
cd frontend && npm run build                  # 4. 前端构建通过
cd frontend && npm run lint                   # 5. 前端规范检查
```

仓库已配置 pre-commit hook（见 `.githooks/pre-commit`），前两步会自动执行。

---

## 拉取请求（PR）

- 一个 PR 对应一个逻辑变更，不要把无关改动打包在一起。
- PR 描述需说明：**改了什么**、**为什么改**、**如何验证**、**影响范围**。
- PR 标题沿用提交规范（`<type>: <描述>`）。
- 合并前需通过：后端构建、前端构建与 lint。

模板见 `.github/pull_request_template.md`。

---

## 版本与发布

版本号遵循语义化版本：

| 变更类型 | 版本位 | 示例 |
| --- | --- | --- |
| 破坏性变更（消费者必须改代码） | MAJOR | `1.0.0` → `2.0.0` |
| 新增功能，向后兼容 | MINOR | `1.0.0` → `1.1.0` |
| 缺陷修复，向后兼容 | PATCH | `1.0.0` → `1.0.1` |

**版本号由 tag 决定**，不手工在多处编辑：

```bash
git tag -a v0.1.0 -m "Release 0.1.0"
git push origin v0.1.0
```

发布时同步更新 `CHANGELOG.md`，按用户影响分组（Added / Changed / Fixed /
Deprecated / Removed / Security），而不是简单罗列 commit message。

---

## 提交改动前的自查清单

- [ ] 提交只做一件逻辑上的事
- [ ] 消息说明了「为什么」，且符合类型约定
- [ ] 构建与测试通过
- [ ] diff 中不含密钥、口令、`.env`
- [ ] 未混入纯格式化改动
- [ ] 新增文件已被 `.gitignore` 正确覆盖（构建产物、日志）

---

## 常用命令速查

```bash
# 从 main 切出特性分支
git checkout main && git pull
git checkout -b feature/xxx

# 交互式暂存：把改动拆成多笔提交
git add -p

# 查看某个文件的历史 / 某行代码的来源
git log --oneline -- <file>
git blame <file>

# 二分查找引入缺陷的提交
git bisect start
git bisect bad HEAD
git bisect good <已知正常的提交>

# 回退（保留改动到工作区 / 彻底丢弃）
git reset --soft HEAD~1
git reset --hard HEAD~1
```
