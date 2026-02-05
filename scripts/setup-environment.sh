#!/bin/bash

##############################################################################
#  林风婚恋交友系统 - 环境配置脚本
#  版本: 1.0
#  用途: 自动配置开发、测试、生产环境
#  使用: chmod +x setup-environment.sh && ./setup-environment.sh [dev|test|prod]
##############################################################################

set -e  # 遇到错误立即退出

# 颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# 脚本路径
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$SCRIPT_DIR"
BACKEND_DIR="$PROJECT_ROOT/linfeng-love-backend-open"
FRONTEND_DIR="$PROJECT_ROOT/linfeng-love-uniapps-open"

##############################################################################
# 辅助函数
##############################################################################

print_header() {
    echo ""
    echo -e "${BLUE}==================================================================${NC}"
    echo -e "${BLUE}$1${NC}"
    echo -e "${BLUE}==================================================================${NC}"
    echo ""
}

print_success() {
    echo -e "${GREEN}✓ $1${NC}"
}

print_error() {
    echo -e "${RED}✗ $1${NC}"
}

print_warning() {
    echo -e "${YELLOW}⚠ $1${NC}"
}

print_info() {
    echo -e "${BLUE}ℹ $1${NC}"
}

check_command() {
    if ! command -v $1 &> /dev/null; then
        print_error "$1 未安装，请先安装 $1"
        return 1
    fi
    print_success "$1 已安装: $(which $1)"
    return 0
}

##############################################################################
# 检查系统环境
##############################################################################

check_system_requirements() {
    print_header "检查系统环境依赖"
    
    local all_ok=true
    
    # 检查 Java
    if check_command java; then
        local java_version=$(java -version 2>&1 | head -n 1)
        print_info "Java 版本: $java_version"
    else
        all_ok=false
    fi
    
    # 检查 Maven
    if check_command mvn; then
        local mvn_version=$(mvn -v | head -n 1)
        print_info "Maven 版本: $mvn_version"
    else
        print_warning "Maven 未安装 - 后端编译可能失败"
        all_ok=false
    fi
    
    # 检查 Git
    if check_command git; then
        local git_version=$(git --version)
        print_info "Git 版本: $git_version"
    else
        print_warning "Git 未安装"
    fi
    
    # 检查 Node.js
    if check_command node; then
        local node_version=$(node -v)
        print_info "Node.js 版本: $node_version"
    else
        print_warning "Node.js 未安装 - 前端编译可能失败"
    fi
    
    # 检查 npm/yarn
    if command -v npm &> /dev/null; then
        print_success "npm 已安装: $(npm -v)"
    elif command -v yarn &> /dev/null; then
        print_success "yarn 已安装: $(yarn -v)"
    else
        print_warning "npm/yarn 未安装"
    fi
    
    echo ""
    if [ "$all_ok" = false ]; then
        print_warning "部分依赖未安装，建议补充安装"
    fi
}

##############################################################################
# 配置后端环境
##############################################################################

setup_backend_dev() {
    print_header "配置后端开发环境"
    
    if [ ! -d "$BACKEND_DIR" ]; then
        print_error "后端目录不存在: $BACKEND_DIR"
        return 1
    fi
    
    print_info "后端项目路径: $BACKEND_DIR"
    
    # 生成配置文件如果不存在
    if [ ! -f "$BACKEND_DIR/src/main/resources/application-local.yml" ]; then
        print_info "创建本地开发配置文件..."
        cat > "$BACKEND_DIR/src/main/resources/application-local.yml" << 'EOF'
# 林风婚恋交友 - 本地开发配置
# 此文件可以覆盖其他配置文件中的设置，用于本地开发

spring:
    datasource:
        type: com.alibaba.druid.pool.DruidDataSource
        druid:
            driver-class-name: com.mysql.cj.jdbc.Driver
            url: jdbc:mysql://localhost:3306/linfeng-love-open?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai&useSSL=false
            username: root
            password: root
            initial-size: 5
            max-active: 20
            min-idle: 5
    redis:
        open: true
        database: 0
        host: localhost
        port: 6379
        password: 
        timeout: 6000ms
        jedis:
            pool:
                max-active: 100
                max-wait: -1ms
                max-idle: 10
                min-idle: 5
    rabbitmq:
        host: localhost
        port: 5672
        username: guest
        password: guest
        virtual-host: /

# 七牛云配置（可选）
qiniu:
  accessKey: your_access_key
  secretKey: your_secret_key
  bucketName: your_bucket_name
  domain: your_domain
  directory: uploads
  max-size: 30

# 微信小程序配置
wx:
  ma:
    appId: your_app_id
    appSecret: your_app_secret

# JWT配置
linfeng:
  jwt:
    secret: dev-secret-key-12345678
    expire: 604800

# 日志级别
logging:
  level:
    io.linfeng: DEBUG
    org.springframework.web: INFO
    org.springframework.security: INFO
    io.swagger.*: ERROR
  file:
    name: logs/linfeng-love.log
    max-size: 10MB
EOF
        print_success "本地开发配置文件已创建"
    else
        print_info "本地开发配置文件已存在"
    fi
    
    # 检查 pom.xml
    if [ -f "$BACKEND_DIR/pom.xml" ]; then
        print_success "pom.xml 文件存在"
    else
        print_error "pom.xml 不存在"
        return 1
    fi
    
    print_success "后端环境配置完成"
    echo ""
}

