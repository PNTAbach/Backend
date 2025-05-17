# Use a slim JDK base image
FROM openjdk:17-jdk-alpine

# Argument to pick up the built JAR
ARG JAR_FILE=target/*.jar

# Copy the JAR file into the image
COPY ${JAR_FILE} app.jar

# Expose port 8080
EXPOSE 8080

# Command to run the JAR
ENTRYPOINT ["java", "-jar", "/app.jar"]
