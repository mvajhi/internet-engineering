# First stage: build the application
ARG JDK_VERSION=21 MAVEN_VERSION=3.9.9-eclipse-temurin-21
FROM maven:${MAVEN_VERSION} AS build
COPY . /app
WORKDIR /app
RUN mvn package -DskipTests

# Second stage: create a slim image
FROM openjdk:${JDK_VERSION}
COPY --from=build /app/target/my-application.jar /app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]