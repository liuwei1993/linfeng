# 林风婚恋交友系统 - Dockerfile
# 用于构建应用容器镜像

# 构建阶段 (可选，用于减小最终镜像大小)
FROM maven:3.8-openjdk-8 AS builder

WORKDIR /build

# 复制项目文件
COPY linfeng-love-backend-open /build/

# 编译项目
RUN mvn clean package -DskipTests -q

# 运行阶段
FROM openjdk:8-jre-slim

LABEL maintainer="linfeng-tech" \
      description="Linfeng Love Application - Dating and Social Platform" \
      version="1.13.0"

WORKDIR /app

# 复制编译结果
COPY --from=builder /build/target/*.jar /app/linfeng-love.jar

# 创建必要的目录
RUN mkdir -p /app/logs /app/config

# 非 root 用户运行（安全最佳实践）
RUN useradd -m -u 1000 linfeng && chown -R linfeng:linfeng /app
USER linfeng

# 暴露端口
EXPOSE 8080

# 健康检查
HEALTHCHECK --interval=30s --timeout=10s --start-period=40s --retries=3 \
    CMD curl -f http://localhost:8080/swagger-ui.html || exit 1

# JVM 参数优化
ENV JAVA_OPTS="-Xms512m -Xmx2048m -XX:+UseG1GC -XX:MaxGCPauseMillis=200 -XX:+PrintGCDateStamps -XX:+PrintGCDetails -Xloggc:/app/logs/gc.log"

# 启动命令
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/linfeng-love.jar --spring.profiles.active=${SPRING_PROFILE:=prod}"]
