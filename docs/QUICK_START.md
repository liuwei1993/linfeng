# 林风婚恋交友系统 - 快速开始指南

## 5 分钟快速启动

### 前置要求

```bash
# 检查系统环境
java -version        # Java 8+
mvn -version         # Maven 3.6+
node -version        # Node.js 14+
npm -version         # npm 6+
mysql --version      # MySQL 8.0+
docker --version     # Docker 20.10+ (可选)
```

---

## 方案 A: 最快启动 (Docker)

### 仅需 3 个命令

```bash
# 1. 进入项目目录
cd /home/liuwei/codes/linfeng-love

# 2. 一键启动全栈应用
docker-compose up -d

# 3. 等待 30 秒，然后访问
# 后端 API: http://localhost:8080
# Swagger: http://localhost:8080/swagger-ui.html
# RabbitMQ: http://localhost:15672 (guest/guest)
# Adminer: http://localhost:8081
```

### 导入数据库

```bash
# 从 QQ 群获取 linfeng-love.sql，然后执行
docker exec -i linfeng-love-mysql mysql -uroot -proot linfeng-love-open < linfeng-love.sql
```

✅ **完成！应用已可访问**

---

## 方案 B: 本地开发

### Step 1: 环境配置 (2 分钟)

```bash
cd /home/liuwei/codes/linfeng-love

# 自动配置环境
chmod +x setup-environment.sh
./setup-environment.sh dev

# 或手动启动服务
# MySQL
docker run -d -p 3306:3306 -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=linfeng-love-open --name mysql mysql:8.0

# Redis
docker run -d -p 6379:6379 --name redis redis:7-alpine

# RabbitMQ
docker run -d -p 5672:5672 -p 15672:15672 --name rabbitmq rabbitmq:3.12-management-alpine
```

### Step 2: 初始化数据库 (1 分钟)

```bash
# 从 QQ 群获取 SQL 脚本
mysql -h localhost -u root -proot linfeng-love-open < linfeng-love.sql
```

### Step 3: 启动后端 (1 分钟)

```bash
cd linfeng-love-backend-open

# 方式 1: 直接运行 (推荐开发)
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=dev"

# 方式 2: 编译后运行
mvn clean package -DskipTests
java -jar target/linfeng-love.jar --spring.profiles.active=dev
```

### Step 4: 启动前端 (1 分钟)

```bash
cd linfeng-love-uniapps-open

npm install
npm run dev
```

✅ **应用启动完成！**

```
后端 API:   http://localhost:8080
文档:      http://localhost:8080/swagger-ui.html
RabbitMQ:  http://localhost:15672
```

---

## 常见快速操作

### 查看日志

```bash
# Docker 方式
docker logs -f linfeng-love-app

# 本地方式
tail -f linfeng-love-backend-open/logs/linfeng-love.log
```

### 重启应用

```bash
# Docker 方式
docker-compose restart app

# 脚本方式
./deploy.sh restart
```

### 停止应用

```bash
# Docker 方式
docker-compose stop

# 脚本方式
./deploy.sh stop
```

### 查看应用状态

```bash
./deploy.sh status
```

---

## 测试 API

### 使用 Swagger 文档

打开浏览器: http://localhost:8080/swagger-ui.html

### 使用 curl 测试

```bash
# 获取嘉宾列表
curl -X GET "http://localhost:8080/api/guest/list" \
  -H "Content-Type: application/json"

# 发送动态
curl -X POST "http://localhost:8080/api/dynamic/add" \
  -H "Content-Type: application/json" \
  -d '{
    "content": "测试动态",
    "images": [],
    "tags": ""
  }'
```

### 使用 Postman

1. 导入 Swagger URL: http://localhost:8080/v2/api-docs
2. 选择要测试的 API
3. 点击 "Send"

---

## 配置说明

### 后端配置文件位置

```
linfeng-love-backend-open/src/main/resources/
├── application.yml              # 通用配置
├── application-dev.yml          # 开发环境
├── application-prod.yml         # 生产环境
├── application-test.yml         # 测试环境
└── application-local.yml        # 本地配置 (自动生成)
```

### 关键配置

