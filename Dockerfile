FROM eclipse-temurin:21-jre-alpine AS build
WORKDIR /app
COPY build/libs/*.jar app.jar
CMD ["java", "-jar", "-XX:MaxRAMPercentage=75", "/app/app.jar"]
