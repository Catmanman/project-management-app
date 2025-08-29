# -------- Build stage --------
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

# Cache deps
COPY pom.xml ./
RUN mvn -q -DskipTests dependency:go-offline

# Build
COPY src ./src
RUN mvn -q -DskipTests package

# -------- Runtime stage --------
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copy fat jar (adjust if your jar name differs)
COPY --from=build /app/target/*.jar /app/app.jar

ENV JAVA_OPTS=""
EXPOSE 8080
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar"]
