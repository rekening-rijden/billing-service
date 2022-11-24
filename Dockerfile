FROM openjdk:17-jdk
MAINTAINER "Luc Janssen"
EXPOSE 8080
COPY target/billing-service-0.0.1-SNAPSHOT.jar api-0.0.1.jar
ENTRYPOINT ["java", "-jar", "api-0.0.1.jar"]