setup_backend_test() {
    print_header "配置后端测试环境"
    
    print_info "使用 application-test.yml 配置"
    
    # 检查测试配置文件是否存在
    if [ ! -f "$BACKEND_DIR/src/main/resources/application-test.yml" ]; then
        print_warning "测试配置文件 application-test.yml 不存在"
    else
        print_success "测试配置文件存在"
    fi
    
    print_success "后端测试环境配置完成"
    echo ""
}

setup_backend_prod() {
    print_header "配置后端生产环境"
    
    # 检查生产配置文件
    if [ ! -f "$BACKEND_DIR/src/main/resources/application-prod.yml" ]; then
        print_error "生产配置文件不存在: $BACKEND_DIR/src/main/resources/application-prod.yml"
        print_warning "请手动创建生产配置文件并设置以下关键参数:"
        echo ""
        echo "  spring.datasource.druid.url: 生产数据库地址"
        echo "  spring.datasource.druid.username: 数据库用户名"
        echo "  spring.datasource.druid.password: 数据库密码"
        echo "  spring.redis.host: Redis服务器地址"
        echo "  spring.rabbitmq.host: RabbitMQ服务器地址"
        echo "  linfeng.jwt.secret: JWT加密密钥（强随机字符串）"
        echo "  qiniu/腾讯云/阿里云: 相关密钥"
        echo ""
        return 1
    else
        print_success "生产配置文件存在"
    fi
    
    print_success "后端生产环境配置完成"
    echo ""
}

##############################################################################
# 配置前端环境
##############################################################################

setup_frontend() {
    print_header "配置前端环境"
    
    if [ ! -d "$FRONTEND_DIR" ]; then
        print_error "前端目录不存在: $FRONTEND_DIR"
        return 1
    fi
    
    print_info "前端项目路径: $FRONTEND_DIR"
    
    # 检查 package.json
    if [ ! -f "$FRONTEND_DIR/package.json" ]; then
        print_error "package.json 不存在"
        return 1
    fi
    
    print_success "前端项目结构检查完成"
    
    # 创建 API 配置文件
    if [ ! -f "$FRONTEND_DIR/src/config/api.config.js" ]; then
        print_info "创建 API 配置文件..."
        mkdir -p "$FRONTEND_DIR/src/config" 2>/dev/null || true
        cat > "$FRONTEND_DIR/src/config/api.config.js" << 'EOF'
// API 服务器配置
// 开发、测试、生产环境配置

const config = {
  dev: {
    baseURL: 'http://localhost:8080',
    timeout: 30000,
    requestInterceptor: true,
    responseInterceptor: true,
  },
  test: {
    baseURL: 'http://test.linfeng.tech:8080',
    timeout: 30000,
    requestInterceptor: true,
    responseInterceptor: true,
  },
  prod: {
    baseURL: 'https://api.linfeng.tech',
    timeout: 30000,
    requestInterceptor: true,
    responseInterceptor: true,
  }
};

// 当前环境（根据 NODE_ENV 判断）
const env = process.env.NODE_ENV || 'dev';
export default config[env] || config.dev;
EOF
        print_success "API 配置文件已创建"
    fi
    
    # 创建环境变量示例
    if [ ! -f "$FRONTEND_DIR/.env.example" ]; then
        print_info "创建环境变量示例文件..."
        cat > "$FRONTEND_DIR/.env.example" << 'EOF'
# 开发环境
VUE_APP_BASE_URL=http://localhost:8080
VUE_APP_API_TIMEOUT=30000

# 微信小程序配置
VUE_APP_WX_APPID=your_app_id

# 第三方服务配置
VUE_APP_FILE_UPLOAD_URL=http://localhost:8080/upload
VUE_APP_FILE_UPLOAD_TOKEN=your_token

# 环境标识
VUE_APP_ENV=development
EOF
        print_success "环境变量示例文件已创建"
    fi
    
    print_success "前端环境配置完成"
    echo ""
}

