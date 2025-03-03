# open-jdk-21을 사용하여 빌드
FROM openjdk:21-jdk-slim

# Docker 내 root directory /app으로 설정
WORKDIR /app

# 타임존을 한국(KST)으로 설정
RUN apt-get update && apt-get install -y tzdata && \
    ln -fs /usr/share/zoneinfo/Asia/Seoul /etc/localtime && \
    dpkg-reconfigure --frontend noninteractive tzdata

# 빌드 후 실행
COPY build/libs/*.jar app.jar

# 쉘 명령어
ENTRYPOINT ["java", "-jar", "app.jar"]