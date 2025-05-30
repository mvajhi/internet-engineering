# First stage: build the application
ARG JDK_VERSION=25-jdk-nanoserver-ltsc2022 MAVEN_VERSION=11
FROM maven:${MAVEN_VERSION} AS build
COPY . /app
WORKDIR /app
RUN mvn package -DskipTests

# Second stage: create a slim image
FROM openjdk:${JDK_VERSION}
COPY --from=build /app/target/my-application.jar /app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]