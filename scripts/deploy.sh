#!/bin/bash

##############################################################################
#  林风婚恋交友系统 - 部署脚本
#  版本: 1.0
#  用途: 自动化构建和部署应用
#  使用: chmod +x deploy.sh && ./deploy.sh [build|deploy|start|stop|status] [dev|test|prod]
##############################################################################

set -e

# 颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m'

# 配置参数
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$SCRIPT_DIR"
BACKEND_DIR="$PROJECT_ROOT/linfeng-love-backend-open"
FRONTEND_DIR="$PROJECT_ROOT/linfeng-love-uniapps-open"
BUILD_DIR="$PROJECT_ROOT/build"
DEPLOY_DIR="/opt/linfeng-love"
JAR_FILE="linfeng-love.jar"
LOG_DIR="$DEPLOY_DIR/logs"
PID_FILE="$DEPLOY_DIR/linfeng-love.pid"
JAVA_OPTS="-Xms512m -Xmx2048m -XX:+UseG1GC -XX:MaxGCPauseMillis=200"

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

##############################################################################
# 编译后端
##############################################################################

build_backend() {
    print_header "编译后端项目"
    
    if [ ! -d "$BACKEND_DIR" ]; then
        print_error "后端目录不存在: $BACKEND_DIR"
        return 1
    fi
    
    if [ ! -f "$BACKEND_DIR/pom.xml" ]; then
        print_error "pom.xml 不存在"
        return 1
    fi
    
    if ! command -v mvn &> /dev/null; then
        print_error "Maven 未安装"
        return 1
    fi
    
    local env=${1:-dev}
    print_info "环境: $env"
    
    cd "$BACKEND_DIR"
    
    print_info "清理旧构建文件..."
    mvn clean -q || true
    
    print_info "编译并打包..."
    mvn package -DskipTests -P $env -q
    
    # 查找生成的 JAR 文件
    local jar_path=$(find "$BACKEND_DIR/target" -name "*.jar" -type f | head -n 1)
    
    if [ -f "$jar_path" ]; then
        mkdir -p "$BUILD_DIR"
        cp "$jar_path" "$BUILD_DIR/$JAR_FILE"
        print_success "后端编译完成: $BUILD_DIR/$JAR_FILE"
        print_info "JAR 文件大小: $(du -h $BUILD_DIR/$JAR_FILE | cut -f1)"
    else
        print_error "未找到生成的 JAR 文件"
        return 1
    fi
    
    cd "$SCRIPT_DIR"
}

##############################################################################
# 编译前端
##############################################################################

build_frontend() {
    print_header "编译前端项目"
    
    if [ ! -d "$FRONTEND_DIR" ]; then
        print_error "前端目录不存在: $FRONTEND_DIR"
        return 1
    fi
    
    if [ ! -f "$FRONTEND_DIR/package.json" ]; then
        print_error "package.json 不存在"
        return 1
    fi
    
    local target=${1:-web}  # web, mp-weixin, app
    print_info "构建目标: $target"
    
    cd "$FRONTEND_DIR"
    
    if ! command -v npm &> /dev/null && ! command -v yarn &> /dev/null; then
        print_error "npm/yarn 未安装"
        return 1
    fi
    
    # 检查依赖是否已安装
    if [ ! -d "node_modules" ]; then
        print_info "安装 NPM 依赖..."
        if command -v npm &> /dev/null; then
            npm install -q
        else
            yarn install -q
        fi
    fi
    
    print_info "构建前端应用..."
    
    case "$target" in
        web)
            npm run build:web -s
            local output_dir="dist"
            ;;
        mp-weixin)
            npm run build:mp-weixin -s
            local output_dir="dist/build/mp-weixin"
            ;;
        app)
            print_warning "APP 构建需要使用 HBuilderX 或 uni-app CLI"
            npm run build:app -s 2>/dev/null || true
            return 1
            ;;
        *)
            print_error "未知的构建目标: $target"
            return 1
            ;;
    esac
    
    if [ -d "$output_dir" ]; then
        mkdir -p "$BUILD_DIR/frontend"
        cp -r "$output_dir" "$BUILD_DIR/frontend/$target"
        print_success "前端编译完成: $BUILD_DIR/frontend/$target"
    else
        print_warning "前端输出目录未找到"
    fi
    
    cd "$SCRIPT_DIR"
}

