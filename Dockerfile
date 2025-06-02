# Use official Java image
FROM openjdk:8-jdk-alpine

# Set working directory
WORKDIR /app

# Copy built jar
COPY target/taskmanager-0.0.1-SNAPSHOT.jar app.jar

# Expose port 8080
EXPOSE 8080

# Run the app
ENTRYPOINT ["java", "-jar", "app.jar"]
