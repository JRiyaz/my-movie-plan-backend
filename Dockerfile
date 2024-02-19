# Start with a base image containing Java runtime
FROM maven:3.8.6-openjdk-8-slim AS builder

WORKDIR /app

COPY . .

RUN mvn clean package -P prod -DskipTests

# Make port 8080 available to the world outside this container
# EXPOSE 8080

FROM openjdk:8-jre

COPY --from=builder /app/target/my-movie-plan.jar app.jar

ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=prod", "app.jar"]

# FROM tomcat:8.5.38
# COPY --from=builder /app/target/my-movie-plan.war app.jar
# ADD ./app.jar /usr/local/tomcat/webapps/
# RUN chmod +x /usr/local/tomcat/bin/catalina.sh
# CMD ["catalina.sh", "run"]
