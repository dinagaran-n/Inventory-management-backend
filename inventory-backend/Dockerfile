# Build stage (uses Maven directly)
FROM maven:3.9.6-eclipse-temurin-17 AS build

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn -DskipTests package

# Run stage
FROM eclipse-temurin:17-jre

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar
COPY inventory.txt /app/inventory.txt

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
