# ---- Stage 1: Build the framework JAR ----
FROM eclipse-temurin:24-jdk AS builder

WORKDIR /build

COPY pom.xml .
COPY src ./src

# Build JAR — sqlite profile has no external DB dependency
RUN apt-get update && apt-get install -y maven > /dev/null 2>&1 && \
    mvn package -B -P sqlite -DskipTests && \
    mv target/mvc-framework-*.jar target/shock.jar

# ---- Stage 2: Runtime ----
FROM eclipse-temurin:24-jre

RUN groupadd -r app && useradd -r -g app -d /app -s /sbin/nologin app

WORKDIR /app

COPY --from=builder /build/target/shock.jar ./lib/shock.jar

# Configurable at runtime via -e or mounted config
ENV APP_PORT=8080
ENV DB_DRIVER=sqlite
ENV DB_URL=jdbc:sqlite:/app/data/app.db
ENV DB_POOL_SIZE=5

RUN mkdir -p /app/data /app/views /app/public && chown -R app:app /app

USER app

EXPOSE ${APP_PORT}

# Replace ENTRYPOINT with your app's main class:
#   java -cp "/app/lib/shock.jar:/*" com.company.Main
