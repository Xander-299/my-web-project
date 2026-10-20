# nginx 部署配置

本目录保存生产环境的前端静态资源托管与 API 反向代理配置。

## 目录说明

```
deploy/nginx/
├── conf/          # nginx 配置文件（已按本项目定制）
├── html/          # 前端构建产物投放目录（git 忽略，需自行构建后拷贝）
├── logs/          # 运行时日志（git 忽略）
├── nginx.exe      # Windows 版 nginx 可执行文件（git 忽略，需自行下载）
└── LICENSE        # nginx BSD 许可证
```

`nginx.exe` 与 `html/` 下的产物不属于源码，未纳入版本管理。首次使用请按下述步骤准备。

## 部署步骤

### 1. 准备 nginx 可执行文件

从 [nginx.org](https://nginx.org/en/download.html) 下载 Windows 版压缩包，将
`nginx.exe` 及 `conf/` 下的基础配置文件复制到本目录（当前 `conf/` 已包含
项目定制后的 `nginx.conf`，覆盖时注意合并）。

### 2. 构建前端

```bash
cd frontend
npm install
npm run build
```

### 3. 投放前端产物

将 `frontend/dist/` 下的全部内容复制到 `deploy/nginx/html/`：

```bash
# Windows PowerShell
Copy-Item -Path frontend\dist\* -Destination deploy\nginx\html\ -Recurse -Force
```

### 4. 启动

```bash
cd deploy/nginx
./nginx.exe          # Windows
./nginx              # Linux / macOS
```

访问 <http://localhost:90>。

停止服务：`nginx.exe -s stop`。

## 反向代理规则

`conf/nginx.conf` 中已配置：

```nginx
location ^~ /api/ {
    rewrite ^/api/(.*)$ /$1 break;
    proxy_pass http://localhost:8080;
}
```

即前端统一请求 `/api/**`，由 nginx 去掉 `/api` 前缀后转发到后端
`http://localhost:8080`——与开发环境下 Vite 代理（`frontend/vite.config.js`）
行为保持一致。

前端访问端口为 **90**，后端 API 端口为 **8080**。
