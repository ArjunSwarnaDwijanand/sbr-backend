# Use the official OpenJDK image as a base
FROM openjdk:17-jdk-slim

# Create a volume for temporary files
VOLUME /tmp

# Argument for the JAR file
ARG JAR_FILE=target/*.jar

# Copy the JAR file to the container
COPY ${JAR_FILE} app.jar

# Entry point to run the application
ENTRYPOINT ["java", "-jar", "/app.jar"]
