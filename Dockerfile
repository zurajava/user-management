# Use an official OpenJDK 17 runtime as a parent image
FROM openjdk:17-jdk-slim-buster
# Set the working directory inside the container
WORKDIR /app

# Copy the packaged JAR file into the container at the working directory
COPY build/libs/user-management-0.0.1-SNAPSHOT.jar app.jar

# Make port 8080 available to the world outside this container
EXPOSE 8088

# Define environment variables
ENV SPRING_PROFILES_ACTIVE=production

# Run the JAR file when the container launches
CMD ["java", "-jar", "app.jar"]
