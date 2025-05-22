# Use a base image
FROM openjdk:17-jdk-slim

# Set work directory
WORKDIR /app

# Add the jar to the container
COPY target/PaymentProcessingSystem-0.0.1-SNAPSHOT.jar app.jar

# Run the application
ENTRYPOINT ["java","-jar","app.jar"]