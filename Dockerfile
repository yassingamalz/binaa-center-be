# Use an OpenJDK image
FROM openjdk:17-jdk-slim as build

# Set working directory
WORKDIR /app

# Copy the built JAR file
COPY target/binaa-center-0.0.1-SNAPSHOT.jar app.jar

# Expose port 8080
EXPOSE 8080

# Run the application
CMD ["java", "-jar", "app.jar"]