##############################################################################
# 部署应用
##############################################################################

deploy_application() {
    print_header "部署应用"
    
    # 检查 JAR 文件
    if [ ! -f "$BUILD_DIR/$JAR_FILE" ]; then
        print_error "JAR 文件不存在，请先执行编译: ./deploy.sh build [env]"
        return 1
    fi
    
    # 创建部署目录
    if [ ! -d "$DEPLOY_DIR" ]; then
        print_info "创建部署目录: $DEPLOY_DIR"
        sudo mkdir -p "$DEPLOY_DIR"
        sudo mkdir -p "$LOG_DIR"
        sudo chown -R $(whoami):$(whoami) "$DEPLOY_DIR"
    fi
    
    # 备份现有 JAR 文件
    if [ -f "$DEPLOY_DIR/$JAR_FILE" ]; then
        local backup_name="${JAR_FILE%.jar}-$(date +%Y%m%d-%H%M%S).jar"
        print_info "备份现有版本: $backup_name"
        mv "$DEPLOY_DIR/$JAR_FILE" "$DEPLOY_DIR/$backup_name"
    fi
    
    # 复制新 JAR 文件
    print_info "复制 JAR 文件到部署目录..."
    cp "$BUILD_DIR/$JAR_FILE" "$DEPLOY_DIR/$JAR_FILE"
    
    # 创建启动脚本
    create_startup_script
    
    print_success "应用部署完成"
    print_info "部署目录: $DEPLOY_DIR"
    print_info "日志目录: $LOG_DIR"
    echo ""
}

##############################################################################
# 创建启动脚本
##############################################################################

create_startup_script() {
    local startup_script="$DEPLOY_DIR/startup.sh"
    
    cat > "$startup_script" << STARTEOF
#!/bin/bash

# 林风婚恋交友系统 - 启动脚本
# 自动生成，请勿手动编辑

PID_FILE="$PID_FILE"
JAR_FILE="$DEPLOY_DIR/$JAR_FILE"
LOG_FILE="$LOG_DIR/linfeng-love-\$(date +%Y%m%d).log"
JAVA_OPTS="$JAVA_OPTS"

# 启动应用
echo "启动林风婚恋交友应用..."
java \$JAVA_OPTS -jar "\$JAR_FILE" >> "\$LOG_FILE" 2>&1 &

# 保存进程ID
echo \$! > "\$PID_FILE"

echo "应用已启动 (PID: \$(cat \$PID_FILE))"
echo "日志文件: \$LOG_FILE"
STARTEOF

    chmod +x "$startup_script"
    print_success "启动脚本已创建: $startup_script"
}

##############################################################################
# 启动应用
##############################################################################

start_application() {
    print_header "启动应用"
    
    # 检查是否已运行
    if [ -f "$PID_FILE" ] && kill -0 $(cat "$PID_FILE") 2>/dev/null; then
        print_warning "应用已在运行 (PID: $(cat $PID_FILE))"
        return 0
    fi
    
    # 检查 JAR 文件
    if [ ! -f "$DEPLOY_DIR/$JAR_FILE" ]; then
        print_error "JAR 文件不存在: $DEPLOY_DIR/$JAR_FILE"
        return 1
    fi
    
    # 检查日志目录
    mkdir -p "$LOG_DIR"
    
    # 启动应用
    print_info "启动应用..."
    
    local log_file="$LOG_DIR/linfeng-love-$(date +%Y%m%d).log"
    
    nohup java $JAVA_OPTS -jar "$DEPLOY_DIR/$JAR_FILE" \
        --spring.profiles.active=$(get_active_profile) \
        >> "$log_file" 2>&1 &
    
    local pid=$!
    echo $pid > "$PID_FILE"
    
    # 等待应用启动
    sleep 3
    
    # 检查应用是否成功启动
    if kill -0 $pid 2>/dev/null; then
        print_success "应用启动成功 (PID: $pid)"
        print_info "日志文件: $log_file"
        
        # 显示最近的日志
        echo ""
        print_info "最近日志 (最后 20 行):"
        echo "---"
        tail -20 "$log_file"
        echo "---"
    else
        print_error "应用启动失败，请检查日志"
        cat "$log_file"
        return 1
    fi
}

