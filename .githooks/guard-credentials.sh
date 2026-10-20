#!/bin/sh
#
# 凭据与构建产物守卫
#
# 由 .githooks/pre-commit 调用，也可单独执行。
#
# 规则：
#   1. 暂存区中若出现真实的硬编码凭据（口令 / 密钥 / api key），
#      拒绝提交。
#   2. 安全的占位符写法一律放行，例如：
#        password: ${DB_PASSWORD:your_password_here}
#        secret:   ${JWT_SECRET:change_me_to_a_random_string}
#        password: your_password_here
#        password: <your-password>
#   3. 构建产物、依赖目录、日志、.env 等不应入库的文件，一律拒绝提交。
#
# 如需跳过：git commit --no-verify（不推荐）

set -e

STAGED=$(git diff --staged --name-only --diff-filter=ACMR)

if [ -z "$STAGED" ]; then
    exit 0
fi

RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m'

fail() {
    echo "${RED}[X] 提交被拒绝：$1${NC}"
    exit 1
}

# ---------------------------------------------------------------------
# 1. 凭据扫描
# ---------------------------------------------------------------------
# 匹配形如 key = value 的赋值。命中后再用占位词表排除明显的示例写法，
# 使开源项目的占位符配置可以正常提交，而真实凭据会被拦下。
SECRET_PATTERN='(password|passwd|secret|api[_-]?key|access[_-]?key([_-]?id)?|private[_-]?key|secret[_-]?key)[[:space:]]*[:=][[:space:]]*[^[:space:]]{8,}'

PLACEHOLDER_PATTERN='\$\{[^}]*\}|\$[A-Z_][A-Z0-9_]*|<[^>]*>|(^|[^A-Za-z0-9])(your|my|example|sample|placeholder|dummy|fake|test|default|change_me|replace_me|changeme|todo)([^A-Za-z0-9]|$)|x{3,}|\*{3,}'

HITS=$(git diff --staged -U0 \
    | grep -E "^\+" \
    | grep -vE "^\+\+\+" \
    | grep -iE "$SECRET_PATTERN" \
    | grep -ivE "$PLACEHOLDER_PATTERN" || true)

if [ -n "$HITS" ]; then
    echo "${YELLOW}检测到疑似硬编码的真实凭据：${NC}"
    echo "$HITS" | head -10
    echo ""
    echo "提示：开源项目请使用占位符写法，例如"
    echo "    password: \${DB_PASSWORD:your_password_here}"
    echo "    secret:   \${JWT_SECRET:change_me_to_a_random_string}"
    echo ""
    fail "请改用环境变量注入或占位符形式"
fi

# ---------------------------------------------------------------------
# 2. 构建产物检查
# ---------------------------------------------------------------------
FORBIDDEN='^(backend/target/|node_modules/|frontend/dist/|\.env$|.*\.log$|.*\.pid$)'

VIOLATIONS=$(echo "$STAGED" | grep -E "$FORBIDDEN" || true)

if [ -n "$VIOLATIONS" ]; then
    echo "${YELLOW}检测到不应提交的文件：${NC}"
    echo "$VIOLATIONS" | head -10
    echo ""
    fail "请确认 .gitignore 规则是否生效"
fi

echo "${GREEN}[OK]${NC} 凭据扫描 / 构建产物检查通过"
exit 0