##############################################################################
# 数据库初始化
##############################################################################

setup_database() {
    print_header "数据库初始化说明"
    
    echo -e "${YELLOW}重要: 数据库脚本需要从QQ群文件获取${NC}"
    echo ""
    echo "数据库初始化步骤:"
    echo "1. 从QQ群 (624039130) 文件中获取 SQL 脚本"
    echo "2. 连接到 MySQL 数据库"
    echo "3. 创建数据库: CREATE DATABASE linfeng-love-open DEFAULT CHARSET utf8mb4;"
    echo "4. 执行 SQL 脚本初始化表结构和数据"
    echo ""
    echo "示例命令:"
    echo "  mysql -u root -p -e 'CREATE DATABASE linfeng-love-open DEFAULT CHARSET utf8mb4;'"
    echo "  mysql -u root -p linfeng-love-open < linfeng-love.sql"
    echo ""
    
    # 检查 MySQL 客户端
    if command -v mysql &> /dev/null; then
        print_success "MySQL 客户端已安装"
        
        # 尝试连接到本地 MySQL
        if mysql -u root -h localhost -e "SELECT 1" &>/dev/null; then
            print_success "可以连接到本地 MySQL"
        else
            print_warning "无法连接到本地 MySQL，请检查 MySQL 是否正在运行"
        fi
    else
        print_warning "MySQL 客户端未安装"
    fi
    
    echo ""
}

##############################################################################
# Redis 和 RabbitMQ 配置
##############################################################################

setup_services() {
    print_header "第三方服务配置"
    
    # Redis
    echo -e "${BLUE}Redis 配置:${NC}"
    if command -v redis-cli &> /dev/null; then
        if redis-cli -h localhost ping &>/dev/null; then
            print_success "Redis 已连接"
        else
            print_warning "无法连接到 Redis (localhost:6379)"
            echo "  启动 Redis: redis-server"
        fi
    else
        print_warning "Redis 未安装"
        echo "  安装 Redis: brew install redis (macOS) 或 apt-get install redis-server (Linux)"
        echo "  或者使用 Docker: docker run -d -p 6379:6379 redis:latest"
    fi
    echo ""
    
    # RabbitMQ
    echo -e "${BLUE}RabbitMQ 配置:${NC}"
    if curl -s -u guest:guest http://localhost:15672/api/aliveness-test &>/dev/null; then
        print_success "RabbitMQ 已连接"
    else
        print_warning "无法连接到 RabbitMQ (localhost:15672)"
        echo "  启动 RabbitMQ: rabbitmq-server"
        echo "  或者使用 Docker:"
        echo "    docker run -d --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:3-management"
    fi
    echo ""
    
    # MySQL
    echo -e "${BLUE}MySQL 配置:${NC}"
    if command -v mysql &> /dev/null; then
        if mysql -h localhost -u root -proot -e "SELECT 1" &>/dev/null; then
            print_success "MySQL 已连接"
        else
            print_warning "无法连接到 MySQL (localhost:3306)"
            echo "  请检查 MySQL 是否正在运行"
        fi
    fi
    echo ""
}

##############################################################################
# 第三方服务集成指南
##############################################################################