##############################################################################
# 停止应用
##############################################################################

stop_application() {
    print_header "停止应用"
    
    if [ ! -f "$PID_FILE" ]; then
        print_warning "应用未运行 (PID 文件不存在)"
        return 0
    fi
    
    local pid=$(cat "$PID_FILE")
    
    if ! kill -0 $pid 2>/dev/null; then
        print_warning "应用已停止 (PID: $pid)"
        rm "$PID_FILE"
        return 0
    fi
    
    print_info "停止应用 (PID: $pid)..."
    
    # 首先尝试优雅关闭
    kill -TERM $pid
    
    # 等待最多 30 秒
    local count=0
    while kill -0 $pid 2>/dev/null && [ $count -lt 30 ]; do
        sleep 1
        count=$((count + 1))
    done
    
    # 如果仍未停止，强制杀死
    if kill -0 $pid 2>/dev/null; then
        print_warning "应用未及时停止，强制杀死..."
        kill -9 $pid
    fi
    
    rm -f "$PID_FILE"
    print_success "应用已停止"
}

##############################################################################
# 查看应用状态
##############################################################################

status_application() {
    print_header "应用状态"
    
    if [ ! -f "$PID_FILE" ]; then
        print_warning "应用未运行"
        return 1
    fi
    
    local pid=$(cat "$PID_FILE")
    
    if kill -0 $pid 2>/dev/null; then
        print_success "应用运行中 (PID: $pid)"
        
        # 显示内存使用情况
        if command -v ps &> /dev/null; then
            echo ""
            print_info "进程信息:"
            ps aux | grep $pid | grep -v grep || true
        fi
        
        # 显示最近日志
        local latest_log=$(find "$LOG_DIR" -name "*.log" -type f -printf '%T@ %p\n' | sort -rn | head -1 | cut -d' ' -f2-)
        if [ -f "$latest_log" ]; then
            echo ""
            print_info "最近日志文件: $latest_log"
            echo "---"
            tail -30 "$latest_log"
            echo "---"
        fi
    else
        print_error "应用未运行 (PID: $pid)"
        rm -f "$PID_FILE"
        return 1
    fi
}

##############################################################################
# 查看日志
##############################################################################

view_logs() {
    print_header "应用日志"
    
    local latest_log=$(find "$LOG_DIR" -name "*.log" -type f -printf '%T@ %p\n' 2>/dev/null | sort -rn | head -1 | cut -d' ' -f2-)
    
    if [ -z "$latest_log" ] || [ ! -f "$latest_log" ]; then
        print_warning "未找到日志文件"
        return 1
    fi
    
    print_info "日志文件: $latest_log"
    print_info "使用 'tail -f' 实时查看日志..."
    echo ""
    
    tail -f "$latest_log"
}

##############################################################################
# 创建 Systemd 服务单元
##############################################################################

