# Start with a base image containing Java runtime
FROM openjdk:8-jdk-alpine

# Add a volume pointing to /tmp
VOLUME /tmp

# Make port 8080 available to the world outside this container
# EXPOSE 8080

# Add the application's jar to the container
ADD target/my-movie-plan.jar my-movie-plan.jar

# Run the jar file
ENTRYPOINT ["java", "-jar", "my-movie-plan.jar"]