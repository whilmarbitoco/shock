# ---- Stage 1: Build the fat JAR ----
FROM eclipse-temurin:24-jdk AS builder

WORKDIR /build

COPY pom.xml .
COPY src ./src

# Install Maven and build a shaded (fat) JAR with all dependencies
# sqlite profile has no external JDBC dependency
RUN apt-get update && apt-get install -y maven > /dev/null 2>&1 && \
    mvn package -B -P sqlite -DskipTests

# ---- Stage 2: Runtime ----
FROM eclipse-temurin:24-jre

# Create non-root user
RUN groupadd -r app && useradd -r -g app -d /app -s /sbin/nologin app

WORKDIR /app

# Copy the fat JAR from builder
COPY --from=builder /build/target/shock.jar ./shock.jar

# Runtime configuration (override with docker run -e or docker-compose)
ENV SERVER_PORT=8080
ENV DB_URL=jdbc:sqlite:/app/data/app.db
ENV DB_POOL_SIZE=5

RUN mkdir -p /app/data && chown -R app:app /app

USER app

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/shock.jar"]
