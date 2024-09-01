# 1단계: 빌드 환경 설정 (빌드 JDK 사용)
FROM eclipse-temurin:21-jdk AS build

# 작업 디렉토리 설정
WORKDIR /app

# 프로젝트의 Gradle 또는 Maven 빌드 파일을 복사
COPY build.gradle settings.gradle* gradlew /app/
COPY gradle /app/gradle

# gradlew 파일에 실행 권한 부여
RUN chmod +x /app/gradlew

# 의존성 캐싱을 위해 의존성만 먼저 설치
RUN ./gradlew dependencies --no-daemon

# 애플리케이션의 소스 파일들을 복사
COPY src /app/src

# JAR 파일 빌드
RUN ./gradlew bootJar --no-daemon

# 2단계: 실행 환경 설정 (런타임 JRE 사용)
FROM eclipse-temurin:21-jre AS runtime

# 작업 디렉토리 설정
WORKDIR /app

# 빌드 단계에서 생성한 JAR 파일 복사
COPY --from=build /app/build/libs/*.jar app.jar

# 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
