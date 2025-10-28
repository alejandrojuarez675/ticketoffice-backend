# Build stage
FROM gradle:8.4-jdk21 AS build
WORKDIR /home/gradle/project
COPY build.gradle settings.gradle ./
COPY src ./src

# Build the application
RUN gradle clean build --no-daemon

# Run stage
FROM eclipse-temurin:21-jre-jammy

# Set working directory
WORKDIR /app

# Copy the built JAR file from the build stage
COPY --from=build /home/gradle/project/build/libs/*.jar /app/app.jar

# Set environment variables
ENV JAVA_OPTS="-Xmx512m -Xms256m -Denvironment=prod"
ENV PORT=8080

# Expose the port the app runs on
EXPOSE $PORT

# Command to run the application
ENTRYPOINT exec java $JAVA_OPTS -Dserver.port=$PORT -jar /app/app.jar
