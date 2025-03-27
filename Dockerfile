#FROM maven:3.9.8-eclipse-temurin-21 AS build
#
#WORKDIR /app
#
#COPY pom.xml .
#COPY src ./src
#
#RUN mvn clean package -DskipTests
#
#FROM eclipse-temurin:21.0.4_7-jre-alpine
#
#WORKDIR /app
#
#RUN mkdir -p /app/log
#
#COPY --from=build /app/target/ptgen.jar .
#
#EXPOSE 8081
## 配置容器启动命令
#CMD ["sh", "-c", "nohup java -jar ptgen.jar > /app/log/ptgen.log 2>&1 & tail -f /app/log/ptgen.log"]

# --------------------------下面是jar包docker部署方式,可以把上面的注释掉 使用下面的---------------------
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

RUN mkdir -p /app/log

COPY ./ptgen.jar .

EXPOSE 8081

# 设置启动命令
CMD ["sh", "-c", "nohup java -jar ptgen.jar > /app/log/ptgen.log 2>&1 & tail -f /app/log/ptgen.log"]
