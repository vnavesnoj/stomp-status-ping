FROM gradle:8.11.1-jdk21 AS build
WORKDIR /app
COPY build.gradle settings.gradle /app/
COPY src /app/src
RUN gradle clean --no-daemon
RUN gradle bootJar --no-daemon

FROM eclipse-temurin:21-jdk-jammy
WORKDIR /app
VOLUME /tmp
COPY --from=build /app/build/libs/*.jar /app/app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar", "--server.address=0.0.0.0"]
