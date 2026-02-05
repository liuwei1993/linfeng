# 林风婚恋交友系统 - 部署指南

## 快速导航

- [系统要求](#系统要求)
- [本地开发环境](#本地开发环境)
- [Docker 部署](#docker-部署)
- [生产环境部署](#生产环境部署)
- [常见问题](#常见问题)
- [性能优化](#性能优化)

---

## 系统要求

### 开发环境
- **操作系统**: Linux、macOS、Windows (WSL2)
- **Java**: 8+ (推荐 OpenJDK 8 或 Oracle JDK)
- **Maven**: 3.6.0+
- **Node.js**: 14.0.0+
- **npm/yarn**: 6.0.0+ / 1.22.0+
- **MySQL**: 8.0.0+
- **Redis**: 5.0.0+ (可选，开发可不用)
- **RabbitMQ**: 3.8.0+ (可选，开发可不用)

### 生产环境
- **操作系统**: Linux (CentOS 7+, Ubuntu 18.04+, Debian 10+)
- **Java**: OpenJDK 8/11 LTS
- **MySQL**: 8.0+ (主从或集群)
- **Redis**: 6.0+ (集群模式)
- **RabbitMQ**: 3.9+ (集群模式)
- **Nginx**: 1.18+
- **Docker** (可选): 20.10+
- **Docker Compose** (可选): 1.29+

### 硬件要求
```
最小配置:
  - CPU: 2 核
  - 内存: 4 GB
  - 硬盘: 50 GB SSD

推荐配置:
  - CPU: 8 核以上
  - 内存: 16 GB
  - 硬盘: 200 GB SSD

高可用配置:
  - 负载均衡器: Nginx/HAProxy
  - 应用服务器: 3+ 实例
  - 数据库: 主从或集群
  - 缓存: Redis 集群
  - 消息队列: RabbitMQ 集群
```

---

## 本地开发环境

### 1. 环境预检查与配置

```bash
# 进入项目目录
cd /home/liuwei/codes/linfeng-love

# 执行环境配置脚本 (推荐)
chmod +x setup-environment.sh
./setup-environment.sh dev

# 或手动检查依赖
java -version
mvn -v
node -v
npm -v
mysql --version
```

### 2. 启动必要的服务

#### MySQL 本地启动
```bash
# macOS
brew services start mysql-server

# Ubuntu/Debian
sudo systemctl start mysql

# 或使用 Docker
docker run -d \
  -p 3306:3306 \
  -e MYSQL_ROOT_PASSWORD=root \
  -e MYSQL_DATABASE=linfeng-love-open \
  --name linfeng-mysql \
  mysql:8.0
```

#### Redis 本地启动
```bash
# macOS
brew services start redis

# Ubuntu/Debian
sudo systemctl start redis-server

# 或使用 Docker
docker run -d \
  -p 6379:6379 \
  --name linfeng-redis \
  redis:7-alpine redis-server --appendonly yes
```

#### RabbitMQ 本地启动
```bash
# macOS
brew services start rabbitmq

# Ubuntu/Debian
sudo systemctl start rabbitmq-server

# 或使用 Docker
docker run -d \
  -p 5672:5672 \
  -p 15672:15672 \
  -e RABBITMQ_DEFAULT_USER=guest \
  -e RABBITMQ_DEFAULT_PASS=guest \
  --name linfeng-rabbitmq \
  rabbitmq:3.12-management-alpine
```

### 3. 初始化数据库

```bash
# 连接到 MySQL
mysql -h localhost -u root -proot

# 创建数据库
CREATE DATABASE linfeng-love-open CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

# 导入 SQL 脚本 (从 QQ 群获取)
mysql -h localhost -u root -proot linfeng-love-open < linfeng-love.sql
```

### 4. 启动后端应用

```bash
# 方法 1: 使用 Maven 直接运行
cd linfeng-love-backend-open
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=dev"

# 方法 2: 编译后运行
mvn clean package -DskipTests
java -jar target/linfeng-love.jar --spring.profiles.active=dev

# 方法 3: 使用 IDE
# 在 IntelliJ IDEA 或 Eclipse 中打开项目，直接运行 Application.java
```

### 5. 启动前端应用

```bash
# 安装依赖
cd linfeng-love-uniapps-open
npm install

# 开发模式运行
npm run dev

# 编译为 H5
npm run build:web

# 编译为小程序
npm run build:mp-weixin

# 编译为 APP (需要 HBuilderX)
npm run build:app
```

### 6. 访问应用

```
后端 API:        http://localhost:8080
Swagger 文档:    http://localhost:8080/swagger-ui.html
Druid 监控:      http://localhost:8080/druid/
MySQL:          localhost:3306
Redis:          localhost:6379
RabbitMQ:       localhost:15672 (用户名/密码: guest/guest)
```

---

## Docker 部署

### 单独容器部署

#### 1. 编译后端为 JAR

```bash
cd linfeng-love-backend-open
mvn clean package -DskipTests -Pprod
```

#### 2. 构建 Docker 镜像

```bash
# 方法 1: 使用提供的 Dockerfile
docker build -t linfeng-love:latest .

# 方法 2: 使用部署脚本
./deploy.sh docker:build
```

#### 3. 运行容器

```bash
docker run -d \
  -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=prod \
  -e SPRING_DATASOURCE_DRUID_URL=jdbc:mysql://host.docker.internal:3306/linfeng-love-open \
  -e SPRING_DATASOURCE_DRUID_USERNAME=root \
  -e SPRING_DATASOURCE_DRUID_PASSWORD=root \
  -e SPRING_REDIS_HOST=host.docker.internal \
  -e SPRING_RABBITMQ_HOST=host.docker.internal \
  -v /opt/linfeng-love/logs:/app/logs \
  --name linfeng-love \
  linfeng-love:latest
```

### Docker Compose 全栈部署 (推荐)

#### 1. 启动完整栈

```bash
# 启动所有服务
docker-compose up -d

# 查看日志
docker-compose logs -f app

# 检查服务状态
docker-compose ps
```

#### 2. 初始化数据库

```bash
# 等待 MySQL 启动完成 (约 30 秒)
sleep 30

# 导入数据库脚本
docker exec linfeng-love-mysql mysql -uroot -proot linfeng-love-open < linfeng-love.sql

# 或手动连接导入
docker-compose exec mysql mysql -uroot -proot linfeng-love-open < linfeng-love.sql
```

#### 3. 访问应用

```
应用:           http://localhost:8080
Swagger:        http://localhost:8080/swagger-ui.html
Adminer (DB):   http://localhost:8081
RabbitMQ:       http://localhost:15672 (guest/guest)
MinIO:          http://localhost:9001 (minioadmin/minioadmin)
```

#### 4. 停止应用

```bash
# 停止所有服务
docker-compose stop

# 删除所有容器
docker-compose down

# 删除数据卷（谨慎！）
docker-compose down -v
```

---

## 生产环境部署

### 架构设计

```
用户 → Nginx (负载均衡 + SSL) → Java App x3 (集群) → MySQL (主从) + Redis (集群) + RabbitMQ (集群)
```

### 1. 服务器准备

#### 系统初始化
```bash
# 更新系统
sudo apt update && sudo apt upgrade -y

# 安装基础工具
sudo apt install -y curl wget git htop vim

# 关闭不必要的服务
sudo systemctl disable avahi-daemon
sudo systemctl stop avahi-daemon
```

#### 安装 Java
```bash
# 安装 OpenJDK 8
sudo apt install -y openjdk-8-jdk

# 验证
java -version

# 设置 JAVA_HOME
echo 'export JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64' >> ~/.bashrc
source ~/.bashrc
```

#### 安装 MySQL (主服务器)
```bash
# 安装 MySQL 8.0
sudo apt install -y mysql-server

# 启用和启动
sudo systemctl enable mysql
sudo systemctl start mysql

# 初始化
sudo mysql_secure_installation

# 创建数据库和用户
sudo mysql -e "CREATE DATABASE linfeng-love-open CHARACTER SET utf8mb4;"
sudo mysql -e "CREATE USER 'linfeng'@'%' IDENTIFIED BY 'your_password';"
sudo mysql -e "GRANT ALL PRIVILEGES ON linfeng-love-open.* TO 'linfeng'@'%';"
sudo mysql -e "FLUSH PRIVILEGES;"
```

#### 安装 Redis
```bash
# 安装 Redis
sudo apt install -y redis-server

# 启用和启动
sudo systemctl enable redis-server
sudo systemctl start redis-server

# 配置持久化 (编辑 /etc/redis/redis.conf)
sudo sed -i 's/# appendonly no/appendonly yes/' /etc/redis/redis.conf
sudo systemctl restart redis-server
```

#### 安装 RabbitMQ
```bash
# 安装 Erlang 和 RabbitMQ
sudo apt install -y erlang
sudo apt install -y rabbitmq-server

# 启用和启动
sudo systemctl enable rabbitmq-server
sudo systemctl start rabbitmq-server

# 启用管理插件
sudo rabbitmq-plugins enable rabbitmq_management

# 创建用户 (如需要)
sudo rabbitmqctl add_user linfeng linfeng_password
sudo rabbitmqctl set_permissions -p / linfeng ".*" ".*" ".*"
```

### 2. 应用部署

#### 使用部署脚本
```bash
# 部署应用
./deploy.sh deploy prod

# 启动应用
./deploy.sh start

# 查看状态
./deploy.sh status

# 查看日志
./deploy.sh logs
```

#### 手动部署
```bash
# 编译应用
mvn clean package -DskipTests -Pprod -f linfeng-love-backend-open/pom.xml

# 创建应用目录
sudo mkdir -p /opt/linfeng-love
sudo chown -R $USER:$USER /opt/linfeng-love

# 复制 JAR 文件
cp linfeng-love-backend-open/target/linfeng-love.jar /opt/linfeng-love/

# 创建启动脚本
cat > /opt/linfeng-love/start.sh << 'EOF'
#!/bin/bash
cd /opt/linfeng-love
java -Xms1g -Xmx4g -XX:+UseG1GC -XX:MaxGCPauseMillis=200 \
  -Dlogging.file.name=logs/linfeng-love.log \
  -jar linfeng-love.jar --spring.profiles.active=prod &
EOF

chmod +x /opt/linfeng-love/start.sh

# 启动应用
/opt/linfeng-love/start.sh
```

### 3. Nginx 配置

#### 安装 Nginx
```bash
sudo apt install -y nginx
sudo systemctl enable nginx
```

#### 配置反向代理
```bash
# 编辑 Nginx 配置
sudo cp nginx.conf /etc/nginx/nginx.conf

# 如果使用 SSL 证书 (Let's Encrypt)
sudo apt install -y certbot python3-certbot-nginx
sudo certbot certonly --standalone -d api.yourdomain.com

# 测试配置
sudo nginx -t

# 启动 Nginx
sudo systemctl restart nginx
```

### 4. 监控和日志

#### 安装监控工具
```bash
# 安装 Prometheus 和 Grafana (可选)
docker run -d -p 9090:9090 prom/prometheus
docker run -d -p 3000:3000 grafana/grafana

# 安装日志收集 (ELK Stack)
docker run -d -p 9200:9200 docker.elastic.co/elasticsearch/elasticsearch:7.15.0
docker run -d -p 5601:5601 docker.elastic.co/kibana/kibana:7.15.0
```

#### 检查日志
```bash
# 应用日志
tail -f /opt/linfeng-love/logs/linfeng-love.log

# Nginx 日志
tail -f /var/log/nginx/access.log
tail -f /var/log/nginx/error.log

# MySQL 日志
tail -f /var/log/mysql/error.log
```

### 5. Systemd 服务 (可选)

#### 创建服务单元
```bash
./deploy.sh systemd:create

# 或手动创建
sudo tee /etc/systemd/system/linfeng-love.service > /dev/null << EOF
[Unit]
Description=Linfeng Love Application
After=network.target

[Service]
Type=simple
User=$USER
WorkingDirectory=/opt/linfeng-love
ExecStart=/usr/bin/java -Xms1g -Xmx4g -XX:+UseG1GC -jar /opt/linfeng-love/linfeng-love.jar
Restart=on-failure
RestartSec=10s

[Install]
WantedBy=multi-user.target
EOF

# 启用和启动
sudo systemctl daemon-reload
sudo systemctl enable linfeng-love
sudo systemctl start linfeng-love

# 查看状态
sudo systemctl status linfeng-love
```

### 6. 备份和恢复

#### 自动备份脚本
```bash
#!/bin/bash
# 每天备份数据库

BACKUP_DIR="/opt/backups/linfeng-love"
mkdir -p $BACKUP_DIR

# 备份 MySQL
mysqldump -u linfeng -p'password' linfeng-love-open | \
  gzip > $BACKUP_DIR/linfeng-love-$(date +%Y%m%d-%H%M%S).sql.gz

# 保留最近 7 天的备份
find $BACKUP_DIR -name "*.sql.gz" -mtime +7 -delete

# 定时执行 (crontab -e)
# 0 2 * * * /path/to/backup.sh
```

---

## 常见问题

### Q: 启动时 "Connection refused"
```
A: 检查 MySQL、Redis、RabbitMQ 是否启动
   检查 application-dev.yml 中的连接信息是否正确
   检查防火墙规则
```

### Q: 数据库导入失败
```
A: 确保 MySQL 版本 8.0+
   检查字符集设置: SET NAMES utf8mb4;
   分段导入大文件
   检查 SQL 文件格式
```

### Q: Docker 容器无法连接到宿主机 MySQL
```
A: 使用 host.docker.internal 替代 localhost (macOS/Windows)
   使用 172.17.0.1 替代 localhost (Linux)
   或在 docker-compose.yml 配置网络
```

### Q: 内存占用过高
```
A: 调整 JVM 参数: -Xms -Xmx
   配置 G1GC: -XX:+UseG1GC -XX:MaxGCPauseMillis=200
   检查是否有内存泄漏
```

### Q: 应用启动缓慢
```
A: 检查网络连接速度
   查看启动日志中的瓶颈
   增加数据库连接池大小
```

---

## 性能优化

### JVM 参数优化
```bash
# 生产环境推荐
java -Xms2g -Xmx4g \
  -XX:+UseG1GC \
  -XX:MaxGCPauseMillis=200 \
  -XX:+PrintGCDateStamps \
  -XX:+PrintGCDetails \
  -Xloggc:logs/gc.log \
  -jar linfeng-love.jar
```

### MySQL 优化
```sql
-- 调整缓冲池大小
SET GLOBAL innodb_buffer_pool_size = 2G;

-- 启用查询缓存 (MySQL 5.7)
SET GLOBAL query_cache_type = 1;
SET GLOBAL query_cache_size = 256M;

-- 增加最大连接数
SET GLOBAL max_connections = 1000;

-- 增加打开文件限制
SET GLOBAL open_files_limit = 65535;
```

### Redis 优化
```bash
# redis.conf 配置
maxmemory 2gb
maxmemory-policy allkeys-lru
appendonly yes
appendfsync everysec
```

### Nginx 优化
```nginx
# worker_processes 设置为 CPU 核数
worker_processes auto;

# 调整连接数
events {
    worker_connections 4096;
}

# 启用缓冲
proxy_buffering on;
proxy_buffer_size 8k;
proxy_buffers 8 8k;
```

---

## 监控命令速查

```bash
# 检查应用状态
./deploy.sh status

# 查看实时日志
./deploy.sh logs
tail -f /opt/linfeng-love/logs/linfeng-love.log

# 检查端口占用
lsof -i :8080
netstat -tulpn | grep 8080

# 检查资源使用
top
free -h
df -h

# 检查数据库
mysql -h localhost -u linfeng -p linfeng-love-open -e "SHOW VARIABLES LIKE '%max_connections%';"

# 检查 Redis
redis-cli ping
redis-cli INFO memory

# 检查 RabbitMQ
rabbitmqctl status
```

---

## 联系和支持

- **项目 GitHub**: https://gitee.com/yuncoder001/linfeng-love
- **QQ 交流群**: 624039130
- **商业支持**: 微信号 18157059657
- **官网**: https://net.linfeng.tech/love.html

---

**文档版本**: 1.0  
**更新日期**: 2026-02-05  
**维护者**: linfeng-tech