create_systemd_service() {
    print_header "创建 Systemd 服务单元"
    
    local service_file="/etc/systemd/system/linfeng-love.service"
    
    if [ -f "$service_file" ]; then
        print_warning "服务单元已存在，跳过创建"
        return 0
    fi
    
    print_info "此操作需要 sudo 权限"
    
    sudo tee "$service_file" > /dev/null << SERVICEEOF
[Unit]
Description=Linfeng Love Application
After=network.target
Wants=network-online.target

[Service]
Type=simple
User=$(whoami)
WorkingDirectory=$DEPLOY_DIR
Environment="JAVA_OPTS=$JAVA_OPTS"
ExecStart=/usr/bin/java \$JAVA_OPTS -jar $DEPLOY_DIR/$JAR_FILE
Restart=on-failure
RestartSec=10s
StandardOutput=journal
StandardError=journal

[Install]
WantedBy=multi-user.target
SERVICEEOF

    sudo systemctl daemon-reload
    sudo systemctl enable linfeng-love.service
    
    print_success "Systemd 服务单元已创建"
    print_info "启动服务: sudo systemctl start linfeng-love"
    print_info "停止服务: sudo systemctl stop linfeng-love"
    print_info "查看状态: sudo systemctl status linfeng-love"
    print_info "查看日志: sudo journalctl -u linfeng-love -f"
}

##############################################################################
# 创建 Docker 镜像
##############################################################################

create_docker_image() {
    print_header "创建 Docker 镜像"
    
    # 检查 Docker
    if ! command -v docker &> /dev/null; then
        print_error "Docker 未安装"
        return 1
    fi
    
    # 检查 JAR 文件
    if [ ! -f "$BUILD_DIR/$JAR_FILE" ]; then
        print_error "JAR 文件不存在，请先执行编译"
        return 1
    fi
    
    # 创建 Dockerfile
    local dockerfile="$BUILD_DIR/Dockerfile"
    cat > "$dockerfile" << 'DOCKEREOF'
FROM openjdk:8-jre-slim

LABEL maintainer="linfeng-tech" \
      description="Linfeng Love Application - Dating and Social Platform"

WORKDIR /app

# 复制 JAR 文件
COPY linfeng-love.jar /app/

# 创建日志目录
RUN mkdir -p /app/logs

# 暴露端口
EXPOSE 8080

# 启动命令
ENTRYPOINT ["java", "-Xms512m", "-Xmx2048m", "-XX:+UseG1GC", "-XX:MaxGCPauseMillis=200", "-jar", "linfeng-love.jar"]
DOCKEREOF

    print_success "Dockerfile 已创建: $dockerfile"
    
    # 构建镜像
    print_info "构建 Docker 镜像..."
    local image_name="linfeng-love:latest"
    
    cd "$BUILD_DIR"
    if docker build -t "$image_name" .; then
        print_success "Docker 镜像构建成功: $image_name"
        print_info "运行容器: docker run -d -p 8080:8080 --name linfeng-love $image_name"
    else
        print_error "Docker 镜像构建失败"
        return 1
    fi
    cd "$SCRIPT_DIR"
}

##############################################################################
# 创建 Docker Compose 配置
##############################################################################

create_docker_compose() {
    print_header "创建 Docker Compose 配置"
    
    local docker_compose="$BUILD_DIR/docker-compose.yml"
    
    cat > "$docker_compose" << 'COMPOSEEOF'
version: '3.8'

services:
  linfeng-love:
    build:
      context: .
      dockerfile: Dockerfile
    container_name: linfeng-love
    ports:
      - "8080:8080"
    environment:
      - SPRING_PROFILES_ACTIVE=prod
      - SPRING_DATASOURCE_URL=jdbc:mysql://mysql:3306/linfeng-love-open
      - SPRING_DATASOURCE_USERNAME=root
      - SPRING_DATASOURCE_PASSWORD=root
      - SPRING_REDIS_HOST=redis
      - SPRING_REDIS_PORT=6379
      - SPRING_RABBITMQ_HOST=rabbitmq
      - SPRING_RABBITMQ_PORT=5672
    depends_on:
      - mysql
      - redis
      - rabbitmq
    networks:
      - linfeng-network
    restart: unless-stopped
    volumes:
      - ./logs:/app/logs

  mysql:
    image: mysql:8.0
    container_name: linfeng-mysql
    environment:
      - MYSQL_ROOT_PASSWORD=root
      - MYSQL_DATABASE=linfeng-love-open
      - MYSQL_CHARSET=utf8mb4
      - MYSQL_COLLATION=utf8mb4_unicode_ci
    ports:
      - "3306:3306"
    networks:
      - linfeng-network
    restart: unless-stopped
    volumes:
      - mysql-data:/var/lib/mysql

  redis:
    image: redis:7-alpine
    container_name: linfeng-redis
    ports:
      - "6379:6379"
    networks:
      - linfeng-network
    restart: unless-stopped
    volumes:
      - redis-data:/data

  rabbitmq:
    image: rabbitmq:3.12-management
    container_name: linfeng-rabbitmq
    environment:
      - RABBITMQ_DEFAULT_USER=guest
      - RABBITMQ_DEFAULT_PASS=guest
    ports:
      - "5672:5672"
      - "15672:15672"
    networks:
      - linfeng-network
    restart: unless-stopped
    volumes:
      - rabbitmq-data:/var/lib/rabbitmq

networks:
  linfeng-network:
    driver: bridge

volumes:
  mysql-data:
  redis-data:
  rabbitmq-data:
COMPOSEEOF

    print_success "Docker Compose 配置已创建: $docker_compose"
    print_info "启动全栈应用: cd $BUILD_DIR && docker-compose up -d"
    print_info "停止应用: docker-compose down"
}