setup_third_party() {
    print_header "第三方服务集成指南"
    
    echo -e "${BLUE}微信小程序认证:${NC}"
    echo "  1. 访问: https://mp.weixin.qq.com"
    echo "  2. 获取 AppID 和 AppSecret"
    echo "  3. 配置在 application-*.yml 的 wx.ma 部分"
    echo ""
    
    echo -e "${BLUE}文件存储 (选一个配置):${NC}"
    echo ""
    echo "  方案1 - 七牛云:"
    echo "    - 访问: https://www.qiniu.com"
    echo "    - 获取: AccessKey, SecretKey, BucketName"
    echo "    - 配置: qiniu 部分"
    echo ""
    echo "  方案2 - 阿里云 OSS:"
    echo "    - 访问: https://oss.aliyun.com"
    echo "    - 获取: AccessKeyId, AccessKeySecret"
    echo "    - 配置: aliyun.oss 部分"
    echo ""
    echo "  方案3 - 腾讯云 COS:"
    echo "    - 访问: https://cloud.tencent.com/product/cos"
    echo "    - 获取: SecretId, SecretKey"
    echo "    - 配置: tencent.cos 部分"
    echo ""
    echo "  方案4 - MinIO (本地/自建):"
    echo "    - Docker: docker run -d -p 9000:9000 -p 9001:9001 minio/minio"
    echo "    - 配置: minio 部分"
    echo ""
    
    echo -e "${BLUE}腾讯云短信 (SMS):${NC}"
    echo "  - 访问: https://cloud.tencent.com/product/sms"
    echo "  - 获取: SecretId, SecretKey, SignName"
    echo "  - 配置: tencentcloud.sms 部分"
    echo ""
    
    echo -e "${BLUE}微信支付:${NC}"
    echo "  - 申请: https://pay.weixin.qq.com"
    echo "  - 获取: MerchantId, ApiKey, CertificatePath"
    echo "  - 配置: wx.pay 部分"
    echo ""
    
    echo -e "${BLUE}腾讯云人脸识别:${NC}"
    echo "  - 访问: https://cloud.tencent.com/product/cii"
    echo "  - 获取: SecretId, SecretKey"
    echo "  - 配置: tencentcloud.face 部分"
    echo ""
    
    echo -e "${BLUE}百度内容审核:${NC}"
    echo "  - 访问: https://ai.baidu.com"
    echo "  - 获取: ApiKey, SecretKey"
    echo "  - 配置: baidu.aip 部分"
    echo ""
}

##############################################################################
# Maven 依赖下载
##############################################################################

download_maven_dependencies() {
    print_header "下载 Maven 依赖"
    
    if [ ! -f "$BACKEND_DIR/pom.xml" ]; then
        print_error "pom.xml 不存在，跳过依赖下载"
        return 1
    fi
    
    if ! command -v mvn &> /dev/null; then
        print_error "Maven 未安装，无法下载依赖"
        return 1
    fi
    
    print_info "此步骤可能需要较长时间，请耐心等待..."
    print_info "如果网络较慢，建议配置 Maven 国内镜像"
    
    cd "$BACKEND_DIR"
    if mvn dependency:resolve -q; then
        print_success "Maven 依赖下载完成"
    else
        print_warning "Maven 依赖下载可能失败，请检查网络连接"
    fi
    cd "$SCRIPT_DIR"
    echo ""
}

##############################################################################
# NPM 依赖下载
##############################################################################

download_npm_dependencies() {
    print_header "下载 NPM 依赖"
    
    if [ ! -f "$FRONTEND_DIR/package.json" ]; then
        print_error "package.json 不存在，跳过依赖下载"
        return 1
    fi
    
    if ! command -v npm &> /dev/null && ! command -v yarn &> /dev/null; then
        print_error "npm/yarn 未安装，无法下载依赖"
        return 1
    fi
    
    cd "$FRONTEND_DIR"
    
    if command -v npm &> /dev/null; then
        print_info "使用 npm 安装依赖..."
        if npm install; then
            print_success "NPM 依赖安装完成"
        else
            print_warning "NPM 依赖安装失败"
        fi
    elif command -v yarn &> /dev/null; then
        print_info "使用 yarn 安装依赖..."
        if yarn install; then
            print_success "Yarn 依赖安装完成"
        else
            print_warning "Yarn 依赖安装失败"
        fi
    fi
    
    cd "$SCRIPT_DIR"
    echo ""
}

##############################################################################
# 输出完整的环境配置总结
##############################################################################

