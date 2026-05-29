# Stage 1: Build the Application
FROM maven:3.9.6-eclipse-temurin-21-alpine AS builder
WORKDIR /app

# Copy the entire multi-module source code
COPY . .

# Build the project
RUN mvn clean package -DskipTests


# Extract the layers
FROM eclipse-temurin:21-jre-alpine AS extractor
WORKDIR /app

# Copy the fat JAR from the builder stage
COPY --from=builder /app/app/target/app.jar app.jar

RUN java -Djarmode=layertools -jar app.jar extract

# The Runtime
FROM eclipse-temurin:21-jre-alpine AS runtime
WORKDIR /app

RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

COPY --from=extractor /app/dependencies/ ./
COPY --from=extractor /app/spring-boot-loader/ ./
COPY --from=extractor /app/snapshot-dependencies/ ./
COPY --from=extractor /app/application/ ./

EXPOSE 8080

ENTRYPOINT ["java", \
            "-XX:MaxRAMPercentage=75.0", \
            "-XX:InitialRAMPercentage=50.0", \
            "-XX:+UseZGC", \
            "-Djava.security.egd=file:/dev/./urandom", \
            "org.springframework.boot.loader.launch.JarLauncher"]