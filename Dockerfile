# open-jdk-17을 사용하여 빌드를 실시
FROM openjdk:17-jdk-slim

# Docker 내 root directory /app으로 설정
WORKDIR /app

# 빌드 후 실행
COPY build/libs/*.jar app.jar

# 쉘 명령어
ENTRYPOINT ["java", "-jar", "app.jar"]