print_summary() {
    print_header "环境配置总结"
    
    cat << 'EOF'
┌──────────────────────────────────────────────────────────────────┐
│         林风婚恋交友系统 - 开发环境配置完成                        │
└──────────────────────────────────────────────────────────────────┘

【后端项目】
  位置: linfeng-love-backend-open
  框架: Spring Boot 2.2.4
  编译: mvn clean install
  运行: mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=dev"
  打包: mvn clean package

【前端项目】
  位置: linfeng-love-uniapps-open
  框架: UniApp + Vue 3
  编译小程序: npm run build:mp-weixin
  编译H5: npm run build:web
  编译APP: npm run build:app
  本地开发: npm run dev

【本地服务配置】
  MySQL:     localhost:3306 (user: root, password: root)
  Redis:     localhost:6379 (password: 无)
  RabbitMQ:  localhost:5672 (user: guest, password: guest)
  Nginx:     http://localhost (如已配置)

【API 访问】
  后端API:      http://localhost:8080
  Swagger文档:  http://localhost:8080/swagger-ui.html
  Druid监控:    http://localhost:8080/druid/

【关键配置文件】
  后端:
    - src/main/resources/application.yml (主配置)
    - src/main/resources/application-dev.yml (开发配置)
    - src/main/resources/application-local.yml (本地配置-可选)
  
  前端:
    - src/config/api.config.js (API配置)
    - .env.example (环境变量示例)

【下一步】
  1. □ 从 QQ 群 (624039130) 获取数据库 SQL 脚本
  2. □ 创建并初始化 MySQL 数据库
  3. □ 配置第三方服务密钥 (微信、文件存储、短信等)
  4. □ 启动 MySQL、Redis、RabbitMQ 服务
  5. □ 下载项目依赖: mvn install (后端) 和 npm install (前端)
  6. □ 启动后端: mvn spring-boot:run
  7. □ 启动前端: npm run dev
  8. □ 在浏览器打开: http://localhost:8080

【常见问题排查】
  - 数据库连接失败: 检查 MySQL 是否启动，用户名密码是否正确
  - Redis 连接失败: 检查 Redis 是否启动，application-dev.yml 中 redis.open 是否为 true
  - 依赖下载失败: 检查网络，考虑配置 Maven/NPM 国内镜像
  - 端口占用: 修改 application.yml 中 server.port

【调试建议】
  - IDE: IntelliJ IDEA (推荐) 或 Visual Studio Code
  - 后端调试: 在 IDE 中使用 Debug 模式运行
  - 前端调试: 浏览器开发者工具 (F12)
  - API 测试: Postman 或 Insomnia
  - 数据库: Navicat、DBeaver 或 MySQL Workbench

EOF
    echo ""
}

##############################################################################
# 主函数
##############################################################################

main() {
    local env=${1:-dev}
    
    # 验证环境参数
    case "$env" in
        dev|test|prod)
            ;;
        *)
            print_error "无效的环境参数: $env"
            echo "用法: $0 [dev|test|prod]"
            exit 1
            ;;
    esac
    
    clear
    echo -e "${BLUE}"
    cat << 'EOF'
╔════════════════════════════════════════════════════════════════╗
║     林风婚恋交友系统 - 环境配置脚本 v1.0                       ║
║     作者: linfeng-tech                                         ║
║     用途: 自动化配置开发/测试/生产环境                        ║
╚════════════════════════════════════════════════════════════════╝
EOF
    echo -e "${NC}"
    echo ""
    
    # 执行配置步骤
    check_system_requirements
    
    case "$env" in
        dev)
            setup_backend_dev
            setup_frontend
            setup_database
            setup_services
            setup_third_party
            
            # 询问是否下载依赖
            read -p "是否现在下载 Maven 依赖? (y/n) " -n 1 -r
            echo
            if [[ $REPLY =~ ^[Yy]$ ]]; then
                download_maven_dependencies
            fi
            
            read -p "是否现在下载 NPM 依赖? (y/n) " -n 1 -r
            echo
            if [[ $REPLY =~ ^[Yy]$ ]]; then
                download_npm_dependencies
            fi
            ;;
            
        test)
            setup_backend_test
            setup_frontend
            setup_database
            setup_services
            ;;
            
        prod)
            setup_backend_prod
            setup_frontend
            setup_third_party
            print_warning "生产环境需要手动配置，请仔细检查所有配置参数"
            ;;
    esac
    
    print_summary
    print_success "环境配置脚本执行完成！"
}

# 执行主函数
main "$@"