##############################################################################
# 获取活跃配置
##############################################################################

get_active_profile() {
    if [ -f "$DEPLOY_DIR/application.properties" ]; then
        grep "spring.profiles.active" "$DEPLOY_DIR/application.properties" | cut -d'=' -f2
    else
        echo "prod"
    fi
}

##############################################################################
# 显示帮助
##############################################################################

show_help() {
    cat << 'EOF'
林风婚恋交友系统 - 部署脚本 v1.0

用法: ./deploy.sh <命令> [参数]

命令:
  build [env]          编译项目
                       env: dev, test, prod (默认: dev)
  
  build:backend [env]  仅编译后端
                       env: dev, test, prod (默认: dev)
  
  build:frontend [tgt] 仅编译前端
                       tgt: web, mp-weixin, app (默认: web)
  
  deploy [env]         部署应用
                       env: dev, test, prod (默认: prod)
  
  start                启动应用
  stop                 停止应用
  restart              重启应用
  status               查看应用状态
  logs                 查看实时日志
  
  docker:build         创建 Docker 镜像
  docker:compose       创建 Docker Compose 配置
  systemd:create       创建 Systemd 服务单元

示例:
  # 编译开发环境
  ./deploy.sh build dev
  
  # 部署生产环境
  ./deploy.sh deploy prod && ./deploy.sh start
  
  # 启动应用并查看日志
  ./deploy.sh start && ./deploy.sh logs
  
  # 创建 Docker 容器
  ./deploy.sh docker:build && docker-compose up -d

EOF
}

##############################################################################
# 主函数
##############################################################################

main() {
    local command=${1:-help}
    local param1=${2:-}
    local param2=${3:-}
    
    case "$command" in
        build)
            build_backend ${param1:-dev}
            build_frontend web
            ;;
        
        build:backend)
            build_backend ${param1:-dev}
            ;;
        
        build:frontend)
            build_frontend ${param1:-web}
            ;;
        
        deploy)
            build_backend ${param1:-dev}
            deploy_application
            ;;
        
        start)
            start_application
            ;;
        
        stop)
            stop_application
            ;;
        
        restart)
            stop_application
            sleep 2
            start_application
            ;;
        
        status)
            status_application
            ;;
        
        logs)
            view_logs
            ;;
        
        docker:build)
            build_backend prod
            create_docker_image
            ;;
        
        docker:compose)
            build_backend prod
            create_docker_compose
            ;;
        
        systemd:create)
            create_systemd_service
            ;;
        
        help|--help|-h)
            show_help
            ;;
        
        *)
            print_error "未知的命令: $command"
            echo "使用 './deploy.sh help' 查看帮助"
            exit 1
            ;;
    esac
}

# 执行主函数
main "$@"