```yaml
# 数据库
spring.datasource.druid.url: jdbc:mysql://localhost:3306/linfeng-love-open
spring.datasource.druid.username: root
spring.datasource.druid.password: root

# Redis
spring.redis.host: localhost
spring.redis.port: 6379

# RabbitMQ
spring.rabbitmq.host: localhost
spring.rabbitmq.port: 5672
spring.rabbitmq.username: guest
spring.rabbitmq.password: guest

# JWT (APP 使用)
linfeng.jwt.secret: 12345678
linfeng.jwt.expire: 604800  # 7 天

# 微信小程序
wx.ma.appId: your_appid
wx.ma.appSecret: your_appsecret

# 七牛云存储
qiniu.accessKey: your_key
qiniu.secretKey: your_secret
qiniu.bucketName: your_bucket
```

---

## 前端配置

### 环境变量

创建 `.env` 文件 (基于 `.env.example`):

```bash
# API 地址
VUE_APP_BASE_URL=http://localhost:8080
VUE_APP_API_TIMEOUT=30000

# 微信配置
VUE_APP_WX_APPID=your_appid

# 环境
VUE_APP_ENV=development
```

---

## 数据库管理

### 查看数据库

**通过 Adminer (Web)**: http://localhost:8081
- 服务器: mysql
- 用户名: root
- 密码: root

**通过命令行**:
```bash
mysql -h localhost -u root -proot linfeng-love-open
```

### 常用 SQL

```sql
-- 查看所有用户
SELECT * FROM t_user LIMIT 10;

-- 查看动态
SELECT * FROM t_dynamic LIMIT 10;

-- 查看聊天消息
SELECT * FROM t_chat_message LIMIT 10;

-- 查看订单
SELECT * FROM t_order LIMIT 10;
```

---

## 部署到生产环境

### 使用部署脚本

```bash
# 编译并部署
./deploy.sh deploy prod

# 启动应用
./deploy.sh start

# 查看状态
./deploy.sh status

# 查看日志
./deploy.sh logs
```

### 或使用 Docker Compose

```bash
# 修改环境变量
vim docker-compose.yml

# 启动应用
docker-compose up -d
```

---

## 遇到问题?

### 常见问题

| 问题 | 解决方案 |
|------|--------|
| 连接 MySQL 失败 | 检查 MySQL 是否启动，用户名密码是否正确 |
| Redis 连接失败 | 检查 Redis 是否启动，端口 6379 是否开放 |
| RabbitMQ 连接失败 | 检查 RabbitMQ 是否启动，端口 5672 是否开放 |
| 端口被占用 | 修改 application.yml 中的 server.port |
| 数据库没有表 | 检查是否导入了 SQL 脚本 |
| API 返回 401 | 检查是否登录，JWT token 是否有效 |

### 获取帮助

- 查看 **TECHNICAL_ARCHITECTURE.md** - 系统架构详解
- 查看 **DEPLOYMENT_GUIDE.md** - 详细部署指南
- 查看 **PROJECT_COMPLETENESS_REPORT.md** - 项目评估报告
- 访问 **Swagger API 文档** - http://localhost:8080/swagger-ui.html
- 加入 **QQ 交流群**: 624039130

---

## 下一步

### 新手推荐

1. ✅ 通过 Docker 启动应用
2. ✅ 访问 Swagger 文档，理解 API
3. ✅ 修改前端配置，运行前端
4. ✅ 在 Swagger 中测试 API
5. ✅ 查看源代码，理解实现逻辑

### 开发者推荐

1. ✅ 本地启动完整环境
2. ✅ 在 IDE 中调试后端代码
3. ✅ 使用 npm run dev 开发前端
4. ✅ 根据需求修改代码
5. ✅ 提交 Pull Request 贡献代码

### 部署者推荐

1. ✅ 阅读 DEPLOYMENT_GUIDE.md
2. ✅ 准备生产环境服务器
3. ✅ 使用 deploy.sh 脚本部署
4. ✅ 配置 Nginx 反向代理
5. ✅ 设置监控和告警

---

## 快速链接

- 📖 [技术架构文档](./TECHNICAL_ARCHITECTURE.md)
- 🚀 [部署指南](./DEPLOYMENT_GUIDE.md)
- 📊 [项目评估报告](./PROJECT_COMPLETENESS_REPORT.md)
- 🔧 [环境配置脚本](./setup-environment.sh)
- 📦 [部署脚本](./deploy.sh)
- 🐳 [Docker Compose 配置](./docker-compose.yml)
- 🌐 [Nginx 配置](./nginx.conf)

---

## 反馈和支持

- **QQ 交流群**: 624039130
- **商业咨询**: 微信 18157059657
- **项目主页**: https://gitee.com/yuncoder001/linfeng-love
- **官网**: https://net.linfeng.tech/love.html

---

**快乐开发！🎉**

如有问题，欢迎加入 QQ 群咨询。
