FROM eclipse-temurin:21-alpine as build
WORKDIR /app

COPY ./mvnw .
COPY ./.mvn .mvn
COPY ./pom.xml .
COPY src src

RUN chmod +x ./mvnw
RUN ./mvnw install -DskipTests
FROM eclipse-temurin:21-alpine

ARG JAR_FILE=/app/target/*.jar
COPY --from=build ${JAR_FILE} ./app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
