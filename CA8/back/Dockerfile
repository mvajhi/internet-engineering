# First stage: build the application
ARG JDK_VERSION=21 MAVEN_VERSION=3.9.9-eclipse-temurin-21
FROM maven:${MAVEN_VERSION} AS build
WORKDIR /app
COPY ./pom.xml /app/pom.xml
RUN mvn dependency:go-offline -B
COPY . /app
RUN mvn package -DskipTests

# Second stage: create a slim image
FROM openjdk:${JDK_VERSION}
COPY --from=build /app/target/*.jar /app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]