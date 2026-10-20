#!/bin/sh
#
# 安装 git hooks
#
# 用法（在仓库根目录执行）：
#   sh .githooks/install-hooks.sh
#
# 作用：
#   将 .githooks/ 下的守卫脚本安装为仓库的 pre-commit hook，
#   使提交前自动进行凭据扫描与构建产物检查。
#
# 说明：
#   .githooks/ 目录本身会随仓库分发，供其他克隆者一键安装；
#   .git/hooks/ 属于本地环境，不纳入版本控制。

set -e

REPO_ROOT=$(cd "$(dirname "$0")/.." && pwd)
HOOKS_DIR="$REPO_ROOT/.git/hooks"
GUARD="$REPO_ROOT/.githooks/guard-credentials.sh"

if [ ! -f "$GUARD" ]; then
    echo "[X] 找不到守卫脚本: $GUARD"
    exit 1
fi

mkdir -p "$HOOKS_DIR"

cat > "$HOOKS_DIR/pre-commit" <<EOF
#!/bin/sh
# 由 .githooks/install-hooks.sh 生成，请勿手工修改。
exec sh "$GUARD"
EOF

chmod +x "$HOOKS_DIR/pre-commit"

echo "[OK] pre-commit hook 已安装到 .git/hooks/pre-commit"
echo "     提交前将自动执行凭据扫描与构建产物检查"
