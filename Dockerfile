# -----------------------------
# 1) 빌드 이미지 (Gradle Wrapper 사용)
# -----------------------------
FROM eclipse-temurin:17-jdk AS builder
WORKDIR /app

# Gradle wrapper 및 설정 파일들 먼저 복사
COPY build.gradle settings.gradle ./
COPY gradlew .
COPY gradle gradle

# 소스 코드 복사
COPY src src

# 부트 JAR 생성
RUN chmod +x gradlew
RUN ./gradlew bootJar

# -----------------------------
# 2) 실행용 이미지 (JRE만 포함 → 가볍게)
# -----------------------------
FROM eclipse-temurin:17-jre
WORKDIR /app

# builder에서 생성된 JAR 가져오기
COPY --from=builder /app/build/libs/*.jar app.jar

# 스프링부트 기본 포트
EXPOSE 8080

# 스프링부트 실행
ENTRYPOINT ["java", "-jar", "app.jar"]
