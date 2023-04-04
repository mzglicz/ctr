FROM eclipse-temurin:17-jdk-alpine as build
WORKDIR /workspace/app

COPY ../gradlew .
COPY ../gradle gradle
COPY ../build.gradle .
COPY ../settings.gradle .
COPY ../src src

RUN ./gradlew bootJar -x test

FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
VOLUME /tmp
ARG DEPENDENCY=/workspace/app
COPY --from=build ${DEPENDENCY}/build/libs/*.jar /app/app.jar
ENTRYPOINT ["java","-jar","app.jar"]