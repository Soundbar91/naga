FROM eclipse-temurin:21-jdk-alpine AS builder

WORKDIR /workspace

COPY gradlew settings.gradle build.gradle ./
COPY gradle ./gradle
COPY common/build.gradle ./common/build.gradle
COPY commerce-api/build.gradle ./commerce-api/build.gradle
COPY pg-api/build.gradle ./pg-api/build.gradle

RUN chmod +x gradlew && ./gradlew :commerce-api:dependencies --no-daemon

COPY common/src ./common/src
COPY commerce-api/src ./commerce-api/src

RUN ./gradlew :commerce-api:bootJar --no-daemon && cp commerce-api/build/libs/commerce-api-*.jar /tmp/app.jar

FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

RUN addgroup -S spring && adduser -S spring -G spring

COPY --from=builder --chown=spring:spring /tmp/app.jar ./app.jar

USER spring:spring

ENTRYPOINT ["java", "-jar", "app.jar"]
